import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


 class SOSGameTest {

    @Test
     void testGameInitialization() {
        SOSGame sosGame = new SOSGame();
        assertNotNull(sosGame);
    }

    @Test
    void testStartButtonAction() {
        SOSGame sosGame = new SOSGame();
        assertNotNull(sosGame);
    }

    @Test
    void testMakingMoves() {
        SOSGame sosGame = new SOSGame();
        assertNotNull(sosGame);

        // Simulate starting the game
        sosGame.getStartButton().doClick();

        // Make a move
        sosGame.getGameLogic().makeMove(0, 0);

        // Test that the move was made successfully
        assertEquals('S', sosGame.getGameLogic().getBoard()[0][0]);
    }

    @Test
    void testInvalidMoves() {
        SOSGame sosGame = new SOSGame();
        assertNotNull(sosGame);

        // starting the game
        sosGame.getStartButton().doClick();

        // Make a legal move
        sosGame.getGameLogic().makeMove(0, 0);

        // Attempt to make an invalid move on an occupied cell
        try {
            sosGame.getGameLogic().makeMove(0, 0);
        }catch (Exception e){
            //using lambda here
        assertThrows(IllegalArgumentException.class, () -> {
            sosGame.getGameLogic().makeMove(0, 0);
        }
        );
        }

        // Test that the cell was not overwritten with an illegal move
        assertEquals('S', sosGame.getGameLogic().getBoard()[0][0]);
    }

    @Test
    void testTurnSwitching() {
        SOSGame sosGame = new SOSGame();
        assertNotNull(sosGame);

        // Simulate starting the game
        sosGame.getStartButton().doClick();

        // Make a move
        sosGame.getGameLogic().makeMove(0, 0);

        // Test that the turn has switched to the next player
        assertTrue(sosGame.getGameLogic().isOTurn());
    }


}
