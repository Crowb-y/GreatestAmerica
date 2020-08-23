import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Jailor extends Trump {

    private Point jailPos;
    private List<PImage> quakeImages;
    private int jailCoolDown;

    public Jailor(String id, Point position, List<PImage> images, int index, int actionPeriod,
                  int animationPeriod, Point jailPos, List<PImage> quakeImages) {
        super(id, position, images, index, actionPeriod, animationPeriod);
        this.jailPos = jailPos;
        this.quakeImages = quakeImages;
        this.jailCoolDown = 0;
    }

    public static Jailor createJailor(String id, Point pos, List<PImage> images, Point jailPos, List<PImage> quakeImages) {
        return new Jailor(id, pos, images, 0, 1, 1, jailPos, quakeImages);
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> trumpTarget =
                world.nearestMiner(super.getPosition());
        if (trumpTarget.isPresent() && moveTo(world,
                trumpTarget.get(), scheduler) && super.getTpCoolDown() == 0) {
            Entity target = trumpTarget.get();
            Quake quake = Quake.createQuake(target.getPosition(), quakeImages);
            if (world.findOpenAround(jailPos).isPresent())
                world.moveEntity(target, world.findOpenAround(jailPos).get());
            else
                world.moveEntity(target, jailPos);
            world.addEntity(quake);
            quake.scheduleActions(scheduler, world, imageStore);
            ((MovingEntity)target).setCaptured();
            super.scheduleActions(scheduler, world, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }
    }
}
