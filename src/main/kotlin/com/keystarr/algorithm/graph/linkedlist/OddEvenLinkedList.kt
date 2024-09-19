package com.keystarr.algorithm.graph.linkedlist

import com.keystarr.datastructure.graph.linkedlist.ListNode
import com.keystarr.datastructure.graph.linkedlist.singlyLinkedListOf

/**
 * LC-328 https://leetcode.com/problems/odd-even-linked-list/description/
 * difficulty: medium
 * constraints:
 *  • 0 <= number of nodes <= 10^4;
 *  • 10^6 <= node.val <= 10^6.
 *
 * Final notes:
 *  • 🏆 done [efficient] by myself in 22 mins. Got stuck on debugging a silly error for like 5-7 mins :D Though I had to preserve
 *   the next and use the temp pointer pattern but turned out I didn't need to.
 *  • 🔥 dry running helped a lot here, I assumed we'd use multiple pointers since its O(n) time single pass and O(1) space
 *   and tried to emulate by reason what the steps should be via dry running. These leetcode final videos of mock interviews
 *   and the author reasoning were instrumental to understand how clean and easy dry runs can be done in code editors on the input data!
 *   Never though to do it that way! (drew mostly).
 *
 * Value gained:
 *  • practiced solving a singly LinkedList modification type problem using multiple pointers (core = two pairs of 2 pointers).
 */
class OddEvenLinkedList {

    /**
     * the usual - O(n) time and O(1) space straight off the bat, huh.
     *
     * we could have two pointers - one at an odd node, another at an even node.
     *
     *  1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7
     *
     *  odd=3 even=4
     *
     *  processNode = 3
     *
     *  lastOdd=1
     *  lastEven=2
     *
     *  attach even to lastEven
     *  attach odd to lastOdd
     *   => 1 -> 3 -> 2 -> 4     5 -> 6 -> 7
     *   processNode=5
     *
     *   odd=processNode=5
     *   even=odd.next=6
     *
     *   odd = even.next.next = 5
     *   even = odd.next = 6
     *
     *
     * So the approach: find pointers to the first odd and even nodes, then go through all nodes in the list via 2 odd and even pointers
     *   => attach all even nodes to the even list and odd nodes to the odd list => merge the two together in the end.
     *
     *
     * Cases - total number of nodes in the list is:
     *  - even =>
     *  - odd =>
     *
     * Edge cases:
     *  - number of nodes < 3 => always return head => do an early return just in case.
     *
     * Time: always O(n)
     * Space: always O(1)
     *
     * ----------
     *
     * could be simplified with less pointers, but not by much, and I like the readability of the current solution so leave as-is
     */
    fun efficient(head: ListNode?): ListNode? {
        if (head?.next?.next == null) return head

        val firstEven = head.next
        var lastOdd = head
        var lastEven = firstEven

        var odd = lastEven?.next
        var even = odd?.next

        while (odd != null) {
            // attach new odd and even nodes to their respective lists
            lastOdd?.next = odd
            lastEven?.next = even

            // update odd/even lists end pointers
            lastOdd = odd
            lastEven = even

            // update pointers to next new odd/even nodes
            odd = lastEven?.next
            even = odd?.next
        }

        // merge two lists together
        lastOdd?.next = firstEven
        lastEven?.next = null

        return head
    }
}

fun main() {
    OddEvenLinkedList().efficient(
        singlyLinkedListOf(1, 2, 3, 4, 5)
    ).debugPrintContents()
}
