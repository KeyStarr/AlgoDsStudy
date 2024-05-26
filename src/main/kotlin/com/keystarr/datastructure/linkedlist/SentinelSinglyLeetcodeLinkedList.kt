package com.keystarr.datastructure.linkedlist

/**
 * Task and general implementation same as [SinglyLeetcodeLinkedList], but use 'sentinel nodes' => in a case of
 * a singly linked list, create a 'dummy' head node which value will always be default; reason - test the assumption
 * that it simplifies the implementation somehow.
 *
 * Value gained:
 *  • weird. Basically changed 3 lines and at least to me implementation only got worse: now we have to keep in the head
 *      at all times that the "head" node is actually a dummy and has no real value (hence, say, [findNode] start with
 *      head.next always).
 *  • have I done it wrong? or what's the catch?
 */
class SentinelSinglyLeetcodeLinkedList : LeetcodeLinkedList {

    private val head = Node(value = -1)
    private var size: Int = 0

    override fun get(index: Int): Int = findNode(index)?.value ?: -1

    override fun addAtHead(value: Int) {
        head.next = Node(value = value, next = head.next)
        size++
    }

    override fun addAtTail(value: Int) {
        if (size == 0) addAtHead(value) else addAtIndex(index = size, value = value)
    }

    override fun addAtIndex(index: Int, value: Int) {
        if (index == 0) {
            addAtHead(value)
            return
        }

        val prevNode = findNode(index - 1) ?: return // once again, weird specification, exception would be better
        prevNode.next = Node(value = value, next = prevNode.next)
        size++
    }

    override fun deleteAtIndex(index: Int) {
        if (size == 0 || index == size) return

        if (index == 0) {
            deleteAtHead()
            return
        }

        val prevNode = findNode(index - 1) ?: return // weird specification
        prevNode.next = prevNode.next?.next
        size--
    }

    private fun deleteAtHead() {
        head.next = head.next?.next
        size--
    }

    private fun findNode(index: Int): Node? {
        if (index >= size) return null

        var currentNode = head.next!!
        repeat(index) { currentNode = currentNode.next!! }
        return currentNode
    }

    private data class Node(
        val value: Int,
        var next: Node? = null,
    )
}
