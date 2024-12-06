package sk.uniba.fmph.dcs.game_board;

import com.sun.management.HotSpotDiagnosticMXBean;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.InterfaceToolUse;

import java.util.Optional;

public final class CurrentThrow implements InterfaceToolUse {
    private int result;
    private final Player player;
    private final Effect throwsFor;
    private boolean finalized = false;

    private CurrentThrow(final Effect throwsFor, final int throwResult, Player player) {
        this.throwsFor = throwsFor;
        this.player = player;
    }

    public static CurrentThrow initiate(final Player player, final Effect effect, final int dices) {
        int[] values = Throw.hod(dices);
        int s = 0;
        for (int i = 0; i < values.length; i++) {
            s += values[i];
        }
        return new CurrentThrow(effect, s, player);
    }

    @Override
    public boolean useTool(final int idx) {
        if (finalized) {
            throw new RuntimeException();
        }
        Optional<Integer> s = player.playerBoard().useTool(idx);
        if(!s.isEmpty()) {
            result += s.get();
        }
        return !s.isEmpty();
    }

    @Override
    public boolean canUseTools() {
        return player.playerBoard().hasSufficientTools(1);
    }

    public int getResult() {
        return result;
    }

    @Override
    public boolean finishUsingTools() {
        finalized = true;
        return true;
    }
}
