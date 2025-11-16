# Tetris Game Architecture Documentation

This document describes the architecture of the Tetris game implementation.

## Overview

The Tetris game follows a **Model-View-Controller (MVC)** pattern with a game loop architecture. The application uses an XApp interface pattern where components implement `paint()` and `handleKeyEvent()` methods.

## Diagrams

Two UML diagrams have been created to visualize the architecture:

1. **tetris-class-diagram.puml** - Shows class relationships and public interfaces
2. **tetris-sequence-diagram.puml** - Shows method call flow during one game loop iteration

To view these diagrams:
- Use a PlantUML viewer (online: http://www.plantuml.com/plantuml/uml/)
- Install PlantUML plugin in IntelliJ IDEA or VS Code
- Use command line: `plantuml tetris-class-diagram.puml`

## Architecture Layers

### 1. Presentation Layer
- **TetrisJVMTest**: Main entry point, creates window and runs game loop
- **EasyComponent**: Swing JComponent that triggers repainting
- **TetrisDemoLayout**: Optional layout manager that combines game, palette, and plot

### 2. Controller Layer
- **TetrisController**: Orchestrates game loop, manages agent input, updates views

### 3. Game Logic Layer
- **TetrisGame**: Game state machine, handles actions and tick progression
- **TetrisModel**: Core game state (grid, score, pieces, shape queue)

### 4. View Layer
- **TetrisView**: Renders the game board, current piece, ghost piece, and score
- **ShapePalette**: Renders preview of upcoming shapes

### 5. Domain Objects
- **TetronSprite**: Individual Tetris piece with position, rotation, collision detection
- **Tetrons**: Static object containing all shape definitions
- **Cell**: Simple data class for grid coordinates

## Key Classes and Their Responsibilities

### TetrisJVMTest (Main Entry Point)
```kotlin
fun main() {
    val ec = EasyComponent()
    val frame = JEasyFrame(ec, "X Tetris JVM Test")
    val xg = XGraphicsJVM(ec)
    ec.xg = xg
    val game = TetrisDemoLayout()
    ec.xApp = game
    frame.addKeyListener(XKeyAdapter(game))

    while (true) {
        ec.repaint()
        Thread.sleep(40) // 25 FPS
    }
}
```
**Responsibility**: Initialize components and run the game loop

---

### TetrisController
**File**: `src/commonMain/kotlin/games/tetris/TetrisController.kt`

**Key Methods Called by Others**:
- `paint(xg: XGraphics)` - Main game loop logic
- `handleKeyEvent(e: XKeyEvent)` - Keyboard input
- `getData(): List<DoubleArray>?` - Data for plotting

**Responsibilities**:
1. Check if game is over, restart if needed
2. Get action from agent (AI or keyboard)
3. Advance game state
4. Update views with current state
5. Trigger rendering

---

### TetrisGame
**File**: `src/commonMain/kotlin/games/tetris/TetrisGame.kt`

**Key Methods Called by Others**:
- `isTerminal(): Boolean` - Check if game is over
- `next(actions: IntArray)` - Advance game by one tick
- `copy(): AbstractGameState` - Create deep copy for AI lookahead
- `score(): Double` - Get current score

**Responsibilities**:
1. Handle player actions (Left, Right, Rotate, Down, Drop)
2. Implement auto-drop based on tick count
3. Manage game ticks and timing

**Action Handling**:
- **Left/Right**: Move piece horizontally
- **Rotate**: Rotate piece clockwise
- **Down**: Move piece down one row, place if can't move, check for completed rows, spawn new piece
- **Drop**: Drop piece to bottom instantly
- **DoNothing**: No action

---

### TetrisModel
**File**: `src/commonMain/kotlin/games/tetris/TetrisModel.kt`

**Key Methods Called by Others**:
- `move(dx: Int, dy: Int): Boolean` - Try to move current piece
- `rotate()` - Try to rotate current piece
- `place()` - Lock piece into grid
- `checkRows(): Boolean` - Check and clear completed rows
- `newShape(): Boolean` - Spawn next piece from queue
- `gameOn(): Boolean` - Check if game can continue
- `getGhost(): TetronSprite?` - Get drop preview piece

**Responsibilities**:
1. Maintain game grid (2D array)
2. Manage current active piece
3. Maintain queue of upcoming pieces (for palette)
4. Handle piece placement and row clearing
5. Calculate score

**Shape Queue System**:
- Maintains queue of 5 upcoming shapes
- When new shape is spawned, takes from front of queue
- Automatically refills queue to maintain size
- Supports both random and deterministic (cyclic) generation

---

### TetrisView
**File**: `src/commonMain/kotlin/games/tetris/TetrisView.kt`

**Key Methods Called by Others**:
- `setData(a, shape, ghostShape, score)` - Update data to render
- `paint(xg: XGraphics)` - Render everything

**Responsibilities**:
1. Calculate cell size based on window size
2. Draw background
3. Draw grid (placed pieces)
4. Draw ghost piece (drop preview in gray)
5. Draw current active piece
6. Draw score text

---

### ShapePalette (NEW)
**File**: `src/commonMain/kotlin/games/tetris/ShapePalette.kt`

**Key Methods Called by Others**:
- `setData(shapeQueue: List<TetronSprite>)` - Update queue to display
- `paint(xg: XGraphics)` - Render palette

**Responsibilities**:
1. Display next N shapes (default 3)
2. Center each shape in its display area
3. Show "Next" title

**Configuration**:
```kotlin
val shapePalette = ShapePalette(nShapes = 3)  // Show 3 upcoming shapes
```

---

### TetronSprite
**File**: `src/commonMain/kotlin/games/tetris/Tetrons.kt`

**Key Methods Called by Others**:
- `move(dx, dy, a): Boolean` - Try to move, validate, undo if invalid
- `rotate(a): Boolean` - Try to rotate, validate, undo if invalid
- `valid(a): Boolean` - Check if current position is valid
- `place(a)` - Write piece to grid
- `getCells(): ArrayList<Cell>` - Get cell positions for rendering
- `copy(): TetronSprite` - Create independent copy

**Responsibilities**:
1. Track piece position (x, y) and rotation
2. Validate positions (collision detection)
3. Transform relative cell positions to absolute grid positions
4. Support rotation with validation

**Properties**:
- `x, y: Int` - Position on grid
- `rot: Int` - Rotation state (0-3)
- `tetron: Int` - Shape type index (0-6)
- `color: Int` - Color index

---

### Tetrons (Object)
**File**: `src/commonMain/kotlin/games/tetris/Tetrons.kt`

**Key Methods Called by Others**:
- `getShape(index: Int, rot: Int): Tetron` - Get shape cells for given type and rotation

**Responsibilities**:
1. Define all 7 Tetris piece shapes
2. Define all rotations for each shape
3. Provide shape lookup

**Shapes Defined**:
1. Square (O-piece) - 1 rotation
2. T-piece - 4 rotations
3. Straight (I-piece) - 2 rotations
4. L-corner - 4 rotations
5. Reverse L-corner - 4 rotations
6. Left skew (S-piece) - 2 rotations
7. Right skew (Z-piece) - 2 rotations

---

## Game Loop Flow

### High-Level Flow
```
1. Main loop calls repaint() every 40ms (25 FPS)
2. EasyComponent.paintComponent() triggers
3. TetrisController.paint() executes:
   - Check if game over → restart if needed
   - Get action from agent (AI or keyboard)
   - Advance game state (TetrisGame.next())
   - Update views (TetrisView, ShapePalette)
   - Render everything
```

### Detailed Action Processing

When a piece moves down and lands:
1. `TetrisGame.next()` → `takeAction(Down)`
2. `TetrisModel.move(0, 1)` → returns false (can't move)
3. `TetrisModel.place()` → writes piece to grid
4. `TetrisModel.checkRows()` → checks for completed rows
   - If row is full: clear it, scroll rows down, add to score
5. `TetrisModel.newShape()` → spawn next piece
   - Remove first piece from queue
   - Position at top of grid
   - Validate position (game over if invalid)
   - Refill queue to maintain size

### Auto-Drop Mechanism
```kotlin
if (dropSkip > 0 && (nInternalTicks % dropSkip) == 0)
    takeAction(Actions.Down.ordinal)
```
Every N ticks, automatically move piece down one row.

---

## Design Patterns Used

### 1. Model-View-Controller (MVC)
- **Model**: TetrisModel (game state, grid, score)
- **View**: TetrisView, ShapePalette (rendering)
- **Controller**: TetrisController (orchestrates game loop)

### 2. Strategy Pattern
- `SimplePlayerInterface` allows pluggable agents
- Can swap between keyboard input and AI agents
- Examples: `TetrisKeyController`, `PolicyEvoAgent`, `SimpleEvoAgent`

### 3. Game State Pattern
- `TetrisGame` implements `ExtendedAbstractGameState`
- Allows deep copying for AI lookahead planning
- AI agents can simulate future moves without affecting actual game

### 4. Command Pattern
- Actions are enumerated: `DoNothing, Left, Right, Rotate, Down, Drop`
- `takeAction(action)` dispatches based on action type
- Centralizes action handling logic

### 5. Observer Pattern (Implicit)
- Main loop triggers repaint
- View observes model through `setData()`
- Loose coupling between layers

### 6. Object Pool Pattern
- Shape queue pre-generates pieces
- Reduces overhead of random generation during gameplay
- Enables palette preview feature

---

## Data Flow

### Rendering Data Flow
```
TetrisModel (state)
    ↓
TetrisController (orchestrator)
    ↓
TetrisView.setData(grid, sprite, ghost, score)
    ↓
TetrisView.paint(xg)
    ↓
XGraphicsJVM (Swing rendering)
```

### Input Data Flow
```
User Keyboard
    ↓
XKeyAdapter
    ↓
TetrisController.handleKeyEvent()
    ↓
TetrisKeyController (agent)
    ↓
TetrisGame.next(action)
    ↓
TetrisModel (state update)
```

### Shape Queue Data Flow
```
TetrisModel.fillShapeQueue()
    ↓
TetrisModel.shapeQueue (ArrayList)
    ├─→ TetrisModel.newShape() (consumes)
    └─→ ShapePalette.setData() (reads)
```

---

## Key Algorithms

### Collision Detection
```kotlin
fun TetronSprite.valid(a: Array<IntArray>): Boolean {
    for (c in Tetrons.getShape(tetron, rot)) {
        val cell = translate(c)
        if (outOfBounds(a, cell)) return false
        if (a[cell.x][cell.y] != BG) return false
    }
    return true
}
```
Checks each cell of the piece against:
1. Grid boundaries
2. Occupied grid cells

### Row Clearing
```kotlin
fun checkRows(): Boolean {
    var r = 0
    while (r < nRows) {
        if (full(r)) {
            score += baseReward
            clearRow(r)
            scroll(r)
            r--  // Re-check same row after scroll
        }
        r++
    }
}
```
Scans from bottom to top, clears full rows, scrolls down.

### Ghost Piece Calculation
```kotlin
fun getGhost(): TetronSprite? {
    val ts = tetronSprite
    if (ts != null) {
        val ghost = ts.copy()
        while (ghost.move(0, 1, a));  // Drop until can't move
        return ghost
    }
    return null
}
```
Creates copy of current piece and drops it to show landing position.

---

## Configuration Options

### TetrisModel Companion Object
```kotlin
var baseReward = 100           // Points per row cleared
var heightFactor = 100         // Bonus for high clears
var defaultRows = 24           // Grid height
var defaultCols = 8            // Grid width
var randomShapeColours = false // Random colors
var cyclicBlockType = true     // Deterministic shapes
var randomInitialRotation = false
var includeColumnDiffs = true  // Include in score
var gameOverPenalty = 0        // Penalty for losing
var dropSkip = 10              // Auto-drop interval
```

### Layout Configuration
In `TetrisDemoLayout.kt`:
```kotlin
val panes = layout.hPartition(
    xg.width(), xg.height(),
    3,
    arrayListOf(0.3, 0.15, 0.55)  // 30% game, 15% palette, 55% plot
)
```

---

## Testing and Running

### Run the game:
```bash
./gradlew runTetrisJVM
```

### Compile only:
```bash
./gradlew compileKotlinJvm
```

### Key Controls:
- **Arrow Left**: Move left
- **Arrow Right**: Move right
- **Arrow Up**: Rotate
- **Arrow Down**: Move down faster
- **Space**: Drop instantly

---

## Extension Points

### Adding New AI Agents
Implement `SimplePlayerInterface`:
```kotlin
interface SimplePlayerInterface {
    fun getAction(gameState: AbstractGameState, playerId: Int): Int
    fun getAgentType(): String
    fun reset(): SimplePlayerInterface
}
```

### Adding New Shapes
Add to `Tetrons` object:
```kotlin
val newShape = arrayOf(
    arrayOf(Cell(0, 0), Cell(1, 0), ...), // Rotation 0
    arrayOf(Cell(0, 0), Cell(0, 1), ...)  // Rotation 1
)
val shapes = arrayOf(..., newShape)
```

### Customizing Palette
Change number of shapes shown:
```kotlin
val shapePalette = ShapePalette(nShapes = 5)  // Show 5 shapes
```

### Customizing Layout
Adjust pane proportions in `TetrisDemoLayout`:
```kotlin
arrayListOf(0.4, 0.2, 0.4)  // Wider game, narrower palette
```

---

## Summary

The Tetris implementation demonstrates clean separation of concerns:
- **Model** manages state and logic independently
- **View** handles rendering without knowing game rules
- **Controller** orchestrates but doesn't implement logic
- **Agents** provide input through a clean interface

This architecture enables:
- Easy AI agent development (game state is copyable)
- Multiple rendering targets (could port to other platforms)
- Unit testing of game logic separate from UI
- Feature additions like the shape palette with minimal changes
