package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;

public class PlaceOnHutAdaptor implements InterfaceFigureLocationInternal {
    private final ToolMakerHutsFields huts;

    /**
     * @param huts
     *            using methods from ToolMakerHutsFields
     */
    public PlaceOnHutAdaptor(final ToolMakerHutsFields huts) {
        this.huts = huts;
    }

    /**
     * @param player
     *            player that places figures
     * @param figureCount
     *            number of figures to place
     *
     * @return true if move is possible
     */
    @Override
    public boolean placeFigures(final Player player, final int figureCount) {
        if (tryToPlaceFigures(player, figureCount) == HasAction.NO_ACTION_POSSIBLE) {
            return false;
        }
        return huts.placeOnHut(player);
    }

    /**
     * does field allow for player to place figures.
     *
     * @param player
     *            player that places figures
     * @param count
     *            number of figures
     *
     * @return `NO_ACTION_POSSIBLE` when player cant place figures on field, `WAITING_FOR_PLAYER_ACTION` otherwise
     */
    @Override
    public HasAction tryToPlaceFigures(final Player player, final int count) {
        if (!player.playerBoard().hasFigures(count)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if (count != 2) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if (!huts.canPlaceOnHut(player)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.AUTOMATIC_ACTION_DONE;
    }

    /**
     * resolve huts.
     *
     * @param player
     *            player that wants to resolve
     * @param inputResources
     *            really don't know todo
     * @param outputResources
     *            don't know either todo
     *
     * @return ACTION_DONE when player can resolve huts, FAILURE otherwise
     */
    @Override
    public ActionResult makeAction(final Player player, final Effect[] inputResources, final Effect[] outputResources) {
        boolean res = huts.actionHut(player);
        if (res) {
            return ActionResult.ACTION_DONE;
        } else {
            return ActionResult.FAILURE;
        }
    }
    // todo

    /**
     * does exactly nothing.
     *
     * @param player
     *            player to do nothing with
     *
     * @return false
     */
    @Override
    public boolean skipAction(final Player player) {
        return false;
    }

    // todo

    /**
     * does exactly nothing.
     *
     * @param player
     *            player to do nothing with
     *
     * @return null
     */
    @Override
    public HasAction tryToMakeAction(final Player player) {
        return null;
    }

    // todo

    /**
     * does exactly nothing.
     *
     * @return false
     */
    @Override
    public boolean newTurn() {
        return huts.newTurn();
    }

    /**
     * @return state of huts
     */
    @Override
    public String state() {
        return huts.state();
    }
}
