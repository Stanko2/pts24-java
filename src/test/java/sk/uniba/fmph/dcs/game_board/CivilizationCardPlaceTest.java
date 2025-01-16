package sk.uniba.fmph.dcs.game_board;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CivilizationCardPlaceTest {
    private CivilizationCardPlace civilizationCardPlace;
    private CivilizationCardPlace nextCardPlace;

    @Mock
    private Player player;

    @Mock
    private CivilizationCardDeck civilizationCardDeck;

    private InterfacePlayerBoardGameBoard mockPlayerBoard;
    private List<Optional<CivilisationCard>> cards = List.of(
            Optional.of(new CivilisationCard(new ImmediateEffect[]{ImmediateEffect.CLAY, ImmediateEffect.WOOD}, new EndOfGameEffect[]{})),
            Optional.of(new CivilisationCard(new ImmediateEffect[]{ImmediateEffect.THROW_WOOD}, new EndOfGameEffect[]{})),
            Optional.of(new CivilisationCard(new ImmediateEffect[]{ImmediateEffect.ARBITRARY_RESOURCE}, new EndOfGameEffect[]{})),
            Optional.empty()
    );
    private int cardIndex = 0;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockPlayerBoard = mock(InterfacePlayerBoardGameBoard.class);
        civilizationCardDeck = mock(CivilizationCardDeck.class);
        when(civilizationCardDeck.getTop()).thenReturn(
                cards.get(cardIndex)
        );

        player = mock(Player.class);
        when(player.playerBoard()).thenReturn(mockPlayerBoard);
        when(player.playerOrder()).thenReturn(new PlayerOrder(1,1));


        civilizationCardPlace = new CivilizationCardPlace(civilizationCardDeck, 2, Map.of(new PlayerOrder(1, 1), player));
        cardIndex = 3;
        nextCardPlace = new CivilizationCardPlace(civilizationCardDeck, 1, Map.of(new PlayerOrder(1, 1), player));

        cardIndex = 0;
        civilizationCardPlace.setNextSlot(nextCardPlace);
    }

    @Test
    void placeFigures_ShouldAllowToPlaceOneFigure() {
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, civilizationCardPlace.tryToPlaceFigures(player,1));
        assertTrue(civilizationCardPlace.placeFigures(player, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, civilizationCardPlace.tryToPlaceFigures(player,1));
        assertFalse(civilizationCardPlace.placeFigures(player, 1));
    }

    @Test
    void makeAction_ShouldFailWithNoFigure() {
        assertEquals(civilizationCardPlace.makeAction(player, new Effect[]{Effect.WOOD}, new Effect[]{Effect.WOOD}), ActionResult.FAILURE);
    }

    @Test
    void makeAction_ShouldFailWithNotEnoughResources() {
        cardIndex = 0;
        civilizationCardPlace.placeFigures(player, 1);

        when(mockPlayerBoard.takeResources(any())).thenReturn(true);

        assertEquals(civilizationCardPlace.makeAction(player, new Effect[]{}, new Effect[]{}), ActionResult.FAILURE);
        assertEquals(civilizationCardPlace.makeAction(player, new Effect[]{Effect.WOOD, Effect.WOOD}, new Effect[]{}), ActionResult.ACTION_DONE);
    }

    @Test
    void makeAction_ShouldFailWithNoCard() {
        cardIndex = 0;
        civilizationCardPlace.placeFigures(player, 1);

        when(mockPlayerBoard.takeResources(any())).thenReturn(true);

        assertEquals(nextCardPlace.makeAction(player, new Effect[]{}, new Effect[]{}), ActionResult.FAILURE);
        assertEquals(nextCardPlace.makeAction(player, new Effect[]{Effect.WOOD, Effect.WOOD}, new Effect[]{}), ActionResult.FAILURE);
        verify(mockPlayerBoard, times(0)).takeResources(any());
    }

    @Test
    void makeAction_ShouldFailWithWrongResources() {
        cardIndex = 0;
        civilizationCardPlace.placeFigures(player, 1);

        when(mockPlayerBoard.takeResources(any())).thenReturn(true);

        assertEquals(civilizationCardPlace.makeAction(player, new Effect[]{Effect.WOOD, Effect.FOOD}, new Effect[]{}), ActionResult.FAILURE);
        assertEquals(civilizationCardPlace.makeAction(player, new Effect[]{Effect.WOOD, Effect.CLAY}, new Effect[]{}), ActionResult.ACTION_DONE);
    }

    @Test
    void makeAction_ShouldGiveEndOfGameEffectsOnSuccess() {
        cardIndex = 0;
        civilizationCardPlace.placeFigures(player, 1);

        when(mockPlayerBoard.takeResources(any())).thenReturn(true);

        assertEquals(civilizationCardPlace.makeAction(player, new Effect[]{Effect.WOOD, Effect.CLAY}, new Effect[]{}), ActionResult.ACTION_DONE);
        verify(mockPlayerBoard, times(1)).giveEndOfGameEffect(any());
        assertEquals(null, civilizationCardPlace.getCard());
    }

    @Test
    void makeAction_ShouldGetFixedResources() {
        cardIndex = 0;
        civilizationCardPlace.placeFigures(player, 1);
        when(mockPlayerBoard.takeResources(any())).thenReturn(true);

        civilizationCardPlace.makeAction(player, new Effect[]{Effect.WOOD, Effect.CLAY}, new Effect[]{});

        verify(mockPlayerBoard, times(2)).giveEffect(any());
        assertEquals(null, civilizationCardPlace.getCard());
    }

    @Test
    void makeAction_ShouldGetThrowResources() {
        cardIndex = 1;
        civilizationCardPlace.placeFigures(player, 1);
        when(mockPlayerBoard.takeResources(any())).thenReturn(true);

        civilizationCardPlace.makeAction(player, new Effect[]{Effect.WOOD, Effect.CLAY}, new Effect[]{});
        verify(mockPlayerBoard, times(2)).giveEffect(any());
        assertEquals(null, civilizationCardPlace.getCard());
    }

    @Test
    void makeAction_ShouldGetWantedResources() {
        cardIndex = 2;
        civilizationCardPlace.placeFigures(player, 1);
        when(mockPlayerBoard.takeResources(any())).thenReturn(true);

        civilizationCardPlace.makeAction(player, new Effect[]{Effect.WOOD, Effect.CLAY}, new Effect[]{Effect.CLAY});
        verify(mockPlayerBoard, times(1)).giveEffect(new Effect[]{Effect.CLAY});
        assertEquals(null, civilizationCardPlace.getCard());
    }

    @Test
    void newTurn_ShouldMoveCardToNext() {
        cardIndex = 0;
        nextCardPlace.placeFigures(player, 1);
        when(mockPlayerBoard.takeResources(any())).thenReturn(true);
        nextCardPlace.makeAction(player, new Effect[]{Effect.WOOD}, new Effect[]{});
        assertEquals(null, nextCardPlace.getCard());
        assertFalse(civilizationCardPlace.newTurn());

        assertNotEquals(null, civilizationCardPlace.getCard());
        assertNotEquals(null, nextCardPlace.getCard());
    }

    @Test
    void newTurn_ShouldEndGameWhenNoCardsLeft() {

        nextCardPlace.placeFigures(player, 1);
        when(mockPlayerBoard.takeResources(any())).thenReturn(true);
        nextCardPlace.makeAction(player, new Effect[]{Effect.WOOD}, new Effect[]{});
        assertEquals(null, nextCardPlace.getCard());

        cardIndex = 3;
        assertTrue(civilizationCardPlace.newTurn());
    }
}

