import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends MovingEntity {

    private final int resourceLimit;

    public MinerFull(String id, Point position, List<PImage> images, int index, int actionPeriod, int animationPeriod,
                     int resourceLimit)
    {
        super(id, position, images, index, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
    }

    public int getResourceLimit() {
        return resourceLimit;
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> fullTarget =
                world.findNearest(super.getPosition(), Blacksmith.class);

        if (fullTarget.isPresent() && moveTo(world,
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
        MinerNotFull miner = MinerNotFull.createMinerNotFull(super.getId(), super.getPosition(),
                super.getImages(), super.getActionPeriod(),
                super.getAnimationPeriod(), getResourceLimit());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    @Override
    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (super.getPosition().adjacent(target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

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

    public static MinerFull createMinerFull(String id, Point pos, List<PImage> images,
                                            int index, int actionPeriod,
                                            int animationPeriod, int resourceLimit) {
        return new MinerFull(id, pos, images, index, actionPeriod, animationPeriod, resourceLimit);
    }
    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.getX() - super.getPosition().getX());
        Point newPos = new Point(super.getPosition().getX() + horiz, super.getPosition().getY());

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - super.getPosition().getY());
            newPos = new Point(super.getPosition().getX(), super.getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = super.getPosition();
            }
        }
        return newPos;
    }
}