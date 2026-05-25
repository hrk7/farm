package farmSimulation.entities;

import farmSimulation.base.Board;
import java.util.Random;

public class Farmer extends Entity {
    private int protectionRadius;
    private Random random = new Random();

    public Farmer(int x, int y, int protectionRadius) {
        super(x, y);
        this.protectionRadius = protectionRadius;
    }

    @Override
    public void tick(Board board) {
        if (!isAlive()) return;

        int newX = x + random.nextInt(3) - 1;
        int newY = y + random.nextInt(3) - 1;
        board.moveEntity(this, newX, newY);

        Potato nearbyPotato = board.findPotatoNearby(x, y, 1);
        if (nearbyPotato != null && nearbyPotato.getMass() > 5.0) {
            int targetX = nearbyPotato.getX();
            int targetY = nearbyPotato.getY();

            harvest(nearbyPotato);
            board.removeEntity(nearbyPotato);
            System.out.println("Farmer zbiera ziemniaka na pozycji (" + targetX + "," + targetY + ").");
            board.markAction();
        }

        Beetle nearbyBeetle = board.findBeetleNearby(x, y, protectionRadius);
        if (nearbyBeetle != null && nearbyBeetle.isAlive()) {
            int targetX = nearbyBeetle.getX();
            int targetY = nearbyBeetle.getY();

            nearbyBeetle.die();
            board.removeEntity(nearbyBeetle);
            System.out.println("Farmer zabija stonkę na pozycji (" + targetX + "," + targetY + ").");
            board.markAction();
        }

        Fox nearbyFox = board.findFoxNearby(x, y, protectionRadius);
        if(nearbyFox != null && nearbyFox.isAlive()){
            int targetX = nearbyFox.getX();
            int targetY = nearbyFox.getY();

            nearbyFox.die();
            board.removeEntity(nearbyFox);
            System.out.println("Farmer zabija lisa na pozycji (" + targetX + "," + targetY + ").");
            board.markAction();
        }
    }

    public void harvest(Potato potato) {
        double harvestedMass = potato.getMass();
        potato.consume(harvestedMass);
    }

    @Override
    public char getSymbol() {
        return 'F';
    }
}