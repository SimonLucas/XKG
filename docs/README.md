# Tetris Architecture Documentation

This directory contains UML diagrams and architecture documentation for the Tetris game.

## Files

- **tetris-class-diagram.puml** - UML class diagram showing class relationships and public interfaces
- **tetris-sequence-diagram.puml** - UML sequence diagram showing game loop method calls
- **tetris-architecture-summary.md** - Detailed architecture documentation

## Viewing the Diagrams

The diagrams are in PlantUML format (.puml files). You can view them using:

### Option 1: Online Viewer
1. Go to http://www.plantuml.com/plantuml/uml/
2. Copy and paste the contents of the .puml file
3. Click "Submit" to render the diagram

### Option 2: IntelliJ IDEA Plugin
1. Install the "PlantUML integration" plugin
2. Open the .puml files directly in IntelliJ
3. The diagram will render in a preview pane

### Option 3: VS Code Extension
1. Install the "PlantUML" extension by jebbs
2. Open the .puml files
3. Press `Alt+D` to preview

### Option 4: Command Line
1. Install PlantUML: `brew install plantuml` (Mac) or download from https://plantuml.com/
2. Generate PNG images:
   ```bash
   plantuml tetris-class-diagram.puml
   plantuml tetris-sequence-diagram.puml
   ```
3. This creates .png files you can open with any image viewer

## Quick Summary

### Class Diagram
Shows:
- Main classes: TetrisController, TetrisGame, TetrisModel, TetrisView, ShapePalette
- Public methods that are called by other classes (not internal implementation)
- Has-A relationships (composition)
- Data flow between components

### Sequence Diagram
Shows:
- One complete iteration of the game loop
- Method call chain from repaint() to rendering
- Action processing (move, rotate, place, new shape)
- View updates (game board and palette)
- All the interactions between objects in temporal order

### Architecture Summary
Contains:
- Detailed explanation of each class and its responsibilities
- Design patterns used (MVC, Strategy, Command, etc.)
- Configuration options
- Extension points for adding features
- Key algorithms (collision detection, row clearing, etc.)

## Key Insights

1. **Clean MVC Separation**: Model (TetrisModel) contains no rendering code, View (TetrisView) contains no game logic
2. **Strategy Pattern**: AI agents are pluggable via SimplePlayerInterface
3. **Immutable Game States**: TetrisGame can be copied for AI lookahead
4. **Queue System**: Shape queue enables the preview palette feature
5. **Single Responsibility**: Each class has a focused, clear purpose

## Quick Reference: Method Call Chain

```
Main Loop
  └─> EasyComponent.repaint()
      └─> paintComponent()
          └─> TetrisController.paint()
              ├─> TetrisGame.next(action)
              │   └─> TetrisModel.move/rotate/place/checkRows/newShape()
              │       └─> TetronSprite.move/rotate/valid/place()
              ├─> TetrisView.setData() & paint()
              └─> ShapePalette.setData() & paint()
```
