package test.tetris

import games.tetris.TetrisController
import games.tetris.TetrisModel
import gui.layout.TetrisDemoLayout
import test.EasyComponent
import test.XGraphicsJVM
import test.XKeyAdapter
import utilities.JEasyFrame

fun main() {
    val ec = EasyComponent()
    val frame = JEasyFrame(ec, "X Tetris JVM Test")
    val xg = XGraphicsJVM(ec)
    ec.xg = xg
    TetrisModel.includeColumnDiffs = true
//    val game = TetrisDemoLayout() // TetrisController()
    val game = TetrisController()
    ec.xApp = game
    frame.addKeyListener(XKeyAdapter(game))
    val frameRate = 25
    val delay = 1000 / frameRate
    while (true) {
        ec.repaint()
        Thread.sleep(delay.toLong())
    }
}
