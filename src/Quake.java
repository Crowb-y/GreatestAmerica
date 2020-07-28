import processing.core.PImage;

import java.util.List;

public class Quake implements Animated {

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    private final String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int animationPeriod;

    public Quake (Point position, List<PImage> images)
    {
        this.id = QUAKE_ID;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        animationPeriod = QUAKE_ANIMATION_PERIOD;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                QUAKE_ACTION_PERIOD);
        scheduler.scheduleEvent(this, createAnimationAction(
                QUAKE_ANIMATION_REPEAT_COUNT),
                QUAKE_ANIMATION_PERIOD);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
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

    @Override
    public void setPosition(Point pos) {
        position = pos;
    }

    @Override
    public int getAnimationPeriod() {
        return animationPeriod;
    }
}
