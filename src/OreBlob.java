import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class OreBlob implements Animated {

    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;

    private final String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;
    private final int animationPeriod;


    public OreBlob(String id, Point position, int imageIndex, int actionPeriod, ImageStore imageStore) {
        this.id = id + BLOB_ID_SUFFIX;
        this.position = position;
        this.imageIndex = imageIndex;
        this.actionPeriod = actionPeriod / BLOB_PERIOD_SCALE;
        this.animationPeriod = BLOB_ANIMATION_MIN + new Random().nextInt(
                BLOB_ANIMATION_MAX
                        - BLOB_ANIMATION_MIN);
        this.images = imageStore.getImageList(BLOB_KEY);
    }


    public String getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }

    @Override
    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }

    @Override
    public PImage getCurrentImage() {
        return images.get(imageIndex);
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public List<PImage> getImages() {
        return images;
    }

    public void setImages(List<PImage> images) {
        this.images = images;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    @Override
    public int getAnimationPeriod() {
        return imageIndex;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                createAnimationAction(0),
                getAnimationPeriod());
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> blobTarget =
                world.findNearest(position, Vein.class);
        long nextPeriod = actionPeriod;

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveToOreBlob( world, blobTarget.get(), scheduler)) {
                Quake quake = new Quake(blobTarget.get().getPosition(), images);
                world.addEntity(quake);
                nextPeriod += actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                nextPeriod);
    }

    @Override
    public Action createActivityAction(
            WorldModel world, ImageStore imageStore)
    {
        return new Activity (this, world, imageStore, 0);
    }

    public boolean moveToOreBlob(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (position.adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPositionOreBlob(world, target.getPosition());

            if (!position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public Point nextPositionOreBlob(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - position.x);
        Point newPos = new Point(position.x + horiz, position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass()
                == Ore.class)))
        {
            int vert = Integer.signum(destPos.y - position.y);
            newPos = new Point(position.x, position.y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass()
                    == Ore.class)))
            {
                newPos = position;
            }
        }

        return newPos;
    }
}
