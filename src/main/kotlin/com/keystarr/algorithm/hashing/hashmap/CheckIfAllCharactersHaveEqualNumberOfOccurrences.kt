package com.keystarr.algorithm.hashing.hashmap

/**
 * LC-1941 https://leetcode.com/problems/check-if-all-characters-have-equal-number-of-occurrences/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= input.length <= 1000;
 *  • input consists of lowercase English;
 *  • no explicit time/space.
 */
class CheckIfAllCharactersHaveEqualNumberOfOccurrences {

    /**
     * Idea:
     *  - create hashmap<char,int>;
     *  - iterate through all characters in a string: count occurrences of each character vai hashmap;
     *  - iterate through hashmap's values, return true if all equal, false otherwise.
     *
     * Edge cases:
     *  - input contains multiple occurrences of a single element => then the string is 'good' cause all elements (1)
     *      occur same number of times, trivially;
     *  - input.length == 1 => a special case of above, works correct.
     *
     * Time: O(n)
     * Space: O(1), cause only 26 characters => 26 entries to hashmap.
     */
    fun efficient(input: String): Boolean {
        val lettersCountMap = mutableMapOf<Char, Int>()
        input.forEach { lettersCountMap[it] = lettersCountMap.getOrDefault(it, 0) + 1 }
        var firstCount: Int? = null
        for (count in lettersCountMap.values) {
            if (firstCount == null) firstCount = count else if (count != firstCount) return false
        }
        return true
    }

    fun lessEfficientButCleaner(input: String): Boolean {
        val lettersCountMap = mutableMapOf<Char, Int>()
        input.forEach { lettersCountMap[it] = lettersCountMap.getOrDefault(it, 0) + 1 }
        return lettersCountMap.values.toSet().size == 1
    }
}
