import processing.core.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public final class VirtualWorld extends PApplet {
    private static String[] ARGS;

    public static final int VIEW_WIDTH = 640;
    public static final int VIEW_HEIGHT = 480;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;

    public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;

    public static final String IMAGE_LIST_FILE_NAME = "imagelist";
    public static final String DEFAULT_IMAGE_NAME = "background_default";
    public static final int DEFAULT_IMAGE_COLOR = 0x808080;

    public static final String FAST_FLAG = "-fast";
    public static final String FASTER_FLAG = "-faster";
    public static final String FASTEST_FLAG = "-fastest";
    public static final double FAST_SCALE = 0.5;
    public static final double FASTER_SCALE = 0.25;
    public static final double FASTEST_SCALE = 0.10;

    public String loadFile = "world.sav";
    public long startTimeMillis = 0;
    public double timeScale = 1.0;

    public ImageStore imageStore;
    public WorldModel world;
    public WorldView view;
    public EventScheduler scheduler;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        parseCommandLine(ARGS);
        loadImages(IMAGE_LIST_FILE_NAME);
        loadWorld(loadFile, this.imageStore);

        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler();
        this.startTimeMillis = System.currentTimeMillis();
        this.scheduleActions(world, scheduler, imageStore);
    }

    public void draw() {
        double appTime = (System.currentTimeMillis() - startTimeMillis) * 0.001;
        double frameTime = (appTime - scheduler.currentTime)/timeScale;
        this.update(frameTime);
        view.drawViewport();
    }

    public void update(double frameTime){
        scheduler.updateOnTime(frameTime);
    }

    // Just for debugging and for P5
    // Be sure to refactor this method as appropriate
    /*public void mousePressed() {
        Point pressed = mouseToPoint();
        System.out.println("CLICK! " + pressed.x + ", " + pressed.y);

        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent()) {
            Entity entity = entityOptional.get();
            System.out.println(entity.id + ": " + entity.getClass() + " : " + entity.health);
        }

    }*/

    public void mousePressed(){
        Point click = mouseToPoint();
        if(world.withinBounds(click) && !(world.isOccupied(click))){
            System.out.println("Snorlax, I choose you!");
            List<PImage> pokeballs = imageStore.getImageList("pokeball");
            Point right = new Point(click.x+1, click.y);
            Pokeball ball = Pokeball.createPokeball(right, 0.55, 0.1, pokeballs);
            world.addEntity(ball);
            ball.scheduleActions(world, imageStore, scheduler);
            List<PImage> snorlax_img = imageStore.getImageList("snorlax");
            Snorlax snorlax = Snorlax.createSnorlax(click, 0.5, 0.5, snorlax_img);
            world.addEntity(snorlax);
            snorlax.scheduleActions(world, imageStore, scheduler);



            for (int x = click.x - 1; x < click.x + 2; x++) {
                for (int y = click.y - 1; y < click.y + 2; y++) {
                    if (world.withinBounds(new Point(x, y))) {
                        world.setBackgroundCell(new Point(x, y), new Background("crackedstone", imageStore.getImageList("crackedstone")));
                        if (world.isOccupied(new Point(x, y))) {
                            Optional<Entity> entityOptional = world.getOccupant(new Point(x, y));
                            Entity entity = entityOptional.get();
                            if (entity instanceof Tree || entity instanceof Sapling || entity instanceof Stump){
                                world.removeEntityAt(new Point(x, y));
                                System.out.println("Oh no Snorlax! I think you crushed a " + entity.getClass());
                                Random random = new Random();
                                int range = 10;
                                int int_random = random.nextInt(range);
                                if(int_random > 4){
                                    List<PImage> berries = imageStore.getImageList("nanabberry");
                                    NanabBerry berry = NanabBerry.createBerry(new Point(x, y), 0.2, berries);
                                    berry.scheduleActions(world, imageStore, scheduler);
                                    world.addEntity(berry);
                                    System.out.println("Looks like an item dropped! Maybe it can help Snorlax!");
                                }
                            }

                        }
                    }

                }
            }
            if(world.withinBounds(new Point(click.x+2, click.y))){
                world.setBackgroundCell(new Point(click.x+2, click.y), new Background("crackedstone", imageStore.getImageList("crackedstone")));
                if(world.isOccupied(new Point(click.x+2, click.y))){
                    Optional<Entity> entityOptional = world.getOccupant(new Point(click.x + 2, click.y));
                    Entity entity = entityOptional.get();
                    if (entity instanceof Tree || entity instanceof Sapling || entity instanceof Stump) {
                        world.removeEntityAt(new Point(click.x+2, click.y));
                        System.out.println("Oh no Snorlax! I think you crushed a " + entity.getClass());
                        Random random = new Random();
                        int range = 10;
                        int int_random = random.nextInt(range);
                        if(int_random > 4){
                            List<PImage> berries = imageStore.getImageList("nanabberry");
                            NanabBerry berry = NanabBerry.createBerry(new Point(click.x+2, click.y), 0.2, berries);
                            berry.scheduleActions(world, imageStore, scheduler);
                            world.addEntity(berry);
                            System.out.println("Looks like an item dropped! Maybe it can help Snorlax!");
                        }
                    }
                }
            }
            if(world.withinBounds(new Point(click.x-2, click.y))){
                world.setBackgroundCell(new Point(click.x-2, click.y), new Background("crackedstone", imageStore.getImageList("crackedstone")));
                if(world.isOccupied(new Point(click.x-2, click.y))){
                    Optional<Entity> entityOptional = world.getOccupant(new Point(click.x - 2, click.y));
                    Entity entity = entityOptional.get();
                    if (entity instanceof Tree || entity instanceof Sapling || entity instanceof Stump) {
                        world.removeEntityAt(new Point(click.x-2, click.y));
                        System.out.println("Oh no Snorlax! I think you crushed a " + entity.getClass());
                        Random random = new Random();
                        int range = 10;
                        int int_random = random.nextInt(range);
                        if(int_random > 4){
                            List<PImage> berries = imageStore.getImageList("nanabberry");
                            NanabBerry berry = NanabBerry.createBerry(new Point(click.x-2, click.y), 0.2, berries);
                            berry.scheduleActions(world, imageStore, scheduler);
                            world.addEntity(berry);
                            System.out.println("Looks like an item dropped! Maybe it can help Snorlax!");
                        }
                    }
                }
            }
            if(world.withinBounds(new Point(click.x, click.y+2))){
                world.setBackgroundCell(new Point(click.x, click.y+2), new Background("crackedstone", imageStore.getImageList("crackedstone")));
                if(world.isOccupied(new Point(click.x, click.y+2))){
                    Optional<Entity> entityOptional = world.getOccupant(new Point(click.x, click.y + 2));
                    Entity entity = entityOptional.get();
                    if (entity instanceof Tree || entity instanceof Sapling || entity instanceof Stump) {
                        world.removeEntityAt(new Point(click.x, click.y+2));
                        System.out.println("Oh no Snorlax! I think you crushed a " + entity.getClass());
                        Random random = new Random();
                        int range = 10;
                        int int_random = random.nextInt(range);
                        if(int_random > 4){
                            List<PImage> berries = imageStore.getImageList("nanabberry");
                            NanabBerry berry = NanabBerry.createBerry(new Point(click.x, click.y+2), 0.2, berries);
                            berry.scheduleActions(world, imageStore, scheduler);
                            world.addEntity(berry);
                            System.out.println("Looks like an item dropped! Maybe it can help Snorlax!");
                        }
                    }
                }
            }
            if(world.withinBounds(new Point(click.x, click.y-2))){
                world.setBackgroundCell(new Point(click.x, click.y - 2), new Background("crackedstone", imageStore.getImageList("crackedstone")));
                if(world.isOccupied(new Point(click.x, click.y-2))){
                    Optional<Entity> entityOptional = world.getOccupant(new Point(click.x, click.y - 2));
                    Entity entity = entityOptional.get();
                    if (entity instanceof Tree || entity instanceof Sapling || entity instanceof Stump) {
                        world.removeEntityAt(new Point(click.x, click.y - 2));
                        System.out.println("Oh no Snorlax! I think you crushed a " + entity.getClass());
                        Random random = new Random();
                        int range = 10;
                        int int_random = random.nextInt(range);
                        if(int_random > 4){
                            List<PImage> berries = imageStore.getImageList("nanabberry");
                            NanabBerry berry = NanabBerry.createBerry(new Point(click.x, click.y-2), 0.2, berries);
                            berry.scheduleActions(world, imageStore, scheduler);
                            world.addEntity(berry);
                            System.out.println("Looks like an item dropped! Maybe it can help Snorlax!");
                        }
                    }
                }
            }



        } else{
            Optional<Entity> entityOptional = world.getOccupant(click);
            Entity entity = entityOptional.get();
            System.out.println("There isn't room for Snorlax here! There's a " + entity.getClass() + " in the way!");
        }

    }

    public void scheduleActions(WorldModel world, EventScheduler eventScheduler, ImageStore imageStore) {
        for (Entity entity : world.entities) {
            entity.scheduleActions(world, imageStore, eventScheduler);
        }
    }

    private Point mouseToPoint() {
        return view.viewport.viewportToWorld(mouseX / TILE_WIDTH, mouseY / TILE_HEIGHT);
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP -> dy -= 1;
                case DOWN -> dy += 1;
                case LEFT -> dx -= 1;
                case RIGHT -> dx += 1;
            }
            view.shiftView(dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME, imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        Arrays.fill(img.pixels, color);
        img.updatePixels();
        return img;
    }

    public void loadImages(String filename) {
        this.imageStore = new ImageStore(createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in,this);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadWorld(String file, ImageStore imageStore) {
        this.world = new WorldModel();
        try {
            Scanner in = new Scanner(new File(file));
            world.load(in, imageStore, createDefaultBackground(imageStore));
        } catch (FileNotFoundException e) {
            Scanner in = new Scanner(file);
            world.load(in, imageStore, createDefaultBackground(imageStore));
        }
    }

    public void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG -> timeScale = Math.min(FAST_SCALE, timeScale);
                case FASTER_FLAG -> timeScale = Math.min(FASTER_SCALE, timeScale);
                case FASTEST_FLAG -> timeScale = Math.min(FASTEST_SCALE, timeScale);
                default -> loadFile = arg;
            }
        }
    }

    public static void main(String[] args) {
        VirtualWorld.ARGS = args;
        PApplet.main(VirtualWorld.class);
    }

    public static List<String> headlessMain(String[] args, double lifetime){
        VirtualWorld.ARGS = args;

        VirtualWorld virtualWorld = new VirtualWorld();
        virtualWorld.setup();
        virtualWorld.update(lifetime);

        return virtualWorld.world.log();
    }
}
