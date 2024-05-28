package com.keystarr.datastructure.stack.simple

interface SimpleStack<T : Any> {

    /**
     * Time: always O(1)
     */
    fun push(value: T)

    /**
     * Time: always O(1)
     * @returns null if empty
     */
    fun pop(): T?

    /**
     * Time: always O(1)
     * @returns null if empty
     */
    fun peek(): T?
}

fun <T : Any> SimpleStack<T>.withPeekDebug(action: () -> Unit) {
    action()
    println(peek())
}
