package com.keystarr.algorithm.array.twopointers.slidingwindow

/**
 * LC-713 https://leetcode.com/problems/subarray-product-less-than-k/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.length <= 3*10^4;
 *  • 1 <= nums\[i] <= 10^3;
 *  • 0 <= k <= 10^6;
 *  • no explicit time/space.
 *
 *  Final note - not counting time, cause that's an example from the Leetcode DSA course => I already had hints.
 *  Still, solved with those hints by myself [efficient], but improved to [efficientClean] only after checking the editorial.
 */
class SubarrayProductLessThanK {

    /**
     * Tools: Sliding Window.
     *
     *
     * Idea: at each iteration move `right` pointer by 1 position. If subarray is valid, then add to the
     * total subarrays sum all subarrays that contain that new value and all previous element down to `left` pointer.
     * If not valid, shrink left until it becomes valid and do that logic above.
     *
     * Key: everytime the window is valid, at most once per current rightInd value, count all valid subarrays of the
     *  window but only including nums\[rightInd] - that is, only NEW valid subarrays!
     *
     * Edge cases:
     *  - k=0 => the result is always 0 (cause nums\[i] > 0)
     *  - nums.length = 1 and nums\[0] < k => result 0.
     *
     * Time: always O(n) cause both indices check each element only once => we always have 2n total iterations (counting both cycles);
     * Space: always O(1).
     */
    fun efficient(nums: IntArray, targetProduct: Int): Int {
        if (targetProduct <= 0) return 0

        var leftInd = 0
        var validSubarraySum = 0
        var currentProduct = 1
        for (rightInd in nums.indices) {
            currentProduct *= nums[rightInd]

            while (currentProduct >= targetProduct && leftInd < rightInd) {
                currentProduct /= nums[leftInd]
                leftInd++
            }

            // condition for cases like [2 5 1000] t = 100, where l = r = 2 (nums[2]=1000) => we exited while, but
            // the current subarray is still invalid
            if (currentProduct < targetProduct) validSubarraySum += rightInd - leftInd + 1
        }
        return validSubarraySum
    }

    /**
     * Same as [efficient] with 2 improvements:
     *  - if targetProduct = 1 then result is always 0 cause the smallest possible product 1, but problem states
     *      "subarrays STRICTLY less than 1" => that can never happen for targetProduct = 1;
     *  - no need for the second while condition part and the condition after that to handle cases when nums\[i] >= targetProduct:
     *      - e.g. consider [10, 5, 1000, 2000, 2] with targetProduct=100;
     *      - if nums[rightInd] >= targetProduct, then left always becomes leftInd = rightInd + 1 with (at that point)
     *          always currentProduct = 1, then currentProduct /= nums[rightInd] is always 0;
     *      - targetProduct is always > 1 cause of precondition;
     *      - in such cases while always stops, and then we increment validSubarraySum by:
     *          rightInd - (rightInd + 1) + 1 = 0 => which is correct, cause current subarray is invalid;
     *          - next iteration always rightInd get incremented and rightInd = leftInd, the algo moves on correctly;
     *      TL;DR; current algo "steps over" values of nums which are >= targetProduct.
     */
    fun efficientClean(nums: IntArray, targetProduct: Int): Int {
        if (targetProduct <= 1) return 0

        var leftInd = 0
        var validSubarraySum = 0
        var currentProduct = 1
        for (rightInd in nums.indices) {
            currentProduct *= nums[rightInd]

            while (currentProduct >= targetProduct) {
                currentProduct /= nums[leftInd]
                leftInd++
            }

            validSubarraySum += rightInd - leftInd + 1
        }
        return validSubarraySum
    }
}

fun main() {
    println(
        SubarrayProductLessThanK().efficientClean(
            intArrayOf(1, 1, 1, 1),
            2,
        )
    )
}
