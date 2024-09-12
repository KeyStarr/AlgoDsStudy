package com.keystarr.algorithm.graph

import java.util.ArrayDeque
import java.util.Queue

/**
 * LC-994 https://leetcode.com/problems/rotting-oranges/submissions/1388105539/
 * difficulty: medium
 * constraints:
 *  • 1 <= grid.size, grid[0].size <= 10;
 *  • grid\[i]\[j] is either 0,1,2.
 *
 * Final notes:
 *  • 🏆 done [efficient] by myself in 25 mins;
 *  • hm, I believe I've done a problem exactly like this when learning graphs! Only that level-1 and fresh==0, don't recall such edges;
 *   Anyway, great logic and good solve time - pure practice here. Feels great to know all the concepts and have them at the
 *   tips of my fingers, and having encountered a similar problem type before.
 *
 * Value gained:
 *  • practiced recognizing and solving a graph simulation type problem efficiently using multi-sourced BFS.
 */
class RottingOranges {

    private val directions = arrayOf(
        intArrayOf(-1, 0), // top
        intArrayOf(1, 0), // bottom
        intArrayOf(0, 1), // right
        intArrayOf(0, -1), // left
    )

    /**
     * we might:
     *  1. count all the fresh oranges;
     *  2. start BFS from rotten oranges. BFS cause it fits the simulation well: each step (=minute) all directly adjacent
     *   fresh oranges turn rotten which is 1to1 with level-order traversal in the graph + from rotten cause the simulation
     *   is driven from the rotten oranges. As we encounter any fresh orange - increment "converted" oranges count;
     *  3. return originalFreshCount == convertedCount
     *   actually return (level - 1). -1 since the first level would be just to pop out all initial rotten oranges and
     *   add the first level of actual fresh ones.
     *
     * Some fresh oranges may not become rotten ever since there may be no valid path from any rotten orange to any fresh orange.
     *
     * If we can make 1 more step => require 1 more level of BFS traversal then always there is at least 1 fresh orange left
     *  on that new level. As we stop, there are no oranges left => current level is the min time.
     *
     * edge cases:
     *  - no oranges at all -> always return 0, trivially no cell has a fresh orange -> correct as-is;
     *  - no fresh oranges -> always return 0 -> we'd do BFS and increase the level if there are any rotten oranges =>
     *   need an early return if originalFreshCount == 0, simply return 0;
     *  - no rotten oranges -> always -1 -> queue would be empty initially, so convertedCount == 0 always and originalFreshCount > 0
     *   -> correct as-is;
     *  - n == m == 1 or n == 1 or m == 1 -> dimensions within constraints play no part here, correct as-is.
     *
     * Time:
     * Space:
     *
     * --------- further optimizations
     *
     * 1. if we're allowed to modify input [grid] - modify the fresh oranges to be rotten => get rid of the need for seen.
     */
    fun efficient(grid: Array<IntArray>): Int {
        val queue: Queue<Node> = ArrayDeque()
        val seen = Array(size = grid.size) { BooleanArray(size = grid[0].size) }

        var originalFreshCount = 0
        for (row in grid.indices) {
            for (column in grid[0].indices) {
                val cell = grid[row][column]
                when (cell) {
                    ORANGE_FRESH -> originalFreshCount++
                    ORANGE_ROTTEN -> {
                        queue.add(Node(row = row, column = column))
                        seen[row][column] = true
                    }
                }
            }
        }

        if (originalFreshCount == 0) return 0

        var convertedCount = 0
        var level = 0
        while (queue.isNotEmpty()) {
            val levelSize = queue.size
            repeat(levelSize) {
                val node = queue.remove()
                directions.forEach { direction ->
                    val newRow = node.row + direction[0]
                    val newCol = node.column + direction[1]
                    if ((newRow !in grid.indices || newCol !in grid[0].indices)
                        || grid[newRow][newCol] != ORANGE_FRESH
                        || seen[newRow][newCol]
                    ) return@forEach

                    queue.add(Node(row = newRow, column = newCol))
                    seen[newRow][newCol] = true
                    convertedCount++
                }
            }
            level++
        }

        return if (convertedCount == originalFreshCount) level - 1 else -1
    }

    private class Node(val row: Int, val column: Int)
}

private const val ORANGE_FRESH = 1
private const val ORANGE_ROTTEN = 2

fun main() {
    println(
        RottingOranges().efficient(
            grid = arrayOf(
                intArrayOf(2, 1, 1),
                intArrayOf(1, 1, 0),
                intArrayOf(0, 1, 1),
            )
        )
    )
}
