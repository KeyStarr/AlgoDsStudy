package com.keystarr.datastructure.stack.simple

/**
 * All time
 */
interface SimpleStack<T : Any> {

    /**
     * Time: always O(1), amortized O(1) allowed
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

    fun clear()
}

fun <T : Any> SimpleStack<T>.withPeekDebug(action: () -> Unit) {
    action()
    println(peek())
}
