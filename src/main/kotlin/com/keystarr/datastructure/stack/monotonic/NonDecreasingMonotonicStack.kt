package com.keystarr.datastructure.stack.monotonic

import com.keystarr.datastructure.stack.simple.SimpleStack
import com.keystarr.datastructure.stack.simple.debugPrintAll
import com.keystarr.datastructure.stack.simple.isNotEmpty
import com.keystarr.datastructure.stack.simple.plain.ArrayListStack
import com.keystarr.datastructure.stack.simple.debugWithPeek

/**
 * Concept - a stack the order of elements in which is always sorted monotonically non-decreasing (increasing or equal).
 *
 * Usually useful to implement on-the-spot and customize according to the problem, to arrive at an efficient solution
 * and reduce O(n) time factor of a single iteration to O(1).
 *
 * e.g. we have [30, 40, 100] in the stack and receive [push] with 35 => to preserve the order in the stack we have to:
 *  - peek() => 100, is not <= 35 => pop() 100;
 *  - peek() => 40, is not <= 35 => pop() 40;
 *  - peek() => 30 is <= 30, stop popping;
 *  - [simpleStack].push(35).
 *
 * If we pushed before that, then stack would've been [30, 40, 100, 35] or [30, 40, 35], both collections are not sorted
 * in a monotonically non-decreasing order => invalid.
 *
 * There are 2 ways basically to preserve the sorting in the stack:
 *  1. simply pop all elements that, when the new one is added, break the order, and push the new one only when, relative to
 *      the last element, the order is maintained;
 *  2. or do all that in p.1 but save all popped elements temporarily and add them back into the stack.
 *
 * The classic "monotonic" stack/queue/dequeue pattern is defined only by 1.
 */
class NonDecreasingMonotonicStack<T : Comparable<T>>(private val simpleStack: SimpleStack<T>) : SimpleStack<T> {

    override val size: Int
        get() = simpleStack.size

    override fun push(value: T) {
        while (simpleStack.isNotEmpty() && simpleStack.peek()!! > value) {
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
}

fun main() {
    val stack: SimpleStack<Int> = NonDecreasingMonotonicStack(ArrayListStack())
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
