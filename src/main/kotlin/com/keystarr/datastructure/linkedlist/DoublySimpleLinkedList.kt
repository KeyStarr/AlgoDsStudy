package com.keystarr.datastructure.linkedlist

/**
 * LC-707 https://leetcode.com/problems/design-linked-list/description/
 * constraints same as in [SinglySimpleLinkedList]
 *
 * Final notes:
 *  • a real implementation would probably expose first/last nodes too! as public val fields or via public methods
 *
 * Value gained:
 *  • cool, so the implementation, however basic mine is, is considerably more complicated than [SinglySimpleLinkedList],
 *      however has not that much of a bigger const compared to [SinglyLeetcodeLinkedList], and same asymptotic complexities;
 *      but really allows that sweet n/2 iterations eliminations for accessing elements on positions [n/2;n) and
 *      a const access for the last element;
 *  • a buffer reference ("dummy pointer") came crucially useful for [addAtIndex], at first bumped into a bug without it,
 *      might be a common pitfall for linkedlist problems! (writing into a field the previous value of which still has
 *      to be used later!)
 *
 * Final notes:
 *  • ofc, I still have ways to go to really master the subject, might consider implementing a more real prod-like
 *      doubly linked list like java's with more methods and optimizations;
 *  • extended the implementation a bit outside the initial problem's scope.
 */
class DoublySimpleLinkedList<E : Any> : DoublyLinkedList<E> {

    private var head: Node<E>? = null
    private var tail: Node<E>? = null
    private var size: Int = 0

    override fun getHead(): E? = head?.value

    override fun getTail(): E? = tail?.value

    override fun get(index: Int): E? = findNode(index)?.value

    internal fun getNode(index: Int): Node<E>? = findNode(index)

    override fun addAtIndex(index: Int, value: E) {
        if (index > size) throw IllegalArgumentException("$index is out of bounds")

        if (index == 0) {
            addAtHead(value)
            return
        }

        if (index == size) {
            addAtTail(value)
            return
        }

        increaseSize {
            val prevNode = findNode(index - 1)
            val currentNode = prevNode?.next
            val newNode = Node(value = value, prev = prevNode, next = currentNode)
            prevNode?.next = newNode
            currentNode?.prev = newNode
        }
    }

    override fun addAtHead(value: E) {
        increaseSize {
            head = Node(value = value, prev = null, next = head)
            head?.next?.prev = head
            if (size == 0) tail = head
        }
    }

    override fun addAtTail(value: E) {
        increaseSize {
            tail = Node(value = value, prev = tail, next = null)
            tail?.prev?.next = tail
            if (size == 0) head = tail
        }
    }

    override fun removeAtIndex(index: Int): E? {
        if (size == 0 || index >= size) throw IllegalArgumentException("$index is out of bounds")
        if (index == 0) return removeHead()
        if (index == size - 1) return removeTail()

        return decreaseSize {
            val node: Node<E> = findNode(index)!!
            node.prev!!.next = node.next
            node.next!!.prev = node.prev
            node.value
        }
    }

    override fun removeHead(): E? = head?.apply {
        decreaseSize {
            head = head?.next
            head?.prev = null
        }
    }?.value

    override fun removeTail(): E? = tail?.apply {
        decreaseSize {
            tail = tail?.prev
            tail?.next = null
        }
    }?.value

    private fun findNode(index: Int): Node<E>? {
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

    private inline fun <R> decreaseSize(block: () -> R): R = block().also { size-- }

    internal class Node<E : Any>(
        val value: E,
        var prev: Node<E>? = null,
        var next: Node<E>? = null,
    ) {
        override fun toString(): String = "${value}, n=${next?.value}, p=${prev?.value}"
    }
}

fun main() {
    val list = DoublySimpleLinkedList<Int>()
    list.apply {
        debugPrint { addAtHead(1) }
        debugPrint { addAtTail(3) }
        debugPrint { addAtIndex(1, 2) }
        get(1)
        debugPrint { removeAtIndex(1) }
        get(1)
        debugPrint { removeHead() }
        debugPrint { addAtTail(80) }
        debugPrint { addAtHead(90) }
        debugPrint { removeTail() }
    }
}

private fun DoublySimpleLinkedList<Int>.debugPrint(action: () -> Unit) {
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
