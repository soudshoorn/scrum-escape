package nl.webser.scrum_escape.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import nl.webser.scrum_escape.GameState;
import nl.webser.scrum_escape.observer.DoorObserver;

/**
 * Door representeert een deur in het spel.
 * Deze klasse implementeert het Observer pattern voor het notificeren van deur status veranderingen.
 */
public class Door {
    // Deur identificatie en eigenschappen
    private final String doorId;
    private final String questionId;
    private final Rectangle bounds;
    private final TextureRegion closedTexture;
    private final TextureRegion openTexture;
    private boolean isOpen;
    private DoorObserver observer;

    /**
     * Maakt een nieuwe deur aan met de gegeven eigenschappen.
     * 
     * @param doorId Unieke identificatie van de deur
     * @param questionId ID van de vraag die bij deze deur hoort
     * @param x X-coördinaat van de deur
     * @param y Y-coördinaat van de deur
     * @param width Breedte van de deur
     * @param height Hoogte van de deur
     * @param closedTexture Texture voor de gesloten deur
     * @param openTexture Texture voor de open deur
     */
    public Door(String doorId, String questionId, float x, float y, float width, float height,
                TextureRegion closedTexture, TextureRegion openTexture) {
        this.doorId = doorId;
        this.questionId = questionId;
        this.bounds = new Rectangle(x, y, width, height);
        this.closedTexture = closedTexture;
        this.openTexture = openTexture;
        this.isOpen = GameState.getInstance().isDoorOpened(doorId);
    }

    /**
     * Stelt de observer in voor deze deur.
     * Implementeert het Observer pattern.
     */
    public void setObserver(DoorObserver observer) {
        this.observer = observer;
    }

    /**
     * Opent de deur en notificeert de observer.
     * Implementeert het Observer pattern.
     */
    public void open() {
        if (!isOpen) {
            isOpen = true;
            GameState.getInstance().markDoorOpened(doorId);
            if (observer != null) {
                observer.onDoorOpened(doorId);
            }
        }
    }

    /**
     * Rendert de deur met de juiste texture gebaseerd op de open/gesloten status.
     */
    public void render(SpriteBatch batch) {
        TextureRegion currentTexture = isOpen ? openTexture : closedTexture;
        batch.draw(currentTexture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * Geeft aan of de deur open is.
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Geeft de deur ID terug.
     */
    public String getDoorId() {
        return doorId;
    }

    /**
     * Geeft de vraag ID terug die bij deze deur hoort.
     */
    public String getQuestionId() {
        return questionId;
    }

    /**
     * Geeft de collision bounds van de deur terug.
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Geeft de X-coördinaat van de deur terug.
     */
    public float getX() {
        return bounds.x;
    }

    /**
     * Geeft de Y-coördinaat van de deur terug.
     */
    public float getY() {
        return bounds.y;
    }

    public void setOpen(boolean open) {
        if (open && !isOpen) {
            open();
        } else {
            isOpen = open;
        }
    }
} 