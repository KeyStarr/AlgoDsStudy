package com.keystarr.datastructure.stack.simple

/**
 * Goal - implement a basic stack using an arraylist. Each method must have O(1) time, with push allowed to have
 * an amortized O(1).
 *
 * Basically in use cases where we operate on relatively small amount of elements and know and can set their max size
 * in advance (maybe for a bit of space overhead or optimizing space but guaranteeing minimal resizing):
 *  - the const for time of each method is slightly better than [BackwardsSinglyLinkedListStack];
 *  - the const for space is significantly better than [BackwardsSinglyLinkedListStack].
 * (my own reasoning/experimentation)
 */
class ArrayListStack<T : Any>(initialCapacity: Int? = null) : SimpleStack<T> {

    private val items: ArrayList<T?> = initialCapacity?.let { ArrayList(it) } ?: ArrayList()
    private var currentInd = EMPTY_CURRENT_IND

    override fun push(value: T) {
        items.add(value)
        currentInd++
    }

    override fun pop(): T? =
        if (currentInd == EMPTY_CURRENT_IND) {
            null
        } else {
            items[currentInd].apply {
                items[currentInd] = null
                currentInd--
            }
        }

    override fun peek(): T? = items[currentInd]

    override fun clear() {
        items.clear()
    }
}

private const val EMPTY_CURRENT_IND = -1

fun main() {
    val stack: SimpleStack<Int> = ArrayListStack()
    stack.apply {
        withPeekDebug { push(3) }
        withPeekDebug { push(8) }
        println(pop())
        println(pop())
        println(pop())
    }
}