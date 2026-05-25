package farmSimulation.entities;

import farmSimulation.base.Board;
import java.util.Random;

public class Farmer extends Entity {
    private int protectionRadius;
    private int visionRadius = 3;
    private Random random = new Random();

    public Farmer(int x, int y, int protectionRadius) {
        super(x, y);
        this.protectionRadius = protectionRadius;
    }

    @Override
    public void tick(Board board) {
        if (!isAlive()) return;

        Potato target = findClosestMaturePotato(board, visionRadius);
        int newX = x;
        int newY = y;

        if (target != null) {
            newX += Integer.compare(target.getX(), x);
            newY += Integer.compare(target.getY(), y);
        } else {
            newX += random.nextInt(3) - 1;
            newY += random.nextInt(3) - 1;
        }

        board.moveEntity(this, newX, newY);

        Potato nearbyPotato = board.findPotatoNearby(x, y, 1);
        if (nearbyPotato != null && nearbyPotato.getMass() >= 5.0) {
            int targetX = nearbyPotato.getX();
            int targetY = nearbyPotato.getY();

            harvest(nearbyPotato);
            board.removeEntity(nearbyPotato);
            board.addScore(10);

            System.out.println("Farmer zbiera ziemniaka na pozycji (" + targetX + "," + targetY + ").");
            board.markAction();
        }

        Beetle nearbyBeetle = board.findBeetleNearby(x, y, protectionRadius);
        if (nearbyBeetle != null && nearbyBeetle.isAlive()) {
            int targetX = nearbyBeetle.getX();
            int targetY = nearbyBeetle.getY();

            board.addScore(1);
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
            board.addScore(15);
            board.removeEntity(nearbyFox);
            System.out.println("Farmer zabija lisa na pozycji (" + targetX + "," + targetY + ").");
            board.markAction();
        }
    }

    private Potato findClosestMaturePotato(Board board, int radius) {
        Potato closest = null;
        int minDistance = Integer.MAX_VALUE;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int checkX = x + dx;
                int checkY = y + dy;

                Entity e = board.getEntityAt(checkX, checkY);
                if (e instanceof Potato) {
                    Potato p = (Potato) e;
                    if (p.getMass() >= 5.0) {
                        int dist = Math.max(Math.abs(dx), Math.abs(dy));
                        if (dist < minDistance) {
                            minDistance = dist;
                            closest = p;
                        }
                    }
                }
            }
        }
        return closest;
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