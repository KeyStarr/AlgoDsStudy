package com.keystarr.algorithm.deque.priorityqueue

import java.util.PriorityQueue

/**
 * LC-1167 https://leetcode.com/problems/minimum-cost-to-connect-sticks/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= sticks.length <= 10^4
 *  - 1 <= sticks\[i] <= 10^4
 *
 * Final notes:
 *  - a very straight-forward Heap question with no real edge cases;
 *  - I stumbled at first to understand exactly why we choose the 2 min numbers each move, and I felt weird that the
 *   total cost was growing so fast in proportion to the input. Which is weird, that I felt weird. I rolled on intuition
 *   and was right, and only 20 mins it came to me (returned to the question and the answer was already in my brain, like
 *   it was always there!) why;
 *  - done [efficient] by myself in 20 mins.
 *
 * Value gained:
 *  - major: practiced recognizing and solving a min Heap problem;
 *  - minor: practiced rolling with decisions fast based on intuition :D (read above)
 */
class MinimumCostToConnectSticks {

    /**
     * Problem rephrase:
     *  - move: combine two elements array\[i]+array\[j] into a single new element, insert back into an array;
     *   cost of the move: i+j
     *  - termination: do moves until there's exactly 1 element in the array left
     * Goal: return the minimum cost of reaching the termination
     *
     * What's the best move?
     * where min(array) = array\[i], and the next minimum after it is array\[j]
     *
     * Intuition:
     *  - since we add the sum of 2 numbers back into the heap, essentially, the numbers that we've removed will then
     *   be added again to the cost once that number will be used for combination again! Therefore, we want for the least
     *   numbers to be used the most amount of times => this way across accumulation we add the least to the total cost.
     *
     * repeatedly find the 2 next minimums in the dynamically updated collection (remove+insert)
     *  => Heap (min)
     *
     * Idea:
     *  - minHeap = PriorityQueue()
     *  - add all [sticks] into minHeap // O(n*logn)
     *  - totalCost = 0
     *  - while(minHeap.size != 1): // O(n-1)
     *   - firstMin = minHeap.remove() // logn
     *   - secondMin = minHeap.remove() // logn
     *   - newStick = firstMin + secondMin
     *   - totalCost += newStick
     *   - minHeap.add(newStick) // logn
     *  - return totalCost
     *
     *
     * Edge cases:
     *  - sum => max sum is 10^4 * 10^4 = 10^8, fits into Int, ok;
     *  - sticks.length == 1 => return 0, correct.
     *  - all sticks\[i] == 1 => no matter, correct.
     *
     * Time: O(nlogn)
     * Space: O(n) for the heap
     */
    fun efficient(sticks: IntArray): Int {
        val minHeap = PriorityQueue<Int>()
        sticks.forEach(minHeap::add)
        var totalCost = 0
        while (minHeap.size != 1) {
            val firstMin = minHeap.remove()
            val secondMin = minHeap.remove()
            val combinedStick = firstMin + secondMin
            totalCost += combinedStick
            minHeap.add(combinedStick)
        }
        return totalCost
    }
}
