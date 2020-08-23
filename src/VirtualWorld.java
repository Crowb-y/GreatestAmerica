import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;
import processing.core.*;

public final class VirtualWorld extends PApplet
{
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String LOAD_FILE_NAME = "world.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;
    private long nextTime;

    public ImageStore getImageStore() {return imageStore;}
    public WorldModel getWorld() {return world;}
    public WorldView getView() {return view;}
    public EventScheduler getScheduler() {return scheduler;}
    public long getNextTime() {return nextTime;}

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }
        if (MovingEntity.checkWinCondition())
            System.out.println("winner winner");
        view.drawViewport();
    }

    public Point mousePosition(){

        int x_num = mouseX / 32;
        int y_num = mouseY / 32;
        return new Point(x_num,y_num);
    }

    //mouse pressed Event
    public void mouseClicked(){
        try {
            Point p = mousePosition();
            Point jailPos = view.getViewPort().viewportToWorld(p.getX(), p.getY());
            Point trumpPos = view.getViewPort().viewportToWorld(p.getX() + 4, p.getY());
            createFilth(jailPos);
            constructWalls(jailPos);
            spawnTrumps(trumpPos, jailPos);
        }
        catch(Exception e) {
            System.out.println(e.getMessage() + "\nCan't place Wall this close to edge");
        }
    }

    public void spawnTrumps(Point pos, Point jailPos){
        Optional<Entity> occupant = world.getOccupant(pos);
        if (occupant.isPresent())
            world.removeEntity(occupant.get());
        Donald donald = Donald.createDonald("trump", pos, imageStore.getImageList("rump"),
                jailPos, imageStore.getImageList("quake"));
        Melania melania = Melania.createMelania("trump", pos.translate(new Point(0, -1)), imageStore.getImageList("melon"));
        world.addEntity(donald);
        world.addEntity(melania);
        donald.scheduleActions(scheduler, world, imageStore);
        melania.scheduleActions(scheduler, world, imageStore);
    }

    public void constructWalls(Point pos) {
        int i = -3;
        while (i < 4) {
            Point top = new Point(pos.getX() + i, pos.getY() + 3);
            Point bottom = new Point(pos.getX() + i, pos.getY() - 3);
            Optional<Entity> topEntity = world.getOccupant(top);
            Optional<Entity> botEntity = world.getOccupant(bottom);
            if (topEntity.isPresent())
                world.removeEntity(topEntity.get());
            if (botEntity.isPresent())
                world.removeEntity(botEntity.get());
            Wall topWall = Wall.createWall("wall", top, imageStore.getImageList("wall"));
            world.addEntity(topWall);
            Wall botWall = Wall.createWall("wall", bottom, imageStore.getImageList("wall"));
            world.addEntity(botWall);
            i++;
        }
        int j = -2;
        while (j < 3) {
            Point left = new Point(pos.getX() - 3, pos.getY() + j);
            Point right = new Point(pos.getX() + 3, pos.getY() + j);
            Optional<Entity> leftEntity = world.getOccupant(left);
            Optional<Entity> rightEntity = world.getOccupant(right);
            if (leftEntity.isPresent())
                world.removeEntity(leftEntity.get());
            if (rightEntity.isPresent())
                world.removeEntity(rightEntity.get());
            Wall leftWall = Wall.createWall("wall", left, imageStore.getImageList("wall"));
            world.addEntity(leftWall);
            Wall rightWall = Wall.createWall("wall", right, imageStore.getImageList("wall"));
            world.addEntity(rightWall);
            j++;
        }
    }

    public void createFilth(Point pos) {
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                Background mud = new Background("mud", imageStore.getImageList("mud"));
                Point tile = new Point(pos.getX() + i, pos.getY() + j);
                Optional<Entity> occupant = world.getOccupant(tile);
                if (occupant.isPresent() &&
                        ((occupant.get().getClass() == Obstacle.class) || (occupant.get().getClass() == Wall.class)))
                    world.removeEntity(occupant.get());
                if (occupant.isPresent() && MovingEntity.class.isInstance(occupant.get()))
                    ((MovingEntity) occupant.get()).setCaptured();
                world.setBackgroundCell(tile, mud);
            }
        }
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            ImageStore.loadImages(in, imageStore, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof ActiveEntity) {
                ((ActiveEntity) entity).scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    public static void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
