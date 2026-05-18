package farmSimulation.entities;

import farmSimulation.base.Board;

public class Potato extends Entity{
    private double mass;
    private final double growthRate = 0.5;

    public Potato(int x, int y, double initialMass){
        super(x, y);
        this.mass = initialMass;
    }

    @Override
    public void tick(Board board){
        if(mass > 0){
            mass += growthRate;
        }
    }

    public void consume(double amount){
        this.mass = Math.max(0, this.mass - amount);
        if(this.mass <= 0){
            die();
        }
    }

    public double getMass(){
        return mass;
    }

    @Override
    public char getSymbol() { return 'o'; };
}