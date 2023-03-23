import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An this that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Snorlax extends Entity implements Moves {

    public ImageStore imageStore;

    public Snorlax(Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super("", position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public static Snorlax createSnorlax(Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Snorlax(position, images, 0, 0, actionPeriod, animationPeriod, 0, 0);
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy strategy = new AStarPathingStrategy();
        List<Point> points = strategy.computePath(this.position, destPos, p -> world.withinBounds(p) && (!(world.isOccupied(p))), (p1, p2) -> adjacent(p1, p2), PathingStrategy.CARDINAL_NEIGHBORS);
        if(points.size() == 0){
            return this.position;
        }

        return points.get(0);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (adjacent(this.position, target.position)) {
            world.removeEntityAt(target.position);
            return true;
        } else {
            Point nextPos = nextPosition(world, target.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    public static boolean adjacent(Point p1, Point p2) {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) || (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> snorTarget = world.findNearest(this.position, new ArrayList<>(List.of(NanabBerry.class)));

        if(snorTarget.isPresent() == true){
            if(moveTo(world, snorTarget.get(), scheduler) == true){
                Entity berry = snorTarget.get();
                eatBerry(world, scheduler, berry, imageStore);
            }
        }
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.actionPeriod);
    }

    public void eatBerry(WorldModel world, EventScheduler scheduler, Entity berry, ImageStore imageStore){
        world.removeEntity(scheduler, berry);
        this.actionPeriod += 0.3;
        this.animationPeriod += 0.3;
        System.out.println("Snorlax ate a nanab berry! He got a little slower...");

        if(this.actionPeriod > 1.1 || this.animationPeriod > 1.1){
            Point temp = this.position;
            world.removeEntity(scheduler, this);
            List<PImage> snorlax_img = imageStore.getImageList("snorlaxsleep");
            Snorlax sleepysnorlax = Snorlax.createSnorlax(temp, 0, 0, snorlax_img);
            world.addEntity(sleepysnorlax);
            System.out.println("Oh no... Snorlax fell asleep.");
        }
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler) {
        eventScheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.actionPeriod);
        eventScheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
    }
}
