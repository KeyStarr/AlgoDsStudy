package com.keystarr.algorithm.greedy

/**
 * LC-1833 https://leetcode.com/problems/maximum-ice-cream-bars/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= costs.size <= 10^5;
 *  • 1 <= costs\[i] <= 10^5;
 *  • 1 <= coins <= 10^8.
 *
 * Final notes:
 *  • done [greedy] in 3 mins. Obviously this problem is only medium if one does counting sort, but since I haven't learned
 *   that concept yet, I can't. No idea why this problem is in DSA leet course practice interleaving session. Maybe just
 *   to warm up on the greedy problems.
 *
 * Value gained:
 *  - none yet! must do counting sort later then.
 */
class MaximumIceCreamBars {

    // TODO: solve via counting sort after learning the concept

    /**
     * maximize elements count chosen with subtracting from coins for each element => choose as many minimum elements
     * as we can.
     *
     * 1. sort ascending
     * 2. add elements as long as coins > 0
     *
     * Time: O(nlogn)
     * Space: O(logn) for sorting
     */
    fun greedy(costs: IntArray, coins: Int): Int {
        costs.sort()
        var coinsLeft = coins
        var bought = 0
        costs.forEach { cost ->
            coinsLeft -= cost
            if (coinsLeft < 0) return bought
            bought++
        }
        return bought
    }
}
