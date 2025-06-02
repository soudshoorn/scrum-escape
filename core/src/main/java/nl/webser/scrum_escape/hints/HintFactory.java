package nl.webser.scrum_escape.hints;

import java.util.Random;

public class HintFactory {
    private static final Random random = new Random();

    public static HintProvider createHintProvider() {
        if (random.nextBoolean()) {
            return new HelpHintProvider();
        } else {
            return new FunnyHintProvider();
        }
    }
}