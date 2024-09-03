package com.keystarr.algorithm.deque.priorityqueue.twoheaps

import java.util.PriorityQueue

/**
 * LC-295: https://leetcode.com/problems/find-median-from-data-stream/description/
 * difficulty: hard (with follow up)
 * constraints:
 *  - -10^5 <= num <= 10^5
 *  - [findMedian] will be called only after at least one [addNum]
 *  - at most 5*10^4 calls to both methods combined
 *
 * naive idea:
 *  - [addNum]: we add each num to the dynamic array using binary search to maintain the sorted order O(nlogn)
 *      - determining the index: O(logn)
 *      - insertion, average/worst in the middle: O(n)
 *  - [findMedian]: O(1)
 *      - halfInd = elements.size/2
 *      - if elements.size % 2 == 1: elements\[halfInd]
 *      - else (elements\[halfInd] + elements\[halfInd])/2
 * Total complexities across all calls:
 *  time: O(n * nlogn) = O(n^2*logn), where n=number of calls to [addNum] = amount of added numbers
 *  space: O(n)
 *
 * or just uses a heap to maintain the sorted property
 *
 * Edge cases:
 *  - only 1 call to [addNum], 1 call to [findMedian] => expected is return the value itself =>
 *
 * ---------- Follow ups attempt
 *
 * follow up: if all integers are in [0, 100] => how to optimize the solution?
 *  what can we optimize? Space is n already, time for [addNum] however is O(logn)
 *
 * => how to reduce the time of [addNum] from O(logn) to O(1), if num is in [0,100]? (while keeping [findMedian] O(1))
 *
 * basically, we need to keep track of only 2 middle values. We may not even have to store all values.
 * can we do that?
 *
 * why [0,100]?
 * - "small"
 * - 0 and positive
 *
 * keep the max/min heaps of middle elements?
 *
 * 3 cases of changing middle elements (suppose we have >2 elements already):
 *  - insertion to the right of the sorted array => middle elements change to the ones to the right
 *  - to the left => middle elements change to the left
 *  - in the middle => middle elements might be whats inserted
 *
 * follow up: if 99% of integers are in [0, 100] => how to optimize the solution?
 *
 * TODO: why such follow ups? 2 heaps work fine outside of [0, 100]. Or are they meant to optimize even the heaps??
 *
 * ------------------ 2 heaps
 *
 * - minHeap: has the right half of the array; maxHeap: has the half left of the array
 *  => if we remove all elements from the maxHeap, then from the minHeap, we'll receive all elements of the array in the non-increasing order;
 * - all elements in [maxHeap] are always less or equal to all in [minHeap]
 * - [addNum] insert the new num into [minHeap] if its less than it's minimum by default. If heaps are out of balance =>
 *  balance them, remove either the min or the max and add to another heap. Balance - abs(minHeap.size - maxHeap.size) <= 1
 * - [findMedian] if one heap is greater in size than another, peek an element in there; otherwise peek both heaps
 *  and compute an average.
 *
 * Balance is crucial to guarantee than middle element / elements is always calculable in O(1) time via one or both heaps.
 *
 * Final notes:
 *  - here I am again with that type of problem. When I did [com.keystarr.algorithm.deque.stack.OnlineStockSpan], at first
 *   I couldn't believe how easy the solution seemed. But I was dead wrong in my assumptions, and learnt what I missed
 *   only after failing a submit and analyzing the case.
 *   Rn here I feel like its the easiest thing, but obviously I miss something crucial. Why? Its a really weird dead
 *   foolish certainty but I cant help but feel it again!
 *  - (after full solution) uh-huh, so the trick here was that, indeed, the naive solution was simple, like, leet-easy.
 *   The hard was to do in not in O(n^2*logn) but O(nlogn) time!
 *   I've incorrectly evaluated the time of the naive solution to be O(nlogn), forgetting that there was an insertion
 *   into the middle of an array, so that's why also it seemed so weird;
 *  - first encounter with the 2 heaps pattern. Very similar indeed in complexity of understanding to the monotonic stack
 *   pattern;
 *  - took ~2h to really understand this one! (and I still kinda dabble. Neetcode's solution btw was muuuuuuuuch more
 *  clear than the leet DSA course's!)
 */
class FindMedianFromDataStream {

    private val maxHeap = PriorityQueue<Int>(Comparator.reverseOrder()) // left half
    private val minHeap = PriorityQueue<Int>() // right half

    fun addNum(num: Int) {
        if (minHeap.isNotEmpty() && num < minHeap.peek()) maxHeap.add(num) else minHeap.add(num)
        val diff = maxHeap.size - minHeap.size
        if (diff == 2) minHeap.add(maxHeap.remove()) else if (diff == -2) maxHeap.add(minHeap.remove())
    }

    fun findMedian(): Double = when {
        maxHeap.size > minHeap.size -> maxHeap.peek().toDouble()
        maxHeap.size < minHeap.size -> minHeap.peek().toDouble()
        else -> (minHeap.peek() + maxHeap.peek()) / 2.0
    }
}

fun main() {
    FindMedianFromDataStream().apply {
        addNum(-1)
        println(findMedian()) // -1
        addNum(-2)
        println(findMedian()) // -1.5
        addNum(-3)
        println(findMedian()) // -2
        addNum(-4)
        println(findMedian()) // -2.5
        addNum(-5)
        println(findMedian()) // -3
    }
}
