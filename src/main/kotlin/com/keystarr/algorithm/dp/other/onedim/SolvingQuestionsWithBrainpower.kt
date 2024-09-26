package com.keystarr.algorithm.dp.other.onedim

import kotlin.math.max

/**
 * LC-2140 https://leetcode.com/problems/house-robber/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= questions.length <= 10^5
 *  • question\[i].length == 2
 *  • 1 <= points\i, brainpower\i <= 10^5
 *
 * Final notes:
 *  • it's peculiar how we've started [topDown] from the 0th index and [bottomUp] from the (n-1) index, directly
 *   opposite to [HouseRobber], [LongestIncreasingSubsequence], [MinCostClimbingStairs]. Why? In all these problems
 *   our current choice affects future choices, it's just that in this particular problem the next valid node can be further
 *   at an unknown length + 🔥 the base case is actually at the (n-1) not at the 0th index;
 *  • why is the base case at (n-1)? Well, we might've started from the beginning of the array with the right index for topDown,
 *   like I tried with [topDownWrong], but I hit the wall there not able to get a correct solution.
 *
 * Value gained:
 *  • practiced both top-down and bottom-up DP;
 *  • apparently some DP problems are best to start top-down from the 0th index, and bottom-up from the end?
 *   And some the other way around? Why? TODO: why??? postponing cause I've hit a wall rn
 */
class SolvingQuestionsWithBrainpower {

    private val questionToMaxProfitMap = mutableMapOf<Int, Long>()

    /**
     * (WRONG APPROACH, see [topDown]. Left here for later reflection)
     * TODO: why does that approach not work? Is it generally wrong or did I make an implementation error?
     *
     * Analysis:
     *  - goal is max
     *  - we have to make multiple choices AND each choice affects further choices (since brainpower is always > 0)
     * => try DP
     *
     * Once again, the main bulk of the design is in the drawing in Obsidian.
     *
     * The core idea for top down is - we have a choice, whether to solve the ith question or not to take it:
     *  - if we solve it, then we must find the first question before it that has the brainpower such that, if we solve
     *   that jth question, we can still take the ith question => the answer would be q\[i].points + maxProfit(j)
     *  - if we skip the ith question, we need to check all questions in the range of [j+1:i-1] and find the best profit amongst
     *   them (cause if we take any of them, we won't be able to take the ith question, we need to consider that possibility).
     *
     * edge cases:
     *  - max points sum is when all brainpower is 1 => (10^5/2)*10.5 = 10^10/2 => doesn't fit into int, we need Long for the sum;
     *  - for ith question there is no question before it such that we can solve jth question and still take the ith =>
     *   than the answer to the current sub-problem is only the profit for the ith question.
     */
    fun topDownWrong(questions: Array<IntArray>): Long = topDownWrong(rightInd = questions.size - 1, questions)

    private fun topDownWrong(rightInd: Int, questions: Array<IntArray>): Long {
        if (rightInd == 0) return questions[0][0].toLong()

        val cachedResult = questionToMaxProfitMap[rightInd]
        if (cachedResult != null) return cachedResult

        var maxProfit = questions[rightInd][0].toLong()
        var j = rightInd - 2 // cause brainpower is at least 1
        while (j >= 0) {
            if (j + questions[j][1] + 1 <= rightInd) {
                maxProfit += topDownWrong(rightInd = j, questions)
                break
            }
            j--
        }

        for (k in j + 1 until rightInd) {
            val kMaxProfit = topDownWrong(rightInd = k, questions)
            if (kMaxProfit > maxProfit) maxProfit = kMaxProfit
        }

        questionToMaxProfitMap[rightInd] = maxProfit
        return maxProfit
    }

    /**
     * Time: O(n)
     * Space: O(n)
     */
    fun topDown(questions: Array<IntArray>): Long = topDown(leftInd = 0, questions)

    private fun topDown(leftInd: Int, questions: Array<IntArray>): Long {
        if (leftInd >= questions.size) return 0

        val cached = questionToMaxProfitMap[leftInd]
        if (cached != null) return cached

        val currentQuestion = questions[leftInd]
        val maxProfitWithCurrent = currentQuestion[0] + topDown(leftInd = leftInd + currentQuestion[1] + 1, questions)
        val maxProfit = max(
            maxProfitWithCurrent,
            topDown(leftInd = leftInd + 1, questions)
        )
        return maxProfit.also { questionToMaxProfitMap[leftInd] = it }
    }

    /**
     * Time: O(n)
     * Space: O(n)
     */
    fun bottomUp(questions: Array<IntArray>): Long {
        val rightIndToMaxProfit = LongArray(size = questions.size).apply {
            set(questions.size - 1, questions.last()[0].toLong())
        }
        for (rightInd in questions.size - 2 downTo 0) {
            val currentQuestion = questions[rightInd]
            val nextQuestionInd = rightInd + currentQuestion[1] + 1
            rightIndToMaxProfit[rightInd] = max(
                currentQuestion[0] + if (nextQuestionInd < questions.size) rightIndToMaxProfit[nextQuestionInd] else 0,
                rightIndToMaxProfit[rightInd + 1],
            )
        }
        return rightIndToMaxProfit[0]
    }
}

fun main() {
    println(
        SolvingQuestionsWithBrainpower().topDownWrong(
            questions = arrayOf(intArrayOf(3, 2), intArrayOf(4, 3), intArrayOf(4, 4), intArrayOf(2, 5))
        )
    )
}
