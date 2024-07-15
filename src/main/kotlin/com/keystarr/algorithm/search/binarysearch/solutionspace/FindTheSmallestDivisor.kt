package com.keystarr.algorithm.search.binarysearch.solutionspace

import kotlin.math.ceil

/**
 * LC-1283 https://leetcode.com/problems/find-the-smallest-divisor-given-a-threshold/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.length <= 5*10^4
 *  • 1 <= nums\[i] <= 10^6
 *  • nums.length <= threshold <= 10^6
 *
 * Final notes:
 *  • cool how the binary search on solution spaces here matched 1to1.
 *
 * Value gained:
 *  • practiced proving the applicability and implementing binary search on solution spaces pattern.
 */
class FindTheSmallestDivisor {

    /**
     * problem rephrase:
     *  - given nums and threshold
     * goal: find the minimum integer `divisor` such that sum(nums/threshold) <= threshold
     *  (division must be ceil)
     *
     * A. the problem asks for minimum in the solution space with:
     *  low boundary = 1;
     *  high boundary = nums.max(), cause we do ceil division and look for min divisor, and any value above that would
     *   always give the same division sum, since we'd round each division result to 1.
     * B. if the value X is a valid answer, then all the values above X are valid answers too (since the division sum
     *  would only be equal or less)
     *    if the value Y is an invalid answer, then all the values below Y are invalid, since the division sum would only
     *    be equal or greater
     * => use binary search on solution spaces
     *
     * Idea:
     *  - implement the standard binary search "find" pattern, with initially:
     *   - left = 1
     *   - right = numbers.max()
     *  - check function is: ceil divide all nums by answerCandidate and return bool: the sum of these results <= threshold.
     *
     * Edge cases:
     *  - max sum is 10^4*10^6 and the divisor is 1 => 10^10, exceeds Int, use long for sum;
     *  - a valid answer always exists (cause min threshold=numbers.size => divisors equal to or above numbers.max() are
     *   the all valid answers).
     *
     * Time: O(n*logk)
     *  - check function is always O(n) time
     *  - there are average/worst O(logk) iterations, where k=numbers.max()
     * Space: O(1)
     */
    fun efficient(numbers: IntArray, threshold: Int): Int {
        var left = 1
        var right = numbers.maxOf { it }
        while (left <= right) {
            val mid = left + (right - left) / 2
            if (numbers.isValidDivisor(divisor = mid.toDouble(), threshold = threshold)) {
                right = mid - 1
            } else {
                left = mid + 1
            }
        }
        return left
    }

    private fun IntArray.isValidDivisor(divisor: Double, threshold: Int): Boolean {
        var sum = 0.0
        forEach { number -> sum += ceil(number / divisor).toLong() }
        return sum <= threshold
    }
}
