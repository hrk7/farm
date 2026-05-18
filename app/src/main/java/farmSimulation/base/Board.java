package farmSimulation.base;
import farmSimulation.entities.*;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int width;
    private int height;
    private Entity[][] grid;
    private List<Entity> entities;

    public void addEntity(Entity entity){
        if(isValid(entity.getX(), entity.getY()) && grid[entity.getY()][entity.getX()] == null){
            grid[entity.getY()][entity.getX()] = entity;
            entities.add(entity);
        }
    }

    public void removeEntity(Entity entity){
        if(isValid(entity.getX(), entity.getY()) && grid[entity.getY()][entity.getX()] == entity){
            grid[entity.getY()][entity.getX()] = null;
        }
        entities.remove(entity);
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

    public Board(int width, int height){
        this.width = width;
        this.height = height;
        this.grid = new Entity[height][width];
        this.entities = new ArrayList<>();
    }
    public void moveEntity(Entity entity, int newX, int newY){
        if(isValid(newY, newX) && grid[newY][newX] == null){
            grid[entity.getY()][entity.getX()] = null;
            entity.setPosition(newY, newX);
            grid[newY][newX] = entity;
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
    public void display(){
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                if(grid[y][x] == null){
                    System.out.print(". ");
                } else {
                    System.out.print(grid[y][x].getSymbol() + " ");
                }
                System.out.println();
            }
        }
        System.out.println("=========");
    }
}
