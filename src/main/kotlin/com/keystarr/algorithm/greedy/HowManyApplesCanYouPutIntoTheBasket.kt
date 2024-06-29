package com.keystarr.algorithm.greedy

import java.util.PriorityQueue

/**
 * LC-1196 https://leetcode.com/problems/how-many-apples-can-you-put-into-the-basket/
 * difficulty: easy
 * constraints:
 *  - 1 <= weight.length <= 10^3
 *  - 1 <= weight\[i] <= 10^3
 *
 * Final notes:
 *  - a very straightforward greedy solution, got it in my mind the second I've read the description;
 *  - done [suboptimalSorting] by myself in 12 mins (⚠️ still pretty slow though! why?)
 *  - good that I didn't go into code when designing the algorithm, it was a bit complex at first to rule out the details
 *   how it will work without going into them, but in the end is much more usable and probably slightly faster (?) than before.
 *
 * Value gained:
 *  - practiced recognizing and solving a problem via the greedy paradigm.
 */
class HowManyApplesCanYouPutIntoTheBasket {

    /**
     * Greedily choose the apple with the min weight out of the remaining weight => maximize the total amount of apples
     * under the constraints.
     *
     * Algorithm:
     *  - sort [weights] ascending;
     *  - iterate through [weights], accumulate the currentWeight, if adding the new apple does not exceed it, add it,
     *   otherwise return usedApples counter;
     *  - return usedApplesCounter // we have used all apples without hitting the weight constraint
     *
     * Time: always O(nlogn) due to sorting
     * Space: O(1) if in-place sorting, otherwise with a new sorted array O(n)
     */
    fun suboptimalSorting(weights: IntArray): Int {
        weights.sort()
        var applesUsed = 0
        var weightLeft = MAX_WEIGHT
        weights.forEach { appleWeight ->
            if (appleWeight > weightLeft) return applesUsed
            applesUsed++
            weightLeft -= appleWeight
        }
        return applesUsed
    }

    /**
     * Time: O(nlogn)
     *  - int to heap conversion:
     *   - array to list conversion O(n)
     *   - heapify the list O(n)
     *  - counting loop average/worst O(k*logn), worst k=n, so average/worst O(nlogn)
     *   - every remove from the heap O(logn)
     *   - k = the number of iterations, the number of apples we can carry, worst k=n
     * Space: O(n) for the heap
     */
    fun suboptimalMinHeap(weights: IntArray): Int {
        val minHeap = PriorityQueue(weights.toList())
        var applesUsed = 0
        var weightLeft = MAX_WEIGHT
        while (minHeap.isNotEmpty()) {
            val appleWeight = minHeap.remove()
            weightLeft -= appleWeight
            if (weightLeft < 0) break
            applesUsed++
        }
        return applesUsed
    }

    // TODO: solve via counting sort for O(n+w) time
}

private const val MAX_WEIGHT = 5000

fun main() {
    println(HowManyApplesCanYouPutIntoTheBasket().suboptimalMinHeap(intArrayOf(100, 200, 150, 1000)))
}
