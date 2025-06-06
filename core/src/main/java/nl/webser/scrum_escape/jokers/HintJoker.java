package nl.webser.scrum_escape.jokers;
import nl.webser.scrum_escape.rooms.Room;

public class HintJoker implements JokerStrategy {
    @Override
    public void gebruik(Room room) {
        room.applyHintJoker();
    }
}