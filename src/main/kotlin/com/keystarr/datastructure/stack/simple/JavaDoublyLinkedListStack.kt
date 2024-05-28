package com.keystarr.datastructure.stack.simple

import java.util.LinkedList

/**
 * based on LC-155 https://leetcode.com/problems/min-stack/
 * but without the getMin method
 *
 * goal - practice implementing the default simple stack, supporting the most common methods and guaranteeing
 *  the required  time complexity
 *
 * Stack=LIFO
 * Core idea: use a default java doubly linked list as a backbone
 *  - push => link to tail, O(1)
 *  - pop => unlink and return O(1)
 *
 * Could've just used a singly linked list, but linked backwards!
 */
class JavaDoublyLinkedListStack<T : Any> : SimpleStack<T> {

    private val items = LinkedList<T>()

    override fun push(value: T) {
        items.addLast(value)
    }

    override fun pop(): T? = if (peek() != null) items.pop() else null

    override fun peek(): T? = items.peek()
}

fun main() {
    val stack: SimpleStack<Int> = JavaDoublyLinkedListStack()
    stack.apply {
        withPeekDebug { push(3) }
        withPeekDebug { push(8) }
        println(pop())
        println(pop())
        println(pop())
    }
}
