package com.keystarr.algorithm.array.twopointers.slidingwindow

/**
 * LC-1695 https://leetcode.com/problems/maximum-erasure-value/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.size <= 10^5;
 *  • 1 <= nums\[i] <= 10^4.
 *
 * Final notes:
 *  • 🎉 done [efficient] by myself in 15 mins;
 *  • when I have abstracted out of the problem story and just laid out the goal / given => seen straight away, that
 *   for the goal of finding the best valid subarray, where best = max sum we can try a sliding window with a var for the
 *   current subarray sum. That abstraction to the pattern, upon which I hanged a hook, feels literally like cheating it works so well 🔥
 *  • prefix sum is for, usually, count of subarrays, not the best valid subarray => here just the sliding window usually is fine.
 *
 * Value gained:
 *  • practiced recognizing and solving a best valid subarray problem efficiently using a sliding window + HashSet.
 */
class MaximumErasureValue {

    /**
     * problem rephrase:
     *  - given: nums IntArray
     *  - goal: find the best valid subarray
     *    - valid = contains only UNIQUE (to the subarray itself) numbers;
     *    - best = max subarray sum.
     *
     * Best valid subarray, best = max sum => try prefixSum with sliding window?
     * valid = only unique elements, hm.
     *
     * - prefixSum
     * - running a sliding window:
     *  - how to expand?
     *    if the number we encounter doesn't have an equal number in the current subarray => include it and increase the sum;
     *  - how to shrink?
     *   if the number we encounter has an equal number in the subarray already => shrink, until we have removed that equal
     *    number from the subarray + reduce the sum accordingly => add the new number.
     *  - keep track of the maximum subarray sum => return it as the result on the sliding window loop's termination.
     *
     * DUH, we don't need a prefix sum, just keep track of the current subarray sum!
     * best subarray, EVEN with the SUM = usually just a sliding window
     * number of best subarrays with sum => prefix sum and stuff
     *
     * keep track of unique numbers and allow checking for duplicates via a HashSet
     *
     * Edge cases:
     *  - nums.size == 1 => we always add into the array and update the max sum with it => correct;
     *  - the new number's duplicate is the last number in the current subarray => empty the subarray entirely, its ok;
     *  - all numbers in nums are equal => the best subarray is any one of those numbers => correct.
     *
     * Time: always O(n)
     *  - hashset add is amortized O(1) and contains check is O(1)
     *  - always n iterations
     * Space: average/worst O(n)
     *  - worst set size is when all [nums] are unique => worst O(n), on average it depends on n
     */
    fun efficient(nums: IntArray): Int {
        val currentSet = mutableSetOf<Int>()
        var currentSum = 0
        var maxSum = 0
        var leftInd = 0
        nums.forEach { number ->
            while (currentSet.contains(number)) {
                val numberToRemove = nums[leftInd]
                currentSet.remove(numberToRemove)
                currentSum -= numberToRemove
                leftInd++
            }
            currentSet.add(number)
            currentSum += number
            if (currentSum > maxSum) maxSum = currentSum
        }
        return maxSum
    }
}
