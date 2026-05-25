package farmSimulation.entities;

import farmSimulation.base.Board;

import java.util.Random;

public class Chicken extends Entity {
    private Random random = new Random();

    public Chicken(int x, int y) {
        super(x, y);
    }

    @Override
    public void tick(Board board) {
        if (!isAlive()) return;

        Beetle nearbyBeetle = board.findBeetleNearby(x,y,1);
        if(nearbyBeetle != null && nearbyBeetle.isAlive()){
            System.out.println("Kura zjada stonkę na pozycji (" + nearbyBeetle.getX() + "," + nearbyBeetle.getY() + ").");
            nearbyBeetle.die();
            board.removeEntity(nearbyBeetle);
            board.markAction();
        } else {
            Potato nearbyPotato = board.findPotatoNearby(x,y,1);
            if(nearbyPotato != null && nearbyPotato.getMass() > 0){
                System.out.println("Brak stonki. Kura podjada ziemniaka na (" + nearbyPotato.getX() + "," + nearbyPotato.getY() + ").");
                nearbyPotato.consume(1.5);
                if(!nearbyPotato.isAlive()){
                    board.removeEntity(nearbyPotato);
                }
                board.markAction();
            }
            else {
                int newX = x + random.nextInt(3) - 1;
                int newY = y + random.nextInt(3) - 1;
                board.moveEntity(this, newX, newY);
            }
        }
    }

    @Override
    public char getSymbol() {
        return 'C';
    }
}