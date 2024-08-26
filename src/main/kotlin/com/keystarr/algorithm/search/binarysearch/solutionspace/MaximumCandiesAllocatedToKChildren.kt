package com.keystarr.algorithm.search.binarysearch.solutionspace

/**
 * LC-2226 https://leetcode.com/problems/maximum-candies-allocated-to-k-children/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= piles.size <= 10^5;
 *  • 1 <= piles\[i] <= 10^7;
 *  • 1 <= targetPiles <= 10^12.
 *
 * Final notes:
 *  • 🏆 recognized binary search on solution spaces about 5 mins in;
 *  • initial designed [solution] in 20 mins;
 *  • ⚠️⚠️🏅done [solution] by myself in 50 mins. TOO LONG! Goal for binary search on solution spaces meds is 30 mins tops.
 *   Also failed 3 submits;
 *  • what could have I done better?
 *    • could've invested LESS time into checking whether binary search on solution spaces was applicable here?
 *     I intuitively felt that it was the tool, so could've just ran through the reasoning fast? Look at the amount of
 *     considerations I've made just by the sheer volume of text...
 *    • could've understood faster that we have to do binary search in either input case, if the split was possible?
 *     may not have had to study these cases at all, that didn't pay off. I deepened my understanding of the approach though,
 *     but in real interview I should've just understood straight away that it works, could've saved about 10 mins and 1 failed run
 *     on that.
 *    => be easier on BSoSS? check applicability faster? assume more? practice more deeper like here => build more intuition,
 *     look in the right directions faster not having to infer every property as if the first time? 🔥🔥
 *     🔥 yea, I guess more deep practice here = less time to infer properties
 *
 *  • this problem is above average tricky med, on top of just the BSoSS:
 *   • infer using long for sum and for total piles;
 *   • infer the edge case when the answer isn't possible and the need for early return for it (cause otherwise crash divide by 0
 *    or breaking the contract for [countPilesOfNoLessThan)).
 *
 * Value gained:
 * • practiced solving a find best (=max metric) combination choices of choices type problem efficiently using binary search
 *   on solution spaces;
 * • learned that I spend too much time on checking the properties for applicability of that tool => decided I need more
 *  practice on these problems to build intuition / better models;
 * • reinforced that for BSoSS goal=max return right, for goal=min return left. Since max means space is ++++---
 *  and with min space is ----++++, in both cases we end up with left>right (left==right+1), but in goal->max the answer is
 *  right and goal->min the answer is left.
 *
 *   right=2
 *   left=3
 *
 *   right=left-1
 *
 *   left=2
 *   right=1
 *   right=left-1
 */
class MaximumCandiesAllocatedToKChildren {

    // TODO: repeat in 1-2 weeks

