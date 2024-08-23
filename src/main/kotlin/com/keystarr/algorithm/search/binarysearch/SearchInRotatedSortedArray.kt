package com.keystarr.algorithm.search.binarysearch

/**
 * LC-33 https://leetcode.com/problems/search-in-rotated-sorted-array/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.size <= 5 * 10^3;
 *  • -10^4 <= nums\[i], target <= 10^4;
 *  • all values in nums are unique.
 *
 * Final notes:
 *  • ⚠️⚠️ failed to solve in 30 mins, had no clue even at how to design the algo, what to try next, since the observations
 *  I made don't seem to lead to a feasible solution with O(logn) time. The problem has no hints at leetcode;
 *  • what a nasty heavy "single observation" problem. If one gets that idea to check halves => golden, otherwise stuck.
 *   Maybe I'm wrong and there was a step-by-step reasoning one could've achieved that, well, I couldn't. I feel frustrated.
 *
 * Value gained:
 *  • practiced a modified binary search problem with a single tricky observation to make which lays out the path to the full solution.
 */
class SearchInRotatedSortedArray {

    // TODO: retry in 1-2 weeks, try to build a better intuition (dry run it more?)

    /**
     * SUMMARY of the solution design, retrace the steps:
     *  1. core observations:
     *   - if [nums] was rotated, then the array is split into 2 parts: original, moved;
     *   - all numbers in the moved part are less than numbers in the original;
     *   - 1=> we can trivially if the target is in the moved part or the original one: in moved if target < nums[0]
     *   - 2=> we can also trivially tell if the current middle element is in the moved or the original part: in moved if nums\[middle] < nums[0]
     *  2. now to the binary search modification:
     *   - if both the target and the middleNumber are in the same (original/moved) part of the array, then perform the regular binary search,
     *    cause its sorted;
     *   - otherwise go the opposite half, cause [target] is guaranteed to not be in the same half with the current middle.
     *
     * Still I understand not the full intuition here, but its ok for now, we have bigger priorities.
     *
     * ------------
     *
     * problem rephrase:
     *  - given:
     *   - a special array of integers:
     *    - sorted ascending;
     *    - only distinct values;
     *    - could've been "rotated", if so:
     *     there is some index k such that nums[k:n-1] are now at the start of the array, and nums[0:k-1] are now at nums[n-1-k]
     *   - target: Int
     *  Goal: return the index of target of -1
     *
     *
     * WRONG (initial) APPROACH
     *
     * Must search in O(logn) time => use binary search.
     *
     * how does "rotation" change the decision for the binary search?
     *  if target < nums\[middle] => where do we go, left or right?
     *
     *
     * 4,5,6,7,0,1,2 target=1
     * 6 < 7 => go left, and target is indeed in that half, but that's not always the case
     *
     *
     * 4,5,6,7,0,1,2 target=1
     * 0 < 7 => we'd wanna go left, but we have to really go right, don't we?
     * r=6
     *
     * 0 < 5
     * r=5
     * 0 < 4
     * rI=mI-1=-1 => uh-huh, so if rI is -1, we try to reset and look in the other half? same if lI ever becomes -1?
     *
     * BUT here we have a pretty case of exactly halves of the array reversed, what if it was not halves?
     *
     * 2 3 4 5 6 7 0 1, t=0
     * go search the original left part until -1 => try the right half
     * l=6
     * r=1
     * m=7
     * 0<7
     * r=6
     * m=6
     * 0 < 6
     * r=5
     * r-l=-1 => same story as with l=0 r=-1
     * and again we have to search the right half, but now a level deeper => l=0 r=1.
     *
     * =>
     *
     * if r-l=-1 search the right half
     * if l-r=-1 search the left half
     *
     * at first, the original 1/2 which we didn't choose, then 1/4 of that 1/2 that we didn't choose etc?
     * at most then logn * logn no? besides, how would I even program that "come back to" portion?
     *
     * -------------------------------------------------------
     *
     * ACTUAL APPROACH:
     *
     * 4,5,6,7,0,1,2 target=2
     *
     * 7 > 4 => the current middle is in the non-moved half
     * 2 < 4 => the target value is in the moved half
     * => go to the moved half, which is the opposite of the non-moved half
     *
     * lI=4
     * rI=6
     *
     * mI=4 + (6-4)/2= 5
     * 2<4 => current middle is in the moved half
     * 2>1 => go right, just like the regular binary search
     *
     * note: m != nums[0], unless m==0, cause all numbers are distinct
     *
     * Time: average/worst O(logn)
     * Space: always O(1)
     *
     * learned the core idea from https://leetcode.com/problems/search-in-rotated-sorted-array/solutions/154836/the-inf-and-inf-method-but-with-a-better-explanation-for-dummies-like-me/,
     * designed and implemented the rest myself.
     */
    fun efficient(nums: IntArray, target: Int): Int {
        var left = 0
        var right = nums.size - 1
        val firstNum = nums[0]
        val isTargetInMoved = target < firstNum
        while (left <= right) {
            val middle = left + (right - left) / 2
            val midNum = nums[middle]
            if (target == midNum) return middle
            val isCurrentMidInMoved = midNum < firstNum
            when {
                isTargetInMoved && !isCurrentMidInMoved -> left = middle + 1
                !isTargetInMoved && isCurrentMidInMoved -> right = middle - 1
                else -> if (target < midNum) right = middle - 1 else left = middle + 1
            }
        }
        return -1
    }
}

fun main() {
    println(
        SearchInRotatedSortedArray().efficient(
            nums = intArrayOf(4, 5, 6, 7, 0, 1, 2),
            target = 0,
        )
    )
}
