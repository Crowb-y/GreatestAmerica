import processing.core.PImage;

import java.util.List;

abstract public class ActiveEntity extends Entity {

    private final int actionPeriod;

    public ActiveEntity(String id, Point position, List<PImage> images, int index, int actionPeriod) {
        super(id, position, images, index);
        this.actionPeriod = actionPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                actionPeriod);
    }

    abstract public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);

    public Action createActivityAction(
            WorldModel world, ImageStore imageStore)
    {
        return new Activity (this, world, imageStore);
    }

    protected int getActionPeriod() {
        return actionPeriod;
    }
}
