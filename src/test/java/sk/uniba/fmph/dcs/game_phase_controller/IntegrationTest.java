package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;
import java.util.List;

class TakeRewardMock implements InterfaceTakeReward {
    private final List<Boolean> expectedBooleanResult;
    private final List<HasAction> expectedHasAction;

    TakeRewardMock(List<Boolean> expectedBooleanResult, List<HasAction> expectedHasAction) {
        this.expectedBooleanResult = expectedBooleanResult;
        this.expectedHasAction = expectedHasAction;

    }

    @Override
    public boolean takeReward(PlayerOrder player, Effect reward) {
        assert !expectedBooleanResult.isEmpty();
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
    private final List<Boolean> doNotFeedExpected;
    private final List<Boolean> feedTribeIfEnoughExpected;
    private final List<Boolean> isTribeFedExpected;

    FeedTribeMock(final List<Boolean> feedTribeExpected, final List<Boolean> doNotFeedExpected, final List<Boolean> feedTribeIfEnoughExpected, final List<Boolean> isTribeFedExpected) {
        this.feedTribeExpected = feedTribeExpected;
        this.doNotFeedExpected = doNotFeedExpected;
        this.isTribeFedExpected = isTribeFedExpected;
        this.feedTribeIfEnoughExpected = feedTribeIfEnoughExpected;
    }

    @Override
    public boolean feedTribeIfEnoughFood() {
        assert !feedTribeIfEnoughExpected.isEmpty();
        return feedTribeIfEnoughExpected.removeFirst();
    }

    @Override
    public boolean feedTribe(final Collection<Effect> resources) {
        assert !feedTribeExpected.isEmpty();
        return feedTribeExpected.removeFirst();
    }

    @Override
    public boolean doNotFeedThisTurn() {
        assert !doNotFeedExpected.isEmpty();
        return feedTribeExpected.removeFirst();
    }

    @Override
    public boolean isTribeFed() {
        assert !isTribeFedExpected.isEmpty();
        return isTribeFedExpected.removeFirst();
    }
}

class FigureLocationMock implements InterfaceFigureLocation {
    private final List<Boolean> newTurnExpected;
    private final List<ActionResult> makeActionExpected;
    private final List<HasAction> tryActionExpected;
    private final List<Boolean> placeFiguresExpected;
    private final List<HasAction> tryPlaceFiguresExpected;

    FigureLocationMock(final List<Boolean> newTurnExpected, final List<ActionResult> makeActionExpected, final List<HasAction> tryActionExpected,
                       final List<Boolean> placeFiguresExpected, final List<HasAction> tryPlaceFiguresExpected) {
        this.newTurnExpected = newTurnExpected;
        this.placeFiguresExpected = placeFiguresExpected;
        this.makeActionExpected = makeActionExpected;
        this.tryActionExpected = tryActionExpected;
        this.tryPlaceFiguresExpected = tryPlaceFiguresExpected;
    }

    @Override
    public boolean placeFigures(PlayerOrder player, int figureCount) {
        assert !placeFiguresExpected.isEmpty();
        return placeFiguresExpected.removeFirst();
    }

    @Override
    public HasAction tryToPlaceFigures(PlayerOrder player, int count) {
        assert !tryPlaceFiguresExpected.isEmpty();
        return tryPlaceFiguresExpected.removeFirst();
    }

    @Override
    public ActionResult makeAction(PlayerOrder player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        assert !makeActionExpected.isEmpty();
        return makeActionExpected.removeFirst();
    }

    @Override
    public boolean skipAction(PlayerOrder player) {
        return true;
    }

    @Override
    public HasAction tryToMakeAction(PlayerOrder player) {
        assert !tryActionExpected.isEmpty();
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
    private final List<Boolean> canUseExpected;

    ToolUseMock(final List<Boolean> canUseExpected) {
        this.canUseExpected = canUseExpected;
    }

    @Override
    public boolean useTool(int idx) {
        assert !canUseExpected.isEmpty();
        return canUseExpected.getFirst();
    }

    @Override
    public boolean canUseTools() {
        assert !canUseExpected.isEmpty();
        return canUseExpected.removeFirst();
    }

    @Override
    public boolean finishUsingTools() {
        return true;
    }
}

public class IntegrationTest {
    // normal game test, only valid actions provided, game end indicated by
}
