package com.keystarr.algorithm.array.prefixsum

/**
 * LC-724 https://leetcode.com/problems/find-pivot-index/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.size <= 10^4;
 *  • -10^3 <= nums\[i] < 10^3.
 *
 * Final notes:
 *  • solved via [suboptimal] in ~10 mins by myself.
 *
 * Value gained:
 *  • practiced recognizing and solving the "2 best valid subarrays" type problem using a customized efficient prefix sum.
 */
class FindPivotIndex {

    /**
     * Note that we have to go through the nums anyway to compare the left/right sums => we might only compute the
     * full nums sum and do a running sum of the current left sum
     *
     * Time: always O(n)
     * Space: always O(1)
     *
     * ---
     *
     * Discovered thanks to one of the top bar time solutions. Settled for [suboptimal], seen no reason to optimize further
     *  before checking the solution.
     */
    fun efficient(nums: IntArray): Int {
        val fullSum = nums.sum()
        var leftSum = 0
        nums.forEachIndexed { ind, number ->
            val rightSum = fullSum - leftSum - number
            if (leftSum == rightSum) return ind
            leftSum += number
        }
        return -1
    }

    /**
     * since we find basically the best valid split of the array into 2 subarrays (excluding one element) sums
     *  => try prefix sum
     *
     * 1. compute prefix sum;
     * 2. iterate through the array, for each index compute the sums of the left and right subarrays in O(1)
     *  => return the first index when both sums are equal (since we need the leftmost)
     *
     * if we haven't returned and terminated => return -1, no such answer exists.
     *
     *
     * edge cases:
     *  - sum => max sum = 10^4 * 10^3=10^7 => fits into int.
     *
     */
    fun suboptimal(nums: IntArray): Int {
        val prefixSums = IntArray(nums.size + 1).apply { set(1, nums[0]) }
        for (i in 1..nums.lastIndex) prefixSums[i + 1] = prefixSums[i] + nums[i]

        for (i in nums.indices) {
            val leftSum = prefixSums[i]
            val rightSum = prefixSums.last() - prefixSums[i + 1]
            if (leftSum == rightSum) return i
        }
        return -1
    }
}
