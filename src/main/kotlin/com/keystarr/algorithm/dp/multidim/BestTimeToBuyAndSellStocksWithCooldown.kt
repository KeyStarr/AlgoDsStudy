package com.keystarr.algorithm.dp.multidim

import kotlin.math.max

/**
 * LC-309 https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= prices.length <= 5 * 10^3
 *  • 0 <= prices\[i] <= 10^3
 *
 * Final notes:
 *  • done [bottomUp] by myself in 20-25 mins;
 *  • apparently the top solution uses the space-optimized bottom-up, BUT starting from the 0th price! 🔥 why, how does it work?
 *   and it is far more succinct than [bottomUp].
 *
 * Value gained:
 *  • practiced implementing bottom-up first, but still reasoned about the top-down first though;
 *  • 💡⚠️ apparently sometimes from which side one starts bottom-up seriously impacts the readability of the code, even if
 *   the same core idea is used?? TODO: verify after understanding and implementing that other bottom-up solution
 */
class BestTimeToBuyAndSellStocksWithCooldown {

    /**
     * problem rephrase:
     *  - given:
     *   - prices: IntArray, where prices\[i] is the prices of the stock on the ith day;
     *  - step rules:
     *   - buy on one day, sell on another. Hold at most 1 bought stock at a time;
     *   - may skip a day;
     *   - if sold, must skip the next day.
     *  - goal: max profit
     *
     * max profit + the result is the combinations of choices, a choice affects further choices => try DP.
     *
     * - goal: return the max profit
     * - what's the state?
     *  - we may start making decision left to right and reduce the problem by making all possible decisions at prices\[i]
     *   => leftInd: Int, denoting the start of the [prices] subarray for the current subproblem;
     *  - isHolding: Boolean. If we're holding the stock, then we can only sell or skip, if not, buy or skip (no cooldown for now)
     *   not the boughtPrice to equalize the subproblems where we've bought the stock on different days but arrived at day
     *   jth holding that stock => basically for each leftInd in prices solve the subproblem exactly once and memoize it;
     *  - isCooldown: Boolean - while on cooldown, we can't buy for 1 day. Also since it's just 1 day. In a general form
     *   it would be cooldownLeft: Int, but since its just 1 day => simplify.
     *  - the actual input of the fun also includes the rest of the original: the array of prices.
     * - recurrent relation (choices at each step):
     *  dp(leftInd,isHolding,isCooldown) = max(skip, buy (if possible) or sell)
     *   skip = dp(leftInd+1,isHolding,false) // cooldown is only 1 day, so if we skip it'd always be false
     *   if (isHolding) sell = dp(leftInd+1, isHolding=false, isCooldown=true) + prices\[leftInd]
     *   else buy = if (isCooldown) 0 else dp(leftInd+1,isHolding=true,false) - prices\[leftInd]
     *
     * actually, isCooldown only affects the choice if isHolding==false.
     *
     * - base cases:
     *  leftInd==prices.length => 0
     *
     * - memoization:
     *  since the size is up to 10^3, just a 3D array: Array(size=n + 1) { Array(size=2) { IntArray(size=2) { } } }
     *
     * Edge cases:
     *  - sum maxProfit => ~10^3*10^3=10^6 fits into Int
     *
     * Time: O(n)
     *  the total number of states is the total number of possible leftInd*isHolding = 2*n
     * Space: O(n)
     */
    fun bottomUp(prices: IntArray): Int {
        // leftInd x isHolding x isCooldown, base cases are prefilled with 0
        val cache = Array(size = prices.size + 1) { Array(size = 2) { IntArray(size = 2) } }
        for (leftInd in prices.size - 1 downTo 0) {
            val nextInd = leftInd + 1
            val currentPrice = prices[leftInd]
            for (isHolding in 0..1) {
                for (isCooldown in 0..1) {
                    val skip = cache[nextInd][isHolding][0]
                    val buyOrSell = if (isHolding == 1) {
                        cache[nextInd][0][1] + currentPrice
                    } else if (isCooldown == 0) {
                        cache[nextInd][1][0] - currentPrice
                    } else 0
                    cache[leftInd][isHolding][isCooldown] = max(skip, buyOrSell)
                }
            }
        }
        return cache[0][0][0]
    }

    /**
     * can we optimize the space complexity? YES, since to compute the result for the current state we only need variations
     * of the state with leftInd+1 => reduce the space to O(1) by using the original array with only the size of 2
     */
    fun bottomUpSpaceOptimized(prices: IntArray): Int {
        val cache = Array(size = 2) { Array(size = 2) { IntArray(size = 2) } }
        for (leftInd in prices.size - 1 downTo 0) {
            val leftIndCache = leftInd % 2
            val nextInd = leftInd + 1
            val nextIndCache = nextInd % 2
            val currentPrice = prices[leftInd]
            for (isHolding in 0..1) {
                for (isCooldown in 0..1) {
                    val skip = cache[nextIndCache][isHolding][0]
                    val buyOrSell = if (isHolding == 1) {
                        cache[nextIndCache][0][1] + currentPrice
                    } else if (isCooldown == 0) {
                        cache[nextIndCache][1][0] - currentPrice
                    } else 0
                    cache[leftIndCache][isHolding][isCooldown] = max(skip, buyOrSell)
                }
            }
        }
        return cache[0][0][0]
    }

    // TODO: refactor into an easier to read solution without using the array for cache, just the variables (look up top submissions)
}
