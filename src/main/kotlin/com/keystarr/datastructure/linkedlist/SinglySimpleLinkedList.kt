package com.keystarr.datastructure.linkedlist

/**
 * LC-707 https://leetcode.com/problems/design-linked-list/description/
 * difficulty: medium
 * constraints:
 *  • 0 <= index, val <= 1000;
 *  • at most 2000 calls to all methods;
 *  • no explicit time/space.
 *
 * Value gained:
 *  • failed 1 submission cause missed the case of deleteAtIndex when index=size! Because implementation in both add
 *      and delete find a previous node to the desired index first, it's super important to think of these edge cases
 *      and catch them in early returns (for singly linked list. but maybe useful for actual problems as well);
 *  • interesting, would using "sentinel" nodes simplify the implementation by any means?
 *
 * Final notes:
 *  • ofc, I still have ways to go to really master the subject, might consider implementing a more real prod-like
 *      singly linked list like java's (does it have it? or only doubly?) with more methods and optimizations;
 *  • extended the implementation a bit outside the initial problem's scope.
 */
class SinglySimpleLinkedList<E : Any> : SinglyLinkedList<E> {

    private var head: Node<E>? = null
    private var size: Int = 0

    override fun getHead(): E? = head?.value

    // weird specification, once again we'd probably throw an exception in case the index is invalid
    override fun get(index: Int): E? = findNode(index)?.value

    override fun addAtHead(value: E) {
        increaseSize { head = Node(value = value, next = head) }
    }

    override fun addAtIndex(index: Int, value: E) {
        if (index > size) throw IllegalArgumentException("$index is out of bounds")

        if (index == 0) {
            addAtHead(value)
            return
        }

        increaseSize {
            val prevNode = findNode(index - 1) ?: return // once again, weird specification, exception would be better
            prevNode.next = Node(value = value, next = prevNode.next)
        }
    }

    override fun removeAtIndex(index: Int): E? {
        if (size == 0 || index == size) throw IllegalArgumentException("$index is out of bounds")
        return if (index == 0) {
            removeHead()
        } else {
            decreaseSize { findNode(index - 1)!!.also { it.next = it.next?.next } }.value
        }
    }

    override fun removeHead(): E? = decreaseSize { head?.next?.also { head = it } }?.value

    private fun findNode(index: Int): Node<E>? {
        if (index >= size) return null

        var currentNode = head
        repeat(index) { currentNode = currentNode!!.next }
        return currentNode!!
    }

    private inline fun increaseSize(block: () -> Unit) {
        block()
        size++
    }

    private inline fun <R : Any?> decreaseSize(block: () -> R) = block().also { size-- }

    private data class Node<E : Any>(
        val value: E,
        var next: Node<E>? = null,
    )
}

fun main() {
    val list: SinglyLinkedList<Int> = SinglySimpleLinkedList()
    list.apply {
        debugPrint { addAtHead(2) }
        debugPrint { removeAtIndex(0) }
        debugPrint { addAtHead(4) }
        debugPrint { addAtHead(7) }
        debugPrint { addAtHead(3) }
        debugPrint { addAtHead(2) }
        debugPrint { addAtHead(5) }
        debugPrint { removeHead() }
        debugPrint { addAtHead(10) }
        println(get(5))
    }
}

private fun SinglyLinkedList<Int>.debugPrint(action: () -> Unit) {
    action()

    var ind = 0
    var value = get(ind)
    while (value != null) {
        println(value)
        ind++
        value = get(ind)
    }
    println("---")
}
