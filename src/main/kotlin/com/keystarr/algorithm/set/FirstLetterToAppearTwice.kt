package com.keystarr.algorithm.set

/**
 * LC-2351 https://leetcode.com/problems/first-letter-to-appear-twice/description/
 * difficulty: easy
 * constraints:
 *  • 2 <= s.length <= 100;
 *  • s consists of lowercase English letters;
 *  • s has at least one repeated letter;
 *  • no explicit time/space.
 */
class FirstLetterToAppearTwice {

    /**
     * Set<Char>, cause we need to find the 1st one to be twice.
     *
     * Time: O(n)
     * Space: O(1) cause only 26 chars in alphabet.
     */
    fun efficient(input: String): Char {
        val occurredLettersSet = mutableSetOf<Char>()
        input.forEach {
            if (occurredLettersSet.contains(it)) return it
            occurredLettersSet.add(it)
        }
        throw IllegalStateException("The result is guaranteed, shouldn't happen.")
    }
}
