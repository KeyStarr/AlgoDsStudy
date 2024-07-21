package com.keystarr.algorithm.graph.backtracking

/**
 * LC-216 https://leetcode.com/problems/combination-sum-iii/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= numbersAmount <= 9
 *  • 1 <= n <= 60
 *
 * Final notes:
 *  • solved by myself [efficient] in 20-30 mins;
 *  • very straightforward implementation (after realizing that we actually need subsets), the good ol' backtracking pattern;
 *  • time estimation is once again hard for me rn, BUT actually the final figure I saw in Editorial looks reasonable and
 *   sorta intuitive. I need to invest time to figure that idea out once and for all, how to estimate time for backtracking
 *   combinations with variable neighbors amounts (but predictable by a very concrete law). At some point later TODO,
 *   now is now that priority (the approximate should be ok for now, got more important tasks).
 *
 * Value gained:
 *  • a great example of a problem that implicitly asks for subsets combination => consider numbers only from n+1;
 *  • practiced backtracking on subsets generation problem.
 */
class CombinationSumIII {

    /**
     * generate all valid combinations => try backtracking
     *
     * USE EACH NUMBER ONLY ONCE!
     *
     * neighbor selection:
     *  - tree level = a number. Max tree height is [numbersAmount]
     *  - launch DFS from several starting points, from all numbers from 1 to 9
     *  - pruning: from number X traverse to all numbers from X+1 to min(9,targetSum-currentSum)
     *      why from X+1?
     *       For the leftmost branch its because all previous numbers were taken,
     *       For every other branch its because we care about only subsets of length [numbersAmount] that sum up to [targetSum],
     *        and, e.g. when going from 1 to 3 we never try 2, because we've already tried the subset (1,2,3) on the previous
     *        branch.
     *
     *  - stop at:
     *      - either currentSum equals to or exceeds the [targetSum];
     *      - or currentNumbers.size == [numbersAmount]
     *  - adding to results:
     *      - when we both reach the required numbers count AND the required sum => add currentNumbers to results.
     *
     * Edge cases:
     *  - n == 1 => if k==1 return 1, otherwise 0 => correct as-is;
     *  - n == 2 => if k==1 return 1, otherwise return 0 => correct as-is.
     *
     * // TODO: estimate time more precisely
     * Time: average/worst O(n*9^numbersAmount). Precise time is quite hard to calculate due to variable neighbors count
     *  - worst tree height is [numbersAmount]
     *  - a node can at worst have 8 neighbors (only for the first node with value 1), on average 9-node.value neighbors;
     *  - a single combination consists of multiple numbers => we use array to keep track of the current combination
     *    => when we reach a valid combination, we need to copy the array => that takes O([numbersAmount]) time
     *   => the approximate total amount of combinations (upper bound) is 9^numbersAmount (9 levels of depth, each previous node
     *    has 9 connections to the new level)
     *   => so each combination required O(n) work to add it, and 9^numbersAmount combinations => n*9^numbersAmount work
     * Space: O(numbersAmount) for the callstack height, if we don't count the results
     */
    fun efficient(numbersAmount: Int, targetSum: Int) = mutableListOf<List<Int>>().apply {
        for (firstNumber in 1..9) {
            backtrack(
                currentNumbers = mutableListOf(firstNumber),
                leftSum = targetSum - firstNumber,
                numbersAmount = numbersAmount,
                results = this,
            )
        }
    }

    private fun backtrack(
        currentNumbers: MutableList<Int>,
        leftSum: Int,
        numbersAmount: Int,
        results: MutableList<List<Int>>
    ) {
        if (currentNumbers.size == numbersAmount || leftSum <= 0) {
            if (currentNumbers.size == numbersAmount && leftSum == 0) results.add(ArrayList(currentNumbers))
            return
        }

        for (nextNumber in currentNumbers.last() + 1..9) {
            currentNumbers.add(nextNumber)
            backtrack(
                currentNumbers = currentNumbers,
                leftSum = leftSum - nextNumber,
                numbersAmount = numbersAmount,
                results = results,
            )
            currentNumbers.removeLast()
        }
    }

    // TODO: refactor to remove the need for multi-start DFS
}

fun main() {
    println(
        CombinationSumIII().efficient(numbersAmount = 2, targetSum = 3)
    )
}
