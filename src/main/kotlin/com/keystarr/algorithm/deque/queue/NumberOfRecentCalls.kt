package com.keystarr.algorithm.deque.queue

/**
 * LC-933 https://leetcode.com/problems/number-of-recent-calls/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= t <= 10^9;
 *  • t is strictly increasing on each test case;
 *  • at most 10^4 calls to [pingViaQueue];
 *  • no explicit time/space;
 *  • single thread.
 */
class RecentCounter {

    private val queue = ArrayDeque<Int>()
    private var recentRequestsCounter = 0

    /**
     * Naive idea:
     *  - create a doubly linkedlist;
     *  - upon receiving [pingViaQueue]:
     *      - add `requestTime` into the list;
     *      - iterate from the end of the list and count each element until element.requestTime > requestTime-3000;
     *      - return counter.
     *
     * Time: O(n^2) where n = number of calls, due to inner counting iteration
     * Space: O(n)
     *
     * How to improve time?
     * Observe - each time we shift the window only by advance = (newRequestTime-lastRequestTime) into the future and discard all
     * calls inside \[lastRequestTimeStart, lastRequestTimeStart+advance].
     *
     * Improve idea:
     *  - save node reference to the earliest valid call on each call after counting, save the counter result;
     *  - on each subsequent call iterate from that reference until element.requestTime falls into the new window, and
     *      decrement the counter for each call.
     *
     * Basically, first requestTime to be removed is the first requestTime that was added
     * => FIFO
     * => Queue!
     *
     * Time: always "amortized" O(n), since we perform at most n-1 removals from the [pingViaQueue] across ALL n calls, and each removal
     *  takes O(1) (roughly speaking, clearest case is when within each new call we remove 1 previous entry, then
     *  it falls down to const. if we remove more than 1 entry at any call, that means that some calls we won't remove
     *  anything => which is still roughly equivalent to the first case in terms of the amount of total removals)
     * Space: average/worst O(n)
     */
    fun pingViaQueue(newRequestTime: Int): Int {
        queue.addLast(newRequestTime)
        val threshold = newRequestTime - 3000
        while (queue.isNotEmpty() && queue.first() < threshold) {
            queue.removeFirst()
            recentRequestsCounter--
        }
        return ++recentRequestsCounter
    }

    /**
     * Same idea as the last in [pingViaQueue] but store items simply in an array and instead of removing just move
     * the pointer. Same time complexities, but worse space const and better time const (not to an equal ration? like,
     * max space required on average would become relatively worse than average time improvement?)
     */
    fun pingViaArray(newRequestTime: Int): Int{
        TODO("implement for fun later")
    }
}
