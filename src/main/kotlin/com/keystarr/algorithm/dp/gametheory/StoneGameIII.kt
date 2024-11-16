package com.keystarr.algorithm.dp.gametheory

import kotlin.math.max
import kotlin.math.min

/**
 * LC-1406 https://leetcode.com/problems/stone-game-iii/
 * difficulty: medium
 * constraints:
 *  • 1 <= piles.size <= 5*10^4;
 *  • -10^3 <= piles\[i] <= 10^3.
 *
 * Final notes:
 *  • tried to solve this pattern first in the context of the Leet DSA course bonus problems run, but failed miserably.
 *   Reason - never seen DP problems with alternating optimization conditions at each step, a great experience!
 *   Went to solve [com.keystarr.algorithm.dp.multidim.StoneGame] and [com.keystarr.algorithm.dp.multidim.StoneGameII] first,
 *   at least to get the core idea and do the maybe suboptimal but correct good enough solutions, and fully understand those;
 *  • understood well enough top-down on all Stone Game I,II,III but completely failed to understand bottom-up on all of these!
 *   ⚠️❓ why is it so much harder here then for most of the previous DP problems I've solved?
 *
 * Value gained:
 *  • practiced solving a "2 optimal players multi-choice simulation" type problem using top-down DP.
 */
class StoneGameIII {

    // TODO: understand and implement bottom up, then space-optimized bottom-up
    //  at the time of writing couldn't really get the idea why we don't need turn dimension for caching in reasonable time
    //  (couldn't quite get why [wrongBottomUp] doesn't work either, fix it later)

    fun wrongBottomUp(piles: IntArray): String {
        val cache = Array(size = 2) { IntArray(size = piles.size + 1) }
        for (startInd in piles.lastIndex downTo 0) {
            for (turnKey in 0..1) {
                val isAlice = turnKey.toBoolean()
                var minMaxResult = if (isAlice) Int.MIN_VALUE else Int.MAX_VALUE
                var currentSum = 0
                val sign = if (isAlice) 1 else -1
                val nextTurnKey = (!isAlice).toInt()
                for (takeStones in 0..min(2, piles.size - startInd - 1)) {
                    currentSum += piles[startInd]
                    minMaxResult = minMax(
                        isAlice,
                        minMaxResult,
                        sign * currentSum + cache[nextTurnKey][startInd + takeStones + 1]
                    )
                }
                cache[turnKey][startInd] = minMaxResult
            }
        }
        val result = cache[1][0]
        return when {
            result > 0 -> "Alice"
            result < 0 -> "Bob"
            else -> "Tie"
        }
    }

    private fun Boolean.toInt() = if (this) 1 else 0

    private fun Int.toBoolean() = this == 1


    /**
     * 2 catches here:
     *  - stone value can be negative, and since both players maximize their score => both have to choose such that the
     *   balance of the stones they take remains the highest;
     *  - there could be a tie.
     *
     * Greedy won't work here, at least I don't see how it could, since based on locally all the visible choices (3 stones),
     *  either player would then: if all 3 are positive, always take all 3, otherwise minimize loses (take only 1,2 or 3 such
     *  that the balance is the highest). But that wouldn't work, consider a simple example of:
     *  1 2 3 4 999
     *  such a greedy Alice would take 6 and lose 999 to Bob. But actually the best strategy for Alice is to take only 1,
     *  then no matter what Bob chooses Alice can take 999 and win.
     *
     * We need to find the best combination of choices and each choice affects further ones (e.g. no matter how many stones
     *  Alice takes she can never take the next one (if it exists), and may lose some further stones).
     *  + subproblems overlap, e.g. in the example above Alice could take 1, Bob 2 and 3, Alice 4; or Alice could take 1 and 2,
     *  Bob 3 and Alice 4, and no matter what came before, actually from 4 "the max score" for either Alice or Bob would remain the same
     *  => the subproblem can be solved separately.
     *
     * => try all combinations efficiently via DP
     *
     * try top-down
     *
     * edge cases:
     *  - all values in [piles] are negative. Then if Alice wins the value would actually be positive, since we add to the total score
     *   all Bob's values with an inverted sign => if he at the end he has a lower negative score than Alice, the result would
     *   actually be totalScore = aliceScore - (bobScore), bobScore < aliceScore, bobScore & aliceScore < 0 => result > 0,
     *   so correct, and vice versa for Bob's win.
     *
     * Time: always O(2*n*3) = O(n)
     *  - we have 2*n states to compute for and for most states we do 3 iterations (maximum amount of further options).
     * Space: always O(n)
     */
    fun topDownDp(piles: IntArray): String {
        val result = topDownDpRecursive(
            startInd = 0,
            isAliceTurn = true,
            cache = Array(size = 2) { IntArray(size = piles.size) { CACHE_EMPTY_VALUE } },
            piles = piles,
        )
        return when {
            result > 0 -> "Alice"
            result < 0 -> "Bob"
            else -> "Tie"
        }
    }

