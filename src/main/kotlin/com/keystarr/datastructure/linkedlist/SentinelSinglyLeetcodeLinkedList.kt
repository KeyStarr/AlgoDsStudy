package com.keystarr.datastructure.linkedlist

/**
 * Task and general implementation same as [SinglySimpleLinkedList], but use 'sentinel nodes' => in a case of
 * a singly linked list, create a 'dummy' head node which value will always be default; reason - test the assumption
 * that it simplifies the implementation somehow.
 *
 * Value gained:
 *  • weird. Basically changed 3 lines and at least to me implementation only got worse: now we have to keep in the head
 *      at all times that the "head" node is actually a dummy and has no real value (hence, say, [findNode] start with
 *      head.next always).
 *  • have I done it wrong? or what's the catch?
 *
 * TODO: revisit & fix, I definitely got the "sentinel node" concept wrong
 */
class SentinelSinglyLeetcodeLinkedList<E : Any> : SinglyLinkedList<E> {

    private val head = Node<E>(value = null)
    private var size: Int = 0

    override fun get(index: Int): E? = findNode(index)?.value

    override fun getHead(): E? = head.next?.value

    override fun addAtHead(value: E) {
        head.next = Node(value = value, next = head.next)
        size++
    }

    override fun addAtIndex(index: Int, value: E) {
        if (index == 0) {
            addAtHead(value)
            return
        }

        val prevNode = findNode(index - 1) ?: return // once again, weird specification, exception would be better
        prevNode.next = Node(value = value, next = prevNode.next)
        size++
    }

    override fun removeAtIndex(index: Int): E? {
        if (size == 0 || index == size) throw IllegalArgumentException("$index is out of bounds")
        if (index == 0) return removeHead()

        val prevNode = findNode(index - 1)!!
        return prevNode.next?.also {
            prevNode.next = prevNode.next?.next
            size--
        }?.value
    }

    override fun removeHead(): E? = head.next.also {
        head.next = head.next?.next
        size--
    }?.value

    private fun findNode(index: Int): Node<E>? {
        if (index >= size) return null

        var currentNode: Node<E> = head.next!!
        repeat(index) { currentNode = currentNode.next!! }
        return currentNode
    }

    private data class Node<E : Any>(
        val value: E?,
        var next: Node<E>? = null,
    )
}
