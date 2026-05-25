package farmSimulation.base;
import farmSimulation.entities.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private int width;
    private int height;
    private Entity[][] grid;
    private List<Entity> entities;
    private Random random = new Random();

    public Board(int width, int height){
        this.width = width;
        this.height = height;
        this.grid = new Entity[height][width];
        this.entities = new ArrayList<>();
    }

    public void addEntity(Entity entity){
        if(isValid(entity.getX(), entity.getY()) && grid[entity.getY()][entity.getX()] == null){
            grid[entity.getY()][entity.getX()] = entity;
            entities.add(entity);
            System.out.println("Dodano: " + entity.getClass().getSimpleName() + " na pozycji (" + entity.getX() + "," + entity.getY() + ").");
        } else {
            System.out.println("Błąd: Nie można dodać " + entity.getClass().getSimpleName() + " na (" + entity.getX() + "," + entity.getY() + "). Miejsce zajęte lub poza planszą.");
        }
    }

    public void removeEntity(Entity entity){
        if(isValid(entity.getX(), entity.getY()) && grid[entity.getY()][entity.getX()] == entity){
            grid[entity.getY()][entity.getX()] = null;
        }
        entities.remove(entity);
    }

    public void spawnRandomEntity(){
        int[] freePosition = findRandomFreePosition();
        if (freePosition == null){
            System.out.println("Brak wolnego miejsca na planszy na nowy obiekt");
            return;
        }
        int x = freePosition[0];
        int y = freePosition[1];

        int chance = random.nextInt(100);
        if(chance < 40){
            Potato newPotato = new Potato(x, y, 1.0);
            addEntity(newPotato);
            System.out.println("Na polu (" + x + "," + y + ") pojawił sie ziemniak!");
        } else if (chance < 70){
            Beetle newBeetle = new Beetle(x, y);
            addEntity(newBeetle);
            System.out.println("Na polu (" + x + "," + y + ") pojawiła sie stonka!");
        } else if (chance < 90){
            Chicken newChicken = new Chicken(x, y);
            addEntity(newChicken);
            System.out.println("Na polu (" + x + "," + y + ") pojawiła sie kura!");
        } else {
            Fox newFox = new Fox(x, y);
            addEntity(newFox);
            System.out.println("Na polu (" + x + "," + y + ") pojawił sie lis!");
        }
    }

    private int[] findRandomFreePosition(){
        for(int i = 0; i < 100; i++){
            int rx = random.nextInt(width);
            int ry = random.nextInt(height);
            if(grid[ry][rx] == null){
                return new int[]{rx, ry};
            }
        }
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(grid[y][x] == null){
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    public void nextTick(){
        List<Entity> copy = new ArrayList<>(entities);
        for(Entity e: copy){
            if(e.isAlive() && e.getClass() == Potato.class){
                e.tick(this);
            }
        }
        for(Entity e: copy){
            if(e.isAlive() && e.getClass() == Beetle.class){
                e.tick(this);
            }
        }
        for(Entity e: copy){
            if(e.isAlive() && e.getClass() == Fox.class){
                e.tick(this);
            }
        }
        for(Entity e: copy){
            if(e.isAlive() && e.getClass() == Chicken.class){
                e.tick(this);
            }
        }
        for(Entity e: copy){
            if(e.isAlive() && e.getClass() == Farmer.class){
                e.tick(this);
            }
        }
    }

    public void moveEntity(Entity entity, int newX, int newY){
        int dx = newX - entity.getX();
        int dy = newY - entity.getY();

        int targetX = newX;
        int targetY = newY;

        if (targetX < 0 || targetX >= width) {
            targetX = entity.getX() - dx;
        }
        if (targetY < 0 || targetY >= height) {
            targetY = entity.getY() - dy;
        }

        if(isValid(targetX, targetY) && grid[targetY][targetX] == null){
            grid[entity.getY()][entity.getX()] = null;

            entity.setPosition(targetX, targetY);

            grid[targetY][targetX] = entity;
        }
    }

    private boolean isValid(int x, int y){
        return  x >= 0 && x < width && y >= 0 && y < height;
    }
    public Potato findPotatoNearby(int cx, int cy, int r){
        return (Potato) findTypeNearby(cx, cy, Potato.class, r);
    }
    public Beetle findBeetleNearby(int cx, int cy, int r){
        return (Beetle) findTypeNearby(cx, cy, Beetle.class, r);
   }
    public Chicken findChickenNearby(int cx, int cy, int r){
        return (Chicken) findTypeNearby(cx, cy, Chicken.class, r);
   }
    public Fox findFoxNearby(int cx, int cy, int r){
        return (Fox) findTypeNearby(cx, cy, Fox.class, r);
    }

    private Entity findTypeNearby(int cx, int cy, Class<?> type, int range) {
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                int nx = cx + dx;
                int ny = cy + dy;
                if (isValid(nx, ny) && grid[ny][nx] != null && type.isInstance(grid[ny][nx])) {
                    return grid[ny][nx];
                }
            }
        }
        return null;
    }

    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }

    public Entity getEntityAt(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return this.grid[y][x];
        }
        return null;
    }
}