    /**
     * retrospect:
     * - one of the core observations is "splitting the piles" usefully means simply that we can distribute X candies
     *  to pile\[i]/X children using just the ith pile.
     *
     * ----
     *
     * problem rephrase:
     *  - given:
     *   - candies: IntArray, where candies\[i] is the amount of candies in the ith candies pile
     *   - childrenCount: Int
     *  - rules:
     *   - pile modification: only split a pile of candies into any number of sub piles (cant merge 2 different piles);
     *   - we must get exactly [targetPiles] even valued piles.
     *  Goal: find the maximum value of a pile, such that we get exactly [targetPiles] piles. Return that value.
     *
     * Is answer guaranteed to exist?
     *  piles=[7]
     *  tP=3
     *  7%3 != 0, BUT we can split 7 into 2 2 2, its that just subpile==1 we simply won't use.
     *
     * can we have targetPiles > sum(piles)?
     *  yes we can => in which case return 0, since there's no way to split them.
     *
     * ------------
     *
     * try binary search on solution spaces, cause we basically find a way to split the piles => to make a combination of
     *  choices such that to maximize some number
     *
     * and I think the solution space looks like ++++---
     *
     * - answer boundaries:
     *  min=0, if its impossible to split piles such that to get even [targetPiles] each of value > 0. 0 is always the backup plan,
     *   always a possible "solution";
     *  max=globally its 10^7, if we have exactly [targetPiles] piles initially of max value, so we split none (cause when we split,
     *   we always reduce).
     *   locally its actually always no greater than piles.max(), since we cant merge => we can never get an answer greater
     *   than the max pre-existing pile's value.
     *
     * - elimination properties:
     *  A. assume there exists a solution such that we can split piles to get [targetPiles] each of value exactly X
     *   => there might exist a solution with the value greater than X
     *   => there is always a solution for each value less than X, cause we can literally get all these piles we got for X
     *    and split each into any lower valued pile
     *   => always go right then.
     *  B. assume its impossible to get X evenly values [targetPiles]:
     *   => then it's impossible to do that for any value greater than X, cause we'd had a case of value higher than X for each
     *    pile that we could each tear down X. And that isn't the case;
     *   => but we might have a solution with value less than X.
     *
     * - how to check if X is possible?
     *  3 major input cases:
     *
     *   1. [targetPiles] == piles.size:
     *    in this case we actually don't need binary search, we just to return the min(piles);
     *    => that we can actually do an early return for with time O(n).
     *
     *    WRONG, we can split the larger piles and may get Y piles of larger size than G original piles mins
     *    => simply not take the original mins and increase the actual min pile value possible!
     *    => we have to consider splitting => WE HAVE TO DO BINARY SEARCH TOO
     *
     *   2. [targetPiles] < piles.size:
     *    => select top max [targetPiles] piles, O(n * log(targetPiles)), return the min of them
     *    or we could just do binary search, it would work too in, well, asymptotically very similar complexity
     *
     *   3. [targetPiles] > piles.size:
     *    we always have to do at least 1 split
     *    => how to check if value X is possible then?
     *    we just try to split each pile into as many of value X as we can and add it to total.
     *    if totalSubpilesOfX >= [targetPiles], answer exists (and possible we could even try greater X!)
     *
     *    time O(n) to check
     *    can't do in O(1) of just total sum floordiv X, since we can't merge any two piles to use the remainders
     *    => have to check how many piles of target size we can make out of each pile individually
     *
     * Edge cases:
     *  - if piles.size() < targetPiles => always return 0, might check in O(n) time =>
     *      we do division on minPileValue, so in that case we'd crash => just early check, early return.
     *  - piles sum => check max sum => 10^5 * 10^7 = 10^12 => store piles sum in Long;
     *  - the maximum total amount of piles is a sum => check max sum => minPileValue min is 1 =>
     *   max amounta piles each max value sum =>  10^5 * 10^7 = 10^12 => use Long to store totalPiles.
     *
     * Time: average/worst O(nlogk)
     *  - best case is [targetPiles] == piles.size, then time O(n);
     *  - average/worst case is [targetPiles] > piles.size, then time is O(nlogk), where k=max possible answer.
     * Space: always O(1)
     *
     * -----
     * failed case #1
     *
     * piles=4 7 5
     * piles.size=3
     * targetPiles=4
     *
     * 3 != 4 and piles.sum()=16 > 4 => do binary search
     *
     * -----
     * failed case #2
     *
     * piles=1 2 3 4 10
     * piles.size=5
     * targetPiles=5
     *
     * actual=1
     * expected=3
     * DUH, if we have even amounta piles and target piles WERE NOT BOUNDED by the min pile since we can just simply
     * one of the larger piles, since that can produce Y piles with larger value then even G min piles we had originally
     * => just take these split results over the original min piles!
     */
    fun solution(piles: IntArray, targetPiles: Long): Int {
        var pilesSum = 0L
        piles.forEach { pilesSum += it }
        if (pilesSum < targetPiles) return 0
        if (pilesSum == targetPiles) return 1

        var left = 1
        var right = piles.maxOf { it }
        while (left <= right) {
            val middle = left + (right - left) / 2
            val totalPiles = piles.countPilesOfNoLessThan(minPileValue = middle)
            if (totalPiles >= targetPiles) {
                left = middle + 1
            } else {
                right = middle - 1
            }
        }
        return right
    }

    private fun IntArray.countPilesOfNoLessThan(minPileValue: Int): Long {
        var totalPiles = 0L
        forEach { pileValue -> totalPiles += pileValue / minPileValue }
        return totalPiles
    }
}

fun main() {
    println(
        MaximumCandiesAllocatedToKChildren().solution(
            piles = intArrayOf(4, 7, 5),
            targetPiles = 4,
        )
    )
}
