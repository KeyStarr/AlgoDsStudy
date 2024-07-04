package com.keystarr.algorithm.search.binarysearch

/**
 * LC-74 https://leetcode.com/problems/search-a-2d-matrix/
 * difficulty: medium
 * constraints:
 *  • m == matrix.length
 *  • n == matrix\[i].length
 *  • 1 <= m, n <= 100
 *  • •10^4 <= matrix\[i]\[j], target <= 10^4
 *
 * Final notes:
 *  • tried to treat left/right/middle as row/column each, but that was too complicated. Couldn't even figure out how to 
 *   calculate the middle row/column correct based on left/right due to rows switching etc;
 *  • used the same idea as in [com.keystarr.algorithm.graph.SnakesAndLadders]: simply treat the 2D array as a flat array,
 *   with all elements from 0 to n*m•1 one after another, save these indices into left/right/middle. BUT CONVERT middle 
 *   to the matrix indices each time we need to access the middle elements itself. Which is super simple and not too costly.
 *
 * Value gained:
 *  • practiced binary search on a 2D array;
 *  • apparently when faced with 2D array index calculation based on other indices, consider simply treating it as a flat 
 *   array for actual index calculations and converting into row/column only where needed.
 */
class SearchA2DMatrix {

    /**
     * the task is to find a number in a sorted collection, as if that wasn't enough hints, in O(log(m*n) time
     *  => use binary search.
     *
     * The trick here is to implement the indices correctly, adapt to the matrix.
     *
     * leftInd = (0,0)
     * rightInd = (m-1,n-1) // m*n matrix, m is the rows amount, n is the columns amount
     *
     * how to compute midInd?
     * midColumnInd = (leftInd[1] + rightInd[1]) / 2
     * midRowInd = (leftInd[0] + leftInd[0]) / 2
     *
     * how to update left/right?
     * target is in left: right = decrease midInd's column by 1, if it is <0 then set it to n-1 and decrease the row by 1
     * target is in right: left = increase midInd's column by 1, if it is >=n then set it to 0 and increase the row by 1
     *
     * what's the termination condition? the goal is to search => so leftInd <= rightInd:
     * left[0] < right[0] || (left[0] == rightInd[0] && left[1] <= right[1])
     *
     * the rest is the classic binary search algorithm.
     *
     * Edge cases:
     *  - max right+left == 100 + 100 = 200, no overflow, ok.
     *
     * Time: we are just updating the indices in the tricky manner, but actually are running the classic binary search
     *  algorithm on the total amount of elements = m*n
     *  => O(log(m*n))
     * Space: O(1) cause we only use indices
     */
    fun efficient(matrix: Array<IntArray>, target: Int): Boolean {
        val rowsAmount = matrix.size
        val columnsAmount = matrix[0].size

        var left = 0
        var right = rowsAmount * columnsAmount - 1
        while (left <= right) {
            val middle = (left + right) / 2
            val middleMatrixInd = middle.toMatrixIndices(columnsAmount)
            val midElement = matrix[middleMatrixInd[0]][middleMatrixInd[1]]
            when {
                target == midElement -> return true
                target < midElement -> right = middle - 1
                else -> left = middle + 1
            }
        }
        return false
    }

    private fun Int.toMatrixIndices(columnsAmount: Int): IntArray = intArrayOf(
        this / columnsAmount,
        this % columnsAmount,
    )
}

fun main() {
    println(
        SearchA2DMatrix().efficient(
            matrix = arrayOf(
                intArrayOf(1, 3, 5, 7),
                intArrayOf(10, 11, 16, 20),
                intArrayOf(23, 30, 34, 60),
            ),
            target = 60,
        )
    )
}
