package com.keystarr.algorithm.dp.gametheory

import kotlin.math.max
import kotlin.math.min

/**
 * ⭐️ a great example of a min-max DP problem with an actual algorithm required (looking at you [StoneGame])
 * LC-1140 https://leetcode.com/problems/stone-game-ii/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= piles.size <= 100;
 *  • 1 <= piles\[i] <= 10^4.
 *
 * Final notes:
 *  • ⚠️ done [topDownDp] in 1h10m by myself. Got stuck for ~20 mins ironing out edge cases to return 0/int.min/max where applicable,
 *   I was very much confused and sort of drifting away (замылилось);
 *  • invested over 1h => give up and learn the solution;
 *  • ⚠️⚠️ note: I find the refactored [topDownDp] version reasonable, but I'm not 100% confident in it, it may fail, I've
 *   not proven it fully => but I still submit. But like I'm confident ENOUGH that it probably is based on the correct reasoning.
 *   Is it OK to do that? ❓(turns out it was correct);
 *  • huh, apparently top-down here is much faster than bottom-up, cause we only compute the actual achievable states which
 *   may be on average much less in count than computing all 2D states possible. Or is it only for 2D bottom-up, is 1D bottom-up faster than top-down?
 *  • skipped further optimizations for now, cause I'm short on time and it takes here too much effort vs projected value rn.
 *
 * Value gained:
 *  • practiced solving a 2 players game simulation type problem suboptimally using min-max top-down dp.
 */
class StoneGameII {

    // TODO: optimize top-down, use prefix sum + cut short when the remaining player takes all stones + get rid of Array(size=2)
    // TODO: do bottom-up, at least with a 2D state (optionally - 1D)
    //  tried, for now failed to understand in reasonable time and effort why we may not keep track of whose turn it is and simply max
    // TODO: is top-down here better? why? when is generally then bottom-up better, and when is top-down?

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
     * Time: always O(n^3)
     *  - for each item in the [piles] either Alice or Bob takes it. On Alice turns she has 2M options, Bob has 2M options
     *   as well, and the amount of options may grow each turn M=max(X,M), so we have a varying amount of branches;
     *  - however, since we only compute each state once due to caching => the max number of computations is defined only
     *   via the total max number of states. And since we've established that maxChoiceHalf grows up to ~n => we have n*n*2 states
     *  WRONG - we indeed have up to n^2 states BUT at each state we iterate average through X values, where X~piles.length
     *  (though usually much less than it) => due to this loop on each state we have O(n^3)
     * Space: always O(n^2) since
     *  - what's the callstack height?
     *   the slowest we go when we make exactly 1 choice each time, that gives us piles.size steps
     *  - cache is n^2
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
     *  => then maxChoiceHalf grows apparently up to size+2.
     *  ⚠️hm, that was partially correct!
     *  TODO: this reasoning is not precise. I learned through testing that on average maxChoiceHalf is at most the lower
     *   power of 2 plus some. Only on piles.size==1 does it reach piles.size, always less than it
     *   Why? Couldn't figure out in reasonable time
     *
     * - also we could get rid of Result and simply make Bob minimize Alice's score. By taking piles Bob simply denies
     *  these to Alice => Alice has to choose from the rest, and Bob chooses such that Alice's score is the minimum possible
     *  (which always means that Bob's score is maximum possible, since he takes all piles Alice didn't get)
     *  => replace result object with a single int => reduce both space and time const (reduce object allocation + GC)
     *  ✅ YAS! That was correct, min/max just the Alice's score simulating both players optimal choices.
     */
    fun topDownDp(piles: IntArray): Int = topDownDpRecursive(
        leftInd = 0,
        maxChoiceHalf = 1,
        isAliceTurn = true,
        cache = Array(size = 2) { Array(size = piles.size) { IntArray(size = piles.size) { -1 } } },
        piles = piles,
    )

    /**
     * goal: return the maximum possible score that Alice can get
     */
    private fun topDownDpRecursive(
        leftInd: Int,
        maxChoiceHalf: Int,
        isAliceTurn: Boolean,
        cache: Array<Array<IntArray>>,
        piles: IntArray,
    ): Int {
        if (leftInd == piles.size) return 0

        val aliceTurnKey = if (isAliceTurn) 1 else 0
        val cachedResult = cache[aliceTurnKey][leftInd][maxChoiceHalf]
        if (cachedResult != -1) return cachedResult

        var minMaxAliceScore = if (isAliceTurn) Int.MIN_VALUE else Int.MAX_VALUE
        val nextIsAliceTurn = !isAliceTurn
        var currentPileSum = piles[leftInd]
        for (takePiles in 1..min(maxChoiceHalf * 2, piles.size - leftInd)) {
            val nextPileInd = leftInd + takePiles
            val nextAliceMaxScore = topDownDpRecursive(
                leftInd = nextPileInd,
                maxChoiceHalf = max(takePiles, maxChoiceHalf),
                isAliceTurn = nextIsAliceTurn,
                cache, piles,
            ) + (if (isAliceTurn) currentPileSum else 0)
            minMaxAliceScore = takeOptimal(isAliceTurn, nextAliceMaxScore, minMaxAliceScore)
            if (nextPileInd != piles.size) currentPileSum += piles[nextPileInd]
        }

        return minMaxAliceScore.also { cache[aliceTurnKey][leftInd][maxChoiceHalf] = it }
    }

    private fun takeOptimal(isAliceTurn: Boolean, r1: Int, r2: Int) = if (isAliceTurn) max(r1, r2) else min(r1, r2)
}

fun main() {
    val input = mutableListOf<Int>()
    repeat(126) { input.add(3) }

    println(
        StoneGameII().topDownDp(
            piles = input.toIntArray()
        )
    )
}
