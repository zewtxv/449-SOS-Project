import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameLogic {
    private char[][] board;
    private boolean isSTurn;
    private String gameMode;
    private String opponentType;
    private int sScore = 0, oScore = 0;
    private boolean gameOver = false;
    private List<List<Point>> sosCombinations = new ArrayList<>();
    private Random random = new Random();
    private GameLogger logger;

    public GameLogic(GameLogger logger) {
        this.logger = logger;
    }

    public void initializeGame(String boardSize, String gameMode, String opponentType) {
        int size = Integer.parseInt(boardSize.split("x")[0]);
        this.gameMode = gameMode;
        this.opponentType = opponentType;
        board = new char[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(board[i], ' ');
        }
        isSTurn = true;
        sScore = 0;
        oScore = 0;
        sosCombinations.clear();
        gameOver = false;
    }

    public boolean makePlayerMove(int row, int col) {
        if (board[row][col] != ' ') {
            return false;
        }
        makeMove(row, col);
        if (logger != null) {
            logger.logAction("Player move at (" + row + ", " + col + ")");
        }
        return true;
    }

    public void makeComputerMove() {
        if (gameOver) return;
        List<Point> emptyCells = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == ' ') {
                    emptyCells.add(new Point(i, j));
                }
            }
        }
        if (!emptyCells.isEmpty()) {
            Point move = emptyCells.get(random.nextInt(emptyCells.size()));
            makeMove(move.x, move.y);
            if (logger != null) {
                logger.logAction("Computer move at (" + move.x + ", " + move.y + ")");
            }
        }
    }

    private void makeMove(int row, int col) {
        board[row][col] = isSTurn ? 'S' : 'O';
        checkForSOS(row, col);
        isSTurn = !isSTurn;
        if (isGameOver()) {
            gameOver = true;
        }
    }

    private void checkForSOS(int row, int col) {
        char currentChar = board[row][col];
        if (currentChar != 'S') return;
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}, {0, -1}, {-1, 0}, {-1, -1}, {-1, 1}};
        for (int[] dir : directions) {
            int dRow = dir[0];
            int dCol = dir[1];

            int oRow = row + dRow;
            int oCol = col + dCol;
            int sRow = oRow + dRow;
            int sCol = oCol + dCol;

            if (isValid(oRow, oCol) && isValid(sRow, sCol) && board[oRow][oCol] == 'O' && board[sRow][sCol] == 'S') {
                sosCombinations.add(Arrays.asList(new Point(row, col), new Point(oRow, oCol), new Point(sRow, sCol)));
                sScore++;
                if ("Simple".equalsIgnoreCase(gameMode)) {
                    gameOver = true;
                    return;
                }
            }
        }
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    public boolean isGameOver() {
        if ("Simple".equals(gameMode) && gameOver) {
            return true;
        }

        for (char[] row : board) {
            for (char c : row) {
                if (c == ' ') return false;
            }
        }
        return true;
    }

    public char[][] getBoard() {
        return board;
    }

    public boolean isSTurn() {
        return isSTurn;
    }

    public String getGameMode() {
        return gameMode;
    }

    public int getSScore() {
        return sScore;
    }

    public int getOScore() {
        return oScore;
    }

    public List<List<Point>> getSOSCombinations() {
        return sosCombinations;
    }

    public void resetGame() {
        initializeGame(board.length + "x" + board.length, gameMode, opponentType);
    }

    public String getOpponentType() {
        return opponentType;
    }
}
