import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SOSGame extends JFrame {
    private GameLogic gameLogic;
    private JButton startButton;

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
        startButton = new JButton("Start Game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBoardSize = (String) boardSizeComboBox.getSelectedItem();
                String selectedGameMode = (String) gameModeComboBox.getSelectedItem();
                gameLogic.initializeGame(selectedBoardSize, selectedGameMode);
                updateGameBoardUI();
            }
        });

        optionsPanel.add(boardSizeLabel);
        optionsPanel.add(boardSizeComboBox);
        optionsPanel.add(gameModeLabel);
        optionsPanel.add(gameModeComboBox);
        optionsPanel.add(startButton);

        add(optionsPanel, BorderLayout.NORTH);

    }

    //FOR UNIT TESTING
    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public JButton getStartButton() {
        return startButton;
    }

    private void updateGameBoardUI() {
        // Remove existing UI
        getContentPane().removeAll();

        // For displaying game board
        JPanel boardPanel = new JPanel(new GridLayout(gameLogic.getBoard().length, gameLogic.getBoard().length));
        char[][] board = gameLogic.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                JButton cellButton = new JButton(Character.toString(board[i][j]));
                cellButton.setPreferredSize(new Dimension(50, 50));
                // Add action listener to handle cell clicks
                int row = i;
                int col = j;
                cellButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Check if it's the player's turn
                        if ((gameLogic.isSTurn() && board[row][col] == ' ') || (gameLogic.isOTurn() && board[row][col] == ' ')) {
                            // Make a move
                            gameLogic.makeMove(row, col);
                            // Update UI
                            updateGameBoardUI();
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid move: Cell already occupied");
                        }
                    }
                });
                boardPanel.add(cellButton);
            }
        }

        // Add the board panel to the content pane
        getContentPane().add(boardPanel, BorderLayout.CENTER);

        // Add labels to indicate whose turn it is
        JLabel turnLabel = new JLabel("It's " + (gameLogic.isSTurn() ? "S" : "O") + "'s turn");
        getContentPane().add(turnLabel, BorderLayout.SOUTH);

        // Repaint the frame to reflect the changes
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SOSGame sosGame = new SOSGame();
            sosGame.setVisible(true);
        });
    }

}


