package nl.webser.scrum_escape.jokers;

public class HintJoker implements JokerStrategy {
    @Override
    public void gebruik() {
        System.out.println("Hint joker gebruikt! Je krijgt een hint.");
        // Voeg hier echte hint-logica toe
    }
}
