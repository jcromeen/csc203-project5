import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An this that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Pokeball extends Entity {

    public Pokeball(Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super("", position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public static Pokeball createPokeball(Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Pokeball(position, images, 0, 0, actionPeriod, animationPeriod, 0, 0);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if(world.withinBounds(this.position)){
            world.removeEntity(scheduler, this);
        }
    }
    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler) {
        eventScheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
        eventScheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);

    }
}
