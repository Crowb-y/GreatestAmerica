import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Donald extends Trump {

    private Point jailPos;
    private List<PImage> quakeImages;

    public Donald(String id, Point position, List<PImage> images, int index, int actionPeriod,
                  int animationPeriod, Point jailPos, List<PImage> quakeImages) {
        super(id, position, images, index, actionPeriod, animationPeriod);
        this.jailPos = jailPos;
        this.quakeImages = quakeImages;
    }

    public static Donald createDonald(String id, Point pos, List<PImage> images, Point jailPos, List<PImage> quakeImages) {
        return new Donald(id, pos, images, 0, 1, 1, jailPos, quakeImages);
    }

    /* Donald's execute activity will enable him to relentlessly hunt down
    * every miner on the world, and 'poofs' them to his corresponding jail.
    * The miners affected will be set to a captured state, and the numCaptured
    * will be incremented to keep track of his great victory. Combined with the
    * overpowered teleportation ability in the parent classes moveTo, Donald is
    * an unstoppable force */

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> trumpTarget =
                world.nearestMiner(super.getPosition());
        if (trumpTarget.isPresent() && moveTo(world,
                trumpTarget.get(), scheduler) && super.getTpCoolDown() == 0) {
            Entity target = trumpTarget.get();
            Quake quake = Quake.createQuake(target.getPosition(), quakeImages);
            if (world.findOpenAround(jailPos).isPresent())
                world.moveEntity(target, world.findOpenAround(jailPos).get());
            else
                world.moveEntity(target, jailPos);
            world.addEntity(quake);
            quake.scheduleActions(scheduler, world, imageStore);
            ((MovingEntity)target).setCaptured();
            MovingEntity.numCaptured += 1;
            super.scheduleActions(scheduler, world, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }
    }
}
