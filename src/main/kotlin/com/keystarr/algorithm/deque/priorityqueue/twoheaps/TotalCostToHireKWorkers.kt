package com.keystarr.algorithm.deque.priorityqueue.twoheaps

import java.util.PriorityQueue
import kotlin.math.max

/**
 * ⭐️ I've done a beautiful, almost exemplary problem-solving reasoning from [efficient] to [efficientCleanest], for a
 *  problem type I, apparently, haven't had enough experience to jump straight to the [efficientCleanest] (but it's certainly possible)
 *
 * LC-2462 https://leetcode.com/problems/total-cost-to-hire-k-workers/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= candidates.size <= 10^5;
 *  - 1 <= candidates\[i] <= 10^5;
 *  - 1 <= targetHires, windowSize <= candidates.size.
 *
 * Final notes:
 *  - ⚠️⚠️ done [efficient] by myself, 3rd attempt submit in 50 mins. Done [efficientCleanest] in another 20 mins, at 1h10 min mark;
 *  - too slow for a real interview, but still would've passed for a good grade I think with not the cleanest, but asymptotically
 *   efficient medium type problem solution under 1h + discovered all the core decisions and most of the optimizations by myself 🏅
 *
 * Value gained:
 *  - practiced solving a simulation type problem efficiently using 2 heaps, 2 pointers and a greedy rule (to choose the first
 *   min elements across both windows);
 *  - practiced designing an efficient algorithm from the "bloated, verbose" first draft [efficient] iteratively to the cleanest
 *   efficient solution [efficientCleanest]. Worked amazingly well! Much faster and better than sitting trying to come up with
 *   the best asymptotic and best const cleanest solution straight away for a complex quite new to me problem!
 */
class TotalCostToHireKWorkers {

    // TODO: repeat in 1-2 weeks

    /**
     * Seems to be an array based greedy simulation type problem.
     *
     * Actually, we need to repeatedly get the minimum element in a live-modified collection => try using a heap.
     * Since we consider 2 windows each of [candidates] size from both start and end of the array, but in case of ties
     *  prefer the smallest index => try 2 heaps.
     *
     * Approach;
     *  - core principle: initially we want all elements to be added across both heaps, but each element exactly once to a single heap;
     *   if candidates < costs.size / 2 that's no a problem
     *
     * ex #1
     * 1 2 4 1 5 6
     *
     * 2 4 1 5 6 candidates=2 targetHires=6
     * result = 1
     * 2 4 5 6
     *
     *
     * observe:
     *  1. if candidates >= costs.size / 2 (remaining costs) then we simply select the first occurrence of the min element;
     *  2. otherwise we must consider only the first [candidates] and last [candidates] elements and select the first min across those.
     *
     * =>
     *
     * - if candidates >= costs.size / 2 => sort elements and return prefix sum for [targetHires];
     * - else:
     *  - add first [candidates] elements into firstHeap and [candidates] elements into lastHeap;
     *  - make a step:
     *   - choose the smallest element of both heaps, preferring the first in case of equal;
     *   - remove it from the heap and add it to the sum;
     *   - if the next element for that heap isn't contained in the other heap (check by index):
     *    - add it to the heap and continue
     *   - else:
     *    - repeat remainingHires times: choose the min element across both heaps prioritizing the first one;
     *    - return the resulting sum.
     *
     * Edge cases:
     *  - sum => max sum is 10^5*10^5=10^10 => use Long for the sum;
     *  - windowSize >= costs.size / 2 => either sort and return the prefix sum at [targetHires] OR add into the second heap
     *   only elements from candidates[windowsSize:candidates.size-1] and skip the first loop;
     *   - special case: candidates == costs.size => nothing changes, same reasoning as for this general edge case;
     *  - candidates.size == 1 => both targetHires and windowsSize always == 1 => always return candidates[0] =>
     *  - targetHires.size == 1 and candidates.size > 1 =>
     *  - windowSize == 1 =>
     *  - ⚠️ MISSED: we are out of hires and still in the first loop =>
     *  - ⚠️ MISSED:
     *
     *
     *
     * ex #2
     * c = 3 2 1 4   tH = 4   wS = 1
     *
     * 1 >= 4/2 => false
     *
     * hL=1
     * fH = ()  fRI = 3
     * lH = (4)  lLI = 3
     *
     * sum = 3 + 2 + 1 + 4
     *
     * ---
     *
     * ex #3
     * c = 31 25 72 79 74 65 84 91 18 59 27 9 81 33 17 58    tH = 11   wS = 2
     *
     * 2 >= 16/2 => false
     *
     * hL=0
     * fH = (65 79)  fRI = 5
     * lH = (81 91)  lLI = 7
     *
     * sum = 17 + 25 + 31 + 33 + 58 + 9 + 27 + 59 + 18 + 72 + 74
     *
     * ---
     *
     * ex #4
     * c = 2 2    tH = 1   wS = 1
     *
     * 1 >= 3/2(1) => return 2
     *
     * Time: average/worst O(nlogn)
     *  - if windowSize >= candidates.size / 2 + candidates.size % 2 => always O(nlogn), where n=candidates.size;
     *  - else: average/worst O(n * logk)
     *   - initially we add k=windowSize elements into each heap;
     *   - across the next two loops we process each element at most once, that is, add and remove it into either heap,
     *    worst is targetHires==candidates.size => we add and remove each element exactly once into/from the heap.
     * Space: average/worst O(n)
     *  - first branch: always O(logn) for sorting, if in-place sort allowed;
     *  - second branch: always O(max(windowSize*2,targetHires)) basically = O(n)
     */
    fun efficient(candidates: IntArray, targetHires: Int, windowSize: Int): Long {
        var totalCost = 0L
        if (windowSize >= candidates.size / 2 + candidates.size % 2) {
            candidates.sort()
            for (i in 0 until targetHires) totalCost += candidates[i]
            return totalCost
        }

        val firstMinHeap = PriorityQueue<Int>()
        var firstRightInd = windowSize - 1
        for (i in 0..firstRightInd) firstMinHeap.add(candidates[i])

        val lastMinHeap = PriorityQueue<Int>()
        var lastLeftInd = candidates.size - windowSize
        for (i in candidates.lastIndex downTo lastLeftInd) lastMinHeap.add(candidates[i])

        var hiresLeft = targetHires
        while (firstRightInd < lastLeftInd && hiresLeft > 0) {
            if (lastMinHeap.isEmpty() || (firstMinHeap.isNotEmpty() && firstMinHeap.peek() <= lastMinHeap.peek())) {
                totalCost += firstMinHeap.remove()
                firstRightInd++
                if (firstRightInd < lastLeftInd) firstMinHeap.add(candidates[firstRightInd])
            } else {
                totalCost += lastMinHeap.remove()
                lastLeftInd--
                if (firstRightInd < lastLeftInd) lastMinHeap.add(candidates[lastLeftInd])
            }
            hiresLeft--
        }

        repeat(hiresLeft) {
            totalCost += if (lastMinHeap.isEmpty() || (firstMinHeap.isNotEmpty() && firstMinHeap.peek() <= lastMinHeap.peek())) {
                firstMinHeap.remove()
            } else {
                lastMinHeap.remove()
            }
        }

        return totalCost
    }

