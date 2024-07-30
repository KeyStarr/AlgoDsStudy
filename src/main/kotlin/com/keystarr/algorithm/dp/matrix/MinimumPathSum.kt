package com.keystarr.algorithm.dp.matrix

import kotlin.math.min

/**
 * LC-64 https://leetcode.com/problems/minimum-path-sum/description/
 * constraints:
 *  • 1 <= grid.size, grid\[i].size <= 200
 *  • 0 <= grid\[i]\[j] <= 200
 *
 * Final notes:
 *  • 🏆done by myself straight-away bottom-up space-optimized, submit 1st try!
 *  • these tricks for outa bounds check really came in useful here, from [UniquePaths], where I was sorta confused by them.
 *
 * Value gained:
 *  • apparently DP can be even more efficient than dijkstra's, huh. Proper decomposition into subproblems with memoization
 *   is truly a wonderful technique, I already sort of familiarized myself with it, but it is no short of surprises, amazes me;
 *  • practiced bottom-up space-optimized DP first on a graph-matrix problem;
 *  • apparently the "start at (0,0), go to (m-1,n-1) with only down or right" is quite a common problem setup.
 */
class MinimumPathSum {

    /**
     * analysis:
     *  - dijkstra
     *   DFS through the graph but define `seen` as Array<IntArray>, where array\[i] is the total sum of numbers along the
     *    path to reach the ith node + when considering using an edge, use it only either if seen[i] == -1 or if the
     *    current sum (on the current path) of numbers is LESS than the seen[i].
     *   => time O(nodes^2)?
     *  - DP
     *   make use of memoization and reduce the time to only O(nodes) => try DP
     *
     * - what's the goal?
     *  return the minimum sum of all numbers along the path from (0,0) to target node
     *
     * - what's the state?
     *  - target node's coordinates: rowInd Int, columnInd Int
     *  - grid: Array<IntArray>
     *
     * - recurrence relation:
     *  start at (n-1,m-1) => what's the minimum sum of all numbers along the path from (0,0) to it?
     *  we can move only right and down
     *
     *  dp(rowInd,columnInd) = grid\[rowInd]\[columnInd] + min(dp(rowInd-1,columnInd), dp(rowInd, columnInd-1))
     *  i.o. the value of the node itself plus the min sum to reach either node from which the current node is accessible
     *
     * - base cases:
     *  rowInd + columnInd == 0 => grid\[rowInd]\[columnInd]
     *
     * - memoization:
     *  the total number of states is n*m. On top-down we'll need n*m array, but on bottom-up we can optimize to O(n), cause
     *  the relation is static and requires only the previous row and current row cached for computation.
     *
     * => do bottom-up DP straight-away as the most efficient one.
     *
     * Edge cases:
     *  - rowInd==0 || columnInd == 0 => only one traversable direction.
     *  - rowInd==1 || columnInd == 1 => the graph is a line, only one direction is accessible => a single path, so return
     *   just the sum of all elements in the [grid] => correct as-is;
     *  - sum => max possible path total sum is upper bound by 200*200 = 4 * 10^4 => fits into Int.
     *
     * Time: O(m*n)
     * Space: O(n)
     */
    fun bottomUpSpaceOptimized(grid: Array<IntArray>): Int {
        val n = grid[0].size
        val previousRow = IntArray(n)
        for (rowInd in grid.indices) {
            for (columnInd in grid[0].indices) {
                val topBuffer = previousRow[columnInd]
                previousRow[columnInd] = grid[rowInd][columnInd]
                val minPathValue = min(
                    if (rowInd > 0) topBuffer else Int.MAX_VALUE,
                    if (columnInd > 0) previousRow[columnInd - 1] else Int.MAX_VALUE,
                )
                previousRow[columnInd] += if (minPathValue != Int.MAX_VALUE) minPathValue else 0
            }
        }
        return previousRow[n - 1]
    }
}
