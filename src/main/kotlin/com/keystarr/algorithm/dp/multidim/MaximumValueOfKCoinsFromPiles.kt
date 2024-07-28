package com.keystarr.algorithm.dp.multidim

import kotlin.math.min

/**
 * LC-2218 https://leetcode.com/problems/maximum-value-of-k-coins-from-piles/description/
 * difficulty: hard
 * constraints:
 *  • 1 <= n < 10^3
 *  • 1 <= piles\[i]\[j] <= 10^5
 *  • 1 <= maxCoins <= sum(piles\[i].length) <= 2000
 *   max we can take all of the coins available (or 2000 if there are more than 2000 coins)
 *
 * Final notes:
 *  • failed to solve the problem by myself in 15-30 mins, chose the approach of taking exactly 1 coin from any pile
 *   until there are no coins left in piles, or no coins are allowed to be taken => that gives O(k*n^2) time complexity,
 *   too slow. Realized I couldn't see the correct approach, so just checked the article since it was the 1st learning phase;
 *  • read the core idea of the solution - just to move by 1 pile forward, and at each pile take up to coinsLeft - and
 *   designed the rest myself successfully;
 *  • FINALLY, first time on multidim DP both top-down and bottom-up solutions 1st attempt submit! Remember about the memoization,
 *   heh, and checked all the edge cases and bases cases especially for the bottom-up;
 *  • ⚠️ so, I've failed to come up within reasonable time with an efficient approach (state/a single step) for DP in both
 *   [MaximumValueOfKCoinsFromPiles] and [BestTimeToBuyAndSellStockIV]. I came up with something at all reminiscent of DP,
 *   which is better than pure bruteforce [LongestCommonSubsequence] (or was that still a form of bruteforce???), but still,
 *   what's the general point I'm missing here???
 *   🔥 what's the key, how to faster understand exactly what the efficient input state is, efficient single step?
 *
 * Value gained:
 *  • I need more practice with both multidim and singledim DP, I must figure out the intuition/core focus points/clues on how
 *   faster find the efficient input state/single step OPTIONS;
 *  • in general the framework is working really well, both the hints and the steps are a great structure. Once the input
 *   state / single step OPTIONS are figured out, the rest I do reliably so far. 🔥 Attention focus points, and their order
 *   in that framework are great;
 *  • practiced solving a multidimensional DP problem with both top-down and bottom-up.
 */
class MaximumValueOfKCoinsFromPiles {

    /**
     * WRONG APPROACH, too slow (by a factor of `n`)
     *
     * -------------
     *
     * problem rephrase:
     *  - given:
     *   - List of List<Int>, where each list is a stack of coin in the order top-to-bottom. Each number represents that
     *    coin's value;
     *   - so basically e.g. to get to coin piles[0][1], we need to first "take" the coin piles[0][0];
     *   - maxCoins: Int - max amount of coins that we can take.
     *  - goal: return the max total value of coins that we can take.
     *
     * analysis:
     *  1. goal is max
     *  2. we achieve the goal by making multiple choices/step, basically answer is derived from the best combination;
     *  3. each choice affects further choices, since if we take coin with ind X, we can now take coin with ind X+1 from that pile,
     *   (otherwise we can't) and take over all 1 less coin total.
     * => try DP
     *
     * - what is the function goal?
     *  return the max total value of coins taken, integer
     *
     * - what is the input = a single state?
     *  - an array of indices `coinsLeftInd`, where coinsLeftInd\[i] means that all coins from 0 to that value - 1
     *   of the pile i are already taken => coinsLeftInd\[i] is accessible currently from that pile;
     *  - leftChoices: Int - how many coins can we still take.
     *
     * - recurrence equation, how do we do one step efficiently?
     *  we have to take at least one coin, min coin value > 0, so any available coin would contribute to the goal
     *  - brute?
     *   start dp on each coin available from every pile
     *   maxCoins*n^2 states => too slow?
     *   dp(coinsLeftInd) = max (for i in coinsLeftInd:
     *      // if the coinsLeftInd[i] is outside the bounds of the pile[i] => base case, continue
     *      coinsLeftInd[i]++
     *      piles[i][coinsLeftInd\[i]-1] + dp(coinsLeftInd)
     *    )
     *   => TODO: is this even DP or not? in this approach there seems to be no subproblem overlap at all
     *
     * - base cases:
     *  - all piles are empty => return 0
     *  - coinsLeft == 0 => return 0
     *
     *  - optimize?
     *   X take only the largest coin?
     *    consider case (1,9999), (100, 4) k = 2, we'd take 100 + 4, but actual max is 1+9999
     *
     */
    fun confusedTopDownDp() {}

