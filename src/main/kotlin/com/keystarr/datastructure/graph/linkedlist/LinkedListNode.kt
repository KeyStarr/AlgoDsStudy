package com.keystarr.datastructure.graph.linkedlist

/**
 * A copy of the Leet's LinkedList node model. TODO: re-do in Java, cause theirs is Java? (e.g. to get rid of named args, which Java obviously doesn't support)
 */
class LinkedListNode(
    var `val`: Int,
    var next: LinkedListNode? = null
) {

    override fun toString() = "$`val`, n=${next?.`val`}"
}

/**
 * In Leet's environment that's the name => minimize the impact of that ambiguous name in our big project yet comply with
 *  Leet's environment to reduce the code modification efforts.
 */
typealias ListNode = LinkedListNode

fun LinkedListNode.formatToEnd(output: StringBuilder = StringBuilder()): String {
    output.append("$`val`, ")
    return next?.formatToEnd(output) ?: output.toString()
}

fun singlyLinkedListOf(vararg nums: Int): LinkedListNode? {
    if (nums.isEmpty()) return null
    return LinkedListNode(nums[0]).apply {
        var currentNode = this
        (1 until nums.size).forEach { ind ->
            currentNode.next = LinkedListNode(nums[ind])
            currentNode = currentNode.next!!
        }
    }
}