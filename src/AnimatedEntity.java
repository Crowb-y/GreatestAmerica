import processing.core.PImage;

import java.util.List;

abstract public class AnimatedEntity extends ActiveEntity {

    private final int animationPeriod;
    private int repeatCount;

    public AnimatedEntity(String id, Point position, List<PImage> images,
                          int index, int actionPeriod, int animationPeriod) {
        super(id, position, images, index, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this, createAnimationAction(this, repeatCount), animationPeriod);
    }

    public int getAnimationPeriod(){
        return animationPeriod;
    }

    public void nextImage() {
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    }

    public static Action createAnimationAction(Entity entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }
}
