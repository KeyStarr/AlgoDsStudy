package com.keystarr.algorithm.linkedlist

// https://leetcode.com/problems/middle-of-the-linked-list/description/
// constraints:
//  - no explicit time
//  - no explicit memory
//  - input size < 100 nodes
class MiddleOfLinkedList {

    // intuition: simply count the size of the list => calculate the second middle index, and then do sequential search
    // time: O(n), space: O(1)
    fun slow(head: ListNode?): ListNode? {
        if (head == null) return null

        val size = head.countSize()
        return head.findNode(index = size / 2)
    }

    private fun ListNode.countSize(currentSize: Int = 1): Int =
        next?.let { next?.countSize(currentSize + 1) } ?: currentSize

    private fun ListNode.findNode(index: Int, currentIndex: Int = 0): ListNode? =
        if (currentIndex == index) this else next?.findNode(index, currentIndex + 1)

    fun fast(head: ListNode?): ListNode? {
        // takes from a leetcode contributor. TODO: HOW DOES IT WORK????
        var p1 = head
        var p2 = head
        while (p2 != null && p2.next != null) {
            p1 = p1?.next // f(x) = x + 1
            p2 = p2?.next?.next // f(x) = x + 2
            println("p1 = $p1, p2 = $p2")
        }
        return p1
    }
}

fun main() {
    val input = linkedListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
    val result = MiddleOfLinkedList().fast(input)
    println(result?.formatToEnd())
}

fun linkedListOf(vararg nums: Int): ListNode? {
    if (nums.isEmpty()) return null
    return ListNode(nums[0]).apply {
        var currentNode = this
        (1 until nums.size).forEach { ind ->
            currentNode.next = ListNode(nums[ind])
            currentNode = currentNode.next!!
        }
    }
}

fun ListNode.formatToEnd(output: StringBuilder = StringBuilder()): String {
    output.append("$value, ")
    return next?.formatToEnd(output) ?: output.toString()
}

class ListNode(
    var value: Int,
    var next: ListNode? = null
) {

    override fun toString() = value.toString()
}
