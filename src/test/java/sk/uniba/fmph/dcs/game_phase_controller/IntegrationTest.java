package sk.uniba.fmph.dcs.game_phase_controller;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

class TakeRewardMock implements InterfaceTakeReward {
    private final List<Boolean> expectedBooleanResult;
    private final List<HasAction> expectedHasAction;

    TakeRewardMock(List<Boolean> expectedBooleanResult, List<HasAction> expectedHasAction) {
        this.expectedBooleanResult = expectedBooleanResult;
        this.expectedHasAction = expectedHasAction;

    }

    @Override
    public boolean takeReward(PlayerOrder player, Effect reward) {
        if (expectedBooleanResult.isEmpty()) return true;
        return expectedBooleanResult.removeFirst();
    }

    @Override
    public HasAction tryMakeAction(PlayerOrder player) {
        assert !expectedHasAction.isEmpty();
        return expectedHasAction.removeFirst();
    }
}

class FeedTribeMock implements InterfaceFeedTribe {
    private final List<Boolean> feedTribeExpected;
    private final List<Boolean> feedTribeIfEnoughExpected;
    private final List<Boolean> isTribeFedExpected;

    FeedTribeMock(final List<Boolean> feedTribeExpected, final List<Boolean> feedTribeIfEnoughExpected, final List<Boolean> isTribeFedExpected) {
        this.feedTribeExpected = feedTribeExpected;
        this.isTribeFedExpected = isTribeFedExpected;
        this.feedTribeIfEnoughExpected = feedTribeIfEnoughExpected;
    }

    @Override
    public boolean feedTribeIfEnoughFood() {
        if (feedTribeIfEnoughExpected.isEmpty()) return true;
        return feedTribeIfEnoughExpected.removeFirst();
    }

    @Override
    public boolean feedTribe(final Collection<Effect> resources) {
        if (feedTribeExpected.isEmpty()) return true;
        return feedTribeExpected.removeFirst();
    }

    @Override
    public boolean doNotFeedThisTurn() {
        return true;
    }

    @Override
    public boolean isTribeFed() {
        if (isTribeFedExpected.isEmpty()) return true;
        return isTribeFedExpected.removeFirst();
    }
}

class FigureLocationMock implements InterfaceFigureLocation {
    private List<Boolean> newTurnExpected;
    private List<ActionResult> makeActionExpected;
    private List<HasAction> tryActionExpected;
    private List<Boolean> placeFiguresExpected;
    private List<HasAction> tryPlaceFiguresExpected;

    FigureLocationMock(final List<Boolean> newTurnExpected, final List<ActionResult> makeActionExpected, final List<HasAction> tryActionExpected,
                       final List<Boolean> placeFiguresExpected, final List<HasAction> tryPlaceFiguresExpected) {
        this.newTurnExpected = newTurnExpected;
        this.placeFiguresExpected = placeFiguresExpected;
        this.makeActionExpected = makeActionExpected;
        this.tryActionExpected = tryActionExpected;
        this.tryPlaceFiguresExpected = tryPlaceFiguresExpected;
    }

    @Override
    public boolean placeFigures(final PlayerOrder player, final int figureCount) {
        if (placeFiguresExpected.isEmpty()) return true;
        return placeFiguresExpected.removeFirst();
    }

    @Override
    public HasAction tryToPlaceFigures(final PlayerOrder player, final int count) {
        if (tryPlaceFiguresExpected.isEmpty()) return HasAction.NO_ACTION_POSSIBLE;
        return tryPlaceFiguresExpected.removeFirst();
    }

    @Override
    public ActionResult makeAction(final PlayerOrder player, final Collection<Effect> inputResources, final Collection<Effect> outputResources) {
        if (makeActionExpected.isEmpty()) return ActionResult.FAILURE;
        return makeActionExpected.removeFirst();
    }

    @Override
    public boolean skipAction(final PlayerOrder player) {
        return true;
    }

    @Override
    public HasAction tryToMakeAction(final PlayerOrder player) {
        if (tryActionExpected.isEmpty()) return HasAction.NO_ACTION_POSSIBLE;
        System.out.println(tryActionExpected);
        return tryActionExpected.removeFirst();
    }

    @Override
    public boolean newTurn() {
        assert !newTurnExpected.isEmpty();
        return newTurnExpected.removeFirst();
    }
}

class NewTurnMock implements InterfaceNewTurn {
    @Override
    public void newTurn() {
        // no need to do anything
    }
}

class ToolUseMock implements InterfaceToolUse {
    private List<Boolean> canUseExpected;

    ToolUseMock(List<Boolean> canUseExpected) {
        this.canUseExpected = canUseExpected;
    }

    @Override
    public boolean useTool(final int idx) {
        if (canUseExpected.isEmpty()) return true;
        return canUseExpected.getFirst();
    }

    @Override
    public boolean canUseTools() {
        if (canUseExpected.isEmpty()) return true;
        return canUseExpected.removeFirst();
    }

    @Override
    public boolean finishUsingTools() {
        return true;
    }
}

