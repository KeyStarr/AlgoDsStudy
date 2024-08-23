package com.keystarr.algorithm.deque.stack

/**
 * LC-232 https://leetcode.com/problems/implement-queue-using-stacks/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= x <= 9
 *  • 0 <= number of calls across all methods summed <= 100
 *
 * goal: implement a queue using only 2 stacks.
 *
 * incoming all push: 1 2 3 4 5
 * current queue: 1 2 3
 * 1stack: 4 3 2 1
 * 2stack:
 *
 * core operations to design for: push and pop.
 *
 * -----------------------
 *
 * Core DS property: at any method call, [mainStack] must contain all pushed but not popped elements so far in the reversed order.
 * - keep 1 stack always empty, and another must contain elements in the order opposite to insertion;
 * - upon push, pop all elements from one stack to the temp stack, add the new element into the main stack, then add
 *  all prior elements from buff to the main stack => maintain the inverse insertion order.
 * => peek() is just mainStack.peek(), empty() is just mainStack.isNotEmpty()
 *
 * Or is push() supposed to be Time O(1)? Don't think so, generally the Queue is expected to be so, but it still depends on the implementation.
 *
 * Final notes:
 *  • done by myself in 20 mins. Got the intuition from the start that there must be some kinda property to maintain using
 *   the two stacks in between each modification calls, but struggled to find it while playing with possible algorithm
 *   decisions live using an input/output example (poking it with a stick!) helped tremendously by the way 🔥;
 *  • LOL 20 mins on easy problem ⚠️. Not sure what I could've done better, just sorta not overcomplicate stuff, cause
 *   I expected this to have some complexity like mediums. What did we learn, Palmer?
 *
 * Value gained:
 *  • practiced Stacks and Queues.
 */
class ImplementQueueUsingStacks {

    private val mainStack = ArrayList<Int>()
    private val tempStack = ArrayList<Int>()

    // TODO: try the second solution, verify why is it amortized O(1) O(1) (some debate that in the comment section)

    /**
     * Time: always O(n), where n=number of elements in the stack at the moment of the call, or average/worst O(m), where
     *  m=number of all calls to push.
     * Space: since both stacks are preallocated, and we don't them here, O(1).
     */
    fun push(number: Int) {
        while (mainStack.isNotEmpty()) tempStack.add(mainStack.removeLast())
        mainStack.add(number)
        while (tempStack.isNotEmpty()) mainStack.add(tempStack.removeLast())
    }

    fun pop(): Int = mainStack.removeLast()

    fun peek(): Int = mainStack.last()

    fun empty(): Boolean = mainStack.isEmpty()
}

fun main() {
    ImplementQueueUsingStacks().apply {
        push(1)
        push(2)
        println(peek())
        println(pop())
        println(empty())
    }
}
