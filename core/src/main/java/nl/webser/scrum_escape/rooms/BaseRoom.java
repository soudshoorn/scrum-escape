package nl.webser.scrum_escape.rooms;

import nl.webser.scrum_escape.screens.GameScreen;

/**
 * Basis klasse voor alle kamers in het spel.
 * Implementeert gemeenschappelijke functionaliteit.
 */
public abstract class BaseRoom {
    protected final GameScreen gameScreen;

    public BaseRoom(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void applyHintJoker() {
        applyHintJokerEffect();
    }

    public boolean applyKeyJoker() {
        return applyKeyJokerEffect();
    }

    public void activateAssistant() {
        applyAssistantEffect();
    }

    /**
     * Template method for applying hint joker effect.
     * To be implemented by concrete room classes.
     */
    protected abstract void applyHintJokerEffect();

    /**
     * Template method for applying key joker effect.
     * To be implemented by concrete room classes.
     * @return true if the key joker was successfully applied
     */
    protected abstract boolean applyKeyJokerEffect();

    /**
     * Template method for applying assistant effect.
     * To be implemented by concrete room classes.
     */
    protected abstract void applyAssistantEffect();
} 