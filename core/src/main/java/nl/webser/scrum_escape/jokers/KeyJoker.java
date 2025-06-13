package nl.webser.scrum_escape.jokers;

import nl.webser.scrum_escape.rooms.BaseRoom;

public class KeyJoker extends Joker {
    public KeyJoker() {
        super(JokerType.KEY_JOKER);
    }

    @Override
    public void gebruik(BaseRoom room) {
        room.applyKeyJoker();
    }
}