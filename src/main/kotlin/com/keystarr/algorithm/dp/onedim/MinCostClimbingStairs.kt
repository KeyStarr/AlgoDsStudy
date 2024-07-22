package com.keystarr.algorithm.dp.onedim

import kotlin.math.min

/**
 * LC-746 https://leetcode.com/problems/min-cost-climbing-stairs/description/
 * difficulty: easy
 * constraints:
 *  - 2 <= cost.length <= 10^3
 *  - 0 <= cost\[i] <= 999
 *
 * Final notes:
 *  -
 *
 * Value gained:
 *  -
 */
class MinCostClimbingStairs {

    private val stepToMinCostCache = mutableMapOf<Int, Int>()

    /**
     * notable problem characteristics:
     *  - goal is to find min;
     *  - each step (literally)) we make a choice, which affects future choices:
     *      - if we take step X+1, we add its cost to the total AND we might make max next step to X+3 basically
     *      - if we take step X+2 we add its cost to the total AND we might make max next step to X+4
     *      (so, literally different future transition choices)
     * => we could try DP.
     *
     * also we have states and transitions => we could model this problem as an implicit graph. Then its asking
     * for the path with minimum total cost in a weighted directed acyclic graph => we could do either DFS or BFS with
     * multi-start (start both individually from nodes 0 and 1).
     *
     * let's start with trying a top-down DP:
     * (top-down for faster implementation, cause in hard cases it is)
     *
     * 1. the solution function signature:
     *  - function goal?
     *   solve the original problem => what's the minimum total cost for reaching ind==cost.length
     *  - return value?
     *   minimum total cost => max cost is 10^3*10^3 => 10^6 => use Int
     *  - input state?
     *   the original costs: IntArray
     *   currentStepInd: Int
     * 2. recursive case:
     *  - recursive relation?
     *   - we can reach the step denoted by currentStepInd only from currentStepInd-1 or currentStepInd-2, and we need
     *    to find the path with the min cost => greedily choose the direction which gives the minimum total cost)
     *   => min( minCost(currentStepInd-1) + costs[currentStepInd], minCost(currentStepInd-2) + costs[currentStepInd-2] )
     * 3. base cases?
     *  we could start either from 0th or 1st steps, so:
     *   - minCost(1) = 0, technically we could reach currentStepInd==1 from the 0th step, but since costs\[i] >= 0, that
     *    never gives a unique optimal solution.
     *   - minCost(0) = 0
     * 4. memoization?
     *  HashMap<Int,Int>, base case for retrieval + store the result of the recursive case before returning it
     *
     * Edge cases:
     *  - all cost\[i] == 0 => always return 0, works correctly; (could do an early return for O(n));
     *  - no sum overflow, worst total cost fits into Int.
     *
     * Time: always O(n)
     *  - due to memoization, we compute the result for each of `n=costs.length` steps exactly once => exactly n calls to [topDownDp]
     *  - the work for each step=[topDownDp] call is O(1)
     * Space: always O(n)
     *  - memoization cache takes O(n), since it caches each [topDownDp] call and there are exactly n calls.
     */
    fun topDownDp(costs: IntArray): Int = topDownDp(currentStepInd = costs.size, costs = costs)

    private fun topDownDp(
        currentStepInd: Int,
        costs: IntArray,
    ): Int {
        if (currentStepInd == 1 || currentStepInd == 0) return 0
        val cache = stepToMinCostCache[currentStepInd]
        if (cache != null) return cache

        val stepA = currentStepInd - 1
        val stepB = currentStepInd - 2
        val minCost = min(
            topDownDp(stepA, costs) + costs[stepA],
            topDownDp(stepB, costs) + costs[stepB],
        )
        stepToMinCostCache[currentStepInd] = minCost
        return minCost
    }

    /**
     *
     */
    fun bottomUpDp() {

    }

    /**
     *
     */
    fun weightedDfs() {

    }
}
