package com.keystarr.datastructure.deque.queue

import com.keystarr.datastructure.graph.linkedlist.DoublySimpleLinkedList

class LinkedListQueue<E : Any> : SimpleQueue<E> {

    private val elements = DoublySimpleLinkedList<E>()

    override fun add(element: E) {
        elements.addAtTail(element)
    }

    override fun peek(): E? = elements.getHead()

    override fun poll(): E? = elements.removeHead()
}

fun main() {
    LinkedListQueue<Int>().apply {
        add(3)
        debugPrintPeek()
        add(4)
        debugPrintPeek()
        debugPrintPoll()
        debugPrintPoll()
        debugPrintPoll()
        debugPrintPeek()
    }
}

private fun LinkedListQueue<Int>.debugPrintPeek() = println("peek: ${peek()}")

private fun LinkedListQueue<Int>.debugPrintPoll() = println("poll: ${poll()}")
