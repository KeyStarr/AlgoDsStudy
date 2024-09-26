package com.keystarr.algorithm.dp.other.matrix

/**
 * LC-63 https://leetcode.com/problems/unique-paths-ii/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= obstacleGrid.size, obstacleGrid\[i].size <= 100;
 *  • obstacleGrid\[i]\[j] is 0 or 1.
 *
 * Final notes:
 *  • done by myself in 25 mins, straight went for bottom-up space-optimized implementation, 1st try submit;
 *  • the core of the problem => the core solution is the same as in [UniquePaths];
 *  • there's truly always a way to improve. Checked out the top submission and learned a few tricks, [shorter], so cool,
 *   I thought I had it all first-try (ego huh).
 *
 * Value gained:
 *  • practiced matrix DP, went for space-optimized bottom-up straight away;
 *  • 🔥 apparently space-optimized DP can get quite tricky if the point is to get as clean as it could be, one needs to
 *  really understand the properties of the current specific problem and use them to the max. Another great examples of that are:
 *   • [com.keystarr.algorithm.dp.multidim.BestTimeToBuyAndSellStockWithTransactionFee];
 *   • [com.keystarr.algorithm.dp.multidim.BestTimeToBuyAndSellStocksWithCooldown].
 */
class UniquePathsII {

    /**
     * analysis:
     *  - goal is the number of unique valid combinations
     *  - a combination is a path in a directed acyclic graph
     *  - each choice affects further choices, since we can only go right/down + there are obstacles => potential dead ends;
     *  => try DP
     *
     * - function goal
     *  return the number of all unique paths from (0,0) to a target node
     *
     * - state / input
     *  target node's coordinates
     *   - rowInd: Int
     *   - columnInd: Int
     *
     * - recurrence relation
     *  start at node (m-1,n-1). we could've reached it only from left or top => find how many paths we could've reached
     *  top and left with:
     *  dp(rowInd,columnInd) = dp(rowInd-1,columnInd) + dp(rowInd,columnInd-1)
     *  with respect to the grid bounds (rowInd>0, columnInd>0)
     *
     * - base cases:
     *  rowInd+columnInd==0 => 1. Exactly one path to reach the first square: only to start there.
     *  grid\[rowInd]\[columnInd] == 1 => an obstacle, we could not have come from here => return 0
     *
     * - memoization:
     *  since the recurrence relation is static and depends only on the current and previous rows => use just a single row
     *  for both
     *
     * edge cases:
     *  - the answer is at most 2*10^9 => fits into int;
     *  - m==1 && n==1 => just one square => always return 1, correct;
     *  - there's no path from (0,0) to (m-1,n-1) => return 0, correct;
     *  - the graph is a line, either m==1 or n==1 => if at least one cell is an obstacle, return 0 => correct;
     *  - either the start or the target nodes are obstacles => return 0 => do an early return for O(1) time/space.
     *
     * Time: O(m*n)
     * Space: O(n)
     */
    fun bottomUpSpaceOptimized(obstacleGrid: Array<IntArray>): Int {
        val m = obstacleGrid.size
        val n = obstacleGrid[0].size
        if (obstacleGrid[0][0] == 1 || obstacleGrid[m - 1][n - 1] == 1) return 0

        val previousRow = IntArray(size = obstacleGrid[0].size)
        previousRow[0] = 1
        for (rowInd in obstacleGrid.indices) {
            for (columnInd in obstacleGrid[0].indices) {
                if (obstacleGrid[rowInd][columnInd] == 1) { // obstacle, couldn't have come here from anywhere
                    previousRow[columnInd] = 0
                    continue
                }

                val topBuffer = previousRow[columnInd]
                if (rowInd + columnInd != 0) previousRow[columnInd] = 0
                if (rowInd > 0) previousRow[columnInd] += topBuffer // could've come from top
                if (columnInd > 0) previousRow[columnInd] += previousRow[columnInd - 1] // could've come from left
            }
        }
        return previousRow[n - 1]
    }

    /**
     * We start at the first row, each cell there has only a neighbor from the left (except the (0,0)) => we compute answers
     * for the first row and store them in `previousRow`. When we get to the 2nd row, every cell except the first one has
     * 2 neighbors, the left and the top + technically, the top neighbor's value is already added if we treat `previousRow`
     * now as the currentRow => we only have to add the left neighbor's value to get an answer for the current row if
     * grid\[i]\[j] isn't an obstacle, and if it is, we need to set it to 0.
     */
    fun shorter(obstacleGrid: Array<IntArray>): Int {
        val m = obstacleGrid.size
        val n = obstacleGrid[0].size
        if (obstacleGrid[0][0] == 1 || obstacleGrid[m - 1][n - 1] == 1) return 0

        val previousRow = IntArray(size = obstacleGrid[0].size)
        previousRow[0] = 1
        for (row in obstacleGrid) {
            for (columnInd in obstacleGrid[0].indices) {
                if (row[columnInd] == 1) {
                    previousRow[columnInd] = 0
                } else if (columnInd > 0) {
                    previousRow[columnInd] += previousRow[columnInd - 1]
                }
            }
        }
        return previousRow[n - 1]
    }
}
