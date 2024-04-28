import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SOSGame extends JFrame {
    private GameLogic gameLogic;
    private JButton startButton;
    private JPanel overlayPanel;
    private JComboBox<String> opponentTypeComboBox;

    public SOSGame() {
        setTitle("SOS Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        gameLogic = new GameLogic();

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
                System.out.println("Selected Game Mode: " + selectedGameMode);
                String opponentType = (String) opponentTypeComboBox.getSelectedItem();
                gameLogic.initializeGame(selectedBoardSize, selectedGameMode, opponentType);
                if ("Computer vs. Computer".equals(opponentType)) {
                    new Thread(SOSGame.this::autoplayGame).start();
                } else {
                    updateGameBoard();
                }
            }
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
        JOptionPane.showMessageDialog(this, message);
        gameLogic.resetGame();
        updateGameBoard();
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
                Thread.sleep(500); //simulate time between moves
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
            // Update the game board on the EDT
            SwingUtilities.invokeLater(this::updateGameBoard);
        }
        // Display game over message on the EDT
        SwingUtilities.invokeLater(this::displayGameOver);
    }



}
