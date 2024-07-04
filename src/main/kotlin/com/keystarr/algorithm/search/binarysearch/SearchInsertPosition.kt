package com.keystarr.algorithm.search.binarysearch

/**
 * LC-35 https://leetcode.com/problems/search-insert-position/editorial/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.length <= 10^4
 *  • •10^4 <= nums\[i] <= 10^4
 *  • nums distinct, sorted in ascending
 *  • •10^4 <= target <= 10^4
 *
 * Value gained:
 *  • practiced binary search;
 *  • interesting:
 *      • insertion pattern works here with the modification of target==middleElement return middle, 
 *       cause all numbers are distinct ,and we can just return the first match if there are any;
 *      • but search pattern works here too, with only modification being `return left` in the end;
 *      • =>> it appears that when there are no duplicates, the problem is heavily simplified for insertion then.
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
    fun basedOnPatternSearch(nums: IntArray, target: Int): Int {
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

    fun basedOnPatternInsertion(nums: IntArray, target: Int): Int {
        var left = 0
        var right = nums.size
        while (left < right) {
            val middle = (left + right) / 2
            val middleElement = nums[middle]
            when {
                target == middleElement -> return middle
                target < middleElement -> right = middle
                else -> left = middle + 1
            }
        }
        return left
    }
}

fun main() {
    println(
        SearchInsertPosition().basedOnPatternSearch(
            nums = intArrayOf(10, 11, 12),
            target = 10,
        )
    )
}
