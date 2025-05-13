package nl.webser.scrum_escape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.Keys;

import nl.webser.scrum_escape.GameState;
import nl.webser.scrum_escape.ScrumEscapeGame;

public class SuccessScreen implements Screen {
    private final ScrumEscapeGame game;
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final BitmapFont titleFont;
    private final BitmapFont scoreFont;
    private final BitmapFont timeFont;
    private final BitmapFont exitFont;
    private final GameState gameState;
    private float timeElapsed;

    public SuccessScreen(ScrumEscapeGame game, float timeElapsed) {
        this.game = game;
        this.gameState = GameState.getInstance();
        this.timeElapsed = timeElapsed;
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 640);
        
        batch = new SpriteBatch();
        
        // Create larger fonts
        titleFont = new BitmapFont();
        titleFont.getData().setScale(2.0f);
        
        scoreFont = new BitmapFont();
        scoreFont.getData().setScale(1.5f);
        
        timeFont = new BitmapFont();
        timeFont.getData().setScale(1.5f);
        
        exitFont = new BitmapFont();
        exitFont.getData().setScale(1.2f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.8f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        
        // Draw title
        titleFont.draw(batch, "Gefeliciteerd!", 250, 500);
        
        // Draw final score
        scoreFont.draw(batch, "Final Score: " + gameState.getScore(), 300, 400);
        
        // Draw time
        int minutes = (int)(timeElapsed / 60);
        int seconds = (int)(timeElapsed % 60);
        timeFont.draw(batch, String.format("Tijd: %02d:%02d", minutes, seconds), 300, 350);
        
        // Draw exit text
        exitFont.draw(batch, "Druk op ESC om af te sluiten", 250, 200);
        
        batch.end();

        // Check for exit
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        titleFont.dispose();
        scoreFont.dispose();
        timeFont.dispose();
        exitFont.dispose();
    }
} 