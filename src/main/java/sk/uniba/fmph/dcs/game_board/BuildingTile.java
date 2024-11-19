package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class BuildingTile implements InterfaceFigureLocationInternal {
    private final Building building;
    private final ArrayList<PlayerOrder> figures;


    public BuildingTile(final Building building) {
        this.building = building;
        figures = new ArrayList<>();
    }

    @Override
    public boolean placeFigures(final Player player, final int figureCount) {
        var action = tryToPlaceFigures(player, figureCount);
        if (action == HasAction.WAITING_FOR_PLAYER_ACTION) {
            figures.add(player.playerOrder());
            return true;
        }

        return false;
    }

    @Override
    public HasAction tryToPlaceFigures(final Player player, final int count) {
        if (count > 1)
            return HasAction.NO_ACTION_POSSIBLE;
        if (figures.contains(player.playerOrder())) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public ActionResult makeAction(final Player player, final Effect[] inputResources, final Effect[] outputResources) {
        OptionalInt points = building.build(List.of(inputResources));
        if (tryToMakeAction(player) == HasAction.NO_ACTION_POSSIBLE) {
            return ActionResult.FAILURE;
        }
        if (points.isEmpty()) {
            return ActionResult.FAILURE;
        }

        player.playerBoard().addPoints(points.getAsInt());
        player.playerBoard().takeResources(inputResources);
        figures.remove(player.playerOrder());
        return ActionResult.ACTION_DONE;
    }

    @Override
    public boolean skipAction(final Player player) {
        return false;
    }

    @Override
    public HasAction tryToMakeAction(final Player player) {
        if (figures.contains(player.playerOrder())) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public boolean newTurn() {
        return false;
    }
}
