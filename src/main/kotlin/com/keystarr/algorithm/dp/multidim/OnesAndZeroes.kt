package com.keystarr.algorithm.dp.multidim

import kotlin.math.max

/**
 * LC-474 https://leetcode.com/problems/ones-and-zeroes/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= strings.size <= 600;
 *  - 1 <= strings\[i].length <= 100;
 *  - strings\[i] consists of only 0 and 1;
 *  - 1 <= maxZeroes, maxOnes <= 100.
 *
 * Final notes:
 *  - done [topDownDp] surprisingly by myself, in about 40-50 mins, but failed to do the bottom up approach here;
 *  - I still don't quite understand why [topDownDp] gives that much of an improvement with memoization. Like, we still
 *   might have a bunch of inputs that default to 2^n time! Or is it just because this particular test cases are configured
 *   in a way that states often overlap? TODO: revisit, understand deeper;
 *  - tried myself, watched Neet, read a couple of solutions -> failed to understand the bottom-up approach.
 *   ⚠️ failed to even dry run. Decided to postpone it and come back later.
 *
 * Value gained:
 *  - practiced solving a "knapsack 0-1" type problem using 3D top-down DP. Bottom-up space-optimized - TODO: TBD
 */
class OnesAndZeroes {

    fun bottomUp(strings: Array<String>, maxZeroes: Int, maxOnes: Int): Int {
        TODO("couldn't understand now - revisit and try again")
    }

    /**
     * problem rephrase:
     *  - given:
     *   - strings: Array<String>, where each strings\[i]\[j] is either 0 or 1;
     *   - atMostOnes: Int
     *   - atMostZeroes: Int
     *  - goal: find the best valid subset of [strings], return the metric
     *   valid = in the entire subset the amount of ones <= [atMostOnes], the amount of zeroes <= [atMostZeroes]
     *   metric = size
     *   best = max
     *
     * best combination = make X choices
     * subset = we must choose whether to include each original element
     *
     * don't think a simply greedy rule exists here, since no matter whether we have less ones or zeroes remaining there are
     *  multiple of accomplishing the goal with differing trade-offs. Though, basically, since we have constraints on both
     *  1's and 0's, it makes sense to try each time select the string with as little 1's and 0's as possible.
     *  if our remaining quotas aren't equal it also makes sense to prioritize picking such an option that draws from
     *  the least quota the most minimal amount. If there are multiple options that draw same minimum amount from the least quota
     *  => pick the one that draws the least from the other quota.
     *
     * actually it seems very much greedy, might as well try.
     *
     * greedy approach:
     *  - pre-process strings, count both zeros and ones in each string;
     *  - we could use 2 min heaps, one which prioritizes ones and another which prioritizes zeroes, but both break the tie
     *   on minimum impact on the other quota;
     *  - create a set for indices of used strings;
     *  - while we have either zeros or ones:
     *   - based on
     *
     * counter example:
     *
     * atMostZeros = 4
     * atMostOnes = 3
     * strings='0000' '01' '01' '01' '001' '110'
     *
     * WRONG, we'd just get '0000' since it has the least amount of 1's, and we have the least quota on ones, but
     *  actually then we use up all our zeroes quota => the more optimal way here would be to hit the middle ground and t
     *  take 3 '01'.
     *
     * we could try making a greedy choice based on the percentage-based cumulative least impact on the remaining quotas:
     *  0000 - 100% zero, 0% ones
     *  01 - 25% zero, 33% ones
     *  001 - 50% zero, 33% ones
     *  110 - 25% zero, 66% ones
     *
     * but that would be O(n^2) for we'd have to recalculate quotas each time we make a choice => probably there is a better solution
     *  for quotas change unpredictably I don't think we can optimize the next best option selection process (like with a heap or sorting)
     *
     * Time: O(n^2)
     *  worst is the quotas are such that we can take all n=strings.size elements => we do n selections for the next min cumulative
     *   cost str, each checks on average n/2 elements.
     * Space: O(n) for the preprocessing
     *
     * ------------- try DP
     *
     * top-down
     *
     * - goal: find the size of the largest subset of strings[leftInd:] such that there are at most target zeroes and ones
     *
     * - input state:
     *  leftInd: Int
     *  leftZeros: Int
     *  leftOnes: Int
     *
     * - recurrence equation:
     *  dp(leftInd, leftZeroes, leftOnes) = max(
     *   dp(leftInd+1, leftZeros-str.zeros,leftOnes-str.ones),
     *   dp(leftInd+1, leftZeros,leftOnes)
     *  )
     *
     * - base cases:
     *  leftZeros < 0 || leftOnes < 0 || leftInd == strings.size -> return 0
     *
     * Time: O(strings.size * atMostOnes * atMostZeroes)
     *  - leftOnes can take up to 0..leftOnes states;
     *  - leftZeroes can take up to 0..leftZeroes states.
     * Space: O(strings.size * atMostOnes * atMostZeroes)
     *
     * ---
     *
     * bottom-up - just start from the base cases, opposite direction same concept.
     *
     * Time: average/worst O(strings.size * atMostOnes * atMostZeroes)
     *  but worst is still actually O(2^n) - when there are no states that overlap
     * Space: O(strings.size * atMostOnes * atMostZeroes)
     *
     * worst n^2 is 360 000, worst here is 600 * 100 * 100 = 6 000 000
     * so, this solution is even worse.
     *
     * ---
     *
     * ⚠️⚠️ oh, no - I actually forgot to add the used numbers count to the input state!
     *  its 4D then with Time(n^2 * atMostOnes * atMostZeroes
     *  => MLE 51th/72
     *
     * this solution is much-much worse then than that supposed O(n^2) one.
     *
     * (also out of curiosity tried without cache for always 2^n => TLE 24th/72)
     *
     * ----------- can we optimize?
     *
     * can we do a 3D DP? Get rid of the "used" from the input state somehow, collect it, khm, bottom-up?
     *
     */
    fun topDownDp(strings: Array<String>, maxZeroes: Int, maxOnes: Int): Int {
        val options = strings.toOptions()
        return topDownRecursive(
            leftInd = 0,
            leftZeroes = maxZeroes,
            leftOnes = maxOnes,
            cache = Array(size = strings.size) {
                Array(size = maxZeroes + 1) { IntArray(size = maxOnes + 1) { -1 } }
            },
            options = options,
        )
    }