    /**
     * goal: return the total score of the game when both Alice and Bob play optimally (+Alice, -Bob, optimally=maximize their own score)
     *  since we need to maximize the score of 2 players (both play optimally), and we need to declare the winner =>
     *  let's simply keep a single total score of the game: Alice's choices impact it positively and Bob's - negatively.
     *
     * input state:
     *  - startInd: Int
     *  - isAliceTurn: Boolean - if Alice, add to the total score and choose via maximization; if Bob, subtract and minimize.
     *
     * recurrence relation:
     *  dp(startInd, isAliceTurn) = minMax(for (takeStones in 0..min(3, piles.size-startInd-1)) +-currentSum + dp(startInd+takesStones,!isAliceTurn))
     *
     * edge cases:
     *  - total score = sum => max sum is when all numbers in [piles] are max positive = 10^4 * 10^3=10^7 fits into int.
     */
    private fun topDownDpRecursive(
        startInd: Int,
        isAliceTurn: Boolean,
        cache: Array<IntArray>,
        piles: IntArray,
    ): Int {
        if (startInd == piles.size) return 0

        val turnKey = if (isAliceTurn) 1 else 0
        val cachedResult = cache[turnKey][startInd]
        if (cachedResult != CACHE_EMPTY_VALUE) return cachedResult

        var currentSum = 0
        var minMaxScore = if (isAliceTurn) Int.MIN_VALUE else Int.MAX_VALUE
        val nextIsAliceTurn = !isAliceTurn
        val sign = if (isAliceTurn) 1 else -1
        for (i in 0 until min(3, piles.size - startInd)) {
            currentSum += piles[startInd + i]
            minMaxScore = minMax(
                isAliceTurn = isAliceTurn,
                sign * currentSum + topDownDpRecursive(
                    startInd = startInd + i + 1,
                    isAliceTurn = nextIsAliceTurn,
                    cache,
                    piles
                ),
                minMaxScore,
            )
        }
        return minMaxScore.also { cache[turnKey][startInd] = it }
    }

    private fun minMax(isAliceTurn: Boolean, o1: Int, o2: Int) = if (isAliceTurn) max(o1, o2) else min(o1, o2)

