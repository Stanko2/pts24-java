package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class CivilizationCardPlaceTest {

    @org.junit.Test
    public void test_placeFigures() {
        var t = new CivilizationCardPlace(new CivilizationCardDeck(new ArrayList<>()));
        Player player1 = new Player(new PlayerOrder(1, 1), null);
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

    @org.junit.Test
    public void test_makeAction() {
        var resources = new ArrayList<Effect>();
        resources.add(Effect.WOOD);
        var cards = new ArrayList<CivilisationCard>();
        cards.add(new CivilisationCard(new ImmediateEffect[]{ImmediateEffect.FOOD}, new EndOfGameEffect[] {EndOfGameEffect.SHAMAN}));
        var t = new CivilizationCardPlace(new CivilizationCardDeck(new ArrayList<>()));
        Player player1 = new Player(new PlayerOrder(1, 1), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        Player player2 = new Player(new PlayerOrder(2, 2), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        t.placeFigures(player1, 1);
        var ret = t.makeAction(player2, new Effect[] { Effect.WOOD }, new Effect[] {});
        assertEquals(ActionResult.FAILURE, ret);
        ret = t.makeAction(player1, new Effect[] { Effect.WOOD }, new Effect[] {});
        assertEquals(ActionResult.ACTION_DONE, ret);
        ret = t.makeAction(player1, new Effect[] {}, new Effect[] {});
        assertEquals(ActionResult.FAILURE, ret);
    }

    @org.junit.Test
    public void test_tryToMakeAction_PlayerPresent() {
        var resources = new ArrayList<Effect>();
        resources.add(Effect.WOOD);
        var cards = new ArrayList<CivilisationCard>();
        cards.add(new CivilisationCard(new ImmediateEffect[]{ImmediateEffect.FOOD}, new EndOfGameEffect[] {EndOfGameEffect.SHAMAN}));
        var t = new CivilizationCardPlace(new CivilizationCardDeck(new ArrayList<>()));
        Player player1 = new Player(new PlayerOrder(1, 1), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        t.placeFigures(player1, 1);
        var ret = t.tryToMakeAction(player1);
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, ret);
    }

    @Test
    public void test_tryToMakeAction_PlayerAbsent() {
        var resources = new ArrayList<Effect>();
        resources.add(Effect.WOOD);
        var cards = new ArrayList<CivilisationCard>();
        cards.add(new CivilisationCard(new ImmediateEffect[]{ImmediateEffect.FOOD}, new EndOfGameEffect[] {EndOfGameEffect.SHAMAN}));
        var t = new CivilizationCardPlace(new CivilizationCardDeck(new ArrayList<>()));
        Player player1 = new Player(new PlayerOrder(1, 1), null);
        var ret = t.tryToMakeAction(player1);
        assertEquals(HasAction.NO_ACTION_POSSIBLE, ret);
    }
}

