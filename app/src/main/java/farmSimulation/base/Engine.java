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
            potato.tick();
        }

        for (Beetle beetle : beetles) {
            if (beetle.isAlive()) {
                // логика поедания жуком картофеля beetle.eat(targetPotato)
            }
        }
    }

    private void runThreatPhase() {
        for (Fox fox : foxes) {
            // логика поиска курицы и атаки в случае нахождения fox.hunt(targetChicken);
        }
    }

    private void runChickenResponsePhase() {
        for (Chicken chicken : chickens) {
            if (chicken.isAlive()) {
                // логика взаимодействия куры с жуками или картофелем chicken.interact(targetBeetle, targetPotato);
                // +логика побега если рядом лиса
            }
        }
    }

    private void runFarmerPhase() {
        for (Farmer farmer : farmers) {
            // логика защиты farmer.protect(...) и сбора урожая farmer.harvest(...)
        }
    }

    private void cleanUpDeadEntities() {
        beetles.removeIf(beetle -> !beetle.isAlive());
        chickens.removeIf(chicken -> !chicken.isAlive());
        potatoes.removeIf(potato -> potato.getMass() <= 0);
    }

    public int getCurrentTick() {
        return currentTick;
    }
}
