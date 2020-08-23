import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Melania extends Trump{

    public Melania(String id, Point position, List<PImage> images, int index, int actionPeriod,
                  int animationPeriod) {
        super(id, position, images, index, actionPeriod, animationPeriod);

    }

    public static Melania createMelania(String id, Point pos, List<PImage> images) {

        return new Melania(id, pos, images, 0, 10, 1);
    }

    /* Melania plays a key role in the Trump team. While Donald has to deal with the
    * hooligan miners, Melania's job is to not let the ore go to waste. Don't ask why
    * Melania has the ability of King Midas, she just does. With the miners gone, there are
    * plenty of oreBlobs for her to turn into spinning gold bars. To keep track of the ultimate win state
    * (Miners and oreBlobs completely captured) we set the OreBlobs captured state to true in addition to updating
    * its imageList and imageIndex. Besides this the behavior is the same. */

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> trumpTarget =
                world.nearestBlob(super.getPosition());
        if (trumpTarget.isPresent() && moveTo(world,
                trumpTarget.get(), scheduler) && super.getTpCoolDown() == 0) {
            Entity target = trumpTarget.get();
            ((MovingEntity)target).setCaptured();
            MovingEntity.numCaptured += 1;
            target.setImages(imageStore.getImageList("gold"));
            target.setImageIndex(0);
            super.scheduleActions(scheduler, world, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }
    }
}

