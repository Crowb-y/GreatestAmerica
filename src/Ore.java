import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Ore implements Active {

    private static final String ORE_KEY = "ore";
    private static final String ORE_ID_PREFIX = "ore -- ";

    private final String id;
    private Point position;
    private List<PImage> images;
    private int actionPeriod;
    private int imageIndex;

    public Ore(String id, Point position, ImageStore imageStore, int actionPeriod) {
        this.id = ORE_ID_PREFIX + id;
        this.position = position;
        this.actionPeriod =  actionPeriod;
        this.images = imageStore.getImageList(ORE_KEY);
        imageIndex = 0;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                actionPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point pos = position;

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        OreBlob blob = new OreBlob(this.id, pos, imageIndex,
                actionPeriod, imageStore);

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }

    public void setPosition(Point pos) {this.position = pos;}

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
