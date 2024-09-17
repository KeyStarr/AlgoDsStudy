package com.keystarr.algorithm

import kotlin.math.max
import kotlin.math.min

/**
 *
 * Final notes:
 *  - ⚠️ done [topDownDp] in 1h10m by myself. Got stuck for ~20 mins ironing out edge cases to return 0/int.min/max where applicable,
 *   I was very much confused and sort of drifting away (замылилось);
 *  - invested over 1h => give up and learn the solution;
 *  -
 *
 *
 */
class StoneGameII {

    /**
     * initially:
     *  M = 1
     *  X either 1 or 2
     *
     * ----
     *
     * suppose Alice at first takes 2 piles
     * M=max(1,2)=2
     *
     * X:[1,4]
     * suppose, Bob then takes 4 piles
     * M=max(2,4)=4
     *
     * X:[1,8]
     * suppose, Alice then takes 16 piles
     * M=max(8,4)=8
     *
     * => if both players take the maximum amount of piles each turn, then we can have up to log2(n) steps.
     *
     * ----
     *
     * suppose Alice at first takes 1 pile
     * M=max(1,1)=1
     *
     * suppose Bob takes 1 pile
     * M=max(1,1)=1
     *
     * => if both players take the minimum amount of piles, we can have exactly n steps.
     *
     * ----
     *
     * observe that all piles are strictly positive => with every pile taken each player increases their score
     *
     * can it be that each has to always take the most amount of piles available?
     *  unlikely, assume we have 30 piles
     *   A=2, B=4, A=8, B=16
     *
     *   the 1st catch is that if the amount of piles is such that, by taking the maximum always, the 1st player would
     *   leave the second player the last turn with the most stones left => they lose
     *
     *  also we might have "anomalies" - e.g. a single pile that is larger than the sum of all other piles combined
     *   => each player must prioritize taking it, if that is possible.
     *
     * we could still try to just simulate the game via DP
     *
     * goal: return the max amount of stones Alice can get, if both play optimally
     * input state:
     *  - leftInd: Int
     *  - maxChoiceHalf: Int
     *  - isAliceTurn: Boolean
     *  result, a pair:
     *   - total sum of Alice max stones and Bob min stones;
     *   - max alice amount of stones.
     *
     * recurrence relation:
     *  dp(leftInd, maxChoiceHalf, isAliceTurn) = if (isAliceTurn) {
     *      max(for (takePiles in 1..min(maxChoiceHalf*2,piles.size)) {
     *          piles.sum(leftInd, leftInd+takePiles-1) + dp(leftInd=leftInd + takePiles, max(maxChoiceHalf,takePiles), isAliceTurn = false) )
     *      }
     *  } else {
     *      min(for (takePiles in 1..min(maxChoiceHalf*2,piles.size)) {
     *          -1 * piles.sum(leftInd, leftInd+takePiles-1) + dp(leftInd=leftInd + takePiles, max(maxChoiceHalf,takePiles), isAliceTurn = true) )
     *      }
     *  }
     *
     * base cases:
     *  - leftInd >= piles.size => return 0,0
     *
     *
     * Time: always O(n^2)
     *  - for each item in the [piles] either Alice or Bob takes it. On Alice turns she has 2M options, Bob has 2M options
     *   as well, and the amount of options may grow each turn M=max(X,M), so we have a varying amount of branches;
     *  - however, since we only compute each state once due to caching => the max number of computations is defined only
     *   via the total max number of states. And since we've established that maxChoiceHalf grows up to ~n => we have n*n*2 states
     * Space:
     *  - what's the callstack height?
     *   the slowest we go when we make exactly 1 choice each time, that gives us piles.size steps
     *   but each step the number of branches may increase
     *
     * ------ optimization
     *
     * - we could convert the map into a 3D array to optimize time const, how though? isAliceTurn is either 0 or 1,
     *  leftInd is in [0:piles.size), but what about maxChoiceHalf - what's the range for that value? Well it starts
     *  always with 1, and it can technically be anything in the range of [1:max], but what's the max?
     *  it grows the fastest only when we pick the maximum available option each turn =>
     *  e.g. size=4 M=1 maxX=2 A=2 B=4 => max was 4
     *  size=128 M=1 maxX=2  A=2 B=4 A=8 B=16 A=32 B=64 A=128
     *  size = 15 A=2 B=4 A=8 B=16
     *
     *  hm, apparently maxChoiceHalf max grows exactly to the closest power of 2 greater than piles.size
     *  or 2^(log2(piles.size).toIntFloor()+1). Hm, not really, since if size=14 we stop at power 8 since 2+8+4=14,
     *  so then it grows apparently up to size+2.
     *
     */
    fun topDownDp(piles: IntArray): Int = topDownDpRecursive(
        leftInd = 0,
        maxChoiceHalf = 1,
        isAliceTurn = true,
        cache = mutableMapOf(),
        piles = piles,
    ).aliceMaxScore

    private fun topDownDpRecursive(
        leftInd: Int,
        maxChoiceHalf: Int,
        isAliceTurn: Boolean,
        cache: MutableMap<CacheKey, Result>,
        piles: IntArray,
    ): Result {
        if (leftInd == piles.size) return Result(0, 0)

        val cacheKey = CacheKey(isAliceTurn, leftInd, maxChoiceHalf)
        val cachedResult = cache[cacheKey]
        if (cachedResult != null) return cachedResult

        val sign = if (isAliceTurn) 1 else -1
        var minMaxResult = Result(if (isAliceTurn) Int.MIN_VALUE else Int.MAX_VALUE, 0)
        val nextIsAliceTurn = !isAliceTurn
        var currentPileSum = piles[leftInd]
        for (takePiles in 1..min(maxChoiceHalf * 2, piles.size - leftInd)) {
            val nextPileInd = leftInd + takePiles
            val result = topDownDpRecursive(
                leftInd = nextPileInd,
                maxChoiceHalf = max(takePiles, maxChoiceHalf),
                isAliceTurn = nextIsAliceTurn,
                cache, piles,
            )
            val newTotalScore = result.totalMinMaxScore + currentPileSum * sign
            if (isTakeFirst(isAliceTurn, newTotalScore, minMaxResult.totalMinMaxScore)) {
                minMaxResult = Result(
                    totalMinMaxScore = newTotalScore,
                    aliceMaxScore = result.aliceMaxScore + if (isAliceTurn) currentPileSum else 0
                )
            }
            if (nextPileInd != piles.size) currentPileSum += piles[nextPileInd]
        }

        return minMaxResult.also { cache[cacheKey] = it }
    }

    private fun isTakeFirst(isAliceTurn: Boolean, r1: Int, r2: Int): Boolean =
        if (isAliceTurn) max(r1, r2) == r1 else min(r1, r2) == r1

    private class Result(
        val totalMinMaxScore: Int,
        val aliceMaxScore: Int,
    )

    private data class CacheKey(
        val isAliceTurn: Boolean,
        val leftInd: Int,
        val maxChoiceHalf: Int,
    )
}

fun main() {
    println(
        StoneGameII().topDownDp(
            piles = intArrayOf(2, 7, 9, 4, 4)
        )
    )
}
