package farmSimulation.entities;

import farmSimulation.base.Board;

public class Farmer extends Entity {
    private double protectionRadius;

    public Farmer(int x, int y, double protectionRadius) {
        super(x, y);
        this.protectionRadius = protectionRadius;
    }

    @Override
    public void tick(Board board) {
        if (!isAlive()) return;

        Potato nearbyPotato = board.findPotatoNearby(x, y);
        if (nearbyPotato != null && nearbyPotato.getMass() > 5.0) {
            harvest(nearbyPotato);
        }

        Beetle nearbyBeetle = board.findBeetleNearby(x, y);
        if (nearbyBeetle != null && nearbyBeetle.isAlive()) {
            nearbyBeetle.die();
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
