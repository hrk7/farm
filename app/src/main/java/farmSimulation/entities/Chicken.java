package farmSimulation.entities;

import java.util.Random;

public class Chicken extends Entity {
    private Random random = new Random();

    public Chicken(int x, int y) {
        super(x, y);
    }

    @Override
    public void tick(Board board) {
        if (!isAlive()) return;

        Beetle beetle = board.findBeetleNearby(x,y);
        if(beetle != null && beetle.isAlive()){
            System.out.println("Kura zjada stonkę na pozycji (" + beetle.getX() + "," + beetle.getY() + ")!");
            beetle.die();
        } else {
            Potato potato = board.findPotatoNearby(x, y);
            if(potato != null && potato.getMass() > 0){
                System.out.println("Brak stonki. Kura podjada ziemniaka na (" + potato.getX() + "," + potato.getY() + ")");
                potato.consume(0.5);
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