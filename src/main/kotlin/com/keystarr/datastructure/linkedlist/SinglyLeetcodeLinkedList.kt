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
 *      singly linked list like java's (does it have it? or only doubly?) with more methods and optimizations.
 */
class SinglyLeetcodeLinkedList : LeetcodeLinkedList {

    private var head: Node? = null
    private var size: Int = 0

    // weird specification, once again we'd probably throw an exception in case the index is invalid
    override fun get(index: Int): Int = findNode(index)?.value ?: -1

    override fun addAtHead(value: Int) {
        increaseSize { head = Node(value = value, next = head) }
    }

    override fun addAtTail(value: Int) {
        if (size == 0) addAtHead(value) else addAtIndex(index = size, value = value)
    }

    override fun addAtIndex(index: Int, value: Int) {
        if (index == 0) {
            addAtHead(value)
            return
        }

        increaseSize {
            val prevNode = findNode(index - 1) ?: return // once again, weird specification, exception would be better
            prevNode.next = Node(value = value, next = prevNode.next)
        }
    }

    override fun deleteAtIndex(index: Int) {
        if (size == 0 || index == size) return

        if (index == 0) {
            deleteAtHead()
            return
        }

        decreaseSize {
            val prevNode = findNode(index - 1) ?: return // weird specification
            prevNode.next = prevNode.next?.next
        }
    }

    private fun deleteAtHead() {
        decreaseSize { head = head?.next }
    }

    private fun findNode(index: Int): Node? {
        if (index >= size) return null

        var currentNode = head
        repeat(index) { currentNode = currentNode!!.next }
        return currentNode!!
    }

    private inline fun increaseSize(block: () -> Unit) {
        block()
        size++
    }

    private inline fun decreaseSize(block: () -> Unit) {
        block()
        size--
    }

    private data class Node(
        val value: Int,
        var next: Node? = null,
    )
}

fun main() {
    val list: LeetcodeLinkedList = SinglyLeetcodeLinkedList()
    list.apply {
        debugPrint { addAtHead(2) }
        debugPrint { deleteAtIndex(1) }
        debugPrint { addAtHead(2) }
        debugPrint { addAtHead(7) }
        debugPrint { addAtHead(3) }
        debugPrint { addAtHead(2) }
        debugPrint { addAtHead(5) }
        debugPrint { addAtTail(5) }
        println(get(5))
    }
}


private fun LeetcodeLinkedList.debugPrint(action: () -> Unit) {
    action()

    var ind = 0
    var value = get(ind)
    while (value != -1) {
        println(value)
        ind++
        value = get(ind)
    }
    println("---")
}
