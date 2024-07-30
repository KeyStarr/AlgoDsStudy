package com.keystarr.algorithm.dp.multidim

import kotlin.math.max

/**
 * LC-714 https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= prices.length <= 5 * 10^4
 *  • 1 <= prices\[i] < 5*10^4
 *  • 0 <= fee < 5*10^4
 *
 * Final notes:
 *  • 🏆FIRST-TIME SOLVED A MULTI-DIM DP PROBLEM BY MYSELF AND SUBMITTED 1st TIME! Never ran the code, actually. Both,
 *   top-down AND bottom-up (but, to be fair, this problem is basically [BestTimeToBuyAndSellStockIV] with very small
 *   variations. Though one would still have to understand why and how we do DP here to adapt correctly);
 *  • done [topDownDp] by myself in 30 mins and [bottomUp] in additional 8 minutes;
 *  • indeed, having worked through the [BestTimeToBuyAndSellStockIV] here I could clearly see the solution including
 *   the trick that got me last time, for only reducing to subproblems with `isHolding: Boolean` instead of the actual
 *   `boughtPrice` in order to guarantee efficient memoization and O(n) time. In fact, I didn't even need to dry-run this
 *   one, to draw the decision tree, just had it in my head 💡.
 *
 * Value gained:
 *  • practiced both top-down and bottom-up multidim DP;
 *  • reinforced efficient subproblem reduction for optimal memoization.
 */
class BestTimeToBuyAndSellStockWithTransactionFee {

    /**
     * problem rephrase:
     *  - given:
     *   - prices: IntArray, where prices\[i] is the price of the stock on ith day;
     *   - fee: Int, the price to pay per each transaction. It is unclear when to pay it, on buying or on selling, but
     *   since only buying and not selling is never an optimal move (since prices are always positive), it doesn't matter.
     *  - step rules:
     *   - we can buy at any day but only if we're not holding any stock;
     *   - we can sell at any day but only if we're holding (purchased the stock and havent sold earlier);
     *   - the number of transactions is unlimited.
     *  - goal: return the max profit (=find the max profit combination of transactions)
     *
     * analysis:
     *  - goal is max
     *  - the result is achieved via a combination of steps, each choice affects further choices
     * => try DP
     *
     * one of the tricks is that we may buy the stock at price less than the fee, but it might be optimal, as long as we
     * later sell at the greater price than the fee.
     *
     * - what's the goal?
     *  return the max profit from doing transactions: Int
     * - what's the state = the input?
     *  - leftInd: Int, denoting the start of the current subproblem's prices subarray
     *  - isHoldingStock: Boolean, to make a decision whether to sell or buy. No specific bought price to memoize the subproblems
     *   efficiently.
     *  - prices: IntArray
     *  - fee: Int
     * - what is a single step with all options?
     *  we may choose whether to buy, sell or skip prices\[leftInd]
     *   - we can always skip
     *   - buy only if not holding, sell only if holding
     *  when buying => subtract the price from the result
     *  when selling => add the price to the result, but subtract the fee
     *
     * hm basically since the fee is the same for all stocks => all sell prices are simply reduced by the fee. So technically
     * some choices of purchase become no longer optimal - its never optimal to buy if later there's no price that's strictly
     * greater than (boughtPrice+fee). But in DP we can't know that, so we just check all the options anyway.
     * We can't consider selling only at that condition, since to efficiently cache the subproblems, each subproblem must not
     * have any data on what was the price we bought the stock at.
     * =>
     * not really, it only makes sense to SELL if the SELL price is LARGER than the FEE!
     *
     * - recurrence relation
     *  dp(leftInd) = max(skip, sell or buy)
     *   skip = dp(leftInd+1)
     *   sell = dp(leftInd+1)+prices\[leftInd]-fee
     *   buy = dp(leftInd+1)-prices\[leftInd]
     * - base cases:
     *  leftInd==prices.size => 0 // max profit, cause there are no prices to perform transactions
     * - memoization:
     *  Array(size=prices.size), since its just 1D DP with at most 5*10^4 states
     *
     * Edge cases:
     *  - sum => 10^4*10^4=10^8 is approximately the max profit boundary, fits into Int;
     *  - all prices are less than the fee => always return 0, to maximize profit we have to skip all prices, 0 transactions.
     *
     * Time: O(n), since we reduce the problem by 1 price element every time and leftInd is all that the problem's input is defined
     *  via => we have duplicate suproblems along the way but, due to memoization, we solve each exactly once.
     * Space: O(n)
     */
    fun topDownDp(prices: IntArray, fee: Int): Int = topDownDp(
        leftInd = 0,
        isHoldingStock = false,
        // could use -1 here, maxProfit will never be negative, cause on each step there's always a path to skipping all further prices
        // which leads to maxProfit==0
        cache = Array(size = prices.size) { Array(size = 2) { null } },
        prices = prices,
        fee = fee,
    )

