package com.keystarr.algorithm.deque.priorityqueue

import java.util.Comparator
import java.util.PriorityQueue

/**
 * LC-2208 https://leetcode.com/problems/minimum-operations-to-halve-array-sum/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= nums.length <= 10^5
 *  - 1 <= nums\[i] <= 10^7
 *
 * Final notes:
 *  - long sum edge case => OK
 *  - use decimal number to subtract AND to save the halved number => FAIL. I wanted to eat and felt in a rush. I felt
 *   like there probably is more to it with rounding, but gave up and just submitted a couple of times in a row without
 *   actually checking these edge cases... turns out, as to be expected, if the decimal numbers add up to more than one
 *   across all the halved numbers (which it easily can do, and even much more) then we might miss the right stop! our
 *   sum would be much less than a rounding error less than target => the answer is off by X operations;
 *  - exactly a half is AN EXIT condition => caught that edge case while designing but its AN EXIT condition, and I wrongfully
 *   wrote it as a keep-in-loop one.
 *
 * Value gained:
 *  - practiced solving a problem efficiently with a PriorityQueue;
 *  - practiced a problem where rounding is really important: it was sort of tricky cause the input is integers.
 *   For the future: whenever there is division of integers => consider whether we need floating point!
 */
class MinimumOperationsToHalveArraySum {

    /**
     * Observations:
     *  - the most contributing factors to the sum are the largest numbers in the array;
     *  - since we need the minimum amount of reduction operations => if we keep reducing the largest number every time
     *   (considering the halved maxes of the prev operations), once the resulting sum is less than originalSum/2, the
     *   amount of operations done is the answer!
     *
     * efficient access to actual max of a changing array with removals/insertions => a max PriorityQueue
     *
     * Idea:
     *  - target: Long =sum(nums)/2 // since input numbers are integers, floor int division is ok cause it would always be less
     *  - queue = PriorityQueue(Comparator.reverseOrder()) // maxheap
     *  - add all nums into queue
     *  - target=sum(nums)/2
     *  - currentSum: Long = Long.MAX_VALUE
     *  - operationsCount = 0
     *  - while(currentSum>target):
     *   - reducedMax = queue.remove() / 2
     *   - queue.add(reducedMax)
     *   - currentSum -= reducedMax
     *   - operationsCount++
     *  - return operationsCount
     *
     * Edge cases:
     *  - max sum is 10^7 * 10^5 = 10^12 => doesn't fit into Int (10^9), use Long for sum (10^18);
     *  - "reduce the sum to AT LEAST a half" => exactly a half, that's only when nums.size == 1 and its an even number
     *   => early return or just currentSum>=target, with equals, as the loop exit condition.
     *  - all elements are 1 => 1/2=0, so reduction will work correctly, even if its a single 1 (technically its 0.5, but
     *   0 in this case satisfies the condition of "reduce by exactly a half or less")
     *
     * Time: O(nlogn)
     *  - fill the maxheap always O(nlogn)
     *  - each removal O(logn), each insertion O(logn) => worst/average O(n*2logn)
     *   worst amount of total iterations is exactly n/2 if all elements are 1s
     * Space: O(n) for the heap
     */
    fun efficient(nums: IntArray): Int {
        if (nums.size == 1) return 1

        val queue = PriorityQueue<Float>(Comparator.reverseOrder())
        nums.forEach { queue.add(it.toFloat()) }

        var currentSum = 0.0
        nums.forEach { currentSum += it }

        val target = currentSum / 2
        var operationsCount = 0
        while (currentSum > target) {
            val reducedMax = queue.remove() / 2F
            queue.add(reducedMax)
            currentSum -= reducedMax
            operationsCount++
        }
        return operationsCount
    }
}

fun main() {
    println(MinimumOperationsToHalveArraySum().efficient(intArrayOf(1, 1)))
}
