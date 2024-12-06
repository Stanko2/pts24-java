package sk.uniba.fmph.dcs.game_board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResourceSourceTest {
    private ResourceSource resourceSource;
    private Player mockPlayer;
    private PlayerOrder mockPlayerOrder;
    private PlayerBoard mockPlayerBoard;

    @BeforeEach
    void setUp() {
        mockPlayerOrder = new PlayerOrder(1, 2);
        mockPlayerBoard = new PlayerBoard();
        mockPlayer = new Player(mockPlayerOrder, new PlayerBoardGameBoardFacade(mockPlayerBoard));

        resourceSource = new ResourceSource(Effect.WOOD, 4);
    }

    @Test
    void testPlaceFigures() {
        assertFalse(resourceSource.placeFigures(mockPlayer, 3));
    }

    @Test
    void testTryToPlaceFiguresSuccess() {
        // Successful placement of figures within the limit
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, resourceSource.tryToPlaceFigures(mockPlayer, 3));
    }

    @Test
    void testTryToPlaceFiguresFailureWhenExceedsLimit() {
        // Exceeding the limit of 7 figures
        resourceSource.tryToPlaceFigures(mockPlayer, 3);
        assertEquals(HasAction.NO_ACTION_POSSIBLE, resourceSource.tryToPlaceFigures(mockPlayer, 5));
    }

    @Test
    void testMakeActionSuccess() {
        resourceSource.tryToPlaceFigures(mockPlayer, 2);

        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, resourceSource.makeAction(mockPlayer, null, null));
    }

    @Test
    void testMakeActionFailure() {
        assertEquals(ActionResult.FAILURE, resourceSource.makeAction(mockPlayer, null, null));
    }

    @Test
    void testSkipAction() {
        resourceSource.tryToPlaceFigures(mockPlayer, 2);
        assertTrue(resourceSource.skipAction(mockPlayer));
        assertEquals(resourceSource.tryToPlaceFigures(mockPlayer, 1), HasAction.WAITING_FOR_PLAYER_ACTION);
    }

    @Test
    void testTryToMakeAction() {
        resourceSource.tryToPlaceFigures(mockPlayer, 2);
        HasAction result = resourceSource.tryToMakeAction(mockPlayer);
        assertNotEquals(HasAction.NO_ACTION_POSSIBLE, result);
    }

    @Test
    void testTryToMakeActionNoFigures() {
        assertEquals(HasAction.NO_ACTION_POSSIBLE, resourceSource.tryToMakeAction(mockPlayer));
    }

    @Test
    void testNewTurn() {
        assertFalse(resourceSource.newTurn());
    }

    @Test
    void testState() {
        assertNotNull(resourceSource.state());
    }
}
