package nl.webser.scrum_escape.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.webser.scrum_escape.observer.Observer;

/**
 * Monster representeert een monster in het spel.
 * Deze klasse implementeert het Observer pattern voor het reageren op foute antwoorden.
 */
public class Monster implements Observer {
    // Hoe lang het monster erover doet om de speler te bereiken (in seconden)
    private static final float MONSTER_DURATION = 10f; // Pas deze waarde aan voor langzamer monster
    // Offset waarmee het monster naast de speler start na een fout antwoord
    private static final float MONSTER_START_OFFSET = 50f; // pixels
    // Monster eigenschappen
    private Texture texture;
    private Vector2 startPosition;
    private Vector2 position;
    private Vector2 targetPosition;
    private boolean active = false;
    private float timer = 0f;
    private boolean reachedPlayer = false;

    /**
     * Maakt een nieuw monster aan op de gegeven positie.
     * 
     * @param x X-coördinaat van het monster
     * @param y Y-coördinaat van het monster
     */
    public Monster(float x, float y) {
        texture = new Texture("monster.png");
        startPosition = new Vector2(x, y);
        position = new Vector2(x, y);
        targetPosition = new Vector2(x, y); // Wordt gezet bij activeren
    }

    /**
     * Activeert het monster en zet de timer en target.
     * @param player De speler waar het monster naartoe moet
     */
    public void activate(Player player) {
        active = true;
        timer = 0f;
        reachedPlayer = false;
        Rectangle playerBounds = player.getBounds();
        // Startpositie op een vaste offset van de speler (diagonaal linksboven)
        startPosition.set(playerBounds.x - MONSTER_START_OFFSET, playerBounds.y - MONSTER_START_OFFSET);
        position.set(startPosition);
        targetPosition.set(playerBounds.x, playerBounds.y);
    }

    /**
     * Update de positie van het monster. Beweegt in 10 seconden naar de speler.
     * @param delta Tijd sinds laatste update
     * @param player De speler (voor collision check)
     * @return true als het monster de speler heeft bereikt
     */
    public boolean update(float delta, Player player) {
        if (!active || reachedPlayer) return false;
        timer += delta;
        float t = Math.min(timer / MONSTER_DURATION, 1f);
        // Lineaire interpolatie naar de speler
        Rectangle playerBounds = player.getBounds();
        targetPosition.set(playerBounds.x, playerBounds.y);
        position.set(startPosition).lerp(targetPosition, t);
        // Check collision
        Rectangle monsterRect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
        if (monsterRect.overlaps(playerBounds) || t >= 1f) {
            reachedPlayer = true;
            active = false;
            return true;
        }
        return false;
    }

    /**
     * Rendert het monster als het actief is.
     */
    public void render(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, position.x, position.y);
        }
    }

    /**
     * Ruimt resources op.
     */
    public void dispose() {
        texture.dispose();
    }

    /**
     * Wordt aangeroepen wanneer een fout antwoord is gegeven.
     * Activeert het monster.
     */
    @Override
    public void onIncorrectAnswer() {
        active = true;
        timer = 0f;
        reachedPlayer = false;
    }

    public boolean isActive() {
        return active;
    }

    public boolean hasReachedPlayer() {
        return reachedPlayer;
    }

    public void reset() {
        active = false;
        timer = 0f;
        reachedPlayer = false;
        position.set(startPosition);
    }
}