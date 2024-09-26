package com.keystarr.algorithm.graph.linkedlist

import com.keystarr.datastructure.graph.linkedlist.ListNode
import com.keystarr.datastructure.graph.linkedlist.singlyLinkedListOf

/**
 * LC-2130 https://leetcode.com/problems/maximum-twin-sum-of-a-linked-list/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= number of nodes <= 10^5;
 *  • 1 <= node.val <= 10^5.
 *
 * Final notes:
 *  • unintentionally had a few hints at the start of this problem:
 *   1. earlier today checked which topics I still had left to solve bonus problems for and caught this problem's name in the
 *    "reverse LL" section => knew it was supposed to involve the reversal;
 *   2. a couple of days ago learned at the first time (or something) and solved a similar problem where we had to traverse
 *    the list by "twins" => reversed the 2nd part of the list as well, so had the context in short memory.
 *   => better resolve in some future just to make sure that I can do that in the wild
 *
 *  • 🏅 solved via [efficient] by myself in 24 mins - great result:
 *   • recognized the need for reversal straight away;
 *   • recognized that we need to find the second middle node to reverse;
 *   • recalled the efficient reversal algorithm correctly, and the finding the 2nd middle node, informally proved it
 *    on the spot via dry-running (not memorization but deep understanding and mastery I try to do here).
 *
 * Value gained:
 *  • practiced solving a "compute metric of the singly LL" type problem using standard tools:
 *   • find the middle node;
 *   • LL reversal;
 *   • traversal via 2 pointers.
 */
class MaximumTwinSumOfALinkedList {

    // TODO: optionally resolve in 2-4 weeks (cause of the unintentional hints)

    /**
     * the LL is always of even size - what does that imply?
     *  along with minNodesAmount == 2 => every node always has exactly 1 twin.
     *
     * singly LL problems are usually solved within O(n) time and O(1) space
     * => basically the O(n) time solution would involve iterating at the same time with 2 pointers one left-to-right
     *  another right-to-left converging in the middle
     *
     * BUT since the list is singly linked we can't traverse right-to-left without storing nodes in the memory (like a stack)
     *
     * BUT if we're allowed to modify the input we can reverse the second half of the list => init the first pointer
     * at the first node and the second and the second middle node => trivially move both by 1 and find the max sum.
     *
     * so, can we modify the input? no one to ask, assume so (if not - use a stack)
     *
     * approach:
     *  - find the second middle node;
     *  - reverse the second half of the LL using that;
     *  - traverse via 2 pointers and find the max twin sum.
     *
     * Edge cases:
     *  - sum => max sum = 10^5 * 2 => fits into Int.
     *
     * Time: always O(n)
     * Space: always O(1)
     *
     * 1 2
     * 1 2 3 4
     */
    fun efficient(head: ListNode): Int {
        val secondMiddleNode = findSecondMiddleNode(head)
        val lastNode = reverse(secondMiddleNode)
        return findMaxSumPair(firstHead = head, secondHead = lastNode)
    }

    /**
     * @param head - the LL must be of even size, at least 2.
     */
    private fun findSecondMiddleNode(head: ListNode): ListNode {
        var slow: ListNode = head
        var fast: ListNode? = head
        while (fast != null) {
            slow = slow.next!!
            fast = fast.next?.next
        }
        return slow
    }

    /**
     * @param head - the LL must be of size at least 1.
     */
    private fun reverse(head: ListNode): ListNode {
        var prev: ListNode? = null
        var current: ListNode? = head
        while (current != null) {
            val originalNext = current.next
            current.next = prev
            prev = current
            current = originalNext
        }
        return prev!!
    }

    /**
     * The linked lists [firstHead] and [secondHead] must be of equal length and of size at least 1.
     */
    private fun findMaxSumPair(firstHead: ListNode, secondHead: ListNode): Int {
        var firstNode: ListNode? = firstHead
        var secondNode: ListNode? = secondHead
        var maxSum = 0
        while (firstNode != null && secondNode != null) {
            val sum = firstNode.`val` + secondNode.`val`
            if (sum > maxSum) maxSum = sum
            firstNode = firstNode.next
            secondNode = secondNode.next
        }
        return maxSum
    }
}

fun main() {
    println(
        MaximumTwinSumOfALinkedList().efficient(
            head = singlyLinkedListOf(1, 2, 3, 20, 5, 8)!!,
        )
    )
}
