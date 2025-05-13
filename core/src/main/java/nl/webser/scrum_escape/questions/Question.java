package nl.webser.scrum_escape.questions;

public class Question {
    private String questionText;
    private String[] choices;
    private int correctIndex;

    public Question(String questionText, String[] choices, int correctIndex) {
        this.questionText = questionText;
        this.choices = choices;
        this.correctIndex = correctIndex;
    }

    public boolean checkAnswer(int answerIndex) {
        return answerIndex == correctIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getChoices() {
        return choices;
    }
}