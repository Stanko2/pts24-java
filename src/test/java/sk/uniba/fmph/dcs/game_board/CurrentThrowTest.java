package sk.uniba.fmph.dcs.game_board;

import org.junit.jupiter.api.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CurrentThrowTest {
    private static class TestPlayer {
        public TestPlayerBoard board;

        public TestPlayer(TestPlayerBoard board) {
            this.board = board;
        }

        public InterfacePlayerBoardGameBoard playerBoard() {
            return board;
        }

        public PlayerOrder playerOrder() {
            return new PlayerOrder(1,1);
        }
    }

    private static class TestPlayerBoard implements InterfacePlayerBoardGameBoard {
        private int toolCount;


        public TestPlayerBoard(int toolCount) {
            this.toolCount = toolCount;
        }

        @Override
        public void giveEffect(Effect[] stuff) {

        }

        @Override
        public void giveEndOfGameEffect(EndOfGameEffect[] stuff) {

        }

        @Override
        public boolean takeResources(Effect[] stuff) {
            return false;
        }

        @Override
        public void giveFigure() {

        }

        @Override
        public boolean takeFigures(int count) {
            return false;
        }

        @Override
        public boolean hasFigures(int count) {
            return false;
        }

        @Override
        public void addPoints(int points) {

        }

        @Override
        public boolean hasSufficientTools(int goal) {
            return toolCount >= goal;
        }

        @Override
        public Optional<Integer> useTool(int idx) {
            if (idx >= toolCount) {
                return Optional.empty();
            }
            return Optional.of(3);
        }
    }

    @Test
    void initiate_ShouldCreateNewThrowWithCorrectSum() {
        TestPlayerBoard board = new TestPlayerBoard(3);
        TestPlayer player = new TestPlayer(board);
        Effect effect = Effect.WOOD;

        CurrentThrow currentThrow = CurrentThrow.initiate(new Player(player.playerOrder(), player.playerBoard()), effect, 3);

        assertNotNull(currentThrow);
        assertTrue(currentThrow.getResult() >= 3); // Minimum possible roll with 3 dice
        assertTrue(currentThrow.getResult() <= 18); // Maximum possible roll with 3 dice
    }

    @Test
    void useTool_WhenToolAvailable_ShouldAddToResult() {
        TestPlayerBoard board = new TestPlayerBoard(3);
        TestPlayer player = new TestPlayer(board);
        Effect effect = Effect.WOOD;

        CurrentThrow currentThrow = CurrentThrow.initiate(new Player(player.playerOrder(), player.playerBoard()), effect, 3);
        var res = currentThrow.getResult();
        boolean result = currentThrow.useTool(0);
        assertTrue(currentThrow.getResult() > res);
        assertTrue(result);
    }

    @Test
    void useTool_WhenNoToolAvailable_ShouldReturnFalse() {
        TestPlayerBoard board = new TestPlayerBoard(0);
        TestPlayer player = new TestPlayer(board);
        Effect effect = Effect.WOOD;

        CurrentThrow currentThrow = CurrentThrow.initiate(new Player(player.playerOrder(), player.playerBoard()), effect, 3);
        boolean result = currentThrow.useTool(0);

        assertFalse(result);
    }

    @Test
    void useTool_AfterFinalized_ShouldThrowException() {
        TestPlayerBoard board = new TestPlayerBoard(3);
        TestPlayer player = new TestPlayer(board);
        Effect effect = Effect.WOOD;

        CurrentThrow currentThrow = CurrentThrow.initiate(new Player(player.playerOrder(), player.playerBoard()), effect, 3);
        currentThrow.finishUsingTools();

        assertThrows(RuntimeException.class, () -> currentThrow.useTool(0));
    }

    @Test
    void canUseTools_WhenToolsAvailable_ShouldReturnTrue() {
        TestPlayerBoard board = new TestPlayerBoard(3);
        TestPlayer player = new TestPlayer(board);
        Effect effect = Effect.WOOD;

        CurrentThrow currentThrow = CurrentThrow.initiate(new Player(player.playerOrder(), player.playerBoard()), effect, 3);

        assertTrue(currentThrow.canUseTools());
    }

    @Test
    void canUseTools_WhenNoToolsAvailable_ShouldReturnFalse() {
        TestPlayerBoard board = new TestPlayerBoard(0);
        TestPlayer player = new TestPlayer(board);
        Effect effect = Effect.WOOD;

        CurrentThrow currentThrow = CurrentThrow.initiate(new Player(player.playerOrder(), player.playerBoard()), effect, 3);

        assertFalse(currentThrow.canUseTools());
    }

    @Test
    void finishUsingTools_ShouldPreventFurtherToolUse() {
        TestPlayerBoard board = new TestPlayerBoard(3);
        TestPlayer player = new TestPlayer(board);
        Effect effect = Effect.WOOD;

        CurrentThrow currentThrow = CurrentThrow.initiate(new Player(player.playerOrder(), player.playerBoard()), effect, 3);

        assertTrue(currentThrow.finishUsingTools());
        assertThrows(RuntimeException.class, () -> currentThrow.useTool(0));
    }

    @Test
    void multipleToolUse_ShouldAccumulateResults() {
        TestPlayerBoard board = new TestPlayerBoard(3);
        TestPlayer player = new TestPlayer(board);
        Effect effect = Effect.WOOD;

        CurrentThrow currentThrow = CurrentThrow.initiate(new Player(player.playerOrder(), player.playerBoard()), effect, 3);

        assertTrue(currentThrow.useTool(0));
        assertTrue(currentThrow.useTool(1));
    }
}