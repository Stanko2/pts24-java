package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.game_phase_controller.GamePhaseController;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CivilizationCardPlace implements InterfaceFigureLocationInternal {
    private CivilisationCard card;
    private PlayerOrder figure;
    private final CivilizationCardDeck deck;
    private final int requiredResources;
    private final List<Player> players;
    private CivilizationCardPlace nextSlot;
    private static List<CivilizationCardPlace> instances = new ArrayList<>();
    public static GamePhaseController gamePhaseController;

    public CivilizationCardPlace(final CivilizationCardDeck deck, final int requiredResources, final Collection<Player> players) {
        this.deck = deck;
        this.requiredResources = requiredResources;
        this.players = (List<Player>) players;
        this.card = deck.getTop().get();
        instances.add(this);
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

    public void setNextSlot(CivilizationCardPlace nextSlot) {
        this.nextSlot = nextSlot;
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

    private Effect[] toEffects(ImmediateEffect[] immediateEffects) {
        var ret = new Effect[immediateEffects.length];
        for (int i = 0; i < immediateEffects.length; i++) {
            switch (immediateEffects[i]){
                case CLAY -> ret[i] = Effect.CLAY;
                case WOOD -> ret[i] = Effect.WOOD;
                case GOLD -> ret[i] = Effect.GOLD;
                case STONE -> ret[i] = Effect.STONE;
                case FOOD -> ret[i] = Effect.FOOD;
            }

        }
        return ret;
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
        if (card == null) {
            return ActionResult.FAILURE;
        }

        if (figure != player.playerOrder()) {
            return ActionResult.FAILURE;
        }

        // player does not have resources
        if (!player.playerBoard().takeResources(inputResources)) {
            return ActionResult.FAILURE;
        }

        // wrong number of resources
        if (inputResources.length != requiredResources) {
            return ActionResult.FAILURE;
        }

        // food is not allowed
        for (int i = 0; i < inputResources.length; i++) {
            if (inputResources[i] == Effect.FOOD)
                return ActionResult.FAILURE;
        }

        player.playerBoard().giveEndOfGameEffect(card.endOfGameEffect());
        ImmediateEffect[] immediateEffect = card.immediateEffect();
        for (int i = 0; i < immediateEffect.length; i++) {
            var effect = immediateEffect[i];
            if (effect == ImmediateEffect.ALL_PLAYERS_TAKE_REWARD) {
                var menu = new RewardMenu(Arrays.stream(toEffects(card.immediateEffect())).toList(), players);
                new AllPlayersTakeReward(menu, gamePhaseController).performEffect(player, outputResources[0]);
                break;
            } else if (effect == ImmediateEffect.ARBITRARY_RESOURCE) {
                new GetSomethingFixed(List.of(outputResources)).performEffect(player, outputResources[i]);
            } else if (effect == ImmediateEffect.POINT) {
                player.playerBoard().addPoints(1);
            } else {
                var x = getEffect(effect);
                if (x instanceof GetSomethingThrow) {
                    x.performEffect(player, ((GetSomethingThrow)x).resource);
                } else {
                    x.performEffect(player, null);
                }
            }

        }

        figure = null;
        card = null;
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

    public CivilisationCard getCard() {
        return card;
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
        if (nextSlot == null) {
            return false;
        }

        moveCards();

        if (card == null) {
            var c = deck.getTop();
            if(c.isEmpty()) {
                return true;
            }
            card = c.get();
        }
        return false;
    }

    private static void moveCards() {
        var moved = true;
        while (moved) {
            moved = false;
            for (var cardPlace : instances) {
                if (cardPlace.nextSlot == null) continue;
                if (cardPlace.card != null && cardPlace.nextSlot.card == null) {
                    moved = true;
                    cardPlace.nextSlot.card = cardPlace.card;
                    cardPlace.card = null;
                }
            }
        }
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
