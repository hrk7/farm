package farmSimulation.entities;

import farmSimulation.base.Board;

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
    public void tick(Board board) {
        if (!isAlive()) return;

        Beetle targetBeetle = board.findBeetleNearby(x, y);
        Potato targetPotato = board.findPotatoNearby(x, y);

        interact(targetBeetle, targetPotato);
    }

    @Override
    public char getSymbol() {
        return 'C';
    }
}