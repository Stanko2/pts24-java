package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.Location;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;
import sk.uniba.fmph.dcs.stone_age.InterfaceNewTurn;
import sk.uniba.fmph.dcs.stone_age.InterfaceFigureLocation;

import java.util.Collection;
import java.util.Map;

public final class NewRoundState implements InterfaceGamePhaseState {
    private final InterfaceFigureLocation[] places;
    private final Map<PlayerOrder, InterfaceNewTurn> playerPlayerBoardMap;

    public NewRoundState(final InterfaceFigureLocation[] places,
                         final Map<PlayerOrder, InterfaceNewTurn> playerPlayerBoardMap) {
        this.places = places;
        this.playerPlayerBoardMap = playerPlayerBoardMap;
    }

    @Override
    public ActionResult placeFigures(final PlayerOrder player, final Location location, final int figuresCount) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult makeAction(final PlayerOrder player, final Location location,
                                   final Collection<Effect> inputResources, final Collection<Effect> outputResources) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult skipAction(final PlayerOrder player, final Location location) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult useTools(final PlayerOrder player, final int toolIndex) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult noMoreToolsThisThrow(final PlayerOrder player) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult feedTribe(final PlayerOrder player, final Collection<Effect> resources) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult doNotFeedThisTurn(final PlayerOrder player) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult makeAllPlayersTakeARewardChoice(final PlayerOrder player, final Effect reward) {
        return ActionResult.FAILURE;
    }

    @Override
    public HasAction tryToMakeAutomaticAction(final PlayerOrder player) {
        // returns NO_ACTION_POSSIBLE if any of InterfaceFigureLocations indicates end of game
        for (InterfaceFigureLocation location : places) {
            if (location.newTurn()) {
                return HasAction.NO_ACTION_POSSIBLE;
            }
        }

        for (InterfaceNewTurn i : playerPlayerBoardMap.values()) {
            i.newTurn();
        }
        return HasAction.AUTOMATIC_ACTION_DONE;
    }
}
