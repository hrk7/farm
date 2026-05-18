package farmSimulation.entities;
public abstract class Entity {
    protected int x;
    protected int y;
    protected boolean alive = true;

    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void tick(Board board);

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public boolean isAlive(){ return alive;}
    public void die() { this.alive = false;}
    public abstract char getSymbol();
}