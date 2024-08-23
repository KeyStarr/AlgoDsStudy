package com.keystarr.algorithm.array.twopointers

/**
 * LC-917 https://leetcode.com/problems/reverse-only-letters/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= text.length <= 100
 *  • text consists of any valid ASCII values in range of [33,122] and doesn't contain '\"' or '\\'
 *
 * Final notes:
 *  • done [tempList] in 10 mins by myself;
 *  • tried doing [twoPointers], failed to come up with a good approach in 10 mis => learned the core ideas of
 *   https://leetcode.com/problems/reverse-only-letters/solutions/178419/java-c-python-two-pointers-one-pass/
 *   and implemented the rest. Decided it's not worth to spend more time figuring out an easy problem, since probably
 *   (maybe I'm wrong) [tempList] would be OK in the real interview, since in most cases an easy problem would be a warmup.
 *   But still decided to practice two pointers.
 *
 * Value gained:
 *  • practiced two pointers on a string partial reversal problem.
 *   Really, should remember that its ok to move just 1-2 pointers at a single iterations, not necessary to just
 *    scroll through both with inner while loops. Also a neat trick to pre-add the entire text into the builder, so we
 *    dont need to append the non-target chars.
 */
class ReverseOnlyLetters {

    // TODO: retry two pointers in 1-2 weeks

    // could've also used a hashset with all these characters added
    private val targetRanges = arrayOf(
        intArrayOf('a'.code, 'z'.code),
        intArrayOf('A'.code, 'Z'.code),
    )

    /**
     * target characters = English alphabet
     *
     * naive idea:
     *  - iterate through the string, add all target characters into a list;
     *  - create result: StringBuilder;
     *  - iterate through the string backwards, if the current character is a target one, then instead of it append
     *   the currentInd char from the targetList; otherwise append the current character.
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     *  - worst: all characters are target => the list is n
     */
    fun tempList(text: String): String {
        val targetChars = mutableListOf<Char>()
        text.forEach { char -> if (char.isTarget()) targetChars.add(char) }

        val result = StringBuilder()
        var targetInd = targetChars.size - 1
        text.forEach { char ->
            if (char.isTarget()) result.append(targetChars[targetInd--]) else result.append(char)
        }
        return result.toString()
    }

    /**
     * Design:
     *  1. add the entire [text] into the string builder => all the non-target chars would already be set, we don't need to change them;
     *  2. iterate, at each step move by at most 2 chars. Move left, then right until both point at the target characters
     *   => update the builder at both positions reversed, move the pointers.
     *
     * Time: always O(n)
     * Space: always O(1) if not counting the result
     */
    fun twoPointers(text: String): String {
        val result = StringBuilder(text)
        var left = 0
        var right = text.length - 1
        while (left < right) {
            when {
                !text[left].isTarget() -> left++
                !text[right].isTarget() -> right--
                else -> {
                    result[left] = text[right]
                    result[right++] = text[left++]
                }
            }
        }
        return result.toString()
    }

    private fun Char.isTarget() = targetRanges.any { range -> code in range[0]..range[1] }
}

fun main() {
    println(
        ReverseOnlyLetters().twoPointers("ab-cd")
    )
}
