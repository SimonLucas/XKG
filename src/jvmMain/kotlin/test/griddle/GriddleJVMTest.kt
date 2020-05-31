package test.griddle

import games.griddle.GriddleController
import games.griddle.words.GridUtil
import test.EasyComponent
import test.XGraphicsJVM
import test.XKeyAdapter
import test.XMouseAdapter
import utilities.JEasyFrame

fun main() {
    val ec = EasyComponent()
    val frame = JEasyFrame(ec, "X Griddle JVM Test")
    val xg = XGraphicsJVM(ec)
    ec.xg = xg
    val game: GriddleController = GriddleController()
    // game.game.dict = WordReader().readIntoTrie()
    game.loadWords(WordReader().readWordList())
    // game.game.a = GridUtil.sampleGrid
    ec.xApp = game
    frame.addKeyListener(XKeyAdapter(game))
    ec.addMouseListener(XMouseAdapter(game))
    val frameRate = 10
    val delay = 1000 / frameRate
    while (true) {
        ec.repaint()
        Thread.sleep(delay.toLong())
        frame.title = "Score = ${game.game.score()}"
    }
}