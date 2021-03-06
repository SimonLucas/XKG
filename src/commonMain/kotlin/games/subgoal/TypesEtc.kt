package games.subgoal


import math.IntVec2d


typealias ScoredNodes = HashMap<Any, Double>

// Type any enables a range of state (obs) to be used
// as nodes of the graph and hence keys for the hashmaps
typealias WeightedArcs = HashMap<Any, Double>
typealias G = HashMap<Any, WeightedArcs>

typealias Path = ArrayList<IntVec2d>
data class ScoredPath (val score: Double, val path: Path? = null )

// these are graph definitions that store the best known path
// beteeen each pair of nodes together with the cost of that path
typealias ScoredPathArcs = HashMap<Any, ScoredPath>
typealias GPath = HashMap<Any, ScoredPathArcs>



interface Updater {
    fun invoke()
}
