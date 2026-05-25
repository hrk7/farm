package farmSimulation;

import farmSimulation.base.Board;
import farmSimulation.entities.*;
import farmSimulation.UI.simulationGUI; // Импорт нового окна
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Board board = new Board(16, 16);

        board.addEntity(new Potato(2, 2, 3.0));
        board.addEntity(new Potato(5, 5, 4.0));
        board.addEntity(new Beetle(2, 3));
        board.addEntity(new Chicken(3, 3));
        board.addEntity(new Fox(7, 7));
        board.addEntity(new Farmer(5, 4, 2.0));

        SwingUtilities.invokeLater(() -> {
            simulationGUI gui = new simulationGUI(board);
            gui.setVisible(true);
        });
    }
}