package com.keystarr.datastructure.queue

interface SimpleQueue<E> {

    fun add(element: E)

    fun peek(): E?

    fun poll(): E?
}
