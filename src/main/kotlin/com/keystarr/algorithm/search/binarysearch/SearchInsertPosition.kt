package com.keystarr.algorithm.search.binarysearch

/**
 *
 * constraints:
 *  - 1 <= nums.length <= 10^4
 *  - -10^4 <= nums\[i] <= 10^4
 *  - nums distinct, sorted in ascending
 *  - -10^4 <= target <= 10^4
 */
class SearchInsertPosition {

    /**
     * sorted and distinct input array + O(logn) time search => binary search
     * since the array has distinct numbers only, no need to handle the edge cases for duplicates
     *
     * design - a straightforward binary search.
     *
     * edge cases:
     *  - max ind sum is (10^4 + (10^4 - 1)) => fits into int, no overflow;
     *  - [target] is less than nums\[0] => return 0, correct (termination at right = -1, left = 0);
     *  - [target] is greater than nums\[nums.size - 1] => return nums.size, correct (termination at left=nums.size, right=nums.size-1).
     *
     * Time: O(logn)
     * Space: O(1)
     */
    fun efficient(nums: IntArray, target: Int): Int {
        var left = 0
        var right = nums.size - 1
        while (left <= right) {
            val middle = (left + right) / 2
            val middleElement = nums[middle]
            when {
                target == middleElement -> return middle
                target < middleElement -> right = middle - 1
                else -> left = middle + 1
            }
        }
        return left
    }
}

fun main() {
    println(
        SearchInsertPosition().efficient(
            nums = intArrayOf(10, 11, 12),
            target = 8,
        )
    )
}
