package com.keystarr.datastructure.linkedlist

/**
 * LC-707 https://leetcode.com/problems/design-linked-list/description/
 * constraints same as in [SinglyLeetcodeLinkedList]
 *
 * Final notes:
 *  • a real implementation would probably expose first/last nodes too! as public val fields or via public methods
 *
 * Value gained:
 *  • cool, so the implementation, however basic mine is, is considerably more complicated than [SinglyLeetcodeLinkedList],
 *      however has not that much of a bigger const compared to [SinglyLeetcodeLinkedList], and same asymptotic complexities;
 *      but really allows that sweet n/2 iterations eliminations for accessing elements on positions [n/2;n) and
 *      a const access for the last element;
 *  • a buffer reference ("dummy pointer") came crucially useful for [addAtIndex], at first bumped into a bug without it,
 *      might be a common pitfall for linkedlist problems! (writing into a field the previous value of which still has
 *      to be used later!)
 *
 * Final notes:
 *  • ofc, I still have ways to go to really master the subject, might consider implementing a more real prod-like
 *      doubly linked list like java's with more methods and optimizations.
 */
class DoublyLeetcodeLinkedList : LeetcodeLinkedList {

    private var head: Node? = null
    private var tail: Node? = null
    private var size: Int = 0

    override fun get(index: Int): Int = findNode(index)?.value ?: -1

    internal fun getNode(index: Int): Node? = findNode(index)

    override fun addAtHead(value: Int) {
        increaseSize {
            head = Node(value = value, prev = null, next = head)
            head?.next?.prev = head
            if (size == 0) tail = head
        }
    }

    override fun addAtTail(value: Int) {
        increaseSize {
            tail = Node(value = value, prev = tail, next = null)
            tail?.prev?.next = tail
            if (size == 0) head = tail
        }
    }

    override fun addAtIndex(index: Int, value: Int) {
        if (index == 0) {
            addAtHead(value)
            return
        }

        if (index == size) {
            addAtTail(value)
            return
        }

        if (index > size) return // weird specification, I'd throw an exception instead

        increaseSize {
            val prevNode = findNode(index - 1)
            val currentNode = prevNode?.next
            val newNode = Node(value = value, prev = prevNode, next = currentNode)
            prevNode?.next = newNode
            currentNode?.prev = newNode
        }
    }

    override fun deleteAtIndex(index: Int) {
        if (size == 0 || index >= size) return

        if (index == 0) {
            decreaseSize {
                head = head?.next
                head?.prev = null
            }
            return
        }

        if (index == size - 1) {
            decreaseSize {
                tail = tail?.prev
                tail?.next = null
            }
            return
        }

        decreaseSize {
            val node = findNode(index)!!
            node.prev!!.next = node.next
            node.next!!.prev = node.prev
        }
    }

    private fun findNode(index: Int): Node? {
        if (index >= size) return null

        return if (index < size / 2) {
            var currentNode = head
            repeat(index) { currentNode = currentNode!!.next }
            currentNode
        } else {
            var currentNode = tail
            repeat(size - (index + 1)) { currentNode = currentNode!!.prev }
            currentNode
        }
    }

    private inline fun increaseSize(block: () -> Unit) {
        block()
        size++
    }

    private inline fun decreaseSize(block: () -> Unit) {
        block()
        size--
    }

    internal class Node(
        val value: Int,
        var prev: Node? = null,
        var next: Node? = null,
    ) {
        override fun toString(): String = "${value}, n=${next?.value}, p=${prev?.value}"
    }
}

fun main() {
    val list = DoublyLeetcodeLinkedList()
    list.apply {
        debugPrint { addAtHead(1) }
        debugPrint { addAtTail(3) }
        debugPrint { addAtIndex(1, 2) }
        println(get(1))
        debugPrint { deleteAtIndex(1) }
        println(get(1))
    }
}

private fun DoublyLeetcodeLinkedList.debugPrint(action: () -> Unit) {
    action()

    var ind = 0
    var node = getNode(ind)
    while (node != null) {
        println(node)
        ind++
        node = getNode(ind)
    }
    println("---")
}
