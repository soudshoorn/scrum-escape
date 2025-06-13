package nl.webser.scrum_escape;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

/**
 * GameState houdt de huidige status van het spel bij.
 * Singleton pattern voor één centrale spelstatus.
 */
public class GameState {
    private static GameState instance;
    private int score;
    private final Set<String> answeredQuestions;
    private final Set<String> openedDoors;
    private final Map<String, Integer> failedAttempts;
    private String activeQuestion;
    private boolean monsterActive;
    private float monsterAlpha;
    private final Set<Integer> foundTIAObjects;

    private GameState() {
        this.score = 0;
        this.answeredQuestions = new HashSet<>();
        this.openedDoors = new HashSet<>();
        this.failedAttempts = new HashMap<>();
        this.foundTIAObjects = new HashSet<>();
        this.monsterAlpha = 0f;
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void addScore(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }

    public boolean isQuestionAnswered(String questionId) {
        return answeredQuestions.contains(questionId);
    }

    public void markQuestionAnswered(String questionId) {
        answeredQuestions.add(questionId);
    }

    public boolean isDoorOpened(String doorId) {
        return openedDoors.contains(doorId);
    }

    public void markDoorOpened(String doorId) {
        openedDoors.add(doorId);
    }

    public void reset() {
        score = 0;
        answeredQuestions.clear();
        openedDoors.clear();
        failedAttempts.clear();
        foundTIAObjects.clear();
        activeQuestion = null;
        monsterActive = false;
        monsterAlpha = 0f;
    }

    public void markQuestionFailed(String questionId) {
        failedAttempts.put(questionId, failedAttempts.getOrDefault(questionId, 0) + 1);
    }

    public boolean isQuestionFailed(String questionId) {
        return failedAttempts.containsKey(questionId);
    }

    public int getFailedAttempts(String questionId) {
        return failedAttempts.getOrDefault(questionId, 0);
    }

    public boolean hasAnyQuestionFailedTwice() {
        return failedAttempts.values().stream().anyMatch(attempts -> attempts >= 2);
    }

    public void resetFailedQuestion(String questionId) {
        failedAttempts.remove(questionId);
    }

    public boolean canAccessDoor(String doorId) {
        // Simpele logica: als de deur niet open is, mag je er niet in
        return !isDoorOpened(doorId);
    }

    public void setActiveQuestion(String questionId) {
        activeQuestion = questionId;
    }

    public String getActiveQuestion() {
        return activeQuestion;
    }

    public void clearActiveQuestion() {
        activeQuestion = null;
    }

    public boolean isQuestionActive() {
        return activeQuestion != null;
    }

    public void setMonsterActive(boolean active) {
        monsterActive = active;
    }

    public boolean isMonsterActive() {
        return monsterActive;
    }

    public float getMonsterAlpha() {
        return monsterAlpha;
    }

    public void updateMonsterAlpha(float delta) {
        if (monsterActive) {
            monsterAlpha = Math.min(1f, monsterAlpha + delta);
        } else {
            monsterAlpha = Math.max(0f, monsterAlpha - delta);
        }
    }

    public void addFoundTIAObject(int tiaType) {
        foundTIAObjects.add(tiaType);
    }

    public Set<Integer> getFoundTIAObjects() {
        return new HashSet<>(foundTIAObjects);
    }

    public boolean hasFoundAllTIAObjects() {
        return foundTIAObjects.size() == 3;
    }
} 