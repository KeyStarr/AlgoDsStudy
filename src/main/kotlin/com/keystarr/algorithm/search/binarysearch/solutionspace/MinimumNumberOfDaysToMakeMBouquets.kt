package com.keystarr.algorithm.search.binarysearch.solutionspace

/**
 * ⭐️💣 retry later, in the general mix. Though since the problem was recognition, I guess that won't be effective,
 *  since I remember that this specific problem is binary search now.
 * LC-1482 https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= bouquetCapacity < bloomDays.size <= 10^5;
 *  • 1 <= bloomDays\[i] <= 10^9;
 *  • 1 <= targetBouquets <= 10^6.
 *
 * Final notes:
 *  • solved with 2 hints (from Leet interface) via 1.5h. From the time I've opened both hints to the time I've submitted,
 *   it probably took me 20-30 mins. So for about 1 hour I just tried wrong approaches;
 *  • abstracted the problem goal: find the best valid subarrays combination. Tried greedy, then felt there's something about
 *   the repeated maxes here and tried monotonic stack; its a combination, and if we take flower X in the bouquet Y,
 *   we can't use it anymore => tried DP; tried simple bruteforce combinatorics. Failed all these :)
 *  • read hint 1, didn't understand it. Read hint 2, and then it just clicked, that hints were talking about properties
 *   of binary search on solution spaces that this problem conforms to;
 *  • designed binary search on solutions spaces straight away, implemented 1st try submit.
 *
 * Value gained:
 *  • practiced binary search on solution spaces. Designed and implemented fine, but failed to recognize by myself.
 *   => need to put in more work, practice more to recognize it in the wild, perhaps, update my hooks for when to consider that approach.
 *    Didn't even think about it at all.
 */
class MinimumNumberOfDaysToMakeMBouquets {

    /**
     * used hint #1: if at day X we can make all [targetBouquets], then at any day later we can still make them,
     *  coz the flowers stay in bloom once they've bloomed, and with time we'd only get more flowers (if there are any not in bloom on day X);
     * => sounds like we can eliminate a lot of candidates to potential min days to bloom
     * => try binary search on solution spaces
     *
     * target=min number of days to wait till we can make [targetBouquets]
     * - what are boundaries?
     *  min: since bloomDays\[i]>0, we'd always have to wait at least 1 day, or even [bloomDays].min();
     *  max: [bloomDays].max(), cause after that day we won't have any new possibilities.
     * - elimination properties:
     *  - if day X is valid (we can make all bouquets) => on any day Y > X we can still make all bouquets
     *    => eliminate all days greater than X, but there still might exist a value less than X, which is valid too;
     *  - if day X is invalid (we can't make all bouquets=not enough adjacent flowers) => we can't make all bouquets on
     *   any day Z < X, coz then we'd only have same or less amount of flowers => there couldn't have been a  valid combination
     *   => eliminate all days less than X, but there still might be value greater than X which is valid (some flowers might still bloom).
     * - how to check if day X is valid?
     *  iterate through [bloomDays]:
     *   - if bloomDays\[i] <= X:
     *    - adjacentFlowerCount++
     *    - if (adjacentFlowerCount==bouquetCapacity):
     *     - bouquets++
     *     - adjacentFlowerCount=0
     *   - else:
     *    - adjacentFlowerCount=0
     *   - if (bouquets >= targetBouquets) return true
     *  - return false
     *  Time: O(bloomDays.size)
     *
     * edge case:
     *  - if bouquetsTarget * flowersInABouquet > bloomDay.size => return -1, cause there is not enough flowers to make
     *   all bouquets even if all flowers are in bloom;
     *  - bouquetsTarget * flowersInABouquet == bloomDay.size => we always wait for all flowers to bloom => return bloomDay.max()
     *   => could do an early return in O(n) time;
     *  - multiplication => check boundaries, targetBouquets * bouquetCapacity = 10^6 * 10^5 = 10^11 => use Long.
     *
     * Time: average/worst O(log(n)*n)
     *  - while has worst log(n) iterations;
     *  - each iterations costs O(n).
     * Space: always O(1)
     */
    fun efficient(
        bloomDays: IntArray,
        targetBouquets: Int,
        bouquetCapacity: Int,
    ): Int {
        val targetFlowers = targetBouquets * bouquetCapacity.toLong()
        if (targetFlowers > bloomDays.size) return -1
        if (targetFlowers == bloomDays.size.toLong()) return bloomDays.maxOf { it }

        var left = 1
        var right = bloomDays.maxOf { it }
        while (left < right) {
            val middle = left + (right - left) / 2
            if (middle.isValidWaitDays(bloomDays, targetBouquets, bouquetCapacity)) {
                right = middle
            } else {
                left = middle + 1
            }
        }
        return left
    }

