import processing.core.PImage;

import java.util.List;
import java.util.Optional;

abstract public class Trump extends MovingEntity {

    int tpCoolDown;

    public Trump(String id, Point position, List<PImage> images, int index, int actionPeriod, int animationPeriod) {
        super(id, position, images, index, actionPeriod, animationPeriod);
        tpCoolDown = 0;
    }

    @Override
    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (tpCoolDown > 0) {
            tpCoolDown--;
            return true;
        }
        if (super.getPosition().adjacent(target.getPosition())) {
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());
            if (getPosition().equals(nextPos)) {
                if (world.findOpenAround(target.getPosition()).isPresent()){
                    nextPos = world.findOpenAround(target.getPosition()).get();
                    tpCoolDown = 10;
                }
                world.moveEntity(this, nextPos);
            }
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

    protected int getTpCoolDown() { return tpCoolDown; }
}
