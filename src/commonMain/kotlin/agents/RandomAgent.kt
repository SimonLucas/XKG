package agents

import ggi.AbstractGameState
import ggi.AbstractGameStateMulti
import ggi.SimplePlayerInterface
import ggi.SimplePlayerInterfaceMulti
import kotlin.random.Random

class RandomAgent(val seed: Long = -1) : SimplePlayerInterface, SimplePlayerInterfaceMulti {

    override fun getAgentType(): String {
        return "RandomAgent"
    }

    val random = Random

    init {
        // if (seed != -1L)
        // random.s
    }

    override fun getAction(gameState: AbstractGameState, playerId: Int): Int {
        if (gameState.nActions() < 1)
            // should really return null if there are zero actions available
            // but that's not allowed in the interface
            return 0
        else
            return random.nextInt(gameState.nActions())
    }

    override fun getAction(gameState: AbstractGameStateMulti, playerId: Int): Int {
        if (gameState.nActions(playerId) < 1)
        // should really return null if there are zero actions available
        // but that's not allowed in the interface
            return 0
        else
            return random.nextInt(gameState.nActions(playerId))

    }

    override fun reset(): SimplePlayerInterface {
        // do nothing
        return this
    }
}
