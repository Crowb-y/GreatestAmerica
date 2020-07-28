import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;


public class Vein implements Active {

    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;

    private final String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;

    public Vein(String id, Point position, List<PImage> images, int imageIndex, int actionPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = imageIndex;
        this.actionPeriod = actionPeriod;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                this.actionPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(position);

        if (openPt.isPresent()) {
            Ore ore = new Ore(id, openPt.get(), imageStore, (ORE_CORRUPT_MIN + new Random().nextInt(
                    ORE_CORRUPT_MAX - ORE_CORRUPT_MIN)));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                actionPeriod);
    }

    @Override
    public Action createActivityAction(
            WorldModel world, ImageStore imageStore)
    {
        return new Activity (this, world, imageStore, 0);
    }

    @Override
    public Point getPosition() {
        return position;
    }

    public void setPosition(Point pos) {this.position = pos;}

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
}
