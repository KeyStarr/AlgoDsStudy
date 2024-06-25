package com.keystarr.datastructure.deque.stack.simple.plain

import com.keystarr.datastructure.deque.stack.simple.SimpleStack
import com.keystarr.datastructure.deque.stack.simple.debugWithPeek

/**
 * Goal - implement a basic stack using an arraylist. Each method must have O(1) time, with push allowed to have
 * an amortized O(1).
 *
 * Basically in use cases where we operate on relatively small amount of elements and know and can set their max size
 * in advance (maybe for a bit of space overhead or optimizing space but guaranteeing minimal resizing):
 *  - the const for time of each method is slightly better than [BackwardsSinglyLinkedListStack];
 *  - the const for space is significantly better than [BackwardsSinglyLinkedListStack].
 * (my own reasoning/experimentation).
 *
 * note: null elements instead of `items.removeAt(currentInd)` because we
 */
class ArrayListStack<T : Any>(initialCapacity: Int? = null) : SimpleStack<T> {

    private val items: ArrayList<T?> = initialCapacity?.let { ArrayList(initialCapacity) } ?: ArrayList()
    private var currentInd = EMPTY_CURRENT_IND

    override val size: Int
        get() = currentInd + 1

    override fun push(value: T) {
        items.add(value)
        currentInd++
    }

    override fun pop(): T? =
        if (currentInd == EMPTY_CURRENT_IND) {
            null
        } else {
            // never shift elements since we always remove the last one (always time O(1))
            items.removeAt(currentInd).apply { currentInd-- }
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
        debugWithPeek { push(3) }
        debugWithPeek { push(8) }
        println(pop())
        println(pop())
        println(pop())
    }
}