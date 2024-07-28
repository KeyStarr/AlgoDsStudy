package com.keystarr.algorithm.dp.multidim

import kotlin.math.max

/**
 * ⭐️ LC-188 https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/
 * difficulty: hard
 * constraints:
 *  • 1 <= maxTransactions <= 10^3
 *  • 1 <= prices.length <= 10^3
 *  • 0 <= prices\[i] <= 10^3
 *
 * Final notes:
 *  • tried to solve by myself first, got most of the design decisions right BUT made 1 critical mistake: I assumed
 *   that the goal of the function in "return max profit" would be to consider the last purchase price too. In that I made
 *   the DP function dependent on "boughtPrice: Int?", and in that multiplied the states by a factor of `n`, cause now
 *   the subproblem of dp(leftInd=X,leftTransactions=Y,holdingStock=Z), when reached from two different states with leftInd1
 *   and leftInd2 price\[leftInd1]!=price\[leftInd2] would produce 2 different results => we'd have to cache: prices.length x maxTransactions x prices.length!
 *  • I have actually done the first solution with just "isHoldingStock" true/false instead of the boughtPrice, but missed
 *   that case described above and failed the 1st submission in that very case;
 *  • => the actual solution was to consider the current price affect (either from selling or buying) only after solving the
 *   remaining subproblem. 🔥 how could I have though of that? How to consider that in the future?
 *  • converted to [bottomUpDp] in a straight-forward manner, but due to 3 typos failed 2 more submissions :D
 *
 * Value gained:
 *  • 🔥 first-time solved a DP problem with >2 dimensions;
 *  • practiced both top-down and bottom-up multidimensional DP;
 *  • indeed the framework of designing DP starting with the top-down (function goal -> input state -> recurrence relation -> base cases)
 *   worked well here, better than my other approach;
 *  • when devising the input state for DP => ask myself, make sure, "is that truly the minimum state we need to give an answer
 *   to a subproblem?"
 */
class BestTimeToBuyAndSellStockIV {

