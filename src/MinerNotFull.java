import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends MovingEntity {

    private int resourceCount;
    private final int resourceLimit;

    public MinerNotFull(String id, Point position, List<PImage> images, int index,
                        int actionPeriod, int animationPeriod, int resourceLimit,
                        int resourceCount) {
        super(id, position, images, index, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public int getResourceLimit() {
        return resourceLimit;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget =
                world.findNearest(super.getPosition(), Ore.class);

        if (notFullTarget.isEmpty() || !moveTo( world,
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
        if (this.resourceCount >= getResourceLimit()) {
            MinerFull miner = MinerFull.createMinerFull(super.getId(), super.getPosition(), super.getImages(),
                    super.getImageIndex(), super.getActionPeriod(), super.getAnimationPeriod(),
                    getResourceLimit());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    @Override
    public boolean moveTo(
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

    public static MinerNotFull createMinerNotFull(String id, Point pos, List<PImage> images,
                                                  int actionPeriod, int animationPeriod,
                                                  int resourceLimit) {
        return new MinerNotFull(id, pos, images,0, actionPeriod,
                animationPeriod, resourceLimit, 0);
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
