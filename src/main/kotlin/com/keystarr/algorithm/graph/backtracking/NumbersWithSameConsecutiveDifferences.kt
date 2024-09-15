package com.keystarr.algorithm.graph.backtracking

/**
 * ⭐️ (a weaker star, but still - good example for valid numbers generation)
 * LC-967 https://leetcode.com/problems/numbers-with-same-consecutive-differences/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= n <= 9
 *  • 0 <= k <= 9
 *
 * Final notes:
 *  • MISSED a GLARING edge case;
 *  • interesting, so, constructing the number from left to right (higher to lower digits) was much easier than from
 *   right to left, AND we could just store the current intermediate number as Int, no lists required => optimal solution!
 *   we could use Int for lower-to-higher digits construction too, but we'd need 10^currentDigitInd or to carry over
 *   current positionMultiplier and do positionMultiplier*10 each iteration (have it as an arg to recursion)
 *   -----
 *   TL;DR; for number generation consider going from higher-to-lower digits and simply currentNumber*10+newDigit to add
 *   the new digit. Plus using only Int as the intermediate number representation
 *  • wow, I ACTUALLY got the time estimate right! The 2nd time for backtracking!!! Reasoned from the tree height and then
 *   the max neighbors each node may have. It seems to follow a pattern, the upper time boundary for this types of problems,
 *   which is perfect for a quick under-the-stress big O (not theta obviously, theta time seems to be much harder and
 *   unrealistic during an interview).
 *
 * Value gained:
 *  • practiced backtracking for integer numbers generation => learnt to try starting from higher to lower digits and
 *   using just a number for an intermediate representation (at least it MAY be easier than starting from lower to higher
 *   generally, that's a hypothesis;
 *  • practiced estimating backtracking algo time => seems to be that the workToCollectCombination * heightOfTree^maxNeighborsForEachNode
 *   formula kinda works, or at least these focal points have proven so far to be good starting points for time estimation.
 */
class NumbersWithSameConsecutiveDifferences {

    private val directions = arrayOf(1, -1)

    /**
     * goal: generate all possible valid combinations => try backtracking
     *
     * neighbors generation / pruning:
     *  - generate one digit at a time, starting from the lowest => 1 number's digit = level of depth in the tree;
     *  - traverse only to nodes on the next level which have a value with ABSOLUTE diff of [k] to the current node's value;
     *      consider 0 as a valid neighbor only if current digit is not `n-1` (counting digits from 0)
     *  - stop when we have exactly [n] digits => add `current` combination to results.
     *
     * result an array => either use a dynamic array and then just convert into a static one OR pre-calculate the number of
     *  combinations and allocate the static array of that size OR determine an approximate upper boundary for combinations
     *  and create a static array of that size (and trim=copy it in the end? better just use a dynamic one then)
     *
     * what's the total combinations count formula based on solely [numberLength] and [absDiff]? quite complicated,
     * see "Time" section below. Just use a dynamic array and convert it in the end.
     *
     * how to build current?
     *  - Int => add new digits via digit*10^currentDigit, remove digits via /10
     *  - IntArray, add/remove but writing the digit to / zeroing the ith element. Final conversion via iteration
     *   and digit*10^currentDigit;
     *  - StringBuilder - creation of a String in the end, conversion to Int, meh
     *
     * edge cases:
     *  - 0 can only be used for non-leading digits;
     *  - k == 0 => a combination can have only 1 digit => consider only 1 direction then, not both to avoid duplicates;
     *  - since n > 1 => its always safe to use 0 as the first digit.
     *
     * Time: O(2^[numberLength])
     *  - the height of the decision tree is always [numberLength]
     *  - from one node in worst case we have 2 children => binary tree (but it can be 1 or 0 children as well, and
     *   leaves are not all on the same level)
     *  - when we arrive at the full valid combination, it's already a number, so we just insert it into the results ArrayList
     *   which takes amortized O(1) time.
     * Space: O([numberLength]) for the decision tree height, if we don't count results space
     *
     * ----
     * could be optimized for absDiff==0 case such that we won't remove from the currentNumber
     */
    fun efficient(numberLength: Int, absDiff: Int): IntArray = mutableListOf<Int>().apply {
        (1..9).forEach { firstDigit ->
            backtrack(
                currentNumber = firstDigit,
                digitsLeftCount = numberLength - 1,
                absDiff = absDiff,
                results = this,
            )
        }
    }.toIntArray()

    /**
     * construct [currentNumber] left-to-right (starting from higher order digits to lower)
     */
    private fun backtrack(
        currentNumber: Int,
        digitsLeftCount: Int,
        absDiff: Int,
        results: MutableList<Int>,
    ) {
        if (digitsLeftCount == 0) {
            results.add(currentNumber)
            return
        }

        val lastDigit = currentNumber % 10
        directions.forEachIndexed { ind, direction ->
            if (ind == 1 && absDiff == 0) return@forEachIndexed

            val newDigit = lastDigit + direction * absDiff
            if (newDigit !in 0..9) return@forEachIndexed

            backtrack(
                currentNumber = currentNumber * 10 + newDigit,
                digitsLeftCount = digitsLeftCount - 1,
                absDiff = absDiff,
                results = results,
            )
        }
    }
}

fun main() {
    println(
        NumbersWithSameConsecutiveDifferences().efficient(
            numberLength = 3,
            absDiff = 7,
        ).contentToString()
    )
}
