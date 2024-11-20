package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.ArrayList;

public class ResourceSource implements InterfaceFigureLocationInternal {
    private String name;
    private final Effect resource;
    private int count;
    private int maxFigures;
    private int maxFigureColors;
    private ArrayList<PlayerOrder> figures;

    public ResourceSource(Effect resource, int count) {

        this.resource = resource;
        this.count = count;
    }

    @Override
    public boolean placeFigures(Player player, int figureCount) {
        return false;
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        return null;
    }

    @Override
    public ActionResult makeAction(Player player, Effect[] inputResources, Effect[] outputResources) {
        return null;
    }

    @Override
    public boolean skipAction(Player player) {
        return false;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
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