    /**
     * My initial blind take on the problem - was history :D
     *
     * ---------
     *
     *
     * each actor makes a choice each step, and each choice affects further choices => try DP.
     * I don't think there's any simple greedy principle here
     *
     * ------- top-down DP, goal - max Alice score
     *
     * try top-down
     *
     * goal: return the maximum sum that Alice can get (starting first) (simulating 1 actor gives us almost the full answer here)
     *  return value Int
     * input state:
     *  - leftInd: Int, denoting the subarrays of stones[leftInd:] - all the stones that are left in the game;
     * recurrence relation:
     *  Alice starts first and at each step she can take either 1, 2 or 3 stones which are left.
     *  let's try all available options at each step and pick the max sum result of these
     *
     *  dp(leftInd) = max(
     *   dp(leftInd) + stones\[leftInd],
     *   dp(leftInd) + sum(stones\[leftInd:leftInd+1]),
     *   dp(leftInd) + sum(stones\[leftInd:leftInd+2])
     *  )
     *
     *  ofc respecting the leftInd < stones.size
     *
     * base cases:
     *  leftInd == stones.size => return 0
     *
     * --------- the entire algorithm
     *
     *
     * 1. calculate the sum of [stones];
     * 2. find via DP the max sum Alice can take;
     * 3. bobMaxScore = stones
     *  return when:
     *   aliceMaxScore > halfStones -> "Alice"
     *   aliceMaxScore < (halfStones + sum % 2) -> "Bob"
     *   else -> "Tie"
     *
     * edge cases:
     *  - sum(stones) is odd alice takes either sum/2 or sum/2+1 stones => if alice took (sum/2+1) then we hit the first
     *   condition in #3, correct; if alice took (sum/2) then Bob wins, cause he took (sum/2+1)
     *
     *
     * its optimal to maximize only Alice's score, even though both players play optimally - since Alice starts,
     *  if she makes an optimal choice at each step, Bob's choices don't matter
     *
     *  OR DO THEY? well, actually they might! Since I predict that Bob probably can make a choice that ruins Alice's
     *   most optimal choice at step X.
     *
     * ----------- oh, we have to simulate both players choices optimally: try complex greedy, no DP?
     *
     *  basically we can run DP and find out the first most optimal move for Alice out of all possible moves, but
     *  if Bob makes such a choice that breaks Alice's second step in that predicted strategy => we no longer know whether
     *  Alice's first choice was optimal or not.
     *
     *  can we simulate then both? First Alice's most optimal choice, then Bob's etc?
     *
     *  what is the most optimal choice for either anyway?
     *   - well, we can have bombs like -999 which outmatch the sum of all other values => neither Bob nor Alice want
     *    to land on these, and would do their best to set another up for such mines;
     *   - also there might be same things but with the highest value like 999, again, higher than all other stones sum
     *    combined => either would want to land up on these and deny the other;
     *   - aside from that, either wants to collect the stones such that the total sum is the largest. This can either mean:
     *    - collecting a lot of smaller value stones, e.g. like if all stones value are equal then the best strategy would be to
     *     just take as many stones at each step for either player;
     *    - all collecting local max stones as much as possible.
     *
     * should we model at the smallest input values - one step first?
     *
     * stones.size < 3:
     *  - all stones\[i] > 0 -> Alice must take all three to leave Bob with score 0;
     *  - all stones\[i] < 0 ->
     *      -1 -2 -3 -> Tie.
     *       Most optimal for Alice is to take first two stones, as to minimize her losses. Since if she takes
     *       -1, Bob then takes -2 in an effort to do the same, and Alice then is forced to take -3 and loses.
     *
     *      -100 -50 -30 -> Bob always wins.
     *       Once again Alice can only try to minimize the loses, but this time take only the 1st stone. As 1st and 2nd = -150
     *       Bob minimizes his loses and only takes -50. Alice then is forced to take -30 and loses with -130
     *
     *      -100 1 3 -> Alice always loses, since she has to take at least one stone and starts first
     *
     *      3 3 3 -> Alice always wins since she can simply take all 3 stones left, and all are positive.
     *       3 3 3 3 -> Alice still always wins with the same first move
     *
     *       3 3 3 10 -> Bob always wins, since 10 is always out of reach for her, no matter the move, and the best
     *        she can do is to take all three (since all are positive) => 9 < 10
     *
     *      5 1 10 3 1 1 1 ->
     *
     *      5 1 10 3 -1000
     *
     *      -1 -100 -10 3 1 1 1000 -> Alice always must take the first three stones since in doing so
     *
     *
     * It seems that if we are to simulate both players => it has to be a complex greedy decision tree, no?
     *  DP doesn't seem to make sense since if just try to maximize Alice's winnings with disregard for Bob's strat, we
     *  will probably make choices that contradict Bob's most optimal behavior => the result wouldn't be optimal.
     *
     *
     * ----------- ok, try DP but with a single score that both actors are trying to pull the best into opposite directions?
     *
     * goal: return the total score = Alice max score - Bob max score
     * input state:
     *  leftInd: Int
     *  isAliceTurn: Boolean
     * recurrence relation:
     *  dp(leftInd, isAliceTurn) = if (isAliceTurn) {
     *   max(
     *    dp(leftInd+1) + stones[leftInd], false)
     *    dp(leftInd+2) + sum(stones[leftInd:leftInd+1]), false),
     *    dp(leftInd+3) + sum(stones[leftInd:leftInd+2]), false),
     *   )
     *  } else {
     *   min(
     *    dp(leftInd+1) - stones[leftInd], true),
     *    dp(leftInd+2) - sum(stones[leftInd:leftInd+1]), true),
     *    dp(leftInd+3) - sum(stones[leftInd:leftInd+2]), true),
     *   )
     *  }
     */
    fun topDownDpTerrible(stones: IntArray): String {
        val cache = Array(size = 2) { IntArray(size = stones.size) { CACHE_EMPTY_VALUE } }
        val totalScore = topDownDpRecursiveTerrible(
            leftInd = 0,
            isAliceTurn = true,
            cache = cache,
            stones = stones,
        )
        println(totalScore)
        return when {
            totalScore > 0 -> "Alice"
            totalScore < 0 -> "Bob"
            else -> "Tie"
        }
    }

