package farmSimulation.base;

import java.util.ArrayList;
import java.util.List;

import farmSimulation.entities.Potato;
import farmSimulation.entities.Beetle;
import farmSimulation.entities.Fox;
import farmSimulation.entities.Chicken;
import farmSimulation.entities.Farmer;

public class Engine {
    private List<Potato> potatoes = new ArrayList<>();
    private List<Beetle> beetles = new ArrayList<>();
    private List<Fox> foxes = new ArrayList<>();
    private List<Chicken> chickens = new ArrayList<>();
    private List<Farmer> farmers = new ArrayList<>();
    private Board board;

    private int currentTick = 0;

    public void addPotato(Potato p) {
        potatoes.add(p);
    }

    public void addBeetle(Beetle b) {
        beetles.add(b);
    }

    public void addFox(Fox fo) {
        foxes.add(fo);
    }

    public void addChicken(Chicken c) {
        chickens.add(c);
    }

    public void addFarmer(Farmer fa) {
        farmers.add(fa);
    }

    public void nextTick() {
        currentTick++;

        runGrowthAndPestPhase(); // фаза роста картофеля и атаки жуков
        runThreatPhase(); // фаза атаки лис
        runChickenResponsePhase(); // фаза реакции чикенов
        runFarmerPhase(); // фаза реакции фермера

        cleanUpDeadEntities(); // удаление мертвых субъектов
    }

    private void runGrowthAndPestPhase() {
        for (Potato potato : potatoes) {
            potato.tick(board);
        }

        for (Beetle beetle : beetles) {
            if (beetle.isAlive()) {
                beetle.tick(board);
            }
        }
    }

    private void runThreatPhase() {
        for (Fox fox : foxes) {
            fox.tick(board);
        }
    }

    private void runChickenResponsePhase() {
        for (Chicken chicken : chickens) {
            chicken.tick(board);
        }
    }

    private void runFarmerPhase() {
        for (Farmer farmer : farmers) {
            farmer.tick(board);
        }
    }

    private void cleanUpDeadEntities() {
        beetles.removeIf(beetle -> !beetle.isAlive());
        chickens.removeIf(chicken -> !chicken.isAlive());
        foxes.removeIf(fox -> !fox.isAlive());
        potatoes.removeIf(potato -> potato.getMass() <= 0);
    }
}