    private fun topDownDp(
        leftInd: Int,
        isHoldingStock: Boolean,
        cache: Array<Array<Int?>>,
        prices: IntArray,
        fee: Int,
    ): Int {
        if (leftInd == prices.size) return 0

        val isHoldingInd = if (isHoldingStock) 1 else 0
        val cachedResult = cache[leftInd][isHoldingInd]
        if (cachedResult != null) return cachedResult

        val nextInd = leftInd + 1
        val skipProfit = topDownDp(leftInd = nextInd, isHoldingStock, cache, prices, fee) // skip
        val currentPrice = prices[leftInd]
        val buyOrSellProfit = if (isHoldingStock) {
            if (currentPrice > fee) {
                topDownDp(leftInd = nextInd, isHoldingStock = false, cache, prices, fee) + currentPrice - fee // sell
            } else 0
        } else {
            topDownDp(leftInd = nextInd, isHoldingStock = true, cache, prices, fee) - currentPrice// buy
        }
        return max(skipProfit, buyOrSellProfit).also { maxProfit -> cache[leftInd][isHoldingInd] = maxProfit }
    }

    /**
     * Convert [topDownDp] to bottomUp, starting from the base cases. The core logic = amount and type of states, choices
     * on how to proceed to next steps = are the same, we change just the direction in which we solve suproblems. Memoization
     * is basically same too.
     */
    fun bottomUp(prices: IntArray, fee: Int): Int {
        // the base case with cache[prices.size][j] are all initialized to 0. It's safe to init other values to 0 too,
        // cause when we access them it's guaranteed they were computed already
        val resultCache = Array(size = prices.size + 1) { IntArray(size = 2) }
        for (leftInd in prices.size - 1 downTo 0) {
            for (isHolding in 0..1) {
                val nextInd = leftInd + 1
                val currentPrice = prices[leftInd]
                resultCache[leftInd][isHolding] = max(
                    resultCache[nextInd][isHolding], // skip
                    if (isHolding == 1) {
                        if (currentPrice > fee) resultCache[nextInd][0] + currentPrice - fee else 0 // sell
                    } else {
                        resultCache[nextInd][1] - currentPrice // buy
                    },
                )
            }
        }
        return resultCache[0][0]
    }

    /**
     * Notice that in order to compute the answer for the current step (resultCache\[nextInd]) we need to know only
     * the result of the next step, with both isHolding false/true
     * + we need to know the results of the next step for both isHolding=f/t with currentLeftInd
     * => use for memoization an array of Array(size=2) { IntArray(size=2) }, to simply cache the current and the next steps
     *
     * Time: O(n)
     * Space: O(1)
     */
    private fun bottomUpSpaceOptimized(prices: IntArray, fee: Int): Int {
        val cache = Array(size = 2) { IntArray(size = 2) }
        for (leftInd in prices.size - 1 downTo 0) {
            val currentIndCache = leftInd % 2
            for (isHolding in 0..1) {
                val nextIndCache = (leftInd + 1) % 2
                val currentPrice = prices[leftInd]
                cache[currentIndCache][isHolding] = max(
                    cache[nextIndCache][isHolding], // skip
                    if (isHolding == 1) {
                        if (currentPrice > fee) cache[nextIndCache][0] + currentPrice - fee else 0 // sell
                    } else {
                        cache[nextIndCache][1] - currentPrice // buy
                    },
                )
            }
        }
        return cache[0][0]
    }
}
