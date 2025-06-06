package nl.webser.scrum_escape.entities;

import nl.webser.scrum_escape.jokers.KeyJoker;
import nl.webser.scrum_escape.jokers.HintJoker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import nl.webser.scrum_escape.jokers.*;

public class Player {
    private static final float MOVEMENT_SPEED = 60f;
    private static final float PLAYER_SIZE = 16f;
    private static final float FRAME_DURATION = 0.15f;

    private float x;
    private float y;
    private final Rectangle bounds;
    private TextureRegion currentTexture;
    private boolean frozen = false;
    private TiledMapTileLayer collisionLayer;
    private BitmapFont font;

    private final Texture[] walkFrames;
    private int frameIndex = 0;
    private float frameTimer = 0;

    private JokerStrategy jokerStrategy;

    private JokerManager jokerManager;

    public Player(float x, float y, TiledMap map) {
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, PLAYER_SIZE, PLAYER_SIZE);
        this.walkFrames = new Texture[]{
            new Texture("player1.png"),
            new Texture("player2.png"),
            new Texture("player3.png"),
            new Texture("player4.png")
        };
        this.currentTexture = new TextureRegion(walkFrames[0]);
        this.collisionLayer = (TiledMapTileLayer) map.getLayers().get("Base");
        this.font = new BitmapFont();
        this.jokerManager = jokerManager;
    }

    public Player(int x, int y, TiledMap map, Rectangle bounds, Texture[] walkFrames) {
        this.bounds = bounds;
        this.walkFrames = walkFrames;
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)
            || Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            frameTimer += delta;
            if (frameTimer >= FRAME_DURATION) {
                frameIndex = (frameIndex + 1) % walkFrames.length;
                currentTexture = new TextureRegion(walkFrames[frameIndex]);
                frameTimer = 0;
            }
        }

        if (frozen) return;

        float oldX = x;
        float oldY = y;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) x -= MOVEMENT_SPEED * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += MOVEMENT_SPEED * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) y += MOVEMENT_SPEED * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) y -= MOVEMENT_SPEED * delta;

        bounds.x = x;
        bounds.y = y;

        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > 800 - PLAYER_SIZE) x = 800 - PLAYER_SIZE;
        if (y > 600 - PLAYER_SIZE) y = 600 - PLAYER_SIZE;

        int tileX = (int) (x / collisionLayer.getTileWidth());
        int tileY = (int) (y / collisionLayer.getTileHeight());
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);

        if (cell != null && cell.getTile() != null) {
            Object blocked = cell.getTile().getProperties().get("blocked");
            if (blocked instanceof Boolean && (boolean) blocked) {
                x = oldX;
                y = oldY;
            }
        }

    }

    public void render(SpriteBatch batch) {
        batch.draw(currentTexture, x, y, PLAYER_SIZE, PLAYER_SIZE);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void dispose() {
        for (Texture t : walkFrames) {
            t.dispose();
        }
        font.dispose();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.bounds.x = x;
        this.bounds.y = y;
    }

    public void moveLeft() {
        if (frozen) return;
        float oldX = x;
        x -= MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
        bounds.x = x;
        if (checkCollision()) {
            x = oldX;
            bounds.x = x;
        }
    }

    public void moveRight() {
        if (frozen) return;
        float oldX = x;
        x += MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
        bounds.x = x;
        if (checkCollision()) {
            x = oldX;
            bounds.x = x;
        }
    }

    public void moveUp() {
        if (frozen) return;
        float oldY = y;
        y += MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
        bounds.y = y;
        if (checkCollision()) {
            y = oldY;
            bounds.y = y;
        }
    }

    public void moveDown() {
        if (frozen) return;
        float oldY = y;
        y -= MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
        bounds.y = y;
        if (checkCollision()) {
            y = oldY;
            bounds.y = y;
        }
    }

    private boolean checkCollision() {
        int tileX = (int)(x / collisionLayer.getTileWidth());
        int tileY = (int)(y / collisionLayer.getTileHeight());
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);
        if (cell != null && cell.getTile() != null) {
            Object blocked = cell.getTile().getProperties().get("blocked");
            if (blocked instanceof Boolean && (boolean) blocked) {
                return true;
            }
        }
        return x < 0 || y < 0 || x > 800 - PLAYER_SIZE || y > 600 - PLAYER_SIZE;
    }

    public void kiesJokerStrategie(JokerStrategy strategy) {
        this.jokerStrategy = strategy;
    }

}
