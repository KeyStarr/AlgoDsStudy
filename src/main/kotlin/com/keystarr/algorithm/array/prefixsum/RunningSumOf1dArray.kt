package com.keystarr.algorithm.array.prefixsum

/**
 * LC-1480 https://leetcode.com/problems/running-sum-of-1d-array/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.length <= 10^3;
 *  • -10^6 <= nums\[i] <= 10^6;
 *  • no explicit time/space.
 *
 *  Final notes: done [timeEfficient] by myself in 5 mins, pure prefix sum pattern; but not the most efficient [efficient].
 */
class RunningSumOf1dArray {

    /**
     * Tools: Prefix Sum.
     *
     * Edge cases:
     *  - greatest sum possible is 10^6*10^3 = 10^9 fits into int.
     *
     * Time: always O(n)
     * Space: always O(n)
     */
    fun timeEfficient(nums: IntArray): IntArray {
        val prefixSum = IntArray(size = nums.size)
        prefixSum[0] = nums[0]
        for (i in 1 until nums.size) prefixSum[i] = prefixSum[i - 1] + nums[i]
        return prefixSum
    }

    /**
     * Same as [efficient] but use the input array as output.
     *
     * Time: always O(n)
     * Space: always O(1)
     *
     * Discovered thanks to https://leetcode.com/problems/running-sum-of-1d-array/
     */
    fun efficient(nums: IntArray): IntArray = nums.apply {
        for (i in 1 until nums.size) nums[i] = nums[i - 1] + nums[i]
    }
}

fun main() {
    println(
        RunningSumOf1dArray().timeEfficient(
            nums = intArrayOf(3, 100, 20),
        )
    )
}
