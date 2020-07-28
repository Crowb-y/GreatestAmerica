import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull implements Miner {

    private String id;
    private Point position;
    private List<PImage> images;
    private final int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;
    private int imageIndex;

    @Override
    public Point getPosition() {
        return position;
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public MinerFull(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceLimit;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        imageIndex = 0;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                actionPeriod);
        scheduler.scheduleEvent(this,
                createAnimationAction(0),
                getAnimationPeriod());
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> fullTarget =
                world.findNearest(position, Blacksmith.class);

        if (fullTarget.isPresent() && moveToFull(world,
                fullTarget.get(), scheduler)) {
            transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    actionPeriod);
        }
    }

    public void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        MinerNotFull miner = new MinerNotFull(id, position,
                images, resourceLimit, actionPeriod,
                animationPeriod);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveToFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (position.adjacent(target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPositionMiner(world, target.getPosition());

            if (!position.equals(nextPos)) {
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
    public Action createActivityAction(
            WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore, 0);
    }

    @Override
    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }

    @Override
    public PImage getCurrentImage() {
        return images.get(imageIndex);
    }

    @Override
    public void setPosition(Point pos) {
        position = pos;
    }

    public Point nextPositionMiner(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.getX() - position.getX());
        Point newPos = new Point(position.getX() + horiz, position.getY());

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - position.getY());
            newPos = new Point(position.getX(), position.getY() + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = position;
            }
        }

        return newPos;
    }

    @Override
    public int getAnimationPeriod() {
        return 0;
    }
}