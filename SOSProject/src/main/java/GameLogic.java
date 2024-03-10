public class GameLogic {
    private char[][] board;
    private boolean isSTurn;

    public void initializeGame(String boardSize, String gameMode) {
        int size = Integer.parseInt(boardSize.split("x")[0]);
        board = new char[size][size];
        isSTurn = true; // default Blue player starts the game
        // Create board with empty sqaures
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public void makeMove(int row, int col) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            throw new IllegalArgumentException("Invalid move: Out of bounds");
        }
        if (board[row][col] != ' ') {
            throw new IllegalArgumentException("Invalid move: Cell already occupied");
        }
        board[row][col] = isSTurn ? 'S' : 'O';

        //changing turns
        isSTurn = !isSTurn;
    }

    public char[][] getBoard() {
        return board;
    }

    public boolean isSTurn() {
        return isSTurn;
    }

    public boolean isOTurn() {
        return !isSTurn;
    }
}
