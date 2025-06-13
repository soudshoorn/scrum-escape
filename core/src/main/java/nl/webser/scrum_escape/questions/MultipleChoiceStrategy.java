package nl.webser.scrum_escape.questions;

import nl.webser.scrum_escape.GameState;

/**
 * MultipleChoiceStrategy is een concrete implementatie van QuestionStrategy
 * voor meerkeuzevragen. Deze klasse zorgt ervoor dat we vragen kunnen maken
 * waarbij de speler uit meerdere antwoorden moet kiezen.
 * 
 * Voorbeeld:
 * Vraag: "Wat is de hoofdstad van Nederland?"
 * Opties: ["Amsterdam", "Rotterdam", "Den Haag", "Utrecht"]
 * Correct antwoord: 0 (Amsterdam)
 */
public class MultipleChoiceStrategy implements QuestionStrategy {
    // Eigenschappen van de vraag
    private final String questionId;    // Unieke ID voor deze vraag
    private final String question;      // De vraagtekst zelf
    private final String[] options;     // Mogelijke antwoorden
    private final int correctOption;    // Index van het juiste antwoord (begint bij 0)
    private final String doorId;        // ID van de deur waar deze vraag bij hoort

    /**
     * Maakt een nieuwe meerkeuzevraag aan.
     * 
     * @param questionId Unieke ID voor deze vraag (bijv. "sprint1")
     * @param question De vraagtekst die aan de speler wordt getoond
     * @param options Array met alle mogelijke antwoorden
     * @param correctOption Index van het juiste antwoord (begint bij 0)
     * @param doorId ID van de deur waar deze vraag bij hoort
     */
    public MultipleChoiceStrategy(String questionId, String question, String[] options, int correctOption, String doorId) {
        this.questionId = questionId;
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
        this.doorId = doorId;
    }

    /**
     * Geeft de vraagtekst terug.
     * @return De vraagtekst als String
     */
    @Override
    public String getQuestion() {
        return question;
    }

    /**
     * Geeft alle mogelijke antwoorden terug.
     * @return Array met alle antwoordopties
     */
    @Override
    public String[] getOptions() {
        return options;
    }

    /**
     * Controleert of het gekozen antwoord correct is.
     * @param selectedOption Het nummer van het gekozen antwoord (begint bij 0)
     * @return true als het antwoord correct is, anders false
     */
    @Override
    public boolean isCorrectAnswer(int selectedOption) {
        return selectedOption == correctOption;
    }

    /**
     * Geeft de unieke ID van deze vraag terug.
     * @return De vraag ID als String
     */
    @Override
    public String getQuestionId() {
        return questionId;
    }

    /**
     * Wordt aangeroepen wanneer de speler een correct antwoord geeft.
     * Dit zorgt ervoor dat:
     * 1. De score wordt verhoogd
     * 2. De vraag wordt gemarkeerd als beantwoord
     * 3. De bijbehorende deur wordt geopend
     */
    @Override
    public void onCorrectAnswer() {
        GameState.getInstance().addScore(10);
        GameState.getInstance().markQuestionAnswered(questionId);
        GameState.getInstance().markDoorOpened(doorId);
    }

    /**
     * Wordt aangeroepen wanneer de speler een fout antwoord geeft.
     * Dit zorgt ervoor dat:
     * 1. De vraag wordt gemarkeerd als beantwoord
     * 2. Het monster wordt geactiveerd
     */
    @Override
    public void onWrongAnswer() {
        GameState.getInstance().markQuestionAnswered(questionId);
    }
}