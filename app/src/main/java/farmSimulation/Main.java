package farmSimulation;

import farmSimulation.base.Board;
import farmSimulation.entities.*;
import farmSimulation.UI.simulationGUI;
import javax.swing.SwingUtilities;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> spawnConfig = new HashMap<>();
        spawnConfig.put("potatoes", 1);
        spawnConfig.put("beetles", 1);
        spawnConfig.put("chickens", 1);
        spawnConfig.put("foxes", 1);
        spawnConfig.put("farmers", 1);

        Board board = new Board(10, 10, 4);

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

        SwingUtilities.invokeLater(() -> {
            simulationGUI gui = new simulationGUI(board);
            gui.setVisible(true);
        });
    }
}