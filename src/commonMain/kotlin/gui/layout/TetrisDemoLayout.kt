package gui.layout

import agents.PolicyEvoAgent
import games.tetris.ShapePalette
import games.tetris.TetrisController
import games.tetris.TetrisModel
import gui.*
import plot.EasyGraphPlot

class TetrisDemoLayout : XApp {

    // just pop a sample layout here for now
    val plotApp: EasyGraphPlot = EasyGraphPlot()
    val arcade = TetrisController()
    val shapePalette = ShapePalette(nShapes = 3)
    val xp = XPalette(seed = 2)

    init {
        arcade.agent =  PolicyEvoAgent(
            sequenceLength = 20,
            nEvals = 100, useMutationTransducer = true,
            discountFactor = 0.98,
            probMutation = 0.3
        )
        // PolicyEvoAgent() // SimpleEvoAgent()
        TetrisModel.includeColumnDiffs = true
        TetrisModel.cyclicBlockType = true
    }

    override fun paint(xg: XGraphics) {

        val layout = Layout(0.05)
        val panes = layout.hPartition(
            xg.width(), xg.height()
            , 3,
            arrayListOf(0.3, 0.15, 0.55)
        )

        panes[0].app = arcade
        panes[1].app = shapePalette
        panes[2].app = plotApp

        // Update the palette with the current shape queue
        shapePalette.setData(arcade.tg.tm.shapeQueue)

        plotApp.setData(arcade.getData())

        // now draw everything

        var cIndex = 0
        val bgRect = XRect(
            xg.centre(), xg.width(), xg.height(),
            XStyle(fg = xp.getColor(cIndex++ ))
        )
        xg.draw(bgRect)

        // draw each pane in a new color
        for (pane in panes) {
            val xRext = pane.XRect(XStyle(fg = xp.colors[cIndex++]))
            xg.draw(xRext)

            pane.app?.let {

                xg.setBounds(pane)
                it.paint(xg)
                xg.releaseBounds()
            }
            // now put the associated app in, if there is one
        }
    }

    override fun handleMouseEvent(e: XMouseEvent) {
        // need to work out how to translate these into the individual apps
    }

    override fun handleKeyEvent(e: XKeyEvent) {
        arcade.handleKeyEvent(e)
    }

}