    /**
     * WRONG, MLE. Funny, it's the 4th DP problem I couldn't solve myself efficiently - the absolute anti-record across
     * all patterns I've been learning so far :D Good thing I'm mastering this pattern then, imagine what chance I'd stood
     * without this training on problems like that.
     *
     * ------------------------------
     *
     * problem rephrase:
     *  - goal: find max profit, as a result of at most k transactions
     *  - transaction: buy stock on day X, sell stock on day Y. Can only buy if don't have anything that's not sold;
     *   max transactions = max k purchases and k sells.
     *  - given:
     *   - maxTransactions: Int
     *   - prices: IntArray, where prices\[i] is the price of the stock on the ith day
     *
     * can we sell, then buy on the same day? doesn't make sense, cause we go towards goal only if sell-buy >0 and
     * if we buy on the same day that we sell, we null out that profit (and actually go minus, cause we bought initially for some price)
     * => no point
     *
     * what approach to choose?
     *  - goal is max
     *  - multiple steps => basically we are to find the best valid combination of decisions
     *  - each decision affects further choices (with each we have less transactions, and also if we buy on day X
     *   and decide to sell on day Y, we can no longer buy on days in range [X+1;Y-1]
     * => try DP for efficient
     *
     * - what's the function goal / return value?
     *  return the max profit its possible to achieve
     *
     * - what are states?
     *  - leftTransactions is 100% one of the vars in the state, cause it regulates how many moves we can make;
     *  - XX WRONG true/false whether we're holding the bought stock or not also affects out future choices, whether we are buying or selling;
     *   => actual `boughtInd` => cause cache\[pricesLeftInd]\[leftTransactions]\[isHolding=true(1)] would be the same
     *    for different prices that we've held and would, for example, sell, WHICH IS WRONG! We must actually remember
     *    the bought price's index! to differentiate between these outcomes.
     *  - probably prices left to process also is part of the target state, cause it, along with these other constraints,
     *   directly defines out future choices.
     * =>
     *  - (leftInd or rightInd): Int for prices
     *  - leftTransactions: Int
     *  - isHoldingStock: Boolean (boughtPrice: Int)
     *
     * - how do we make a step?
     *  for each prices\[i] we have 3 choices:
     *   - we can always skip
     *   - if isHoldingStock -> we can sell; else -> we can buy
     *  selling
     *   only contributes to the goal if prices\[i] > boughtPrice
     *  buying
     *   only makes sense if there's a price ahead greater than prices\[i] - but we can't know that as of now? so we always try
     *   to buy
     *
     * recurrence relation:
     *  dp(i) = max(
     *   dp(i+1, boughtPrice, leftTransactions), // skip
     *   if (boughtPrice != -1)
     *    if (prices[i] > boughtPrice) dp(i+1, -1, leftTransactions-1) + (prices[i] - boughtPrice) else 0 // sell
     *   else
     *    dp(i+1, prices[i], leftTransactions) // buy
     *  )
     *
     * base cases are on the right:
     *  - leftTransactions == 0
     *  - i == prices.size // sometimes its best not to use all transactions (sometimes the only available ones left are negative)
     *
     * observations:
     *  - we can buy the stock on different days (on 2 different paths), BUT sell on the same day => states can overlap,
     *   cause future subproblem then would be the same (the diff in prices that weve sold would be added separately)
     *   => its 100% efficient solution is a DP
     *
     * edge cases:
     *  - sum => max profit is if we have max days in a pattern of [maxPrice, minPrice, maxPrice, minPrice, ..] =>
     *   10^3 / 2 => fits into Int;
     *  - [dayPrices] is strictly decreasing => the best strategy is to never make transactions => always return 0 =>
     *   we do consider the path of always skipping.
     *
     * Time: O(n^2*maxTransactions)
     *  - the total worst number of states = prices.size * maxTransactions * prices.size = O(n^2*maxTransactions)
     *  => O(min([maxTransactions], [dayPrices].size))?
     * Space: O(n^2*maxTransactions)
     */
    fun topDownDp(maxTransactions: Int, dayPrices: IntArray): Int =
        topDownDp(
            boughtPriceInd = null,
            leftTransactions = maxTransactions,
            leftInd = 0,
            // currentPriceInd x leftTransactions x boughtPriceInd
            cache = Array(size = dayPrices.size) { Array(size = maxTransactions + 1) { IntArray(size = dayPrices.size + 1) { -1 } } },
            dayPrices = dayPrices,
        )

    private fun topDownDp(
        boughtPriceInd: Int?,
        leftTransactions: Int,
        leftInd: Int,
        cache: Array<Array<IntArray>>,
        dayPrices: IntArray,
    ): Int {
        if (leftInd == dayPrices.size || leftTransactions == 0) return 0

        val boughtCacheInd =
            (boughtPriceInd ?: -1) + 1 // if no stock was bought => index is 0, if it was, it starts from 1
        val cachedResult = cache[leftInd][leftTransactions][boughtCacheInd]
        if (cachedResult != -1) return cachedResult

        val nextInd = leftInd + 1
        val result = max(
            topDownDp(boughtPriceInd, leftTransactions, nextInd, cache, dayPrices),
            if (boughtPriceInd != null) {
                if (dayPrices[leftInd] > dayPrices[boughtPriceInd]) {
                    val profit = dayPrices[leftInd] - dayPrices[boughtPriceInd]
                    profit + topDownDp(boughtPriceInd = null, leftTransactions - 1, nextInd, cache, dayPrices)
                } else 0
            } else {
                topDownDp(boughtPriceInd = leftInd, leftTransactions, nextInd, cache, dayPrices)
            }
        )
        cache[leftInd][leftTransactions][boughtCacheInd] = result
        return result
    }

