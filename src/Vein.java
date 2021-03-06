import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;


public class Vein extends ActiveEntity {

    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;
    private static final String ORE_KEY = "ore";

    public Vein(String id, Point position, List<PImage> images, int index, int actionPeriod) {
        super(id, position, images, index, actionPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(super.getPosition());

        if (openPt.isPresent()) {
            Ore ore = Ore.createOre(super.getId(), openPt.get(), imageStore.getImageList(ORE_KEY), (ORE_CORRUPT_MIN + new Random().nextInt(
                    ORE_CORRUPT_MAX - ORE_CORRUPT_MIN)));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                super.getActionPeriod());
    }

    public static Vein createVein(String id, Point pos, List<PImage> images, int actionPeriod) {
        return new Vein(id, pos, images, 0, actionPeriod);
    }

}
