import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;

public class SOSGame extends JFrame implements GameLogger {
    private GameLogic gameLogic;
    private JButton startButton;
    private JButton replayButton;
    private JPanel overlayPanel;
    private JComboBox<String> opponentTypeComboBox;
    private FileWriter logWriter;

    public SOSGame() {
        setTitle("SOS Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            logWriter = new FileWriter("game_log.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        gameLogic = new GameLogic(this);

        JPanel optionsPanel = new JPanel();
        JLabel boardSizeLabel = new JLabel("Board Size:");
        JComboBox<String> boardSizeComboBox = new JComboBox<>(new String[]{"3x3", "4x4", "5x5"});
        JLabel gameModeLabel = new JLabel("Game Mode:");
        JComboBox<String> gameModeComboBox = new JComboBox<>(new String[]{"Simple", "General"});
        JLabel opponentTypeLabel = new JLabel("Opponent:");
        opponentTypeComboBox = new JComboBox<>(new String[]{"Player", "Computer", "Computer vs. Computer"});

        startButton = new JButton("Start Game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBoardSize = (String) boardSizeComboBox.getSelectedItem();
                String selectedGameMode = (String) gameModeComboBox.getSelectedItem();
                String opponentType = (String) opponentTypeComboBox.getSelectedItem();
                gameLogic.initializeGame(selectedBoardSize, selectedGameMode, opponentType);
                if ("Computer vs. Computer".equals(opponentType)) {
                    new Thread(SOSGame.this::autoplayGame).start();
                } else {
                    updateGameBoard();
                }
            }
        });

        replayButton = new JButton("Replay Game");
        replayButton.addActionListener(e -> {
            gameLogic.resetGame();
            updateGameBoard();
        });

        optionsPanel.add(boardSizeLabel);
        optionsPanel.add(boardSizeComboBox);
        optionsPanel.add(gameModeLabel);
        optionsPanel.add(gameModeComboBox);
        optionsPanel.add(opponentTypeLabel);
        optionsPanel.add(opponentTypeComboBox);
        optionsPanel.add(startButton);

        add(optionsPanel, BorderLayout.NORTH);
        overlayPanel = new JPanel();
        overlayPanel.setOpaque(false);
        add(overlayPanel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    logWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void logAction(String action) {
        try {
            logWriter.write(action + System.lineSeparator());
            logWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateGameBoard() {
        getContentPane().removeAll();
        JPanel boardPanel = new JPanel(new GridLayout(gameLogic.getBoard().length, gameLogic.getBoard().length));
        boardPanel.setPreferredSize(new Dimension(500, 500));

        char[][] board = gameLogic.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                JButton cellButton = new JButton(board[i][j] == ' ' ? " " : Character.toString(board[i][j]));
                cellButton.setFont(new Font("Arial", Font.BOLD, 24));
                cellButton.setPreferredSize(new Dimension(50, 50));
                int row = i;
                int col = j;
                cellButton.addActionListener(e -> {
                    if (gameLogic.makePlayerMove(row, col)) {
                        cellButton.setText(Character.toString(gameLogic.getBoard()[row][col]));
                        if (gameLogic.isGameOver()) {
                            displayGameOver();
                        } else if ("Computer".equals(gameLogic.getOpponentType()) && !gameLogic.isSTurn()) {
                            SwingUtilities.invokeLater(() -> {
                                gameLogic.makeComputerMove();
                                updateGameBoard();
                            });
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid move: Cell already occupied");
                    }
                });
                boardPanel.add(cellButton);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
        JLabel turnLabel = new JLabel("It's " + (gameLogic.isSTurn() ? "S" : "O") + "'s turn");
        add(turnLabel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void displayGameOver() {
        String message = "Game Over! ";
        if ("Simple".equals(gameLogic.getGameMode())) {
            message += "The first SOS was made. ";
        }
        message += gameLogic.getSScore() > gameLogic.getOScore() ? "S wins!" : gameLogic.getSScore() < gameLogic.getOScore() ? "O wins!" : "It's a draw!";

        // Custom dialog with a replay option
        Object[] options = {"Replay"};
        int choice = JOptionPane.showOptionDialog(this, message, "Game Over",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            gameLogic.resetGame();
            updateGameBoard();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SOSGame sosGame = new SOSGame();
            sosGame.setVisible(true);
        });
    }

    private void autoplayGame() {
        while (!gameLogic.isGameOver()) {
            gameLogic.makeComputerMove();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
            SwingUtilities.invokeLater(this::updateGameBoard);
        }
        SwingUtilities.invokeLater(this::displayGameOver);
    }
}

interface GameLogger {
    void logAction(String action);
}
