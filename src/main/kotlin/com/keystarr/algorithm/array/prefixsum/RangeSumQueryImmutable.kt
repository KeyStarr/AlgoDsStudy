package com.keystarr.algorithm.array.prefixsum

/**
 * LC-303 https://leetcode.com/problems/range-sum-query-immutable/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.size <= 10^4;
 *  • -10^5 <= nums\[i] <= 10^5;
 *  • 0 <= left <= right < nums.size;
 *  • at most 10^4 calls to [sumRange].
 *
 *
 * Goal: given an int array implement a DS with a single method to compute the sum of any subarray of it
 *  => just use prefix sums.
 *
 * Constructor then taken O(n) time and O(n) space. But can be hoisted to the first call to [sumRange] via lazy init if needs be.
 *
 * Final notes:
 *  • done by myself in 6 mins;
 *  • failed a bunch of test runs due to silly mistakes, since I got excited that the solution is so simple and just
 *   went to do it asap relaxing the attention way too much.
 *
 * Value gained:
 *  • practiced solving a "design a DS" type problem using the pure init-time prefix sum.
 */
class RangeSumQueryImmutable(private val nums: IntArray) {

    private val prefixSums = IntArray(size = nums.size).apply {
        this[0] = nums[0]
        for (i in 1..nums.lastIndex) this[i] = this[i - 1] + nums[i]
    }

    /**
     * Time: always O(1)
     * Space: always O(1)
     */
    fun sumRange(left: Int, right: Int) = prefixSums[right] - prefixSums[left] + nums[left]
}
