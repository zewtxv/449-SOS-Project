import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SOSGameTest {

    private GameLogic gameLogic;
    private StringBuilder logOutput;

    @BeforeEach
    void setUp() {
        logOutput = new StringBuilder();
        GameLogger mockLogger = action -> logOutput.append(action).append("\n");
        gameLogic = new GameLogic(mockLogger);
        gameLogic.initializeGame("3x3", "Simple", "Player");
    }

    @Test
    void testInitialBoardIsEmpty() {
        char[][] board = gameLogic.getBoard();
        for (char[] row : board) {
            for (char cell : row) {
                assertEquals(' ', cell);
            }
        }
    }

    @Test
    void testMakePlayerMove() {
        assertTrue(gameLogic.makePlayerMove(0, 0));
        assertEquals('S', gameLogic.getBoard()[0][0]);
        assertTrue(logOutput.toString().contains("Player move at (0, 0)"));
    }

    @Test
    void testInvalidMove() {
        gameLogic.makePlayerMove(0, 0);
        assertFalse(gameLogic.makePlayerMove(0, 0));
        assertTrue(logOutput.toString().contains("Player move at (0, 0)"));
    }

    @Test
    void testGameOverBySOS() {
        gameLogic.makePlayerMove(0, 0);
        gameLogic.makePlayerMove(1, 0);
        gameLogic.makePlayerMove(0, 1);
        gameLogic.makePlayerMove(1, 1);
        gameLogic.makePlayerMove(2, 1);
        assertTrue(gameLogic.isGameOver());
    }


    @Test
    void testGameReset() {
        gameLogic.makePlayerMove(0, 0);
        gameLogic.resetGame();
        assertEquals(' ', gameLogic.getBoard()[0][0]);
        assertTrue(logOutput.toString().contains("Player move at (0, 0)"));
    }

    @Test
    void testLoggingMultipleActions() {
        gameLogic.makePlayerMove(0, 0);  // S
        gameLogic.makePlayerMove(0, 1);  // O
        assertEquals('O', gameLogic.getBoard()[0][1]);
        assertTrue(logOutput.toString().contains("Player move at (0, 0)"));
        assertTrue(logOutput.toString().contains("Player move at (0, 1)"));
    }
}