    // horrible, horrible code :D wrote it while flashed by a rare glimpse of true desperation, since the problem was so hard
    // to do by myself and even understand the solution at first. Gonna leave it here then for funsies
    private fun topDownDpRecursiveTerrible(
        leftInd: Int,
        isAliceTurn: Boolean,
        cache: Array<IntArray>,
        stones: IntArray,
    ): Int {
        if (leftInd == stones.size) return 0

        val turnKey = if (isAliceTurn) 1 else 0
        val cachedResult = cache[turnKey][leftInd]
        if (cachedResult != CACHE_EMPTY_VALUE) return cachedResult

        return (if (isAliceTurn) {
            max(
                topDownDpRecursiveTerrible(leftInd = leftInd + 1, isAliceTurn = false, cache, stones) + stones[leftInd],
                max(
                    if (leftInd + 1 < stones.size) topDownDpRecursiveTerrible(
                        leftInd = leftInd + 2,
                        isAliceTurn = false,
                        cache,
                        stones
                    ) + stones[leftInd] + stones[leftInd + 1] else 0,
                    if (leftInd + 2 < stones.size) topDownDpRecursiveTerrible(
                        leftInd = leftInd + 3,
                        isAliceTurn = false,
                        cache,
                        stones
                    ) + stones[leftInd] + stones[leftInd + 1] + stones[leftInd + 2] else 0,
                )
            )
        } else {
            min(
                topDownDpRecursiveTerrible(leftInd = leftInd + 1, isAliceTurn = true, cache, stones) + stones[leftInd],
                min(
                    if (leftInd + 1 < stones.size) topDownDpRecursiveTerrible(
                        leftInd = leftInd + 2,
                        isAliceTurn = true,
                        cache,
                        stones
                    ) - stones[leftInd] - stones[leftInd + 1] else 0,
                    if (leftInd + 2 < stones.size) topDownDpRecursiveTerrible(
                        leftInd = leftInd + 3,
                        isAliceTurn = true,
                        cache,
                        stones
                    ) - stones[leftInd] - stones[leftInd + 1] - stones[leftInd + 2] else 0,
                )
            )
        }).also { result -> cache[turnKey][leftInd] = result }
    }
}

private const val CACHE_EMPTY_VALUE = Int.MIN_VALUE

fun main() {
    println(
        StoneGameIII().wrongBottomUp(intArrayOf(1, 2, 3, 6))
    )
}
