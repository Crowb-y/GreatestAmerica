public class Animation extends Action {
    private final int repeatCount;

    public Animation(Entity entity, int repeatCount) {
        super(entity);
        this.repeatCount = repeatCount;
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        ((AnimatedEntity) super.getEntity()).nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(super.getEntity(),
                    AnimatedEntity.createAnimationAction(super.getEntity(), Math.max(repeatCount - 1, 0)),
                    ((AnimatedEntity) super.getEntity()).getAnimationPeriod());
        }
    }
}
