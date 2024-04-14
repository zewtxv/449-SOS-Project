import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SOSGameTest {

    private GameLogic gameLogic;

    @BeforeEach
    void setUp() {
        gameLogic = new GameLogic();
        gameLogic.initializeGame("3x3", "Simple", "Player");
    }

    @Test
    void testInitialBoardIsEmpty() {
        char[][] board = gameLogic.getBoard();
        for (char[] row : board) {
            for (char cell : row) {
                assertEquals(' ', cell, "Board should be initialized with empty cells.");
            }
        }
    }

    @Test
    void testMakeMove() {
        assertTrue(gameLogic.makePlayerMove(0, 0), "Move should be successful.");
        assertEquals('S', gameLogic.getBoard()[0][0], "First move should be 'S'.");
    }
}
