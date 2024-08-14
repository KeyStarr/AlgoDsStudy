package com.keystarr.algorithm.hashing.hashmap

/**
 * LC-406 https://leetcode.com/problems/longest-palindrome/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= letters.length <= 2000
 *  • letters consists of only lowercase/uppercase English.
 *
 * Final notes:
 *  • 🎉 done [efficient] by myself in 12 mins;
 *  • rephrasing the goal helped to catch my initial error in understanding the goal, I kinda skimmed over and though
 *   we needed to find the longest SUBSTRING palindrome (seen that other problem recently);
 *  • I sooo was afraid and not sure of whether the solution is correct. Like, I proved it more intuitively basing on these
 *   2 observations I made.
 *
 * Value gained:
 *  • practiced solving a palindrome problem with, well, just adapting the palindrome properties to the context.
 */
class LongestPalindrome {

    /**
     * problem rephrase:
     *  - given: a String, consists of lowercase and uppercase English only
     *  - goal: find the length of the max length palindrome that can be build out of that string (as a combination of chars)
     *      case-sensitive!
     *
     * ---------------------------------------
     *
     * trivial - brute force, try all possible combinations.
     *
     * observation: to build a palindrome, we need to 2 of the same letter.
     * approach:
     *  - count all letter frequencies, case-sensitive;
     *  - iterate through the frequencies, for each result+=frequency/2.
     *  - return result * 2
     *
     * edge cases:
     *  - if we have at least 1 letter with odd frequency => use it too as the center of the palindrome => increase total
     *   answer by 1.
     *
     * dry run:
     * abccccdd
     * a: 1, b: 1, c: 4, d: 2
     * 2+1=3
     * 3*2=6
     * 2 odd frequencies => result = 6 + 1 = 7
     *
     * Time: always O(n)
     * Space: always O(42) = O(1)
     *
     * Can't improve time, cause to find out the longest length we must check all options that we have to build it with.
     * Space is already O(1).
     */
    fun efficient(letters: String): Int {
        val letterFrequenciesMap = mutableMapOf<Char, Int>()
        letters.forEach { letterFrequenciesMap[it] = letterFrequenciesMap.getOrDefault(it, 0) + 1 }

        var isAnyOdd = false
        var letterPairs = 0
        letterFrequenciesMap.values.forEach { frequency ->
            if (!isAnyOdd && frequency % 2 == 1) isAnyOdd = true
            letterPairs += frequency / 2
        }
        return letterPairs * 2 + if (isAnyOdd) 1 else 0
    }
}
