package com.keystarr.algos.arrays

import kotlin.math.abs

/**
 * LC-977 https://leetcode.com/problems/squares-of-a-sorted-array/description/
 * constraints:
 *  • 1 <= nums.length <= 10^4;
 *  • -10^4 <= nums\[i] <= 10^4;
 *  • nums is sorted non-decreasing;
 *  • with follow-up time O(n);
 *  • no explicit space.
 *
 * Final note - done an [efficient] myself in 40 mins, but not the cleanest [efficientCleaner].
 */
class SquaresOfASortedArray {

    /**
     * hints:
     * - positives preserve sorting;
     * - negatives become positive and need to be put in right places;
     * - (key, took some time to realize) all negatives after squaring are sorted descending.
     *
     * naive (discarded):
     *  start from beginning of the array
     *  if number < 0 abs it and use binary search to insert it into the correct place (square later?)
     *  time worst case O(n^2)
     *
     * efficient (two pointers, merge-sort style):
     *  square all numbers in-place O(n)
     *  merge both parts of the array in-place O(n)
     *  time always O(n)
     *  space O(n)
     *
     *  edge cases:
     *      - no negative numbers => splitInd = 0
     *      - all numbers are negative => splitInd = nums.size - 1
     *      - all numbers are 0
     *
     * Time: O(n)
     * Space: O(n)
     */
    fun efficient(nums: IntArray): IntArray {
        // find the index of negative-positive point break
        var splitInd = nums.size

        for (i in nums.indices) {
            if (nums[i] >= 0) {
                splitInd = i
                break
            }
        }

        // square all numbers
        for (i in nums.indices) nums[i] = nums[i] * nums[i]

        var leftInd = splitInd - 1
        var rightInd = splitInd
        var currentResultInd = 0
        val result = IntArray(size = nums.size)
        while (leftInd >= 0 && rightInd < nums.size) {
            val currentLeft = nums[leftInd]
            val currentRight = nums[rightInd]
            if (currentLeft < currentRight) {
                result[currentResultInd] = currentLeft
                leftInd--
            } else {
                result[currentResultInd] = currentRight
                rightInd++
            }
            currentResultInd++
        }

        while (leftInd >= 0) {
            result[currentResultInd++] = nums[leftInd]
            leftInd--
        }

        while (rightInd < nums.size) {
            result[currentResultInd++] = nums[rightInd]
            rightInd++
        }

        return result
    }

    /**
     * A variation of [efficient] but much simpler and concise.
     *
     * Ideas:
     *  KEY: fill the result array starting from max numbers, not min (start filling from the end);
     *  =>
     *  - run a single for by numbers indices, no need to catch exceeding boundaries. If left/end exceed, since
     *      both are converging in the middle => we will always stay within the boundaries and correct logic, at the end
     *      if positive/negative amount is not equal a pointer would simply get to the smallest element of the abs negatives/positives,
     *      which will be sorted correctly;
     *  - we need to iterate exactly nums.size - 1 amount of times, cause we need to take each element once, and according
     *      to above exceeding boundaries is not the problem.
     *
     * Discovered thanks to https://leetcode.com/problems/squares-of-a-sorted-array/editorial/
     *
     * Time: O(n)
     * Space: O(n)
     */
    fun efficientCleaner(nums: IntArray): IntArray {
        var leftInd = 0
        var rightInd = nums.size - 1

        val result = IntArray(nums.size)
        for (resultInd in (nums.size - 1) downTo 0) {
            val currentLeft = nums[leftInd]
            val currentRight = nums[rightInd]
            val choice = if (abs(currentLeft) > abs(currentRight)) {
                leftInd++
                currentLeft
            } else {
                rightInd--
                currentRight
            }
            result[resultInd] = choice * choice
        }

        return result
    }
}

fun main() {
    println(SquaresOfASortedArray().efficientCleaner(intArrayOf(-4, -1, 0, 3, 10)).contentToString())
}
