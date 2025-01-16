package sk.uniba.fmph.dcs.stone_age;

import sk.uniba.fmph.dcs.game_board.Building;
import sk.uniba.fmph.dcs.game_board.GameBoardFactory;
import sk.uniba.fmph.dcs.game_board.Player;
import sk.uniba.fmph.dcs.game_board.SimpleBuilding;
import sk.uniba.fmph.dcs.game_phase_controller.*;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardFactory;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StoneAgeGameFactory {
    public static StoneAgeGame createStoneAge(int numPlayers) {

        var playerOrders = new HashMap<Integer, PlayerOrder>();
        for (int i = 0; i < numPlayers; i++) {
            playerOrders.put(i, new PlayerOrder(i, numPlayers));
        }

        var observable = new StoneAgeObservable();
        var playerBoardGameBoards = new HashMap<PlayerOrder, PlayerBoardGameBoardFacade>();
        var playerBoards = new HashMap<PlayerOrder, PlayerBoard>();

        for (int i = 0; i < numPlayers; i++) {
            var playerBoard = PlayerBoardFactory.createPlayerBoard(5, 3);
            playerBoardGameBoards.put(playerOrders.get(i), playerBoard.getValue());
            playerBoards.put(playerOrders.get(i), playerBoard.getKey());
        }



        var players = new ArrayList<Player>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(playerOrders.get(i), playerBoardGameBoards.get(i)));
        }

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


        var gameBoard = GameBoardFactory.createGameBoard(players, cards, buildings);


        var dispatcher = new HashMap<GamePhase, InterfaceGamePhaseState>();
        dispatcher.put(GamePhase.PLACE_FIGURES, new PlaceFigureState(gameBoard.getLocations()));

        dispatcher.put(GamePhase.FEED_TRIBE, new FeedTribeState(convert(playerBoardGameBoards)));
        dispatcher.put(GamePhase.MAKE_ACTION, new MakeActionState(gameBoard.getLocations()));
        dispatcher.put(GamePhase.NEW_ROUND, new NewRoundState(gameBoard.getLocations().values().toArray(new InterfaceFigureLocation[0]), convert(playerBoardGameBoards)));
        dispatcher.put(GamePhase.GAME_END, new GameEndState());
        dispatcher.put(GamePhase.WAITING_FOR_TOOL_USE, new WaitingForToolUse(convert(playerBoardGameBoards)));
        var gamePhaseController = new GamePhaseController(dispatcher, playerOrders.get(0));

        return new StoneAgeGame(playerOrders, observable, gamePhaseController, playerBoards, gameBoard);
    }

    private static <T,K> Map<PlayerOrder, T> convert(Map<PlayerOrder, K> map) {
        var res = new HashMap<PlayerOrder, T>();
        for(var x : map.keySet()) {
            res.put(x, (T)map.get(x));
        }
        return res;
    }
}
