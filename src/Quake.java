import processing.core.PImage;

import java.util.List;

public class Quake extends AnimatedEntity {

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake (String id, Point position, List<PImage> images, int index, int actionPeriod,
                  int animationPeriod, int repeatCount)
    {
        super(id, position, images, index, actionPeriod, animationPeriod);
        super.setRepeatCount(repeatCount);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public static Quake createQuake(Point pos, List<PImage> images) {
        return new Quake(QUAKE_ID, pos, images, 0, QUAKE_ACTION_PERIOD,
                QUAKE_ANIMATION_PERIOD, QUAKE_ANIMATION_REPEAT_COUNT);
    }
}
