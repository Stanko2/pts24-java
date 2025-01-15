package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.CivilisationCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class GameBoardFactory {
    public static GameBoard createGameBoard(ArrayList<Player> players, ArrayList<CivilisationCard> cards, ArrayList<Building> buildings) {
        var stacks = new ArrayList<Stack<Building>>();
        for (int i = 0; i < players.size(); i++) {
            stacks.add(new Stack<>());
        }
        for (int i = 0; i < buildings.size(); i++) {
            stacks.get(i% players.size()).push(buildings.get(i));
        }
        
        return new GameBoard(players, stacks, new CivilizationCardDeck(cards));
    }
}
