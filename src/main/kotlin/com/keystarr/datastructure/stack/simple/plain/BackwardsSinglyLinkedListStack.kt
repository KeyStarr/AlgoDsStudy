package com.keystarr.datastructure.stack.simple.plain

import com.keystarr.datastructure.stack.simple.SimpleStack
import com.keystarr.datastructure.stack.simple.debugWithPeek

/**
 * Goal - implement the basic stack using a backwards singly linked list, meaning each node has only a `prev` reference.
 * All operations must perform with O(1) time, the stack itself must take exactly O(n) space.
 *
 * My favorite one out of current [SimpleStack] implementations, extremely elegant!
 * UPD: apparently I made things a bit overcomplicated and the same can be achieved using a classic [SinglyLinkedListStack]
 */
class BackwardsSinglyLinkedListStack<T : Any> : SimpleStack<T> {

    private var tail: Node<T>? = null
    private var _size = 0

    override val size: Int
        get() = _size

    override fun push(value: T) {
        tail = Node(value = value, prev = tail)
        _size ++
    }

    override fun pop(): T? = tail?.value.apply { tail = tail?.prev }.also { _size-- }

    override fun peek(): T? = tail?.value

    override fun clear() {
        tail = null
    }

    private class Node<T>(
        val value: T,
        var prev: Node<T>? = null,
    )
}

fun main() {
    val stack: SimpleStack<Int> = BackwardsSinglyLinkedListStack()
    stack.apply {
        debugWithPeek { push(3) }
        debugWithPeek { push(8) }
        println(pop())
        println(pop())
        println(pop())
    }
}
