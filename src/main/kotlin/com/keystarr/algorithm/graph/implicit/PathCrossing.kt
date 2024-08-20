package com.keystarr.algorithm.graph.implicit

/**
 * LC-1496 https://leetcode.com/problems/path-crossing/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= path.length <= 10^4;
 *  • path\[i] is either N,S,E,W.
 *
 * Final notes:
 *  • done [efficient] by myself in ~15 mins;
 *   messed up directions a lot cause treated them by habit as if we were working with a matrix, but that's a 2D plane.
 *   technically though it doesn't matter as long as orientation is relatively preserved
 *  • I am very surprised that a DFS with seen path simulation is actually an efficient solution time/space wise asymptotically.
 *   My intuition tells me there gotta be some optimization we can make, but apparently not.
 *
 * Value gained:
 *  • practiced a kinda implicit graph problem, but really very explicit, just greatly unusual in the DFS simulation.
 *   that single observation that crossing = visiting a previously seen coordinate was everything. Implicit cause
 *   "graph" is never mentioned.
 */
class PathCrossing {

    private val directions = mapOf(
        'N' to intArrayOf(0, 1),
        'S' to intArrayOf(0, -1),
        'W' to intArrayOf(-1, 0),
        'E' to intArrayOf(1, 0),
    )

    /**
     * we could model the problem as a graph, perform the DFS along the edges provided, each direction denoting an edge
     * between the prevX,prevY node and the prevX+direction[0],prevY+direction[1] nodes.
     *
     * what does "crossing" exactly mean?
     * since we move exactly "1 unit" in either direction then we must land at a coordinate we've already previously landed
     *  before, always.
     *
     * a simple approach #1
     *  emulate the problem iterating through the path and making a move each time updating the coordinates, but
     *  save each new coordinates into a set => when making a move check if we've already been to the coordinate, if so,
     *  return false
     *
     * basically a DFS simulation along the path with returning true if we encounter a  previously seen node
     *
     * Time: average/worst O(n)
     *  - worst is when we don't cross => emulate the entire path
     * Space: average/worst O(n)
     *  - worst is when we don't cross => add exactly n coordinates to the set
     *
     * --------------
     *
     * can the space can be reduced to O(1) or at least the const of it?
     *
     */
    fun efficient(path: String): Boolean {
        val seen = mutableSetOf<Node>().apply { add(Node(0, 0)) }
        var currentX = 0
        var currentY = 0
        path.forEach { direction ->
            val diff = directions.getValue(direction)
            currentX += diff[0]
            currentY += diff[1]
            val nextNode = Node(x = currentX, y = currentY)
            if (seen.contains(nextNode)) return true
            seen.add(nextNode)
        }
        return false
    }

    private data class Node(val x: Int, val y: Int)
}

fun main() {
    println(
        PathCrossing().efficient("NES")
    )
}
