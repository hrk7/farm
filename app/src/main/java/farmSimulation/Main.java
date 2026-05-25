package farmSimulation;

import farmSimulation.base.Board;
import farmSimulation.entities.*;
import farmSimulation.UI.simulationGUI;
import javax.swing.SwingUtilities;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Board board = new Board(16, 16);

        Map<String, Integer> spawnConfig = new HashMap<>();
        spawnConfig.put("potatoes", 10);
        spawnConfig.put("beetles", 5);
        spawnConfig.put("chickens", 5);
        spawnConfig.put("foxes", 2);
        spawnConfig.put("farmers", 2);

        for(int i = 0; i < spawnConfig.get("potatoes"); i++){
            board.spawnRandomEntity(new Potato(0, 0, 1.0));
        }
        for(int i = 0; i < spawnConfig.get("beetles"); i++){
            board.spawnRandomEntity(new Beetle(0, 0));
        }
        for(int i = 0; i < spawnConfig.get("chickens"); i++){
            board.spawnRandomEntity(new Chicken(0, 0));
        }
        for(int i = 0; i < spawnConfig.get("foxes"); i++){
            board.spawnRandomEntity(new Fox(0, 0));
        }
        for(int i = 0; i < spawnConfig.get("farmers"); i++){
            board.spawnRandomEntity(new Farmer(0, 0, 1));
        }

        /*
        board.addEntity(new Potato(2, 2, 3.0));
        board.addEntity(new Potato(5, 5, 4.0));
        board.addEntity(new Beetle(2, 3));
        board.addEntity(new Chicken(3, 3));
        board.addEntity(new Fox(7, 7));
        board.addEntity(new Farmer(5, 4, 2));
        */
        SwingUtilities.invokeLater(() -> {
            simulationGUI gui = new simulationGUI(board);
            gui.setVisible(true);
        });
    }
}