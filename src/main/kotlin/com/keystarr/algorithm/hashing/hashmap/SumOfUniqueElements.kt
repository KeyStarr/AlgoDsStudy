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

/**
 * LOL encountered the same problem 3 months ago => solved a lil bit faster and 1st time run/submit.
 * 🔥 didn't remember AT ALL NO RECOLLECTION that I've done this same problem previously.
 *
 * -------------
 *
 * LC-1748 https://leetcode.com/problems/sum-of-unique-elements/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.size <= 100;
 *  • 1 <= nums\[i] <= 100.
 *
 * Final notes:
 *  • done [hashmap] by myself in 5 mins;
 *
 * Value gained:
 *  • practiced recognizing and solving a uniqueness-type problem efficiently using counting via a hashmap.
 */
class SumOfUniqueElements2 {

    /**
     * trivial approach:
     *  - one pass: count all elements;
     *  - one pass: sum all elements that have a count of exactly 1.
     * Time: always O(n)
     * Space: average/worst O(n)
     *  - worst is all elements are unique => exactly n entries
     *
     * ----
     *
     * can we do better - O(1) space? or a single pass, reduce the time const?
     * (time would anyway be always O(n) since we need to check each element at least once)
     *
     * we could use 2 sets notUnique and unique. As we do a single pass, check if element is in notUnique -> do nothing,
     *  if it's in unique, remove it from there and add to not-unique, if neither -> add to unique.
     * => Time is same but reduced const, but space asymptotically is worse since it'll be always O(n) now.
     */
    fun hashmap(nums: IntArray): Int {
        val numToFreqMap = mutableMapOf<Int, Int>()
        nums.forEach { number -> numToFreqMap[number] = numToFreqMap.getOrDefault(number, 0) + 1 }

        var sum = 0
        numToFreqMap.entries.forEach { if (it.value == 1) sum += it.key }
        return sum
    }

    /**
     * since we have only up to 100 unique numbers => might utilize an array for better time const due to no hashing.
     */
    fun array(nums: IntArray): Int{
        val numToFreq = IntArray(size = 101)
        nums.forEach { number -> numToFreq[number] = numToFreq[number] + 1 }

        var sum = 0
        numToFreq.forEachIndexed { num, freq -> if (freq == 1) sum += num }
        return sum
    }
}