package nl.webser.scrum_escape.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class TIAObject implements Readable {
    private final Rectangle bounds;
    private final int tiaType; // 1 = Transparantie, 2 = Inspectie, 3 = Aanpassing
    private boolean found;
    private static final String[] TIA_MESSAGES = {
        "Alles wat we doen moet zichtbaar zijn voor iedereen. Dit betekent dat alle aspecten van het proces, de voortgang en eventuele problemen open en eerlijk worden gedeeld met het hele team.",
        "Regelmatig controleren of we op de goede weg zijn. Door frequent te inspecteren kunnen we snel bijsturen waar nodig en leren van wat we zien.",
        "Direct veranderen als we iets beter kunnen doen. Wanneer we tijdens inspectie verbeterpunten zien, passen we ons proces of product direct aan."
    };

    public TIAObject(float x, float y, float width, float height, int value) {
        this.bounds = new Rectangle(x, y, width, height);
        this.tiaType = value;
        this.found = false;
    }

    public void render(SpriteBatch batch) {
        // No rendering needed, the TIA objects are already in the map
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }
    @Override
    public String getMessage() {
        return TIA_MESSAGES[tiaType - 1];
    }

    public int getTiaType() {
        return tiaType;
    }
}