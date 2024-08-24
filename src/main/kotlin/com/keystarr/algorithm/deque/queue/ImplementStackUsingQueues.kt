package com.keystarr.algorithm.deque.queue

/**
 * LC-225 https://leetcode.com/problems/implement-stack-using-queues/description/
 * difficulty: easy
 *
 * full input: 1 2 3 4 5
 * push so far: 1 2 3
 * queue: 3 2 1
 *
 * constraints:
 *  • 1 <= value <= 9
 *  • at most 100 calls total
 *
 * One queue => technically we could do the same as 2 queues, but add elements into the temp array, and then the new element
 *  into the queue, and then all elements from the temp array back into the queue. Or are we supposed to not use any collections
 *  EXCEPT that single queue?
 *
 * Final notes:
 *  • done the 2 queue approach by myself under 10 mins. Designed the 1 queue with an array instead of the 2nd one quickly,
 *   decided not to go deeper since its the easy question and probably would stop there in a real interview;
 *  • => checked the 1 queue solution, learned the core idea thanks to https://leetcode.com/problems/implement-stack-using-queues/solutions/62516/concise-1-queue-java-c-python/
 *  • very similar to [com.keystarr.algorithm.deque.stack.ImplementQueueUsingStacks]
 *
 * Value gained:
 *  • practiced queues and stacks.
 */
class ImplementStackUsingQueues {

    private var mainQueue = ArrayDeque<Int>()
    private var tempQueue = ArrayDeque<Int>()

    /* TWO QUEUES */

    /**
     * Time: average/worst O(n), where n=number of calls to push
     *  worst is when we called push n times and only then the first pop.
     * Space: amortized O(1) since we don't allocate new objects, may only reallocate for expansion the inner array in the deck
     */
    fun pushTwoQueues(value: Int) {
        tempQueue.add(value)
        while (mainQueue.isNotEmpty()) tempQueue.add(mainQueue.removeFirst())

        val buff = mainQueue
        mainQueue = tempQueue
        tempQueue = buff
    }

    fun popTwoQueues(): Int = mainQueue.removeFirst()

    fun topTwoQueues(): Int = mainQueue.first()

    fun emptyTwoQueues(): Boolean = mainQueue.isEmpty()

    /* FOLLOW UP - ONE QUEUE */

    /**
     * 1. put the new element in the queue;
     * 2. remove all elements from the queue until we reach the new element and straight away add them back into the queue
     *  => queue.enqueue(queue.dequeue) just cycles through the elements, shifts each first one to the end of the queue
     *   => if done n times, gives the original state before modifications.
     *
     * so we basically put the new element to be the front of the queue (adhering to stack's FIFO), but cycle all elements in
     * front of it to be in the back of it, preserving their reversed order of insertion.
     */
    fun push(value: Int) {
        val elementsToCycle = mainQueue.size
        mainQueue.addLast(value)
        repeat(elementsToCycle) { mainQueue.addLast(mainQueue.removeFirst()) }
    }
}