    private fun Int.isValidWaitDays(
        bloomDays: IntArray,
        targetBouquets: Int,
        bouquetCapacity: Int,
    ): Boolean {
        var adjacentFlowers = 0
        var bouquets = 0
        bloomDays.forEach { day ->
            if (day <= this) {
                adjacentFlowers++
                if (adjacentFlowers == bouquetCapacity) {
                    bouquets++
                    adjacentFlowers = 0
                }
            } else {
                adjacentFlowers = 0
            }
            if (bouquets == targetBouquets) return true
        }
        return false
    }

    /**
     * the original reasoning, not even a mention of binary search)
     *
     * ------------
     *
     * problem rephrase:
     *  - given:
     *   - bouquetsTarget: Int
     *   - flowersInABouquet: Int
     *   - bloomDay: IntArray, where bloomDay\[i] is the number of days to pass before the ith flower can be used in a bouquet;
     *   - 1 flower can be used only in 1 bouquet;
     *  - goal: return the minimum amount of days to wait to make exactly bouquetsTarget bouquets, or -1 if it's impossible.
     *
     * approach:
     *  - flowers can be used indefinitely after their bloom day was on => we can make bouquets at the time of when all are ready,
     *   if we needed;
     *  - the answer always exists if we have enough flowers, since its always an option to just wait for all the flowers to bloom;
     *  given have enough flowers, how to determine what's the combination of flowers into bouquets such that we have to wait min days?
     *
     * since we need only adjacent flowers to make a bouquet
     *
     * basically for each flower we make a choice: in which bouquet exactly do we use it, or not use at all?
     * or even for each [bouquetCapacity] adjacent flowers we make that choice? whether to use or not to use them in a bouquet
     *
     * bouquet = subarray
     * the amount of time we have to wait to collect the jth bouquet = subarray.max()
     * the answer is, max(subarray.max() in subarrays), where subarrays.size == bouquetsTarget
     *
     * basically we want to find the best valid combination to split the [bloomDays] into [bouquetsTarget] subarrays
     *  valid = each subarray must contain [bouquetCapacity] flowers, a single element can be used exactly once in one subarray
     *  best = such a combination, that the max element among all subarrays is as minimum as possible
     *
     * best subarray combination based on just a max element => try sliding window?
     * or just two pointers?
     *
     * start from the minimum element?
     *
     * -----------------------
     *
     * when we include an ith flower into a bouquet => we can't use it for other bouquets => try DP?
     * is it subproblem optimal?
     * do suproblems overlap?
     *
     * suppose we pick the 1st flower into a bouquet => now we need to also add adjacent [bouquetCapacity] flowers.
     * then we might try to use the next first available flower to add it into a bouquet and use [bouquetCapacity]
     * available flowers there. Then we might skip the flower and try the next flower, if we have enough flowers
     * and return the min most max element in one of such valid combinations
     *
     * Time:
     *
     * - each time we make a bouquet, we have that many options to start it from = remainingFlowersLeft - targetRemainingFlowers positions
     *   where targetRemainingFlowers = (bouquetsTarget-bouquetsMade)*[bouquetCapacity] and remainingFlowersLeft=flowersAmount-rightMostBouquetInd
     *
     *
     * 2 10 5 1 4 3 14 tB=2 bC=2
     *
     * ans=5 (51 43)
     *
     * fastestBouquets=[10,10,5,4,4,14]
     *
     *
     * monotonic stack??
     *
     * 1 10 3 10 2, tB=3, bC=1
     *
     * non-increasing?
     *
     * bloomDays.forEach { day ->
     *   while(day >= stack.peek()) stack.pop()
     *   stack.add(day)
     * }
     *
     * sliding window + monotonic stack?
     *
     * sliding window of size [bouquetCapacity]
     *
     * left=0
     * right=0
     *
     * windowsAmount=bloomDays.size-bouquetCapacity
     * fastestBouquets=IntArray(size-windowsAmount)
     *
     * while (right < bouquetCapacity) stack.
     *
     * while (right < windowsAmount)
     *
     *
     *
     * private fun addToNonDecreasingStack(): Int {
     *
     * }
     */

    /**
     * goal - return the minimum possible max number across all subarrays of a valid combination of [bouquetsLeft]
     *  with [bouquetCapacity], using flowers from [bloomDays] starting from the [leftInd] flower only
     */
    private fun solve(
        bloomDays: IntArray,
        leftInd: Int,
        bouquetsLeft: Int,
        bouquetCapacity: Int,
    ) {
//        if (leftInd == bloomDays.size) return 0

        val flowersNeeded = bouquetsLeft * bouquetCapacity - 1

        var maxWait = bloomDays[leftInd]
//        for (i in leftInd until leftInd + bouquetCapacity) maxWait = max(bloomDays[])

        for (i in leftInd until bloomDays.size - flowersNeeded) {
            var currentBouquetMaxBloomDay = bloomDays[leftInd]
        }
    }
}

fun main() {
    println(
        MinimumNumberOfDaysToMakeMBouquets().efficient(
            bloomDays = intArrayOf(7, 7, 7, 7, 12, 7, 7),
            targetBouquets = 2,
            bouquetCapacity = 3,
        )
    )
}
