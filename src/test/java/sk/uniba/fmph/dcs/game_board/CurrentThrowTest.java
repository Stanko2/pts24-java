package sk.uniba.fmph.dcs.game_board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

class CurrentThrowTest {
    private CurrentThrow currentThrow;

    @Mock
    private Player player;

    private InterfacePlayerBoardGameBoard mockPlayerBoard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockPlayerBoard = mock(InterfacePlayerBoardGameBoard.class);

        player = mock(Player.class);
        when(player.playerBoard()).thenReturn(mockPlayerBoard);
        when(player.playerOrder()).thenReturn(new PlayerOrder(1,1));

        currentThrow = new CurrentThrow(Effect.WOOD, 3, player);
    }

    @Test
    void getResult_shouldBeInRange() {
        assertTrue(currentThrow.getResult() >= 3);
        assertTrue(currentThrow.getResult() <= 18);
    }

    @Test
    void useTool_increasesResult() {
        when(mockPlayerBoard.useTool(0)).thenReturn(Optional.of(2));
        var result = currentThrow.getResult();

        assertTrue(currentThrow.useTool(0));
        assertEquals(currentThrow.getResult(), result+2);
    }

    @Test
    void useTool_throwWhenFinalized() {
        assertTrue(currentThrow.finishUsingTools());
        assertFalse(currentThrow.canUseTools());
        Assertions.assertThrows(RuntimeException.class, ()->currentThrow.useTool(0));
    }
}