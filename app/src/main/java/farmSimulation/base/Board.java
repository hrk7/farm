package farmSimulation.base;
import farmSimulation.entities.*;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int width;
    private int height;
    private Entity[][] grid;
    private List<Entity> entities;

    public Board(int width, int height){
        this.width = width;
        this.height = height;
        this.grid = new Entity[height][width];
        this.entities = new ArrayList<>();
    }
    public void moveEntity(Entity entity, int newX, int newY){
        if(isValid(newX, newY) && grid[newX][newY] == null){
            grid[entity.getX()][entity.getY()] = null;
            entity.setPosition(newX, newY);
            grid[newX][newY] = entity;
        }
    }
    private boolean isValid(int x, int y){
        return  x >= 0 && x < width && y >= 0 && y < height;
    }
    public Potato findPotatoNearby(int cx, int cy){
        return (Potato) findTypeNearby(cx, cy, Potato.class);
    }
    public Beetle findBeetleNearby(int cx, int cy){
        return (Beetle) findTypeNearby(cx, cy, Beetle.class);
   }
    public Chicken findChickenNearby(int cx, int cy){
        return (Chicken) findTypeNearby(cx, cy, Chicken.class);
   }
    public Fox findFoxNearby(int cx, int cy){
        return (Fox) findTypeNearby(cx, cy, Fox.class);
    }
    private Entity findTypeNearby(int cx, int cy, Class<?> type){
        for(int dx = -1; dx <= 1; dx++){
            for(int dy = -1; dy <= 1; dy++){
                int nx = cx + dx;
                int ny = cy + dy;
                if(isValid(nx, ny) && grid[nx][ny] != null && type.isInstance(grid[nx][ny])){
                    return grid[nx][ny];
                }
            }
        }
        return null;
   }
}
