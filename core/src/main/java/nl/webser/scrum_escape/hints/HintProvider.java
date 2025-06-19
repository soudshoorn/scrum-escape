package nl.webser.scrum_escape.hints;

public interface HintProvider {
    String getHint();
    
    /**
     * Geeft een vraag-specifieke hint terug
     * @param questionId De ID van de vraag
     * @param questionText De tekst van de vraag
     * @return Een hint die specifiek bij deze vraag past
     */
    String getHintForQuestion(String questionId, String questionText);
}
