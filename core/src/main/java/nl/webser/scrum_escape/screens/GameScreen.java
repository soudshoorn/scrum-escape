package nl.webser.scrum_escape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import nl.webser.scrum_escape.AssetManager;
import nl.webser.scrum_escape.GameState;
import nl.webser.scrum_escape.ScrumEscapeGame;
import nl.webser.scrum_escape.entities.Door;
import nl.webser.scrum_escape.entities.Player;
import nl.webser.scrum_escape.entities.TIAObject;
import nl.webser.scrum_escape.observer.DoorObserver;
import nl.webser.scrum_escape.questions.QuestionManager;
import nl.webser.scrum_escape.questions.QuestionStrategy;
import nl.webser.scrum_escape.ui.TypewriterEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * GameScreen is het hoofdscherm van het Scrum Escape spel.
 * Deze klasse is verantwoordelijk voor:
 * 1. Het tonen van het spel
 * 2. Het verwerken van speler input
 * 3. Het updaten van de spelstatus
 * 4. Het renderen van alle spel elementen
 * 
 * Design Patterns gebruikt:
 * - Observer Pattern: Voor het monitoren van deur status veranderingen
 * - Strategy Pattern: Voor het afhandelen van verschillende vraag types
 * - Singleton Pattern: Voor GameState en AssetManager
 * - State Pattern: Voor het beheren van verschillende spel states
 */
public class GameScreen implements Screen, DoorObserver {
    // UI Instellingen
    private static final float UI_PADDING = 20f;              // Padding rond UI elementen
    private static final float CAMERA_ZOOM = 0.5f;            // Zoom niveau van de camera
    private static final float QUESTION_BOX_HEIGHT = 200f;    // Hoogte van het vraagvak
    private static final float QUESTION_BOX_Y = 0f;           // Y-positie van het vraagvak
    private static final float QUESTION_TEXT_X = 50f;         // X-positie van de vraagtekst
    private static final float QUESTION_TEXT_Y = 150f;        // Y-positie van de vraagtekst
    private static final float QUESTION_LINE_SPACING = 25f;   // Ruimte tussen tekstregels
    
    // Timing instellingen
    private static final float MESSAGE_DURATION = 1f;         // Hoe lang een bericht wordt getoond
    private static final float MESSAGE_FADE_DURATION = 0.5f;  // Hoe lang het fade effect duurt
    private static final float WARNING_DURATION = 2f;         // Hoe lang een waarschuwing wordt getoond
    
    // Venster afmetingen
    private static final int WINDOW_WIDTH = 800;              // Breedte van het spelvenster
    private static final int WINDOW_HEIGHT = 640;             // Hoogte van het spelvenster
    
    // Monster eigenschappen
    private static final float MONSTER_SPEED = 100f;          // Snelheid van het monster
    private static final float MONSTER_FADE_SPEED = 0.5f;     // Snelheid van het fade effect
    private static final float MONSTER_SIZE = 32f;            // Grootte van het monster in pixels
    private static final float MONSTER_OFFSET = 40f;          // Afstand van het monster tot de deur
    
    // Kern spel componenten (final omdat deze niet veranderen na initialisatie)
    private final SpriteBatch batch;                          // Voor het tekenen van sprites
    private final OrthographicCamera camera;                  // Camera voor het spel
    private final OrthographicCamera uiCamera;                // Camera voor de UI
    private final Player player;                              // De speler
    private final QuestionManager questionManager;            // Beheert alle vragen
    private final GameState gameState;                        // Houdt de spelstatus bij
    private final BitmapFont font;                            // Lettertype voor tekst
    private final TypewriterEffect typewriterEffect;          // Typewriter effect voor tekst
    private final TiledMap map;                              // De spelkaart
    private final OrthogonalTiledMapRenderer mapRenderer;     // Tekent de spelkaart
    private final List<Door> doors;                          // Alle deuren in het spel
    private final List<TIAObject> tiaObjects;                // Alle TIA objecten in het spel
    
