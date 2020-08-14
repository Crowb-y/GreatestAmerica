import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends Miner {

    private int resourceCount;

    public MinerNotFull(String id, Point position, List<PImage> images, int index,
                        int actionPeriod, int animationPeriod, int resourceLimit,
                        int resourceCount) {
        super(id, position, images, index, actionPeriod, animationPeriod, resourceLimit);
        this.resourceCount = resourceCount;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget =
                world.findNearest(super.getPosition(), Ore.class);

        if (notFullTarget.isEmpty() || !moveToNotFull( world,
                notFullTarget.get(),
                scheduler)
                || !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }
    }
    public boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.resourceCount >= super.getResourceLimit()) {
            MinerFull miner = MinerFull.createMinerFull(super.getId(), super.getPosition(), super.getImages(),
                    super.getImageIndex(), super.getResourceLimit(), super.getActionPeriod(),
                    super.getAnimationPeriod());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
    public boolean moveToNotFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (super.getPosition().adjacent(target.getPosition())) {
            resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            Point nextPos = nextPositionMiner(world, target.getPosition());

            if (!super.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public static MinerNotFull createMinerNotFull(String id, Point pos, List<PImage> images,
                                                  int actionPeriod, int animationPeriod,
                                                  int resourceLimit) {
        return new MinerNotFull(id, pos, images,0, actionPeriod,
                animationPeriod, resourceLimit, 0);
    }
}
