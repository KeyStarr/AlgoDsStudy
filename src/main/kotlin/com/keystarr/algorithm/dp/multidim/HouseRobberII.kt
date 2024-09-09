package com.keystarr.algorithm.dp.multidim

import kotlin.math.max

/**
 * LC-213 https://leetcode.com/problems/house-robber-ii/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= houses.size <= 100;
 *  • 0 <= houses\[i] <= 10^3.
 *
 * Final notes:
 *  • done both [topDownDp] and [bottomUpFromRight] by myself in 40 mins;
 *  • ⚠️⚠️ done [bottomUpFromLeft] after learning the core concept from the solutions on leet. I completely forgot ⚠️ that we
 *   could simply go bottom-up left-to-right instead of right-to-left for O(1) space! Didn't even enter my mind, I just
 *   thought that for right-to-left O(1) space doesn't seem achievable (is it though?);
 *  • funny how can solve DP problems left-to-right AND right-to-left but the optimal solution appears to be only in a single
 *   of these directions.
 *
 * Value gained:
 *  • practiced solving a multi-dim (2D) DP problem using a bottom-up space-optimized approach, optimized gradually from top down.
 */
class HouseRobberII {

    // TODO: retry in 1-2 weeks, try to choose the bottom-up direction correctly first-try

    /* BOTTOM UP */

    fun bottomUpBest(houses: IntArray): Int {
        TODO("Understand and design the best solution with 1 pass, e.g. https://leetcode.com/problems/house-robber-ii/solutions/59934/simple-ac-solution-in-java-in-o-n-with-explanation/")
    }

    /**
     * Unlike [bottomUpFromRight] we increase the subproblem from the left and base cases are then just the houses.size == 1
     * (just the first element) and the houses.size == 2 (the max of the first two then) and we build on top of these then.
     *
     * Very much the same solution as [HouseRobber] but we take the max of two major cases due to the circular constraints:
     *  1. allow taking the first house, but never take the last house;
     *  2. always skip the first house, but allow taking the last house.
     *
     * Quite excessive, since in the 1st case skipping the first house is usually not the best strategy, since we can't
     *  consider the last house then, but we do the same AND consider the last house in the second case. And vice versa
     *  for the second case concerning the first house => the const could be optimized.
     *
     * Edge cases:
     *  - houses.size == 1 => always return houses[0] => early return not to crash;
     *  - houses.size == 2 => always return max(houses[0], houses[1]) => early return to avoid crashing.
     *
     * Time: always O(n)
     * Space: always O(1)
     *
     * By going from left to right we improved the space from O(n) to O(1) compared to [bottomUpFromRight].
     *
     * ------------
     *
     * Learned the core idea from some comment in solutions, designed the rest myself + simplified thanks to
     * https://www.youtube.com/watch?v=rWAJCfYYOvM&ab_channel=NeetCode (init prev/prevPrev at 0 and handle size == 1 edge case)
     */
    fun bottomUpFromLeft(houses: IntArray): Int =
        if (houses.size == 1) {
            houses[0]
        } else {
            max(
                simpleRob(houses = houses, startInd = 0, endInd = houses.lastIndex - 1),
                simpleRob(houses = houses, startInd = 1, endInd = houses.lastIndex),
            )
        }

    private fun simpleRob(houses: IntArray, startInd: Int, endInd: Int): Int {
        var prevPrev = 0
        var prev = 0
        for (i in startInd..endInd) {
            val currentHouse = houses[i]
            val prevBuffer = prev
            prev = max(prevPrev + currentHouse, prev)
            prevPrev = prevBuffer
        }
        return max(prev, prevPrev)
    }

    /**
     * Same complexity as [topDownDp], but less const due to no callstack space.
     */
    fun bottomUpFromRight(houses: IntArray): Int {
        val dp = Array(size = 2) { IntArray(size = houses.size + 2) }
        dp[0][houses.lastIndex] =
            houses.last() // base case, last house, but we didn't take the first (if we did, its always 0)

        for (leftInd in houses.lastIndex - 1 downTo 1) {
            for (isFirstChosen in 0..1) {
                dp[isFirstChosen][leftInd] = max(
                    houses[leftInd] + dp[isFirstChosen][leftInd + 2],
                    dp[isFirstChosen][leftInd + 1],
                )
            }
        }
        return max(dp[0][1], houses[0] + dp[1][2])
    }

    /* TOP DOWN */

    private lateinit var houses: IntArray
    private lateinit var cache: Array<IntArray>

    /**
     * 1 2 3 4 5
     * if we rob house 1, we cant rob house 2
     * if we don't rob house 1, we can rob house 2, but cant rob house 3
     * etc
     *
     * we are tasked to make the best valid combination + each choice affects further choices + subproblems overlap
     *  because, e.g. if we rob house 1 and then skip house 3 and rob house 4 instead OR if we rob houses 2 and 4 we have the
     *  same subproblem of finding the best valid combination of houses[4:], solving a reduced problem
     * => try DP
     *
     * start with top-down:
     *  - goal: find the best valid combination
     *   valid = we cant choose adjacent elements, including the last element being adjacent to the first one
     *   best = max sum of chosen elements
     *   return value: Int (max sum 100*1000=10^5 fits into Int)
     *
     *  - input state:
     *   - leftInd: Int denoting the start of the subarray of the current subproblem's input;
     *   - isFirstChosen: Boolean, denoting whether the first house was chosen, to respect the circular constraint.
     *
     *  basically for each house we can either take it or not, also respecting the constraint given by the previous choices
     *
     * but the trick is: how to respect that circular constraint???????
     *  1 2 3 4 5
     *  if we have only leftInd as the input => we cant check that in case we choose 1 that we cant choose 5!
     *  and it does limit only 5/1 combination
     *  => maybe just add a flag to the input whether the first element was taken? make it 2D
     *
     * - recurrence relation:
     *  dp(lI, iFC) = max( houses\[lI] + dp(lI+2, iFC), dp(lI+1, iFC)
     *
     * - base cases:
     *  - lI >= houses.size || isFirstChosen && lI == houses.lastIndex => return 0
     *
     * Time: always O(n)
     * Space: always O(n)
     */
    fun topDownDp(houses: IntArray): Int {
        this.houses = houses
        this.cache = Array(size = 2) { IntArray(size = houses.size) { -1 } }
        return max(
            houses[0] + topDownRecursive(leftInd = 2, isFirstChosen = true),
            topDownRecursive(leftInd = 1, isFirstChosen = false),
        )
    }

    private fun topDownRecursive(
        leftInd: Int,
        isFirstChosen: Boolean,
    ): Int {
        if (leftInd >= houses.size || (isFirstChosen && leftInd == houses.lastIndex)) return 0

        val isFirstKey = isFirstChosen.toInt()
        val cached = cache[isFirstKey][leftInd]
        if (cached != -1) return cached

        return max(
            houses[leftInd] + topDownRecursive(leftInd = leftInd + 2, isFirstChosen = isFirstChosen),
            topDownRecursive(leftInd = leftInd + 1, isFirstChosen = isFirstChosen)
        ).also { result -> cache[isFirstKey][leftInd] = result }
    }

    private fun Boolean.toInt() = if (this) 1 else 0
}

fun main() {
    println(
        HouseRobberII().bottomUpFromLeft(
            houses = intArrayOf(100),
        )
    )
}
