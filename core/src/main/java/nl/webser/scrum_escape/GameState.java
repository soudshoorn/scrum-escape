package nl.webser.scrum_escape;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

/**
 * GameState is een klasse die de huidige status van het spel bijhoudt.
 * Deze klasse gebruikt het Singleton pattern, wat betekent dat er maar
 * één GameState in het hele spel bestaat.
 * 
 * De GameState houdt bij:
 * 1. De score van de speler
 * 2. Welke vragen al zijn beantwoord
 * 3. Welke deuren al zijn geopend
 * 4. Welke TIA objecten zijn gevonden
 * 5. Welke vragen fout zijn beantwoord
 * 6. Of het monster actief is
 */
public class GameState {
    // Singleton instance
    private static GameState instance;
    
    // Spel status variabelen
    private int score;                                    // Huidige score
    private final Set<String> answeredQuestions;          // Vragen die correct zijn beantwoord
    private final Set<String> openedDoors;                // Deuren die zijn geopend
    private final Set<Integer> foundTIAObjects;            // TIA objecten die zijn gevonden
    private final Set<String> failedQuestions;            // Vragen die fout zijn beantwoord
    private final Map<String, Integer> failedAttempts;    // Aantal foute pogingen per vraag
    private String activeQuestionId;                      // ID van de huidige actieve vraag
    private boolean monsterActive;                        // Of het monster actief is
    private float monsterAlpha;                          // Transparantie van het monster

    /**
     * Private constructor voor het Singleton pattern.
     * Initialiseert alle verzamelingen en variabelen.
     */
    private GameState() {
        score = 0;
        answeredQuestions = new HashSet<>();
        openedDoors = new HashSet<>();
        foundTIAObjects = new HashSet<>();
        failedQuestions = new HashSet<>();
        failedAttempts = new HashMap<>();
        activeQuestionId = null;
        monsterActive = false;
        monsterAlpha = 1f;
    }

    /**
     * Geeft de enige instantie van GameState terug.
     * Als er nog geen instantie bestaat, wordt er een nieuwe gemaakt.
     * 
     * @return De GameState instantie
     */
    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    /**
     * Verhoogt de score met 10 punten.
     * Wordt aangeroepen wanneer een vraag correct wordt beantwoord.
     */
    public void addScore() {
        score += 10;
    }

    /**
     * Geeft de huidige score terug.
     * @return De score als integer
     */
    public int getScore() {
        return score;
    }

    /**
     * Controleert of een vraag al is beantwoord.
     * @param questionId De ID van de te controleren vraag
     * @return true als de vraag al is beantwoord, anders false
     */
    public boolean isQuestionAnswered(String questionId) {
        return answeredQuestions.contains(questionId);
    }

    /**
     * Markeert een vraag als beantwoord.
     * @param questionId De ID van de beantwoorde vraag
     */
    public void markQuestionAnswered(String questionId) {
        answeredQuestions.add(questionId);
    }

    /**
     * Controleert of een deur al is geopend.
     * @param doorId De ID van de te controleren deur
     * @return true als de deur al is geopend, anders false
     */
    public boolean isDoorOpened(String doorId) {
        return openedDoors.contains(doorId);
    }

    /**
     * Markeert een deur als geopend.
     * @param doorId De ID van de geopende deur
     */
    public void markDoorOpened(String doorId) {
        openedDoors.add(doorId);
    }

    /**
     * Reset alle spelstatus naar de beginwaarden.
     * Wordt aangeroepen wanneer een nieuw spel wordt gestart.
     */
    public void reset() {
        score = 0;
        answeredQuestions.clear();
        openedDoors.clear();
        foundTIAObjects.clear();
        failedQuestions.clear();
        failedAttempts.clear();
        activeQuestionId = null;
        monsterActive = false;
        monsterAlpha = 1f;
    }

    /**
     * Markeert een vraag als fout beantwoord en verhoogt het aantal pogingen.
     * @param questionId De ID van de fout beantwoorde vraag
     */
    public void markQuestionFailed(String questionId) {
        failedQuestions.add(questionId);
        int attempts = failedAttempts.getOrDefault(questionId, 0) + 1;
        failedAttempts.put(questionId, attempts);
    }

    /**
     * Controleert of een vraag fout is beantwoord.
     * @param questionId De ID van de te controleren vraag
     * @return true als de vraag fout is beantwoord, anders false
     */
    public boolean isQuestionFailed(String questionId) {
        return failedQuestions.contains(questionId);
    }

    /**
     * Geeft het aantal foute pogingen voor een vraag terug.
     * @param questionId De ID van de vraag
     * @return Het aantal foute pogingen
     */
    public int getFailedAttempts(String questionId) {
        return failedAttempts.getOrDefault(questionId, 0);
    }

    /**
     * Controleert of er een vraag is die twee keer fout is beantwoord.
     * @return true als er een vraag twee keer fout is beantwoord, anders false
     */
    public boolean hasAnyQuestionFailedTwice() {
        for (int count : failedAttempts.values()) {
            if (count >= 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reset de fout status van een vraag.
     * @param questionId De ID van de vraag
     */
    public void resetFailedQuestion(String questionId) {
        failedQuestions.remove(questionId);
        failedAttempts.remove(questionId);
    }

    /**
     * Controleert of een deur toegankelijk is.
     * Speciale logica voor de finale deur (score check).
     * @param doorId De ID van de te controleren deur
     * @return true als de deur toegankelijk is, anders false
     */
    public boolean canAccessDoor(String doorId) {
        if (doorId.equals("door6")) {
            // Controleer of score 5 of hoger is
            return score >= 5;
        }
        return true;
    }

    /**
     * Stelt de actieve vraag in.
     * @param questionId De ID van de actieve vraag
     */
    public void setActiveQuestion(String questionId) {
        activeQuestionId = questionId;
    }

    /**
     * Geeft de huidige actieve vraag terug.
     * @return De ID van de actieve vraag
     */
    public String getActiveQuestion() {
        return activeQuestionId;
    }

    /**
     * Verwijdert de actieve vraag.
     * Wordt aangeroepen wanneer een vraag is beantwoord.
     */
    public void clearActiveQuestion() {
        activeQuestionId = null;
    }

    /**
     * Controleert of er een actieve vraag is.
     * @return true als er een actieve vraag is, anders false
     */
    public boolean isQuestionActive() {
        return activeQuestionId != null;
    }

    /**
     * Stelt de monster status in.
     * Reset de monster alpha als het monster wordt geactiveerd.
     * @param active Of het monster actief moet zijn
     */
    public void setMonsterActive(boolean active) {
        monsterActive = active;
        if (active) {
            monsterAlpha = 1f;
        }
    }

    /**
     * Controleert of het monster actief is.
     */
    public boolean isMonsterActive() {
        return monsterActive;
    }

    /**
     * Geeft de huidige monster alpha waarde terug.
     */
    public float getMonsterAlpha() {
        return monsterAlpha;
    }

    /**
     * Update de monster alpha waarde voor fade effect.
     */
    public void updateMonsterAlpha(float delta) {
        if (monsterActive) {
            monsterAlpha = Math.max(0, monsterAlpha - 0.5f * delta);
        }
    }

    /**
     * Voegt een gevonden TIA object toe aan de lijst.
     */
    public void addFoundTIAObject(int tiaType) {
        foundTIAObjects.add(tiaType);
    }

    /**
     * Geeft de set van gevonden TIA objecten terug.
     */
    public Set<Integer> getFoundTIAObjects() {
        return foundTIAObjects;
    }

    public boolean hasFoundAllTIAObjects() {
        return foundTIAObjects.size() == 3;
    }
} 