package com.keystarr.algorithm

/**
 * 💣 failed to recognize and EVEN struggled to design given the approach => full reinforce later
 * LC-2517 https://leetcode.com/problems/maximum-tastiness-of-candy-basket/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= subarrayLength <= prices.length <= 10^5;
 *  • 1 <= price\[i] <= 10^9.
 *
 * Final notes:
 *  • couldn't solve by myself in 1h [solution] => read all 4 hints, all hinting at details of binary search on solution spaces
 *   => straight away didn't understand how that can even work => dry ran, understood somewhat, at least proved with models
 *   [efficient], but didn't yet quite grasp the full intuition still (have to constantly use 2nd system to "touch" the design,
 *   to get how it works);
 *  • best valid subset => binary search on solution spaces is most efficient, huh. Didn't even think about it, again!
 *   Just like in [com.keystarr.algorithm.search.binarysearch.solutionspace.MinimumNumberOfDaysToMakeMBouquets] in regard to
 *   not even thinking about the binary search (failed during practice as well), though there the problem asked for the best valid subarray;
 *  • why didn't I even think to try binary search on solution spaces here though?????
 *
 * Value gained:
 *  • practiced recognizing and implement binary search on solution spaces.
 */
class MaximumTastinessOfCandyBasket {

    // TODO: full solve in 1-2 weeks

    /**
     * Binary search on solution spaces:
     *  - goal: maximize metric
     *   metric = min abs diff between any two elements
     *  - min possible tastiness: 0
     *  - max possible: 10^5 (or just bound by prices.max())
     *  - elimination properties:
     *   - we can't check fast whether a subset with metric exactly X exists, but we can quickly check if a subset
     *    with metric no less than X exists;
     *   if a subset with metric greater or equal to X AND size exactly k
     *    - exists =>
     *     - there always is a subset with metric less than X (at least this very one we've found, its at least X, so its great than any less)
     *     - there might be a subset with metric greater than X
     *     => increase X
     *    - doesn't exist =>
     *     - trivially, no subset with metric greater than X exists;
     *     - but there might be a subset with metric less than X
     *     => reduce X.
     *  - how do we check if a subset with metric no less than X exists?
     *   sort the [prices], always pick the first element and iterate from left to right, pick exactly (k-1) elements,
     *   each only if (prices\[i] - lastPickedElement) >= X.
     *   so, preprocessing O(nlogn) and each check is O(n)
     *
     * Time: O(nlogn + nlogk) = O(n*(logn+k))
     *  - sorting input O(nlogn)
     *  - worst checks O(logk)
     *  - cost of 1 check is O(n)
     * Space: sort O(logn)
     */
    fun efficient(prices: IntArray, k: Int): Int {
        prices.sort()

        var left = 0
        var right = prices.maxOf { it }
        while (left <= right) {
            val metric = left + (right - left) / 2
            println(metric)
            if (isThereSubsetWithMetricNoLessThan(metric, prices, k)) {
                left = metric + 1
            } else {
                right = metric - 1
            }
        }
        return left
    }

    private fun isThereSubsetWithMetricNoLessThan(metric: Int, prices: IntArray, k: Int): Boolean {
        var lastElement = prices[0]
        var takenCount = 1
        for (i in 1 until prices.size) {
            if (prices[i] - lastElement >= metric) {
                lastElement = prices[i]
                takenCount++
                if (takenCount == k) return true
            }
        }
        return false
    }

    /**
     * problem rephrase:
     *  - given:
     *   - prices: integer array
     *   - k: length of the subarray
     *  Goal: find the best valid SUBSET, return it's "tastiness"
     *   valid=length [subarrayLength]
     *   best = maximum "tastiness"
     *    "tastiness" = min abs diff of any two elements in the subset
     *
     * brute force: try all possible subsets, compute the sweetness for each and save the max one
     * time: O(n*(n-1)*(n-2)*..*(n-k))
     *
     * 3 1 10
     *
     * if we choose an element X to put into the subset => how does it affect further choices?
     * we need to find an element out of the remaining ones with the max abs diff with all the elements in the subset
     *
     * each step we make a choice => might be greedy, might be DP
     *
     * ------------------------
     *
     * we need to keep the min abs distance as maximum as possible
     * =>
     * subarrayLength==0 or 1 => invalid
     * subarrayLength==2 => choose the min and the max elements (max abs diff)
     * subarrayLength==3 => for the 3rd element choose the element closest to the average between already chosen min and max
     * subarrayLength==4 => for the 4rth choose an element closest to averages |1st - 2nd| or |2nd - 3rd|
     *
     * we'd have subarrayLength iterations, each:
     *  - compute the averages => average O(subsetSize);
     *  - for each remaining element (average ~n elements) find the min abs diff with the averages (~subsetSize iterations).
     * => Time: O(subsetSize+n*subsetSize)=O(n*subsetSize)
     *
     * 13,5,1,8,21,2
     * k=2 => 21,1
     * k=3 => 21 2 13 or 21 5 13
     *
     * ----------
     *
     * might there be a better one? time O(n+k)?
     *
     * sort the [prices].
     *
     * 21 13 5 1 8 2
     * =>
     * 1 2 5 8 13 21
     *  1 3 3 5 8
     *     4
     *
     * 1 20 23 28, k = 2
     *  19 3  5
     *
     *
     *
     * 1st and 2nd: choose min and max
     * 3rd: choose the number in the middle
     * 4th: choose the number with the min absDiff to
     *
     *
     */
    fun solution(prices: IntArray, subsetSize: Int) {
        val subset = mutableListOf(prices.minOf { it }, prices.maxOf { it })

        var min = prices[0]
        var minInd = 0
        var max = prices[1]
        var maxInd = 0
        prices.forEachIndexed { ind, num ->
            if (num > max) {
                max = num
                maxInd = ind
            }
            if (num < min) {
                min = num
                minInd = ind
            }
        }
        subset.apply {
            add(min)
            add(max)
        }

        val used = BooleanArray(size = prices.size).apply {
            set(minInd, true)
            set(maxInd, true)
        }
        repeat(subsetSize - 2) {
            val averages = mutableListOf<Int>()
            for (i in 1 until subset.size) {
                averages.add(subset[i] - subset[i - 1])
            }

            prices.forEachIndexed { ind, price ->
                if (used[ind]) return@forEachIndexed
                var minDiff = Int.MAX_VALUE
                var minDiffInd = -1
                averages.forEach { average ->

                }
            }
        }

    }
}

fun main() {
    println(
        MaximumTastinessOfCandyBasket().efficient(
            prices = intArrayOf(13, 5, 1, 8, 21, 2),
            k = 3,
        )
    )
}
