package nl.webser.scrum_escape.jokers;

public class KeyJoker implements JokerStrategy {
    private final int kamerId;

    public KeyJoker(int kamerId) {
        this.kamerId = kamerId;
    }

    @Override
    public void gebruik() {
        System.out.println("Key joker gebruikt! Kamer " + kamerId + " is ontgrendeld.");
        // Voeg hier echte sleutel-logica toe
    }
}
