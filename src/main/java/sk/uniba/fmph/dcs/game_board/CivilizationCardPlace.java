package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.CivilisationCard;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.HasAction;

import java.util.ArrayList;

public class CivilizationCardPlace implements InterfaceFigureLocationInternal {
    private int requiredResources;
    private CivilisationCard card;
    private ArrayList<PlayerOrder> figures;

    @Override
    public boolean placeFigures(final Player player, final int figureCount) {
        return false;
    }

    @Override
    public HasAction tryToPlaceFigures(final Player player, final int count) {
        return null;
    }

    @Override
    public ActionResult makeAction(final Player player, final Effect[] inputResources, final Effect[] outputResources) {
        return null;
    }

    @Override
    public boolean skipAction(final Player player) {
        return false;
    }

    @Override
    public HasAction tryToMakeAction(final Player player) {
        return null;
    }

    @Override
    public boolean newTurn() {
        return false;
    }

    @Override
    public String state() {
        return "";
    }
}
