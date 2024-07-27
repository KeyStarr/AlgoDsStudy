package com.keystarr.algorithm.dp.onedim

/**
 * ⭐️ LC-322 https://leetcode.com/problems/coin-change/description/
 * difficulty: medium
 * constraints:
 *  • coins.length <= 12
 *  • 1 <= coins\[i] <= 2^31 -1 // fits into Int
 *  • 0 <= amount <= 10^4
 *
 * Final notes:
 *  • ⚠️ couldn't solve in 1h by myself:
 *   • failed to find the DP approach ⚠️ to the problem!
 *    • before now solved only problems where problem reduction was to
 *     basically make a step and "cut" 1 element from the array of all values that needed to be processed, that was the only
 *     definition of the remaining problem I've worked with;
 *    • here I reasoned:
 *     • the amount of slots (coins possible to take) is unknown, implicitly unbounded only by the relationship between
 *      the [amount] and [coins] available;
 *     • tried somehow using and removing a coin from [coins], like, trying amount%coin and amount/coin etc. Failed to
 *      see problem reduction that way;
 *     • completely DIDN'T EVEN THINK of the problem space via just [amount] 💡 and trying to making 1 step => reducing it.
 *      (which was the correct approach).
 *     • eventually just tried to do greedy [greedyWrong] and failed miserably on like 8X/160 cases, which was unreasonably
 *      complicated to even dry run and understand the issue (due to no actual final min amount combination of coins
 *      given in the answer, just the amount).
 *  • studied Editorial and 1 top submission => ⚠️ STILL couldn't understand the solution!!!
 *   ONLY after watching Neetcode's https://www.youtube.com/watch?v=H9bfqozjoqs&ab_channel=NeetCode understood it finally,
 *   I guess that's coz he managed to visually and gradually lead from the brute force along multiple optimizations
 *   on a concrete example to the final optimized solution. What a chad.
 *  • словил метанойю, буквально, теперь вижу четко, втф, мол, как раньше не видел;
 *  • interesting, it seems that [topDownDp] actually might on average require less different amount states computations,
 *   since [bottomUp] computes for exactly [amount] states always, and [topDownDp] only for those states reachable by the
 *   subtraction of valid combinations of coins, which may be far less than the actual [amount]! (though callstack is a
 *   weight still). Anyway that's a matter of const and not important asymptotically;
 *  • yet again I failed 1 submission just cause I spent so much effort on the solution I forgot to eventually, for top-down,
 *   to add memoization(((( => TLE due to duplicate calls.
 *
 * Value gained:
 *  • 🔥first time solved a DP problem where the actual problem division was to be performed not by cutting 1 element
 *   out of the input collection, but reducing an Integer representing the problem space!
 *  • try designing DP top-down by default like that?
 *   - what is a single step?
 *   - how to reduce the problem?
 *   - when do we get the answer?
 *   => what's the recurrent relation?
 *   - what are base cases?
 */
class CoinChange {

    /**
     * WRONG SOLUTION, WRONG APPROACH
     * like in [SolvingQuestionsWithBrainpower], decided to leave it for further retrospection
     *
     * ---------------
     *
     * goal: if it's possible to combine coins to reach the exact [amount], return the minimum amount of coins, otherwise -1.
     * given: all possible denominations of coins, each of which may be used infinite number of times.
     *
     * what to use?
     *  - multiple steps
     *  - end goal is the best (->min) combination
     *  - each step affects future choices (cause amount-=chosenCoin), BUT we can still take any coin that fits under
     *   the left amount => more like pure greedy than DP?
     *
     * try "greedy":
     *  - sort the coins
     *  - for maxCoinInd = coins.size-1 downTo 0:
     *   - maxCoinsMax = left / coins\[maxCoinInd]
     *   - for maxCoinsTaken = maxCoinsMax downTo 1:
     *    - left = amount - maxCoinsTaken*coins[maxCoinInd]
     *    - if (left == 0) return maxCoinsTaken
     *    - iterate through rightInd = maxCoinInd-1 downTo 0:
     *     - currentCoin = coins[rightInd]
     *     - takeCoins = left / currentCoin
     *     - if takeCoins == 0:
     *      - break
     *     - coinsCount += takeCoins
     *     - left -= takeCoins * currentCoin
     *     - if left == 0:
     *      - return coinsCount
     * - return -1
     *
     * Edge cases:
     *  - amount == 0 => minCoin==1 => always return 0 => early return check;
     *  - coins.length == 1 => answer is amount/coins[0] division remainder is 0, otherwise-1 => correct, the first answer condition
     *   in the maxCoinTaken loop will be met.
     *
     * Time: O(nlogn+n*k*n)=O(nlogn + k*n^2) = O(k*n^2)
     *  - sort the coins O(logn)
     *  - maxCoinInd has n states
     *   - maxCoinsTaken has average/worst k = amount/coins.max() states
     *   - otherCoins have maxCoinInd-1 states for each iteration => average/worst n states
     * Space: O(1)
     */
    fun greedyWrong(coins: IntArray, amount: Int): Int {
        if (amount == 0) return 0

        coins.sort()
        for (maxCoinInd in coins.size - 1 downTo 0) {
            val maxCoinsMax = amount / coins[maxCoinInd]
            for (maxCoinsTaken in maxCoinsMax downTo 1) {
                var left = amount - maxCoinsTaken * coins[maxCoinInd]
                if (left == 0) return maxCoinsTaken
                var coinsTaken = maxCoinsTaken
                for (otherCoinInd in maxCoinInd - 1 downTo 0) {
                    val currentCoin = coins[otherCoinInd]
                    val takeCoins = left / currentCoin
                    if (takeCoins == 0) continue // even 1 currentCoin exceeds [amount], try smaller coins then
                    coinsTaken += takeCoins
                    left -= takeCoins * currentCoin
                    if (left == 0) return coinsTaken
                }
            }
        }
        return -1
    }

