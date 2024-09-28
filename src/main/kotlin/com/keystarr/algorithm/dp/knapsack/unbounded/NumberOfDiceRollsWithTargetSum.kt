package com.keystarr.algorithm.dp.knapsack.unbounded

import kotlin.math.min

/**
 * LC-1155 https://leetcode.com/problems/number-of-dice-rolls-with-target-sum/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= diceAmount, maxFace <= 30;
 *  • 1 <= targetSum <= 10^3.
 *
 * Final notes:
 *  • done [topDownDp] by myself in 25 mins (with full time/space proof besides the why is worst k^n states is not possible);
 *  • ⚠️😆 I feel such he
 *  • done [bottomUp] at about 40 min mark;
 *  • ⚠️⚠️ got stuck on [bottomUpSpaceOptimized] for 20 mins: did the core idea quickly, but failed the 3rd test case
 *   kept failing: found 1 bug in like 15 mins, failed to find the others under 1h => checked the solution as per rules ⚠️
 *   (LOL, just missed the current[0]=0 after swapping for initial prev[0]==1, and we don't touch current[0] ever in the inner loop,
 *    but prev[0] must always be == 0 (base case leftSum==0 but not all nums used))
 *
 *  • 🔥 intuitively straight away went on to focus on each element's possible variations, since learned to do that recently for the
 *   valid combination (knapsack) type DP problems.
 *   ⚠️ but didn't recognize this as the knapsack type until reading
 *   https://leetcode.com/problems/number-of-dice-rolls-with-target-sum/solutions/355940/c-coin-change-2/
 *
 *   even though author claims this problem's closeness to CoinChangeII I disagree => I think it's closer to the 0-1 knapsack.
 *   thinking of it through the unbounded knapsack lens result in something similar to [topDownDpUnboundedKnaspack], which is not optimal.
 *   in [bottomUpSpaceOptimized] we focus on each slot, trying every possible valid value for it => this is closer
 *   to the 0-1 knapsack approach, its just that every slot is not static single value but a value of some set.
 *   so its closer to [com.keystarr.algorithm.dp.knapsack.zero_one.TargetSum2]
 *
 * Value gained:
 *  • practiced solving a "count all valid combinations (with multiple choices + consequences => subproblem overlapping"
 *   type problem using top-down/bottom-up space-optimized DP via a 0-1 knapsack pattern.
 */
class NumberOfDiceRollsWithTargetSum {

    /**
     * Only use 2 rows for caching.
     *
     * Time: same as [bottomUp] O(n*m*k)
     * Space: always O(m)
     */
    fun bottomUpSpaceOptimized(diceAmount: Int, maxFace: Int, targetSum: Int): Int {
        val rowSize = targetSum + 1
        var prev = IntArray(size = rowSize).apply { set(0, 1) }
        var current = IntArray(size = rowSize)
        for (dicesLeft in 1..diceAmount) {
            current[0] = 0 // cause initial prev[0] == 1, but after the first swap [0] must be always == 0
            for (leftSum in targetSum downTo 1) {
                current[leftSum] = 0
                for (face in 1..min(maxFace, leftSum)) {
                    current[leftSum] = (current[leftSum] + prev[leftSum - face]) % MODULO
                }
            }

            val temp = prev
            prev = current
            current = temp
        }
        return prev[targetSum]
    }

    /**
     * since to compute state with diceLeft=X we rely only on the state diceLeft==X+1 in [topDownDp] => we might do bottom-up
     *  with O(m) space only using 2 or even 1 row.
     *
     * although iterative bottom-up would have one time const advantage on recursive with no callstack,
     *  but it would compute all n*m states, even the non-reachable ones from the input => potentially it would have
     *  slower time const, but same asymptotic time-wise.
     *
     * and maybe there's an even better approach/solution with even better time asymptotic, trickier
     *
     * try doing the bottom-up for sport and practice (could be asked to do it in a real interview though)
     *
     * -----
     *
     * Let's convert the [topDownDp] as-is first.
     */
    fun bottomUp(diceAmount: Int, maxFace: Int, targetSum: Int): Int {
        // base cases cache[0] are set to 0 implicitly + base cases for cache[diceAmount-1:0][0] to 0 as well
        val cache = Array(size = diceAmount + 1) { IntArray(size = targetSum + 1) }
        cache[0][0] = 1 // base case when we have used up all dices and all sum up to exactly [targetSum]

        for (dicesLeft in 1..diceAmount) {
            for (leftSum in targetSum downTo 1) {
                for (face in 1..min(maxFace, leftSum)) {
                    cache[dicesLeft][leftSum] =
                        (cache[dicesLeft][leftSum] + cache[dicesLeft - 1][leftSum - face]) % MODULO
                }
            }
        }
        return cache[diceAmount][targetSum]
    }

