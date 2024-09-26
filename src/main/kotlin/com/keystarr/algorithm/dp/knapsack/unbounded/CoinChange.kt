package com.keystarr.algorithm.dp.knapsack.unbounded

import kotlin.math.min

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
     * Time: average/worst O(amount*coins.size)
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
     * Time: O(amount*coins.size)
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

/**
 * Re-solved as a part of the effort of solidifying my understanding of the "Unbound Knapsack" DP type problems.
 *
 * LOL, [bottomUpSpaceOptimized] is actually far less readable than the previous incarnation of it CoinChange.bottomUp :D
 *
 * Well, this time I definitely understood it way better than the last, judging by the notes. Did both the top-down
 *  and the bottom-up by myself this time completely (having though an idea of the Unbounded Knapsack in the cache of my head)
 */
class CoinChange2 {

    /**
     * Note that to compute the result for a single state we need to only know further leftSum for the current startInd
     *  AND an arbitrary newLeftSum element in the next startInd row => we can trivially reduce space to O(m)
     *
     * Time: always O(n*m) same as [bottomUp]
     * Space: always O(m), reduced by a factor of n from [bottomUp].
     *
     * ----------
     *
     * dry run for a failed test case
     *
     * prev = [0, MAX, MAX]
     * current = [MAX, MAX, MAX]
     *
     * startInd=1
     * leftSum=1
     *
     * newLeftSum = 1-MAX
     * current[1] = min(MAX, prev[1]=MAX)=MAX
     *
     * startInd=1
     * leftSum=2
     * nLS=2-MAX
     * current[2] = min(MAX,prev[2]=MAX)=MAX
     *
     * swap(current,prev)
     * startInd=0
     * leftSum=1
     *
     * newLeftSum=1-1=0
     * current[1] = min(current[0])
     *
     * => ah, forgot to set prev[0]=0 initially, fixed.
     */
    fun bottomUpSpaceOptimized(nums: IntArray, targetSum: Int): Int {
        // base case - can't reduce leftSum with nothing, but set [0]=0 since for startInd+1 its never achievable, and for lower startInd
        // it must remain a 0 and will never be modified, as a base case
        var prev = IntArray(size = targetSum + 1) { Int.MAX_VALUE }.apply { set(0, 0) }
        // [0] is a base case too, implicitly initialized with a 0 (to reduce leftSum to 0 we trivially need 0 elements)
        var current = IntArray(size = targetSum + 1)
        for (startInd in nums.lastIndex downTo 0) {
            for (leftSum in 1..targetSum) {
                val newLeftSum = leftSum - nums[startInd]
                current[leftSum] = min(
                    if (newLeftSum >= 0 && current[newLeftSum] != Int.MAX_VALUE) current[newLeftSum] + 1 else Int.MAX_VALUE,
                    prev[leftSum],
                )
            }
            val temp = prev
            prev = current
            current = temp
        }
        return if (prev[targetSum] == Int.MAX_VALUE) -1 else prev[targetSum]
    }

    /**
     *
     * Note to carefully handle the base cases:
     *  1. every value in cache\[nums.size] must be Int.MAX_VALUE, since then we have no values left to reduce the leftSum,
     *   and leftSum can't be 0, since we'd never reach cache[nums.size][0];
     *  2. init all cache\[startInd][0] with 0, since if leftSum == 0 then it trivially takes 0 new coins to reduce leftSum to 0.
     *
     * ----------- careful with loop directions
     *
     * A.
     * out for - obviously, we need to iterate top-to-bottom since the value of each startInd leftSum depends on the value of the
     *  next startInd.
     *
     * B.
     * !! We must iterate through the leftSum bottom-to-top since if we take the same con, we have same startInd but less
     *  targetSum + it may be possible to achieve leftSum==0 while taking the same coin repeatedly => the leftSum then
     *  would only decrease => if we compute top-to-bottom then
     *
     *  nums=[1,2,5] targetSum=11
     *  e.g. current state startInd=2 leftSum=10
     *  10-5=5
     *
     *  cache[2][5], if we were to compute leftSum top-to-bottom would still be default Int.MAX_VALUE => we'd incorrectly
     *   decide that its impossible to build a valid combination from that initial state
     *
     *  if we'd compute leftSum bottom-to-top then cache[2][5] would already be 1 => cache[2][10]=min(cache[2][5]+1,cache[3][10])
     *   = min(1+1,Int.MAX)=2
     *
     * C. Start inner loop for leftSum with 1 since 0 is a base case and must not be changed.
     */
    fun bottomUp(nums: IntArray, targetSum: Int): Int {
        val cache = Array(size = nums.size + 1) { IntArray(size = targetSum + 1) { Int.MAX_VALUE } }
        for (startInd in nums.indices) cache[startInd][0] = 0

        for (startInd in nums.lastIndex downTo 0) {
            for (leftSum in 1..targetSum) {
                val newLeftSum = leftSum - nums[startInd]
                cache[startInd][leftSum] = min(
                    if (newLeftSum >= 0 && cache[startInd][newLeftSum] != Int.MAX_VALUE) cache[startInd][newLeftSum] + 1 else Int.MAX_VALUE,
                    cache[startInd + 1][leftSum],
                )
            }
        }
        return if (cache[0][targetSum] == Int.MAX_VALUE) -1 else cache[0][targetSum]
    }

