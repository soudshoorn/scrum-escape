package nl.webser.scrum_escape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input.Keys;

import nl.webser.scrum_escape.GameState;
import nl.webser.scrum_escape.ScrumEscapeGame;

/**
 * EndScreen is het scherm dat wordt getoond wanneer het spel is afgelopen.
 * Dit kan zijn door het winnen van het spel of door het verliezen.
 */
public class EndScreen implements Screen {
    private final ScrumEscapeGame game;
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont titleFont;
    private final BitmapFont scoreFont;
    private final BitmapFont buttonFont;
    private final Rectangle restartButton;
    private final GameState gameState;

    /**
     * Maakt een nieuw end screen aan.
     * 
     * @param game De hoofdgame instantie
     */
    public EndScreen(ScrumEscapeGame game) {
        this.game = game;
        this.gameState = GameState.getInstance();
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 640);
        
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        
        // Create larger fonts
        titleFont = new BitmapFont();
        titleFont.getData().setScale(2.0f);
        
        scoreFont = new BitmapFont();
        scoreFont.getData().setScale(1.5f);
        
        buttonFont = new BitmapFont();
        buttonFont.getData().setScale(1.2f);
        
        // Create restart button
        restartButton = new Rectangle(300, 200, 200, 50);
    }

    /**
     * Rendert het end screen.
     * Toont een fade-in effect en een restart bericht.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();
        
        // Draw title
        titleFont.draw(batch, "Game Over!", 300, 500);
        
        // Draw final score
        scoreFont.draw(batch, "Final Score: " + gameState.getScore(), 300, 400);
        
        // Draw restart button text
        buttonFont.draw(batch, "Press SPACE to Restart", 250, 250);
        
        batch.end();

        // Check for restart
        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            gameState.reset();
            game.setScreen(new GameScreen());
            dispose();
        }
    }

    /**
     * Past het scherm aan bij een grootte verandering.
     */
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

    /**
     * Ruimt resources op wanneer het scherm wordt afgesloten.
     */
    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        titleFont.dispose();
        scoreFont.dispose();
        buttonFont.dispose();
    }
} 