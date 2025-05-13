package nl.webser.scrum_escape.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import nl.webser.scrum_escape.observer.Observer;

/**
 * Monster representeert een monster in het spel.
 * Deze klasse implementeert het Observer pattern voor het reageren op foute antwoorden.
 */
public class Monster implements Observer {
    // Monster eigenschappen
    private Texture texture;
    private Vector2 position;
    private boolean active = false;

    /**
     * Maakt een nieuw monster aan op de gegeven positie.
     * 
     * @param x X-coördinaat van het monster
     * @param y Y-coördinaat van het monster
     */
    public Monster(float x, float y) {
        texture = new Texture("monster.png");
        position = new Vector2(x, y);
    }

    /**
     * Update de positie van het monster.
     * Het monster beweegt naar de speler toe als het actief is.
     * 
     * @param delta Tijd sinds laatste update
     * @param player De speler om naar toe te bewegen
     */
    public void update(float delta, Player player) {
        if (active) {
            // Bereken richting naar speler
            Vector2 dir = new Vector2(player.getBounds().x, player.getBounds().y)
                .sub(position)
                .nor();
            
            // Beweeg in die richting
            position.add(dir.scl(100 * delta));
        }
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
    }
}