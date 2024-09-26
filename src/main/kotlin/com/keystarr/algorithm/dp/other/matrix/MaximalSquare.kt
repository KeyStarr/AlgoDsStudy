package com.keystarr.algorithm.dp.other.matrix

import kotlin.math.max
import kotlin.math.min

/**
 * ⭐️ a stark example of a hidden DP problem + what a magnificent core observation here to make about recursive square composition rule
 * LC-221 https://leetcode.com/problems/maximal-square/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= matrix.size, matrix[0].size <= 300;
 *  • matrix\[i]\[j] is either '0' or '1'.
 *
 * Final notes:
 *  • ⚠️⚠️ failed [wrongDfs] in 1h10m, designed based in on wrong observation => fail submit. Actually there are cases when there might
 *   be 1's which I formulated as "candidates for squares" but which don't all form a square. E.g. get two square, one bigger
 *   one smaller, and merge the smaller one into the bigger one up to 1/2 deep into one side. We'd not filter any 1's of the
 *   2nd square, coz well, they form a square, but then we'd say both squares form a huge square of combined dimensions, which they don't;
 *  • 💡 yet again, if one bases the design on correct core observations => its be a swim, like [topDownDp]. But if the
 *   observation are incorrect or lead away from the clean efficient solution, don't utilize the properties required for it
 *   => its gonna be a nightmare.
 *   => 🔥 observation and induction skills rule! How do I master these? Is this topic based? How to generalize, what are
 *    the useful models/focus points here?
 *  • learned the core observation for DP about the recursive square composition from https://www.youtube.com/watch?v=6X7Ha2PrDmM
 *   and designed/implemented the rest in all [topDownDp], [bottomUpDp], [bottomUpSpaceOptimizedDp].
 *   🏅💡I really just needed that critical observation to make it work, I had the understanding of the DP framework for the rest.
 *
 * Value gained:
 *  • practiced DP for an efficient solution on a hidden (kinda) DP problem, practiced going from the top-down DP to bottom-up space-optimized;
 *  • practiced noticing/understanding the core observation on a problem with, really, 1 critical observation to make.
 *   💡 how awesome this is that, if one mastered DP on a decent level, the solution is just about making that observation.
 *    And it can be inferenced here really, live, simply start reasoning from the minimal square possible, then increase
 *    dimension by one, then again by one and find the pattern!!! Which I failed to do for some reason, I think I lacked
 *    confidence, a belief that I CAN find it (plus I've tried this problem initially very tired at 3 am in the morning,
 *    still recovering from the sickness, sooo).
 */
class MaximalSquare {

    // TODO: failed to recognize DP, retry in 1-2 weeks

    private val allowedDirections = arrayOf(
        intArrayOf(0, 1), // right
        intArrayOf(1, 0), // bottom
        intArrayOf(1, 1), // bottom-right
    )

    private lateinit var matrix: Array<CharArray>
    private lateinit var cache: Array<IntArray>