    /**
     * motivation for the DP is same as in [confusedTopDownDp], but the approach is different.
     *
     * - what is the goal?
     *  find the max total value of coins
     *
     * - whats the input = a single state?
     *  depends on how we do a step.
     *  let's try taking, in a loop, from 0 to [maxCoins] from pile\[i], after each step call dp on the remaining piles.
     *  - leftInd: Int - the start boundary of the subarray of the original piles, such that piles[0:leftInd] is the current subproblem;
     *  - coinsLeft: Int - how many coins we still can take
     *  - piles: List<List<Int>>
     *
     * - recurrence equation:
     *   dp(leftInd, coinsLeft) = max(for each takeCoins from 0 to coinsLeft:
     *      dp(leftInd=1, coinsLeft - takeCoins) + sum(pile\[leftInd][0:takeCoins])
     *   )
     * - base cases:
     *  - coinsLeft == 0 => return 0
     *  - leftInd == piles\[i] => return 0
     *
     * - memoization
     *  since we have up to 10^3 coins and max 2000 coins takes => max is 3*10^3 states => use the int array `leftInd x coinsLeft`
     *  where values are answers
     *
     * start top-down from leftInd=0 and coinsLeft=maxCoins, opposite of the base cases
     *
     * edge cases:
     *  - pile\[i].size < coinsLeft => from each pile try taking up to min(pile\[i].size, coinsLeft) coins.
     *
     * Time: always O(n*k*x)
     *  - we try n piles
     *  - from each pile we try taking k different coin amounts
     *   => total number of states = n*k
     *  - at each state we do O(x) work, where x=min(coinsLeft, pile\[i].size) at that particular state.
     * Space: always O(n*k)
     */
    fun topDownDp(piles: List<List<Int>>, maxCoins: Int): Int = topDownDp(
        leftInd = 0,
        coinsLeft = maxCoins,
        piles = piles,
        cache = Array(size = piles.size + 1) { IntArray(maxCoins + 1) { -1 } },
    )

    private fun topDownDp(
        leftInd: Int,
        coinsLeft: Int,
        piles: List<List<Int>>,
        cache: Array<IntArray>,
    ): Int {
        if (leftInd == piles.size || coinsLeft == 0) return 0

        val cachedResult = cache[leftInd][coinsLeft]
        if (cachedResult != -1) return cachedResult

        val nextInd = leftInd + 1
        var maxValue = topDownDp(leftInd = nextInd, coinsLeft = coinsLeft, piles, cache)
        var coinsSum = 0
        var actualCoinsLeft = coinsLeft
        val currentPile = piles[leftInd]

        for (coinInd in 0 until min(coinsLeft, currentPile.size)) {
            coinsSum += currentPile[coinInd]
            val maxLocalValue = coinsSum + topDownDp(
                leftInd = nextInd,
                coinsLeft = --actualCoinsLeft,
                piles, cache,
            )
            if (maxLocalValue > maxValue) maxValue = maxLocalValue
        }

        return maxValue.also { cache[leftInd][coinsLeft] = it }
    }

    fun bottomUp(piles: List<List<Int>>, maxCoins: Int): Int {
        // base cases are prefilled with 0 already
        val results = Array(size = piles.size + 1) { IntArray(maxCoins + 1) }
        for (pileInd in piles.size - 1 downTo 0) {
            for (coinsLeft in 1..maxCoins) {
                val currentPile = piles[pileInd]
                var coinsSum = 0
                var actualCoinsLeft = coinsLeft
                var maxTotalValue = results[pileInd + 1][actualCoinsLeft]
                for (coinInd in 0 until min(coinsLeft, currentPile.size)) {
                    coinsSum += currentPile[coinInd]
                    actualCoinsLeft--
                    val totalValue = results[pileInd + 1][actualCoinsLeft] + coinsSum
                    if (totalValue > maxTotalValue) maxTotalValue = totalValue
                }
                results[pileInd][coinsLeft] = maxTotalValue
            }
        }
        return results[0][maxCoins]
    }
}

fun main() {
    println(
        MaximumValueOfKCoinsFromPiles().bottomUp(
            piles = listOf(
                listOf(1, 100, 3),
                listOf(7, 8, 9),
            ),
            maxCoins = 2,
        ),
    )
}
