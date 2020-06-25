package test.arcade

import games.arcade.ArcadeTestApp
import gui.geometry.ContainsTestApp
import test.EasyComponent
import test.XGraphicsJVM
import test.XKeyAdapter
import test.XMouseAdapter
import utilities.JEasyFrame

fun main() {
    val ec = EasyComponent()
    val frame = JEasyFrame(ec, "X Graphics Arcade Test")
    val xg = XGraphicsJVM(ec)
    ec.xg = xg
    val app = ArcadeTestApp()
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
