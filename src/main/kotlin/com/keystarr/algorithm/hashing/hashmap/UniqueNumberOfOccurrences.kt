package com.keystarr.algorithm.hashing.hashmap

/**
 * LC-1207 https://leetcode.com/problems/unique-number-of-occurrences/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= numbers.size <= 1000;
 *  • -1000 <= numbers\[i] <= 1000.
 *
 * Final notes:
 *  • done [map] by myself in 8 mins.
 *
 * Value gained:
 *  • practiced recognizing and solving a problem efficiently with counting pattern via both a hashmap and an array indices
 *   + practiced set for distinct.
 */
class UniqueNumberOfOccurrences {

    /**
     * Problem rephrase:
     *  Given: array of integers
     *  Goal: return true if the number of occurrences of each value is unique (amongst other occurrence counters)
     *
     * Design:
     *  - since we have only 2000 numbers at most => we could use an array to count occurrences;
     *   (or a map, but array has better time const)
     *  - then iterate through the array/map, if the occurrence count (isnt 0 in case of array also) the set doesnt contain
     *   => add it into the set, otherwise return false.
     *  - return true if havent returned yet and finished the loop
     *
     * Time:
     *  - counting O(n)
     *  - iterating O(n)
     * Space:
     *  - counting if array O(2000) if map O(n)
     *  - set worst is all occurrs were unique => O(n), cause n occurrences for n numbers
     */
    fun map(numbers: IntArray): Boolean {
        val numToOccurMap = mutableMapOf<Int, Int>()
        numbers.forEach { numToOccurMap[it] = numToOccurMap.getOrDefault(it, 0) + 1 }

        val occursSet = mutableSetOf<Int>()
        numToOccurMap.values.forEach { occurrence ->
            if (occursSet.contains(occurrence)) return false
            occursSet.add(occurrence)
        }
        return true
    }

    /**
     * Same core idea, but array for counting instead of the map: better time complexity, but worse space complexity on average.
     * (where it could be O(n) it is O(2001) even though n can be << 2000)
     */
    fun array(numbers: IntArray): Boolean {
        val occurrences = IntArray(size = 2001)
        numbers.forEach {
            val key = it + 1000
            occurrences[key] = occurrences[key] + 1
        }

        val occursSet = mutableSetOf<Int>()
        occurrences.forEach {
            if (it == 0) return@forEach
            if (occursSet.contains(it)) return false
            occursSet.add(it)
        }
        return true
    }
}
