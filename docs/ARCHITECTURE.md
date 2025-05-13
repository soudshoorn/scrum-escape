# Scrum Escape - Technische Documentatie

## Inhoudsopgave
1. [Project Overzicht](#project-overzicht)
2. [Design Patterns](#design-patterns)
3. [SOLID Principes](#solid-principes)
4. [Code Structuur](#code-structuur)

## Project Overzicht

Scrum Escape is een educatief spel dat spelers leert over Scrum en Agile principes. Het spel is gebouwd met moderne programmeertechnieken om het flexibel en onderhoudbaar te maken.

### Kern Functionaliteiten
- Speler navigatie door een virtuele omgeving
- Interactie met deuren die vragen bevatten
- Monster systeem voor foute antwoorden
- TIA (Transparantie, Inspectie, Aanpassing) objecten verzamelen
- Score systeem
- Vraag management systeem

## Design Patterns

### 1. Strategy Pattern
Het Strategy Pattern wordt gebruikt voor het afhandelen van verschillende soorten vragen. Dit is als een menu in een restaurant waar je verschillende gerechten kunt kiezen, elk met hun eigen bereidingswijze.

**Implementatie:**
```java
// Interface die definieert hoe een vraag werkt
public interface QuestionStrategy {
    String getQuestion();
    String[] getOptions();
    boolean checkAnswer(int selectedOption);
}

// Concrete implementatie voor meerkeuzevragen
public class MultipleChoiceStrategy implements QuestionStrategy {
    private final String question;
    private final String[] options;
    private final int correctOption;

    // Implementatie van de interface methodes
    @Override
    public boolean checkAnswer(int selectedOption) {
        return selectedOption == correctOption;
    }
}
```

**Voordelen:**
- Makkelijk nieuwe vraagtypes toevoegen (bijvoorbeeld open vragen of puzzels)
- Flexibele antwoordverwerking
- Gescheiden verantwoordelijkheden

### 2. Observer Pattern
Het Observer Pattern wordt gebruikt voor het monitoren van deur status en monster activiteit. Dit werkt als een deurbel systeem: als iemand op de bel drukt, krijgen alle aangesloten ontvangers een seintje.

**Implementatie:**
```java
// Interface voor deur observers
public interface DoorObserver {
    void onDoorOpened(String doorId);
}

// Implementatie in GameScreen
public class GameScreen implements DoorObserver {
    @Override
    public void onDoorOpened(String doorId) {
        // Toon de juiste vraag of activeer het monster
        if (doorId.equals("door1")) {
            showQuestion("sprint1");
        }
    }
}
```

**Voordelen:**
- Losse koppeling tussen componenten
- Efficiënte status updates
- Makkelijk nieuwe observers toevoegen

### 3. Singleton Pattern
Het Singleton Pattern wordt gebruikt voor het beheren van gedeelde resources en game state. Dit is als een scorebord in een voetbalwedstrijd: er is er maar één, en iedereen kijkt ernaar.

**Implementatie:**
```java
public class GameState {
    private static GameState instance;
    private int score;
    
    // Private constructor
    private GameState() {
        score = 0;
    }
    
    // Publieke methode om de enige instantie te krijgen
    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }
}
```

**Voordelen:**
- Centrale toegang tot gedeelde resources
- Consistente spelstatus
- Efficiënt geheugengebruik

### 4. Template Method Pattern
Het Template Method Pattern wordt gebruikt in de scherm klassen. Dit is als een receptenboek: elk recept volgt dezelfde structuur, maar de inhoud is anders.

**Implementatie:**
```java
// Basis interface voor alle schermen
public interface Screen {
    void render();    // Tekent het scherm
    void update();    // Update de logica
    void dispose();   // Ruimt op
}

// Concrete implementatie
public class GameScreen implements Screen {
    @Override
    public void render() {
        // Tekent het speelscherm
    }
    
    @Override
    public void update() {
        // Update de speler, deuren, etc.
    }
}
```

## SOLID Principes

### 1. Single Responsibility Principle (SRP)
Elke klasse heeft één specifieke verantwoordelijkheid:
- `GameState`: Beheert alleen de spelstatus
- `AssetManager`: Beheert alleen de assets
- `QuestionManager`: Beheert alleen de vragen

### 2. Open/Closed Principle (OCP)
Het systeem is open voor uitbreiding maar gesloten voor modificatie:
- Nieuwe vraagtypes kunnen worden toegevoegd zonder bestaande code aan te passen
- Nieuwe schermen kunnen worden toegevoegd zonder bestaande schermen te wijzigen

### 3. Liskov Substitution Principle (LSP)
Subtypes kunnen worden gebruikt in plaats van hun parent types:
- Alle vraag strategieën kunnen worden gebruikt waar een `QuestionStrategy` wordt verwacht
- Alle schermen kunnen worden gebruikt waar een `Screen` wordt verwacht

### 4. Interface Segregation Principle (ISP)
Interfaces zijn specifiek voor client behoeften:
- `DoorObserver` bevat alleen methodes voor deur updates
- `QuestionStrategy` bevat alleen methodes voor vraag verwerking

### 5. Dependency Inversion Principle (DIP)
Hoge-niveau modules zijn niet afhankelijk van lage-niveau modules:
- `GameScreen` gebruikt interfaces in plaats van concrete implementaties
- `QuestionManager` werkt met `QuestionStrategy` interface

## Code Structuur

### Bestandsstructuur
```
nl.webser.scrum_escape/
├── ScrumEscapeGame.java (Hoofdklasse)
├── GameState.java (Spelstatus)
├── AssetManager.java (Assets)
├── screens/
│   ├── GameScreen.java (Speelscherm)
│   └── EndScreen.java (Eindscherm)
├── entities/
│   ├── Player.java (Speler)
│   ├── Door.java (Deuren)
│   ├── Monster.java (Monster)
│   └── TIAObject.java (TIA objecten)
├── questions/
│   ├── QuestionStrategy.java (Vraag interface)
│   ├── MultipleChoiceStrategy.java (Meerkeuzevragen)
│   └── QuestionManager.java (Vraag beheer)
└── observer/
    ├── Observer.java (Observer interface)
    └── DoorObserver.java (Deur observer)
```

### Belangrijke Interfaces
1. `Screen`: Basis voor alle schermen
2. `QuestionStrategy`: Basis voor alle vraag types
3. `Observer`: Basis voor alle observers
4. `DoorObserver`: Specifiek voor deur observers

## Conclusie

Scrum Escape is gebouwd met moderne programmeertechnieken die het spel:
- Flexibel maken (makkelijk aan te passen)
- Onderhoudbaar maken (makkelijk te begrijpen)
- Uitbreidbaar maken (makkelijk nieuwe features toe te voegen)
- Betrouwbaar maken (minder kans op bugs)

De gebruikte design patterns en SOLID principes zorgen ervoor dat de code goed georganiseerd is en makkelijk te begrijpen is voor andere programmeurs. Dit maakt het makkelijk om het spel verder te ontwikkelen of aan te passen. 