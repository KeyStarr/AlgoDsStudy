package com.keystarr.datastructure.graph.linkedlist

interface SinglyLinkedList<E : Any> {

    fun getHead(): E?

    fun get(index: Int): E?

    fun addAtHead(value: E)

    fun addAtIndex(index: Int, value: E)

    /**
     *
     * @return null if list is empty and no remove was performed.
     */
    fun removeHead(): E?

    /**
     *
     * @throws IllegalArgumentException index is out of bounds.
     */
    fun removeAtIndex(index: Int): E?
}