package com.keystarr.algorithm.other.bitmanipulation

/**
 * LC-136 https://leetcode.com/problems/single-number/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.length <= 3*10^4;
 *  • -3*10^4 <= nums\[i] <= 3*10^4;
 *  • each element in the array appears twice except for one element which appears only once.
 *
 * Final notes:
 *  • only learnt xor from the DSA course. I knew efficient here was bitwise, but couldn't come up with it myself in
 *   reasonable time;
 *  • even after dry-running I still don't understand fully intuitively why 2 numbers cancel each it out with XOR even if
 *   there are different other numbers in between. Decided it isn't the priority right now, so leave it as-is right now.
 *   TODO: why, what's the proof?
 *
 * Value gained:
 *  • practiced approximating efficient solution target complexities from a suboptimal one;
 *  • practiced designing an efficient solution via bitwise operations;
 *  • reinforced XOR properties in real problems.
 */
class SingleNumber {

    /**
     * problem rephrase:
     *  - given is an array of integers
     *  - the input is always such that all distinct elements have a duplicate except exactly one
     *  goal: find that single distinct element
     *
     * approach #1 hashmap
     *  - count all elements
     *  - iterate through the map entries, upon finding the one with count == 1 return it's key
     * Time: O(n)
     *  - counting O(n)
     *  - finding the 1 O(n)
     * Space: O(n) for the map with n/2 entries
     *
     * we won't improve time asymptotically, since we'd have to check elements at least depending on n, but we probably
     * can improve space - how?
     *
     * xor all numbers, the result will be the only single number in the [nums]
     * Time: O(n)
     * Space: O(1)
     */
    fun efficient(nums: IntArray): Int {
        var mask = nums[0]
        for (i in 1 until nums.size) {
            val number = nums[i]
            mask = mask.xor(number)
        }
        return mask
    }
}
