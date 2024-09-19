package com.keystarr.algorithm.array

/**
 * LC-2000 https://leetcode.com/problems/reverse-prefix-of-word/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= word.length <= 250;
 *  • word and targetChar consist only of lowercase English.
 *
 * Final notes:
 *  • done [efficient] by myself in 7 mins.
 *
 * Value gained:
 *  • practiced solving a partial string reversal type problem using a single pass and a string builder.
 */
class ReversePrefixOfWord {

    /**
     * trivial approach:
     *  - find the first occurrence of [targetChar];
     *  - iterate through the string backwards from that targetChar to 0, add all chars into a StringBuilder;
     *  - either split the first string or iterate through it again this time forward and add all chars. Or iterate once
     *   until targetCharInd+1 add all chars into a stack or reverse the StringBuilder before appending the rest.
     *
     * I suppose the follow-up would be - how to do that in 1 pass, no? or maybe we could use O(1) space somehow?
     *
     * Edge cases:
     *  - [targetChar] isn't present in the word => correct as-is. But may early return for O(1) time/space!
     *
     * Time: always O(n)
     * Space: always O(n), if we don't count the result space then O(1)
     *
     * one pass, huh, just two loops.
     *
     * ---- optimization
     *
     * we definitely can't improve time cause we have to add each char to the result and we cant modify the original input
     * string. Space - I'm sure we cant too for the same reason, we have to allocate new String of n length.
     */
    fun efficient(word: String, targetChar: Char): String {
        val firstOccurInd = word.indexOfFirst { it == targetChar }
        if (firstOccurInd == -1) return word

        val builder = StringBuilder()
        for (i in firstOccurInd downTo 0) builder.append(word[i])
        for (i in firstOccurInd + 1 until word.length) builder.append(word[i])
        return builder.toString()
    }

    /**
     * Core same as [efficient] but less code, a single loop - when i <= than firstOccurInd take chars in reverse, later
     *  take in the original order. Though arguably [efficient] is more readable.
     *
     * Learned thanks to https://leetcode.com/problems/reverse-prefix-of-word/description/
     */
    fun efficientCleaner(word: String, targetChar: Char): String {
        val firstOccurInd = word.indexOfFirst { it == targetChar }
        if (firstOccurInd == -1) return word

        val result = StringBuilder()
        for (i in word.indices) result.append(if (i <= firstOccurInd) word[firstOccurInd - i] else word[i])
        return result.toString()
    }
}

fun main() {
    println(ReversePrefixOfWord().efficient(word = "abcdefd", targetChar = 'd'))
}