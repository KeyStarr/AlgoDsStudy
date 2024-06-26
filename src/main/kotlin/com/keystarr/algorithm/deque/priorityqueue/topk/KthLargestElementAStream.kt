package com.keystarr.algorithm.deque.priorityqueue.topk

import java.util.PriorityQueue
import kotlin.math.min

/**
 * LC-703 https://leetcode.com/problems/kth-largest-element-in-a-stream/description/
 * difficulty: easy (though really should be leet-medium because heap)
 * constraints:
 *  - 1 <= k <= 10^4
 *  - 0 <= nums.length <= 10^4
 *  - -10^4 <= nums\[i] <= 10^4
 *  - -10^4 <= value <= 10^4
 *  - at most 10^4 calls to add
 *  - always at least k elements in the array when [add] is called
 *
 * top kth element = here kth max element => try a heap (min)
 *  (min cause we'll remove the minimum elements when heap.size exceeds k+1 and keep k maximums that way)
 *
 * Idea #1:
 *  - use a minHeap, the usual top k pattern to maintain with every new element added having k maxes of all input so far;
 *  - how to get the kth maximum? minHeap.peek() will always be the kth maximum, cause minHeap has k maximums of input so far,
 *   => the minimum of those (minHeap.peek()) will be the kth maximum
 * total time: O(n*logk*k)
 * space: O(k)
 *
 * can we remove the k factor, reduce searching for max from O(k) to O(1)
 *
 * idea #2 = two heaps:
 *  - use a maxHeap alongside the minHeap, maintain it in the same way via top k heap pattern;
 *  - at the end of every
 *
 * Edge cases:
 *  - the initial \[nums].size > k => we need to use [add] to add even the initial numbers passed via the constructor!
 *
 * Time: n*logk, where n=total number of elements: initial ones plus all inserted ones
 * Space: O(k) cause we have at most k+1 elements in the heap
 *
 * Final notes:
 *  - I got lost a bit and forgot at first that the problem asked for kth max and not the k maxes, then realized it,
 *   then got confused again thinking that kth max is not minHeap.peek() if we maintain the heap via the heap top k pattern D:
 *   then finally realized that its just as straightforward as it gets;
 *  - failed 1st run though still, cause forgot that if nums.size > k we actually need to start poppin' even on init. Oh.
 *
 * Value gained:
 *  - practiced a simple top k min heap for efficient solution for a data stream top kth problem.
 */
class KthLargestElementAStream(
    private val k: Int,
    nums: IntArray,
) {

    private val minHeap = PriorityQueue<Int>()

    init {
        nums.forEach { add(it) }
    }

    fun add(value: Int): Int {
        minHeap.add(value)
        if (minHeap.size > k) minHeap.remove()
        return minHeap.peek()
    }
}
