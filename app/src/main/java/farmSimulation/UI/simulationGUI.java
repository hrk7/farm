package farmSimulation.UI;

import farmSimulation.base.Board;
import farmSimulation.entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class simulationGUI extends JFrame {
    private Board board;
    private GridPanel gridPanel;
    private Timer timer;

    public simulationGUI(Board board) {
        this.board = board;

        setTitle("simulation v2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gridPanel = new GridPanel();
        add(gridPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton stepButton = new JButton("Następny krok");
        JButton autoButton = new JButton("Auto-play");

        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.nextTick();
                gridPanel.repaint();
            }
        });

        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.nextTick();
                gridPanel.repaint();
            }
        });

        autoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer.isRunning()) {
                    timer.stop();
                    autoButton.setText("Auto-play");
                    stepButton.setEnabled(true);
                } else {
                    timer.start();
                    autoButton.setText("Stop");
                    stepButton.setEnabled(false);
                }
            }
        });

        controlPanel.add(stepButton);
        controlPanel.add(autoButton);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private class GridPanel extends JPanel {
        private final int CELL_SIZE = 50;

        public GridPanel() {
            setPreferredSize(new Dimension(board.getWidth() * CELL_SIZE, board.getHeight() * CELL_SIZE));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (int y = 0; y < board.getHeight(); y++) {
                for (int x = 0; x < board.getWidth(); x++) {
                    int px = x * CELL_SIZE;
                    int py = y * CELL_SIZE;

                    g.setColor(new Color(144, 238, 144));
                    g.fillRect(px, py, CELL_SIZE, CELL_SIZE);

                    g.setColor(Color.GRAY);
                    g.drawRect(px, py, CELL_SIZE, CELL_SIZE);

                    Entity entity = board.getEntityAt(x, y);
                    if (entity != null) {
                        drawEntity(g, entity, px, py);
                    }
                }
            }
        }

        private void drawEntity(Graphics g, Entity entity, int px, int py) {
            if (entity instanceof Farmer) g.setColor(Color.BLUE);
            else if (entity instanceof Fox) g.setColor(Color.RED);
            else if (entity instanceof Chicken) g.setColor(Color.WHITE);
            else if (entity instanceof Beetle) g.setColor(Color.BLACK);
            else if (entity instanceof Potato) g.setColor(new Color(139, 69, 19));

            g.fillOval(px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10);

            g.setColor(Color.WHITE);
            if (entity instanceof Chicken) g.setColor(Color.BLACK);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(String.valueOf(entity.getSymbol()), px + CELL_SIZE / 2 - 8, py + CELL_SIZE / 2 + 8);
        }
    }
}