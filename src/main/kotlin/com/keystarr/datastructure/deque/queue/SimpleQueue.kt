package com.keystarr.datastructure.deque.queue

interface SimpleQueue<E> {

    fun add(element: E)

    fun peek(): E?

    fun poll(): E?
}
