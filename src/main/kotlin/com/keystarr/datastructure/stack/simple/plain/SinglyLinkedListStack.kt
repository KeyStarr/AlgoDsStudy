package com.keystarr.datastructure.stack.simple.plain

import com.keystarr.datastructure.stack.simple.SimpleStack
import com.keystarr.datastructure.stack.simple.debugWithPeek

/**
 * Goal - implement the stack using the classic singly linked list, with each node linked to the NEXT one.
 * Same as [BackwardsSinglyLinkedListStack], but with nodes pointing in the, ahem, conventionally agreed upon direction.
 */
class SinglyLinkedListStack<T : Any> : SimpleStack<T> {

    private var head: Node<T>? = null
    private var _size = 0

    override val size: Int
        get() = _size

    override fun push(value: T) {
        head = Node(value = value, next = head)
        _size ++
    }

    override fun pop(): T? = head?.value.apply { head = head?.next }.also { _size-- }

    override fun peek(): T? = head?.value

    override fun clear() {
        head = null
    }

    private class Node<T>(
        val value: T,
        val next: Node<T>? = null,
    )
}

fun main() {
    val stack: SimpleStack<Int> = SinglyLinkedListStack()
    stack.apply {
        debugWithPeek { push(3) }
        debugWithPeek { push(8) }
        println(pop())
        println(pop())
        println(pop())
    }
}
