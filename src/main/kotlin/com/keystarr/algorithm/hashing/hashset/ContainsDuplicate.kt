package com.keystarr.algorithm.hashing.hashset

/**
 * LC-217 https://leetcode.com/problems/contains-duplicate/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.size <= 10^5;
 *  • -10^9 <= nums\[i] <= 10^9.
 *
 * Final notes:
 *  • done [efficient] by myself in 5 mins;
 *  • there probably is a tricky O(1) space solution here, with like bitmask or something, but I don't think its worth
 *   investing time into that right now => check => hm, interesting, apparently not ⚠️ why no bitmask tricks like the one with distinct number?
 *
 * Value gained:
 *  • practiced recognizing and solving a counting problem efficiently using a hashset.
 */
class ContainsDuplicate {

    /**
     * goal: return true if any value is not distinct in integers array.
     *
     * simple approach:
     *  - iterate through [nums], add each into a set, if newly encountered number is already in the set => return true.
     * Time: average/worst O(n)
     *  worst is all numbers are distinct => n iterations
     * Space: average/worst O(n), same worst case as time, then n elements in the set
     */
    fun efficient(nums: IntArray): Boolean {
        val set = mutableSetOf<Int>()
        nums.forEach { number ->
            if (set.contains(number)) return true
            set.add(number)
        }
        return false
    }
}
