package com.keystarr.algorithm.hashing.hashmap

/**
 * LC-1748 https://leetcode.com/problems/sum-of-unique-elements/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.length <= 100;
 *  • 1 <= nums\[i] <= 100;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • solved via [efficient] by myself in 7 mins.
 *
 * Value gained:
 *  • MAAAAAAAAAN, AGAIN failed a bunch of runs due to stupid mistakes for being inattentive. Is that cause I sleep badly
 *      and dont do cardio in the morning????? like, failed 3-4 runs due to missing defaultValue=0 and that we sum up keys
 *      and not values...
 *  • easy hashmap problems seem ready for interviews, all in all, if I AM ATTENTIVE.
 */
class SumOfUniqueElements {

    /**
     * unique => hashset/hashmap? since we need to differentiate uniques from non-uniques => hashmap.
     *
     * Idea:
     *  - create hashmap number->count;
     *  - iterate through [nums], if number is present in the hashmap increment its count, otherwise add it into the map
     *      with the count of 1;
     *  - iterate through entries of the hashmap, return the sum of all numbers which have count==1.
     *
     * Edge cases: no real ones, works correctly as-is.
     *
     * Time: always O(n)
     * Space: O(k), where k=amount of distinct numbers in nums (average/worst O(n))
     */
    fun efficient(nums: IntArray): Int {
        val numberToCountMap = mutableMapOf<Int, Int>()
        nums.forEach { number -> numberToCountMap[number] = numberToCountMap.getOrDefault(number, 0) + 1 }
        return numberToCountMap.entries.sumOf { if (it.value == 1) it.key else 0 }
    }
}
