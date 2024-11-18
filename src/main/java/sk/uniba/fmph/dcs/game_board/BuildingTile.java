package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class BuildingTile implements InterfaceFigureLocationInternal {
    private Building building;
    private ArrayList<PlayerOrder> figures;


    public BuildingTile(Building building) {
        this.building = building;
        figures = new ArrayList<>();
    }

    @Override
    public boolean placeFigures(Player player, int figureCount) {
        var action = tryToPlaceFigures(player, figureCount);
        if (action == HasAction.WAITING_FOR_PLAYER_ACTION) {
            figures.add(player.playerOrder());
            return true;
        }

        return false;
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (count > 1)
            return HasAction.NO_ACTION_POSSIBLE;
        if (figures.contains(player.playerOrder())) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public ActionResult makeAction(Player player, Effect[] inputResources, Effect[] outputResources) {
        OptionalInt points = building.build(List.of(inputResources));
        if (points.isEmpty()) {
            return ActionResult.FAILURE;
        }
        // TODO: add points to player
        player.playerBoard().takeResources(inputResources);
        return ActionResult.ACTION_DONE;
    }

    @Override
    public boolean skipAction(Player player) {
        return false;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if(figures.contains(player.playerOrder())) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public boolean newTurn() {
        figures.clear();
        return true;
    }
}