    // Spel status variabelen
    private Door currentDoor;                                // De huidige deur waar de speler mee interacteert
    private QuestionStrategy currentQuestion;                // De huidige vraag die wordt getoond
    private boolean showingQuestion;                         // Of er een vraag wordt getoond
    private boolean waitingForAnswer;                        // Of we wachten op een antwoord
    private float prevPlayerX, prevPlayerY;                  // Vorige positie van de speler
    private String currentMessage;                           // Huidige bericht dat wordt getoond
    private float messageTimer;                              // Timer voor berichten
    private boolean isMessageFading;                         // Of het bericht aan het vervagen is
    private String lastFailedQuestionId;                     // ID van de laatst fout beantwoorde vraag
    private float monsterX, monsterY;                        // Positie van het monster
    private com.badlogic.gdx.graphics.g2d.TextureRegion monsterTexture;  // Monster sprite
    private com.badlogic.gdx.graphics.g2d.SpriteBatch effectBatch;       // Voor speciale effecten
    private com.badlogic.gdx.graphics.glutils.ShapeRenderer shapeRenderer;  // Voor het tekenen van vormen
    private float warningTimer;                              // Timer voor waarschuwingen
    private boolean showingWarning;                          // Of er een waarschuwing wordt getoond
    private TIAObject currentTIAObject;                      // Huidig TIA object waar de speler mee interacteert
    private int finalQuestionIndex = 0;                      // Index van de huidige finale vraag
    private boolean showingFinalQuestion = false;            // Of we een finale vraag tonen
    private boolean hasMoved = false;                        // Of de speler heeft bewogen
    private boolean showingWelcome = true;                   // Of we de welkomsttekst tonen
    private float welcomeTimer = 0;                          // Timer voor welkomsttekst
    private static final float WELCOME_DURATION = 2f;        // Hoe lang de welkomsttekst blijft staan na beweging
    private float gameTimer = 0;                             // Timer voor totale speeltijd
    private boolean gameCompleted = false;                   // Of het spel is voltooid
    private float completionTimer = 0;                       // Timer voor transitie naar success screen
    private static final float COMPLETION_DELAY = 5f;        // Hoe lang wachten voor success screen
    
    // Add these fields at the top of the class with other fields
    private com.badlogic.gdx.audio.Sound correctSound;
    private com.badlogic.gdx.audio.Sound wrongSound;
    private com.badlogic.gdx.audio.Sound welcomeSound;
    private com.badlogic.gdx.audio.Sound winnerSound;

    /**
     * Maakt een nieuw GameScreen aan.
     * Initialiseert alle spel componenten en laadt de spelkaart.
     */
    public GameScreen() {
        // Initialiseer basis componenten
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
        camera.zoom = CAMERA_ZOOM;
        
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // Laad en stel de spelkaart in
        map = AssetManager.getInstance().getTiledMap("scrum.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        
        // Initialiseer speler en managers
        player = new Player(300, 420, map);
        questionManager = QuestionManager.getInstance();
        gameState = GameState.getInstance();
        font = new BitmapFont();
        typewriterEffect = new TypewriterEffect();
        
        // Load sound effects
        correctSound = AssetManager.getInstance().getSound("correct.wav");
        wrongSound = AssetManager.getInstance().getSound("wrong.wav");
        welcomeSound = AssetManager.getInstance().getSound("welcome.wav");
        winnerSound = AssetManager.getInstance().getSound("winner.wav");
        
        // Start welkomsttekst met langzamere typewriter snelheid
        typewriterEffect.setTypingSpeed(0.03f); // Langzamere snelheid voor welkomsttekst
        welcomeSound.play();
        typewriterEffect.start("Welkom bij Scrum Escape!\n\n" +
            "Gebruik de pijltjestoetsen om te bewegen.\n" +
            "Vind alle TIA objecten en beantwoord de vragen correct.\n" +
            "Pas op voor het monster als je een vraag fout beantwoordt!");
        typewriterEffect.setTypingSpeed(0.015f); // Reset naar normale snelheid
        
        // Initialiseer lijsten voor deuren en TIA objecten
        doors = new ArrayList<>();
        tiaObjects = new ArrayList<>();
        
        // Laad deuren en TIA objecten van de kaart
        loadDoors();
        loadTIAObjects();
        
        // Initialiseer spel status
        showingQuestion = false;
        waitingForAnswer = false;
        monsterTexture = AssetManager.getInstance().getTextureRegion("monster.png");
        effectBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        monsterX = 0;
        monsterY = 0;
    }

    /**
     * Laadt alle deuren van de spelkaart.
     * Elke deur heeft een unieke ID en is gekoppeld aan een vraag.
     */
    private void loadDoors() {
        MapObjects doorObjects = map.getLayers().get("Doors").getObjects();
        for (MapObject object : doorObjects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                Integer doorIdInt = object.getProperties().get("doorId", Integer.class);
                Integer questionIdInt = object.getProperties().get("questionId", Integer.class);
                
                if (doorIdInt != null && questionIdInt != null) {
                    String doorId = "door" + doorIdInt;
                    String questionId;
                    
                    // Zet vraag IDs om naar het juiste formaat
                    switch (questionIdInt) {
                        case 1: questionId = "sprint1"; break;
                        case 2: questionId = "sprint2"; break;
                        case 3: questionId = "sprint3"; break;
                        case 4: questionId = "sprint4"; break;
                        case 5: questionId = "sprint5"; break;
                        case 6: questionId = "finale"; break;
                        default: questionId = "sprint1"; // Fallback
                    }
                    
                    // Maak een nieuwe deur aan
                    Door door = new Door(
                        doorId,
                        questionId,
                        rect.x,
                        rect.y,
                        rect.width,
                        rect.height,
                        AssetManager.getInstance().getTextureRegion("door_closed.png"),
                        AssetManager.getInstance().getTextureRegion("door_open.png")
                    );
                    door.setObserver(this);
                    doors.add(door);
                }
            }
        }
    }

