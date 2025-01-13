package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;

import java.util.Arrays;

public final class GetSomethingThrow implements EvaluateCivilisationCardImmediateEffect {
    public final Effect resource;
    private CurrentThrow currentThrow;

    public GetSomethingThrow(final Effect resource) {
        this.resource = resource;

    }

    @Override
    public boolean performEffect(final Player player, final Effect choice) {
        if (choice != this.resource) {
            return false;
        }
        currentThrow = new CurrentThrow(choice, 2, player);
        int pocet = currentThrow.getResult() / choice.points();
        Effect[] res = new Effect[pocet];
        Arrays.fill(res, choice);
        player.playerBoard().giveEffect(res);
        return true;
    }
}
