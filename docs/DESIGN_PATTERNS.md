# Design Patterns in Scrum Escape

Hieronder vind je een overzicht van de belangrijkste design patterns die in de Scrum Escape game zijn toegepast, met een korte uitleg en verwijzing naar de relevante klassen.

---

## 1. Strategy Pattern
**Toepassing:**
Het Strategy pattern wordt gebruikt voor de verschillende soorten vragen in het spel. De interface `QuestionStrategy` definieert het contract voor vragen, en concrete strategieën zoals `MultipleChoiceStrategy` implementeren deze interface. Hierdoor kan het spel flexibel verschillende vraagtypes hanteren.

**Belangrijkste klassen:**
- `QuestionStrategy` (interface)
- `MultipleChoiceStrategy` (implementatie)
- `QuestionManager` (gebruikt strategieën)

---

## 2. Observer Pattern
**Toepassing:**
Het Observer pattern wordt gebruikt om objecten te laten reageren op gebeurtenissen. Bijvoorbeeld, het `Monster` object implementeert de `Observer` interface en wordt geactiveerd bij een fout antwoord. Ook is er een `DoorObserver` interface waarmee `GameScreen` reageert op het openen van deuren.

**Belangrijkste klassen:**
- `Observer` (interface)
- `Monster` (observer van foute antwoorden)
- `DoorObserver` (interface)
- `GameScreen` (observer van deuren)
- `Door` (subject)

---

## 3. Singleton Pattern
**Toepassing:**
Het Singleton pattern zorgt ervoor dat er maar één instantie is van bepaalde klassen die de centrale status of resources beheren, zoals de spelstatus (`GameState`), de vragen (`QuestionManager`) en de assets (`AssetManager`).

**Belangrijkste klassen:**
- `GameState`
- `QuestionManager`
- `AssetManager`

---

## 4. Template Method Pattern
**Toepassing:**
Het Template Method pattern wordt gebruikt voor de kamers in het spel. De abstracte klasse `BaseRoom` definieert de structuur van kameracties (zoals het toepassen van een joker), terwijl subklassen zoals `GameRoom` de specifieke implementatie invullen.

**Belangrijkste klassen:**
- `BaseRoom` (abstracte template)
- `GameRoom` (concrete implementatie)

---

## 5. State Pattern
**Toepassing:**
Het State pattern wordt toegepast in de manier waarop het spel verschillende schermen en toestanden beheert, zoals het hoofdspel, het eindscherm en het successcherm. De `GameScreen`, `EndScreen` en `SuccessScreen` zijn verschillende toestanden van het spel, beheerd door `ScrumEscapeGame`.

**Belangrijkste klassen:**
- `ScrumEscapeGame` (beheerder van states)
- `GameScreen`, `EndScreen`, `SuccessScreen` (verschillende states)

--- 