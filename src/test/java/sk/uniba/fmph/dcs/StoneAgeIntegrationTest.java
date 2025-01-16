package sk.uniba.fmph.dcs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StoneAgeIntegrationTest {
    private StoneAgeGame stoneAgeGame;
    private StoneAgeObservable observable;

    @BeforeEach
    void setup() {
        observable = new StoneAgeObservable();
        observable.registerObserver(0, gameState -> System.out.println(gameState));
        stoneAgeGame = StoneAgeGameFactory.createStoneAge(2, observable);
    }

    @Test
    void makeActions() {
        assertTrue(stoneAgeGame.placeFigures(0, Location.FOREST, 2));
        assertTrue(stoneAgeGame.placeFigures(1, Location.QUARRY, 4));
        assertTrue(stoneAgeGame.placeFigures(0, Location.HUT, 2));
        assertTrue(stoneAgeGame.placeFigures(1, Location.TOOL_MAKER, 1));
        assertFalse(stoneAgeGame.placeFigures(0, Location.HUT, 2));
        assertFalse(stoneAgeGame.placeFigures(1, Location.CLAY_MOUND, 1));
        assertTrue(stoneAgeGame.placeFigures(0, Location.CLAY_MOUND, 1));
        assertTrue(stoneAgeGame.placeFigures(1, Location.CLAY_MOUND, 1));
        assertTrue(stoneAgeGame.placeFigures(0, Location.CLAY_MOUND, 1));
        assertTrue(stoneAgeGame.placeFigures(0, Location.CLAY_MOUND, 1));

        assertTrue(stoneAgeGame.doNotFeedThisTurn(0));
        assertTrue(stoneAgeGame.feedTribe(1, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.STONE, Effect.STONE)));

        assertTrue(stoneAgeGame.placeFigures(1, Location.CIVILISATION_CARD1, 1));
        assertTrue(stoneAgeGame.placeFigures(0, Location.CLAY_MOUND, 4));
        assertTrue(stoneAgeGame.placeFigures(1, Location.FOREST, 3));
        assertTrue(stoneAgeGame.placeFigures(0, Location.BUILDING_TILE1, 1));
        assertTrue(stoneAgeGame.placeFigures(1, Location.FIELD, 1));

        assertTrue(stoneAgeGame.makeAction(1, Location.CIVILISATION_CARD1, List.of(Effect.STONE), List.of()));
        assertTrue(stoneAgeGame.makeAction(0, Location.BUILDING_TILE1, List.of(Effect.WOOD), List.of()));

        assertTrue(stoneAgeGame.doNotFeedThisTurn(0));
        assertTrue(stoneAgeGame.doNotFeedThisTurn(1));
    }
}
