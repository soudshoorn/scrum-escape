package nl.webser.scrum_escape.rooms;

public interface Room {
    void applyHintJoker();
    boolean applyKeyJoker();
    void activateAssistant();
    String getName();
}