package games.tetris

import gui.*
import gui.XColor.Companion.black
import gui.XColor.Companion.gray
import math.Vec2d
import kotlin.math.min


class TetrisView(val nCols: Int, val nRows: Int, var binarise: Boolean = false) : XApp {

    var count = 0
    override fun paint(xg: XGraphics) {

        // work out the cell size
        calcCellSize(xg)
        drawBackground(xg)
        if (binarise) {
            binarise()
        }
        draw(xg, a)
        drawGhostShape(xg, ghostShape)
        drawShape(xg, shape)
        statusText(xg)
        // println(cellSize)
    }

    fun binarise() {
        val aa = a
        if (aa == null) return
        for (i in 0 until nCols) {
            for (j in 0 until nRows) {
                if (aa[i][j] != BG) aa[i][j] = WHITE
            }
        }
    }

    private fun statusText(xg: XGraphics) {
        val str = "Score: ${score}"
        val ts = TStyle(size = cellSize)
        val text = XText(
            str, Vec2d(cellSize * nCols / 2, cellSize), tStyle = ts
        )
        xg.draw(text)
    }

    fun calcCellSize(xg: XGraphics) {
        // val
        centre = Vec2d(xg.width() / 2, xg.height() / 2)

        // work out the cell size as the minimum of the
        // row and col size fits
        val colSize = xg.width() / nCols
        val rowSize = xg.height() / (nRows - topVisibleRow)
        cellSize = min(colSize, rowSize)
    }

    override fun handleMouseEvent(e: XMouseEvent) {
    }

    override fun handleKeyEvent(e: XKeyEvent) {
    }

    var topVisibleRow = 0
    var shape: TetronSprite? = null
    var ghostShape: TetronSprite? = null
    var a: Array<IntArray>? = null
    var w: Double = 1.0
    var h: Double = 1.0
    var cellSize = 20.0
    var centre = Vec2d()
    var score = 0

    fun setData(a: Array<IntArray>?, shape: TetronSprite?, ghostShape: TetronSprite?, score: Int = 0) {
        this.a = a
        this.shape = shape
        this.ghostShape = ghostShape
        this.score = score
    }

    fun drawBackground(xg: XGraphics) {
        val style = XStyle()
        val rect = XRect(centre, xg.width(), xg.height(), style)
        style.fg = black
        // style
        xg.draw(rect)
    }

    fun draw(xg: XGraphics, a: Array<IntArray>?) {
        if (a == null) return

        val style = XStyle()
        val rect = XRect(centre, cellSize, cellSize, style)
        for (i in 0 until nCols) {
            for (j in topVisibleRow until nRows) {
                // println(a[i][j])
                // need to just set the rectangle to draw
                style.fg = colors[a[i][j]]
                style.stroke = true
                style.lc = gray
                style.lineWidth = 1.0
                rect.centre = Vec2d((i + 0.5) * cellSize, (j + 0.5) * cellSize)
                xg.draw(rect)
                if (a[i][j] == BG) {
                    style.stroke = true
                    style.lc = XColor.blue
                    style.fg = colors[BG]
                    xg.draw(rect)
                }
            }
        }
    }

    fun drawShape(xg: XGraphics, ts: TetronSprite?) {
        if (ts == null) return
        val style = XStyle()
        val rect = XRect(centre, cellSize, cellSize, style)
        style.fg = colors[ts.color]
        // style.fg.a = 0.1f
        style.lc = black.copy(a=0.8f)
        style.stroke = true
        style.lineWidth = 1.0
        for (cell in ts.getCells()) {
            rect.centre = Vec2d((cell.x + 0.5) * cellSize, (cell.y + 0.5) * cellSize)
            xg.draw(rect)
        }
    }

    fun drawGhostShape(xg: XGraphics, ts: TetronSprite?) {
        if (ts == null) return
        val style = XStyle()
        val rect = XRect(centre, cellSize, cellSize, style)
        style.fg = XColor.gray
        style.lc = XColor.white
        style.fg.a = 0.8f
        style.lineWidth = 1.0
        style.stroke = true
        for (cell in ts.getCells()) {
            rect.centre = Vec2d((cell.x + 0.5) * cellSize, (cell.y + 0.5) * cellSize)
            xg.draw(rect)
        }
    }

    companion object {
        var colors = arrayOf(
            XColor.green, XColor.blue, XColor.red,
            XColor.yellow, XColor.magenta, XColor.pink, XColor.cyan, XColor.black, XColor.gray, XColor.white
        )

        // size of each block in pixels
        var cellSize = 20

        // var BG = 0
        var frame = XColor.blue

        // code for the background colour
        val BG = 7
        val WHITE = colors.size-1

    }
}