public class IntegrationTest {
    // normal game test, only valid actions provided, game end indicated by FigureLocationAdaptor
    @Test
    public void firstTest() {
        // setup
        PlayerOrder p1 = new PlayerOrder(0, 4);
        PlayerOrder p2 = new PlayerOrder(1, 4);
        PlayerOrder p3 = new PlayerOrder(2, 4);
        PlayerOrder p4 = new PlayerOrder(3, 4);
        InterfaceFeedTribe interfaceFeedTribe1 = new FeedTribeMock(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        List<HasAction> a1 = new ArrayList<>();
        a1.add(HasAction.WAITING_FOR_PLAYER_ACTION);

        List<ActionResult> a2 = new ArrayList<>();
        a2.add(ActionResult.ACTION_DONE);
        a2.add(ActionResult.FAILURE);

        List<Boolean> a3 = new ArrayList<>();
        a3.add(false);
        a3.add(true);

        InterfaceFigureLocation figureLocationMock = new FigureLocationMock(a3, a2, a1, new ArrayList<>(), new ArrayList<>());

        InterfaceTakeReward interfaceTakeReward = new TakeRewardMock(Arrays.asList(true, true, true, true), Arrays.asList(HasAction.WAITING_FOR_PLAYER_ACTION, HasAction.WAITING_FOR_PLAYER_ACTION, HasAction.WAITING_FOR_PLAYER_ACTION, HasAction.AUTOMATIC_ACTION_DONE));
        Map<PlayerOrder, InterfaceFeedTribe> interfaceFeedTribeCollection = new HashMap<>();
        interfaceFeedTribeCollection.put(p1, interfaceFeedTribe1);
        interfaceFeedTribeCollection.put(p2, interfaceFeedTribe1);
        interfaceFeedTribeCollection.put(p3, interfaceFeedTribe1);
        interfaceFeedTribeCollection.put(p4, interfaceFeedTribe1);
        Map<Location, InterfaceFigureLocation> places = new HashMap<>();
        places.put(Location.BUILDING_TILE1, figureLocationMock);
        InterfaceNewTurn interfaceNewTurn1 = new NewTurnMock();
        Map<PlayerOrder, InterfaceToolUse> interfaceToolUseCollection = new HashMap<>();
        interfaceToolUseCollection.put(p1, new ToolUseMock(new ArrayList<>()));
        interfaceToolUseCollection.put(p2, new ToolUseMock(new ArrayList<>()));
        interfaceToolUseCollection.put(p3, new ToolUseMock(new ArrayList<>()));
        interfaceToolUseCollection.put(p4, new ToolUseMock(new ArrayList<>()));

        GamePhaseController gpc = GamePhaseControllerFactory.createGamePhaseController(interfaceTakeReward, interfaceFeedTribeCollection, places, interfaceNewTurn1, interfaceToolUseCollection, p1);

        System.out.println(gpc.state());
        gpc.placeFigures(p1, Location.BUILDING_TILE1, 1);
        System.out.println(gpc.state());
        /*
        System.out.println(gpc.state());
        gpc.placeFigures(p2, Location.BUILDING_TILE1, 1);
        System.out.println(gpc.state());
        gpc.placeFigures(p3, Location.BUILDING_TILE1, 1);
        gpc.placeFigures(p4, Location.BUILDING_TILE1, 1);
        gpc.placeFigures(p1, Location.BUILDING_TILE1, 1);
        gpc.placeFigures(p2, Location.BUILDING_TILE1, 1);
        gpc.placeFigures(p3, Location.BUILDING_TILE1, 1);
        gpc.placeFigures(p4, Location.BUILDING_TILE1, 1);

        gpc.makeAction(p1, Location.BUILDING_TILE1, new ArrayList<>(), new ArrayList<>());
        gpc.makeAction(p2, Location.BUILDING_TILE1, new ArrayList<>(), new ArrayList<>());
        gpc.makeAction(p3, Location.BUILDING_TILE1, new ArrayList<>(), new ArrayList<>());
        gpc.makeAllPlayersTakeARewardChoice(p1, Effect.WOOD);
        gpc.makeAllPlayersTakeARewardChoice(p2, Effect.WOOD);
        gpc.makeAllPlayersTakeARewardChoice(p3, Effect.WOOD);
        gpc.makeAllPlayersTakeARewardChoice(p4, Effect.WOOD);
        gpc.makeAction(p4, Location.BUILDING_TILE1, new ArrayList<>(), new ArrayList<>());

        gpc.feedTribe(p1, new ArrayList<>());
        gpc.feedTribe(p2, new ArrayList<>());
        gpc.feedTribe(p3, new ArrayList<>());
        gpc.feedTribe(p4, new ArrayList<>());

        gpc.placeFigures(p1, Location.BUILDING_TILE1, 1);
        gpc.placeFigures(p2, Location.BUILDING_TILE1, 1);
        gpc.placeFigures(p3, Location.BUILDING_TILE1, 1);
        gpc.placeFigures(p4, Location.BUILDING_TILE1, 1);
        gpc.placeFigures(p1, Location.BUILDING_TILE1, 1);
        gpc.placeFigures(p2, Location.BUILDING_TILE1, 1);
        gpc.placeFigures(p3, Location.BUILDING_TILE1, 1);
        gpc.placeFigures(p4, Location.BUILDING_TILE1, 1);

        gpc.makeAction(p1, Location.BUILDING_TILE1, new ArrayList<>(), new ArrayList<>());
        gpc.makeAction(p2, Location.BUILDING_TILE1, new ArrayList<>(), new ArrayList<>());
        gpc.makeAction(p3, Location.BUILDING_TILE1, new ArrayList<>(), new ArrayList<>());
        gpc.makeAction(p4, Location.BUILDING_TILE1, new ArrayList<>(), new ArrayList<>());

        gpc.feedTribe(p1, new ArrayList<>());
        gpc.feedTribe(p2, new ArrayList<>());
        System.out.println(gpc.state());
        gpc.feedTribe(p3, new ArrayList<>());
        gpc.feedTribe(p4, new ArrayList<>());
        System.out.println(gpc.state());

         */
    }
}
