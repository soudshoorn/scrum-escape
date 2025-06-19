package nl.webser.scrum_escape.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import nl.webser.scrum_escape.AssetManager;
import nl.webser.scrum_escape.GameState;
import nl.webser.scrum_escape.ScrumEscapeGame;
import nl.webser.scrum_escape.entities.Door;
import nl.webser.scrum_escape.entities.Kamerinfo;
import nl.webser.scrum_escape.entities.Monster;
import nl.webser.scrum_escape.entities.Player;
import nl.webser.scrum_escape.entities.TIAObject;
import nl.webser.scrum_escape.hints.HintFactory;
import nl.webser.scrum_escape.hints.HintProvider;
import nl.webser.scrum_escape.jokers.Joker;
import nl.webser.scrum_escape.jokers.JokerManager;
import nl.webser.scrum_escape.observer.DoorObserver;
import nl.webser.scrum_escape.questions.QuestionManager;
import nl.webser.scrum_escape.questions.QuestionStrategy;
import nl.webser.scrum_escape.rooms.GameRoom;
import nl.webser.scrum_escape.ui.TypewriterEffect;



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
    private boolean jokerUsed = false;
    // Joker mag maar één keer gebruikt worden

    // Add these fields at the top of the class with other fields
    private com.badlogic.gdx.audio.Sound correctSound;
    private com.badlogic.gdx.audio.Sound wrongSound;
    private com.badlogic.gdx.audio.Sound welcomeSound;
    private com.badlogic.gdx.audio.Sound winnerSound;
    private TIAObject activeTIAObject = null; // Houdt bij of speler op een TIA-object staat
    private static final float MESSAGE_BOX_HEIGHT = 180f; // Vaste hoogte voor alle berichten
    private Monster monster; // Monster instantie

    //hints
    private String currentHint;
    private boolean canShowHint = false;
    
    private boolean debugMode = false;

    private final JokerManager jokerManager = new JokerManager();
    private final GameRoom gameRoom;

    private boolean jokerGekozen = false;

    private Kamerinfo kamerinfo;
    private Kamerinfo activeKamerinfo = null;





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
        "Pas op voor het monster als je een vraag fout beantwoordt!\n\n" +
        "Kies nu je joker:\n" +
        "Druk op 1 voor een Hint Joker (altijd hints)\n" +
        "Druk op 2 voor een Key Joker (sleutel voor 1 kamer)");
        typewriterEffect.setTypingSpeed(0.015f); // Reset naar normale snelheid

        // Initialiseer lijsten voor deuren en TIA objecten
        doors = new ArrayList<>();
        tiaObjects = new ArrayList<>();

        // Laad deuren en TIA objecten van de kaart
        loadDoors();
        loadTIAObjects();
        loadKamerinfo();

        // Initialiseer spel status
        showingQuestion = false;
        waitingForAnswer = false;
        monsterTexture = AssetManager.getInstance().getTextureRegion("monster.png");
        effectBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        monsterX = 0;
        monsterY = 0;
        monster = new Monster(0, 0); // Startpositie linksboven
        // Monster NIET activeren bij start

        // Initialize game room
        gameRoom = new GameRoom(this);
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

    private void loadKamerinfo() {
    MapLayer bookLayer = map.getLayers().get("Book"); // Use your actual layer name
    if (bookLayer != null) {
        MapObjects bookObjects = bookLayer.getObjects();
        for (MapObject object : bookObjects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                String message = object.getProperties().get("message", String.class);
                if (message == null) message = "Dit is het kamerboek. Hier staat uitleg over deze kamer.";
                kamerinfo = new Kamerinfo(rect.x, rect.y, rect.width, rect.height, message);
                // If you only want one book, break after the first
                break;
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
        monster.reset();
        // Monster NIET activeren bij nieuw spel
    }

    /**
     * Wordt aangeroepen elke frame om het spel te renderen.
     * @param delta Tijd sinds de laatste frame in seconden
     */
    @Override
    public void render(float delta) {
        player.setFrozen(showingQuestion || waitingForAnswer);
        Gdx.gl.glClearColor(0.157f, 0.071f, 0.102f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        camera.position.set(
            player.getX() + player.getBounds().width / 2,
            player.getY() + player.getBounds().height / 2,
            0
        );
        camera.update();
        player.update(delta);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        mapRenderer.setView(camera);
        mapRenderer.render();
        for (Door door : doors) {
            door.render(batch);
        }
        for (TIAObject tiaObject : tiaObjects) {
            tiaObject.render(batch);
        }
        player.render(batch);
        // Monster renderen
        monster.render(batch);
        batch.end();

        if (debugMode) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 0, 0, 1); // Red for hitboxes
        
            Rectangle playerBounds = player.getBounds();
            shapeRenderer.rect(playerBounds.x, playerBounds.y, playerBounds.width, playerBounds.height);
        
            for (Door door : doors) {
                Rectangle doorBounds = door.getBounds();
                shapeRenderer.rect(doorBounds.x, doorBounds.y, doorBounds.width, doorBounds.height);
            }

            if (activeKamerinfo != null) {
                Rectangle kamerinfoBounds = activeKamerinfo.getBounds();
                shapeRenderer.rect(kamerinfoBounds.x, kamerinfoBounds.y, kamerinfoBounds.width, kamerinfoBounds.height);
            }
        
            shapeRenderer.end();
        }
        renderUI();
    }

    /**
     * Update de spelstatus.
     * @param delta Tijd sinds de laatste update in seconden
     */
    private void update(float delta) {
        typewriterEffect.update(delta);
        if (!gameCompleted) {
            gameTimer += delta;
        }
        if (gameCompleted) {
            completionTimer += delta;
            if (completionTimer >= COMPLETION_DELAY) {
                Gdx.app.postRunnable(() -> {
                    ((ScrumEscapeGame) Gdx.app.getApplicationListener()).showSuccess(gameTimer);
                    dispose();
                });
            }
        }
        if (hasMoved && showingWelcome) {
            welcomeTimer += delta;
            if (welcomeTimer >= WELCOME_DURATION) {
                showingWelcome = false;
            }
        }
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
        if (showingWarning) {
            warningTimer -= delta;
            if (warningTimer <= 0) {
                showingWarning = false;
            }
        }
        // Monster alleen updaten als hij actief is
        if (monster.isActive()) {
            if (monster.update(delta, player)) {
                ((ScrumEscapeGame) Gdx.app.getApplicationListener()).showGameOver();
                monster.reset();
            }
        }
        handleInput();
        checkKamerinfoCollision();
        if (!showingQuestion && !waitingForAnswer) {
            checkDoorCollision();
            checkTIACollision();
        } else {
            if (activeTIAObject != null) {
                activeTIAObject = null;
            }
        }
    }

    private void checkKamerinfoCollision() {
    if (kamerinfo != null && player.getBounds().overlaps(kamerinfo.getBounds())) {
        activeKamerinfo = kamerinfo;
    } else if (activeKamerinfo != null) {
        activeKamerinfo = null;
    }
}

    /**
     * Controleert of de speler met een deur botst.
     * Als dat zo is, wordt de interactie afgehandeld.
     */
    private void checkDoorCollision() {
        for (Door door : doors) {
            if (door.getBounds().overlaps(player.getBounds())) {
                if (!door.isOpen()) {
                    // Terugzetten naar vorige positie als de deur dicht is
                    player.setPosition(prevPlayerX, prevPlayerY);
                    if (!showingQuestion && !waitingForAnswer) {
                        handleDoorInteraction(door);
                    }
                }
                return;
            }
        }
    }


    /**
     * Handelt de interactie met een deur af.
     * @param door De deur waar de speler mee interacteert
     */
    private void handleDoorInteraction(Door door) {
        if (door.isOpen()) {
            return;
        }

        if (door.getQuestionId().equals("finale")) {
            showFinalQuestion();
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
        showingQuestion = true;
        waitingForAnswer = true;
        gameState.setActiveQuestion(door.getQuestionId());
        typewriterEffect.start(currentQuestion.getQuestion());
        // Wis hint bij nieuwe vraag
        clearHint();
        // Monster niet resetten bij nieuwe vraag
    }
    /**
     * Handelt het antwoord van de speler af.
     * @param selectedOption Het gekozen antwoord (0-based index)
     */
    private void handleAnswer(int selectedOption) {
        if (!waitingForAnswer) return;

        waitingForAnswer = false;
        boolean isCorrect = currentQuestion.checkAnswer(selectedOption);
        int failedAttempts = gameState.getFailedAttempts(currentQuestion.getQuestionId());

        if (isCorrect) {
            correctSound.play();
            gameState.addScore(10);
            gameState.markQuestionAnswered(currentQuestion.getQuestionId());
            gameState.setMonsterActive(false);
            gameState.resetFailedQuestion(currentQuestion.getQuestionId());
            // Reset monster als deze actief was (dus na een fout antwoord)
            if (monster.isActive()) {
                monster.reset();
            }
            if (showingFinalQuestion) {
                finalQuestionIndex++;
                if (finalQuestionIndex >= 3) {
                    // Alle finale vragen zijn beantwoord
                    gameCompleted = true;
                    winnerSound.play();
                    showMessage("Gefeliciteerd! Je hebt alle vragen correct beantwoord!");
                    // Zorg ervoor dat de finale deur open gaat
                    for (Door door : doors) {
                        if (door.getQuestionId().equals("finale")) {
                            door.setOpen(true);
                            gameState.markDoorOpened(door.getDoorId());
                            break;
                        }
                    }
                } else {
                    showMessage("Correct! Ga door naar de volgende vraag.");
                }
            } else {
                showMessage("Correct! De deur is nu open.");
                currentDoor.setOpen(true);
                gameState.markDoorOpened(currentDoor.getDoorId());
            }
        } else {
            // Check of dit de tweede fout is
            if (failedAttempts >= 1) {
                // Direct game over
                wrongSound.play();
                gameState.markQuestionFailed(currentQuestion.getQuestionId());
                ((ScrumEscapeGame) Gdx.app.getApplicationListener()).showGameOver();
                monster.reset();
                return;
            }
            handleWrongAnswer();
        }

        showingQuestion = false;
        currentQuestion = null;
        gameState.clearActiveQuestion();
        // Wis hint na het beantwoorden van een vraag
        clearHint();
    }

    private void handleWrongAnswer() {
        wrongSound.play();
        gameState.markQuestionFailed(currentQuestion.getQuestionId());
        gameState.setMonsterActive(true);
        showMessage("Fout! Het monster komt eraan!");
        showingWarning = true;
        warningTimer = WARNING_DURATION;
        waitingForAnswer = false;
        canShowHint = true;
        monster.activate(player);
    }

    public void showHint() {
        if (currentQuestion == null) {
            currentHint = "Geen vraag actief";
            return;
        }
        
        HintProvider hintProvider = HintFactory.createHintProvider();
        currentHint = hintProvider.getHintForQuestion(currentQuestion.getQuestionId(), currentQuestion.getQuestion());
        System.out.println("Hint: " + currentHint);
    }

    /**
     * Verwijdert de huidige hint (wordt aangeroepen bij nieuwe vragen of na gebruik)
     */
    public void clearHint() {
        currentHint = null;
    }

    public void addEducationalAid(String aid) {
        System.out.println("Educatief hulpmiddel: " + aid);
    }

    public void openDoorWithKeyJoker() {
        correctSound.play();
        gameState.markQuestionAnswered(currentQuestion.getQuestionId());
        currentDoor.setOpen(true);
        gameState.markDoorOpened(currentDoor.getDoorId());
        gameState.clearActiveQuestion();
        monster.reset();
        showingQuestion = false;
        waitingForAnswer = false;
        clearHint();
        showMessage("Je hebt de Key Joker gebruikt! De deur is nu open zonder straf.");
    }

    /**
     * Gebruikt de joker om een vraag over te slaan zonder straf.
     * De deur gaat direct open en het monster wordt gereset.
     */
    private void useJoker() {
        if (!jokerGekozen) {
            showMessage("Je hebt nog geen joker gekozen!");
            return;
        }

        Joker joker = jokerManager.getGekozenJoker();
        if (joker == null) {
            showMessage("Je hebt geen joker geselecteerd!");
            return;
        }

        if (joker.getType() == Joker.JokerType.KEY_JOKER && jokerUsed) {
            showMessage("Je hebt de Key Joker al gebruikt!");
            return;
        }

        GameRoom room = getRoomForDoor(currentDoor);
        joker.gebruik(room);
        jokerUsed = true;
    }
    /**
     * Verwerkt speler input.
     * Beweegt de speler en handelt antwoorden af.
     */
    private void handleInput() {
        if (!jokerGekozen && showingWelcome) {
            player.setFrozen(true);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                jokerManager.chooseJoker(0); // Hint Joker
                jokerGekozen = true;
                showMessage("Hint Joker geselecteerd!");
                player.setFrozen(false);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                jokerManager.chooseJoker(1); // Key Joker
                jokerGekozen = true;
                showMessage("Key Joker geselecteerd!");
                player.setFrozen(false);
            }
            return; // Wacht tot er gekozen is
        }

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

            if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                useJoker();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                GameRoom room = getRoomForDoor(currentDoor);
                room.activateAssistant();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            debugMode = !debugMode;
        }
        
    }

    /**
     * Toont een bericht aan de speler.
     * @param message Het bericht om te tonen
     */
    public void showMessage(String message) {
        currentMessage = message;
        messageTimer = MESSAGE_DURATION;
        isMessageFading = false;
    }

    /**
     * Rendert een generiek bericht met consistente achtergrond en layout.
     */
    private void renderGenericMessage(String message) {
        // Vervang harde regeleindes door spaties zodat alles netjes gewrapped wordt, maar behoud expliciete nieuwe regels met '|||'
        String[] logicalLines = message.replace("\n", " ").split("\\|\\|\\|");
        List<String> lines = new ArrayList<>();
       
        if (currentHint != null) {
            lines.add("Hint: " + currentHint);
            lines.add("");
        }

        for (String logicalLine : logicalLines) {
            lines.addAll(wrapText(logicalLine.trim(), font, WINDOW_WIDTH - 2 * QUESTION_TEXT_X));
        }
        float totalTextHeight = lines.size() * QUESTION_LINE_SPACING;
        float boxHeight = MESSAGE_BOX_HEIGHT;
        float boxY = 0;
        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.85f);
        shapeRenderer.rect(0, boxY, WINDOW_WIDTH, boxHeight);
        shapeRenderer.end();
        batch.begin();
        float y = boxY + (boxHeight + totalTextHeight) / 2;
        for (String line : lines) {
            font.draw(batch, line, QUESTION_TEXT_X, y);
            y -= QUESTION_LINE_SPACING;
        }
        batch.end();
    }

    /**
     * Rendert de UI elementen.
     * Toont score en alle berichten via één generieke renderer.
     */
    private void renderUI() {
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        
        // Score bovenaan
        font.draw(batch, "Score: " + gameState.getScore(), UI_PADDING, WINDOW_HEIGHT - UI_PADDING);
        
        // Joker tekst eronder
        if (jokerManager.getGekozenJoker() != null) {
            String jokerText = "Joker: " + (jokerManager.getGekozenJoker().getType() == Joker.JokerType.HINT_JOKER ? "Hint Joker" : "Key Joker");
            font.draw(batch, jokerText, UI_PADDING, WINDOW_HEIGHT - UI_PADDING - 25);
        } else {
            font.draw(batch, "Joker: Geen gekozen", UI_PADDING, WINDOW_HEIGHT - UI_PADDING - 25);
        }

        if (waitingForAnswer && !jokerUsed) {
            font.draw(batch, "Druk op J om een joker te activeren", UI_PADDING, WINDOW_HEIGHT - UI_PADDING - 40);
        }
        batch.end();
        if (showingWelcome) {
            renderGenericMessage(typewriterEffect.getCurrentText());
            return;
        }
        if (activeTIAObject != null) {
            renderGenericMessage(formatTIAMessage(activeTIAObject.getTiaType()));
            return;
        }
        if (currentMessage != null) {
            renderGenericMessage(currentMessage);
            return;
        }
        if (showingQuestion && currentQuestion != null && !showingWarning) {
            StringBuilder vraag = new StringBuilder();
            vraag.append(currentQuestion.getQuestion()).append("|||\n|||"); // extra lege regel
            String[] options = currentQuestion.getOptions();
            for (int i = 0; i < options.length; i++) {
                vraag.append((i + 1)).append(") ").append(options[i]).append("|||");
            }
            renderGenericMessage(vraag.toString());
        }
        if (activeKamerinfo != null) {
            renderGenericMessage(activeKamerinfo.getMessage());
        }
    }

    /**
     * Controleert of de speler met een TIA object botst.
     * Toont bericht zolang speler op het object staat, verwijdert als speler eraf gaat.
     */
    private void checkTIACollision() {
        boolean found = false;
        for (TIAObject tiaObject : tiaObjects) {
            if (player.getBounds().overlaps(tiaObject.getBounds())) {
                if (!tiaObject.isFound()) {
                    tiaObject.setFound(true);
                    gameState.addFoundTIAObject(tiaObject.getTiaType());
                }
                activeTIAObject = tiaObject;
                found = true;
                break;
            }
        }
        if (!found && activeTIAObject != null) {
            // Speler is van het object af
            activeTIAObject = null;
        }
    }

    /**
     * Formatteert een bericht voor een gevonden TIA object.
     * @param tiaType Het type TIA object (1=T, 2=I, 3=A)
     * @return Het geformatteerde bericht
     */
    private String formatTIAMessage(int tiaType) {
        String tiaName;
        String explanation;
        switch (tiaType) {
            case 1:
                tiaName = "Transparantie";
                explanation = "Transparantie betekent dat iedereen dezelfde waarheid ziet. In Scrum is dit cruciaal zodat beslissingen worden genomen op basis van gedeelde informatie. Denk aan zichtbare burndown charts, duidelijke Definition of Done, en open communicatie.";
                break;
            case 2:
                tiaName = "Inspectie";
                explanation = "Inspectie houdt in dat het team regelmatig het werk en het proces beoordeelt. Hierdoor kunnen ze op tijd afwijkingen ontdekken. Inspectie gebeurt bijvoorbeeld tijdens de Daily Scrum, Sprint Review en Retrospective.";
                break;
            case 3:
                tiaName = "Aanpassing";
                explanation = "Aanpassing betekent dat het team bereid is koers te wijzigen op basis van wat uit inspectie blijkt. Scrum-teams passen hun aanpak aan om beter te presteren. Dit kan een procesaanpassing zijn, een nieuwe manier van samenwerken, of een technische verandering.";
                break;
            default:
                tiaName = "Onbekend";
                explanation = "";
        }

        return "Je hebt het " + tiaName + " TIA object gevonden!" + "\n" + explanation;
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
        monster.dispose();
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

    private GameRoom getRoomForDoor(Door door) {
        return gameRoom;
    }

    private void showFinalQuestion() {
        if (!gameState.hasFoundAllTIAObjects()) {
            player.setPosition(prevPlayerX, prevPlayerY);
            showMessage("Je hebt nog niet alle TIA items gevonden!");
            return;
        }

        if (finalQuestionIndex >= 3) {
            // Alle finale vragen zijn beantwoord
            gameCompleted = true;
            winnerSound.play();
            showMessage("Gefeliciteerd! Je hebt alle vragen correct beantwoord!");
            return;
        }

        QuestionStrategy question = questionManager.getFinalQuestion(finalQuestionIndex);
        if (question != null) {
            currentQuestion = question;
            showingQuestion = true;
            waitingForAnswer = true;
            showingFinalQuestion = true;
        }
    }
}