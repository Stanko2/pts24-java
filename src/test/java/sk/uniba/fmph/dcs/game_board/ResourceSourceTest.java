package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;


import static org.junit.Assert.*;


public class ResourceSourceTest {

    @Test
    public void testPlaceFigures() {
        var t = new ResourceSource(Effect.CLAY, 2);
        Player player1 = new Player(new PlayerOrder(1, 1), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        Player player2 = new Player(new PlayerOrder(2, 2), null);
        var ret = t.placeFigures(player1, 2);
        assertFalse(ret);
        ret = t.placeFigures(player1, 1);
        assertTrue(ret);
        ret = t.placeFigures(player1, 1);
        assertFalse(ret);
        ret = t.placeFigures(player2, 1);
        assertTrue(ret);
    }

    @Test
    public void testTryToPlaceFiguresSuccess() {
        var t = new ResourceSource(Effect.CLAY, 2);
        Player player1 = new Player(new PlayerOrder(1, 1), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, t.tryToPlaceFigures(player1, 3));
    }

    @Test
    public void testTryToPlaceFiguresFailureWhenExceedsLimit() {
        var t = new ResourceSource(Effect.CLAY, 2);
        Player player1 = new Player(new PlayerOrder(1, 1), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        t.tryToPlaceFigures(player1, 3);
        assertEquals(HasAction.NO_ACTION_POSSIBLE, t.tryToPlaceFigures(player1, 8));
    }

    @Test
    public void testMakeActionSuccess() {
        var t = new ResourceSource(Effect.CLAY, 2);
        Player player1 = new Player(new PlayerOrder(1, 1), new PlayerBoardGameBoardFacade(new PlayerBoard()));

        t.tryToPlaceFigures(player1, 2);
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, t.makeAction(player1, null, null));
    }

    @Test
    public void testMakeActionFailure() {
        var t = new ResourceSource(Effect.CLAY, 2);
        Player player1 = new Player(new PlayerOrder(1, 1), new PlayerBoardGameBoardFacade(new PlayerBoard()));

        assertEquals(ActionResult.FAILURE, t.makeAction(player1, null, null));
    }

    @Test
    public void testSkipAction() {
        var t = new ResourceSource(Effect.CLAY, 2);
        Player player1 = new Player(new PlayerOrder(1, 1), null);

        t.tryToPlaceFigures(player1, 2);
        assertTrue(t.skipAction(player1));
        assertEquals(t.tryToPlaceFigures(player1, 1), HasAction.WAITING_FOR_PLAYER_ACTION);
    }

    @Test
    public void testTryToMakeAction() {
        var t = new ResourceSource(Effect.CLAY, 2);
        Player player1 = new Player(new PlayerOrder(1, 1), new PlayerBoardGameBoardFacade(new PlayerBoard()));


        t.tryToPlaceFigures(player1, 2);
        HasAction result = t.tryToMakeAction(player1);
        assertNotEquals(HasAction.NO_ACTION_POSSIBLE, result);
    }

    @Test
    public void testTryToMakeActionNoFigures() {
        var t = new ResourceSource(Effect.CLAY, 2);
        Player player1 = new Player(new PlayerOrder(1, 1), new PlayerBoardGameBoardFacade(new PlayerBoard()));

        assertEquals(HasAction.NO_ACTION_POSSIBLE, t.tryToMakeAction(player1));
    }
}
