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

    /**
     * Rephrase the problem:
     * "find the longest valid subarray. Constraints: amount of 0 and amount of 1 must equal =>
     * sum of the subarray must equal to amount of 0's."
     *
     * Potentially useful properties:
     *  - amount of 0's = subarray.length - sum(subarray).
     *
     * Idea:
     *  - init 2 prefix amount of X hashmaps:
     *      - amountOfOnes<Int,Set<Int>> amount of '1' to all indices that have it;
     *      - amountOfZeroes<Int,Set<Int>> same idea but for '0'.
     *  - iterate through nums and fill them;
     *  - iterate through amountOfOnes.entries:
     *      - iterate through entry.value indices set:
     *          - if amountOfZeroes[entry.key].contains(entry.value[i]) => update the max length
     *
     */

    /**
     * As we expand, both the amount of 0's and 1's are either stale or increasing. As we need to balance both 0's and 1's
     * then there will be always such a subarray nums\[i:j] that it contains min(amount of 1's, amount of 0's) and is valid
     * => answer is that min * 2.
     *
     * Edge cases:
     *  - nums.length=0 => early return 0;
     *
     * Time: O(n);
     * Space: O(1).
     */
//    fun solution(nums: IntArray): Int {
//        if (nums.size == 1) return 0
//
//        val amountOfOnes = nums.sum()
//        val amountOfZeros = nums.size - amountOfOnes
//        return min(amountOfZeros, amountOfOnes) * 2
//    }

//    /**
//     * Rephrase the problem:
//     * "find the longest valid subarray. Constraints: amount of 0 and amount of 1 must equal =>
//     * sum of the subarray must equal to amount of 0's."
//     *
//     * possible tools - sliding window/prefix sum/hashmap.
//     *
//     * useful properties:
//     *  - when we expand right, then if the array was valid it always becomes invalid;
//     *  - the amount of both 0's and 1's is increasing but can be stale;
//     *  - amount of 0's = subarray.length - sum(subarray).
//     *
//     * idea:
//     *  - use 2 hashmaps for storing:
//     *      - prefix amount of 1's;
//     *      - prefix amount of 0's;
//     *      - both initialized with map[0] = 1.
//     *  - iterate through
//     *
//     */
//    fun solution(nums: IntArray): Int {
//
//    }

    /**
     *
     * "find the longest valid subarray. Constraints: amount of 0 and amount of 1 must equal"
     *
     * properties:
     *  - sum of the subarray must equal to amount of 0's;
     *  - any subarray of length 1 is invalid.
     *
     * longest valid subarray => sliding window:
     *  - start at rightInd = 0, leftInd = 0;
     *  - init maxValidLength = 0;
     *  - init currentOnesCount with if nums[0] is 1 => 1 else 0;
     *  - iterate through rightInd until nums.size-1 increase by 1:
     *      - if nums[rightInd] is '1' => increment currentOnesCount;
     *      - if current subarray is valid and it's length is greater than maxValidLength, then write it's length into maxValidLength;
     *      - if subarray is invalid, move left until its valid again, decrement currentOnesCount when nums[leftInd] == 1.
     *  - return maxValidLength.
     *
     * Edge cases:
     *  - nums[0] for leftInd-1 => handled via init and starting from leftInd=rightInd=1;
     *  - the max 1's amount is 10^5 => fits into Int;
     *  - nums.size=1 => early return 0;
     *  - nums.size=2 => works correctly.
     *
     * Time: O(n)
     * Space: O(1)
     *
     * CANT SHRINK WHEN CONSTRAINT IS INVALID BECAUSE IF CURRENT SUBARRAY BECOMES INVALID, IT CAN BECOME VALID STILL AS WE EXPAND.
     */
//    fun solution(nums: IntArray): Int {
//        if (nums.size == 1) return 0
//
//        var leftInd = 0
//        var maxValidLength = 0
//        var currentOnesCount = 0
//        for (rightInd in 0 until nums.size - 1) {
//            currentOnesCount += nums[rightInd]
//            val subarraySize = rightInd - leftInd + 1
//            if (currentOnesCount == subarraySize && subarraySize > maxValidLength) {
//                maxValidLength = subarraySize
//            } else {
//                while (currentOnesCount != rightInd - leftInd + 1) {
//                    currentOnesCount -= nums[leftInd]
//                    leftInd++
//                }
//            }
//        }
//
//        return maxValidLength
//    }
}

fun main() {
    println(
        ContiguousArray().efficient(
            nums = intArrayOf(0, 0, 0, 0, 0, 1, 1, 1)
        )
    )
}
