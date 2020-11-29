package games.tetris

import ggi.AbstractGameState
import ggi.AbstractValueFunction
import kotlin.math.abs
import kotlin.math.min
import kotlin.random.Random

class TetrisValueFunction : AbstractValueFunction {
    companion object {
        val rand = Random
        val eps = 1e-10
        val cellFactor = 0.01
    }
    override fun value(gameState: AbstractGameState): Double {
        val noise = rand.nextDouble() * eps
        if (!(gameState is TetrisGame)) return noise
        val a = gameState.tm.a
        var hScore = 0.0
        for (i in 0 until a.size) {
            for (j in 0 until a[i].size) {
                if (a[i][j] != TetrisConstants.BG)
                    hScore += j * j * cellFactor
            }
        }
        return hScore
    }
}



class ColHeightDiff : AbstractValueFunction {
    companion object {
        val rand = Random
        val eps = 1e-10
    }

    var heights = ArrayList<Int>()
    fun getHeights(gameState: AbstractGameState): ArrayList<Int> {
        value(gameState)
        return heights
    }

    override fun value(gameState: AbstractGameState): Double {
        val noise = rand.nextDouble() * eps
        if (!(gameState is TetrisGame)) return noise
        val a = gameState.tm.a
        val tm = (gameState as TetrisGame).tm
        val colHeights = Array<Int>(a.size){ tm.nRows }
        for (i in 0 until tm.nCols) {
            for (j in 0 until tm.nRows) {
                if (a[i][j] != TetrisConstants.BG) {
                    colHeights[i] = min(colHeights[i], j)
                }
            }
        }
        // set up the colHeights
        heights.clear()
        heights.addAll(colHeights)
        // now sum the differences in heights
        var hScore = 0.0
        for (i in 1 until tm.nCols)
            hScore += abs(colHeights[i-1] - colHeights[i])
        return -hScore
    }
}

