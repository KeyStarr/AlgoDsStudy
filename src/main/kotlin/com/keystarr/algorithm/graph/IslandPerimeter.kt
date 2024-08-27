package com.keystarr.algorithm.graph

/**
 * ⭐️ a great example of a graph problem with custom traversal, not pure DFS/BFS adapting to the matrix format and requirements
 * LC-463 https://leetcode.com/problems/island-perimeter/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= grid.size, grid[0].size <= 100
 *
 * Final notes:
 *  • ⚠️ done [suboptimal] by myself in 30 mins;
 *  • technically this is a traversal type problem, but the point is that it can be solved without recursion and without seen,
 *   simply by traversing through each node in the order it is present in the grid. Since for each node we might compute
 *   statically via a formula how much it contributes to an answer;
 *  • 🔥 the traversal algorithm is neither DFS nor BFS. While current node has a neighbor to its right in the grid,
 *   we go right, but when it doesn't, we go down at the first node in the current row! In DFS we would have been alternating
 *   row traversal order.
 *
 * Value gained:
 *  • practiced solving a graph problem using a matrix-based traversal without seen;
 *  • ⚠️ should've done much faster? for easy problems timing is 5-15 mins. But I don't see how its that much leet-easy
 *   since it requires a matrix-based graph traversal.
 */
class IslandPerimeter {

    // TODO: retry in 1-2 weeks

    private lateinit var grid: Array<IntArray>
    private lateinit var seen: Array<BooleanArray>

    private val allowedDirections = arrayOf(
        intArrayOf(-1, 0), // top
        intArrayOf(1, 0), // bottom
        intArrayOf(0, 1), // right
        intArrayOf(0, -1), // left
    )

    /**
     * problem rephrase:
     *  - given: a graph represented via a matrix, where grid\[i]\[j]==1 is a node, and direct bounds (left,right,bottom,top)
     *   between nodes are edges;
     *  - there is exactly 1 connected component in the graph, and within it's perimeter there are no grid\[i]\[j] == 0;
     *  - assume outside of grid boundary cells there are 0.
     *  - length of a side of a single cell == 1.
     *  Goal: return the perimeter of the connected component
     *   perimeter = sum of all outer sides of the connected component
     *
     * 0  0  0
     *    _ _
     * 0 |1 1|
     *   - -
     * 0  0  0
     *
     * expected = 6
     * total bounds count = 8
     * we don't count the bound between the two cells both times
     *
     *
     * 0 0 0           0 0 0
     * 0 1 0           1 1 1
     * 1 1 1           1 1 1
     * 0 1 0           1 1 1
     *
     * note: the sum for nodes which are surrounded by other nodes on all sides is always 0, so we need to take into
     *  account only boundary nodes.
     *
     * what types of boundary nodes are there?
     *  1. outward line: like in the 1st example, have 3 side with 0's around them;
     *  2. corners or inner line: like in the 3rd example or like in the original example, have 2 neighbor zeros;
     *  3. bricks (in a wall: like in the 3rd example, have only 1 zero around.
     *
     * note: each note contributes to the answer exactly the amount of its neighboring zeros.
     *
     * Approach:
     *  - iterate through the [grid], if cell isn't 0, launch DFS from it;
     *  dfs:
     *   goal: return the sum of all 0's on sides of all nodes in the connected component (count the perimeter of the connected component)
     *   pre-order: as we iterate through neighbors of the current node, add to its perimeter 1 if the neighbor == 0,
     *    else launch dfs and add its result to the perimeter.
     *
     * edge case:
     *  - if a node neighbor is out of bound of the grid => count it as a 0, add to answer!
     *  - there are no cells == 1 in the grid => always return 0;
     *  - inner cells == 0 (surrounded by all cells == 1) are impossible => count only the outward.
     *   impossible by the problem statement
     *
     * Time: average/worst O(nodes + edges) = O(nodes)
     *  edges ~ nodes*4
     *  worst = all nodes are == 1 => we launch dfs from each and try each edges
     *  best = grid(0,0) == 1 and its the only node == 1 => we just check it, 4 its neighbors and return straight away, O(1)
     * Space: always O(nodes)
     *  - seen takes O(nodes)
     */
    fun suboptimal(grid: Array<IntArray>): Int {
        this.grid = grid
        this.seen = Array(size = grid.size) { BooleanArray(size = grid[0].size) }
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if (grid[row][col] == 0) continue
                seen[row][col] = true
                return dfs(row = row, col = col)
            }
        }
        return 0
    }

    /**
     * Goal: return the perimeter value for all nodes in the connected component starting from [row][col] and not in seen
     *  at the initial time of the call.
     */
    private fun dfs(row: Int, col: Int): Int {
        var perimeter = 0
        allowedDirections.forEach { direction ->
            val newRow = row + direction[0]
            val newCol = col + direction[1]
            val doesCellExist = isValid(row = newRow, col = newCol)
            if (!doesCellExist || grid[newRow][newCol] == 0) perimeter++
            if (doesCellExist && !seen[newRow][newCol] && grid[newRow][newCol] == 1) {
                seen[newRow][newCol] = true
                perimeter += dfs(row = newRow, col = newCol)
            }
        }
        return perimeter
    }

    private fun isValid(row: Int, col: Int) = row in grid.indices && col in grid[0].indices

    /**
     * Core idea same as [suboptimal], but note that for each node the amount it contributes to answer is static,
     *  the traversal order does not matter, the formula is always the same => simply go through the entire grid left to right
     *  from (0,0) and current node's metric to the answer.
     *
     * Time: always O(nodes + edges) = O(nodes)
     *  edges ~ nodes*4
     * Space: always O(1)
     *
     * => change from [suboptimal]: improve space from O(n) to O(1), but loose the best case of O(1), a good trade generally.
     *
     * ----
     *
     * Learned the core concept from the discussion session, done the rest myself.
     */
    fun efficient(grid: Array<IntArray>): Int {
        var perimeter = 0
        for (row in grid.indices) {
            for (col in grid[0].indices) {
                if (grid[row][col] == 0) continue
                allowedDirections.forEach { direction ->
                    val neighborRow = row + direction[0]
                    val neighborCol = col + direction[1]
                    if (!grid.isValid(neighborRow, neighborCol) || grid[neighborRow][neighborCol] == 0) perimeter++
                }
            }
        }
        return perimeter
    }

    private fun Array<IntArray>.isValid(row: Int, col: Int) = row in indices && col in get(0).indices
}

fun main() {
    println(
        IslandPerimeter().suboptimal(
            grid = arrayOf(
                intArrayOf(0, 0, 0),
                intArrayOf(0, 1, 1),
                intArrayOf(0, 0, 0),
            )
        )
    )
}
