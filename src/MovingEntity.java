import processing.core.PImage;

import java.util.List;

abstract public class MovingEntity extends AnimatedEntity {


    public MovingEntity(String id, Point position, List<PImage> images, int index,
                 int actionPeriod, int animationPeriod)
    {
        super(id, position, images, index, actionPeriod, animationPeriod);
    }

    abstract public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);

    abstract public Point nextPosition(
            WorldModel world,
            Point destPos);
}