    /**
     * problem rephrase:
     *  - given:
     *   - diceAmount: Int
     *   - maxFace: Int
     *   - targetSum: Int
     *  goal: return the normalized amount of all valid combinations
     *   combination = always diceAmount elements, each element equals to [1,maxFace]
     *   valid = the sum of the combination equals targetSum
     *   normalized = answer % (10^9 + 7)  (so that it fits into Int)
     *
     * count all of something = we could try either backtracking or DP
     * since we don't need all actual combinations => try DP first? if its possible it will potentially be more optimal
     *
     * try top-down DP
     *
     * goal: the original goal
     * input state:
     *  - startInd: Int, denoting the amount of elements left
     *  - leftSum: Int
     *
     * recurrence relation:
     *  we must always take each element, the question is only with what face=value? => for each element try all possible values ([1,maxFace],
     *   sum all the results (since we need the total number of combinations across all possible)
     *
     *  dp(startInd, leftSum) = moduloSum( for face in 1..maxFace: dp(startInd+1,leftSum-face)
     *
     * base cases:
     *  - startInd==diceAmount:
     *   - if leftSum == 0:
     *    - return 1
     *   - else return 0
     *  - if leftSum <= 0: return 0
     *
     * cache: try a 2D array since both startInd and leftSum are reasonably low (30, 1000).
     *
     * Time: average O(n*m*k), worst O(k^n)
     *  - we have ~n*m states, where n=diceAmount, m=targetSum;
     *   (not all states are reachable, obviously)
     *  - at each state we do k=maxFace work.
     *  => will it pass? max units of computations roughly is 30 * 1000 * 30 = 9 * 10^5 => yep, will pass under 1 sec (10^9 is max, right?
     *
     *  - tree height is n, potentially worst (full) tree width is k^n=30^30=oh,that wouldn't pass though wouldn't it))
     *
     *   but is it really possible? then targetSum would be greater than the sum of any path in the tree => we'd have to try
     *   all combinations. max path sum is all max faces * n = 30*30=900, targetSum=1000, so that is possible then
     *
     *   ? but is it possible that there would be no cache hits, that all k^n states would be unique???
     *    let's implement and see, I'm getting too unnecessarily complicated, no?
     *
     *
     * Space: always O(n*m)
     *  - cache takes always O(n*m);
     *  - worst callstack height is n.
     *
     * ----------- modulo arithmetics dry run check
     *
     *  10 + 5 + 3 = 18
     *
     *  %3
     *
     *  10%3=1
     *  (1+5)%3=0
     *  (3+0)%3=0
     *
     *  18%3=0
     */
    fun topDownDp(diceAmount: Int, maxFace: Int, targetSum: Int): Int =
        topDownDpRecursive(
            diceLeft = diceAmount,
            leftSum = targetSum,
            maxFace = maxFace,
            cache = Array(size = diceAmount + 1) { IntArray(size = targetSum + 1) { CACHE_EMPTY_VAL } }
        )

    private fun topDownDpRecursive(
        diceLeft: Int,
        leftSum: Int,
        maxFace: Int,
        cache: Array<IntArray>,
    ): Int {
        if (diceLeft == 0) return if (leftSum == 0) 1 else 0

        val cachedResult = cache[diceLeft][leftSum]
        if (cachedResult != CACHE_EMPTY_VAL) return cachedResult

        var totalCombinations = 0
        for (face in 1..min(maxFace, leftSum)) {
            val combinations = topDownDpRecursive(
                diceLeft = diceLeft - 1,
                leftSum = leftSum - face,
                maxFace, cache,
            )
            totalCombinations = (totalCombinations + combinations) % MODULO
        }
        return totalCombinations.also { cache[diceLeft][leftSum] = it }
    }

    /**
     * (WRONG)
     *
     * Tried more of an Unbounded Knapsack focused approach after solving via [bottomUpSpaceOptimized] as a learning experiment.
     *
     * Fails on 2nd and 3rd test cases, failed to fix fast enough, let it go for now.
     */
    fun topDownDpUnboundedKnaspack(diceAmount: Int, maxFace: Int, targetSum: Int): Int =
        topDownDpRecursiveUnboundedKnapsack(
            diceLeft = diceAmount,
            leftSum = targetSum,
            currentFace = 1,
            maxFace = maxFace,
            cache = Array(size = diceAmount + 1) { Array(size = maxFace + 1) { IntArray(size = targetSum + 1) { CACHE_EMPTY_VAL } } },
        )

    private fun topDownDpRecursiveUnboundedKnapsack(
        diceLeft: Int,
        leftSum: Int,
        currentFace: Int,
        maxFace: Int,
        cache: Array<Array<IntArray>>,
    ): Int {
        if (diceLeft == 0) return if (leftSum == 0) 1 else 0
        if (leftSum <= 0 || currentFace == maxFace + 1) return 0

        val cachedResult = cache[diceLeft][currentFace][leftSum]
        if (cachedResult != CACHE_EMPTY_VAL) return cachedResult

        val take = topDownDpRecursiveUnboundedKnapsack(
            diceLeft = diceLeft - 1,
            leftSum = leftSum - currentFace,
            currentFace = currentFace,
            maxFace, cache,
        )
        val skip = topDownDpRecursiveUnboundedKnapsack(
            diceLeft = diceLeft,
            leftSum = leftSum,
            currentFace = currentFace + 1,
            maxFace, cache
        )
        return (take + skip).also { cache[diceLeft][currentFace][leftSum] = it }
    }
}

private const val CACHE_EMPTY_VAL = -1
private const val MODULO = 1_000_000_000 + 7
