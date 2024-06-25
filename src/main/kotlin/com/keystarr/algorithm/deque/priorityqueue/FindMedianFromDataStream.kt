package com.keystarr.algorithm.deque.priorityqueue

/**
 * LC-295: https://leetcode.com/problems/find-median-from-data-stream/description/
 * difficulty: hard (with follow up)
 * constraints:
 *  - -10^5 <= num <= 10^5
 *  - [findMedian] will be called only after at least one [addNum]
 *  - at most 5*10^4 calls to both methods combined
 *
 * naive idea:
 *  - [addNum]: we add each num to the dynamic array using binary search to maintain the sorted order
 *      - determining the index: O(logn)
 *      - insertion: amortized O(1)
 *  - [findMedian]: O(1)
 *      - halfInd = elements.size/2
 *      - if elements.size % 2 == 1: elements\[halfInd]
 *      - else (elements\[halfInd] + elements\[halfInd])/2
 * Total complexities across all calls:
 *  time: O(nlogn), where n=number of calls to [addNum] = amount of added numbers
 *  space: O(n)
 *
 * or just uses a heap to maintain the sorted property
 *
 * Edge cases:
 *  - only 1 call to [addNum], 1 call to [findMedian] => expected is return the value itself =>
 *
 * ----------
 *
 * follow up:
 *  - if all integers are in [0, 100] => how to optimize the solution?
 *  - if 99% of integers are in [0, 100] => how to optimize the solution?
 *
 *
 *
 *
 *
 * ------------------
 *
 *
 * Final notes:
 *  - here I am again with that type of problem. When I did [com.keystarr.algorithm.deque.stack.OnlineStockSpan], at first
 *   I couldn't believe how easy the solution seemed. But I was dead wrong in my assumptions, and learnt what I missed
 *   only after failing a submit and analyzing the case.
 *   Rn here I feel like its the easiest thing, but obviously I miss something crucial. Why? Its a really weird dead
 *   foolish certainty but I cant help but feel it again!
 */
class FindMedianFromDataStream {

    private val elements = mutableListOf<Int>()

    fun addNum(num: Int) {
        elements.insertSortedAscending(num)
    }

    private fun MutableList<Int>.insertSortedAscending(num: Int) {
        val ind = binarySearch(num)
        val insertionInd = if (ind < 0) (ind + 1) * -1 else ind
        add(insertionInd, num)
    }

    fun findMedian(): Double {
        val halfInd = elements.size / 2
        return if (elements.size % 2 == 1) elements[halfInd].toDouble() else (elements[halfInd - 1] + elements[halfInd]) / 2.0
    }
}

fun main() {
    FindMedianFromDataStream().apply {
        addNum(3)
        println(findMedian())
        addNum(1)
        println(findMedian())
        addNum(2)
        println(findMedian())
    }
}
