package com.keystarr.algorithm.deque.priorityqueue.topk

import java.util.*
import kotlin.math.abs
import kotlin.math.max

/**
 *
 * constraints:
 *  - 1 <= k <= arr.length
 *  - 1 <= arr.length <= 10^4
 *  - arr SORTED ASCENDING
 *  - -10^4 <= arr\[i], x <= 10^4
 *  - whats the constraint on x??????????????? no one to ask => assume same as arr\[i]
 *
 * Final notes:
 *  -
 *
 * Value gained:
 *  -
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
    fun solution(numbers: IntArray, k: Int, x: Int): List<Int> {
        val diffsToX = IntArray(size = numbers.size)
        numbers.forEachIndexed { ind, num -> diffsToX[ind] = abs(num - x) }

        val maxHeap = PriorityQueue<Pair<Int, Int>> { o1, o2 -> o2.second - o1.second }
        diffsToX.forEachIndexed { ind, diff ->
            if (maxHeap.size < k) {
                maxHeap.add(ind to diff)
            } else {
                if (maxHeap.peek()?.second != diff) maxHeap.add(ind to diff)
                if (maxHeap.size > k) maxHeap.remove()
            }
        }

        return maxHeap.map { numbers[it.first] }.sorted()
    }
}

fun main() {
    println(
        FindKClosestElements().solution(
            numbers = intArrayOf(0, 1, 1, 1, 2, 3, 6, 7, 8, 9),
            k = 9,
            x = 4,
        )
    )
}
