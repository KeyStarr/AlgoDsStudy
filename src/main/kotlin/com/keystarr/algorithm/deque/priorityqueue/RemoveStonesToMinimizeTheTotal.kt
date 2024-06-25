package com.keystarr.algorithm.deque.priorityqueue

import java.util.PriorityQueue

/**
 * LC-1962 https://leetcode.com/problems/remove-stones-to-minimize-the-total/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= piles.length <= 10^5
 *  - 1 <= piles\[i] <= 10^4
 *  - 1 <= k <= 10^5
 *
 * Final notes:
 *  - the main trick here was, well, to just know when and how to use a Heap;
 *  - other than that, another tricky part was to put back into the heap not the remainder of the division but actually
 *   the (maxPile - maxPile/2), the amount which was left after we took that half. Which is technically either a
 *   maxPile/2 or maxPile/2+1, so another way to express it would be halvedMaxPile + maxPile%2. Or maybe its not even
 *   a trick, and just I fell into that (before submitting), cause the previous problem was exactly like that and we
 *   put back the remainder (personal momentary context issue?);
 *  - also its funny that we could just return without doing k operations if maxPile == 1 => did that trick get me
 *   beats 100% in kotlin? Nah, probably just this problem+kotlin is too niche;
 *  - done in ~20-25 mins (didn't record the exact time).
 *
 * Value gained:
 *  - practiced recognizing and solving a problem efficiently with MaxHeap.
 */
class RemoveStonesToMinimizeTheTotal {

    /**
     * Observations:
     *  - we need to get minimum possible stones after [exactOperationsCount] reductions => do each reduction on the biggest pile, cause
     *   we'll reduce the most then (half of it would be greater than or equal to a half of any other pile);
     *  - we need k maximum numbers from [piles], and we need to insert the halved piles back into [piles], we need
     *   to consider them too, cause even after reduction the remainder may be the max of all piles.
     *
     * Repeatedly find max with dynamic updates to the collection => Heap.
     *
     * Idea:
     *  - maxHeap = PriorityQueue(Comparator.reversedOrder())
     *  - totalSum = 0
     *  - add all piles into maxHeap AND sum values along the way // O(n*logn)
     *  - repeat([exactOperationsCount]):
     *      - maxPile = maxHeap.remove()
     *      - if (maxPile == 1) return totalSum
     *      - halvedMaxPile = maxPile / 2
     *      - totalSum -=
     *      - maxHeap.add(maxPile - halvedMaxPile)
     *  - return totalSum
     *
     * Edge cases:
     *  - max total number of stones 10^4 * 10^5 = 10^9, fits into Int, ok;
     *  - division => rounding? YES, make sure to back into the pile (maxPile-maxPile/2). If maxPile is odd, then
     *   maxPile - floor(maxPile/2) = 1 => and we need that 1 stone in the total sum!;
     *  - piles.length == 1 and k == 1 and piles\[i] == 1 => floor(1/2) == 0 => return 1, correct;
     *  - all piles\[i] == 1 => return the original sum, correct.
     *
     * Time: always (n*logn + k*logn)
     * Space: always O(n)
     *
     * ---
     * Possible optimization: bound the heap at [exactOperationsCount], then Time becomes (nlogn + klogk)
     */
    fun efficient(piles: IntArray, exactOperationsCount: Int): Int {
        val maxHeap = PriorityQueue<Int>(Comparator.reverseOrder())
        var totalSum = 0
        piles.forEach { pile ->
            maxHeap.add(pile)
            totalSum += pile
        }
        repeat(exactOperationsCount) {
            val maxPile = maxHeap.remove()
            if (maxPile == 1) return totalSum
            val halvedMaxPile = maxPile / 2
            totalSum -= halvedMaxPile
            maxHeap.add(maxPile - halvedMaxPile)
        }
        return totalSum
    }
}
