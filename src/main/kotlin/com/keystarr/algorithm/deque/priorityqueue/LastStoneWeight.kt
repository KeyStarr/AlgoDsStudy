package com.keystarr.algorithm.deque.priorityqueue

import java.util.PriorityQueue

/**
 * LC-1046 https://leetcode.com/problems/last-stone-weight/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= stones.length <= 30
 *  • 1 <= stones\[i] <= 1000
 *
 * Final notes:
 *  • tried to solve the problem without using a priority queue => [abomination]. Very funny how determining the
 *   insertion index using a pre•built kotlin's binary search turned into a little nightmare;
 *  • technically [priorityQueue] does the same as the [abomination], only the insertion to maintain the sorted order
 *   is performed under the hood in the heap implementation of the priority queue, ofc differently but the goal is similar.
 *
 * Value gained:
 *  • practiced a priority queue via java's binary heap implementation. Here clearly understanding and using this DS 
 *   is so much cleaner and faster to implement rather than reinventing a similar mechanism on the spot :))) 
 */
class LastStoneWeight {

    /**
     * problem rephrase:
     *  - given an unsorted array of integers
     *  - 1 move: find max and next max numbers, if both are equal remove both, otherwise remove the least one and
     *   add into an array a new number: the other one minus the removed one
     * Goal: perform moves until there are no or 1 number left, return it or 0.
     *
     * taking 2 next maxes each time, and inserting a new number sometimes =>
     * - sort the array O(nlogn), convert into a dynamic array (ArrayList)
     * - remove maxes via incrementing a pointer to the remaining elements
     * - insert the new number in a sorted way O(logn), the amount of such insertions depends on n, so O(nlogn)
     *
     * edge cases:
     *  - stones == 1 => return stones[0]
     *
     * // TODO: why? how to prove that worst amount of iterations here is a const * n?
     * total time: O(nlogn)
     * space: O(n)
     */
    fun abomination(stones: IntArray): Int {
        if (stones.size == 1) return stones[0]

        val sortedList = stones.sortedDescending().toMutableList()
        var currentInd = 0
        while (currentInd < sortedList.size - 1) {
            val max = sortedList[currentInd]
            val nextMax = sortedList[++currentInd]
            currentInd++
            val diff = max - nextMax
            if (diff != 0) sortedList.add(sortedList.getInsertionIndexSorted(startFrom = currentInd, num = diff), diff)
        }
        return if (currentInd == sortedList.size - 1) sortedList[currentInd] else 0
    }

    private fun List<Int>.getInsertionIndexSorted(startFrom: Int, num: Int): Int {
        val ind = binarySearch(num, fromIndex = startFrom, comparator = { first, second ->
            val n1 = first * -1
            val n2 = second * -1
            when {
                n1 == n2 -> 0
                n1 > n2 -> 1
                else -> -1
            }
        }
        )
        return if (ind < 0) (ind + 1) * -1 else ind
    }

    /**
     * use a priority queue via a heap:
     *  - add all numbers into a binary heap O(nlogn)
     *  - remove 2 maxes each time via O(logn), total O(nlogn)
     *  - add the remainder via O(logn), total O(nlogn)
     *
     * // TODO: same question as in [abomination] here
     * total time: O(nlogn)
     * space: O(n)
     */
    fun priorityQueue(stones: IntArray): Int {
        if (stones.size == 1) return stones[0]

        val heap = PriorityQueue<Int>(Comparator.reverseOrder())
        stones.forEach(heap::add) // O(nlogn)

        while (heap.size > 1) {
            val max = heap.remove() // O(logn)
            val nextMax = heap.remove() // O(logn)
            val diff = max - nextMax
            if (diff != 0) heap.add(diff) // O(logn)
        }

        return if (heap.isEmpty()) 0 else heap.peek() // always O(1)
    }
}

fun main() {
    println(LastStoneWeight().priorityQueue(intArrayOf(8, 7, 4, 2, 1, 1))) // 8 7 4 2 1 1
}
