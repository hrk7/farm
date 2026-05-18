package farmSimulation.entities;
import java.util.Random;

public class Beetle extends Entity{
    private Random random = new Random();

    public Beetle(int x, int y){
       super(x, y);
    }

    @Override
    public void tick(){
        if(!isAlive()) return;
    }

}
