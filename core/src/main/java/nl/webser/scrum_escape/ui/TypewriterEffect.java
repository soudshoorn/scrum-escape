package nl.webser.scrum_escape.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TypewriterEffect {
    private String fullText;
    private String currentText;
    private float timer;
    private float typingSpeed;
    private boolean isTyping;
    private boolean isComplete;
    private static final float DEFAULT_TYPING_SPEED = 0.015f; // Standaard snelheid

    public TypewriterEffect() {
        this.typingSpeed = DEFAULT_TYPING_SPEED;
        this.currentText = "";
        this.isTyping = false;
        this.isComplete = false;
    }

    public void setTypingSpeed(float speed) {
        this.typingSpeed = speed;
    }

    public void start(String text) {
        this.fullText = text;
        this.currentText = "";
        this.timer = 0;
        this.isTyping = true;
        this.isComplete = false;
    }

    public void update(float delta) {
        if (!isTyping || isComplete) return;

        timer += delta;
        if (timer >= typingSpeed) {
            timer = 0;
            if (currentText.length() < fullText.length()) {
                currentText = fullText.substring(0, currentText.length() + 1);
            } else {
                isComplete = true;
                isTyping = false;
            }
        }
    }

    public void render(SpriteBatch batch, BitmapFont font, float x, float y) {
        font.draw(batch, currentText, x, y);
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void skip() {
        currentText = fullText;
        isComplete = true;
        isTyping = false;
    }

    public String getCurrentText() {
        return currentText;
    }

    public boolean isTypingComplete() {
        return !isTyping;
    }
} 