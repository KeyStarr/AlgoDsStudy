package com.keystarr.algorithm.dp.knapsack.unbounded

/**
 * ⭐️ another great example of the unbounded knapsack type DP along with [CoinChange]
 *
 * LC-518 https://leetcode.com/problems/coin-change-ii/
 * difficulty: medium
 * constraints:
 *  • 1 <= coins.size <= 300;
 *  • 1 <= coins\[i] <= 5 * 10^3;
 *  • all values in coins are unique;
 *  • 0 <= amount <= 5 * 10^3.
 *
 * Final notes:
 *  • done [slowTopDownDp] in 45 mins by myself;
 *  • ⚠️⚠️ failed to come up with a design for a more efficient DP in 1h => checked solutions;
 *  • invested a heavy amount of time (2-4h) into unbounded/0-1 knapsack and understood deeper [CoinChange] and [com.keystarr.algorithm.dp.knapsack.zero_one.TargetSum]
 *   then came back to this one => finally it all makes sense quite intuitively,
 *   ⚠️ though I still don't clearly understand exactly why the [slowTopDownDp] solution is that much slower, like,
 *    asymptotic complexity estimation-wise it makes sense but intuitively - not really. Either way we spawn same calls, no?
 *    or do we spawn that many more calls with the [slowTopDownDp]?
 *   but its good enough for now, we're out of the time budget.
 *
 * Value gained:
 *  • solved an "unbounded knapsack" type problem ("return the number of valid combinations") using bottom-space optimized DP;
 *  • learned deeper and reinforced the "unbounded knapsack" DP pattern;
 */
class CoinChangeII {

    /**
     * Make another few observations based on [bottomUpSpaceOptimized]:
     *  1. actually, if we keep only one row as the cache, when we compute current\[leftSum] we only need from the previous
     *   row exactly this value which is already stored there current\[leftSum]! Basically, the amount of combinations
     *   if we skip the current coin and try further ones only is already stored in there => we only need to add all combinations
     *   which result if we take the current coin (relative to the current leftSum)
     *   + the other value is dependent only on prev value of the current row which we compute left-to-right
     *   => we may use exactly 1 row for the cache!
     *  2. if targetSum is less than current coin, there's no point to compute since newLeftSum will always be negative
     *   and hit the base case => start considering leftSum states only min from the value of the current coin
     *
     * same asymptotic time and space as [bottomUpSpaceOptimized]
     *  => but reduce both the space and the time const slightly (though, imho, trade off maintainability!)
     *
     */
    fun bottomUpCleanest(targetSum: Int, nums: IntArray): Int {
        val current = IntArray(size = targetSum + 1).apply { set(0, 1) }
        for (startInd in nums.lastIndex downTo 0) {
            val number = nums[startInd]
            for (leftSum in number..targetSum) current[leftSum] += current[leftSum - number]
        }
        return current[targetSum]
    }

    /**
     * Note that current row's computation depends solely on it's previous values and the next row => reduce the space to O(m).
     *
     * nums=[99,1]    targetSum=100
     *
     * prev=[1,0]  current=[1,0]
     *
     * startInd=1  leftSum=1
     * current[1] = current[]
     *
     * Time: always O(n*m), same as [bottomUp]
     * Space: always O(m)
     */
    fun bottomUpSpaceOptimized(targetSum: Int, nums: IntArray): Int {
        val rowSize = targetSum + 1
        var prev = IntArray(size = rowSize).apply { set(0, 1) }
        var current = IntArray(size = rowSize).apply { set(0, 1) }

        for (startInd in nums.lastIndex downTo 0) {
            for (leftSum in 1..targetSum) {
                val newLeftSum = leftSum - nums[startInd]
                val takeCurrent = if (newLeftSum >= 0) current[newLeftSum] else 0
                val goNext = prev[leftSum]
                current[leftSum] = takeCurrent + goNext
            }
            val temp = prev
            prev = current.also { current = prev }
            current = temp
        }
        return prev[targetSum]
    }

    /**
     * Start with converting the [topDownDp] as-is without optimizations.
     * Careful:
     *  1. init cache with 0's to fill the base cases for startInd=nums.size+1
     *  2. init all cache[startInd][0]=1 for base case of leftSum+1
     *  3. inner loop:
     *      3.1 !!! start left-to-right since we have an option to stay on the same startInd yet only reduce leftSum
     *       => compute leftSum from lowest to highest to be used for these cases correctly;
     *      3.2 start from 1, since 0 is a base case and must not be changed.
     *
     * Time: always O(n*m), same as [topDownDp] but on average probably worse time const since we visit some non-achievable from cache[0]\[targetSum] states;
     *  (though with recursion callstack overhead its not clear)
     * Space: always O(n*m)
     */
    fun bottomUp(targetSum: Int, nums: IntArray): Int {
        val cache = Array(size = nums.size + 1) { IntArray(size = targetSum + 1) }
        for (startInd in cache.indices) cache[startInd][0] = 1

        for (startInd in nums.lastIndex downTo 0) {
            for (leftSum in 1..targetSum) {
                val newLeftSum = leftSum - nums[startInd]
                val takeCurrent = if (newLeftSum >= 0) cache[startInd][newLeftSum] else 0
                val goNext = cache[startInd + 1][leftSum]
                cache[startInd][leftSum] = takeCurrent + goNext
            }
        }
        return cache[0][targetSum]
    }

