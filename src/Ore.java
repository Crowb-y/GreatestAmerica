import processing.core.PImage;

import java.util.List;

public class Ore extends ActiveEntity {

    private static final String ORE_ID_PREFIX = "ore -- ";

    public Ore(String id, Point position, List<PImage> images, int index, int actionPeriod) {
        super(id, position, images, index, actionPeriod);
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

    public static Ore createOre(String id, Point pos, List<PImage> images, int actionPeriod) {
        return new Ore(ORE_ID_PREFIX + id, pos, images, 0, actionPeriod);
    }
}
