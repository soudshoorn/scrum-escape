package nl.webser.scrum_escape;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.webser.scrum_escape.screens.GameScreen;
import nl.webser.scrum_escape.screens.EndScreen;
import nl.webser.scrum_escape.screens.SuccessScreen;

/**
 * ScrumEscapeGame is de hoofdklasse van het spel.
 * Deze klasse erft over van LibGDX's Game klasse en beheert de verschillende schermen.
 */
public class ScrumEscapeGame extends Game {
    private SpriteBatch batch;

    /**
     * Wordt aangeroepen bij het starten van het spel.
     * Initialiseert de game en laadt assets.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        AssetManager.getInstance().loadAssets();
        setScreen(new GameScreen());
    }

    /**
     * Ruimt resources op wanneer het spel wordt afgesloten.
     */
    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        AssetManager.getInstance().dispose();
    }

    /**
     * Toont het game over scherm.
     * Wordt aangeroepen wanneer de speler het spel verliest.
     */
    public void showGameOver() {
        setScreen(new EndScreen(this));
    }

    public void showSuccess(float timeElapsed) {
        setScreen(new SuccessScreen(this, timeElapsed));
    }
}