package sk.uniba.fmph.dcs.game_phase_controller;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class MockClass implements InterfaceFigureLocation, InterfaceFeedTribe, InterfaceToolUse, InterfaceTakeReward {

    LinkedList<HasAction> hasActionExpected;
    LinkedList<ActionResult> actionResultExpected;
    LinkedList<Boolean> booleanExpected;

    MockClass(String hasAction, String actionResult, String bool) {
        hasActionExpected = new LinkedList<>();
        for (char c : hasAction.toCharArray()) {
            switch (c) {
                case 'w':
                    hasActionExpected.addLast(HasAction.WAITING_FOR_PLAYER_ACTION);
                    break;
                case 'n':
                    hasActionExpected.addLast(HasAction.NO_ACTION_POSSIBLE);
                    break;
                case 'a':
                    hasActionExpected.addLast(HasAction.AUTOMATIC_ACTION_DONE);
            }
        }
        actionResultExpected = new LinkedList<>();
        for (char c : actionResult.toCharArray()) {
            switch (c) {
                case 'F':
                    actionResultExpected.addLast(ActionResult.FAILURE);
                    break;
                case 'D':
                    actionResultExpected.addLast(ActionResult.ACTION_DONE);
                    break;
                case 'T':
                    actionResultExpected.addLast(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE);
                    break;
                case 'R':
                    actionResultExpected.addLast(ActionResult.ACTION_DONE_ALL_PLAYERS_TAKE_A_REWARD);
            }
        }
        booleanExpected = new LinkedList<>();
        for (char c : bool.toCharArray()) {
            switch (c) {
                case 'y':
                    booleanExpected.addLast(true);
                    break;
                case 'n':
                    booleanExpected.addLast(false);
            }
        }
    }

    private boolean returnBoolean() {
        if (booleanExpected.isEmpty()) {
            System.out.println("empty boolean");
            return false;
        }
        return booleanExpected.removeFirst();
    }

    private HasAction returnHasAction() {
        if (hasActionExpected.isEmpty()) {
            System.out.println("empty hasAction");
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return hasActionExpected.removeFirst();
    }

    private ActionResult returnActionResult() {
        if (actionResultExpected.isEmpty()) {
            System.out.println("empty action result");
            return ActionResult.FAILURE;
        }
        return actionResultExpected.removeFirst();
    }

    @Override
    public boolean feedTribeIfEnoughFood() {
        return returnBoolean();
    }

    @Override
    public boolean feedTribe(Collection<Effect> resources) {
        return returnBoolean();
    }

    @Override
    public boolean doNotFeedThisTurn() {
        return returnBoolean();
    }

    @Override
    public boolean isTribeFed() {
        return returnBoolean();
    }

    @Override
    public boolean placeFigures(PlayerOrder player, int figureCount) {
        return returnBoolean();
    }

    @Override
    public HasAction tryToPlaceFigures(PlayerOrder player, int count) {
        return returnHasAction();
    }

    @Override
    public ActionResult makeAction(PlayerOrder player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        return returnActionResult();
    }

    @Override
    public boolean skipAction(PlayerOrder player) {
        return returnBoolean();
    }

    @Override
    public HasAction tryToMakeAction(PlayerOrder player) {
        return returnHasAction();
    }

    @Override
    public boolean newTurn() {
        return returnBoolean();
    }

    @Override
    public boolean takeReward(PlayerOrder player, Effect reward) {
        return returnBoolean();
    }

    @Override
    public HasAction tryMakeAction(PlayerOrder player) {
        return returnHasAction();
    }

    @Override
    public boolean useTool(int idx) {
        return returnBoolean();
    }

    @Override
    public boolean canUseTools() {
        return returnBoolean();
    }

    @Override
    public boolean finishUsingTools() {
        return returnBoolean();
    }
}

class MockNewTurn implements InterfaceNewTurn {

    @Override
    public void newTurn() {
        // not relevant, void function in mock class
    }
}

public class IntegrationTest2 {

    @Test
    public void someTest() {
        PlayerOrder p1 = new PlayerOrder(0, 4);
        PlayerOrder p2 = new PlayerOrder(1, 4);
        PlayerOrder p3 = new PlayerOrder(2, 4);
        PlayerOrder p4 = new PlayerOrder(3, 4);

        // ulozenie figurok, posledny uz nema na vyber, musi ich davat na lov, preto treba niekde dat
        // automatic action done, a pozriet, co vsetko bude volane, ciel je dostat sa do stavu makeAction prveho
        /*
        String hasAction = "www";
        String actionResult = "AAA";
        String bool = "yyy";
         */
        // po znacku || to vsetko funguje, stav waiting for tool use pre hraca 0
        // ciel -  aby hrac pouzil tools uspesne
        String hasAction = "wwa nnnnnnnn ww || w";
        String actionResult = "T||& A";
        String bool = "yyy yn || yyyyn && y";

        MockClass everything = new MockClass(hasAction, actionResult, bool);
        MockNewTurn mockNewTurn = new MockNewTurn();

        Map<PlayerOrder, InterfaceFeedTribe> feedTribeMap = new HashMap<>();
        feedTribeMap.put(p1, everything);
        feedTribeMap.put(p2, everything);
        feedTribeMap.put(p3, everything);
        feedTribeMap.put(p4, everything);

        Map<Location, InterfaceFigureLocation> places = new HashMap<>();
        places.put(Location.BUILDING_TILE1, everything);
        places.put(Location.TOOL_MAKER, everything);

        Map<PlayerOrder, InterfaceToolUse> interfaceToolUseCollection = new HashMap<>();
        interfaceToolUseCollection.put(p1, everything);
        interfaceToolUseCollection.put(p2, everything);
        interfaceToolUseCollection.put(p3, everything);
        interfaceToolUseCollection.put(p4, everything);

        GamePhaseController gpc = GamePhaseControllerFactory.createGamePhaseController(everything, feedTribeMap, places, mockNewTurn, interfaceToolUseCollection, p1);
        gpc.placeFigures(p1, Location.BUILDING_TILE1, 1);
        System.out.println(gpc.state());
        gpc.placeFigures(p2, Location.TOOL_MAKER, 1);
        System.out.println(gpc.state());
        gpc.placeFigures(p3, Location.BUILDING_TILE1, 1);
        System.out.println(gpc.state());

        gpc.makeAction(p1, Location.BUILDING_TILE1, new HashSet<>(), new HashSet<>());
        System.out.println(gpc.state());
        assertTrue(gpc.useTools(p1, 5));
        System.out.println(gpc.state());
        gpc.noMoreToolsThisThrow(p1);
        System.out.println(gpc.state());
    }


}
