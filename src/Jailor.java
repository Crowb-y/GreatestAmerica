import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Jailor extends Trump {

    public Jailor(String id, Point position, List<PImage> images, int index, int actionPeriod, int animationPeriod) {
        super(id, position, images, index, actionPeriod, animationPeriod);
    }

    public static Jailor createJailor(String id, Point pos, List<PImage> images) {
        return new Jailor(id, pos, images, 0, 10, 5);
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> trumpTarget =
                world.nearestMiner(super.getPosition());
        System.out.println(trumpTarget.get().toString());

        if (trumpTarget.isPresent() && moveTo(world,
                trumpTarget.get(), scheduler)) {
            world.removeEntity(trumpTarget.get());
            scheduler.unscheduleAllEvents(trumpTarget.get());
            super.scheduleActions(scheduler, world, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }
    }
}
