package nl.webser.scrum_escape.jokers;

import nl.webser.scrum_escape.rooms.BaseRoom;

public class HintJoker extends Joker {
    public HintJoker() {
        super(JokerType.HINT_JOKER);
    }

    @Override
    public void gebruik(BaseRoom room) {
        room.applyHintJoker();
    }
}