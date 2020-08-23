import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Midas extends Trump{

    private Point jailPos;

    public Midas(String id, Point position, List<PImage> images, int index, int actionPeriod,
                  int animationPeriod, Point jailPos) {
        super(id, position, images, index, actionPeriod, animationPeriod);
        this.jailPos = jailPos;
    }

    public static Midas createMidas(String id, Point pos, List<PImage> images,Point jailPos) {
        return new Midas(id, pos, images, 0, 20, 150, jailPos);
    }

    //Still need to edit below
    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> trumpTarget =
                world.nearestMiner(super.getPosition());
        if (trumpTarget.isPresent() && moveTo(world,
                trumpTarget.get(), scheduler)) {
            Entity target = trumpTarget.get();
            world.moveEntity(target, jailPos);
            ((MovingEntity)target).setCaptured();
            super.scheduleActions(scheduler, world, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }
    }
}
