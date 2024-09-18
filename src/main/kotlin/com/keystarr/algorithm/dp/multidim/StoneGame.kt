package com.keystarr.algorithm.dp.multidim

import kotlin.math.max
import kotlin.math.min

/**
 * ⭐️ a fun example of a problem efficiently solved by pure observation and reasoning resulting in the most trivial algorithm possible
 *
 * LC-877 https://leetcode.com/problems/stone-game/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= piles.size <= 500, always even;
 *  • 1 <= piles\[i] <= 500;
 *  • sum(piles) is always odd.
 *
 * Final notes:
 *  • done [topDownDp] (unrefactored) by myself in 45 mins knowing already the twist of [StoneGameIII], having learned it 1h before;
 *  • dry ran [topDownDp] before implementing it, which took quite some effort. Didn't understand it (couldn't model past the
 *   basic intuition why it works) before almost entirely, now I feel like I've built the intuition better why it works
 *   and how its done, but still, the full confidence eludes me. Why? How to fix that / understand deep?
 *   UPD: after a night well-slept I think I see it better. Basically its like a game of chess, i.o. Alice tries to model
 *    all possible turns when both choose optimally, but instead of the default dumb human "1-2 turns ahead" she can actually
 *    model literally all possible turns in advance using DP;

 *  • 🔥 my first ever encounter with a DP problem that requires switching maximization/minimization in the decision function
 *   based on some rule. ⚠️⚠️ my brain still breaks some when I try to model out why and how exactly this works, like,
 *   how does the first agent see the world to be able to reason like that, in full depth - too many inter-dependent branches!

 *  • 🔥 my first ever encounter on leetcode with a problem that's solved basically efficiently just by reasoning, with
 *   no real algorithm [efficient]. ⚠️ the proof sounded completely alien to me at first, but now I get it more, I wonder
 *   if I'll ever be able to such acutely observe distinct properties of the problems and quickly in a live setting come up
 *   with such beautiful meaningful reasoning and efficient solutions.
 *
 * Value gained:
 *  • solved a 2-agent game simulation type problem using a top-down min-max DP, and using pure strategy reasoning.
 */
class StoneGame {

    // TODO: full resolve in 2-3 weeks (at least reinforce the reasoning and the proof, if not DP)

    // TODO (optional): try bottom-up DP

    /**
     * The sum of stones is always odd - why is that important?
     *  => there are no ties, i.o. one can't split [piles] in two subsets such that their sum is equal.
     *
     * The size of [piles] is always even - why is that important? That's trickier.
     *  1 2 999 4
     *
     *  On a minimal example (where Alice cannot just take all elements, has to make a deliberate choice) observe that
     *  a simple greedy strategy "pick the greater of 2 options" wouldn't work.
     *
     *  But actually the first player to make the move can dictate the game - Alice can either choose all even numbers, or
     *   all odd numbers, guaranteed. E.g. 1 2 3 4
     *   - Alice can choose either 1 or 4:
     *    - Alice 1 => Bob can take either 2 or 4:
     *     - Bob 2 => Alice takes 3;
     *     - Bob 4 => Alice takes 3.
     *    - Alice 4 => Bob can take either 1 or 3:
     *     - Bob 1 => Alice takes 2;
     *     - Bob 3 => Alice takes 2.
     *
     * And by the first observation, no two subsets of [piles] are of equal sum => either all odd numbers or all even numbers
     *  subset must be greater than the other, and Alice can always choose the greater one!
     *
     * i.o. since both players see all stones at all times, before the start of the game Alice can simply calculate the sum
     *  of all even numbers and the sum of all odd numbers. If former is greater she starts with the 0th pile (first even-indexed),
     *  if latter - she starts with the last (odd indexed) pile, and always wins.
     *
     * 1000 3 4 999
     *
     * Time: always O(1)
     * Space: always O(1)
     *
     * -----
     *
     * Failed initially to understand why / build the real intuition via text solutions or even Neetcode, but a well slept night +
     *  https://www.youtube.com/watch?v=TZ_yIA8BJWw&ab_channel=3.5%D0%B7%D0%B0%D0%B4%D0%B0%D1%87%D0%B8%D0%B2%D0%BD%D0%B5%D0%B4%D0%B5%D0%BB%D1%8E
     *  finally worked.
     */
    fun efficient(piles: IntArray) = true

    /* TOP-DOWN DP */

