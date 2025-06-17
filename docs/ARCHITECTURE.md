# Scrum Escape - Technische Documentatie

## Inhoudsopgave
1. [Project Overzicht](#project-overzicht)
2. [Design Patterns](#design-patterns)
3. [SOLID Principes](#solid-principes)
4. [Code Structuur](#code-structuur)
5. [Code Kwaliteit](#code-kwaliteit)

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
Het Strategy Pattern wordt gebruikt voor het afhandelen van verschillende soorten vragen en jokers. Dit is als een menu in een restaurant waar je verschillende gerechten kunt kiezen, elk met hun eigen bereidingswijze.

**Implementatie:**
```java
// Interface die definieert hoe een vraag werkt
public interface QuestionStrategy {
    String getQuestion();
    String[] getOptions();
    boolean checkAnswer(int selectedOption);
}

// Interface voor joker strategieën
public interface JokerStrategy {
    void gebruik(BaseRoom room);
}
```

**Voordelen:**
- Makkelijk nieuwe vraagtypes toevoegen (bijvoorbeeld open vragen of puzzels)
- Flexibele antwoordverwerking
- Gescheiden verantwoordelijkheden
- Uitbreidbaar joker systeem

### 2. Observer Pattern
Het Observer Pattern wordt gebruikt voor het monitoren van deur status en monster activiteit. Dit werkt als een deurbel systeem: als iemand op de bel drukt, krijgen alle aangesloten ontvangers een seintje.

**Implementatie:**
```java
// Interface voor deur observers
public interface DoorObserver {
    void onDoorOpened(String doorId);
}

// Interface voor algemene observers
public interface Observer {
    void onIncorrectAnswer();
}
```

**Voordelen:**
- Losse koppeling tussen componenten
- Efficiënte status updates
- Makkelijk nieuwe observers toevoegen
- Flexibele monster activatie

### 3. Singleton Pattern
Het Singleton Pattern wordt gebruikt voor het beheren van gedeelde resources en game state. Dit is als een scorebord in een voetbalwedstrijd: er is er maar één, en iedereen kijkt ernaar.

**Implementatie:**
```java
public class GameState {
    private static GameState instance;
    
    private GameState() {
        // Initialisatie
    }
    
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
- Gedeelde configuratie

### 4. Template Method Pattern
Het Template Method Pattern wordt gebruikt in de kamer en joker klassen. Dit is als een receptenboek: elk recept volgt dezelfde structuur, maar de inhoud is anders.

**Implementatie:**
```java
public abstract class BaseRoom {
    public void applyHintJoker() {
        applyHintJokerEffect();
    }

    protected abstract void applyHintJokerEffect();
}

public abstract class Joker implements JokerStrategy {
    @Override
    public abstract void gebruik(BaseRoom room);
}
```

**Voordelen:**
- Consistente structuur voor kamers
- Flexibele joker implementaties
- Herbruikbare code
- Duidelijke interface

## SOLID Principes

### 1. Single Responsibility Principle (SRP)
Elke klasse heeft één specifieke verantwoordelijkheid:
- `GameState`: Beheert alleen de spelstatus
- `AssetManager`: Beheert alleen de assets
- `QuestionManager`: Beheert alleen de vragen
- `Player`: Beheert alleen speler beweging en status
- `Door`: Beheert alleen deur status en interactie

### 2. Open/Closed Principle (OCP)
Het systeem is open voor uitbreiding maar gesloten voor modificatie:
- Nieuwe vraagtypes kunnen worden toegevoegd zonder bestaande code aan te passen
- Nieuwe schermen kunnen worden toegevoegd zonder bestaande schermen te wijzigen
- Nieuwe jokers kunnen worden toegevoegd zonder bestaande code te wijzigen
- Nieuwe kamers kunnen worden toegevoegd via BaseRoom

### 3. Liskov Substitution Principle (LSP)
Subtypes kunnen worden gebruikt in plaats van hun parent types:
- Alle vraag strategieën kunnen worden gebruikt waar een `QuestionStrategy` wordt verwacht
- Alle schermen kunnen worden gebruikt waar een `Screen` wordt verwacht
- Alle jokers kunnen worden gebruikt waar een `JokerStrategy` wordt verwacht
- Alle kamers kunnen worden gebruikt waar een `BaseRoom` wordt verwacht

### 4. Interface Segregation Principle (ISP)
Interfaces zijn specifiek voor client behoeften:
- `DoorObserver` bevat alleen methodes voor deur updates
- `QuestionStrategy` bevat alleen methodes voor vraag verwerking
- `JokerStrategy` bevat alleen methodes voor joker gebruik
- `Readable` bevat alleen methodes voor leesbare objecten

### 5. Dependency Inversion Principle (DIP)
Hoge-niveau modules zijn niet afhankelijk van lage-niveau modules:
- `GameScreen` gebruikt interfaces in plaats van concrete implementaties
- `QuestionManager` werkt met `QuestionStrategy` interface
- `BaseRoom` werkt met `JokerStrategy` interface
- `Door` werkt met `DoorObserver` interface

## Code Kwaliteit

### 1. Large Class
De `GameScreen` klasse is relatief groot en zou opgesplitst kunnen worden in kleinere, meer specifieke klassen:
- UI rendering logica
- Input verwerking
- Vraag weergave
- Monster beheer

### 2. Duplicate Code
Er is minimale code duplicatie door gebruik van:
- Template Method Pattern voor kamers en jokers
- Strategy Pattern voor vragen
- Observer Pattern voor events
- Interface implementaties

### 3. Long Method & Shotgun Surgery
- Methoden zijn over het algemeen kort en specifiek
- Veranderingen zijn gelokaliseerd door gebruik van patterns
- UI rendering logica zou verder opgesplitst kunnen worden

### 4. Mocking, Stubs & Randwaarden
- GameState gebruikt constanten voor configuratie
- Vragen en antwoorden zijn goed gestructureerd
- Monster gedrag is configureerbaar
- TIA objecten hebben vaste berichten

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
├── jokers/
│   ├── JokerStrategy.java (Joker interface)
│   ├── Joker.java (Basis joker klasse)
│   └── KeyJoker.java (Specifieke joker)
├── rooms/
│   ├── BaseRoom.java (Basis kamer klasse)
│   └── GameRoom.java (Spelkamer implementatie)
└── observer/
    ├── Observer.java (Observer interface)
    └── DoorObserver.java (Deur observer)
```

## Conclusie

Scrum Escape is gebouwd met moderne programmeertechnieken die het spel:
- Flexibel maken (makkelijk aan te passen)
- Onderhoudbaar maken (makkelijk te begrijpen)
- Uitbreidbaar maken (makkelijk nieuwe features toe te voegen)
- Betrouwbaar maken (minder kans op bugs)

De gebruikte design patterns en SOLID principes zorgen ervoor dat de code goed georganiseerd is en makkelijk te begrijpen is voor andere programmeurs. Dit maakt het makkelijk om het spel verder te ontwikkelen of aan te passen.

### Verbeterpunten
1. GameScreen klasse opsplitsen in kleinere componenten
2. UI rendering logica verder modulariseren
3. Meer unit tests toevoegen
4. Documentatie uitbreiden met meer voorbeelden 