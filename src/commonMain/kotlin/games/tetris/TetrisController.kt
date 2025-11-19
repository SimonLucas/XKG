package games.tetris

import agents.PolicyEvoAgent
import agents.SimpleEvoAgent
import games.arcade.RolloutDataSource
import ggi.AbstractGameState
import ggi.SimplePlayerInterface
import gui.*
import gui.XKeyMap.Companion.down
import gui.XKeyMap.Companion.left
import gui.XKeyMap.Companion.right
import gui.XKeyMap.Companion.space
import gui.XKeyMap.Companion.up

class TetrisController : XApp , RolloutDataSource{

    var tg: TetrisGame

    // val tm: TetrisModel
    val keyController = TetrisKeyController()
    var agent: SimplePlayerInterface = keyController
    val nCols = 10
    val nRows = 22

    val tv = TetrisView(nCols, nRows)

    init {
        // tetris model params will be used within game
        setModelParams()
        tg = TetrisGame()
        // need to connect this model to the game!
    }

    override fun paint(xg: XGraphics) {
        // this does much more than just paint: it manages the whole next step
        if (tg.isTerminal()) {
            // start a new game if game over
            tg = TetrisGame()
        } else {
            val action = agent.getAction(tg.copy(), 0)
            tg.next(intArrayOf(action))
            var score = tg.score()
            val message = "${tg.nTicks()}\t $score\t $action\t ${tg.totalTicks()}\t ${tg.subGoal()}"
            // println(message)
        }
        tv.setData(
            tg.tm.a,
            tg.tm.tetronSprite,
            tg.tm.getGhost(),
            tg.score().toInt())
        tv.paint(xg)
    }

    override fun handleMouseEvent(e: XMouseEvent) {
        // no mouse control for this game
    }

    override fun handleKeyEvent(e: XKeyEvent) {
        keyController.handleKeyEvent(e)
    }

    private fun setModelParams() {
        TetrisModel.defaultCols = nCols
        TetrisModel.defaultRows = nRows
        TetrisModel.includeColumnDiffs = false
        TetrisModel.gameOverPenalty = 0
        TetrisModel.cyclicBlockType = false
        TetrisModel.randomInitialRotation = true
        TetrisModel.randomShapeColours = false
        TetrisModel.gameOverPenalty = 0
        TetrisModel.dropSkip = 50
    }

    override fun getData() : List<DoubleArray>? {
        val safe = agent
        if (safe is RolloutDataSource) return safe?.getData()
        else return null
    }
}

class TetrisKeyController : SimplePlayerInterface {

    var currentKey: Int? = null

    fun handleKeyEvent(e: XKeyEvent) {
        println(e)
        if (e.t == XKeyEventType.Pressed || e.t == XKeyEventType.Down) {
            currentKey = e.keyCode
            println("Set current key to $currentKey")
        } else if (e.t == XKeyEventType.Released) {
            currentKey = null
        }
    }

    override fun getAgentType(): String {
        return "Tetris Key Controller"
    }

    val keyMap: HashMap<Int, Int> =
        hashMapOf(
            // this needs some tidying up
            left to Actions.Left.ordinal,
            up to Actions.Rotate.ordinal,
            right to Actions.Right.ordinal,
            down to Actions.Down.ordinal,
            space to Actions.Drop.ordinal
        )

    // in fact all that needs doing in this class is to set up
    // the keyMap, so should just push everything to that general class
    override fun getAction(gameState: AbstractGameState, playerId: Int): Int {
        val key = currentKey
        if (key == null) return Actions.DoNothing.ordinal
        else {
            val proposed = keyMap.get(key)
            println("Returning proposed action: $proposed")
            currentKey = null
            return if (proposed == null) Actions.DoNothing.ordinal else proposed
        }
    }

    override fun reset(): SimplePlayerInterface {
        return this
    }
}
