import processing.core.PImage;

import java.util.List;
import java.util.Random;

/**
 * An this that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Tree extends Entity {

    public Tree(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public static Tree createTree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        return new Tree(id, position, images, 0, 0, actionPeriod, animationPeriod, health, 0);
    }

    private boolean transformTree(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Stump stump = Stump.createStump(Functions.STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            //trydropBerry(world, scheduler, imageStore, this.position);
            Point np = new Point(this.position.x, this.position.y-1);
            if(/*int_random > 2 && */!world.isOccupied(np) && world.withinBounds(np)){
                List<PImage> berries = imageStore.getImageList("nanabberry");
                NanabBerry berry = NanabBerry.createBerry(np, 0.2, berries);
                berry.scheduleActions(world, imageStore, scheduler);
                world.addEntity(berry);
                System.out.println("Looks like an item dropped! Maybe it can help Snorlax!");
            }


            return true;
        }

        return false;
    }

    /*public void trydropBerry(WorldModel world, EventScheduler scheduler, ImageStore imageStore, Point pos){
        Random random = new Random();
        int range = 10;
        int int_random = random.nextInt(range);
        Point np = new Point(pos.x, pos.y-1);
        //System.out.println("test");
        if(*//*int_random > 2 && *//*!world.isOccupied(np) && world.withinBounds(np)){
            List<PImage> berries = imageStore.getImageList("nanabberry");
            NanabBerry berry = NanabBerry.createBerry(np, 0.2, berries);
            berry.scheduleActions(world, imageStore, scheduler);
            world.addEntity(berry);
            System.out.println("Looks like an item dropped! Maybe it can help Snorlax!");
        }
    }*/

    private boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        return transformTree(world, scheduler, imageStore);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (!transformPlant(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler) {
        eventScheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.actionPeriod);
        eventScheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
    }
}
