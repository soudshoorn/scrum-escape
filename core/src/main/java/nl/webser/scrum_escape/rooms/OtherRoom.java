package nl.webser.scrum_escape.rooms;

import nl.webser.scrum_escape.screens.GameScreen;

public class OtherRoom implements Room {
    private final GameScreen gameScreen;

    public OtherRoom(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void applyHintJoker() {
        gameScreen.showHint();
    }

    @Override
    public boolean applyKeyJoker() {
        gameScreen.showMessage("De Key Joker werkt alleen in de Daily Scrum of Review kamer!");
        return false;
    }

    @Override
    public String getName() {
        return "Other";
    }
}