    private lateinit var piles: IntArray
    private lateinit var cache: Array<Array<IntArray>>

    /**
     * can we do greedy?
     *  not really, cause there might be "anomalies" - huge piles of stones such that they are larger than all or most
     *   other stones combined => we can't just base our choices of off which of the available stones is larger.
     *   e.g. say piles = 3 500 1 2, and Alice goes first. A simple greedy would say "pick greater of available stones"
     *    she'd pick 3 and loose, since Bob would take 500. Alice couldve picked 2, Bob would have to pick either 3 or 1
     *    and then Alice would pick 500 and win => Alice can always win here.
     *  just greedy won't work
     *
     * we make multiple choices, each affects further choices, and we need to find the combination which gives the maximum => try DP
     * how to simulate both players with DP though?
     * keep a single score - simulate the min-max game: all Alice's choices contribute positively, Bob's - negatively.
     *
     * try top-down DP
     * goal: return the total score such that at each step based on who's turn it is, we either maximized or minimized the answer
     *
     * Time: always O(n^2)
     *  - each step we have two choices, and each next step reduces the amount of choices to make by 1 =>
     *   without memoization time complexity is 2^n, where n=piles.size;
     *  - with memoization we have a 3D state, and technically the worst time complexity is O(n^2 * 2) = O(n^2), but
     *   the const reduces that, since:
     *    - for each leftInd=X we only consider rightInd >= X;
     *    - and since we never consider cache[1][0][0] (bob goes first).
     *   in fact, judging by the dry run, it might be so that all the states that are achievable by either moving left
     *    or right inwards and having started with Alice, that is, where left != 0 and right != piles.lasIndex =>
     *    all these "middle" states computed only once.
     *   and the actual time complexity is:
     *    - left=0 and right takes [0:piles.lastIndex];
     *    - right=piles.lastIndex and left takes [0:piles.lastIndex];
     *    - all the middle states, well, it turns out its quite complex to compute how many those are there.
     *     But that all certainly depends on O(n^2).
     *
     * Space: always O(n^2)
     *  - cache takes O(n^2) space;
     *  - max call stack height is n.
     *
     * ------------------
     *
     * It appears that we can simply let go of 1 dimension denoting whose turn it is now - why?
     * Judging by our dry run, those subproblems that duplicate by leftInd/rightInd always have the same isAliceTurn value - why?
     *
     * Doesn't really matter, skip for now, since the most efficient solution here is just "true" :) Save optimization for further
     *  stone games. Skip bottom-up for that same reason too.
     */
    fun topDownDp(piles: IntArray): Boolean {
        this.cache = Array(size = 2) { Array(size = piles.size) { IntArray(size = piles.size) { CACHE_EMPTY_VALUE } } }
        this.piles = piles
        return topDownDpRecursive(
            leftInd = 0,
            rightInd = piles.lastIndex,
            isAliceTurn = true,
        ) > 0
    }

    private fun topDownDpRecursive(
        leftInd: Int,
        rightInd: Int,
        isAliceTurn: Boolean,
    ): Int {
        val sign = if (isAliceTurn) 1 else -1
        if (leftInd == rightInd) return sign * piles[leftInd]

        val turnKey = if (isAliceTurn) 1 else 0
        val cachedResult = cache[turnKey][leftInd][rightInd]
        if (cachedResult != CACHE_EMPTY_VALUE) return cachedResult

        val nextIsAliceTurn = !isAliceTurn
        return pickOptimal(
            isNowAliceTurn = isAliceTurn,
            sign * piles[leftInd] + topDownDpRecursive(
                leftInd = leftInd + 1,
                rightInd = rightInd,
                isAliceTurn = nextIsAliceTurn
            ),
            sign * piles[rightInd] + topDownDpRecursive(
                leftInd = leftInd,
                rightInd = rightInd - 1,
                isAliceTurn = nextIsAliceTurn
            ),
        ).also { cache[turnKey][leftInd][rightInd] = it }
    }

    private fun pickOptimal(isNowAliceTurn: Boolean, o1: Int, o2: Int) = if (isNowAliceTurn) max(o1, o2) else min(o1, o2)
}

private const val CACHE_EMPTY_VALUE = Int.MIN_VALUE

fun main() {
    println(
        StoneGame().topDownDp(piles = intArrayOf(3, 7, 2, 3))
    )
}
