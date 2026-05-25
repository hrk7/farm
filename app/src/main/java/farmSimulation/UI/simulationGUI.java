package farmSimulation.UI;

import farmSimulation.base.Board;
import farmSimulation.entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class simulationGUI extends JFrame {
    private Board board;
    private GridPanel gridPanel;
    private Timer timer;
    private JLabel scoreLabel;

    private Image farmerImg;
    private Image foxImg;
    private Image chickenImg;
    private Image beetleImg;
    private Image potatoImg;
    private Image potatoBigImg;

    public simulationGUI(Board board) {
        this.board = board;

        try {
            farmerImg = ImageIO.read(new File("app/src/main/java/farmSimulation/images/farmer.png"));
            foxImg = ImageIO.read(new File("app/src/main/java/farmSimulation/images/fox.png"));
            chickenImg = ImageIO.read(new File("app/src/main/java/farmSimulation/images/chicken.png"));
            beetleImg = ImageIO.read(new File("app/src/main/java/farmSimulation/images/beetle.png"));
            potatoImg = ImageIO.read(new File("app/src/main/java/farmSimulation/images/potato.png"));
            potatoBigImg = ImageIO.read(new File("app/src/main/java/farmSimulation/images/potatoBig.png"));
        } catch (IOException e) {
            System.out.println("Błąd ładowania obrazków.");
            e.printStackTrace();
        }

        setTitle("simulation v2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scoreLabel = new JLabel("Wynik: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);

        gridPanel = new GridPanel();
        add(gridPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton stepButton = new JButton("Następny krok");
        JButton autoButton = new JButton("Auto-play");

        JLabel speedLabel = new JLabel("Prędkość (mnożnik): ");
        JTextField speedField = new JTextField("1", 3);
        speedField.setToolTipText("Wpisz mnożnik i naciśnij Enter");

        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.nextTick();
                scoreLabel.setText("Wynik: " + board.getScore());
                gridPanel.repaint();
            }
        });

        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.nextTick();
                scoreLabel.setText("Wynik: " + board.getScore());
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
                    speedField.setEnabled(true);
                } else {
                    timer.start();
                    autoButton.setText("Stop");
                    stepButton.setEnabled(false);
                }
            }
        });

        speedField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double multiplier = Double.parseDouble(speedField.getText().replace(',', '.'));

                    if (multiplier <= 0) {
                        multiplier = 1.0;
                        speedField.setText("1");
                    }

                    int newDelay = (int) (500 / multiplier);
                    timer.setDelay(Math.max(1, newDelay));

                } catch (NumberFormatException ex) {
                    speedField.setText("1");
                    timer.setDelay(500);
                }
            }
        });

        controlPanel.add(stepButton);
        controlPanel.add(autoButton);
        controlPanel.add(speedLabel);
        controlPanel.add(speedField);
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
            Image imgToDraw = null;

            if (entity instanceof Farmer) {
                imgToDraw = farmerImg;
            } else if (entity instanceof Fox) {
                imgToDraw = foxImg;
            } else if (entity instanceof Chicken) {
                imgToDraw = chickenImg;
            } else if (entity instanceof Beetle) {
                imgToDraw = beetleImg;
            } else if (entity instanceof Potato) {
                Potato p = (Potato) entity;
                if (p.getMass() >= 5.0) {
                    imgToDraw = potatoBigImg;
                } else {
                    imgToDraw = potatoImg;
                }
            }

            if (imgToDraw != null) {
                g.drawImage(imgToDraw, px, py, CELL_SIZE, CELL_SIZE, null);
            }
        }
    }
}