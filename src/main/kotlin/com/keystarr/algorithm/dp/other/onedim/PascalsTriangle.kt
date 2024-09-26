package com.keystarr.algorithm.dp.other.onedim

/**
 * ⭐️ a unique obvious-solution "don't even have to know DP" type DP problem
 * LC-118 https://leetcode.com/problems/pascals-triangle/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= targetRows <= 30.
 *
 * Final notes:
 *  • done [bottomUp] by myself in 10 mins;
 *  • didn't realize the [bottomUp] was DP until read it in solutions. Indeed, I jumped straight to bottom-up DP,
 *   we start at base case row=1 being just listOf(1), and then increase the subproblem by adding one more element, and computing
 *   each element of that row using the solutions to 1-2 subproblems of the previous row! Crystal clear bottom-up DP, where
 *   the goal is to compute the pascal's triangle for a given row X. (base case is X=1, X=2 uses the solution to X=1, X=3 for X=2 etc);
 *  • very much an intro DP problem only a step above [NthTribonacciNumber] with an intuitive bottom-up solution;
 *  •
 *
 * Value gained:
 *  • practiced solving a special 2D list generation type problem using bottom-up DP (without even realizing it was DP :D
 *   just following the implementation formula, since it was clearly laid out).
 */
class PascalsTriangle {

    /**
     * 1
     * 1 1
     * 1 2 1
     * 1 3 3 1
     *
     * "directly above" in terms of list of lists is:
     *  triangle\[row]\[col] = triangle\[row-1][col-1] + triangle\[row-1]\[col]
     *
     * trivial: generate numbers one by one.
     *
     * edge cases:
     *  - targetRows == 1 => don't enter the loop, return triangle, correct.
     *
     * Time: always O(targetRows^2)
     *  - outer loop has targetRows-1 iterations;
     *  - inner loop has worst targetRows iterations, but on average targetRows/2 iterations.
     * Space: always O(targetRows^2)
     *  - rows list size is always targetRows;
     *  - columns size is worst targetRows, average targetRows/2.
     */
    fun bottomUp(targetRows: Int): List<List<Int>> {
        val triangle = mutableListOf(listOf(1))
        for (row in 1 until targetRows) {
            val newRow = mutableListOf<Int>()
            val prevRow = triangle[row - 1]
            val targetColumns = row + 1
            repeat(targetColumns) { col ->
                val prevCol = col - 1
                newRow.add(
                    (if (prevCol >= 0) prevRow[prevCol] else 0) + (if (col < prevRow.size) prevRow[col] else 0)
                )
            }
            triangle.add(newRow)
        }
        return triangle
    }
}
