package com.keystarr.algorithm.hashing.hashset

/**
 * LC-1832 https://leetcode.com/problems/check-if-the-sentence-is-pangram/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= sentence.length <= 1000;
 *  • sentence consists of lowercase English letters;
 *  • no explicit time/space.
 */
class CheckIfTheSentenceIsPangram {

    /**
     * Idea - convert input into a HashSet => check its size. Since its only English lowercase letters,
     * then if it is 26 then true else false.
     *
     * No important edge cases.
     *
     * Time: O(n) for input to set conversion;
     * Space: O(1) cause only 26 characters in the set max.
     */
    fun efficient(sentence: String) = sentence.toSet().size == 26

    /**
     * Idea - use an array of size 26 where each slot is a flag whether the letter has occurred or not, and each index
     * is calculated via ASCII like letterCode - 'a'.code.
     *
     * No important edge cases.
     *
     * Time: O(n) for counting.
     * Space: O(1) cause fixed array of size 26.
     */
    fun array(sentence: String): Boolean {
        val occurrences = BooleanArray(size = 26)
        sentence.forEach {
            val index = it.code - 97
            if (!occurrences[index]) occurrences[index] = true
        }
        return occurrences.all { it }
    }

    /**
     * Idea - we need to know whether each character is present + we know all characters in advance => check for each
     * character whether it is present.
     *
     * Time: O(n) but the const is quite big compared to other solutions here, it's 26n
     *  (though maybe compared to hash function const not that bad?)
     * Space: O(1)
     *
     * Discovered thanks to https://leetcode.com/problems/check-if-the-sentence-is-pangram/
     */
    fun checkAllLetters(sentence: String): Boolean {
        for (i in 0 until 26) {
            val currentChar = ('a'.code + i).toChar()
            if (!sentence.contains(currentChar)) return false
        }
        return true
    }
}