    /**
     * Observation:
     *  1. in order to make a 2x2 square with cell X at its top-left corner:
     *   - X must be == 1;
     *   - its neighbors right, bottom right and bottom must be == 1.
     *  2. in order to make a 3x3 square with same X cell:
     *   - its right, bottom right and bottom neighbors must themselves be the top-left corner of valid 2x2 squares.
     *  3. in order to make a 4x4 square at same X:
     *   - these very same neighbors, each must make a valid 3x3 square.
     *  4. if, say, two neighbors make at most a 3x3 square, and one neighbor makes only 2x2 square, then we're bounded
     *   by it and can only make a 3x3 square at cell X, using only 2x2 squares from other two neighbors.
     *   Another example - if one neighbors makes 0 square, i.o. itself == 0, then the largest square we can make at
     *   X is 1x2, X itself.
     *
     * if dp returns the side of the largest square we can make at cell X=(row,col) Int, then:
     * => maxSquare(row, col) = min(maxSquare(row+1, col), maxSquare(row+1,col+1), dp(row,col+1))
     *
     * Observation:
     *  5. node X and its right neighbor both share X's bottom-right node as their neighbors => we'd compute the equation
     *   at least twice. Even X is no the top-left corner in matrix => even more, cause for all top-left nodes which they are
     *   connected by 1s to X, we'd re-compute X etc;
     *   => subproblems overlap AND are clean to compute, don't depend on any previous state
     *
     * => DP, top-down:
     * 1. goal: return the side of the largest square we can make at cell X=(row,col), return type Int
     * 2. input state: rowInd, colInd of the top-left corner of the potential square;
     * 3. recurrence equation:
     *  dp(row, col) = min(dp(row+1, col), dp(row+1,col+1), dp(row,col+1))
     * 4. base cases:
     *  - either row and/or col are out of bounds;
     *  - matrix\[row]\[col] == 0;
     * 5. memoization: Array<IntArray>, where for every top-left corner of the potential square we store its int max square side.
     *
     * Edge cases:
     *  -
     *
     * Time: always O(n*m), we check each cell and for each we compute the answer exactly once;
     * Space: O(n*m) if we allocate the new array for memoization, but we could also modify the original array in which case its O(1).
     *
     * ------------
     * Learned thanks to https://www.youtube.com/watch?v=6X7Ha2PrDmM
     */
    fun topDownDp(matrix: Array<CharArray>): Int {
        this.matrix = matrix
        this.cache = Array(size = matrix.size) { IntArray(size = matrix[0].size) { -1 } }
        var maxSquareSide = 0
        for (row in matrix.indices) {
            for (col in matrix[0].indices) {
                val localMaxSquareSide = dp(row = row, col = col)
                if (localMaxSquareSide > maxSquareSide) maxSquareSide = localMaxSquareSide
            }
        }
        return maxSquareSide * maxSquareSide
    }

    private fun dp(row: Int, col: Int): Int {
        if ((row !in matrix.indices || col !in matrix[0].indices)) return 0

        val cachedResult = cache[row][col]
        if (cachedResult != -1) return cachedResult

        if (matrix[row][col] == '0') {
            cache[row][col] = 0
            return 0
        }

        val maxSquareSide = min(dp(row + 1, col), min(dp(row + 1, col + 1), dp(row, col + 1)))
        return (maxSquareSide + 1).also { result -> cache[row][col] = result }
    }

    /**
     * Start at the base cases. No need to start out of bounds, may simply start at the bottom-right matrix corner,
     * since it being the top-left matrix corner will always form a valid square with at most 1 side size.
     *
     * Same complexities as [topDownDp], same space optimization idea for rewriting [matrix].
     *
     */
    fun bottomUpDp(matrix: Array<CharArray>): Int {
        val m = matrix.size
        val n = matrix[0].size

        val cache = Array(size = m) { IntArray(size = n) }
        for (row in m - 1 downTo 0) {
            for (col in n - 1 downTo 0) {
                if (matrix[row][col] == '0') {
                    cache[row][col] = 0
                    continue
                }

                var minNeighborMaxSquareSide = Int.MAX_VALUE
                allowedDirections.forEach { direction ->
                    val newRow = row + direction[0]
                    val newCol = col + direction[1]
                    if (!matrix.isValid(newRow, newCol)) {
                        minNeighborMaxSquareSide = 0
                        return@forEach
                    }

                    minNeighborMaxSquareSide = min(cache[newRow][newCol], minNeighborMaxSquareSide)
                }
                cache[row][col] = minNeighborMaxSquareSide + 1
            }
        }

        val maxSquareSide = cache.maxOf { row -> row.maxOf { it } }
        return maxSquareSide * maxSquareSide
    }

