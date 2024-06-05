package com.keystarr.algorithm.array.slidingwindow

/**
 * LC-643 https://leetcode.com/explore/interview/card/leetcodes-interview-crash-course-data-structures-and-algorithms/703/arraystrings/4594/
 * difficulty: easy
 * constraints:
 *  • 1 <= k <= nums.length <= 10^5;
 *  • -10^4 <= nums\[i] <= 10^4;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • solved via [efficient] in 20 minutes;
 *  • since there are no explicit time/space constraints, O(n*k) solution would also do, but decided to practice O(n).
 */
class MaximumAverageSubarrayI {

    /**
     * Tools: Fixed Sliding Window.
     *
     * Useful observations:
     *  - if the sum of subarray X > sum of Y, then always average of X > average of Y;
     *  - if we'd count the sum of the current subarray every rightInd update => time complexity would be O(nums.size*validLength),
     *      instead, as is customary for sliding window problems, keep track of current sum and update it by only elements which change.
     *
     * Edge cases:
     *  - validLength == nums.size => return the average of the entire nums;
     *  - validLength = 1 => return the max number in nums;
     *  - nums.size == 1 => validLength can only be 1 => return nums[0];
     *  - nums has negative numbers => basic sum / average calculation already accounts for that.
     *
     * Time: always O(n);
     * Space: always O(1).
     */
    fun efficient(nums: IntArray, validLength: Int): Double {
        var currentSum = 0
        for (i in 0 until validLength) currentSum += nums[i]

        var maxSum = currentSum
        for (rightInd in (validLength until nums.size)) {
            val leftInd = rightInd - validLength + 1
            currentSum = currentSum - nums[leftInd - 1] + nums[rightInd]
            if (currentSum > maxSum) maxSum = currentSum
        }

        return maxSum / validLength.toDouble()
    }
}

fun main() {
    println(
        MaximumAverageSubarrayI().efficient(
            nums = intArrayOf(1, 12, -5, -6, 50, 3),
            validLength = 4,
        )
    )
}
