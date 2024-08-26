package com.keystarr.algorithm.graph.linkedlist

/**
 * LC-876 https://leetcode.com/problems/middle-of-the-linked-list/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= number of nodes <= 100;
 *  • 1 <= Node.value <= 100.
 *
 * Value gained:
 *  • internalized better and practiced a very new (to me) approach of "fast and slow" when applied to LinkedLists.
 */
class MiddleOfTheLinkedList {

    /**
     * Hint: SinglyLinkedList + find an element in the middle => Two Pointers, "Fast and slow".
     *
     * Idea:
     *  - create two pointers, fast=head and slow=head;
     *  - iterate until fast.next.next != null:
     *      - slow=slow.next;
     *      - fast=fast.next.next.
     *  - return if (fast.next != null) slow.next else slow
     *
     *  1 2 3
     *  1 2 3 4
     *
     * Edge cases:
     *  - number of nodes = 1 => return head, correct;
     *  - number of nodes = 2 (e.g. 100 33) => return the second (33), correct.
     *
     */
    fun twoPointers1(head: LinkedListNode): LinkedListNode {
        var slow = head
        var fast = head
        while (fast.next?.next != null) {
            slow = slow.next!!
            fast = fast.next!!.next!!
        }
        return if (fast.next == null) slow else slow.next!!
    }
}

fun LinkedListNode.formatToEnd(output: StringBuilder = StringBuilder()): String {
    output.append("$`val`, ")
    return next?.formatToEnd(output) ?: output.toString()
}

class LinkedListNode(
    var `val`: Int,
    var next: LinkedListNode? = null
) {

    override fun toString() = "$`val`, n=${next?.`val`}"
}

fun linkedListOf(vararg nums: Int): LinkedListNode? {
    if (nums.isEmpty()) return null
    return LinkedListNode(nums[0]).apply {
        var currentNode = this
        (1 until nums.size).forEach { ind ->
            currentNode.next = LinkedListNode(nums[ind])
            currentNode = currentNode.next!!
        }
    }
}
