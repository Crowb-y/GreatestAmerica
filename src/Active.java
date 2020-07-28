public interface Active extends Entity {
    void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore);

    void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);

    Action createActivityAction(
            WorldModel world, ImageStore imageStore);
}
