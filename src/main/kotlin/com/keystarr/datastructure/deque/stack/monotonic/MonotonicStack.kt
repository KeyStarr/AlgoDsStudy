package com.keystarr.datastructure.deque.stack.monotonic

import com.keystarr.datastructure.deque.stack.monotonic.MonotonicStack.Type.*
import com.keystarr.datastructure.deque.stack.simple.SimpleStack
import com.keystarr.datastructure.deque.stack.simple.debugPrintAll
import com.keystarr.datastructure.deque.stack.simple.debugWithPeek
import com.keystarr.datastructure.deque.stack.simple.isNotEmpty
import com.keystarr.datastructure.deque.stack.simple.plain.ArrayListStack

/**
 * Concept - a stack the order of elements in which is always sorted monotonically.
 *
 * Usually useful to implement on-the-spot and customize according to the problem, to arrive at an efficient solution
 * and reduce O(n) time factor of a single iteration to O(1).
 *
 * There are 2 ways basically to preserve the sorting in the stack:
 *  1. simply pop all elements that, when the new one is added, would break the order relative to it.
 *      Push the new one only when the order is maintained between it and the top element;
 *  2. or do all that in p.1 but save all popped elements temporarily and add them back into the stack.
 *
 * The classic "monotonic" stack/queue/dequeue pattern is defined only by #1.
 *
 * ----- non-decreasing stack example -----
 *
 * We have [30, 40, 100] and try to [push] 35 => to preserve the order in the stack we have to:
 *  - peek() => 100, is not <= 35 => pop() 100;
 *  - peek() => 40, is not <= 35 => pop() 40;
 *  - peek() => 30 is <= 35, stop popping;
 *  - [simpleStack].push(35).
 *  result: stack=[30, 35]
 *
 * If we pushed before that, then stack would've been [30, 40, 100, 35] or [30, 40, 35], both collections are not sorted
 * in a monotonically non-decreasing order => invalid.
 */
class MonotonicStack<T : Comparable<T>>(
    type: Type,
    private val simpleStack: SimpleStack<T>,
) : SimpleStack<T> {

    // could've made an interface, extracted each implementation into a class and build a factory to bind it all together,
    // but, imho, that'd be overengineering for such a simple case
    private val isSortBroken: (mostRecent: T, new: T) -> Boolean = when (type) {
        NON_INCREASING -> { mostRecent, new -> new > mostRecent }
        DECREASING -> { mostRecent, new -> new >= mostRecent }
        NON_DECREASING -> { mostRecent, new -> new < mostRecent }
        INCREASING -> { mostRecent, new -> new <= mostRecent }
    }

    override val size: Int
        get() = simpleStack.size

    override fun push(value: T) {
        while (simpleStack.isNotEmpty() && isSortBroken(simpleStack.peek()!!, value)) {
            simpleStack.pop()
            // here we sometimes do some problem-related logic based on the popped value
        }
        simpleStack.push(value)
    }

    override fun pop(): T? = simpleStack.pop()

    override fun peek(): T? = simpleStack.peek()

    override fun clear() {
        simpleStack.clear()
    }

    enum class Type {
        NON_INCREASING,
        NON_DECREASING,
        INCREASING,
        DECREASING,
    }
}

// too lazy for unit test here for now :D
fun main() {
    println("-------- non increasing --------")
    runNonIncreasing()
    println("\n-------- non decreasing --------")
    runNonDecreasing()
}

private fun runNonIncreasing() {
    val stack: SimpleStack<Int> = MonotonicStack(NON_INCREASING, ArrayListStack())
    stack.apply {
        debugWithPeek { push(100) }
        debugWithPeek { push(80) }
        debugWithPeek { push(60) }
        debugWithPeek { push(70) }
        debugWithPeek { push(60) }
        debugWithPeek { push(75) }
        debugWithPeek { push(85) }
        debugPrintAll()
        println("expected 100 85")
    }
}

private fun runNonDecreasing() {
    val stack: SimpleStack<Int> = MonotonicStack(NON_DECREASING, ArrayListStack())
    stack.apply {
        debugWithPeek { push(100) }
        debugWithPeek { push(80) }
        debugWithPeek { push(60) }
        debugWithPeek { push(70) }
        debugWithPeek { push(60) }
        debugWithPeek { push(75) }
        debugWithPeek { push(85) }
        debugPrintAll()
        println("expected 60 60 75 85")
    }
}
