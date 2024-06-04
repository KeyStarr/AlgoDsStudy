package com.keystarr.algorithm.deque.queue

/**
 * LC-346 https://leetcode.com/problems/moving-average-from-data-stream/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= windowSize <= 10^3;
 *  • -10^5 <= value <= 10^5;
 *  • at most 10^4 calls will be made to next;
 *  • no explicit time/space;
 *  • single threaded.
 */
class MovingAverage(private val windowSize: Int) {

    private val queue = ArrayDeque<Int>()
    private var currentSum: Double = 0.0

    /**
     * The list of current relevant numbers changes every time we exceed the window, and on each subsequent time we
     * add the new number - as we add the new number, we have to remove the oldest received number from the current window
     * => remove from head (the oldest), add to tail => FIFO => use the Queue to keep track of the current relevant numbers.
     *
     * (saving all received numbers and iterating each time from the end for `windowSize` would give O(n*m) time complexity,
     * where n = is the number of calls, m=windowSize, with O(n) space)
     *
     * Idea:
     *  - create a queue as the class field;
     *  - create currentSum as the class field;
     *  - on each [next] call:
     *      - queue.add(newNumber)
     *      - if queue.size > windowSize:
     *          - val polledNumber = queue.poll()
     *          - currentSum -= polledNumber
     *      - currentSum += newNumber
     *      - return currentSum/windowSize
     *
     * Edge cases:
     *  - max sum is 10^3*10^5=10^8 => fits into int;
     *  - windowSize=1 => works ok;
     *  - windowSize=number of calls => works ok;
     *  - first call ok cause windowSize > 0;
     *  - call to next when queue.size < windowSize => COMPUTE AVERAGE BY QUEUE.SIZE not windowSize! (failed a submission lol)
     *
     * Time: O(n), where n = number of calls
     * Space: O(k), where k = windowSize.
     */
    fun next(newNumber: Int): Double {
        queue.addLast(newNumber)
        if (queue.size > windowSize) currentSum -= queue.removeFirst()
        currentSum += newNumber
        return currentSum / queue.size
    }
}
