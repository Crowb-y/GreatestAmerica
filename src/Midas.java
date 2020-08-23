import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Midas extends Trump{

    public Midas(String id, Point position, List<PImage> images, int index, int actionPeriod,
                  int animationPeriod) {
        super(id, position, images, index, actionPeriod, animationPeriod);

    }

    public static Midas createMidas(String id, Point pos, List<PImage> images) {

        return new Midas(id, pos, images, 0, 20, 150);
    }

    //Still need to edit below
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> trumpTarget =
                world.nearestBlob(super.getPosition());
        if (trumpTarget.isPresent() && moveTo(world,
                trumpTarget.get(), scheduler) && super.getTpCoolDown() == 0) {
            Entity target = trumpTarget.get();
            ((MovingEntity)target).setCaptured();
            target.setImages(imageStore.getImageList("gold"));
            super.scheduleActions(scheduler, world, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }
    }
}

