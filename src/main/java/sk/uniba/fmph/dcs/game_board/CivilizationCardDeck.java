package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.CivilisationCard;

import java.util.*;

public final class CivilizationCardDeck {
    private Queue<CivilisationCard> queue;

    public CivilizationCardDeck(final ArrayList<CivilisationCard> cards) {
        queue = new LinkedList<>();
        Collections.shuffle(cards);
        for (var c : cards) {
            queue.add(c);
        }
    }

    public Optional<CivilisationCard> getTop() {
        if (queue.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(queue.remove());
    }

    public String state() {
        return new JSONObject(Map.of("size", queue.size())).toString();
    }
}
