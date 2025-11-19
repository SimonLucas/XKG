# PlantUML Plugin Setup for IntelliJ IDEA

## Installation Complete ✓

Graphviz has been successfully installed:
- **Location**: `/opt/homebrew/bin/dot`
- **Version**: 14.0.4

## Configure IntelliJ IDEA PlantUML Plugin

### Step 1: Install PlantUML Plugin (if not done)
1. Open IntelliJ IDEA
2. Go to: **IntelliJ IDEA > Settings** (or `⌘,`)
3. Navigate to: **Plugins**
4. Click **Marketplace** tab
5. Search for: **PlantUML integration**
6. Click **Install**
7. Restart IntelliJ IDEA

### Step 2: Configure Graphviz Path
1. Open IntelliJ IDEA Settings (`⌘,`)
2. Navigate to: **Languages & Frameworks > PlantUML**
3. In the **Graphviz dot executable** field, enter:
   ```
   /opt/homebrew/bin/dot
   ```
4. Click **Test** button to verify the path works
5. Click **OK** to save

### Alternative: Let Plugin Auto-Detect
The PlantUML plugin should automatically detect Graphviz in the standard Homebrew location (`/opt/homebrew/bin/dot`). If it doesn't:
1. Try restarting IntelliJ IDEA
2. Or manually set the path as shown above

## Viewing the Diagrams

### Option 1: Preview in IntelliJ (Recommended)
1. Open any `.puml` file in the `docs/` directory:
   - `tetris-class-diagram.puml`
   - `tetris-sequence-diagram.puml`
2. The diagram should automatically render in a preview pane on the right
3. You can zoom in/out using the toolbar
4. Right-click the diagram to export as PNG/SVG

### Option 2: Generate PNG from Command Line
Install PlantUML CLI (optional):
```bash
brew install plantuml
cd docs
plantuml tetris-class-diagram.puml
plantuml tetris-sequence-diagram.puml
```

This creates PNG files that you can open with any image viewer.

### Option 3: Online Viewer (No installation needed)
1. Go to: http://www.plantuml.com/plantuml/uml/
2. Open a `.puml` file in a text editor
3. Copy the entire contents
4. Paste into the web form
5. Click **Submit** to render

## Troubleshooting

### Plugin shows "Cannot find Graphviz"
**Solution**: Manually set the path as described in Step 2 above.

### Plugin shows "dot command not found"
**Solution**: Your PATH might not include `/opt/homebrew/bin`. Add to your shell:
```bash
echo 'export PATH="/opt/homebrew/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

### Diagrams render but look weird
**Solution**: The PlantUML files use standard syntax. If layout is strange:
- Try zooming out (the diagrams might be large)
- Check that Graphviz version is 14.0+ (run: `dot -V`)

### Plugin crashes or freezes
**Solution**:
1. Increase memory for IntelliJ: **Help > Edit Custom VM Options**
2. Add: `-Xmx4g` (increase heap to 4GB)
3. Restart IntelliJ

### Still having issues?
**Fallback Solution**: Use the online viewer at http://www.plantuml.com/plantuml/uml/

## Verification Test

To verify everything is working:
1. Open: `docs/tetris-class-diagram.puml`
2. You should see a UML class diagram showing:
   - TetrisController
   - TetrisGame
   - TetrisModel
   - TetrisView
   - ShapePalette
   - And their relationships

If you see the diagram, everything is working correctly! 🎉

## Quick Reference: Diagram Locations

- **Class Diagram**: Shows structure and relationships
  - File: `docs/tetris-class-diagram.puml`
  - Shows: Classes, methods, has-a relationships

- **Sequence Diagram**: Shows game loop flow
  - File: `docs/tetris-sequence-diagram.puml`
  - Shows: Method calls, timing, interactions

- **Architecture Doc**: Detailed explanation
  - File: `docs/tetris-architecture-summary.md`
  - Shows: Everything explained in detail
