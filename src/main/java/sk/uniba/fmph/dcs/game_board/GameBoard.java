package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;
import sk.uniba.fmph.dcs.stone_age.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameBoard implements InterfaceGetState {
    private final Map<Location, InterfaceFigureLocationInternal> locations;
    private final CivilizationCardDeck deck;

    public GameBoard(final Collection<Player> players, final Building[] buildings) {
        ToolMakerHutsFields fields = new ToolMakerHutsFields(players.size());
        locations = new HashMap<>();
        deck = new CivilizationCardDeck(new ArrayList<>());
        locations.put(Location.HUT, new PlaceOnHutAdaptor(fields));
        locations.put(Location.FIELD, new PlaceOnFieldsAdaptor(fields));
        locations.put(Location.TOOL_MAKER, new PlaceOnToolMakerAdaptor(fields));
        locations.put(Location.CLAY_MOUND, new ResourceSource(Effect.CLAY, players.size()));
        locations.put(Location.FOREST, new ResourceSource(Effect.WOOD, players.size()));
        locations.put(Location.QUARRY, new ResourceSource(Effect.STONE, players.size()));
        locations.put(Location.RIVER,  new ResourceSource(Effect.GOLD, players.size()));
        locations.put(Location.BUILDING_TILE1, new BuildingTile(buildings[0]));
        locations.put(Location.BUILDING_TILE2, new BuildingTile(buildings[1]));
        locations.put(Location.BUILDING_TILE3, new BuildingTile(buildings[2]));
        locations.put(Location.BUILDING_TILE4, new BuildingTile(buildings[3]));
        locations.put(Location.CIVILISATION_CARD1, new CivilizationCardPlace(deck));
        locations.put(Location.CIVILISATION_CARD2, new CivilizationCardPlace(deck));
        locations.put(Location.CIVILISATION_CARD3, new CivilizationCardPlace(deck));
        locations.put(Location.CIVILISATION_CARD4, new CivilizationCardPlace(deck));
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
