import processing.core.PImage;

import java.util.List;

/**
 * An this that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class NanabBerry extends Entity {

    public NanabBerry(Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super("", position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public static NanabBerry createBerry(Point position, double animationPeriod, List<PImage> images) {
        return new NanabBerry(position, images, 0, 0, 0, animationPeriod, 0, 0);
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler) {
        eventScheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
    }
}
