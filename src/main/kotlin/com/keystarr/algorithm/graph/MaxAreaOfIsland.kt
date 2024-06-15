package com.keystarr.algorithm.graph

/**
 * LC-695 https://leetcode.com/problems/max-area-of-island/description/
 * difficulty: medium
 * constraints:
 *  - m == grid.length (column size)
 *  - n == grid\[i].length (row size)
 *  - 1 <= m, n <= 50
 *  - grid\[i]\[j] is either 0 or 1.
 *
 * Final notes:
 *  - solved via [solution] by myself in ~30 mins;
 *  - FAILED 1ST SUBMISSION!! ONCE AGAIN LIKE IN [NumberOfIslands] ERRONEOUSLY TYPES J UPPER BOUNDARY TO BE grid.size
 *      WHILE IT MUST BE grid[0].size CAUSE THE MATRIX ISNT OF SQUARE FORM!!!! I knew that, but blacked out in the moment
 *      and sorta typed it outa habit;
 *
 * Value gained:
 *  - practiced graph DFS on input in the form of a matrix of nodes. Basically the problem's pattern is 1to1 with [NumberOfIslands],
 *   it'd be cool to solve more distinguished variations of this, like, really different rules for edges or smth;
 *  - stay vigilant when traversing matrix, make sure upper boundaries are correct!
 */
class MaxAreaOfIsland {

    private val allowedDirections = arrayOf(
        intArrayOf(-1, 0), // top
        intArrayOf(+1, 0), // bottom
        intArrayOf(0, -1), // left
        intArrayOf(0, +1), // right
    )

    /**
     * Problem rephrase:
     *  - we are given an undirected cyclic graph in a form of a matrix;
     *  - each element is a node and two nodes are connected via an edge if the distance between them
     *   by x or by y of is exactly 1. Each node has up to 4 edges.
     * Goal: return the max amount of nodes in a connected component (i.o. find the best connected component and return its metric)
     *
     * Best connected component + undirected graph => traversal.
     *
     * Idea:
     *  - seen = Array(size = grid.size) { BooleanArray(size = grid[0].size) } // safe cuz grid.size is at least 1
     *  - maxComponentArea = 0
     *  - for i in grid.indices:
     *      - for j in grid[0].indices:
     *          - newComponentArea = dfs(i,j, grid, seen)
     *          - if (newComponentArea > maxComponentArea) maxComponentArea = newComponentArea
     *  - return maxComponentArea
     *
     * dfs(i, j, grid, seen)
     * Goal: return the count of all previously unseen (at the beginning of current call) nodes of the component with the node i,j
     *  - base case
     *      - if grid[i][j] == 0 return 0
     *      - if seen[i][j] == true => return 0
     *  - recursive case
     *      - seen[i][j] = true
     *      // if i,j are respectively for each case within the boundaries of grid
     *      - newNodesCount = 1 + dfs(left) + dfs(right) + dfs(bottom) + dfs(top)
     *      - return newNodesCount
     *
     * Edge cases:
     *  - m == 1 and n == 1 => a single node, if grid[0][0] == 1 expected answer is 1, otherwise 0 => correct;
     *  - out of bounds of grid indices are considered water, i.o. these are not nodes.
     *
     * Time: O(n*m + e)
     *  - try each cell in the matrix exactly once => n*m
     *  - try each edge exactly once => e
     * Space: O(n*m)
     *  - seen is n*m
     *  - worst callstack size of dfs is the max area of the component, but generally depends on n*m
     */
    fun solution(grid: Array<IntArray>): Int {
        val seen = Array(size = grid.size) { BooleanArray(size = grid[0].size) }
        var maxComponentArea = 0
        for (i in grid.indices) {
            for (j in grid[0].indices) {
                val newComponentArea = dfs(i, j, grid, seen)
                if (newComponentArea > maxComponentArea) maxComponentArea = newComponentArea
            }
        }
        return maxComponentArea
    }

    private fun dfs(i: Int, j: Int, grid: Array<IntArray>, seen: Array<BooleanArray>): Int {
        if (grid[i][j] == 0) return 0
        if (seen[i][j]) return 0

        seen[i][j] = true
        var newNodesCount = 1
        allowedDirections.forEach { direction ->
            val newI = i + direction[0]
            val newJ = j + direction[1]
            if ((newI >= 0 && newI < grid.size) && (newJ >= 0 && newJ < grid[0].size)) {
                newNodesCount += dfs(newI, newJ, grid, seen)
            }
        }
        return newNodesCount
    }

    // TODO: solve via Union Find
}

fun main() {
    println(
        MaxAreaOfIsland().solution(
            arrayOf(
                intArrayOf(0, 0, 0, 0, 0),
                intArrayOf(0, 1, 0, 1, 0),
                intArrayOf(0, 1, 1, 1, 0),
                intArrayOf(0, 0, 0, 1, 0),
                intArrayOf(0, 0, 0, 0, 0),
            )
        )
    )
}