    /**
     * find the best (min) combination + subproblems may overlap => DP
     *
     * - what is a step? to reach the [amount] we can take exactly 1 of ANY coin (they're infinite)
     * - whats problem reduction? once we've taken a coin X, we need to find the fewest coins to reach [amount] - X
     * - what is the answer?
     *  we choose the minimum amount of coins needed through trying each available coin for the current [amount] + 1
     *  (for the coin taken during this step)
     *  => recurrence relation: dp(amount) = min(for coin in coins: dp(amount-coin)) + 1
     * - base case is amount==0 answer is 0
     * - memoization: IntArray or HashMap, array would be faster on average but may take up significantly (within n-Y) extra space,
     *  but map may be better space-wise, yet slower on write/read due to object creation (Entry) and hash computation
     *  since worst [amount] is 10^4, prefer a hashmap.
     *
     * edge case:
     *  - all choices from the current [amount] lead to -1 answer => return -1 (no valid combination exists for the given amount)
     *
     * worst amount of states is O(amount), average depends on it (precisely non-trivially defined via actual coins available,
     *  but NEVER GREATER THAN [amount] since minCoin==1, and amount/1=amount)
     * =>
     * Time: average/worst O(amount)
     * Space: average/worst O(amount for memoization)
     *  - average/worst cache is O(n)
     *  - average/worst callstack is also O(n)
     */
    fun topDownDp(coins: IntArray, amount: Int): Int = topDownDp(
        coins = coins,
        amount = amount,
        amountToMinCoinsMap = mutableMapOf()
    )

    private fun topDownDp(coins: IntArray, amount: Int, amountToMinCoinsMap: MutableMap<Int, Int>): Int {
        if (amount < 0) return -1
        if (amount == 0) return 0

        val cache = amountToMinCoinsMap[amount]
        if (cache != null) return cache

        var minCoinsTotal = Int.MAX_VALUE
        coins.forEach { coin ->
            val minCoins = topDownDp(coins, amount - coin, amountToMinCoinsMap)
            if (minCoins == -1) return@forEach
            if (minCoins < minCoinsTotal) minCoinsTotal = minCoins
        }
        return (if (minCoinsTotal == Int.MAX_VALUE) -1 else minCoinsTotal + 1).also { result ->
            amountToMinCoinsMap[amount] = result
        }
    }

    /**
     * Time: O(amount)
     * Space: O(amount), unable to optimize to a few variables since we need multiple arbitrary previous states
     */
    fun bottomUp(coins: IntArray, amount: Int): Int {
        val amountToMinCoins = IntArray(size = amount + 1).apply { set(0, 0) }
        for (currentAmount in 1..amount) {
            var minCoins = Int.MAX_VALUE
            coins.forEach { coin ->
                val prevAmount = currentAmount - coin
                if (prevAmount < 0) return@forEach

                val prevMinCoins = amountToMinCoins[prevAmount]
                if (prevMinCoins == -1) return@forEach

                if (prevMinCoins < minCoins) minCoins = prevMinCoins
            }
            amountToMinCoins[currentAmount] = if (minCoins == Int.MAX_VALUE) -1 else minCoins + 1
        }
        return amountToMinCoins[amount]
    }
}
