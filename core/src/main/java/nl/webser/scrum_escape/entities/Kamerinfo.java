package nl.webser.scrum_escape.entities;

import com.badlogic.gdx.math.Rectangle;

public class Kamerinfo implements Readable {
    private final Rectangle bounds;
    private final String message;
    private boolean found = false;

    public Kamerinfo(float x, float y, float width, float height, String message) {
        this.bounds = new Rectangle(x, y, width, height);
        this.message = message;
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
        return message;
    }
}