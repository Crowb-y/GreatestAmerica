import processing.core.PImage;

import java.util.List;

abstract public class MovingEntity extends AnimatedEntity {

    // these static variables are used to tell the game when the Trumps win.

    public static int numCaptured;
    public static int numBlobs;
    public static int numMiners;

    // this boolean value is only set to true when a trump has interacted with a MovingEntity
    private boolean captured;

    public MovingEntity(String id, Point position, List<PImage> images, int index,
                 int actionPeriod, int animationPeriod)
    {
        super(id, position, images, index, actionPeriod, animationPeriod);
        captured = false;
    }

    abstract public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);

    abstract public Point nextPosition(
            WorldModel world,
            Point destPos);

    public boolean getCaptured() { return captured; }

    public void setCaptured() { captured = true; }

    // Win condition check called by VirtualWorld during every tick.
    public static boolean checkWinCondition() {
        if (numBlobs > 0)
            return (numBlobs + numMiners - numCaptured) == 0;
        else
            return false;
    }

}