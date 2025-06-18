package nl.webser.scrum_escape.questions;

import java.util.HashMap;
import java.util.Map;

import nl.webser.scrum_escape.GameState;

/**
 * QuestionManager is een klasse die alle vragen in het spel beheert.
 * Deze klasse gebruikt het Singleton pattern, wat betekent dat er maar
 * één QuestionManager in het hele spel bestaat.
 * 
 * De QuestionManager:
 * 1. Bewaart alle vragen in een HashMap
 * 2. Geeft vragen terug wanneer ze nodig zijn
 * 3. Controleert of vragen bestaan
 * 4. Beheert de finale vragen
 */
public class QuestionManager {
    // Singleton instance
    private static QuestionManager instance;
    
    // HashMap die alle vragen opslaat, met de vraag ID als sleutel
    private final Map<String, QuestionStrategy> questions;

    // Moeilijkere en inhoudelijkere TIA vragen
    private static final String[] FINAL_QUESTIONS = {
        "Waarom is transparantie essentieel voor effectieve samenwerking binnen Scrum?",
        "Hoe draagt inspectie bij aan het tijdig bijsturen van een Scrum project?",
        "Wat houdt het principe van aanpassing (adaptation) in binnen het Scrum framework?"
    };

    // Complexere antwoordopties
    private static final String[][] FINAL_ANSWERS = {
        {"Omdat het team dan open is over werk en obstakels", 
        "Omdat het team weet wat iedereen doet", 
        "Omdat Scrum zonder transparantie niet werkt", 
        "Omdat het de basis is voor vertrouwen en empirisme"},

        {"Omdat het team dan wekelijks de backlog inspecteert", 
        "Omdat inspectie tijdig problemen blootlegt", 
        "Omdat stakeholders dan kunnen controleren", 
        "Omdat het verplicht is in de sprint review"},

        {"Het aanpassen van het plan bij nieuwe inzichten", 
        "Het optimaliseren van code tijdens de sprint", 
        "Het aanpassen van processen op basis van inspectieresultaten", 
        "Het aanpassen van de teamstructuur"}
    };

    /**
     * Private constructor voor het Singleton pattern.
     * Initialiseert de HashMap en voegt alle vragen toe.
     */
    private QuestionManager() {
        questions = new HashMap<>();
        initializeQuestions();
    }

    /**
     * Geeft de enige instantie van QuestionManager terug.
     * Als er nog geen instantie bestaat, wordt er een nieuwe gemaakt.
     * 
     * @return De QuestionManager instantie
     */
    public static QuestionManager getInstance() {
        if (instance == null) {
            instance = new QuestionManager();
        }
        return instance;
    }

    /**
     * Initialiseert alle vragen in het spel.
     * Elke vraag heeft:
     * - Een unieke ID
     * - Een vraagtekst
     * - Antwoordopties
     * - Een index voor het juiste antwoord
     * - Een deur ID waar de vraag bij hoort
     */
    private void initializeQuestions() {
        // Sprint 1 vragen
        addQuestion("sprint1", "Wat is het belangrijkste doel van een Sprint Review?", 
            new String[]{
                "Het product increment inspecteren en feedback verzamelen",
                "De volgende sprint plannen",
                "De Definition of Done aanpassen",
                "De velocity van het team verhogen"
            }, 0, "door1");

        // Sprint 2 vragen
        addQuestion("sprint2", "Wie is verantwoordelijk voor het maximaliseren van de waarde van het product?", 
            new String[]{
                "De Scrum Master",
                "De Stakeholders",
                "De Product Owner",
                "Het hele Scrum Team"
            }, 2, "door2");

        // Sprint 3 vragen
        addQuestion("sprint3", "Wat is het doel van de Daily Scrum?", 
            new String[]{
                "De product backlog prioriteren",
                "De voortgang bespreken en het plan voor de komende 24 uur aanpassen",
                "De sprint backlog aanpassen",
                "De stakeholders informeren"
            }, 1, "door3");

        // Sprint 4 vragen
        addQuestion("sprint4", "Wat is een kenmerk van een goed Scrum Team?", 
            new String[]{
                "Het team werkt altijd overuren",
                "Het team volgt alleen instructies van de manager",
                "Het team werkt zelfstandig en is multidisciplinair",
                "Het team bestaat uit alleen ontwikkelaars"
            }, 2, "door4");

        // Sprint 5 vragen
        addQuestion("sprint5", "Wat gebeurt er als het werk niet af is aan het einde van de sprint?", 
            new String[]{
                "Het werk wordt automatisch als af beschouwd",
                "Het team werkt door tot het af is",
                "De sprint wordt verlengd",
                "Het werk wordt teruggezet naar de product backlog"
            }, 3, "door5");
    }

    /**
     * Voegt een nieuwe vraag toe aan de HashMap.
     * 
     * @param id Unieke ID voor de vraag
     * @param question De vraagtekst
     * @param options Array met antwoordopties
     * @param correctOption Index van het juiste antwoord
     * @param doorId ID van de deur waar de vraag bij hoort
     */
    private void addQuestion(String id, String question, String[] options, int correctOption, String doorId) {
        questions.put(id, new MultipleChoiceStrategy(id, question, options, correctOption, doorId));
    }

    /**
     * Geeft een vraag terug op basis van zijn ID.
     * 
     * @param questionId De ID van de gevraagde vraag
     * @return De gevraagde vraag, of null als deze niet bestaat
     */
    public QuestionStrategy getQuestion(String questionId) {
        return questions.get(questionId);
    }

    /**
     * Controleert of een vraag bestaat.
     * 
     * @param questionId De ID van de te controleren vraag
     * @return true als de vraag bestaat, anders false
     */
    public boolean hasQuestion(String questionId) {
        return questions.containsKey(questionId);
    }

    /**
     * Geeft een finale vraag terug.
     * Deze vragen zijn alleen beschikbaar als alle TIA objecten zijn gevonden.
     * 
     * @param questionIndex De index van de finale vraag (0-2)
     * @return De finale vraag, of null als deze niet beschikbaar is
     */
    public static QuestionStrategy getFinalQuestion(int questionIndex) {
        // Controleer of de index geldig is
        if (questionIndex < 0 || questionIndex >= FINAL_QUESTIONS.length) {
            return null;
        }
        
        // Controleer of alle TIA objecten zijn gevonden
        boolean allTIAFound = GameState.getInstance().getFoundTIAObjects().size() == 3;
        if (!allTIAFound) {
            return null;
        }
        
        // Maak en geef de finale vraag terug
        return new MultipleChoiceStrategy(
            "final" + questionIndex,
            FINAL_QUESTIONS[questionIndex],
            FINAL_ANSWERS[questionIndex],
            0,
            "door6"
        );
    }

    /**
     * Controleert of een antwoord op een finale vraag correct is.
     * Voor finale vragen is altijd het eerste antwoord correct.
     * 
     * @param questionIndex De index van de finale vraag
     * @param selectedOption Het gekozen antwoord
     * @return true als het antwoord correct is, anders false
     */
    public static boolean checkFinalAnswer(int questionIndex, int selectedOption) {
        return selectedOption == 0; // Eerste antwoord is altijd correct voor finale vragen
    }
} 