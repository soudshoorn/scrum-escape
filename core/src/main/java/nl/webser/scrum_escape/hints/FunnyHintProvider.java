package nl.webser.scrum_escape.hints;

import java.util.HashMap;
import java.util.Map;

public class FunnyHintProvider implements HintProvider {
    private final Map<String, String> questionHints;
    
    public FunnyHintProvider() {
        questionHints = new HashMap<>();
        initializeQuestionHints();
    }
    
    private void initializeQuestionHints() {
        // Sprint 1 - Sprint Review
        questionHints.put("sprint1", "Grappige hint: Een Sprint Review is als een kookshow - je laat zien wat je hebt gemaakt en krijgt feedback van de jury!");
        
        // Sprint 2 - Product Owner
        questionHints.put("sprint2", "Grappige hint: De Product Owner is als een chef-kok die bepaalt wat er op het menu staat. Zonder chef geen gerechten!");
        
        // Sprint 3 - Daily Scrum
        questionHints.put("sprint3", "Grappige hint: Daily Scrum is als een korte team-briefing: 'Wat heb ik gedaan? Wat ga ik doen? Zie ik obstakels?' - net als in een actiefilm!");
        
        // Sprint 4 - Scrum Team
        questionHints.put("sprint4", "Grappige hint: Een goed Scrum Team is als een Zwitsers zakmes - ze kunnen alles en zijn zelfstandig!");
        
        // Sprint 5 - Onafgemaakt werk
        questionHints.put("sprint5", "Grappige hint: Onafgemaakt werk gaat terug naar de backlog, net als een pizza die niet op tijd klaar is - je moet opnieuw bestellen!");
        
        // Finale vragen
        questionHints.put("final0", "Grappige hint: Transparantie is als een glazen huis - iedereen kan zien wat er binnen gebeurt!");
        questionHints.put("final1", "Grappige hint: Inspectie is als een detective die problemen opspoort voordat ze te groot worden!");
        questionHints.put("final2", "Grappige hint: Aanpassing is als een kameleon - je verandert direct van kleur als de omgeving verandert!");
    }
    
    @Override
    public String getHint() {
        return "Grappige hint: Scrum is als pizza maken, maar zonder de ananas!";
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
