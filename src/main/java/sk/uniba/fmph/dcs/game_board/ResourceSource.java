package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResourceSource implements InterfaceFigureLocationInternal {
    private String name;
    private final Effect resource;
    private int players;
    private Map<PlayerOrder, Integer> figures;
    private static Map<Effect, Integer> MULTIPLIERS = Map.of(Effect.WOOD, 3, Effect.CLAY, 4, Effect.STONE, 5, Effect.GOLD, 6);

    public ResourceSource(Effect resource, int playerCount) {
        this.resource = resource;
        this.players = playerCount;
        figures = new HashMap<>();
    }

    /**
     * TODO.
     *
     * @param player
     * @param figureCount
     *
     * @return TODO
     */
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        return false;
    }

    /**
     * TODO.
     *
     * @param player
     * @param count
     *
     * @return TODO
     */
    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (figures.size() + count >= 7) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        figures.put(player.playerOrder(), count);
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    /**
     * TODO.
     *
     * @param player
     * @param inputResources
     * @param outputResources
     *
     * @return TODO
     */
    @Override
    public ActionResult makeAction(Player player, Effect[] inputResources, Effect[] outputResources) {
        if (tryToMakeAction(player) == HasAction.NO_ACTION_POSSIBLE) {
            return ActionResult.FAILURE;
        }
        return ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE;
    }

    /**
     * TODO.
     *
     * @param player
     *
     * @return TODO
     */
    @Override
    public boolean skipAction(Player player) {
        figures.remove(player.playerOrder());
        return true;
    }

    /**
     * TODO.
     *
     * @param player
     *
     * @return TODO
     */
    @Override
    public HasAction tryToMakeAction(Player player) {
        var c = CurrentThrow.initiate(player, resource, figures.get(player.playerOrder()));
        if (!c.canUseTools()) {
            int count = c.getResult() / MULTIPLIERS.get(resource);
            for (int i = 0; i < count; i++) {
                player.playerBoard().giveEffect(new Effect[]{ resource });
            }
            return HasAction.AUTOMATIC_ACTION_DONE;
        }

        if (!figures.containsKey(player.playerOrder())) {
            return HasAction.NO_ACTION_POSSIBLE;
        }

        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    /**
     * TODO.
     *
     * @return TODO
     */
    @Override
    public boolean newTurn() {
        return false;
    }

    /**
     * TODO.
     *
     * @return TODO
     */
    @Override
    public String state() {

        return new JSONObject().toString();
    }
}
