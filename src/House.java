import processing.core.PImage;

import java.util.List;

/**
 * An this that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class House extends Entity {

    public House(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public static House createHouse(String id, Point position, List<PImage> images) {
        return new House(id, position, images, 0, 0, 0, 0, 0, 0);
    }
}
