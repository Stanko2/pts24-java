package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static sk.uniba.fmph.dcs.stone_age.ImmediateEffect.*;


public class CivilizationCardPlace implements InterfaceFigureLocationInternal {
    private int requiredResources;
    private CivilisationCard card;
    private PlayerOrder figure;
    private final CivilizationCardDeck deck;

    public CivilizationCardPlace(CivilizationCardDeck deck) {
        this.deck = deck;
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
    public boolean placeFigures(final Player player, final int figureCount) {
        if (figure != null) {
            return false;
        }
        figure = player.playerOrder();
        return true;
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
    public HasAction tryToPlaceFigures(final Player player, final int count) {
        if (figure != null) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    private EvaluateCivilisationCardImmediateEffect getEffect(ImmediateEffect effect) {
        switch (effect) {
            case WOOD -> {
                return new GetSomethingFixed(List.of(Effect.WOOD));
            }
            case CLAY -> {
                return new GetSomethingFixed(List.of(Effect.CLAY));
            }
            case STONE -> {
                return new GetSomethingFixed(List.of(Effect.STONE));
            }
            case GOLD -> {
                return new GetSomethingFixed(List.of(Effect.GOLD));
            }
            case FOOD -> {
                return new GetSomethingFixed(List.of(Effect.FOOD));
            }
            case CARD -> {
                return new GetCard(deck);
            }
            case THROW_CLAY -> {
                return new GetSomethingThrow(Effect.CLAY);
            }
            case THROW_GOLD -> {
                return new GetSomethingThrow(Effect.GOLD);
            }
            case THROW_STONE -> {
                return new GetSomethingThrow(Effect.STONE);
            }
            case THROW_WOOD -> {
                return new GetSomethingThrow(Effect.WOOD);
            }
        }
        if (figure != null) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    private EvaluateCivilisationCardImmediateEffect getEffect(final ImmediateEffect effect) {
        switch (effect) {
            case WOOD -> {
                return new GetSomethingFixed(List.of(Effect.WOOD));
            }
            case CLAY -> {
                return new GetSomethingFixed(List.of(Effect.CLAY));
            }
            case STONE -> {
                return new GetSomethingFixed(List.of(Effect.STONE));
            }
            case GOLD -> {
                return new GetSomethingFixed(List.of(Effect.GOLD));
            }
            case FOOD -> {
                return new GetSomethingFixed(List.of(Effect.FOOD));
            }
            case CARD -> {
                return new GetCard(deck);
            }
            case THROW_CLAY -> {
                return new GetSomethingThrow(Effect.CLAY);
            }
            case THROW_GOLD -> {
                return new GetSomethingThrow(Effect.GOLD);
            }
            case THROW_STONE -> {
                return new GetSomethingThrow(Effect.STONE);
            }
            case THROW_WOOD -> {
                return new GetSomethingThrow(Effect.WOOD);
            }
            default -> {
                return null;
            }
        }
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
    public ActionResult makeAction(final Player player, final Effect[] inputResources, final Effect[] outputResources) {
        if(!player.playerBoard().takeResources(inputResources)) {
            return ActionResult.FAILURE;
        }
        player.playerBoard().giveEndOfGameEffect(card.endOfGameEffect());
        for (var effect : card.immediateEffect()) {
            var x = getEffect(effect);
            x.performEffect(player, null);
        }
        figure = null;

        var x = deck.getTop();
        if(!x.isEmpty()) {
            card = x.get();
        }
        return ActionResult.ACTION_DONE;
    }

    /**
     * TODO.
     *
     * @param player
     *
     * @return TODO
     */
    @Override
    public boolean skipAction(final Player player) {
        figure = null;
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
    public HasAction tryToMakeAction(final Player player) {
        if (player.playerOrder() != figure) {
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
        return figure != null;
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
