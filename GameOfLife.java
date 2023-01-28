import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameOfLife extends JFrame {
    private final int ROWS = 50;
    private final int COLS = 50;
    private final int CELL_SIZE = 10;
    private boolean started = false;

    private boolean[][] currentGen;
    private boolean[][] nextGen;
    private JButton startButton;
    private JButton resetButton;
    private JPanel gridPanel;
    private Timer timer;

    public GameOfLife() {
        currentGen = new boolean[ROWS][COLS];
        nextGen = new boolean[ROWS][COLS];

        startButton = new JButton("Start");
        resetButton = new JButton("Reset");

        gridPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLS; col++) {
                        if (currentGen[row][col]) {
                            g.setColor(Color.BLACK);
                            g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        } else {
                            g.setColor(Color.WHITE);
                            g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        }
                    }
                }
            }
        };

        gridPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int col = e.getX() / CELL_SIZE;
                int row = e.getY() / CELL_SIZE;
                currentGen[row][col] = !currentGen[row][col];
                gridPanel.repaint();
            }
        });

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(started){
                    timer.stop();
                    started = false;
                    for (int row = 0; row < ROWS; row++) {
                        for (int col = 0; col < COLS; col++) {
                            currentGen[row][col] = false;
                        }
                    }
                    gridPanel.repaint();
                }
                else{
                    timer.start();
                    started = true;
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                started = false;
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLS; col++) {
                        currentGen[row][col] = false;
                    }
                }
                gridPanel.repaint();
            }
        });

        timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
                gridPanel.repaint();
            }
        });

        setLayout(new BorderLayout());
        add(startButton, BorderLayout.NORTH);
        add(resetButton, BorderLayout.SOUTH);
        add(gridPanel, BorderLayout.CENTER);
        setSize(COLS * CELL_SIZE, ROWS * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }


    private void update() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int liveNeighbors = countLiveNeighbors(row, col);
                if (currentGen[row][col]) {
                    if (liveNeighbors < 2 || liveNeighbors > 3) {
                        nextGen[row][col] = false;
                    } else {
                        nextGen[row][col] = true;
                    }
                } else {
                    if (liveNeighbors == 3) {
                        nextGen[row][col] = true;
                    } else {
                        nextGen[row][col] = false;
                    }
                }
            }
        }
        currentGen = nextGen.clone();
    }

    private int countLiveNeighbors(int row, int col) {
        int liveNeighbors = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    int neighborRow = (row + i + ROWS) % ROWS;
                    int neighborCol = (col + j + COLS) % COLS;
                    if (currentGen[neighborRow][neighborCol]) {
                        liveNeighbors++;
                    }
                }
            }
        }
        return liveNeighbors;
    }

    public static void main(String[] args) {
        new GameOfLife();
    }
}