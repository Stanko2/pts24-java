package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;
import sk.uniba.fmph.dcs.stone_age.Location;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameBoard implements InterfaceGetState {
    private final Map<Location, InterfaceFigureLocationInternal> locations;
    private static final int CLAY_IN_CLAY_MOUND = 18;
    private static final int STONE_IN_QUARRY = 12;
    private static final int GOLD_IN_RIVER = 10;
    private static final int WOOD_IN_FOREST = 28;

    public GameBoard(final Collection<Player> players, final Building[] buildings) {
        ToolMakerHutsFields fields = new ToolMakerHutsFields(players.size());
        locations = new HashMap<>();
        locations.put(Location.HUT, new PlaceOnHutAdaptor(fields));
        locations.put(Location.FIELD, new PlaceOnFieldsAdaptor(fields));
        locations.put(Location.TOOL_MAKER, new PlaceOnToolMakerAdaptor(fields));
        locations.put(Location.CLAY_MOUND, new ResourceSource(Effect.CLAY, CLAY_IN_CLAY_MOUND));
        locations.put(Location.FOREST, new ResourceSource(Effect.WOOD, WOOD_IN_FOREST));
        locations.put(Location.QUARRY, new ResourceSource(Effect.STONE, STONE_IN_QUARRY));
        locations.put(Location.RIVER,  new ResourceSource(Effect.GOLD, GOLD_IN_RIVER));
        locations.put(Location.BUILDING_TILE1, new BuildingTile(buildings[0]));
        locations.put(Location.BUILDING_TILE2, new BuildingTile(buildings[1]));
        locations.put(Location.BUILDING_TILE3, new BuildingTile(buildings[2]));
        locations.put(Location.BUILDING_TILE4, new BuildingTile(buildings[3]));
        locations.put(Location.CIVILISATION_CARD1, new CivilizationCardPlace());
        locations.put(Location.CIVILISATION_CARD2, new CivilizationCardPlace());
        locations.put(Location.CIVILISATION_CARD3, new CivilizationCardPlace());
        locations.put(Location.CIVILISATION_CARD4, new CivilizationCardPlace());
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
