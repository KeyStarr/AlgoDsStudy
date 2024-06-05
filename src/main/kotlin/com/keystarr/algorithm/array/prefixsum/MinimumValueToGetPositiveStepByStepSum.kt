package com.keystarr.algorithm.array.prefixsum

/**
 * LC-1413 https://leetcode.com/problems/minimum-value-to-get-positive-step-by-step-sum/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.length <= 100;
 *  • -100 <= nums\[i] <= 100;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • implemented [efficient] by myself in 20 mins, but not [efficientCleaner];
 *  • for 10-15 minutes I was confused trying to understand EXACTLY what problem is about!
 */
class MinimumValueToGetPositiveStepByStepSum {

    /**
     * Tools: Prefix Sum.
     *
     * Intuition - find `startValue` such that each prefixSum\[i] + startValue >= 1, but at least `startValue` = 1.
     *
     * Problem rephrasing (startValue=outsideNumber for clarity):
     *  sum = 0
     *  sum += outsideNumber + nums[0] >= 1
     *  sum += outsideNumber + nums[1] >= 1
     *  ..
     *  sum += outsideNumber + nums[nums.size - 1] >= 1
     *
     * Goal - find such an outsideNumber that for each iteration of calculating its sum with nums elements the intermediate
     * sum is >= 1.
     *
     * Suppose we have prefixSum array pre-computed, then
     * goal - find the minimum value in that array and return if (minSum >= 1) 1 else 1 - minSum.
     *
     * Edge cases:
     *  - minSum=0 => 1 - 0 => 1, correct;
     *  - minSum>=1 => always 1, cause minimum POSITIVE.
     *
     * Time: always O(n)
     * Space: always O(1) if we reuse input array as output (if not, O(n))
     */
    fun efficient(nums: IntArray): Int {
        for (i in 1 until nums.size) nums[i] = nums[i - 1] + nums[i]

        var minSum = nums[0]
        nums.forEach { number -> if (number < minSum) minSum = number }

        return if (minSum >= 1) 1 else 1 - minSum
    }

    /**
     * Core idea and space/time complexity same as [efficient], but less logic and make n iterations instead of 2n.
     *
     * Discovered thanks to https://leetcode.com/problems/minimum-value-to-get-positive-step-by-step-sum/submissions/1259589351/
     */
    fun efficientCleaner(nums: IntArray): Int {
        var totalSum = 0
        var minSum = Int.MAX_VALUE
        nums.forEach { number ->
            totalSum += number
            if (totalSum < minSum) minSum = totalSum
        }
        return if (minSum >= 1) 1 else 1 - minSum // TODO: why does editorial have just "-minVal + 1;"? Revisit and figure out.
    }
}

fun main() {
    println(
        MinimumValueToGetPositiveStepByStepSum().efficient(
            nums = intArrayOf(-3, 2, -3, 4, 2)
        )
    )
}
