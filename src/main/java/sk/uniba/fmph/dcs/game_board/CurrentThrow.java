package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceToolUse;

import java.util.Optional;

public class CurrentThrow implements InterfaceToolUse {
    private int result = 0;
    private final Player player;
    private final Effect throwsFor;
    private boolean finalized = false;

    public CurrentThrow(final Effect throwsFor, final int diceCount, final Player player) {
        this.throwsFor = throwsFor;
        this.player = player;
        var ret = Throw.hod(diceCount);
        for (int i = 0; i < ret.length; i++) {
            this.result += ret[i];
        }
    }

    @Override
    public boolean useTool(final int idx) {
        if (finalized) {
            throw new RuntimeException();
        }
        Optional<Integer> s = player.playerBoard().useTool(idx);
        if (!s.isEmpty()) {
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
