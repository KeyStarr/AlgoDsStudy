package com.keystarr.algorithm.hashtable

/**
 * LC-1189 https://leetcode.com/problems/maximum-number-of-balloons/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= text.length <= 10^4;
 *  • text consists of only English lowercase.
 *
 * Final notes:
 *  • done [efficient] in 10 mins;
 *  • could use an array for reducing the const, but no point for learning rn.
 */
class MaximumNumberOfBalloons {

    /**
     * Rephrase the problem:
     *  "Count all the amount of occurrences of distinct characters from "balloon", calculate the amount of times that word
     *  may be constructed given the occurrences count (say, l and o must be used x2, other letters once)".
     *
     * Counting 5 distinct characters:
     * - use a hashmap;
     * - use an array;
     * - use 5 variables.
     *
     * Use a hashmap cause it is cleaner for real world production.
     * Calculate by getting the min across all characters need for the word.
     *
     * Edge cases:
     *  - text.length == 1 => works correctly;
     *  - 'o' and 'l' are required twice => handled.
     *
     * Time: O(n)
     * Space: O(1)
     */
    fun efficient(text: String): Int {
        val charsCountMap = mutableMapOf('b' to 0, 'a' to 0, 'l' to 0, 'o' to 0, 'n' to 0)
        text.forEach { char ->
            if (charsCountMap.contains(char)) charsCountMap[char] = charsCountMap[char]!! + 1
        }
        charsCountMap['l'] = charsCountMap['l']!! / 2
        charsCountMap['o'] = charsCountMap['o']!! / 2
        return charsCountMap.values.minOf { it }
    }
}
