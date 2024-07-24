package com.keystarr.algorithm.dp.onedim

import kotlin.math.max

/**
 * LC-198 https://leetcode.com/problems/house-robber/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= nums.length <= 100
 *  - 0 <= nums\[i] <= 400
 *
 * Final notes:
 *  -
 *
 * Value gained:
 *  -
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
}
