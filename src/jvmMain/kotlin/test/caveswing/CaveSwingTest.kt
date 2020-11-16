package test.caveswing

import games.caveswing.CaveSwingApp
import games.caveswing.MovableObject
import math.Vec2d
import test.EasyComponent
import test.XGraphicsJVM
import test.XKeyAdapter
import test.XMouseAdapter
import utilities.JEasyFrame

fun main() {
    AppTest().startApp()
}

class AppTest() {
    fun startApp() {
        val ec = EasyComponent(600, 300)
        val frame = JEasyFrame(ec, "Cave Swing Test")
        val xg = XGraphicsJVM(ec)
        ec.xg = xg
        val app = CaveSwingApp()
        ec.xApp = app

        frame.addKeyListener(XKeyAdapter(app))
        ec.addMouseListener(XMouseAdapter(app))
        ec.addMouseMotionListener(XMouseAdapter(app))

        val frameRate = 25
        val delay = 1000 / frameRate
        while (true) {
            ec.repaint()
            Thread.sleep(delay.toLong())
        }
    }

}

class Test() {
    fun testMoveable() {
        val ob = MovableObject()
        val f = Vec2d(1.0, 0.0)
        repeat(10) {
            ob.update(f, 1.0)
            println(ob)
        }
    }
}
