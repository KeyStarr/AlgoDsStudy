package com.keystarr.algorithm.dp.matrix

/**
 * LC-62 https://leetcode.com/problems/unique-paths/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= m, n <= 100.
 *
 * Final notes:
 *  • done [topDownDp] by myself in 1h;
 *  • 🔥1st try submit;
 *  • 🔥🔥💡 disciplined myself not to submit before I dry-run => CAUGHT 2 CRITICAL MISTAKES WHEN VISUAL DRY-RUNNING!!!
 *   • at first added +1 every time for each edge to the result on top of the recursive call to the neighbors => basically
 *    just counted the total number of edges that were taken along all paths considered, with duplicates :)
 *    => when I drew the n=2 m=3 case, I realized that we just need to go from the base case 1;
 *   • forgot to implement the edge case of rowInd==0 and columnInd==0, to not consider the out-of-bounds direction o_O
 *    (even thought about it during design-phase) => visually it was obvious to check.
 *  • study & practice really paid off here with graph given as a matrix traversal, and the pattern for allowedDirections;
 *  • hm, [bottomUp] here actually didn't come naturally, I had to check the answer cause I got confused how to start it,
 *   how to really implement it and what was the core idea. These >0 inside for loops are quite unusual;
 *  • for some reason it was sooooo satisfying to draw/dry run exactly on the graph tablet.
 *
 * Value gained:
 *  • 🏆solved my first ever DP matrix problem;
 *  • practiced both bottom-up and top-down on a DP matrix problem.
 */
class UniquePaths {

    private val topDownDirections = arrayOf(
        intArrayOf(-1, 0), // top
        intArrayOf(0, -1), // left
    )

    /**
     * what approach?
     *  - the matrix can be modelled as a graph;
     *  - goal is the number of all unique paths from node A to B;
     *  - since the goal is all unique paths, then basically path-context for each node of DFS/BFS would be basically
     *   all previous visited nodes => we wouldn't be able to use seen to optimize the traversal => that is a
     *   valid brute-force, but the total time/space would be huge (since everytime we visit a node we consider all directions,
     *   and we will the same node multiple times often);
     *  - that kinda looks like the subproblems are duplicated + the goal is not max/min, but each choice does affect
     *   further choices => try DP?
     *
     * - what's the goal?
     *  return the amount of unique paths that lead to the node with rowInd, columnInd
     *
     * - what's the state?
     *  - target node's coordinates: rowInd, columnInd
     *
     * - recurrence relation:
     *  we may start at the current node being the target node => how many paths do lead to it then?
     *  dp(currentNode) = sum(for each edge: dp(neighborNode))
     *
     *  valid edges for each node are only top and left cells in the matrix, if they exist (since only allowed direction
     *  to travel for the robot are bottom and right)
     *
     * - base cases:
     *  rowInd==0 && columnInd==0 => return 0 // start node has no valid inbound edges
     *
     * - memoization:
     *  subproblems do repeat, we may arrive at the same node from different neighboring nodes => the subproblem with it
     *  is the same no matter the previous context
     *  the total number of states = total number of nodes = n*m. Since max n==m==100, max total number is 10^4 => simply
     *  use Array(size=m) { IntArray(size=n) } for caching
     *
     * Edge cases:
     *  - n == m == 1 => 0? cause robot can make exactly no moves? or 1 cause hes already standing there, and it doesn't whether
     *   he made a move or not? No one to ask for clarification, so I'd say 1?
     *  - n==1 || m==1 => basically a tree with only either right or down direction possible => always return 1, correct
     *   (might actually do an early return for O(1) time and space, otherwise it is O(n) or O(m) time and space respectively)
     *  - traverse graph given as a matrix => check if the edge exists and not outa bounds of the matrix on traversal
     *   with the allowedDirections pattern.
     *
     * Time: O(n*m) cause we amount of suproblems is n*m and due to memoization we solve each exactly once;
     * Space: O(n*m)
     */
    fun topDownDp(m: Int, n: Int): Int =
        if (m != 1 && n != 1) {
            topDownDpInternal(
                rowInd = m - 1,
                columnInd = n - 1,
                cache = Array(size = m) { IntArray(size = n) { -1 } },
            )
        } else 1

    private fun topDownDpInternal(
        rowInd: Int,
        columnInd: Int,
        cache: Array<IntArray>,
    ): Int {
        if (rowInd + columnInd == 0) return 1 // node (0,0) has no inbound edges

        val cachedResult = cache[rowInd][columnInd]
        if (cachedResult != -1) return cachedResult

        var pathsCount = 0
        topDownDirections.forEach { edge ->
            val newRowInd = rowInd + edge[0]
            val newColumnInd = columnInd + edge[1]
            if (newRowInd < 0 || newColumnInd < 0) return@forEach

            pathsCount += topDownDpInternal(rowInd = newRowInd, columnInd = newColumnInd, cache)
        }
        return pathsCount.also { cache[rowInd][columnInd] = it }
    }

    /**
     * start with the base cases => from (0,0) = 1, same goal, same core principle
     *  - if rowInd > 0 then we could've come to the current cell from the top cell => add to the current cell's pathsCount
     *   the number of paths we could've reached the top cell;
     *  - if columnInd > 0 then we could've come to the current cell from the left cell => add to the current cell's pathsCount
     *   the number of paths we could've reached the left cell.
     * Add directly into the tabulation array, no need for an intermediate variable!
     */
    fun bottomUp(m: Int, n: Int): Int {
        if (m == 1 || n == 1) return 1

        val cache = Array(size = m) { IntArray(size = n) }
        cache[0][0] = 1
        for (rowInd in 0 until m) {
            for (columnInd in 0 until n) {
                if (rowInd > 0) cache[rowInd][columnInd] += cache[rowInd - 1][columnInd]
                if (columnInd > 0) cache[rowInd][columnInd] += cache[rowInd][columnInd - 1]
            }
        }
        return cache[m - 1][n - 1]
    }

    /**
     * to compute the answer for cell (rowInd,columnInd) we actually only need to know 2 previous answers:
     *  for cell (rowInd-1,columnInd) and (rowInd, columnInd-1)
     * => it's enough to keep actually just one IntArray for the cache, just the previous row, and overwrite it on-the-fly
     *  with a new one.
     *
     * Time: O(m*n)
     * Space: O(n), reduced by a factor of m
     */
    fun bottomUpSpaceOptimized(m: Int, n: Int): Int {
        if (m == 1 || n == 1) return 1

        val previousRow = IntArray(size = n)
        previousRow[0] = 1
        for (rowInd in 0 until m) {
            for (columnInd in 0 until n) {
                val topBuffer = previousRow[columnInd]
                if (rowInd + columnInd != 0) previousRow[columnInd] = 0
                if (rowInd > 0) previousRow[columnInd] += topBuffer
                if (columnInd > 0) previousRow[columnInd] += previousRow[columnInd - 1]
            }
        }
        return previousRow[n - 1]
    }
}

fun main() {
    println(UniquePaths().bottomUpSpaceOptimized(m = 2, n = 3))
}
