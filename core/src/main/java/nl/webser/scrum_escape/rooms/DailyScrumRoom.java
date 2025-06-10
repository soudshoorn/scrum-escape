package nl.webser.scrum_escape.rooms;

import nl.webser.scrum_escape.screens.GameScreen;

public class DailyScrumRoom implements Room {
    private final GameScreen gameScreen;

    public DailyScrumRoom(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void applyHintJoker() {
        gameScreen.showHint();
    }

    @Override
    public boolean applyKeyJoker() {
        // Open de deur zonder straf, monster resetten, etc.
        gameScreen.openDoorWithKeyJoker();
        return true;
    }
    @Override
    public void activateAssistant() {
        gameScreen.showHint();
        gameScreen.addEducationalAid("Stappenplan: Check de Definition of Done!");
        gameScreen.showMessage("Je denkt als een echte product owner!");
    }

    @Override
    public String getName() {
        return "Daily Scrum";
    }
}