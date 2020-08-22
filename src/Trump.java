import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Trump extends MovingEntity {

    public Trump(String id, Point position, List<PImage> images, int index, int actionPeriod, int animationPeriod) {
        super(id, position, images, index, actionPeriod, animationPeriod);
        System.out.println("try" + position.getX());
    }

    public static Trump createTrump(String id, Point pos, List<PImage> images) {
        return new Trump(id, pos, images, 0, 2, 100);
    }

    @Override
    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (super.getPosition().adjacent(target.getPosition())) {
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

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> trumpTarget =
                world.nearestMiner(super.getPosition());

        if (trumpTarget.isPresent() && moveTo(world,
                trumpTarget.get(), scheduler)) {
            world.removeEntity(trumpTarget.get());
            scheduler.unscheduleAllEvents(trumpTarget.get());
        }
        else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }

    }
}
