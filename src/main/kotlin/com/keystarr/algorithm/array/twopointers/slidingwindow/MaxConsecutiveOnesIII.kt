package com.keystarr.algorithm.array.twopointers.slidingwindow

/**
 * LC-1004 https://leetcode.com/problems/max-consecutive-ones-iii/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.length <= 10^5;
 *  • nums\[i] is either 0 or 1;
 *  • 0 <= k <= nums.length.
 *
 * Final notes:
 *  • solved via [efficient] by myself in 20 mins;
 *  • found a bug during first run - subtracted the amount of zeros in the longest subarray from its length. I forgot
 *      that we simply flip all of them to 1's.
 */
class MaxConsecutiveOnesIII {

    /**
     * Tools: Sliding Window.
     *
     * Useful observations:
     *  - if the subarray is valid, then the amount of 1's in it equals to its size, because it is assumed that we
     *      flip all 0's to 1's.
     *
     * Problem rephrasing for simplification:
     *  - find the longest subarray with at most `k` `0`'s;
     *  - just return its length.
     *
     * Edge cases:
     *  - k = nums.length => the amount of 1s in the entire nums;
     *  - k=0 => only subarrays of 1's are valid, it's ok.
     *
     * Time: always O(n)
     * Space: always O(1)
     */
    fun efficient(nums: IntArray, maxZerosCount: Int): Int {
        var leftInd = 0
        var maxLength = 0

        var currentZerosCount = 0
        for (rightInd in nums.indices) {
            if (nums[rightInd] == 0) currentZerosCount++

            while (currentZerosCount > maxZerosCount) {
                if (nums[leftInd] == 0) currentZerosCount--
                leftInd++
            }

            val currentLength = rightInd - leftInd + 1
            if (currentLength > maxLength) maxLength = currentLength
        }

        return maxLength
    }

    fun efficientCleaner(nums: IntArray, maxZerosCount: Int): Int {
        // the editorial solution has same O time/space complexity, but has less logic
        // didn't get it under 2 mins, decided it's not worth it rn => maybe check it back later for funsies
        TODO()
    }
}

fun main() {
    println(
        MaxConsecutiveOnesIII().efficient(
            nums = intArrayOf(1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0),
            maxZerosCount = 2,
        )
    )
}
