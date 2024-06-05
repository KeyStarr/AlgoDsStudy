package com.keystarr.algorithm.array.prefixsum

/**
 * LC-2270 https://leetcode.com/problems/number-of-ways-to-split-array/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= nums.length <= 10^5;
 *  • -10^5 <= nums\[i] <= 10^5;
 *  • no explicit time/space.
 *
 * Final notes:
 *  - solved via [timeEfficient] by myself with hints from the DSA course (as an example problem to theory, so no time record);
 *  - failed 1 submission, missed a corner case of sum exceeding Int!! Figured the problem out myself.
 */
class NumberOfWaysToSplitArray {

    /**
     * Tools: Prefix-sum.
     *
     * Problem rephrasing, the split is valid if:
     *  - the split is valid if (sum of first `k` elements) >= (sum of last `k` elements);
     *  - 0 <= i < n-1 (don't count the case when we sum the entire array);
     * =>
     * for i in (0..n-2) compute the formula: sum(nums\[0:i]) >= sum(nums[i+1:n-1]), if true, increment the counter.
     *
     * Edge cases:
     *  - nums.length = 2 => current algo behaves correctly;
     *  - reminder: i != n-1 => accounted for in the loop's index range;
     *  - nums\[i] negative => simple summing is enough;
     *  - prefixSum array must contain Long, cause the largest sum is 10^5*10^5=10^10 which exceeds Int.
     *
     * Time: always O(n)
     * Space: always O(n)
     */
    fun timeEfficient(nums: IntArray): Int {
        val prefixSum = LongArray(nums.size)
        prefixSum[0] = nums[0].toLong()
        for (i in (1 until nums.size)) prefixSum[i] = prefixSum[i - 1] + nums[i]

        var validSplitCount = 0
        for (i in (0 until nums.size - 1)) {
            val sumOfFirst = prefixSum[i]
            val sumOfLast = prefixSum[nums.size - 1] - prefixSum[i] // sum of nums[i+1:]
            if (sumOfFirst >= sumOfLast) validSplitCount++
        }

        return validSplitCount
    }

    fun efficient(nums: IntArray): Int {
        TODO("There is a solution with space O(1), not so important rn - revisit and optimize later")
    }
}

fun main() {
    println(
        NumberOfWaysToSplitArray().timeEfficient(
            nums = intArrayOf(10, 4, -8, 7),
        )
    )
}
