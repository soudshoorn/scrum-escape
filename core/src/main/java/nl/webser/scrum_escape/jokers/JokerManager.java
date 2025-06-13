package nl.webser.scrum_escape.jokers;

import java.util.ArrayList;
import java.util.List;

public class JokerManager {
    private final List<Joker> availableJokers;
    private Joker chosenJoker;

    public JokerManager() {
        availableJokers = new ArrayList<>();
        availableJokers.add(new HintJoker());
        availableJokers.add(new KeyJoker());
    }

    public void chooseJoker(int index) {
        if (index >= 0 && index < availableJokers.size()) {
            chosenJoker = availableJokers.get(index);
        }
    }

    public Joker getGekozenJoker() {
        return chosenJoker;
    }

    public void gebruikJoker(Joker joker) {
        if (joker.getType() == Joker.JokerType.KEY_JOKER) {
            availableJokers.removeIf(j -> j.getType() == Joker.JokerType.KEY_JOKER);
        }
        chosenJoker = null;
    }
}