    /**
     * we must find the total number of valid combinations + we may take each element an infinite number of times
     * => try DP unbounded knapsack (we have infinite number of different valued items, goal is to count total number of valid
     *  combinations. valid constraint = sum of the combination must be equal X)
     *
     * note that if we reduce the targetSum having chosen different coins on different paths but to the same state,
     *  the number of combinations it'd take to build that state X would intertwine with the number of ways we can achieve
     *  this state of these different paths => the problem is divisible into overlapping subproblems
     *
     * goal: return the number of combinations we can build out of [nums] that sum up to [targetSum]
     *
     * to minimize the work at each state, suppose state is nums[startInd:] and leftSum => since its unbound knapsack,
     *  at each state we might either take another nums\[startInd] coin or skip it and try other coins
     *  and the result number of combinations is the sum of these both ways at each state
     *
     * input state:
     *  startInd: Int - denotes the start of the subarray of [nums] for coins we still consider taking;
     *  leftSum: Int
     *
     * recurrence relation:
     *  dp(startInd,leftSum) = take + skip
     *      take = dp(startInd,leftSum-nums\[startInd])
     *      skip = dp(startInd+1,leftSum)
     *
     * base cases:
     *  - FIRST! leftSum == 0 => return 1, trivially to reduce leftSum to 0 we can only subtract nothing, since nums\[i] > 0
     *   => there's only this one way;
     *  - leftSum < 0 => return 0, nums\[i] > 0, trivially we cant increase the leftSum then no matter what startInd value is;
     *  - startInd == nums.size => return 0, trivially we can't reduce leftSum to 0 with no elements.
     *
     * i.o. we try all possible variations of taking the first coin any valid number of times along with other possible
     *  coins + all promising possible combinations of taking any variations of the second coin along with other coins
     *  etc
     *
     *
     * targetSum is in [0,5000] => relatively small => use an array for caching it
     *
     * Time: average/worst O(n*m)
     *  - we have n*m states;
     *  - at each state we do O(1) time work.
     * Space: always O(n*m)
     *
     * ----------
     *
     * could be optimized to O(m) space since each row's computation depends only on the current row and the next row
     * but at the cost of potentially increasing time const even beyond the diff due to recursive callstack overhead
     */
    fun topDownDp(targetSum: Int, nums: IntArray): Int =
        topDownDpRecursive(
            startInd = 0,
            leftSum = targetSum,
            cache = Array(size = nums.size) { IntArray(size = targetSum + 1) { CACHE_EMPTY_VAL } },
            nums = nums,
        )

    private fun topDownDpRecursive(
        startInd: Int,
        leftSum: Int,
        cache: Array<IntArray>,
        nums: IntArray,
    ): Int {
        if (leftSum == 0) return 1
        if (leftSum < 0 || startInd == nums.size) return 0

        val cachedResult = cache[startInd][leftSum]
        if (cachedResult != CACHE_EMPTY_VAL) return cachedResult

        val takeCurrent = topDownDpRecursive(startInd = startInd, leftSum = leftSum - nums[startInd], cache, nums)
        val goNext = topDownDpRecursive(startInd = startInd + 1, leftSum = leftSum, cache, nums)
        return (takeCurrent + goNext).also { combinations -> cache[startInd][leftSum] = combinations }
    }

