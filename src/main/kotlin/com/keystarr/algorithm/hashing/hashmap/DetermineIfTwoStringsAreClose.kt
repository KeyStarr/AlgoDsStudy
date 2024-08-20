package com.keystarr.algorithm.hashing.hashmap

/**
 * ⭐️ a great example of a HIDDEN meta-counting problem (counting on steroids)
 * LC-1657 https://leetcode.com/problems/determine-if-two-strings-are-close/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= word1.length, word2.length <= 10^5;
 *  - word1 and word contain lowercase English.
 *
 * Final notes:
 *  • 🏆 done [efficient] by myself, total 31mins:
 *   • designed in 24 mins, design choices were not obvious at first, but got all 1st try through step-by-step fog clearing 🏆;
 *   • implemented in 7 mins.
 *  • 🔥 like a number of times before, I couldn't see how to solve the problem at first, I considered the direction of
 *   actually conducting transformations. Then I just started reasoning "bad to good, good to great" kinda principle,
 *   reasoning where can't the answer be achieved (since the problem is asking for true/false) and reducing the solution
 *   space conclusion by conclusion arrived at the final question of same length, same charset but different number of occurrences!
 *   => only THEN I've realized that it's not an actual simulation problem, but simply a meta-occurrence counting one)
 *  • that meta-occurrence level is fun though, good that I've encountered that before, the idea popped earlier in my head
 *   than it would've otherwise, I think.
 *
 * Value gained:
 *  • practiced reducing a complex conditions problem down to a very narrow subset of conditions and a narrow question;
 *  • practiced solving a meta-occurrence problem using both an IntArray (small distinct elements range)
 *   and a HashMap (large distinct elements range).
 */
class DetermineIfTwoStringsAreClose {

    // TODO: optionally, retry in 1-2 weeks, I got the solution correct and quick, but was surprised still :D
    //  try out sorting instead of the meta-frequency

    /**
     * Problem rephrase:
     *  - given: strings word1 and word2
     *  - allowed operations:
     *   A. swap any two chars in the string;
     *   B. change all occurrences of one character into another EXISTING character, and all ITS occurrences into the first one.
     *    (BOTH WAYS!)
     *  - goal: return true if it is possible to transform word1 into word2 or vice versa using an unlimited amount of allowed operations.
     *   (note: transform one, but another word always stays UNCHANGED)
     *
     * observations:
     *  - neither operations changes string length => if words are of non-equal length, early return false;
     *  - neither operation changes the charset => if charsets of strings are not equal, early return false;
     *  - if number of occurrences is equal => order doesn't matter since we can swap any 2 chars unlim times, thus achieving
     *   any permutation;
     *  - if lengths and charsets are same BUT the number of OCCURS DIFFERS
     *   => the only operation to change the occurrences swaps it between different characters
     *   => if we have the same frequency of occurrences in both strings => we can swap these in between in one string
     *    to achieve the other, and only then.
     *
     * only lowercase english => use an array for counting.
     *
     * caabbba abbbccc
     *
     * c: 1
     * a: 3
     * b: 3
     *
     * a: 1
     * b: 3
     * c: 3
     *
     * a: 3
     * b: 3
     * c: 1
     *
     * edge cases:
     *  - word1.length == word2.length == 1 => changes nothing, correct as-is;
     *  - same number of occurrences BUT of characters from a NON-EQUAL charset between both strings => charset check first is essential.
     *
     * Time: always O(n + m)
     *  - counting occurs of chars in word1 O(n), word2 O(m);
     *  - charset check O(26);
     *  - counting frequency of occurs for word1 O(26), word2 O(26), cause we have only up to 26 unique characters in either string;
     *  - comparing two frequency maps O(26).
     *
     * Space: always O(1)
     *  - occurs1 is exactly 26 elements, and occurs2 too;
     *  - frequency1 worst is 26 elements, and occurs2 too.
     */
    fun efficient(word1: String, word2: String): Boolean {
        if (word1.length != word2.length) return false

        val occurs1 = word1.occurrences()
        val occurs2 = word2.occurrences()

        // charset check
        occurs1.forEachIndexed { ind, occur1 ->
            val occur2 = occurs2[ind]
            if ((occur1 == 0 && occur2 != 0) || (occur1 != 0 && occur2 == 0)) return false
        }

        val freqOccurs1 = occurs1.frequency()
        val freqOccurs2 = occurs2.frequency()
        return freqOccurs1 == freqOccurs2
    }

    private fun String.occurrences(): IntArray {
        val occurrences = IntArray(size = 26)
        forEach { char ->
            val key = char - 'a'
            occurrences[key] = occurrences[key] + 1
        }
        return occurrences
    }

    private fun IntArray.frequency(): Map<Int, Int> {
        val map = mutableMapOf<Int, Int>()
        forEach { number -> map[number] = map.getOrDefault(number, 0) + 1 }
        return map
    }
}