    /**
     * Laadt alle TIA objecten van de spelkaart.
     * TIA objecten zijn verzamelbare items die nodig zijn voor de finale vragen.
     */
    private void loadTIAObjects() {
        MapLayer tiaLayer = map.getLayers().get("TIA");
        if (tiaLayer != null) {
            MapObjects tiaObjects = tiaLayer.getObjects();
            for (MapObject object : tiaObjects) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectObject = (RectangleMapObject) object;
                    Rectangle rect = rectObject.getRectangle();
                    Integer value = object.getProperties().get("value", Integer.class);
                    
                    if (value != null) {
                        // Maak de objecten groter voor betere zichtbaarheid
                        float width = Math.max(rect.width, 64f);   // Minimale breedte van 64 pixels
                        float height = Math.max(rect.height, 64f);  // Minimale hoogte van 64 pixels
                        
                        TIAObject tiaObject = new TIAObject(
                            rect.x,
                            rect.y,
                            width,
                            height,
                            value
                        );
                        this.tiaObjects.add(tiaObject);
                    }
                }
            }
        }
    }

    /**
     * Wordt aangeroepen wanneer het scherm wordt getoond.
     * Reset de spelstatus naar de beginwaarden.
     */
    @Override
    public void show() {
        gameState.reset();
    }

    /**
     * Wordt aangeroepen elke frame om het spel te renderen.
     * @param delta Tijd sinds de laatste frame in seconden
     */
    @Override
    public void render(float delta) {
        // Freeze speler als er een vraag open is
        player.setFrozen(showingQuestion || waitingForAnswer);
        // Maak het scherm schoon
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update spel status
        update(delta);

        // Update camera positie om de speler te volgen
        camera.position.set(
            player.getX() + player.getBounds().width / 2,
            player.getY() + player.getBounds().height / 2,
            0
        );
        camera.update();

        // Update speler (voor animatie)
        player.update(delta);

        // Begin met renderen
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        // Teken de spelkaart
        mapRenderer.setView(camera);
        mapRenderer.render();
        
        // Teken alle deuren
        for (Door door : doors) {
            door.render(batch);
        }
        
        // Teken alle TIA objecten
        for (TIAObject tiaObject : tiaObjects) {
            tiaObject.render(batch);
        }
        
        // Teken de speler
        player.render(batch);
        
        // Teken het monster als het actief is
        if (gameState.isMonsterActive()) {
            batch.setColor(1, 1, 1, gameState.getMonsterAlpha());
            batch.draw(monsterTexture, monsterX, monsterY, MONSTER_SIZE, MONSTER_SIZE);
            batch.setColor(1, 1, 1, 1);
        }
        
        batch.end();
        
        // Teken de UI
        renderUI();
    }

    /**
     * Update de spelstatus.
     * @param delta Tijd sinds de laatste update in seconden
     */
    private void update(float delta) {
        // Update typewriter effect
        typewriterEffect.update(delta);
        
        // Update game timer
        if (!gameCompleted) {
            gameTimer += delta;
        }
        
        // Update completion timer
        if (gameCompleted) {
            completionTimer += delta;
            if (completionTimer >= COMPLETION_DELAY) {
                Gdx.app.postRunnable(() -> {
                    ((ScrumEscapeGame) Gdx.app.getApplicationListener()).showSuccess(gameTimer);
                    dispose();
                });
            }
        }
        
        // Update welkomsttekst timer
        if (hasMoved && showingWelcome) {
            welcomeTimer += delta;
            if (welcomeTimer >= WELCOME_DURATION) {
                showingWelcome = false;
            }
        }
        
        // Update bericht timer
        if (currentMessage != null) {
            messageTimer -= delta;
            if (messageTimer <= 0) {
                if (isMessageFading) {
                    currentMessage = null;
                    isMessageFading = false;
                } else {
                    isMessageFading = true;
                    messageTimer = MESSAGE_FADE_DURATION;
                }
            }
        }
        // Update waarschuwing timer
        if (showingWarning) {
            warningTimer -= delta;
            if (warningTimer <= 0) {
                showingWarning = false;
            }
        }
        // Update monster alpha
        gameState.updateMonsterAlpha(delta);
        // Verwerk speler input altijd zodat antwoorden werken
        handleInput();
        // Controleer botsingen alleen als er geen vraag open is
        if (!showingQuestion && !waitingForAnswer) {
            checkDoorCollision();
            checkTIACollision();
        }
    }

    /**
     * Controleert of de speler met een deur botst.
     * Als dat zo is, wordt de interactie afgehandeld.
     */
    private void checkDoorCollision() {
        for (Door door : doors) {
            if (!door.isOpen() && player.getBounds().overlaps(door.getBounds())) {
                // Reset speler positie om door botsing te voorkomen
                player.setPosition(prevPlayerX, prevPlayerY);
                
                // Controleer of de speler toegang heeft tot deze deur
                if (!gameState.canAccessDoor(door.getDoorId())) {
                    showMessage("Je moet eerst alle andere kamers hebben open gespeeld voordat je deze kamer kunt betreden!");
                    return;
                }
                
                // Controleer of dit de laatst gefaalde vraag was
                if (door.getQuestionId().equals(lastFailedQuestionId)) {
                    showMessage("Je moet eerst een andere kamer proberen voordat je deze opnieuw kunt proberen!");
                    return;
                }
                
                handleDoorInteraction(door);
                break;
            }
        }
    }

    /**
     * Handelt de interactie met een deur af.
     * @param door De deur waar de speler mee interacteert
     */
    private void handleDoorInteraction(Door door) {
        if (door.getQuestionId().equals("finale")) {
            if (!gameState.hasFoundAllTIAObjects()) {
                showMessage("Je hebt nog niet alle TIA items gevonden!");
                return;
            }
            showQuestion(door);
        } else {
            showQuestion(door);
        }
    }

    /**
     * Toont een vraag voor de gegeven deur.
     * @param door De deur waar de vraag bij hoort
     */
    private void showQuestion(Door door) {
        if (gameState.isQuestionActive()) {
            showMessage("Je moet eerst de huidige vraag afmaken!");
            return;
        }

        currentDoor = door;
        
        // Gebruik Strategy pattern voor verschillende vraag types
        if (door.getQuestionId().equals("finale")) {
            currentQuestion = QuestionManager.getFinalQuestion(finalQuestionIndex);
            showingFinalQuestion = true;
        } else {
            currentQuestion = questionManager.getQuestion(door.getQuestionId());
            showingFinalQuestion = false;
        }
        
        if (currentQuestion == null) {
            if (showingFinalQuestion) {
                showMessage("Gefeliciteerd! Je hebt alle vragen correct beantwoord!");
                showingQuestion = false;
                waitingForAnswer = false;
                showingFinalQuestion = false;
            }
            return;
        }

        // Update spel status
        showingQuestion = true;
        waitingForAnswer = true;
        gameState.setActiveQuestion(door.getQuestionId());
        
        // Start typewriter effect voor de vraag
        typewriterEffect.start(currentQuestion.getQuestion());
        
        // Positioneer monster boven de deur
        monsterX = door.getX() + MONSTER_OFFSET;
        monsterY = door.getY() + door.getBounds().height + 20;
        
        // Activeer monster als dit een herkansing is
        if (gameState.getFailedAttempts(door.getQuestionId()) > 0) {
            gameState.setMonsterActive(true);
        }
    }

    /**
     * Handelt het antwoord van de speler af.
     * @param selectedOption Het gekozen antwoord (0-based index)
     */
    private void handleAnswer(int selectedOption) {
        if (!waitingForAnswer) return;
        
        if (showingFinalQuestion) {
            handleFinalQuestionAnswer(selectedOption);
        } else {
            handleNormalQuestionAnswer(selectedOption);
        }
    }

    /**
     * Handelt het antwoord voor een finale vraag af.
     * @param selectedOption Het gekozen antwoord
     */
    private void handleFinalQuestionAnswer(int selectedOption) {
        if (QuestionManager.checkFinalAnswer(finalQuestionIndex, selectedOption)) {
            handleCorrectFinalAnswer();
        } else {
            handleWrongFinalAnswer();
        }
    }

    /**
     * Handelt het antwoord voor een normale vraag af.
     * @param selectedOption Het gekozen antwoord
     */
    private void handleNormalQuestionAnswer(int selectedOption) {
        if (currentQuestion.checkAnswer(selectedOption)) {
            handleCorrectAnswer();
        } else {
            handleWrongAnswer();
        }
    }

    /**
     * Handelt een correct antwoord af.
     * Opent de deur en geeft punten.
     */
    private void handleCorrectAnswer() {
        correctSound.play();
        gameState.markQuestionAnswered(currentQuestion.getQuestionId());
        currentDoor.setOpen(true);
        gameState.markDoorOpened(currentDoor.getDoorId());
        gameState.clearActiveQuestion();
        gameState.setMonsterActive(false);
        showingQuestion = false;
        waitingForAnswer = false;
        showMessage("Correct! De deur is nu open.");
    }

    /**
     * Handelt een fout antwoord af.
     * Activeert het monster en geeft een waarschuwing.
     */
    private void handleWrongAnswer() {
        wrongSound.play();
        gameState.markQuestionFailed(currentQuestion.getQuestionId());
        int failedAttempts = gameState.getFailedAttempts(currentQuestion.getQuestionId());
        
        if (failedAttempts >= 2) {
            ((ScrumEscapeGame) Gdx.app.getApplicationListener()).showGameOver();
        } else {
            gameState.setMonsterActive(true);
            showingWarning = true;
            warningTimer = WARNING_DURATION;
            showMessage("Dat is niet correct! Het monster komt dichterbij...\nBeantwoord de vraag goed of het monster zal je te pakken krijgen!");
            waitingForAnswer = true;
        }
    }

    /**
     * Handelt een correct antwoord voor een finale vraag af.
     * Toont de volgende finale vraag of eindigt het spel.
     */
    private void handleCorrectFinalAnswer() {
        finalQuestionIndex++;
        QuestionStrategy nextQuestion = QuestionManager.getFinalQuestion(finalQuestionIndex);
        if (nextQuestion != null) {
            currentQuestion = nextQuestion;
            typewriterEffect.start(currentQuestion.getQuestion());
        } else {
            // Speler heeft alle finale vragen voltooid
            winnerSound.play();
            typewriterEffect.start("Gefeliciteerd! Je hebt alle vragen correct beantwoord!\n\n" +
                "Je hebt het Scrum Escape spel succesvol voltooid!\n" +
                "Je hebt alle TIA objecten verzameld en alle vragen correct beantwoord.\n" +
                "Je bent nu een echte Scrum expert!");
            currentDoor.setOpen(true);
            gameState.markDoorOpened(currentDoor.getDoorId());
            gameState.clearActiveQuestion();
            gameState.setMonsterActive(false);
            showingQuestion = false;
            waitingForAnswer = false;
            showingFinalQuestion = false;
            gameCompleted = true;  // Markeer het spel als voltooid
        }
    }

    /**
     * Handelt een fout antwoord voor een finale vraag af.
     * Toont een waarschuwing en activeert het monster.
     */
    private void handleWrongFinalAnswer() {
        gameState.setMonsterActive(true);
        showingWarning = true;
        warningTimer = WARNING_DURATION;
        showMessage("Dat is niet correct! Het monster komt dichterbij...\nBeantwoord de vraag goed of het monster zal je te pakken krijgen!");
        waitingForAnswer = true;
    }

    /**
     * Verwerkt speler input.
     * Beweegt de speler en handelt antwoorden af.
     */
    private void handleInput() {
        // Bewaar huidige positie voor botsingsdetectie
        prevPlayerX = player.getX();
        prevPlayerY = player.getY();
        
        // Verwerk beweging
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
            hasMoved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
            hasMoved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.moveUp();
            hasMoved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.moveDown();
            hasMoved = true;
        }
        
        // Verwerk antwoorden
        if (waitingForAnswer) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) handleAnswer(0);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) handleAnswer(1);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) handleAnswer(2);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) handleAnswer(3);
        }
    }

    /**
     * Toont een bericht aan de speler.
     * @param message Het bericht om te tonen
     */
    private void showMessage(String message) {
        currentMessage = message;
        messageTimer = MESSAGE_DURATION;
        isMessageFading = false;
    }

    /**
     * Rendert de UI elementen.
     * Toont score, berichten en vragen.
     */
    private void renderUI() {
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        // Render score linksboven zonder achtergrond
        font.draw(batch, "Score: " + gameState.getScore(), UI_PADDING, WINDOW_HEIGHT - UI_PADDING);
        
        // Render welkomsttekst als deze nog moet worden getoond
        if (showingWelcome) {
            renderWelcomeMessage();
        }
        
        // Render bericht indien aanwezig
        if (currentMessage != null) {
            renderMessage();
        }
        // Render vraag indien aanwezig en geen waarschuwing
        if (showingQuestion && currentQuestion != null && !showingWarning) {
            renderQuestion();
        }
        batch.end();
    }

    private void renderWelcomeMessage() {
        // Calculate text height for background
        List<String> lines = wrapText(typewriterEffect.getCurrentText(), font, WINDOW_WIDTH - 2 * QUESTION_TEXT_X);
        float totalHeight = lines.size() * QUESTION_LINE_SPACING;
        
        // Draw black background - extend to bottom of screen
        batch.end();
        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.85f); // Zwarte, deels transparante achtergrond
        shapeRenderer.rect(QUESTION_TEXT_X - 20, 0, WINDOW_WIDTH - 2 * (QUESTION_TEXT_X - 20), QUESTION_TEXT_Y + 20);
        shapeRenderer.end();
        batch.begin();
        
        // Draw text
        float y = QUESTION_TEXT_Y;
        for (String line : lines) {
            font.draw(batch, line, QUESTION_TEXT_X, y);
            y -= QUESTION_LINE_SPACING;
        }
    }

    /**
     * Rendert een bericht zonder achtergrond.
     */
    private void renderMessage() {
        // Calculate text height for background
        List<String> lines = wrapText(currentMessage, font, WINDOW_WIDTH - 2 * QUESTION_TEXT_X);
        float totalHeight = lines.size() * QUESTION_LINE_SPACING;
        
        // Draw black background
        batch.end();
        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.85f); // Zwarte, deels transparante achtergrond
        shapeRenderer.rect(QUESTION_TEXT_X - 20, QUESTION_TEXT_Y - totalHeight - 20, WINDOW_WIDTH - 2 * (QUESTION_TEXT_X - 20), totalHeight + 40);
        shapeRenderer.end();
        batch.begin();
        
        // Draw text
        float y = QUESTION_TEXT_Y;
        for (String line : lines) {
            font.draw(batch, line, QUESTION_TEXT_X, y);
            y -= QUESTION_LINE_SPACING;
        }
    }

    /**
     * Rendert een vraag met antwoordopties, alleen tekstueel en correct genummerd.
     */
    private void renderQuestion() {
        // Zwarte achtergrond tekenen achter vraag en antwoorden
        batch.end();
        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.85f); // Zwarte, deels transparante achtergrond
        shapeRenderer.rect(QUESTION_TEXT_X - 20, QUESTION_TEXT_Y - QUESTION_BOX_HEIGHT + 40, WINDOW_WIDTH - 2 * (QUESTION_TEXT_X - 20), QUESTION_BOX_HEIGHT);
        shapeRenderer.end();
        batch.begin();
        
        // Vraagtekst wrappen en tekenen
        String questionText = currentQuestion.getQuestion();
        String[] options = currentQuestion.getOptions();
        float y = QUESTION_TEXT_Y + 20; // Start 50 pixels lower
        List<String> wrappedQuestion = wrapText(questionText, font, WINDOW_WIDTH - 2 * QUESTION_TEXT_X);
        for (String line : wrappedQuestion) {
            font.draw(batch, line, QUESTION_TEXT_X, y);
            y -= QUESTION_LINE_SPACING;
        }
        
        // Extra ruimte tussen vraag en antwoorden
        y -= QUESTION_LINE_SPACING * 1.5;
        
        // Antwoorden netjes onder elkaar tekenen, met wrapping per antwoord
        for (int i = 0; i < options.length; i++) {
            String optionText = (i + 1) + ") " + options[i];
            List<String> wrappedOption = wrapText(optionText, font, WINDOW_WIDTH - 2 * QUESTION_TEXT_X);
            for (String line : wrappedOption) {
                font.draw(batch, line, QUESTION_TEXT_X, y);
                y -= QUESTION_LINE_SPACING;
            }
        }
    }

    /**
     * Helper functie om tekst te wrappen op basis van pixel breedte.
     * @param text De tekst om te wrappen
     * @param font Het lettertype om te gebruiken
     * @param maxWidth Maximale breedte in pixels
     * @return Lijst met tekstregels
     */
    private List<String> wrapText(String text, BitmapFont font, float maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        
        for (String word : words) {
            String testLine = currentLine.toString() + (currentLine.length() > 0 ? " " : "") + word;
            GlyphLayout layout = new GlyphLayout(font, testLine);
            
            if (layout.width > maxWidth) {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    lines.add(word);
                    currentLine = new StringBuilder();
                }
            } else {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            }
        }
        
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        
        return lines;
    }

    /**
     * Controleert of de speler met een TIA object botst.
     */
    private void checkTIACollision() {
        for (TIAObject tiaObject : tiaObjects) {
            if (!tiaObject.isFound() && player.getBounds().overlaps(tiaObject.getBounds())) {
                handleTIACollision(tiaObject);
                break;
            }
        }
    }

    /**
     * Handelt de interactie met een TIA object af.
     * @param tiaObject Het TIA object waar de speler mee interacteert
     */
    private void handleTIACollision(TIAObject tiaObject) {
        tiaObject.setFound(true);
        gameState.addFoundTIAObject(tiaObject.getTiaType());
        showMessage(formatTIAMessage("Je hebt een TIA object gevonden!", tiaObject.getTiaType()));
    }

    /**
     * Formatteert een bericht voor een gevonden TIA object.
     * @param message Het basis bericht
     * @param tiaType Het type TIA object (1=T, 2=I, 3=A)
     * @return Het geformatteerde bericht
     */
    private String formatTIAMessage(String message, int tiaType) {
        String tiaName;
        switch (tiaType) {
            case 1: tiaName = "Transparantie"; break;
            case 2: tiaName = "Inspectie"; break;
            case 3: tiaName = "Aanpassing"; break;
            default: tiaName = "Onbekend";
        }
        return message + "\n\nJe hebt " + tiaName + " gevonden!";
    }

    /**
     * Wordt aangeroepen wanneer een deur wordt geopend.
     * @param doorId De ID van de geopende deur
     */
    @Override
    public void onDoorOpened(String doorId) {
        // Update de spelstatus wanneer een deur wordt geopend
        gameState.markDoorOpened(doorId);
    }

    /**
     * Wordt aangeroepen wanneer het venster wordt aangepast.
     * @param width Nieuwe breedte
     * @param height Nieuwe hoogte
     */
    @Override
    public void resize(int width, int height) {
        // Update camera viewport
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
        
        // Update UI camera viewport
        uiCamera.viewportWidth = width;
        uiCamera.viewportHeight = height;
        uiCamera.update();
    }

    /**
     * Wordt aangeroepen wanneer het spel wordt gepauzeerd.
     */
    @Override
    public void pause() {}

    /**
     * Wordt aangeroepen wanneer het spel wordt hervat.
     */
    @Override
    public void resume() {}

    /**
     * Wordt aangeroepen wanneer het scherm wordt verborgen.
     */
    @Override
    public void hide() {}

    /**
     * Wordt aangeroepen wanneer het scherm wordt verwijderd.
     * Ruimt alle resources op.
     */
    @Override
    public void dispose() {
        batch.dispose();
        effectBatch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        map.dispose();
        mapRenderer.dispose();
        correctSound.dispose();
        wrongSound.dispose();
        welcomeSound.dispose();
        winnerSound.dispose();
    }
}