    /**
     * Can we reduce time and space complexity to O(n*k)? => reduce the number of states by a factor of [dayPrices]?
     *
     * Actually, the goal of [topDownDp] is to return the max profit, right? The problem is that if we've purchased
     * the stock on different days, say, X and Y, but then held on to it until the day Z, and in both cases (separate paths)
     * we decide to sell it on that day Z => in implementation [topDownDp] that was a problem, cause upon selling we
     * calculated the impact of the initial purchase price on the profit => these two subproblems are actually different,
     * the input is the same BUT for that boughtPrice value.
     * => can we somehow get rid of that initial price in the subproblem definition?
     *
     * YES, WE CAN! Simply interpret the function goal "return the max profit" in a way that we sell and increase the profit
     * by the selling price WITHOUT bought price consideration. And when we actually buy => we DECREASE the profit by
     * that purchase price
     * => then the subproblem is defined only via (pricesLeftInd: Int, transactionsLeft: Int, isHoldingStock: Boolean)
     *  and in that case above we solve it only once, and retrieve via memoization the 2nd time
     * => overall numbers of states is [dayPrices].size * [maxTransactions] = O(n*k), cause isHolding stock is a const factor of 2
     *
     * the trade-off is that we are now going down the paths of selling stock for 0 or the negative profit though!
     * but even though its redundant, the total number of states is less than [topDownDp] by a factor of n
     * // TODO: how's that so? that optimization of whether to sell or not appealed to me straight away so hard... I'm buffled
     *
     * Time: O(n*k)
     * Space: O(n*k)
     */
    fun topDownDpOptimized(maxTransactions: Int, dayPrices: IntArray): Int =
        topDownDpOptimized(
            leftInd = 0,
            isHoldingStock = false,
            leftTransactions = maxTransactions,
            // currentPriceInd x leftTransactions x isHoldingStock
            cache = Array(size = dayPrices.size) { Array(size = maxTransactions + 1) { Array(size = 2) { null } } },
            dayPrices = dayPrices,
        )

    private fun topDownDpOptimized(
        leftInd: Int,
        isHoldingStock: Boolean,
        leftTransactions: Int,
        cache: Array<Array<Array<Int?>>>,
        dayPrices: IntArray,
    ): Int {
        if (leftInd == dayPrices.size || leftTransactions == 0) return 0

        val isHoldingInd = if (isHoldingStock) 1 else 0
        val cachedResult = cache[leftInd][leftTransactions][isHoldingInd]
        if (cachedResult != null) return cachedResult

        val nextInd = leftInd + 1
        val currentPrice = dayPrices[leftInd]
        val result = max(
            topDownDpOptimized(nextInd, isHoldingStock, leftTransactions, cache, dayPrices),
            if (isHoldingStock) {
                currentPrice + topDownDpOptimized(nextInd, false, leftTransactions - 1, cache, dayPrices)
            } else {
                -currentPrice + topDownDpOptimized(nextInd, true, leftTransactions, cache, dayPrices)
            }
        )
        cache[leftInd][leftTransactions][isHoldingInd] = result
        return result
    }

    /**
     * Same complexities as [topDownDpOptimized].
     */
    fun bottomUpDp(maxTransactions: Int, dayPrices: IntArray): Int {
        // all base cases are prefilled with 0
        val dp = Array(size = dayPrices.size + 1) { Array(size = 2) { IntArray(size = maxTransactions + 1) } }
        for (leftInd in (dayPrices.size - 1) downTo 0) {
            for (leftTransactions in 1..maxTransactions) {
                for (isHoldingStock in 0..1) {
                    val nextInd = leftInd + 1
                    val currentPrice = dayPrices[leftInd]
                    dp[leftInd][isHoldingStock][leftTransactions] = max(
                        dp[nextInd][isHoldingStock][leftTransactions],
                        if (isHoldingStock == 1) {
                            currentPrice + dp[nextInd][0][leftTransactions - 1]
                        } else {
                            -currentPrice + dp[nextInd][1][leftTransactions]
                        }
                    )
                }
            }
        }
        return dp[0][0][maxTransactions]
    }

    // TODO: simplify, the top bar time solution is much more succinct - understand why and implement
}

fun main() {
    println(
        BestTimeToBuyAndSellStockIV().bottomUpDp(
            maxTransactions = 2,
            dayPrices = intArrayOf(3, 3, 5, 0, 0, 3, 1, 4),
        )
    )
}
