package nl.webser.scrum_escape.jokers;

import nl.webser.scrum_escape.rooms.BaseRoom;

public abstract class Joker implements JokerStrategy {
    public enum JokerType {
        HINT_JOKER,
        KEY_JOKER
    }

    protected final JokerType type;

    protected Joker(JokerType type) {
        this.type = type;
    }

    public JokerType getType() {
        return type;
    }

    @Override
    public abstract void gebruik(BaseRoom room);
}