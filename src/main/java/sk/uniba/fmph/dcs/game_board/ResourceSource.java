package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.HashMap;
import java.util.Map;

public class ResourceSource implements InterfaceFigureLocationInternal {
    private String name;
    private final Effect resource;
    private int players;
    private Map<PlayerOrder, Integer> figures;
    private static final Map<Effect, Integer> MULTIPLIERS = Map.of(Effect.WOOD, 3, Effect.CLAY, 4, Effect.STONE, 5, Effect.GOLD, 6);
    private static final int CAPACITY = 7;

    public ResourceSource(final Effect resource, final int playerCount) {
        this.resource = resource;
        this.players = playerCount;
        figures = new HashMap<>();
    }

    /**
     * Places figures onto a resourceSource
     *
     * @param player player that places figures
     * @param figureCount count of figures
     *
     * @return if the placement was successful
     */
    @Override
    public boolean placeFigures(final Player player, final int figureCount) {
        return tryToPlaceFigures(player, figureCount) != HasAction.NO_ACTION_POSSIBLE;
    }

    /**
     * Tries to place figures onto a resourceSource
     *
     * @param player player that places figures
     * @param count count of figures
     *
     * @return if the figures could be placed
     */
    @Override
    public HasAction tryToPlaceFigures(final Player player, final int count) {
        if (figures.size() + count >= CAPACITY) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        figures.put(player.playerOrder(), count);
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    /**
     * Claims a resource for player
     *
     * @param player player on turn
     * @param inputResources unused
     * @param outputResources unused
     *
     * @return if the action was successful
     */
    @Override
    public ActionResult makeAction(final Player player, final Effect[] inputResources, final Effect[] outputResources) {
        if (tryToMakeAction(player) == HasAction.NO_ACTION_POSSIBLE) {
            return ActionResult.FAILURE;
        }
        return ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE;
    }

    /**
     * Skips turn for player
     *
     * @param player player that skipped
     *
     * @return true
     */
    @Override
    public boolean skipAction(final Player player) {
        figures.remove(player.playerOrder());
        return true;
    }

    /**
     * tries to make action automatically
     *
     * @param player player that is making action
     *
     * @return if the action could be made
     */
    @Override
    public HasAction tryToMakeAction(final Player player) {
        if (!figures.containsKey(player.playerOrder())) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        var c = new CurrentThrow(resource, figures.get(player.playerOrder()), player);
        if (!c.canUseTools()) {
            int count = c.getResult() / MULTIPLIERS.get(resource);
            for (int i = 0; i < count; i++) {
                player.playerBoard().giveEffect(new Effect[] {resource});
            }
            return HasAction.AUTOMATIC_ACTION_DONE;
        }

        if (!figures.containsKey(player.playerOrder())) {
            return HasAction.NO_ACTION_POSSIBLE;
        }

        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    /**
     * resets for a new turn
     *
     * @return
     */
    @Override
    public boolean newTurn() {
        return false;
    }

    /**
     * get state
     *
     * @return state of this object
     */
    @Override
    public String state() {

        return new JSONObject().toString();
    }
}
