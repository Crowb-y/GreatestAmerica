public final class Action
{
    private ActionKind kind;
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public Action(
            ActionKind kind,
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public ActionKind getKind() {return kind;}
    public Entity getEntity() {return entity;}
    public WorldModel getWorld() {return world;}
    public ImageStore getImageStore() {return imageStore;}
    public int getRepeatCount() {return repeatCount;}

    public void executeAction(EventScheduler scheduler) {
        switch (kind) {
            case ACTIVITY:
                executeActivityAction(scheduler);
                break;

            case ANIMATION:
                executeAnimationAction(scheduler);
                break;
        }
    }

    public void executeAnimationAction(EventScheduler scheduler)
    {
        entity.nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(entity,
                    entity.createAnimationAction(
                            Math.max(repeatCount - 1,
                                    0)),
                    entity.getAnimationPeriod());
        }
    }

    public void executeActivityAction(EventScheduler scheduler)
    {
        switch (entity.getEntityKind()) {
            case MINER_FULL:
                entity.executeMinerFullActivity(world,
                        imageStore, scheduler);
                break;

            case MINER_NOT_FULL:
                entity.executeMinerNotFullActivity(world,
                        imageStore, scheduler);
                break;

            case ORE:
                entity.executeOreActivity(world,
                        imageStore, scheduler);
                break;

            case ORE_BLOB:
                entity.executeOreBlobActivity(world,
                        imageStore, scheduler);
                break;

            case QUAKE:
                entity.executeQuakeActivity(world,
                        imageStore, scheduler);
                break;

            case VEIN:
                entity.executeVeinActivity(world,
                        imageStore, scheduler);
                break;

            default:
                throw new UnsupportedOperationException(String.format(
                        "executeActivityAction not supported for %s",
                        entity.getEntityKind()));
        }
    }
}
