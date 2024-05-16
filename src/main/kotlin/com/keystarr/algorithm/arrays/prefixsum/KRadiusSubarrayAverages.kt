package com.keystarr.algorithm.arrays.prefixsum

/**
 * LC-2090 https://leetcode.com/problems/k-radius-subarray-averages/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.size <= 10^5;
 *  • 0 <= nums\[i] <= 10^5;
 *  • 0 <= k <= 10^5.
 *
 * Final notes:
 *  - done [subEfficientPrefixSum] by myself in 25 mins, accepted 1st try;
 *  - averages of subarrays => a glaring hint at prefixSum;
 *  - subarrays of always k*2+1 => an obscured hint at sliding window!;
 *  - the real trick of the problem: edge cases.
 */
class KRadiusSubarrayAverages {

    /**
     * Tools: Prefix Sum.
     *
     * [3, 2, 7, 9, 5]
     * k = 2
     *
     * i = 2
     * i - k = 2 - 2 = 0
     * i + k = 2 + 2 = 4
     * averages[2] = sum of the entire array / (k*2+1)
     *
     * Idea:
     *  - pre-compute prefixSum array;
     *  - iterate from i = k until nums.size - k + 1:
     *      - compute the sum of the subarray nums[i-k:i+k] via prefixSum by O(1);
     *      - compute average sum/k, write into averages[i].
     *  - fill nums[:k-1] and nums[nums.size-k+1:] fill -1.
     *
     * Edge cases:
     *  - k=0 => all averages\[i] = nums\[i], works correctly;
     *  - k=nums.size/2 => only averages[nums.size/2] is computed, the rest are -1;
     *  - k>nums.size/2 => averages is all -1;
     *  - the greatest average may be k = 10^5 all nums\[i] = 10^5 and nums.size=10^5 => (10^5 * 10^5)/10^5 = 10^5 fits into int;
     *  - the greatest subarray sum may be 10^5 * 10^5 = 10^10 => prefixSum array must be Long.
     *
     * Time: average/worst O(n);
     * Space: average/worst always O(n).
     */
    fun subEfficientPrefixSum(nums: IntArray, radius: Int): IntArray {
        if (radius == 0) return nums

        val prefixSum = LongArray(nums.size)
        prefixSum[0] = nums[0].toLong()
        for (i in 1 until nums.size) prefixSum[i] = prefixSum[i - 1] + nums[i]

        val averages = IntArray(nums.size) { -1 }
        for (i in radius until nums.size - radius) {
            val rightInd = i - radius
            val sum = prefixSum[i + radius] - prefixSum[rightInd] + nums[rightInd]
            averages[i] = (sum / (radius * 2 + 1)).toInt()
        }

        return averages
    }

    /**
     * Tools: sliding window.
     *
     * Idea:
     *  - initialize 2 pointers: leftInd=k; rightInd=radius*2+1;
     *  - initialize output array with -1 elements;
     *  - iterate through i=[k:n-k], on each iteration:
     *      - update currentSum according to new elements;
     *      - record the average into the averages array;
     *      - increment both leftInd and rightInd.
     *
     * Edge cases:
     *  - k=0 => just return nums, correct;
     *  - k>nums.size/2 => return all -1, handled;
     *  - k==nums.size/2 => compute only the middleInd to average, correct;
     *  - the greatest average may be k = 10^5 all nums\[i] = 10^5 and nums.size=10^5 => (10^5 * 10^5)/10^5 = 10^5 fits into int;
     *  - the greatest subarray sum may be 10^5 * 10^5 = 10^10 => currentSum var must be Long.
     *
     * n=5 k=3 3>2
     *
     * Time: average/worst O(n);
     * Space: average/worst, if not counting the averages array, O(1).
     */
    fun efficientSlidingWindow(nums: IntArray, radius: Int): IntArray {
        if (radius == 0) return nums

        val averages = IntArray(size = nums.size) { -1 }

        val windowSize = radius * 2 + 1
        if (windowSize > nums.size) return averages

        var currentSum: Long = 0
        for (i in 0 until windowSize) currentSum += nums[i]
        averages[radius] = (currentSum / windowSize).toInt()

        var leftInd = 1
        for (rightInd in windowSize until nums.size) {
            currentSum = currentSum - nums[leftInd - 1] + nums[rightInd]
            val middleInd = rightInd - radius
            averages[middleInd] = (currentSum / windowSize).toInt()
            leftInd++
        }

        return averages
    }
}

fun main() {
    println(
        KRadiusSubarrayAverages().efficientSlidingWindow(
            nums = intArrayOf(7, 4, 3, 9, 1, 8, 5, 2, 6),
            radius = 3,
        ).contentToString()
    )
}
