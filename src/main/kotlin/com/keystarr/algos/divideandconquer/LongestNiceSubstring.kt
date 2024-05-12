package com.keystarr.algos.divideandconquer

/* LC-1763 https://leetcode.com/problems/longest-nice-substring/description/
 easy
 constraints:
  1 <= s.length <= 100
  s consists of uppercase and lowercase English letters.
 no explicit time/space constraints.
 general thoughts:
    - the hardest part is that if we use a simple walkthrough approach whether the substring is nice may change
        multiple times when adding a new character! that invalidates, I think, a lot of tools;
    - I failed to come up with a way to crack it via D&C, couldn't find a base case at all, 0 ideas.
 */
class LongestNiceSubstring {

    /*
    hints (that I caught from description):
        1. since we need to tell if a string consists of a unique set of characters which may occur multiple times
            => that hints of a set, that's set behavior to check that efficiently
        2. we need to find a substring, order matters => probably go from left to right sequentially, loop
    time: ≈ O(n^3)
    space: O(n)
     */
    fun iterativeSlow(s: String): String {
        var lastNiceIndPair: Pair<Int, Int>? = null
        // O(n)
        (s.indices).forEach { startInd ->
            val currentSubstringSet = mutableSetOf<Char>()
            // on average O(n/2) ≈ O(n)
            (startInd until s.length).forEach { currentInd: Int ->
                val currentChar = s[currentInd]
                currentSubstringSet.add(currentChar)
                // isNice() takes at most O(n/2) ≈ O(n)
                if (currentSubstringSet.isNice() && (currentInd - startInd) > lastNiceIndPair.length()) {
                    lastNiceIndPair = startInd to currentInd
                }
            }
        }
        return lastNiceIndPair?.let { s.substring(it.first, it.second + 1) } ?: ""
    }

    private fun Set<Char>.isNice() = all {
        if (it.isUpperCase()) contains(it.lowercaseChar()) else contains(it.uppercaseChar())
    }

    /* time: O(n^2)
       space: O(n)
     */
    fun iterativeFaster(s: String): String {
        var lastNiceIndPair: Pair<Int, Int>? = null

        // time: O(n)
        (s.indices).forEach { startInd ->
            val currentCharsSet = mutableSetOf<Char>()
            val missingCharsSet = mutableSetOf<Char>()
            // time: on average O(n/2) ≈ O(n)
            (startInd until s.length).forEach { currentInd: Int ->
                // time: ≈O(1)
                val currentChar = s[currentInd]
                val matchChar = currentChar.reverseCase()
                currentCharsSet.add(currentChar)

                if (missingCharsSet.contains(currentChar)) missingCharsSet.remove(currentChar)
                if (!currentCharsSet.contains(matchChar)) missingCharsSet.add(matchChar)

                val isNice = missingCharsSet.isEmpty()
                if (isNice && (currentInd - startInd) > lastNiceIndPair.length()) {
                    lastNiceIndPair = startInd to currentInd
                }
            }
        }
        return lastNiceIndPair?.let { s.substring(it.first, it.second + 1) } ?: ""
    }

    private fun Pair<Int, Int>?.length() = this?.let { second - first } ?: 0

    /*
    Couldn't solve it myself, based on:
    https://leetcode.com/problems/longest-nice-substring/solutions/1645774/java-why-divide-conquer-explained/

    intuition: start with a recursive case first, no the base one. How can we reduce the problem? If there is a
        character without a match, then no substring containing it can be called nice + we need the first longest
        substring => find the first character with no match and split the string by it, apply the same pattern, stop
        when there is no such character (the substring is nice, which automatically would be the longest one in the current
        "half" of the string).

    time: O(n (recursive calls at worst) * n^2 (findFirstBlockerChar)) = O(n^3)
     */

    fun divideAndConquer(input: String): String {
        val firstBlockerCharInd = input.slowFindFirstBlockerCharInd()
        if (firstBlockerCharInd == -1) return input

        val left = if (firstBlockerCharInd == 0) "" else input.substring(0, firstBlockerCharInd)
        val right = if (firstBlockerCharInd == input.length - 1) {
            ""
        } else {
            input.substring(firstBlockerCharInd + 1, input.length)
        }

        val leftResult = divideAndConquer(left)
        val rightResult = divideAndConquer(right)

        return if (leftResult.length >= rightResult.length) leftResult else rightResult
    }

    // time: O(n^2)
    private fun String.slowFindFirstBlockerCharInd(): Int = indexOfFirst { !contains(it.reverseCase()) }

    /*
    An improved version of [divideAndConquer] above:
         - finding a blocker char costs O(n) time;
         - no string copy, do slices by indices (sliding window?).

     Worst case time-wise - each character is a blocker => we have to perform:
     O(n) + O(n-1) + O(n-2) + ...  + O(1) (checks)
     time: ≈O(n^2).
     */
    fun divideAndConquerFaster(input: String): String {
        val range = fasterDivideAndConquerIteration(
            input = input,
            startInd = 0,
            endInd = input.length,
        )
        return if (range.isNotEmpty()) input.substring(range[0], range[1]) else ""
    }

    private fun fasterDivideAndConquerIteration(
        input: String,
        startInd: Int,
        endInd: Int,
    ): IntArray {
        if (endInd - startInd < 2) return intArrayOf()

        val firstBlockerCharInd = input.fastFindFirstBlockerInd(startInd, endInd)
        if (firstBlockerCharInd == -1) return intArrayOf(startInd, endInd)

        val leftIndices = fasterDivideAndConquerIteration(
            input = input,
            startInd = startInd,
            endInd = firstBlockerCharInd,
        )
        val rightIndices = fasterDivideAndConquerIteration(
            input = input,
            startInd = firstBlockerCharInd + 1,
            endInd = endInd,
        )

        return if (leftIndices.length() >= rightIndices.length()) leftIndices else rightIndices
    }

    // time: O(n) + O(n) = O(n)
    private fun String.fastFindFirstBlockerInd(startInd: Int, endInd: Int): Int {
        val charset = charset(startInd, endInd)
        (startInd until endInd).forEach { ind ->
            if (!charset.contains(get(ind).reverseCase())) return ind
        }
        return -1
    }

    // time: O(n)
    private fun String.charset(startInd: Int, endInd: Int): Set<Char> {
        val set = HashSet<Char>(endInd - startInd)
        (startInd until endInd).forEach { set.add(get(it)) }
        return set
    }

    // time: O(1)
    private fun Char.reverseCase() = if (isUpperCase()) lowercaseChar() else uppercaseChar()

    private fun IntArray.length() = if (isEmpty()) 0 else this[1] - this[0]
}

fun main() {
    val solver = LongestNiceSubstring()
    println(solver.divideAndConquerFaster("XqLJWaEEcAjslIXxKZgufikxFwVVYUlvYrIeGRyS"))
}
