package nl.webser.scrum_escape.hints;

import java.util.HashMap;
import java.util.Map;

public class HelpHintProvider implements HintProvider {
    private final Map<String, String> questionHints;
    
    public HelpHintProvider() {
        questionHints = new HashMap<>();
        initializeQuestionHints();
    }
    
    private void initializeQuestionHints() {
        // Sprint 1 - Sprint Review
        questionHints.put("sprint1", "Denk aan het doel van een Sprint Review: het inspecteren van het product increment en het verzamelen van feedback van stakeholders.");
        
        // Sprint 2 - Product Owner
        questionHints.put("sprint2", "De Product Owner is verantwoordelijk voor het maximaliseren van de waarde van het product. Denk aan wie de beslissingen neemt over wat er gebouwd wordt.");
        
        // Sprint 3 - Daily Scrum
        questionHints.put("sprint3", "De Daily Scrum is een korte bijeenkomst voor het Development Team om het werk te synchroniseren en het plan voor de komende 24 uur aan te passen.");
        
        // Sprint 4 - Scrum Team
        questionHints.put("sprint4", "Een goed Scrum Team is zelfstandig en multidisciplinair. Ze kunnen alle werk uitvoeren dat nodig is om een 'Done' increment te leveren.");
        
        // Sprint 5 - Onafgemaakt werk
        questionHints.put("sprint5", "Als werk niet af is aan het einde van de sprint, wordt het teruggezet naar de Product Backlog. De sprint wordt niet verlengd.");
        
        // Finale vragen
        questionHints.put("final0", "Transparantie betekent dat alle aspecten van het proces, de voortgang en problemen open en eerlijk worden gedeeld.");
        questionHints.put("final1", "Inspectie helpt om tijdig problemen te identificeren en bij te sturen waar nodig.");
        questionHints.put("final2", "Aanpassing betekent dat je direct verandert wanneer je tijdens inspectie verbeterpunten ziet.");
    }
    
    @Override
    public String getHint() {
        return "Tip: Denk aan de kernprincipes van Scrum, zoals transparantie en samenwerking.";
    }
    
    @Override
    public String getHintForQuestion(String questionId, String questionText) {
        // Probeer eerst een specifieke hint te vinden
        String specificHint = questionHints.get(questionId);
        if (specificHint != null) {
            return specificHint;
        }
        
        // Fallback naar generieke hint
        return getHint();
    }
}
