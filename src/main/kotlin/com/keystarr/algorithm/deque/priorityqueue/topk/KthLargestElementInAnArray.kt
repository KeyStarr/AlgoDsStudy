package com.keystarr.algorithm.deque.priorityqueue.topk

import java.util.PriorityQueue

/**
 * LC-215 https://leetcode.com/problems/kth-largest-element-in-an-array/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= k <= nums.length <= 10^5
 *  • -10^4 <= nums\[i] <= 10^4
 *
 * Final notes:
 *  • a very straightforward problem, the distilled top k heap pattern.
 *
 * Value gained:
 *  • practiced recognizing/solving a problem using a heap top k pattern.
 */
class KthLargestElementInAnArray {

    /**
     * Goal: return kth largest element in the array (i.o. if it was sorted descending, then array\[k])
     * Solution constraint: without sorting
     *
     * kth max always exists since k is within the nums.size
     *
     * top k => try a min heap
     *
     * Idea:
     *  - minHeap = PriorityQueue<Int>() // binary min heap
     *  - go through each [nums] and add it into the heap, if heap.size > k, heap.remove()
     *  - return minHeap.remove() // the min element in the collection that is k maxes => kth max in the original array
     *
     * Edge cases:
     *  - k == 1 && nums.size == 1 => nums[0] is the answer, correct as-is;
     *  - k == 1 => basically just the max of the nums, correct;
     *  - nums.size == 1 then k can only be 1, checked already;
     *  - nums\[i] == heap.peek() => its ok, the logic is correct as-is;
     *  - k == nums.length => the minimum element in [nums], correct.
     *
     * Time: always O(n * logk), worst k=n so worst O(n*logn)
     * Space: O(n)
     */
    fun suboptimal(nums: IntArray, k: Int): Int {
        val minHeap = PriorityQueue<Int>()
        nums.forEach {
            minHeap.add(it)
            if (minHeap.size > k) minHeap.remove()
        }
        return minHeap.remove()
    }

    // TODO: implement the efficient solution here. Using quick select?
}
