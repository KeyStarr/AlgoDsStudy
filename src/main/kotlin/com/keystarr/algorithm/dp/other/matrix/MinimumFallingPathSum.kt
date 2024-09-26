package com.keystarr.algorithm.dp.other.matrix

import kotlin.math.min

/**
 * LC-931 https://leetcode.com/problems/minimum-falling-path-sum/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= matrix.length == matrix\[i].length <= 100;
 *  • -100 <= matrix\[i]\[j] <= 100.
 *
 * Final notes:
 *  • solved by myself via [bottomUpSpaceOptimized] in 40 mins;
 *  • went for the most efficient implementation straight away, submit 1st try 🔥;
 *  • a different format from the matrix DP I've solved so far, but I've nailed it still. Cool! Much of the same tricks
 *   worked and the hard-worked for mental model of how exactly to arrive at the space-optimized bottom-up with all its
 *   shortcuts worked well. Deliberate practice pays off 🔥
 *
 * Value gained:
 *  • practiced a space-optimized bottom-up DP on a matrix type problem.
 */
class MinimumFallingPathSum {

    /**
     * problem rephrase:
     *  - goal: return the sum of all elements along the best valid path
     *   valid=starts at row==0, moves only bottom/diagonal-bottom-left/diagonal-bottom-right, ends at row==m-1
     *   best=min
     *  - given: Array<IntArray>
     *
     * the matrix is a square, always. Why????
     *
     * analysis:
     *  - we have a directed acyclic weighted graph represented via a matrix;
     *  - for the path we have `n` valid starting node sand `n` valid end nodes;
     *  - each choice affects further choices
     * =>
     * dijkstra for weighted? visit the node multiple times, if each subsequent time the path sum we arrive at to it with
     *  is less than the previous visit's.
     * Time: O(nodes^2)
     *
     * can we do better?
     *
     * try DP, if we could split into subproblems such that through memoization we'd compute the answer exactly once for
     * each node, time would be O(nodes). Hint at DP: combinations + min + weights + each choice affects further choices
     *
     * -------
     *
     * - function goal:
     *  return the minimum sum of a falling path from the node (0,0) to target node;
     *
     * - what's a state?
     *  - target node's coordinates:
     *   rowInd: Int
     *   columnInd: Int
     *
     * - recurrence relation:
     *  since we have the entire end row as valid end nodes => try launching the dp from each of those nodes?
     *  suppose we launched dp from one of the valid end nodes. How could we have traversed to current node? From either
     *  top or diagonal-top cells => what was the minimum sum of the path to get to those?
     *   dp(rowInd, columnInd) = min(
     *    dp(rowInd-1,columnInd-1),
     *    dp(rowInd-1,columnInd),
     *    dp(rowInd+1,columnInd+1),
     *   ) + matrix[rowInd+columnInd]
     *  WITH RESPECT TO BOUNDARIES
     *
     * - base case:
     *  any node with rowInd==0 => the only path to get there is to start there => return matrix[0]\[columnInd]
     *
     * - memoization:
     *  actually for bottom-up we just need the previous row cached to compute all nodes for the current row =>
     *  try using IntArray(size=matrix[0].size)
     *
     * Edge cases:
     *  - m == n == 1 => a single cell matrix => the answer is the element itself => correct;
     *  - max sum is => we take a node each row, so maxN*maxValue = 100*100 = 10^4 => fits into Int.
     *
     * for space-optimized bottom-up straight away
     * Time: O(n^2)
     * Space: O(n)
     */
    fun bottomUpSpaceOptimized(matrix: Array<IntArray>): Int {
        val n = matrix.size
        var previousRow = IntArray(size = n)
        var currentRow = IntArray(size = n)
        for (rowInd in matrix.indices) {
            for (columnInd in matrix.indices) {
                currentRow[columnInd] = matrix[rowInd][columnInd] + if (rowInd > 0) {
                    min(
                        if (columnInd > 0) previousRow[columnInd - 1] else Int.MAX_VALUE,
                        min(
                            previousRow[columnInd],
                            if (columnInd < n - 1) previousRow[columnInd + 1] else Int.MAX_VALUE,
                        )
                    )
                } else 0 // base cases
            }
            val buffer = previousRow
            previousRow = currentRow
            currentRow = buffer
        }
        return previousRow.minOf { it }
    }
}

fun main() {
    println(
        MinimumFallingPathSum().bottomUpSpaceOptimized(
            matrix = arrayOf(
                intArrayOf(2, 1, 3),
                intArrayOf(6, 5, 4),
                intArrayOf(7, 8, 9),
            )
        )
    )
}
