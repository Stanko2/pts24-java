package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class GameBoard implements InterfaceGetState {
    private final Map<Location, InterfaceFigureLocationInternal> locations;
    private final CivilizationCardDeck deck;
    private final Map<PlayerOrder, Player> players;

    public GameBoard(final Map<PlayerOrder, Player> players, final ArrayList<Stack<Building>> buildings, final CivilizationCardDeck deck) {
        ToolMakerHutsFields fields = new ToolMakerHutsFields(players.size());
        this.players = players;
        locations = new HashMap<>();
        this.deck = deck;
        locations.put(Location.HUT, new PlaceOnHutAdaptor(fields));
        locations.put(Location.FIELD, new PlaceOnFieldsAdaptor(fields));
        locations.put(Location.TOOL_MAKER, new PlaceOnToolMakerAdaptor(fields));
        locations.put(Location.CLAY_MOUND, new ResourceSource(Effect.CLAY, players.size()));
        locations.put(Location.FOREST, new ResourceSource(Effect.WOOD, players.size()));
        locations.put(Location.QUARRY, new ResourceSource(Effect.STONE, players.size()));
        locations.put(Location.RIVER,  new ResourceSource(Effect.GOLD, players.size()));
        locations.put(Location.BUILDING_TILE1, new BuildingTile(get(buildings, 0)));
        locations.put(Location.BUILDING_TILE2, new BuildingTile(get(buildings, 1)));
        locations.put(Location.BUILDING_TILE3, new BuildingTile(get(buildings, 2)));
        locations.put(Location.BUILDING_TILE4, new BuildingTile(get(buildings, 3)));
        var card1 = new CivilizationCardPlace(deck, 1, players);
        locations.put(Location.CIVILISATION_CARD1, card1);
        var card2 = new CivilizationCardPlace(deck, 2, players);
        locations.put(Location.CIVILISATION_CARD2, card2);
        var card3 = new CivilizationCardPlace(deck, 3, players);
        locations.put(Location.CIVILISATION_CARD3, card3);
        var card4 = new CivilizationCardPlace(deck, 4, players);
        locations.put(Location.CIVILISATION_CARD4, card4);
        card4.setNextSlot(card3);
        card3.setNextSlot(card2);
        card2.setNextSlot(card1);
    }

    public InterfaceFigureLocationInternal getLocation(Location location) {
        return locations.get(location);
    }

    public Map<Location, InterfaceFigureLocation> getLocations() {
        var res = new HashMap<Location, InterfaceFigureLocation>();
        for (var x : locations.keySet()) {
            res.put(x, new InterfaceFigureLocation() {
                @Override
                public boolean placeFigures(PlayerOrder player, int figureCount) {
                    return locations.get(x).placeFigures(players.get(player), figureCount);
                }

                @Override
                public HasAction tryToPlaceFigures(PlayerOrder player, int count) {
                    return locations.get(x).tryToPlaceFigures(players.get(player), count);
                }

                @Override
                public ActionResult makeAction(PlayerOrder player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
                    return locations.get(x).makeAction(players.get(player), inputResources.toArray(new Effect[0]), outputResources.toArray(new Effect[0]));
                }

                @Override
                public boolean skipAction(PlayerOrder player) {
                    return locations.get(x).skipAction(players.get(player));
                }

                @Override
                public HasAction tryToMakeAction(PlayerOrder player) {
                    return locations.get(x).tryToMakeAction(players.get(player));
                }

                @Override
                public boolean newTurn() {
                    return locations.get(x).newTurn();
                }
            });
        }
        return res;
    }

    private Stack<Building> get(ArrayList<Stack<Building>> in, int index) {
        try {
            return in.get(index);
        } catch (IndexOutOfBoundsException e) {
            return new Stack<>();
        }
    }

    /**
     * @return state combined from everything on the game board
     */
    @Override
    public String state() {
        Map<Location, String> states = new HashMap<>();

        for (var x: locations.keySet()) {
            states.put(x, locations.get(x).state());
        }

        var ret = new JSONObject(states);
        return ret.toString();
    }
}
