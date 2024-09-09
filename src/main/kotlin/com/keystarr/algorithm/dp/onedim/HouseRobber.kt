package com.keystarr.algorithm.dp.onedim

import kotlin.math.max

/**
 * LC-198 https://leetcode.com/problems/house-robber/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.length <= 100
 *  • 0 <= nums\[i] <= 400
 *
 * Final notes:
 *  • hm, still, the author of the course designed the solution basically from bottom-up, and so did I. Why implement
 *   top-down then preferred though? Here I'd say bottom-up would've been even easier to translate from the design to the
 *   implementation;
 *  • I feel SO GREAT drawing 'em cases on the digital. Wow. Same satisfaction when I hit the pen and see a highlight
 *   circle drawn around the number as I've had with watching these problem solutions reviews, only better!
 *  • curious, this problem's recurrence equation is almost exactly like the [MinCostClimbingStairs]: looking either
 *   1 or 2 options behind and finding max/min with the added cost of the current step. Do most DP problems share that
 *   structure, or is it like just the tiny bit?
 *
 * Value gained:
 *  • practiced both top-down and bottom-up DP on an intro problem to the subject with only 2 cumulative paths options
 *   to choose from when looking backward.
 */
class HouseRobber {

    private val rightIndToMaxProfitMap = mutableMapOf<Int, Int>()

    /**
     * notable characteristics:
     *  - we must make a choice of element at a step, and that choice affects further choices;
     *   (if we take house X, then we can no more take houses X-1 and X+1)
     *  - goal: find max (max money collected along all robbed houses)
     * => try dp
     *
     * design a top-down solution. Core idea - gradually reduce the size of the problem to base cases, then build up
     * the total solution from those gradually using the recurrence relation.
     *
     * 1. solution function signature:
     *  - goal: what is the maximum profit we can rob out of the given houses?
     *  - input:
     *   - houseProfit: IntArray
     *   - rightInd: Int - the index which detonates the right boundary of the subarray (inclusive) of houseProfit[0:rightInd]
     *    such that we must provide the solution for it.
     * 2. base cases:
     *  - 1 house => return it's profit, nothing to choose from
     *  - 2 houses => choose the house with most profit, return it
     * 3. recursive:
     *  - recurrence relation => we can choose to either rob the current house AND take the max profit up until the currentHouseInd-2
     *   or rob the house currentHouseInd-1, and we want the max profit
     *   => max(dp(rightInd-2) + houseProfit\[rightInd], dp(rightInd-1)
     * 4. since the subproblems may overlap => use memoization to guarantee no redundant computation. Use HashMap<Int,Int>
     *
     * Edge cases:
     *  - [houseProfits].length == 1 or .length == 2 => hit the base cases on the first call to [topDownDp] and return via O(1).
     *
     * Time: O(n) since we compute exactly 1 recursive relation for each value of rightInd from 2 to n-1
     *  (and base cases, including calls using memoization and the work besides recursive calls in the recursive case
     *   are all O(1))
     * Space: O(n) for the hashmap + callstack
     */
    fun topDownDp(houseProfits: IntArray): Int = topDownDp(rightInd = houseProfits.size - 1, houseProfits)

    private fun topDownDp(rightInd: Int, houseProfits: IntArray): Int {
        if (rightInd == 0) return houseProfits[0]
        if (rightInd == 1) return max(houseProfits[0], houseProfits[1])

        val cachedResult = rightIndToMaxProfitMap[rightInd]
        if (cachedResult != null) return cachedResult

        return max(
            topDownDp(rightInd - 1, houseProfits),
            topDownDp(rightInd - 2, houseProfits) + houseProfits[rightInd]
        ).also { result -> rightIndToMaxProfitMap[rightInd] = result }
    }

    /**
     * Since we need only the results from 2 previous calls => use variables to store these, no need for the entire cache
     * of n calls (could also optimize [topDownDp] like that, but decided to skip for now)
     */
    fun bottomUp(houseProfits: IntArray): Int {
        val firstHouse = houseProfits.first()
        if (houseProfits.size == 1) return firstHouse

        var prevPrev = firstHouse
        var prev = max(firstHouse, houseProfits[1])
        for (rightInd in 2 until houseProfits.size) {
            val buffer = prev
            prev = max(prev, prevPrev + houseProfits[rightInd])
            prevPrev = buffer
        }
        return prev
    }
}

fun main(){
    println(HouseRobber().bottomUp(

        houseProfits = intArrayOf(1, 3, 1, 3, 100),
    ))
}
