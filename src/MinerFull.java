import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends Miner {

    public MinerFull(String id, Point position, List<PImage> images, int index,
                     int resourceLimit, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, index, actionPeriod, animationPeriod, resourceLimit);
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> fullTarget =
                world.findNearest(super.getPosition(), Blacksmith.class);

        if (fullTarget.isPresent() && moveToFull(world,
                fullTarget.get(), scheduler)) {
            transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }
    }

    public void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        MinerNotFull miner = new MinerNotFull(super.getId(), super.getPosition(),
                super.getImages(), super.getResourceLimit(), super.getActionPeriod(),
                super.getAnimationPeriod());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveToFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (super.getPosition().adjacent(target.getPosition())) {
            return true;
        } else {
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
}