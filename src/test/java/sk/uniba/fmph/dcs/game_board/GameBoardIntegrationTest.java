package sk.uniba.fmph.dcs.game_board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameBoardIntegrationTest {
    private GameBoard gameBoard;

    private ArrayList<Player> players;

    @Mock
    private InterfacePlayerBoardGameBoard playerBoard;


    @BeforeEach
    void setup() {
        players = new ArrayList<Player>();
        playerBoard = mock(InterfacePlayerBoardGameBoard.class);
        when(playerBoard.hasFigures(any(Integer.class))).thenReturn(true);
        when(playerBoard.takeResources(any(Effect[].class))).thenReturn(true);

        players.add(new Player(new PlayerOrder(1,2), playerBoard));
        players.add(new Player(new PlayerOrder(2,2), playerBoard));

        var buildings = new ArrayList<Building>();
        buildings.add(new SimpleBuilding(List.of(Effect.WOOD)));
        buildings.add(new SimpleBuilding(List.of(Effect.WOOD)));
        buildings.add(new SimpleBuilding(List.of(Effect.WOOD)));
        buildings.add(new SimpleBuilding(List.of(Effect.WOOD)));

        var cards = new ArrayList<CivilisationCard>();
        cards.add(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.FOOD, ImmediateEffect.WOOD}, new EndOfGameEffect[]{}));
        cards.add(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.THROW_WOOD}, new EndOfGameEffect[]{EndOfGameEffect.SHAMAN}));
        cards.add(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.CARD}, new EndOfGameEffect[]{}));
        cards.add(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.FOOD, ImmediateEffect.WOOD}, new EndOfGameEffect[]{EndOfGameEffect.ART}));
        cards.add(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.FOOD, ImmediateEffect.WOOD}, new EndOfGameEffect[]{}));
        cards.add(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.FOOD, ImmediateEffect.WOOD}, new EndOfGameEffect[]{EndOfGameEffect.MUSIC}));
        cards.add(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.FOOD, ImmediateEffect.WOOD}, new EndOfGameEffect[]{}));


        gameBoard = GameBoardFactory.createGameBoard(players, cards, buildings);
    }

    @Test
    void makeActions() {
        assertTrue(gameBoard.getLocation(Location.FOREST).placeFigures(players.get(0), 1));
        assertTrue(gameBoard.getLocation(Location.QUARRY).placeFigures(players.get(1), 1));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, gameBoard.getLocation(Location.FOREST).makeAction(players.get(0), new Effect[]{}, new Effect[]{}));
        assertEquals(ActionResult.FAILURE, gameBoard.getLocation(Location.QUARRY).makeAction(players.get(0), new Effect[]{}, new Effect[]{}));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, gameBoard.getLocation(Location.QUARRY).makeAction(players.get(1), new Effect[]{}, new Effect[]{}));

        assertTrue(gameBoard.getLocation(Location.TOOL_MAKER).placeFigures(players.get(0),1));
        assertFalse(gameBoard.getLocation(Location.TOOL_MAKER).placeFigures(players.get(1),1));

        assertEquals(ActionResult.ACTION_DONE, gameBoard.getLocation(Location.TOOL_MAKER).makeAction(players.get(0), new Effect[]{}, new Effect[]{}));
        verify(playerBoard, times(1)).giveEffect(new Effect[]{Effect.TOOL});

        assertTrue(gameBoard.getLocation(Location.HUT).placeFigures(players.get(0),2));
        assertEquals(ActionResult.ACTION_DONE, gameBoard.getLocation(Location.HUT).makeAction(players.get(0), new Effect[]{}, new Effect[]{}));
        verify(playerBoard, times(1)).giveFigure();


        assertTrue(gameBoard.getLocation(Location.CIVILISATION_CARD1).placeFigures(players.get(0),2));
        assertEquals(ActionResult.ACTION_DONE, gameBoard.getLocation(Location.CIVILISATION_CARD1).makeAction(players.get(0), new Effect[]{Effect.WOOD}, new Effect[]{}));
        verify(playerBoard, atLeastOnce()).giveEndOfGameEffect(any(EndOfGameEffect[].class));
        assertEquals(null, ((CivilizationCardPlace)gameBoard.getLocation(Location.CIVILISATION_CARD1)).getCard());
        assertFalse(gameBoard.getLocation(Location.CIVILISATION_CARD1).newTurn());
        assertNotEquals(null, ((CivilizationCardPlace)gameBoard.getLocation(Location.CIVILISATION_CARD1)).getCard());


        assertTrue(gameBoard.getLocation(Location.BUILDING_TILE1).placeFigures(players.get(0),1));
        assertEquals(ActionResult.ACTION_DONE, gameBoard.getLocation(Location.BUILDING_TILE1).makeAction(players.get(0), new Effect[]{Effect.WOOD}, new Effect[]{}));
        verify(playerBoard, times(1)).addPoints(any(Integer.class));

        assertTrue(gameBoard.getLocation(Location.FIELD).placeFigures(players.get(1),1));
        assertEquals(ActionResult.ACTION_DONE, gameBoard.getLocation(Location.FIELD).makeAction(players.get(1), new Effect[]{}, new Effect[]{}));
        verify(playerBoard, times(1)).giveEffect(new Effect[]{Effect.FIELD});


    }
}
