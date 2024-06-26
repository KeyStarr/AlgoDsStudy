package com.keystarr.algorithm.deque.priorityqueue.topk

import java.util.*
import kotlin.math.abs

/**
 * LC-658 https://leetcode.com/problems/find•k•closest•elements/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= k <= arr.length
 *  • 1 <= arr.length <= 10^4
 *  • arr SORTED ASCENDING
 *  • •10^4 <= arr\[i], x <= 10^4
 *  • whats the constraint on x??????????????? no one to ask => assume same as arr\[i]
 *
 * Final notes:
 *  • missed a crucial condition
 *  • use comparator for all heap conditions? which may be plenty, succinct
 *  • comparator use minus is more succinct!!
 *
 * Value gained:
 *  • heap is not always the efficient solution for top k elements, here we have a sorted input array and can simply
 *   leverage that into a binary search / a sliding window for better time complexity! => so consider heap for top k,
 *   but by default still at least consider it;
 *  • I think that all heap prioritization logic should be put into a comparator for clarity. It seems that the main loop
 *   logic would probably (hypothesis from 2 problems so far) stay the same in most problems;
 *  • practiced using a Heap to solve top k problem WITH NON•SINGLE logic conditions, though here its not an efficient solution.
 */
class FindKClosestElements {

    /**
     * problem rephrase:
     *  Goal: find closest [k] integers to integer [x], return in ascending order.
     *   closest is defined via:
     *    - if numbers are different, then whichever has min abs diff with x is the one;
     *    - if numbers are equal, then whichever of them is smaller.
     *
     * find top k numbers, where top = min abs diff to x => try a heap?
     *
     * Idea:
     *  - pre-process: compute the "diffsToX" array via iterating through [numbers] and subtracting x from each number with abs; O(n)
     *  - iterate through diffsToX, add each into a maxHeap (binary heap), if maxHeap.size > k then maxHeap.remove() (O(n * logk)
     *   (remove max, since we want only k mins)
     *  - map indices back to numbers from maxHeap, return numbers sorted ascending // O(k * logk)
     *
     * Edge cases:
     *  - k == 1 => correct;
     *  - arr.length == 1 => correct;
     *  - k == 1, arr.length == 1 => return the [numbers], may do early return, but will work ok as a general case;
     *  - k == numbers.length => return numbers, may do early return, but will work ok as a general case;
     *  - maxDiff is 10^4 - (-10^4) = 10^8 => fits into Int, ok;
     *  - what if heap.remove() and b have the same distance to `x` and heap.size == `k`? No info in description, no one to ask.
     *   => failed a test case, for some reason in this case we must prefer numbers that appear EARLIER in the ORIGINAL array. Wut?
     *
     * Time: always O(n*logk + k*logk), worst k = numbers.size, so average/worst O(nlogn)
     * Space: always O(n)
     *  - O(n) for the diffs array
     *  - O(k) for the heap
     */
    fun suboptimal(numbers: IntArray, k: Int, x: Int): List<Int> {
        val diffsToX = IntArray(size = numbers.size)
        numbers.forEachIndexed { ind, num -> diffsToX[ind] = abs(num - x) }

        val maxHeap = PriorityQueue<Pair<Int, Int>> { o1, o2 ->
            if (o1.second == o2.second) o2.first - o1.first else o2.second - o1.second
        }
        diffsToX.forEachIndexed { ind, diff ->
            maxHeap.add(numbers[ind] to diff)
            if (maxHeap.size > k) maxHeap.remove()
        }

        return maxHeap.map { it.first }.sorted()
    }

    // TODO: solve via:
    //  1) binary search
    //  2) sliding window
}

fun main() {
    println(
        FindKClosestElements().suboptimal(
            numbers = intArrayOf(1,2,3,4,5),
            k = 4,
            x = 3,
        )
    )
}
