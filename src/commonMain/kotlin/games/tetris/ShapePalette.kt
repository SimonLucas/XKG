package games.tetris

import gui.*
import gui.XColor.Companion.black
import gui.XColor.Companion.gray
import math.Vec2d
import kotlin.math.min

/**
 * ShapePalette displays the next N shapes that will appear in the Tetris game.
 * @param nShapes The number of shapes to display
 */
class ShapePalette(val nShapes: Int = 3) : XApp {

    var shapeQueue: List<TetronSprite> = emptyList()
    var cellSize = 15.0
    var centre = Vec2d()

    override fun paint(xg: XGraphics) {
        calcCellSize(xg)
        drawBackground(xg)
        drawTitle(xg)
        drawShapes(xg)
    }

    fun setData(shapeQueue: List<TetronSprite>) {
        this.shapeQueue = shapeQueue.take(nShapes)
    }

    private fun calcCellSize(xg: XGraphics) {
        centre = Vec2d(xg.width() / 2, xg.height() / 2)

        // Calculate cell size based on available space
        // Each shape needs roughly 4x4 cells, with padding between them
        val availableHeight = xg.height() / (nShapes * 5.0) // 4 for shape + 1 for padding
        val availableWidth = xg.width() / 5.0 // Leave room for 4 cells + margins
        cellSize = min(availableHeight, availableWidth)
    }

    private fun drawBackground(xg: XGraphics) {
        val style = XStyle()
        val rect = XRect(centre, xg.width(), xg.height(), style)
        style.fg = XColor(0.12f, 0.12f, 0.16f)
        xg.draw(rect)
    }

    private fun drawTitle(xg: XGraphics) {
        val str = "Next"
        val ts = TStyle(size = cellSize * 1.2)
        val text = XText(
            str, Vec2d(xg.width() / 2, cellSize * 1.5), tStyle = ts
        )
        xg.draw(text)
    }

    private fun drawShapes(xg: XGraphics) {
        val startY = cellSize * 3.5
        val spacing = cellSize * 5

        for ((index, sprite) in shapeQueue.withIndex()) {
            val offsetY = startY + (index * spacing)
            drawShape(xg, sprite, offsetY)
        }
    }

    private fun drawShape(xg: XGraphics, sprite: TetronSprite, offsetY: Double) {
        val style = XStyle()
        val rect = XRect(centre, cellSize, cellSize, style)
        style.fg = TetrisView.colors[sprite.color]
        style.lc = gray
        style.stroke = true
        style.lineWidth = 1.0

        // Get the cells for this shape at rotation 0
        val cells = sprite.getCells()

        // Calculate bounding box to center the shape
        val minX = cells.minOfOrNull { it.x } ?: 0
        val maxX = cells.maxOfOrNull { it.x } ?: 0
        val minY = cells.minOfOrNull { it.y } ?: 0
        val maxY = cells.maxOfOrNull { it.y } ?: 0

        val centerOffsetX = -(minX + maxX) / 2.0
        val centerOffsetY = -(minY + maxY) / 2.0

        for (cell in cells) {
            val x = (cell.x + centerOffsetX) * cellSize + xg.width() / 2
            val y = (cell.y + centerOffsetY) * cellSize + offsetY
            rect.centre = Vec2d(x, y)
            xg.draw(rect)
        }
    }

    override fun handleMouseEvent(e: XMouseEvent) {
        // No mouse interaction needed
    }

    override fun handleKeyEvent(e: XKeyEvent) {
        // No key interaction needed
    }
}
