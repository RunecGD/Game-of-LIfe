package GameOfLife;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GameLife extends JFrame {
    private static final int CELL_SIZE = 40;
    private static final int CANVAS_WIDTH = 1240;
    private static final int CANVAS_HEIGHT = 830;
    ArrayList<Coord> RED_COORD = new ArrayList<>();
    ArrayList<Coord> WHITE_COORD = new ArrayList<>();
    private JPanel canvas;

    public GameLife() {
        setTitle("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        setLocationRelativeTo(null);
        int cellsPerRow = CANVAS_WIDTH / CELL_SIZE;
        int cellsPerCol = CANVAS_HEIGHT / CELL_SIZE;
        Ranges.setSize(new Coord(cellsPerRow, cellsPerCol));

        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCells(g);
            }
        };
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        canvas.setBackground(Color.WHITE);
        add(canvas, BorderLayout.CENTER);
    }

    private void drawCells(Graphics g) {
        Random random = new Random();
        double liveProbability = 0.005;

        for (Coord coord : Ranges.getAllCoords()) {
            if (random.nextDouble() < liveProbability) {
                g.setColor(Color.RED);
                RED_COORD.add(coord);
            } else {
                g.setColor(Color.WHITE);
                WHITE_COORD.add(coord);
            }
            g.fillRect(coord.x * CELL_SIZE, coord.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(coord.x * CELL_SIZE, coord.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    public void updateGame() {
        // Обновление состояния игры
        ArrayList<Coord> newRedCoords = new ArrayList<>();
        ArrayList<Coord> newWhiteCoords = new ArrayList<>();

        for (Coord coord : Ranges.getAllCoords()) {
            int redCount = 0;
            int whiteCount = 0;
            for (Coord around : Ranges.getCoordsAround(coord)) {
                if (RED_COORD.contains(around)) {
                    redCount++;
                } else {
                    whiteCount++;
                }
            }

            // Применение правил игры
            if (RED_COORD.contains(coord)) {
                if (redCount < 2 || redCount > 3) {
                    newWhiteCoords.add(coord); // Клетка умирает
                } else {
                    newRedCoords.add(coord); // Клетка выживает
                }
            } else {
                if (redCount == 3) {
                    newRedCoords.add(coord); // Новая клетка рождается
                } else {
                    newWhiteCoords.add(coord); // Клетка остается мертвой
                }
            }
        }

        RED_COORD = newRedCoords;
        WHITE_COORD = newWhiteCoords;
        canvas.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameLife game = new GameLife();
            game.setVisible(true);
            new Timer(6000, e -> game.updateGame()).start();
        });
    }
}