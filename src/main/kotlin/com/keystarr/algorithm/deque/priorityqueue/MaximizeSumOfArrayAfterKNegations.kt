package com.keystarr.algorithm.deque.priorityqueue

import java.util.PriorityQueue

/**
 * LC-1005 https://leetcode.com/problems/maximize-sum-of-array-after-k-negations/description/
 * difficulty: easy (imho, leet-medium 100% for sheer amount of logic for O(nlogn) solution, and especially for O(n) solution via counting sort)
 * constraints:
 *  • 1 <= nums.size <= 10^4;
 *  • -100 <= nums\[i] <= 100;
 *  • 1 <= k <= 10^4.
 *
 * Final notes:
 *  • done [heap] by myself in 25 mins;
 *  • tried to do the sorting approach for curiosity, cause it sounded easier on paper, and just drowned in logic, failed
 *   to do it 25 mins and decided not to continue.
 *
 * Value gained:
 *  • practiced recognizing and solving a max sum after modifications type problem using a min heap.
 */
class MaximizeSumOfArrayAfterKNegations {

    // TODO: do the time O(n) space O(1) solution using counting sort, after learning it
    //  ref - https://leetcode.com/problems/maximize-sum-of-array-after-k-negations/solutions/252849/c-java-o-n-o-1/

    /**
     * goal: maximize the sum while performing exactly k operations.
     *
     * there may be negative elements
     *
     * cases:
     *  1. if there are negative elements left => -1 * the next minimum negative element (cause its the largest contribution
     *   for a single step to the goal in that case);
     *  2. if are no negative elements left, if there's a 0 => simply return the current sum, i.o. as if we'd applied all remaining operations to the element == 0;
     *  3. if there are no negative elements and no 0 elements =>
     *   a) if the remaining k is even, return the current sum, i.o as if we've applied all k operations to the same element,
     *    and since it was even amounta times => the elements preserves its sign in the end;
     *   b) if k is odd, minimum element * -1, and return the resulting sum (cause its the least piece we can take from the metric, and we want it maxed).
     *
     * design choices to efficiently implement these decision tree:
     *  1. repeatedly find minimum elements (while negatives are left, or 0) in the live modified collection
     *   => minHeap or since we're only concerned with negative elements, could use do sorting;
     *  2. if negative numbers are gone, if the min element is a 0, return the sum; else we get the minimum positive number,
     *   so the decision tree is satisfied.
     *
     * Edge cases:
     *  - sum => what is max sum? => max amount of elements each is max => 10^4 * 10^2 = 10^6, fits into int.
     *
     * nums=3 -1 0 2
     * operationsCount=3
     * sum=4
     *
     * heap = 3 1 0 2
     * cMin=-1
     * oL=2
     * sum=6
     *
     *
     * Time: average/worst O(nlogn), best O(n)
     *  - heapify and compute sum O(n);
     *  - main loop:
     *   - worst is all numbers are negative and k>nums.size, we perform n remove and n add operations => O(nlogn) total time;
     *   - best is there are no negative numbers => total time is O(n).
     * Space: always O(n)
     *  - heap has always n elements.
     */
    fun heap(nums: IntArray, operationsCount: Int): Int {
        var operationsLeft = operationsCount
        val minHeap = PriorityQueue(nums.toList()) // convert to heapify for O(n) time
        var currentSum = nums.sum()
        while (operationsLeft > 0) {
            val currentMin = minHeap.peek()
            if (currentMin < 0) {
                val newElement = minHeap.remove() * -1
                minHeap.add(newElement)
                currentSum += newElement * 2
                operationsLeft--
            } else if (currentMin == 0) {
                return currentSum
            } else {
                if (operationsLeft % 2 == 1) currentSum -= currentMin * 2
                return currentSum
            }
        }
        return currentSum
    }
}

fun main() {
    println(
        MaximizeSumOfArrayAfterKNegations().heap(
            nums = intArrayOf(-4, -2, -3),
            operationsCount = 4,
        )
    )
}

// 8 5 5 3 2 3