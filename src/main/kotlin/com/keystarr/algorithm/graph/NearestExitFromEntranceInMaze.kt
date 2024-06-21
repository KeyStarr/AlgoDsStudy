package com.keystarr.algorithm.graph

import java.util.*

/**
 * LC-1926 https://leetcode.com/problems/nearest-exit-from-entrance-in-maze/description/
 * difficulty: medium
 * constraints:
 *  - maze.length == m
 *  - maze\[i].length == n
 *  - 1 <= m,n <= 100
 *  - maze\[i]\[j] is either '.' or '+'
 *  - entrance will always be an empty cell
 *
 * Final notes:
 *  - done by myself via [solution] in 30 mins;
 *  - 2nd submit, cause MISTYPED AGAIN (!!!!! at least 3rd time) the size of seen, this time, to be not maze[0].size but
 *      accidentally maze.size.
 *
 * Value gained:
 *  - practiced BFS for finding the shortest valid path in a graph;
 *  - from now on in every matrix problem declare explicit `m` and `n` variables for matrix size???
 *      to counter that problem with accidental wrong size? idk [isExit] looks bad.
 */
class NearestExitFromEntranceInMaze {

    private val allowedDirections = arrayOf(
        intArrayOf(-1, 0), // top
        intArrayOf(1, 0), // bottom
        intArrayOf(0, -1), // left
        intArrayOf(0, 1), // right
    )

    /**
     * Problem rephrase:
     *  We are given:
     *   - m*n matrix, where elements == '.' are nodes and direct bounds between them (up,down,left,right) are edges
     *    => an undirected graph
     *   - starting point: entranceRow, entranceColumn
     *   - entrance can't be used as an exit
     * Goal: return the number of edges along shortest path from the starting node toward the nearest matrix boundary,
     *  if it doesn't exist -1
     *
     * Shortest path => BFS
     *
     * Idea:
     *  - start BFS at the starting point;
     *  - once we remove a node from the queue which is at the border of the matrix => return the number of edges
     *      encountered along the way to it;
     *      (rationale - with BFS we explore nodes by the order of their distance from the start, so, once we've reached
     *      an exit first time, it's at least one of the shortest paths with same distance, and we can use any single of these)
     *  - use a simple seen 2D BooleanArray;
     *  - use allowedDirections pattern as a config.
     *
     * Edge cases:
     *  - there's no path to exit => we will visit every node available from starting point and queue will become empty,
     *      we'll return -1, correct;
     *  - the smallest matrix: m==n==1 (a single node in the matrix) => we have an entrance and no exit => since
     *      entrance is already in the queue and marked as seen before BFS => we won't consider it => return -1, correct.
     *  - entrance is technically an exit (at the border of the matrix) => we won't consider it => correct.
     *  - m==1 and n>1 or m>1 and n==1 => a graph in a shape of a horizontal or a vertical line => any node that isnt
     *      a wall or a entrance is an exit => we'll return the first such node, correct;
     *  - there are no walls => correct;
     *  - there are no empty cells besides the start => queue wll be empty after one while iteration => -1, correct;
     *
     * Time: average/worst O(n*m + e)
     * Space: O(n*m)
     */
    fun solution(maze: Array<CharArray>, entrance: IntArray): Int {
        val rows = maze.size
        val columns = maze[0].size

        val queue: Queue<Node> = ArrayDeque<Node>().apply {
            add(Node(rowInd = entrance[0], columnInd = entrance[1], edgesTaken = 0))
        }
        val seen = Array(size = rows) { BooleanArray(size = columns) }
        seen[entrance[0]][entrance[1]] = true

        while (queue.isNotEmpty()) {
            val node = queue.remove()
            allowedDirections.forEach { direction ->
                val newRow = node.rowInd + direction[0]
                val newColumn = node.columnInd + direction[1]
                if ((newRow in maze.indices && newColumn in maze[0].indices)
                    && maze[newRow][newColumn] == '.'
                    && !seen[newRow][newColumn]
                ) {
                    val newEdgesTakes = node.edgesTaken + 1
                    if (isExit(newRow, newColumn, rows, columns)) return newEdgesTakes
                    queue.add(Node(rowInd = newRow, columnInd = newColumn, edgesTaken = newEdgesTakes))
                    seen[newRow][newColumn] = true
                }
            }
        }

        return -1
    }

    private fun isExit(rowInd: Int, columnInd: Int, rows: Int, columns: Int) =
        (rowInd == 0 || rowInd == rows - 1) || (columnInd == 0 || columnInd == columns - 1)

    private class Node(
        val rowInd: Int,
        val columnInd: Int,
        val edgesTaken: Int,
    )
}