    /**
     * Without rewriting [matrix] we could improve the space by only storing the result of the previous and the current row.
     * 2 rows cause we need both the bottom-right and right elements to compute the current: same col, different rows.
     * => Space: O(m) instead of (n*m)
     * ---
     * Could even use just a variable for prevBottomRight instead of the second row, but 2 rows is cleaner code with [allowedDirections].
     */
    fun bottomUpSpaceOptimizedDp(matrix: Array<CharArray>): Int {
        val m = matrix.size
        val n = matrix[0].size

        val cachedRows = Array(size = 2) { IntArray(size = n) }
        var totalMaxSquareSide = 0
        for (row in m - 1 downTo 0) {
            for (col in n - 1 downTo 0) {
                if (matrix[row][col] == '0') {
                    cachedRows[row % 2][col] = 0
                    continue
                }

                var minNeighborMaxSquareSide = Int.MAX_VALUE
                allowedDirections.forEach { direction ->
                    val newRow = row + direction[0]
                    val newCol = col + direction[1]
                    minNeighborMaxSquareSide = if (matrix.isValid(newRow, newCol)) {
                        min(cachedRows[newRow % 2][newCol], minNeighborMaxSquareSide)
                    } else 0
                }
                val maxSquareSide = minNeighborMaxSquareSide + 1
                cachedRows[row % 2][col] = maxSquareSide
                totalMaxSquareSide = max(maxSquareSide, totalMaxSquareSide)
            }
        }

        return totalMaxSquareSide * totalMaxSquareSide
    }

    private fun Array<CharArray>.isValid(row: Int, col: Int): Boolean = row in indices && col in get(0).indices


    /* WRONG initial APPROACH */


    private val verticalDirections = arrayOf(
        intArrayOf(-1, 0), // top
        intArrayOf(1, 0), // bottom
    )
    private val horizontalDirections = arrayOf(
        intArrayOf(0, -1), // left
        intArrayOf(0, 1), // right
    )

    private val rightDownDirections = arrayOf(
        intArrayOf(0, 1), // right
        intArrayOf(1, 0), // bottom
    )


    /**
     *
     * what is a square?
     *  e.g.
     *   2x2
     *   1,3 1,4
     *   2,3 2,4
     *
     *   3x3
     *   1,3 1,4 1,5
     *   2,3 2,4 2,5
     *   3,3 3,4 3,5
     *
     *  => a collection of nodes where there is the same amount of each present rowInd/columnInd and all coordinates
     *  form a connected component
     *
     * -----------
     *
     * a graph?
     *
     * a frequency counting problem?
     *
     * for each connected components in the graph:
     *  - traverse, add all column indices into a frequency IntArray index=number->frequency, do same with rows;
     *   rows = 1: 3, 2: 3, 3: 3
     *   cols = 3: 3, 4: 3, 5: 3
     *  -
     *
     * it doesn't make sense to traverse 0's, coz 1's are not always squares even if these are together, might be rectangles.
     *
     *
     * rows =
     *  0: 2
     *  1: 4
     *  2: 5
     *  3: 2
     *
     * columns =
     * 0: 4
     * 1: 1
     * 2: 3
     * 3: 3
     * 4: 2
     *
     * ----------------------
     *
     * what 1's are never a part of a square? (with area >1)
     *  1. if on 3 sides of the cell==1 there are 0's or boundary
     *  2. if 2 sides == 1, but they are opposite
     *
     * so, the cell==1 is only a candidate for a square if it:
     *  - has more than one neighbor==1;
     *  - if two neighbors==1, these must be not on opposite sides;
     *  - all of its neighbors are also valid candidates for a square.
     *
     * idea:
     *  1. "iron out the 1's": traverse through the graph and flip to 0 / mark as not candidates all nodes that:
     *   - exactly 4 sides 0/boundary;
     *   - exactly on 3 sides have 0/boundary;
     *   - exactly 2 sides == 1, but opposite sides.
     *   => we should be left then with only connected components of the graph of rectangular shape which contain only 1's
     *  2. traverse through the graph again, this time for every cell that == 1 and is not seen launch the dfs:
     *   - dfs goal = return the width and the height of the connected component;
     *   - the possible square's area will be min(height,width)*min(height,width).
     *  3. return the max square area across all such connected components.
     *
     * edge cases:
     *  - there is no connected component of area > 1 || there are no 1's in the graph => if we ever see at least one 1, mark it, it's possible that after
     *   phase 1 there will be no 1's left. Then return either 1 if we've seen it or 0 if not.
     *
     * Time: always O(n*m)
     *  - first pass we check O(n*m) cells;
     *  - second pass, worst is the entire graph consists of 1's => we check worst O(n*m) cells, but each exactly once.
     * Space: always O(1)
     */
    fun wrongDfs(matrix: Array<CharArray>): Int {
        val m = matrix.size
        val n = matrix[0].size

        var seenAtLeastOne = false
        for (row in 0 until m) {
            for (column in 0 until n) {
                if (matrix[row][column] == '1') {
                    seenAtLeastOne = true
                    var verticalNeighbors = 0
                    verticalDirections.forEach { direction ->
                        val neighborRow = row + direction[0]
                        val neighborColumn = column + direction[1]
                        if ((neighborRow in matrix.indices && neighborColumn in matrix[0].indices)
                            && matrix[neighborRow][neighborColumn] == '1'
                        ) verticalNeighbors++
                    }

                    var horizontalNeighbors = 0
                    horizontalDirections.forEach { direction ->
                        val neighborRow = row + direction[0]
                        val neighborColumn = column + direction[1]
                        if ((neighborRow in matrix.indices && neighborColumn in matrix[0].indices)
                            && matrix[neighborRow][neighborColumn] == '1'
                        ) horizontalNeighbors++
                    }

                    val neighbors = horizontalNeighbors + verticalNeighbors
                    if (neighbors == 0 || neighbors == 1 || (neighbors == 2 && horizontalNeighbors != 1)) {
                        matrix[row][column] = '0'
                    }
                }
            }
        }
        if (!seenAtLeastOne) return 0

        matrix.forEach {
            println(it.contentToString())
        }

        val seen = Array(size = m) { BooleanArray(size = n) }
        var maxSquareSide = 1
        for (row in 0 until m) {
            for (column in 0 until n) {
                if (matrix[row][column] == '1') {
                    val bottomRightNode = matrix.dfsMeasureRectangle(row, column, seen)
                    val minSide = min(bottomRightNode.first - row + 1, bottomRightNode.second - column + 1)
                    if (minSide > maxSquareSide) maxSquareSide = minSide
                }
            }
        }
        return maxSquareSide * maxSquareSide
    }

