package farmSimulation.entities;
import farmSimulation.base.Board;

import java.util.Random;

public class Beetle extends Entity{
    private Random random = new Random();

    public Beetle(int x, int y){
       super(x, y);
    }

    @Override
    public void tick(Board board){
        if(!isAlive()) return;

        Potato nearbyPotato = board.findPotatoNearby(x, y, 1);
        if(nearbyPotato != null){
           nearbyPotato.consume(1.0);
           if(!nearbyPotato.isAlive()){
               board.removeEntity(nearbyPotato);
           }
           System.out.println("Stonka żeruje na ziemniaku na pozycji (" + x + "," + y + ").");
        } else {
           int newX = x + random.nextInt(3) - 1;
           int newY = y + random.nextInt(3) - 1;
           board.moveEntity(this, newX, newY);
        }
    }
    @Override
    public char getSymbol() { return 'B'; }
}
