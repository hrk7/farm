package farmSimulation.entities;

public class Chicken extends Entity {

    public Chicken(int x, int y) {
        super(x, y);
    }

    public void interact(Beetle beetle, Potato potato) {
        if (!isAlive()) return;

        if (beetle != null && beetle.isAlive()) {
            beetle.die();
        }
        else if (potato != null && potato.getMass() > 0) {
            potato.consume(0.5);
        }
    }

    @Override
    public void tick() {
        if (!isAlive()) return;
    }

    @Override
    public char getSymbol() {
        return 'C';
    }
}