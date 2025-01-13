package sk.uniba.fmph.dcs.game_board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;
import sk.uniba.fmph.dcs.stone_age.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ResourceSourceTest {

    private ResourceSource resourceSource;

    @Mock
    private Player player;

    private InterfacePlayerBoardGameBoard mockPlayerBoard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        resourceSource = new ResourceSource(Effect.WOOD, 2);
        mockPlayerBoard = mock(InterfacePlayerBoardGameBoard.class);

        player = mock(Player.class);
        when(player.playerBoard()).thenReturn(mockPlayerBoard);
        when(player.playerOrder()).thenReturn(new PlayerOrder(1,1));
    }

    @Test
    void placeFigures_WithinCapacity_ReturnsTrue() {
        assertTrue(resourceSource.placeFigures(player, 3));
    }

    @Test
    void placeFigures_ExceedsCapacity_ReturnsFalse() {
        assertFalse(resourceSource.placeFigures(player, 8));
    }

    @Test
    void tryToPlaceFigures_WithinCapacity_ReturnsWaitingForAction() {
        assertEquals(
                HasAction.WAITING_FOR_PLAYER_ACTION,
                resourceSource.tryToPlaceFigures(player, 3)
        );
    }

    @Test
    void tryToPlaceFigures_ExceedsCapacity_ReturnsNoActionPossible() {
        assertEquals(
                HasAction.NO_ACTION_POSSIBLE,
                resourceSource.tryToPlaceFigures(player, 8)
        );
    }

    @Test
    void makeAction_WithValidPlayer_ReturnsActionDoneWaitForToolUse() {
        resourceSource.placeFigures(player, 3);

        assertEquals(
                ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
                resourceSource.makeAction(player, new Effect[]{}, new Effect[]{})
        );
    }

    @Test
    void makeAction_WithInvalidPlayer_ReturnsFailure() {
        assertEquals(
                ActionResult.FAILURE,
                resourceSource.makeAction(player, new Effect[]{}, new Effect[]{})
        );
    }

    @Test
    void skipAction_RemovesFiguresAndReturnsTrue() {
        resourceSource.placeFigures(player, 3);

        assertTrue(resourceSource.skipAction(player));
        assertEquals(
                HasAction.NO_ACTION_POSSIBLE,
                resourceSource.tryToMakeAction(player)
        );
    }

    @Test
    void tryToMakeAction_WithNoTools_CompletesAutomatically() {
        try (MockedConstruction<CurrentThrow> mocked = mockConstruction(CurrentThrow.class, (mock, context)->{
            when(mock.canUseTools()).thenReturn(false);
            when(mock.getResult()).thenReturn(9); // Will give 3 wood (9/3)
        })) {
            resourceSource.placeFigures(player, 3);

            assertEquals(
                    HasAction.AUTOMATIC_ACTION_DONE,
                    resourceSource.tryToMakeAction(player)
            );
            verify(mockPlayerBoard, times(3))
                    .giveEffect(new Effect[]{Effect.WOOD});
        }
    }

    @Test
    void tryToMakeAction_WithTools_ReturnsWaitingForAction() {
        try (MockedConstruction<CurrentThrow> mocked = mockConstruction(CurrentThrow.class, (mock, context)->{
            when(mock.canUseTools()).thenReturn(true);
        })) {
            resourceSource.placeFigures(player, 3);

            assertEquals(
                    HasAction.WAITING_FOR_PLAYER_ACTION,
                    resourceSource.tryToMakeAction(player)
            );
        }
    }

    @Test
    void tryToMakeAction_WithNoFigures_ReturnsNoActionPossible() {
        assertEquals(
                HasAction.NO_ACTION_POSSIBLE,
                resourceSource.tryToMakeAction(player)
        );
    }

    @Test
    void newTurn_AlwaysReturnsFalse() {
        assertFalse(resourceSource.newTurn());
    }

    @Test
    void state_ReturnsValidJsonString() {
        assertNotNull(resourceSource.state());
        assertTrue(resourceSource.state().startsWith("{"));
        assertTrue(resourceSource.state().endsWith("}"));
    }
}