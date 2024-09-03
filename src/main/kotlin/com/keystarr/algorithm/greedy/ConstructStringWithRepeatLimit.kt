package com.keystarr.algorithm.greedy

import kotlin.math.min

/**
 * ⭐️ a great example of a greedy optimized string generation type problem
 * LC-2182 https://leetcode.com/problems/construct-string-with-repeat-limit/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= repeatLimit <= input.size <= 10^5;
 *  • input consists of only lowercase English.
 *
 * Final notes:
 *  • 🏆 done [efficient] by myself in 30-35 mins. Spent another 10 mins to prove the time complexity to be O(n)
 *  • 🔥started to observe, reduce the solution space and caught on the greedy choice straight away;
 *  • 🔥 design-phase caught the only major edge case here with hitting the repeatLimit and not having any other distinct chars left;
 *  • 🔥 almost straight away came up with the optimization to count chars using an IntArray to avoid sorting as we need to
 *   iterate in a descending alphabetic value order through the available chars. If we'd have more distinct chars, or they'd
 *   be more sparse => it'd better to use a map for counting and sort its entries for O(klogk) time.
 *
 * Value gained:
 *  • practiced solving the best valid string generation type problem using greedy and optimized char counting via an array.
 */
class ConstructStringWithRepeatLimit {

    /**
     * problem rephrase:
     *  - given:
     *   - input: String
     *   - repeatLimit: Int
     *  - goal: generate and return the lexicographically largest valid string
     *   valid = achieved via a permutation of [input], but with additional constraints:
     *    - each character can be no more than [repeatLimit] in a row;
     *    - we don't have to use all characters.
     *
     * lexicographically largest => the first pos to differ from any other valid strings got to be largest in the result string
     *
     * observations:
     *  1. we must use all available characters in the descending order alphabetically, but respecting the [repeatLimit].
     *   If we can't add the remaining largest character alphabetically due to the repeat constraint, add a single instance
     *   of the next largest available character, then use the initial largest char;
     *  2. we may not use all characters in [input] ONLY if by following the p.1 logic we arrive at the situation such that
     *   the only remaining characters are all instances of the same char, and current string ends with said character repeated
     *   exactly [repeatLimit] times.
     *   We don't use all chars, but since at every step we made the greedy choice of taking the maximum possible char
     *   to maximize the lexi value => that string is the largest lexi we can get.
     *
     * how to start?
     *  1. sort [input];
     *  2. or count all chars in [input] and then sort entries.
     *  actually, just use an int array for counting since we only have lowercase English letters in [input] => automatically
     *  get them in the sored order!
     *
     * edge cases:
     *  - input.length == 1 => always return input as result (a new instance is allowed) => correct as-is;
     *  - repeatLimit == 1 => nothing special, correct as-is;
     *  - repeatLimit == input.length => at each step append the max available amount of the largest char remaining =>
     *   min(repeatLimit, charToFreq\[i]) would always result in charToFreq\[i] => correct as-is;
     *  - we've used up the [repeatLimit] of the remaining largest char for the current streak, but there are no other
     *   chars left => check for this special condition => early return if so the result built so far.
     *
     * Time: average/worst O(n)
     *  - we have up to k = max distinct chars count, worst k=26 iterations of the outer loop;
     *  - we have up to g = (charFreq/repeatLimit) iterations of the 2nd inner loop. Worst is repeatLimit==1 and charFreq == input.size / 2
     *   and next char would have input.size / 2 instances for the repeat limit logic to work. But in total, across all
     *   outer loop iterations 1st inner loop has up to input.length iterations;
     *  - the 2nd inner loop sets the nextLargestRemainInd, and across all outer loop iterations it can only move up to through
     *   input.length elements => have up to n iterations => contributes only a const to O(n).
     * Space: always O(k + n) = O(26 + n) = O(n)
     *  - charToFreq array is always O(k), where k=max distinct chars count, here its always k=26;
     *  - string builder is up to n size.
     */
    fun efficient(input: String, repeatLimit: Int): String {
        val charToFreq = IntArray(size = 26)
        input.forEach { char ->
            val key = char.key()
            charToFreq[key] = charToFreq[key] + 1
        }

        val result = StringBuilder()
        var nextLargestRemainInd = -1
        for (i in charToFreq.size - 1 downTo 0) {
            if (charToFreq[i] == 0) continue

            val currentChar = i.char()
            while (charToFreq[i] > 0) {
                val toAddCount = min(repeatLimit, charToFreq[i])
                repeat(toAddCount) { result.append(currentChar) }
                charToFreq[i] = charToFreq[i] - toAddCount

                if (charToFreq[i] == 0) continue

                // we've hit the repeat limit for now, but still have largest char instances left => break the streak with the next
                // largest char, to start a new one
                if (nextLargestRemainInd == -1 || nextLargestRemainInd >= i) nextLargestRemainInd = i - 1
                while (nextLargestRemainInd >= 0 && charToFreq[nextLargestRemainInd] == 0) nextLargestRemainInd--
                if (nextLargestRemainInd == -1) return result.toString()
                charToFreq[nextLargestRemainInd] = charToFreq[nextLargestRemainInd] - 1
                result.append(nextLargestRemainInd.char())
            }
        }
        return result.toString()
    }

    private fun Char.key() = this - 'a'

    private fun Int.char() = 'a' + this
}

fun main() {
    println(
        ConstructStringWithRepeatLimit().efficient(
            input = "cczazcc",
            repeatLimit = 3,
        )
    )
}
