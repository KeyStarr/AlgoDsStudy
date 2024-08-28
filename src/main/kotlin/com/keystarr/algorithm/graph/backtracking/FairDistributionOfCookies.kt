package com.keystarr.algorithm.graph.backtracking

/**
 * ⭐️ a problem that looks like it might have a square/nlogn solution at least, maybe greedy, but actually is only backtracking!
 *  very similar to [com.keystarr.algorithm.graph.MaximalNetworkRank] and [com.keystarr.algorithm.hashing.hashmap.string.WordPattern],
 *  all three looking like there might be more efficient solutions than brute force, yet there are none!
 *  => always start with a brute force, unless you're confident you see a better solution straight ahead (if you do - be real cautious with that!)
 *
 * LC-2305 https://leetcode.com/problems/fair-distribution-of-cookies/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= cookies.size <= 8;
 *  • 1 <= cookies\[i] <= 10^5;
 *  • 2 <= childrenAmount <= cookies.size.
 *
 * Final notes:
 *  • done [backtrack] in 15 mins;
 *  • ⚠️ gave up for the efficient solution at 32 mins, don't see any threads unfortunately;
 *  • turns out the backtracking IS the most efficient (reasonable) approach expected here! We could only do some more
 *   optimization for a const improvement, but that's about it.
 *
 * Value gained:
 *  • wow, unlike some, khm, other problems I went straight for the brute force solution first, cause couldn't straight away
 *   see leads to a better one + reasoned with such small constraints backtracking would be accepted => we could then
 *   go from there. Turns out there isn't a better (reasonable) one! I still would have to prove it though, i guess, in a
 *   real interview setting, but that already quite good.
 */
class FairDistributionOfCookies {


    /**
     * are there tricks/formulas we could use to speed up the solution to at least O(n * m)?
     *
     * ---------
     *
     * what about greedy?
     * - sort the cookies;
     * - give the minimum valued bag to the minimum total sum child.
     *
     * bags = [8,15,10,20,8], childrenAmount = 2
     * sorted = 8,8,10,15,20
     *
     * bagInd=4    childrenSum = [ 38, 23 ]
     * actualAnswer=38
     * expected=31 => WRONG
     *
     * ------------
     *
     * can we group numbers up somehow? something about the average after all?
     *
     * technically we may not give some children any bags, but that is never an optimal choice
     *
     * observations:
     *  - if childrenAmount == bags.size => return bags.min()
     *  - if childrenAmount == bags.size - 1, i.o. we have 1 excessive bag => give 2 smallest bags possible to 1 child, the rest across other children;
     *  - if childrenAmount == bags.size - 2, i.o. we have 2 excessive bags =>
     *
     *
     * => I don't see a more efficient path to solution
     */
//    fun solution(bags: IntArray, childrenAmount: Int): Int {
//
//    }


    /* backtracking */

    private lateinit var bags: IntArray
    private lateinit var childrenToTotalSum: IntArray
    private var childrenAmount = -1

    private var minMaxTotalSum = Int.MAX_VALUE

    /**
     * problem rephrase:
     *  - given:
     *   - cookies: IntArray, where cookies\[i] is the amount of cookies in the ith bag;
     *   - childrenAmount: Int.
     *  - step rules:
     *   - 1 bag of cookies must go to exactly 1 child, cannot be split.
     *  - we have to assign ALL bags to children => 1 child can receive more than one bag.
     *
     *  Goal: assign bags to exactly [childrenAmount] such to get the minimum metric
     *   metric = maximum TOTAL number of cookies any single child got (total = sum across all bags)
     *
     * we need the total number of cookies that any child got to be minimum, but we have to use all cookie bags
     * => we have to distribute all cookie bags as evenly as possible in terms of total amount of cookies each child gets
     *
     * ---------
     *
     * approach #1
     * given the cookies size constraint => try backtracking?
     * then goal is basically to find the best valid combination
     *  valid = each bag is assigned to a child
     *  best = minimum max single child total cookies
     *
     * we could also try sorting cookies first
     *
     * backtracking would go such:
     *  - currentCombination: Array<MutableList<Int>> where comb\[i] are all indices of cookie bags that we gave to the ith child;
     *  - we could sort the input, and give the minimum valued bag remaining to the minimum total sum child;
     *  - try all such combinations, record the min metric and return it.
     *
     * edge cases:
     *  - if childrenAmount == cookies.size => always return cookies.max() => correct as-is.
     *
     *
     * formula division/modulo wouldnt work cause we cant split the bags
     *
     * // TODO: why do some estimate it at k^n?
     * Time: always O(n^m)
     *  - n=bags.size and m=childrenAmount
     *  - number of combinations = n * (n-1) * (n-2) * .. * (n-m), so O(n^m)
     * Space: always O(n+m)
     *  - childrenToTotalSum always takes O(m) space;
     *  - backtracking callstack height is worst always O(n).
     */
    fun suboptimal(bags: IntArray, childrenAmount: Int): Int {
        this.bags = bags
        this.childrenAmount = childrenAmount
        this.childrenToTotalSum = IntArray(size = childrenAmount)
        backtrack(bagsInd = 0)
        return minMaxTotalSum
    }

    private fun backtrack(bagsInd: Int) {
        if (bagsInd == bags.size) {
            val metric = childrenToTotalSum.maxOf { it }
            if (metric < minMaxTotalSum) minMaxTotalSum = metric
            return
        }

        for (i in childrenToTotalSum.indices) {
            childrenToTotalSum[i] += bags[bagsInd]
            backtrack(bagsInd + 1)
            childrenToTotalSum[i] -= bags[bagsInd]
        }
    }
}
