package nl.webser.scrum_escape.jokers;

import java.util.HashSet;
import java.util.Set;

public class JokerManager {
    private final Set<Joker> beschikbareJokers = new HashSet<>();
    private Joker gekozenJoker;

    // Voeg een joker toe aan de beschikbare jokers (bijv. bij spelstart)
    public void voegBeschikbareJokerToe(Joker joker) {
        beschikbareJokers.add(joker);
    }

    // Kies een joker uit de beschikbare jokers
    public void kiesJoker(Joker joker) {
        if (beschikbareJokers.contains(joker)) {
            this.gekozenJoker = joker;
            System.out.println("Joker gekozen: " + joker);
        } else {
            System.out.println("Joker niet beschikbaar: " + joker);
        }
    }

    public Joker getGekozenJoker() {
        return gekozenJoker;
    }

    public boolean jokerBeschikbaar(Joker joker) {
        return beschikbareJokers.contains(joker);
    }

    // Gebruik een joker (verwijder uit beschikbare jokers en reset gekozen)
    public void gebruikJoker(Joker joker) {
        beschikbareJokers.remove(joker);
        if (gekozenJoker == joker) {
            gekozenJoker = null;
        }
    }

    public Set<Joker> getBeschikbareJokers() {
        return beschikbareJokers;
    }
}
