package sk.uniba.fmph.dcs.game_board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import static org.junit.Assert.*;

class CivilizationCardPlaceTest {
    private CivilizationCardPlace cardPlace;
    private CivilizationCardDeck mockDeck;
    private Player mockPlayer;
    private PlayerOrder mockPlayerOrder;
    private CivilisationCard mockCard;
    private PlayerBoard mockPlayerBoard;

    @BeforeEach
    void setUp() {
        mockDeck = new CivilizationCardDeck(new ArrayList<>());
        mockPlayerOrder = new PlayerOrder(1, 2);
        mockPlayerBoard = new PlayerBoard();
        mockPlayer = new Player(mockPlayerOrder, new PlayerBoardGameBoardFacade(new PlayerBoard()));
        mockCard = new CivilisationCard(new ImmediateEffect[]{}, new EndOfGameEffect[]{});

        cardPlace = new CivilizationCardPlace(mockDeck);
    }

    @Test
    void testPlaceFiguresSuccess() {
        assertTrue(cardPlace.placeFigures(mockPlayer, 1));
    }

    @Test
    void testPlaceFiguresFailureWhenAlreadyPlaced() {
        cardPlace.placeFigures(mockPlayer, 1);
        assertFalse(cardPlace.placeFigures(mockPlayer, 1));
    }

    @Test
    void testTryToPlaceFigures() {
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, cardPlace.tryToPlaceFigures(mockPlayer, 1));
        cardPlace.placeFigures(mockPlayer, 1);
        assertEquals(HasAction.NO_ACTION_POSSIBLE, cardPlace.tryToPlaceFigures(mockPlayer, 1));
    }

    @Test
    void testMakeActionSuccess() {
        Effect[] inputResources = {Effect.WOOD, Effect.CLAY};
        Effect[] outputResources = {};

        cardPlace.makeAction(mockPlayer, inputResources, outputResources);
    }

    @Test
    void testMakeActionFailureDueToInsufficientResources() {
        Effect[] inputResources = {Effect.WOOD};
        Effect[] outputResources = {};

        assertEquals(ActionResult.FAILURE, cardPlace.makeAction(mockPlayer, inputResources, outputResources));
    }

    @Test
    void testSkipAction() {
        cardPlace.placeFigures(mockPlayer, 1);
        assertTrue(cardPlace.skipAction(mockPlayer));
    }

    @Test
    void testTryToMakeAction() {
        cardPlace.placeFigures(mockPlayer, 1);
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, cardPlace.tryToMakeAction(mockPlayer));
        var otherPlayer = new Player(new PlayerOrder(2,2), new PlayerBoardGameBoardFacade(mockPlayerBoard));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, cardPlace.tryToMakeAction(otherPlayer));
    }

    @Test
    void testNewTurn() {
        assertFalse(cardPlace.newTurn());
        cardPlace.placeFigures(mockPlayer, 1);
        assertTrue(cardPlace.newTurn());
    }

    @Test
    void testState() {
        assertNotNull(cardPlace.state());
    }
}

