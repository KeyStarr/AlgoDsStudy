package com.keystarr.algorithm.search.binarysearch

/**
 * LC-374: https://leetcode.com/problems/guess-number-higher-or-lower/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= maxNumber <= 2^31 - 1;
 *  • 1 <= guess <= maxNumber.
 *
 * Final notes:
 *  • done [efficient] by myself in 5 mins;
 *  • ⚠️ felt unsteady over the boundary conditions, whether to do left <= right or left < right.
 *   good enough for now, dry ran a bit and proved by a single case at least that with left==right we still must check
 *   the middle for target => go next, got bigger priorities.
 *
 * Value gained:
 *  • practiced binary search on a pure binary search simulation type problem.
 */
class GuessNumberHigherOrLower(private val answer: Int) {

    /**
     * search for the number in the predefined solution space (1 to n) + eliminate half based on a guess
     * => binary search
     *
     * Time: average/worst O(n)
     * Space: always O(1)
     *
     * --------------
     *
     * solution space here looks like:
     *  ..--X++.., where X=target number which always exists
     *
     *
     * maxNumber=10, target=1
     *
     * left=1  right=1
     * middle = 1
     * comparisonResult == 0 => return 1.
     */
    fun efficient(maxNumber: Int): Int {
        var left = 1
        var right = maxNumber
        while (left <= right) {
            val middle = left + (right - left) / 2
            val comparisonResult = guess(middle)
            if (comparisonResult == 0) return middle
            if (comparisonResult > 0) left = middle + 1 else right = middle - 1
        }
        throw IllegalStateException("must not happen, answer is guaranteed to exist")
    }


    /**
     * pre-defined in leet environment = simulation for testing/understanding
     */
    private fun guess(guess: Int): Int = answer - guess
}
