package com.keystarr.algorithm.hashtable

import kotlin.math.max

/**
 * LC-525 https://leetcode.com/problems/contiguous-array/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.length <= 10^5;
 *  • nums\[i] is either 0 or 1.
 *
 * Final notes:
 *  • couldn't solve by myself at all in 60 mins, tried 4 types of solutions with prefixsum/hashmap/sliding window to no use;
 *  • can't yet fully understand the current solution even with explanations, though I think I get it intuitively.
 */
class ContiguousArray {

    /**
     * Tools: Prefix Sum, HashMap.
     *
     * Replace all 0's with -1 and rephrase the problem:
     * "Return the size of the longest subarray which sum is 0".
     *
     * Idea:
     * - use hashmap sum->index to store prefixSum;
     * - iterate through all nums:
     *  - update current sum with nums\[i];
     *  - if map.contains(currentSum) => update maxValidLength to max(i - map\[currentSum], maxValidLength);
     *  - else map\[currentSum] = i.
     *  return maxValidLength.
     *
     * Time: O(n)
     * Space: O(n)
     *
     * Discovered thanks to:
     * https://leetcode.com/problems/contiguous-array/solutions/99646/easy-java-o-n-solution-presum-hashmap/
     */
    fun efficient(nums: IntArray): Int {
        val prefixSumToStartIndMap = mutableMapOf(0 to -1)
        var currentSum = 0
        var maxValidLength = 0
        nums.forEachIndexed { rightInd, number ->
            currentSum += if (number == 1) 1 else -1
            val leftInd = prefixSumToStartIndMap[currentSum]
            if (leftInd == null) {
                prefixSumToStartIndMap[currentSum] = rightInd
            } else {
                val validSubarrayLength = rightInd - leftInd
                maxValidLength = max(maxValidLength, validSubarrayLength)
            }
        }
        return maxValidLength
    }
}

fun main() {
    println(
        ContiguousArray().efficient(
            nums = intArrayOf(0, 0, 0, 0, 0, 1, 1, 1)
        )
    )
}
