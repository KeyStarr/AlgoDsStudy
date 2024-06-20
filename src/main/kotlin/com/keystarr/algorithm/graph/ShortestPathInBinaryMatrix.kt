package com.keystarr.algorithm.graph

import java.util.*

/**
 * LC-1091 https://leetcode.com/problems/shortest-path-in-binary-matrix/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= grid.length, grid\[i].length <= 100;
 *  • grid\[i]\[j] is either 0 or 1.
 *
 * Value gained:
 *  • consciously solved a graph problem with DFS for the first time ever, who-hoo!!
 *  • practiced the "allowed directions" pattern, wow, it DOES pay off with the growing amount of directions,
 *      I'm not even tempted to try hardcoding them all even for funsies :D
 */
class ShortestPathInBinaryMatrix {

    // all directions are important to check, even though in some cases we will stumble upon an already seen node,
    // (like, when we can go both bottom and bottom-right even though bottom-right leads to the answer),
    // but there is an edge case for each possible direction where it is the only valid way to the answer
    private val allowedDirections = arrayOf(
        intArrayOf(-1, 0), // top
        intArrayOf(1, 0), // bottom
        intArrayOf(0, -1), // left
        intArrayOf(0, 1), // right
        intArrayOf(1, 1), // diagonal bottom-right
        intArrayOf(-1, 1), // diagonal top-right
        intArrayOf(1, -1), // diagonal bottom-left
        intArrayOf(-1, -1), // diagonal top-left
    )

    /**
     * Problem rephrase:
     * "Given a matrix n*n, where nodes are elements == 0 and edges are 8-directional borders between these nodes,
     * return the length of the shortest valid path. A valid path must start with node (0,0) and end with (n-1, n-1),
     * in between consists only of existing nodes and edges"
     * return the number of NODES in the path.
     *
     * Shortest path in a graph - BFS + seen + allowed directions pattern? Start at [0][0].
     *
     * Edge cases:
     *  - min matrix has a single element matrix[0][0] => if its 0, return 1 else 0
     *  - matrix[0][0] == 1 => early return -1
     *  - matrix[n-1][n-1] == 1 => early return -1
     *
     * Time: always O(n^2 + e) = O(n^2), cause we visit each node and use each edge at most once:
     *  - n = the amount of nodes is proportional to n^2, at most n^2
     *  - e = the amount of edges is proportional to n, at most ~8*n^2 (rounding for nodes on edges of the matrix)
     * Space: always O(n^2) cause seen is n^2
     */
    fun efficient(grid: Array<IntArray>): Int {
        if (grid[0][0] == 1 || grid[grid.size - 1][grid[0].size - 1] == 1) return -1

        val queue: Queue<Node> = ArrayDeque()
        val seen = Array(size = grid.size) { BooleanArray(size = grid[0].size) }
        queue.add(Node(0, 0, 1))
        seen[0][0] = true

        while (queue.isNotEmpty()) {
            val node = queue.remove()
            if (node.rowInd == grid.size - 1 && node.columnInd == grid[0].size - 1) return node.steps

            allowedDirections.forEach { direction ->
                val newRowInd = node.rowInd + direction[0]
                val newColumnInd = node.columnInd + direction[1]
                if (doesCellExist(newRowInd, newColumnInd, grid)
                    && grid[newRowInd][newColumnInd] == 0
                    && !seen[newRowInd][newColumnInd]
                ) {
                    queue.add(Node(newRowInd, newColumnInd, node.steps + 1))
                    seen[newRowInd][newColumnInd] = true // add before even visiting (queue.remove) to guarantee to add only once
                }
            }
        }

        return -1
    }

    private fun doesCellExist(newRowInd: Int, newColumnInd: Int, grid: Array<IntArray>) =
        (newRowInd >= 0 && newRowInd < grid.size)
                && (newColumnInd >= 0 && newColumnInd < grid[0].size)

    private class Node(
        val rowInd: Int,
        val columnInd: Int,
        val steps: Int,
    )
}

fun main() {
    println(
        ShortestPathInBinaryMatrix().efficient(
            arrayOf(
                intArrayOf(0, 1),
                intArrayOf(1, 0)
            )
        )
    )
}
