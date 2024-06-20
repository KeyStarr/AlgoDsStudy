package com.keystarr.algorithm.graph

import java.util.ArrayDeque
import java.util.Queue

/**
 * LC-1293 https://leetcode.com/problems/01-matrix/description/
 * difficulty: HARD!!!
 * constraints:
 *  • m == grid.length, n == grid\[i].length
 *  • 1 <= m, n <= 40
 *  • 1 <= k <= m*n
 *  • grid\[i]\[j] is either 0 or 1
 *  • grid[0][0] == grid[m-1][n-1] == 0
 *
 * Value gained:
 *  • apparently we MIGHT visit the SAME NODE multiple times, but in the context of different paths! That is, when each
 *   step in the path is defined not only by the coordinates of the node, BUT also with some path metric value!!!!
 *   Like here, it is the amount of some specific types of nodes we encountered which has a constraint set on it
 *   => seen can become a 3D array of seen states of nodes/stepping stone via all paths!
 *  • practiced graph BFS with tracking of the distance via the state variable.
 */
class ShortestPathInAGridWithObstaclesElimination {

    private val allowedDirections = arrayOf(
        intArrayOf(-1, 0), // top
        intArrayOf(1, 0), // bottom
        intArrayOf(0, -1), // left
        intArrayOf(0, 1), // right
    )

    /**
     * Input form: m*n matrix, where elements are nodes and edges are direct bounds between elements (top,bottom,right,left)
     *
     * Goal rephrase:
     * "Find the shortest valid path or return -1 if none exist, return the number of edges along it"
     * where "valid" = "from (0,0) to (n-1,n-1) AND with at most [k] elements equal to 1"
     *
     * Shortest path in a graph => try BFS
     * Trick: if some node we check is still in a queue and currentPath leads to it with less obstacles
     *  => find it in the queue via time O(g) and update it's obstacleCount to current path??? total time time then O(m*n*g?)
     *
     * (checked the course article)
     *
     * Ah, treat same node visit but with different demolitionsRemaining amounts left as DIFFERENT seen states!!!
     * define seen as a 3D array
     * => disambiguate that case above, visit the same node from all these different (obstacle-wise) ways.
     *
     * seen size is [m][n][k+1] cause we can have a range from 0 to at most k obstacles destroyed along the path to a node X
     *
     * Edge cases:
     *  given: k is at least 1, and start/end point is never an obstacle;
     *  - m==n==1 => only 1 element in the [grid] => its both the start and the end, can't be an obstacle => answer is 0, correct.
     *  - any direction can lead to the shortest path => make sure to traverse all directions;
     *  - m == 1 && n > 1 => a horizontal line, move only right, destination is rightmost element => correct;
     *  - n == 1 && m > 1 => a vertical line, move only down, destination is the bottom element => correct.
     *
     * Time: always O(n*m*k)
     * Space: always O(n*m*k)
     */
    // TODO: when multiple path with different obstacles met reach the same node, isn't the path with least obstacles met
    //  always the better one? here we draw all valid paths until the end or max obstacles, apparently not? why?
    fun efficient(grid: Array<IntArray>, k: Int): Int {
        val queue: Queue<NodeVariant> = ArrayDeque<NodeVariant>().apply { add(NodeVariant(0, 0, 0, 0)) }
        val seen = Array(size = grid.size) { Array(size = grid[0].size) { BooleanArray(size = k + 1) } }
        seen[0][0][0] = true

        while (queue.isNotEmpty()) {
            val nodeVariant = queue.remove()

            if (nodeVariant.i == grid.size - 1 && nodeVariant.j == grid[0].size - 1) return nodeVariant.edges

            allowedDirections.forEach { direction ->
                val newX = nodeVariant.i + direction[0]
                val newY = nodeVariant.j + direction[1]
                if (!grid.doesCellExist(newX, newY)) return@forEach
                val newObstaclesMet = nodeVariant.obstaclesMet + grid[newX][newY]
                if (newObstaclesMet > k || seen[newX][newY][newObstaclesMet]) return@forEach
                queue.add(NodeVariant(newX, newY, newObstaclesMet, nodeVariant.edges + 1))
                seen[newX][newY][newObstaclesMet] = true
            }
        }

        return -1
    }

    private fun Array<IntArray>.doesCellExist(i: Int, j: Int) = i in indices && j in get(0).indices

    private class NodeVariant(
        val i: Int,
        val j: Int,
        val obstaclesMet: Int,
        val edges: Int,
    )
}

fun main() {
    println(
        ShortestPathInAGridWithObstaclesElimination().efficient(
            grid = arrayOf(
                intArrayOf(0, 0, 0),
                intArrayOf(1, 1, 0),
                intArrayOf(0, 0, 0),
                intArrayOf(0, 1, 1),
                intArrayOf(0, 0, 0)
            ),
            k = 1,
        )
    )
}