    /**
     * ex #5
     * c = 1 3 2 4    tH = 4   wS = 3
     *
     * initial lastLeftInd=max(4-3,3)=3
     *
     * hL=4
     * fH = (1 3 2)  fRI = 2
     * lH = (4)  lLI = 3
     *
     *
     * ---
     *
     * ex #6
     * c = 1 3 2 4    tH = 4   wS = 4
     *
     * initial lastLeftInd=max(4-3,4)=4
     *
     * hL=4
     * fH = (1 3 2 4)  fRI = 3
     * lH = ()  lLI = 4
     *
     * since last min heap is initially empty, we'll never add/remove from this => never access candidates\[lLI]
     *
     * -----------------------------
     *
     * made 3 optimizations to [efficient]:
     *  1. remove the last loop, just keep going until we either have hires left. If we've added all elements but have hires left
     *   => don't add any new ones, but keep going. if we've not added all elements BUT reached the hires target => early return
     *   => use a single processing loop with these modifications;
     *  2. remove the first branch => in case we can't have equal [windowSize] elements in the heap initially, simply
     *   put [windowSize] elements into the first heap, and only the remaining amount into the second one;
     *  3. reduce the main loop simply to run exactly [targetHires] times, since that's the ONLY actual termination condition.
     *   Reason - if firstRightInd >= lastLeftInd we still have hires to do, the only change is that we don't add elements to heaps anymore.
     *   + stop updating indices when we have no more elements to add, don't wastefully update them when no longer needed
     *    (i.o. update either index only if there's at least one element left not added to the heaps)
     *
     * discovered 1-2 myself, felt that conditions were a mess, but couldn't pinpoint why. Done 3rd thanks to one of the comments in
     *  https://leetcode.com/problems/total-cost-to-hire-k-workers/editorial/
     *
     * Time: average O(targetHires * log(windowSize)), worst O(nlogn)
     * Space: always O(windowSize)
     *
     * either heap grows only up to windowSize elements
     */
    fun efficientCleanest(candidates: IntArray, targetHires: Int, windowSize: Int): Long {
        val firstMinHeap = PriorityQueue<Int>()
        var firstRightInd = windowSize - 1
        for (i in 0..firstRightInd) firstMinHeap.add(candidates[i])

        val lastMinHeap = PriorityQueue<Int>()
        var lastLeftInd = max(candidates.size - windowSize, windowSize)
        for (i in candidates.lastIndex downTo lastLeftInd) lastMinHeap.add(candidates[i])

        var totalCost = 0L
        repeat(targetHires) {
            if (lastMinHeap.isEmpty() || (firstMinHeap.isNotEmpty() && firstMinHeap.peek() <= lastMinHeap.peek())) {
                totalCost += firstMinHeap.remove()
                if (firstRightInd < lastLeftInd - 1) {
                    firstRightInd++
                    firstMinHeap.add(candidates[firstRightInd])
                }
            } else {
                totalCost += lastMinHeap.remove()
                if (firstRightInd < lastLeftInd - 1) {
                    lastLeftInd--
                    lastMinHeap.add(candidates[lastLeftInd])
                }
            }
        }
        return totalCost
    }
}

fun main() {
    println(
        TotalCostToHireKWorkers().efficient(
            candidates = intArrayOf(2, 1, 2),
            targetHires = 1,
            windowSize = 1,
        )
    )
}
