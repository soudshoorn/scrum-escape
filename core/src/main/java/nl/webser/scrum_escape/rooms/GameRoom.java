package nl.webser.scrum_escape.rooms;

import nl.webser.scrum_escape.screens.GameScreen;

/**
 * Simpele kamer implementatie voor het spel.
 * Handelt alle kamer functionaliteit af.
 */
public class GameRoom extends BaseRoom {
    public GameRoom(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    protected void applyHintJokerEffect() {
        gameScreen.showHint();
    }

    @Override
    protected boolean applyKeyJokerEffect() {
        gameScreen.openDoorWithKeyJoker();
        return true;
    }

    @Override
    protected void applyAssistantEffect() {
        String[] messages = {
            "Scrum is gebaseerd op empirische procescontrole: transparantie, inspectie en aanpassing.",
            "De vijf Scrum waarden zijn: moed, focus, toewijding, respect en openheid.",
            "Een Sprint is een container voor alle andere Scrum events. Het is een time-box van één maand of minder.",
            "Het Development Team bestaat uit professionals die het werk uitvoeren om een 'Done' increment te leveren.",
            "De Product Owner is verantwoordelijk voor het maximaliseren van de waarde van het product.",
            "De Daily Scrum is een 15-minuten durende bijeenkomst voor het Development Team om het werk te synchroniseren.",
            "Tijdens de Daily Scrum beantwoordt elk teamlid drie vragen:\n1. Wat heb ik gedaan?\n2. Wat ga ik doen?\n3. Zie ik obstakels?",
            "De Daily Scrum is NIET een status meeting voor de Scrum Master of Product Owner.",
            "Het Development Team is zelf verantwoordelijk voor het organiseren van de Daily Scrum.",
            "De Daily Scrum vindt elke dag op dezelfde tijd en plaats plaats."
        };
        int randomIndex = (int) (Math.random() * messages.length);
        gameScreen.showMessage(messages[randomIndex]);
    }
} 