    private fun topDownRecursive(
        leftInd: Int,
        leftZeroes: Int,
        leftOnes: Int,
        cache: Array<Array<IntArray>>,
        options: List<Option>,
    ): Int {
        if (leftZeroes < 0 || leftOnes < 0) return -1
        if (leftInd == options.size || (leftZeroes == 0 && leftOnes == 0)) return 0

        val cached = cache[leftInd][leftZeroes][leftOnes]
        if (cached != -1) return cached

        val nextInd = leftInd + 1
        val option = options[leftInd]
        return max(
            topDownRecursive(
                leftInd = nextInd,
                leftZeroes = leftZeroes - option.zeroes,
                leftOnes = leftOnes - option.ones,
                cache, options,
            ) + 1,
            topDownRecursive(
                leftInd = nextInd,
                leftZeroes = leftZeroes,
                leftOnes = leftOnes,
                cache, options,
            ),
        ).also { result -> cache[leftInd][leftZeroes][leftOnes] = result }
    }

    private fun Array<String>.toOptions() = ArrayList<Option>(size).also { options ->
        forEach { str ->
            var zeroes = 0
            var ones = 0
            str.forEach { char -> if (char == '0') zeroes++ else ones++ }
            options.add(Option(zeroes = zeroes, ones = ones))
        }
    }

    private class Option(val zeroes: Int, val ones: Int)
}

fun main() {
    println(
        OnesAndZeroes().topDownDp(
            strings = arrayOf("10", "0001", "111001", "1", "0"),
            maxZeroes = 5,
            maxOnes = 3,
        )
    )
}