    /**
     * problem rephrase:
     *  - given:
     *   - coins: IntArray
     *   - targetSum: Int
     *  goal: return the size of the minimum sized valid combination of elements
     *      valid:
     *       - all elements in the combination sum == targetSum;
     *       - any element from [coins] can be taken any number of times.
     *
     * seems like an "Unbound Knapsack" type problem, since we can take an infinite amount of each element, and we must
     * fit the total value sum constraint. Twist: we minimize the size of the combination.
     *
     * try dp, start with top-down
     *
     * goal: return the size of the minimum combination of values from nums[startInd:], may use each infinitely, that
     *  sums up to [targetSum].
     *
     * input state:
     *  - startInd: Int, consider including all reasonable amounts of coins\[startInd] only in a single main branch to
     *   optimize caching and do the minimum amount of work at every state
     *  - leftSum: Int
     *
     * recurrence relation:
     *  dp(startInd, leftSum) = min(takeCurrent, skip)
     *   takeCurrent = dp(startInd, leftSum-coins\[startInd]) + 1 // +1 since we've taken the current coin
     *   skip = dp(startInd+1, leftSum)
     *
     * base cases:
     *  - leftSum < 0 => return Int.MAX_VALUE // signify that there was no valid combination down that path, in any min() unless the other is int max val this will lose
     *  - leftSum == 0 => return 0
     *
     * edge cases:
     *  - [targetSum] is impossible to achieve => as per problem, return -1
     *
     * Time: average/worst O(n*m)
     *  - we have n=nums.size m=targetSum O(n*m) states. left sum can be [0,targetSum] since all [nums] are positive;
     *  - we do O(1) work at each state;
     *  - worst tree height is when there is a coin of value == 1, then tree height is targetSum. Tree width is hard to estimate.
     *
     *  best case is when we have the fewest amount of unique possible states, kind of hard to estimate (don't want to rn, feel resistance=lazy??)
     *   like when all numbers are equal? then we'd have as many take+skip equal as possible
     *
     * Space: always O(n*m)
     *  we can use arrays, since targetSum is [0;10^4]
     */
    fun topDownDp(nums: IntArray, targetSum: Int): Int =
        topDownDpRecursive(
            startInd = 0,
            leftSum = targetSum,
            cache = Array(size = nums.size) { IntArray(size = targetSum + 1) { EMPTY_CACHE_VAL } },
            nums = nums,
        ).let { result -> if (result == Int.MAX_VALUE) -1 else result }

    private fun topDownDpRecursive(
        startInd: Int,
        leftSum: Int,
        cache: Array<IntArray>,
        nums: IntArray,
    ): Int {
        if (leftSum == 0) return 0
        if (leftSum < 0 || startInd == nums.size) return Int.MAX_VALUE

        val cachedResult = cache[startInd][leftSum]
        if (cachedResult != EMPTY_CACHE_VAL) return cachedResult

        val take = topDownDpRecursive(startInd = startInd, leftSum = leftSum - nums[startInd], cache, nums)
        val skip = topDownDpRecursive(startInd = startInd + 1, leftSum = leftSum, cache, nums)
        return min(if (take != Int.MAX_VALUE) take + 1 else take, skip).also { result ->
            cache[startInd][leftSum] = result
        }
    }
}

private const val EMPTY_CACHE_VAL = -1

fun main() {
    println(
        CoinChange2().bottomUp(nums = intArrayOf(1, 2, 5), targetSum = 100)
    )
}