    /**
     * can we try greedy?
     *  trivial strategy: each turn take the largest valued coin we can get
     *  problem: what if because of our combination we won't be able to get past some remainder? and if we've chosen
     *   at some point less valued coins we could've gotten the combination?
     *
     *  e.g. target=32   coins=[21,5,4]
     *   -21 = 11, -5=6, -5=1 => can't achieve target
     *   but we could've taken 4x8 => got 0 left
     *   or even take 5x4, left=12, 4x3 => 7 coins, 0 left
     *
     * try dp?
     *
     * WAIT a second => we're NOT asked the MINIMUM amount of combinations => we're asked the TOTAL number of combinations
     *  possible to make that amount.
     *
     * could we try backtracking?
     * we have up to 300 coins = choices, but the amount is pretty low, only 5000
     *
     * whats the worst case for the backtracking here?
     *  - we have a coin == 1 and target == 5000 => 5000 steps, along each we have ~m (m=coins.size) choices => 5000^100 => way. too. much.
     *
     * OK, not backtracking
     *
     * ----------------------------------------
     *
     * try DP
     *
     *
     * (postfactum: DP cause e.g. leftSum=5, coins=[1,2,5], if we take 1x2 then leftSum=3 and if we take 2x1 leftSum=3 as well,
     *  and the number of combinations we can make up leftSum=3 out of coins does not depend on any previous choices =>
     *  can be calculated and cached/retrieved as an individual subproblem)
     *
     * top-down
     *
     * goal: return the amount of combinations possible to make up leftSum out of an infinite amount of any [coins]
     * input state:
     *  leftSum: Int
     * recurrence relation:
     *  dp(leftSum) = for coin in coins: if (coin <= leftSum) sum += dp(leftSum-coin)
     * base cases:
     *  leftSum == 0: return 1
     *  leftSum < 0: return 0
     *
     * Time: always O(n*m)
     *  - m states, where m=target;
     *  - each state we have ~n iterations, where n=coins.size.
     *  worst is 5000*300=15 * 10^5, will pass
     * Space: always O(m)
     *
     * dry run:
     *  amount=5, coins=[1,2,5]
     *
     *  take 1 => 4 left
     *      take 1 => 3 left
     *          take 1 => 2 left
     *              take 1 => 1 left
     *                  take 1 => 0 left
     *                   +1
     *                  X
     *              take 2 => 0 left
     *               +1
     *              X
     *         X
     *     take 2 => 1 left
     *         take 1 => 0 left
     *          +1
     *         X
     *     X
     *
     * 1 + 1 + 1 + 2
     * 1 + 2 + 1 + 1
     * => wrong, duplicates
     *
     * how to avoid duplicate counts of the coins?
     *
     *  trivial slow way: keep track of the current combination via an intarray(size=300) and add each combination into a set
     *   => probably too slow, hard to estimate worst number of combinations, and there's certainly a better way
     *
     *  can we prune somehow before even attempting?
     *
     *   what if we change the recurrence relation? instead of picking each coin once try all the possible amounts of every coin?
     *
     *     dp(leftInd, leftSum):   while(leftSum>0) combinations += dp(leftInd+1, leftSum-coins\[ind] * i++)
     *
     *   and goal then: return the number of combinations of coins[leftInd:] (infinite amount of any) that sum up to [targetSum]
     *
     *   => we guaranteed try each amount of coins exactly once
     *
     *   Time: always O(n*m^2)
     *    - we have ~n*m states;
     *    - how much work for each state? worst is coin=1 and leftSum=m => we have iterations => average/worst O(m) work on each state.
     *   Space: always O(n*m)
     *
     *
     * ------------------------------------
     *
     * dry run
     *
     * amount = 5    coins=[1,2,5]
     *
     * leftInd=0  leftSum=5
     *  take 0x1:
     *
     *  take 1x1:
     *    lI=1 lS=4
     *      take 1x2:
     *        lI=2 lS=2:
     *          0
     *      take 2x2:
     *        lI=2 lS=0
     *          +1
     *  take 1x2:
     *
     *  YES, consider also skipping taking the current coin at all
     *  => dp(leftInd, leftSum):   while(leftSum>0)
     *                                  combinations += dp(leftInd+1, leftSum)
     *                                  leftSum -= coins[leftInd]
     *
     *
     * THE ORDER of base cases is crucial as well.
     *
     * --------- optimization
     *
     * since we always go for the leftInd+1 for the computation of the current subproblem
     *  => we might do bottom-up and optimize the space from O(n*m) to O(m)
     *
     */
    fun slowTopDownDp(targetSum: Int, coins: IntArray): Int = slowTopDownDpRecursive(
        leftInd = 0,
        leftSum = targetSum,
        cache = Array(size = coins.size) { IntArray(targetSum + 1) { -1 } },
        coins = coins,
    )

    private fun slowTopDownDpRecursive(
        leftInd: Int,
        leftSum: Int,
        cache: Array<IntArray>,
        coins: IntArray,
    ): Int {
        if (leftSum == 0) return 1
        if (leftInd == coins.size) return 0

        val cachedResult = cache[leftInd][leftSum]
        if (cachedResult != -1) return cachedResult

        val coin = coins[leftInd]
        val nextLeftInd = leftInd + 1
        var newLeft = leftSum
        var combinations = 0
        while (newLeft >= 0) {
            combinations += slowTopDownDpRecursive(leftInd = nextLeftInd, leftSum = newLeft, cache, coins)
            newLeft -= coin
        }
        return combinations.also { cache[leftInd][leftSum] = it }
    }
}

private const val CACHE_EMPTY_VAL = -1

fun main() {
    println(
        CoinChangeII().topDownDp(
            targetSum = 5,
            nums = intArrayOf(1, 2, 5)
        )
    )
}
