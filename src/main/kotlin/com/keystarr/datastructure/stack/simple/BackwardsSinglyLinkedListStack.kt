package com.keystarr.datastructure.stack.simple

/**
 * Goal - implement the basic stack using a backwards singly linked list, meaning each node has only a `prev` reference.
 * All operations must perform with O(1) time, the stack itself must take exactly O(n) space.
 *
 * My favorite one out of current [SimpleStack] implementations, extremely elegant!
 */
class BackwardsSinglyLinkedListStack<T : Any> : SimpleStack<T> {

    private var tail: Node<T>? = null

    override fun push(value: T) {
        tail = Node(value = value, prev = tail)
    }

    override fun pop(): T? {
        val oldTail = tail ?: return null
        tail = tail?.prev
        return oldTail.value
    }

    override fun peek(): T? = tail?.value

    private class Node<T>(
        val value: T,
        var prev: Node<T>? = null,
    )
}

fun main() {
    val stack: SimpleStack<Int> = BackwardsSinglyLinkedListStack()
    stack.apply {
        withPeekDebug { push(3) }
        withPeekDebug { push(8) }
        println(pop())
        println(pop())
        println(pop())
    }
}
