package farmSimulation.entities;

import farmSimulation.base.Board;

import java.util.Random;

public class Fox extends Entity {
    private Random random = new Random();

    public Fox(int x, int y) {
        super(x, y);
    }

    public void hunt(Chicken chicken) {
        if (chicken != null && chicken.isAlive()) {
            chicken.die();
        }
    }

    @Override
    public void tick(Board board) {
        if (!isAlive()) return;

        Chicken targetChicken = board.findChickenNearby(x, y, 1);

        if (targetChicken != null && targetChicken.isAlive()) {
            hunt(targetChicken);
            board.removeEntity(targetChicken);
        } else {
            int newX = x + random.nextInt(5) - 2;
            int newY = y + random.nextInt(5) - 2;

            board.moveEntity(this, newX, newY);
        }
    }

    @Override
    public char getSymbol() {
        return '狐';
    }
}