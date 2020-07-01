package test.arcade

import agents.RandomAgent
import agents.SimpleEvoAgent
import games.arcade.ArcadeTestApp
import games.arcade.AsteroidsGame
import gui.geometry.ContainsTestApp
import test.EasyComponent
import test.XGraphicsJVM
import test.XKeyAdapter
import test.XMouseAdapter
import utilities.JEasyFrame

fun main() {
    val ec = EasyComponent(640, 480)
    val frame = JEasyFrame(ec, "X Graphics Arcade Test")
    val xg = XGraphicsJVM(ec)
    ec.xg = xg
    AsteroidsGame.useActionAdapter = true
    val app = ArcadeTestApp()
    app.agent = SimpleEvoAgent(probMutation = 0.05, useMutationTransducer = false)
    app.agent = RandomAgent()
    ec.xApp = app
    frame.addKeyListener( XKeyAdapter(app))
    ec.addMouseListener( XMouseAdapter(app))
    ec.addMouseMotionListener( XMouseAdapter(app))
    val frameRate = 25
    val delay = 1000 / frameRate
    while (true) {
        ec.repaint()
        Thread.sleep(delay.toLong())
    }
}
