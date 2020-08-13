abstract public class Action
{
    private final Entity entity;

    public Action(Entity entity) {
        this.entity = entity;
    }

    abstract void executeAction(EventScheduler scheduler);

    protected Entity getEntity() {
        return entity;
    }
}
