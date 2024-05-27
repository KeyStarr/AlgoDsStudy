package com.keystarr.algorithm.linkedlist

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
    fun solution(head: ListNode): ListNode {
        var slow = head
        var fast = head
        while (fast.next?.next != null) {
            slow = slow.next!!
            fast = fast.next!!.next!!
        }
        return if (fast.next == null) slow else slow.next!!
    }
}
