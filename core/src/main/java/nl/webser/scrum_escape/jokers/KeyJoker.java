package nl.webser.scrum_escape.jokers;
import nl.webser.scrum_escape.rooms.Room;

public class KeyJoker implements JokerStrategy {
    @Override
    public void gebruik(Room room) {
        room.applyKeyJoker();
    }
}