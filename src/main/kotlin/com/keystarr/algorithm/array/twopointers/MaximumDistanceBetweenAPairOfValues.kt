package com.keystarr.algorithm.array.twopointers

import kotlin.math.abs
import kotlin.math.max

/**
 * LC-1855 https://leetcode.com/problems/maximum-distance-between-a-pair-of-values/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums1.size, nums2.size <= 10^5;
 *  • 1 <= nums1\[i], nums2\[j] <= 10^5;
 *  • both nums1 and nums2 are non-increasing.
 *
 * Final notes:
 *  • ⚠️⚠️ spent ~30-40 mins doing [wrongGoal] ))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))
 *   turns out I interpreted the goal wrong. The task clearly specified that the condition holds only one way, indices + arrays in it
 *   are not interchangeable!
 *   Got used to I guess seeing problems with the opposite being true => didn't pay enough attention and ended up solving
 *    a far more difficult problem.
 *  • as I've realized the goal mistake (failed a test case on submission), done [efficient] by myself in ~10-15 mins;
 *  • 🔥 solid reasoning though, I feel like I've done the right solution for [wrongGoal] and when I realized the interpretation error,
 *   I have solidly, step-by-step based on the non-decreasing observations, on the minimum input sets reasoned what the algorithm's
 *   conditions should be (still new to me, to do such bold reasoning and be confident in observations that I infer).
 *
 * Value gained:
 *  • 💡PAY SPECIAL ATTENTION, FORCE MYSELF to interpret the "find valid pair" type questions correctly: does the condition
 *   directly specify that the condition CAN BE REVERSED if applied to 2 different collections? If not - its a lot simpler then!
 *   (a common thread of errors => I must learn to better approach every problem phenomenologically, yet maximize efficiency on recognizing familiar patterns);
 *  • practiced efficiently solving a "find the best valid pair" type question on 2 arrays using 2 pointers.
 */
class MaximumDistanceBetweenAPairOfValues {

    // TODO: interpreted the goal wrong => retry in 1-2 weeks, shuffled, to practiced just that.

    /**
     * Since valid is only i <= j && nums1\[i] <= nums2\[j]
     *
     * then 2 pointers until we have no elements in either array:
     *  - if that condition is true:
     *   - update the max distance;
     *   - j++;
     *   (since by decreasing i we'll only decrease current distance, but if we increase j there's a chance the
     *    pair still will be valid => we'll increase current distance)
     *  - else if i > j:
     *   - j++ (no point in moving i since with invalid indices we'll never get a valid pair)
     *  - else if nums1\[i] > nums2\[j]:
     *   - i++ (try to decrease nums1\[i], since array is sorted non-increasing)
     *
     * end when either array is out of elements, since trivially we have no valid pairs left.
     *
     * Time: average/worst O(max(n, m)), n=nums1.size m=nums2.size
     * Space: always O(1)
     */
    fun efficient(nums1: IntArray, nums2: IntArray): Int {
        var i = 0
        var j = 0
        var maxDistance = 0
        while (i < nums1.size && j < nums2.size) {
            when {
                i <= j && nums1[i] <= nums2[j] -> {
                    maxDistance = max(maxDistance, j - i)
                    j++
                }
                i > j -> j++
                else -> i++ // nums1[i] > nums2[j]
            }
        }
        return maxDistance
    }

    /**
     *
     * WRONG - MISUNDERSTOOD THE REQUIREMENTS!!!
     * it turns out that the pair can ONLY be valid if i <= j && nums1\[i] <= nums2\[j]
     *  => it IS NOT VALID if the arrays are the other way around! j <= i && nums2\[j] <= nums1\[i]
     *
     * I assumed the inverse was true since I've solved many problems that specified that, but here its clearly only
     *  ONE WAY.
     *
     * --------------------
     *
     * given:
     *  - nums1 and nums2 IntArray;
     *  - both are NON-INCREASING!
     *
     * goal: return the metric of the best valid pair with one number from [nums1] and another from [nums2]. If there are none, return 0.
     *  valid = i <= j && nums\[i] <= nums\[j]
     *  metric = distance = j - i
     *  best = max distance
     *
     * the "both arrays are non-increasing" sticks out => can we leverage that?
     *
     * key observation: BOTH the index and the number HAVE TO BE GREATER than the other index/number (if not equal).
     *
     * approach:
     *  - two pointers, while either array still has at least 1 element, do:
     *   - if one number is larger than the other:
     *    - if indices are also valid => we update the max distance;
     *    - we move larger number's index to the right
     *     (if its index is larger, then moving the other index would only reduce the current distance and any potential
     *      distance if next number to larger is still valid => is never a correct move. If its index is lower, than
     *      moving the order index never produces a valid pair with any further elements in that other's array)
     *   - if numbers are equal:
     *    - if indices aren't equal => we move the larger index forward (same logic as with numbers, moving smaller number's index
     *     will only decrease the current distance, and ruin chances of a greater one if in the array of the larger number
     *     by moving index to right we'd make more valid pairs)
     *    - if indices are equal => future numbers in both arrays are either equal to or less than the current number in both
     *     => check both future numbers, if either is equal to the current number, move to it (since it'd be a valid pair still)
     *     otherwise move either one.
     *
     * when we're out of either array's numbers => terminate, since there are no valid pairs left.
     *
     * Edge cases:
     *  - nums1.size == 1 && nums2.size == 1 =>
     *  - nums1.size == 1 || nums2.size == 1 =>
     *  - all numbers in both arrays are equal =>
     *
     *
     * 3 3 3
     * 3 3 1
     *
     * Time:
     * Space:
     */
    fun wrongGoal(nums1: IntArray, nums2: IntArray): Int {
        var i = 0
        var j = 0
        var maxDistance = 0
        while (i < nums1.size && j < nums2.size) {
            val num1 = nums1[i]
            val num2 = nums2[j]
            if (num1 == num2) {
                if (i == j) {
                    var lastEqualInd1 = i
                    while (lastEqualInd1 < nums1.size && nums1[lastEqualInd1] == num1) lastEqualInd1++

                    var lastEqualInd2 = j
                    while (lastEqualInd2 < nums2.size && nums2[lastEqualInd2] == num2) lastEqualInd2++

                    if (lastEqualInd1 >= lastEqualInd2) i++ else j++
                } else {
                    maxDistance = max(maxDistance, abs(i - j))
                    if (i < j) j++ else i++
                }
            } else {
                if (num1 < num2) {
                    if (i < j) maxDistance = max(maxDistance, j - i)
                    j++
                } else {
                    if (i > j) maxDistance = max(maxDistance, i - j)
                    i++
                }
            }
        }
        return maxDistance
    }
}

fun main() {
    println(
        MaximumDistanceBetweenAPairOfValues().efficient(
            nums1 = intArrayOf(5, 4),
            nums2 = intArrayOf(3, 2),
        )
    )
}
