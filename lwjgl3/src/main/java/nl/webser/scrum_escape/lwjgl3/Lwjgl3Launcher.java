package nl.webser.scrum_escape.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import nl.webser.scrum_escape.ScrumEscapeGame;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Scrum Escape");
        config.setWindowedMode(800, 640);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new ScrumEscapeGame(), config);
    }
}