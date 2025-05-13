package nl.webser.scrum_escape.questions;

/**
 * QuestionStrategy is een interface die definieert hoe een vraag in het spel werkt.
 * Dit is een voorbeeld van het Strategy Pattern, wat betekent dat we verschillende
 * soorten vragen kunnen maken die allemaal op dezelfde manier werken.
 * 
 * Denk aan een restaurant menu: elk gerecht heeft zijn eigen bereidingswijze,
 * maar ze volgen allemaal dezelfde structuur (naam, ingrediënten, prijs).
 */
public interface QuestionStrategy {
    /**
     * Geeft de vraagtekst terug die aan de speler wordt getoond.
     * @return De vraagtekst als String
     */
    String getQuestion();

    /**
     * Geeft de mogelijke antwoordopties terug.
     * @return Een array met alle mogelijke antwoorden
     */
    String[] getOptions();

    /**
     * Controleert of het gekozen antwoord correct is.
     * @param selectedOption Het nummer van het gekozen antwoord (begint bij 0)
     * @return true als het antwoord correct is, anders false
     */
    boolean isCorrectAnswer(int selectedOption);

    /**
     * Geeft een unieke identificatie voor deze vraag terug.
     * Dit wordt gebruikt om bij te houden welke vragen al zijn beantwoord.
     * @return De vraag ID als String
     */
    String getQuestionId();

    /**
     * Wordt aangeroepen wanneer de speler een correct antwoord geeft.
     * Bijvoorbeeld: score verhogen, deur openen, etc.
     */
    void onCorrectAnswer();

    /**
     * Wordt aangeroepen wanneer de speler een fout antwoord geeft.
     * Bijvoorbeeld: monster activeren, waarschuwing tonen, etc.
     */
    void onWrongAnswer();
    
    /**
     * Controleert het antwoord en voert de juiste actie uit.
     * Dit is een standaard implementatie die we kunnen hergebruiken.
     * 
     * @param selectedOption Het nummer van het gekozen antwoord (begint bij 0)
     * @return true als het antwoord correct is, anders false
     */
    default boolean checkAnswer(int selectedOption) {
        boolean isCorrect = isCorrectAnswer(selectedOption);
        if (isCorrect) {
            onCorrectAnswer();
        } else {
            onWrongAnswer();
        }
        return isCorrect;
    }
}