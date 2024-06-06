package com.keystarr.algorithm.hashing.hashmap

/**
 * LC-217 https://leetcode.com/problems/contains-duplicate/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.length <= 10^5;
 *  • -10^9 <= nums\[i] <= 10^9;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • solved via [efficient] by myself in 5 mins.
 *
 * Value gained:
 *  • [SumOfUniqueElements] and this one indicate in the favor of the hypothesis that I'm ready for leet-easy hashset/hashmap
 *      problems on the interviews (solving consistently under 5 mins, first submission correct). I should solve in the
 *      coming days, maybe, a couple more, and then simply lay it off - only do mediums on hashset/hashmap from now on
 *      as spaced repetition / learning new approaches.
 */
class ContainsDuplicate {

    /**
     * rephrase: "return true if there are no duplicates in the array"
     *
     * duplicate => hashmap/hashset?
     *
     * Idea:
     *  - create a hashset;
     *  - iterate through nums:
     *      - if a number is contained in hashset => return true;
     *  - if terminated the loop without returning => return false.
     *
     * No real edge cases.
     *
     * Time: always O(n)
     * Space: worst/average O(k), where k-is the amount of distinct numbers in [nums] (basically O(n)).
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
