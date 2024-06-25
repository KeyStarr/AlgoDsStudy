package com.keystarr.datastructure.deque.stack.simple

/**
 * All time
 */
interface SimpleStack<T : Any> {

    val size: Int

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

inline fun <T : Any> SimpleStack<T>.debugWithPeek(action: () -> Unit) {
    action()
    println(peek())
}

inline fun <reified T : Any> SimpleStack<T>.debugPrintAll() {
    val elements = Array<T?>(size = size) { null }
    var ind = size - 1
    while (isNotEmpty()) elements[ind--] = pop()
    println(elements.contentToString())
}

fun <T : Any> SimpleStack<T>.isNotEmpty() = size != 0
