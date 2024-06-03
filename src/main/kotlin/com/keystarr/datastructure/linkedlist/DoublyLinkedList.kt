package com.keystarr.datastructure.linkedlist

/**
 * Specification based on https://leetcode.com/problems/design-linked-list/ plus more convenience methods like [removeTail].
 */
interface DoublyLinkedList<E: Any> {

    fun getHead(): E?

    fun getTail(): E?

    fun get(index: Int): E?

    fun addAtHead(value: E)

    fun addAtTail(value: E)

    fun addAtIndex(index: Int, value: E)

    /**
     *
     * @return null if list is empty and no remove was performed.
     */
    fun removeHead(): E?

    /**
     *
     * @return null if list is empty and no remove was performed.
     */
    fun removeTail(): E?

    /**
     *
     * @throws IllegalArgumentException index is out of bounds.
     */
    fun removeAtIndex(index: Int): E?
}
