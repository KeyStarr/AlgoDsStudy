package com.keystarr.algorithm.deque.priorityqueue

import java.util.PriorityQueue

/**
 * LC-2336 https://leetcode.com/problems/smallest-number-in-infinite-set/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= num <= 1000;
 *  • at most 1000 calls to both methods.
 *
 * idea #1
 * 1. since we pop only the smallest integer => keep track of the upper lower non-removed boundary via the nonRemovedMin: Int variable
 * 2. make a minHeap for integers in the removed area, which are < nonRemovedMin.
 *  use minHeap to retrieve the min amongst them (cause they can be scattered)
 *  use minSet to check for O(1) time if the item is contained (instead of a factor of k to each [addBack] call)
 * 3. on [addBack]:
 *  - if num == nonRemovedMin - 1 => nonRemovedMin--
 *  - else minHeap.add(num)
 * 4. on [popSmallest]:
 *  - return if minHeap.isNotEmpty()  minHeap.remove()
 *  - else nonRemovedMin++
 *
 * edge cases:
 *  • [addBack] num is already in the set => don't add it!
 *   but how to check if its present?
 *
 * Time: average/worst O(logk(g+k)), where t=total number of calls to both methods, k=number of calls to addBack.
 *  • add/remove from heap is average/worst O(logk)
 *  • up to 1000 calls, so say worst is we do 250 removals and add 250 numbers, then do 250 removals and add 250 back,
 *   adding always starting from the smallest
 *   => first 250 removals each is O(1), first 250 adds each is O(log250), next 250 removals each is O(log250), next
 *   250 adds is O(log250).
 *    g = number of calls to pop smallest
 *    O(log(k)*g + log(k)*k) = average/worst O(logk(g+k))
 * Space: O(k)
 *  • worst if we do g=t/2 removals and then add k=t/2 numbers from the smallest, we'd have k/2 elements in the [minHeap],
 *   and the same amount in the [minSet].
 *
 * Could time be improved?
 *
 * Final notes:
 *  • 🎉 done [popSmallest] and [addBack] by myself in 25 mins, 1st try submit, first problem of the day;
 *  • but I feel that probably there is more concise solution with at least a better const;
 *  • why don't I feel like improving the solution??? I got a successful submit and have a lot of other work to do, so I guess
 *   that's why. But is that solution enough for interviews? I strongly guess there's a better one;
 *  • yep, just as I've guessed - checked solutions - there's a TreeSet approach, but I don't have a profound grasp on
 *   that tool yet => leave be for now, but good intuition. Basically its a DS which, in this case,
 *   provides O(logn) contains, add and remove, but all in the same package and for O(n) memory cost.
 *   Actually, my contains check is O(1) so the time const is faster))))) buuut at the memory / code complexity cost, and
 *   asymptotically doesn't matter since where there is contains, there is average/worst remove which is anyway O(logn).
 *
 * Value gained:
 *  • practiced efficiently solving a class contract implementation problem using a HashSet and a minHeap;
 *   It's really great how I've reasoned on the spot, once again at first I felt the wall of understanding, then tried
 *   to step-by-step reason what do we exactly need to do, and how to solve these subproblems efficiently adapting to this
 *   particular context.
 */
class SmallestNumberInInfiniteSet {

    private val minHeap = PriorityQueue<Int>()
    private val minSet = mutableSetOf<Int>()
    private var nonRemovedMin = 1

    // TODO: re-implement after mastering the ordered set concept

    /**
     * Must remove from the set and return the smallest integer (from not yet removed ones). Up to Int.MAX?
     * Oh, we have at most 1000 calls to both methods so, way below int max.
     */
    fun popSmallest(): Int = if (minHeap.isNotEmpty()) {
        val min = minHeap.remove()
        minSet.remove(min)
        min
    } else nonRemovedMin++

    /**
     * Add the integer back but only if it was removed.
     */
    fun addBack(num: Int) {
        if (num >= nonRemovedMin || minSet.contains(num)) return

        if (num == nonRemovedMin - 1) {
            nonRemovedMin--
        } else {
            minHeap.add(num)
            minSet.add(num)
        }
    }
}
