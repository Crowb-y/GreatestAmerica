import processing.core.PImage;

import java.util.List;

public class Ore extends ActiveEntity {

    private static final String ORE_KEY = "ore";
    private static final String ORE_ID_PREFIX = "ore -- ";

    public Ore(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, 0, actionPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point pos = super.getPosition();

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        OreBlob blob = OreBlob.createOreBlob(super.getId(), pos, super.getImageIndex(),
                super.getActionPeriod(), imageStore);

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }

}
