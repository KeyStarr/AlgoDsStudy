package com.keystarr.algorithm.dp.onedim

import kotlin.math.min

/**
 * LC-746 https://leetcode.com/problems/min-cost-climbing-stairs/description/
 * difficulty: easy
 * constraints:
 *  • 2 <= cost.length <= 10^3
 *  • 0 <= cost\[i] <= 999
 *
 * Final notes:
 *  • consciously solved my 1st DP problem ever, yaaaaay!!!!! :D
 *  • bottom up might (hypothesis) generally be just what we do with recursion, but on the backtracking way when
 *   we are actually returning and finishing calls, popping from the callstack;
 *  • the pattern for both the top-down DP and to bottom-up conversion fits fine here as-is.
 *
 * Value gained:
 *  • practiced DP both top-down and bottom-up;
 *  • is bottom-up DP always basically the same as what we do with top-down when backtracking and popping calls from the stack?
 */
class MinCostClimbingStairs {

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
     *  since we pre-hand know exactly the amount of states and its fairly small (worst is 1000) => we could optimize using
     *  simply the IntArray, where index is the step and cache\[currentStepInd] is the minCost to get to said step.
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
    fun topDownDp(costs: IntArray): Int = topDownDp(
        currentStepInd = costs.size,
        stepToMinCostCache = IntArray(size = costs.size + 1) { -1 },
        costs = costs
    )

    private fun topDownDp(
        currentStepInd: Int,
        stepToMinCostCache: IntArray,
        costs: IntArray,
    ): Int {
        if (currentStepInd == 1 || currentStepInd == 0) return 0
        val cachedCost = stepToMinCostCache[currentStepInd]
        if (cachedCost != -1) return cachedCost

        val stepA = currentStepInd - 1
        val stepB = currentStepInd - 2
        val minCost = min(
            topDownDp(stepA, stepToMinCostCache, costs) + costs[stepA],
            topDownDp(stepB, stepToMinCostCache, costs) + costs[stepB],
        )
        stepToMinCostCache[currentStepInd] = minCost
        return minCost
    }

    /**
     * Same core idea, just computing min cost for reach step iteratively starting from the bottom (first steps).
     * Basically its exactly what we do when we backtracked computing the recursive tree from bottom to top.
     */
    fun bottomUpDp(costs: IntArray): Int {
        val minCostPerStep = IntArray(size = costs.size + 1) { 0 }
        for (stepInd in 2 until minCostPerStep.size) {
            val stepA = stepInd - 1
            val stepB = stepInd - 2
            minCostPerStep[stepInd] = min(
                minCostPerStep[stepA] + costs[stepA],
                minCostPerStep[stepB] + costs[stepB],
            )
        }

        return minCostPerStep[costs.size]
    }

    fun weightedDfs() {
        // TODO: try doing, for funsies
    }
}

fun main() {
    println(
        MinCostClimbingStairs().bottomUpDp(
            intArrayOf(10, 15, 20),
        )
    )
}
