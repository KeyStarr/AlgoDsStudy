package com.keystarr.datastructure.stack.monotonic

import com.keystarr.datastructure.stack.simple.SimpleStack
import com.keystarr.datastructure.stack.simple.debugPrintAll
import com.keystarr.datastructure.stack.simple.debugWithPeek
import com.keystarr.datastructure.stack.simple.isNotEmpty
import com.keystarr.datastructure.stack.simple.plain.ArrayListStack

/**
 * Same as [NonDecreasingMonotonicStack] but the order is the opposite, each next element must be less than or equal to
 * the previous.
 */
class NonIncreasingMonotonicStack<T : Comparable<T>>(private val simpleStack: SimpleStack<T>) : SimpleStack<T> {

    override val size: Int
        get() = simpleStack.size

    override fun push(value: T) {
        while (simpleStack.isNotEmpty() && simpleStack.peek()!! < value) {
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
    val stack: SimpleStack<Int> = NonIncreasingMonotonicStack(ArrayListStack())
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