    /** goal - find the bottom-right most node coords reachable from [rowInd] [columnInd] node.
     */
    private fun Array<CharArray>.dfsMeasureRectangle(
        rowInd: Int,
        columnInd: Int,
        seen: Array<BooleanArray>,
    ): Pair<Int, Int> {
        var result = rowInd to columnInd
        rightDownDirections.forEach { direction ->
            val nextRow = rowInd + direction[0]
            val nextColumn = columnInd + direction[1]
            if ((nextRow in indices && nextColumn in get(0).indices)
                && get(nextRow)[nextColumn] == '1'
                && !seen[nextRow][nextColumn]
            ) {
                seen[nextRow][nextColumn] = true
                val tempResult = dfsMeasureRectangle(nextRow, nextColumn, seen)
                if (tempResult.first - result.first + tempResult.second - result.second > 0) result = tempResult
            }
        }
        return result
    }
}


fun main() {
    println(
        com.keystarr.algorithm.dp.other.matrix.MaximalSquare().bottomUpSpaceOptimizedDp(
            arrayOf(charArrayOf('1', '1'))
//            arrayOf(
//                charArrayOf('1', '0', '1', '0', '0'),
//                charArrayOf('1', '0', '1', '1', '1'),
//                charArrayOf('1', '1', '1', '1', '1'),
//                charArrayOf('1', '0', '0', '1', '0'),
//            )
//            arrayOf(
//                charArrayOf('1', '1', '1', '1', '0'),
//                charArrayOf('1', '1', '1', '1', '0'),
//                charArrayOf('1', '1', '1', '1', '1'),
//                charArrayOf('1', '1', '1', '1', '1'),
//                charArrayOf('0', '0', '1', '1', '1')
//            )
        )
    )
}
