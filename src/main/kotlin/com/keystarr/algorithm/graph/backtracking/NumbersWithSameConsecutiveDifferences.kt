package com.keystarr.algorithm.graph.backtracking

/**
 *
 * constraints:
 *  - 2 <= n <= 9
 *  - 0 <= k <= 9
 *
 * Final notes:
 *  - MISSED a GLARING edge case!
 *
 * Value gained:
 *  -
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
     * Time: O([numberLength]*2^[numberLength])
     *  - the height of the decision tree is always [numberLength]
     *  - from one node in worst case we have 2 children => binary tree (but it can be 1 or 0 children as well, and
     *   leaves are not all on the same level)
     *  - when we reach the leaf, we copy the `current` array and add it into results => that takes O([numberLength]) time
     * Space: O([numberLength]) for the decision tree height, if we don't count results space
     *
     * ----
     * could be optimized for absDiff==0 case such that we won't remove from the currentNumber
     */
    fun solution(numberLength: Int, absDiff: Int): IntArray = mutableListOf<Int>().apply {
        (0..9).forEach { firstDigit ->
            backtrack(
                currentNumber = mutableListOf(firstDigit),
                targetDigits = numberLength,
                absDiff = absDiff,
                results = this,
            )
        }
    }.toIntArray()

    private fun backtrack(
        currentNumber: MutableList<Int>,
        targetDigits: Int,
        absDiff: Int,
        results: MutableList<Int>,
    ) {
        if (currentNumber.size == targetDigits) {
            results.add(currentNumber.toNumber())
            return
        }

        directions.forEachIndexed { ind, direction ->
            if (ind == 1 && absDiff == 0) return@forEachIndexed

            val newDigit = currentNumber.last() + direction * absDiff
            if (newDigit in 1..9 || (newDigit == 0 && currentNumber.size != targetDigits - 1)) {
                currentNumber.add(newDigit)
                backtrack(currentNumber, targetDigits, absDiff, results)
                currentNumber.removeLast()
            }
        }
    }

    private fun List<Int>.toNumber(): Int {
        var positionMultiplier = 1
        var result = 0
        for (digit in this) {
            result += digit * positionMultiplier
            positionMultiplier *= 10
        }
        return result
    }
}

fun main() {
    println(
        NumbersWithSameConsecutiveDifferences().solution(
            numberLength = 2,
            absDiff = 0,
        ).contentToString()
    )
}
