package com.keystarr.algorithm.graph.linkedlist

/**
 * LC-2095 https://leetcode.com/problems/delete-the-middle-node-of-a-linked-list/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= number of nodes <= 10^5;
 *  • 1 <= node.value <= 10^5.
 *
 * Final notes:
 *  • done [efficient] by myself in 20 mins;
 *  • for now solved OK, but could do much better with more linked lists practice:
 *   recognized the fast&slow pattern quickly, but spent excessive time applying it:
 *    forgot that we're supposed to start fast at head.next to get the N/2th node exactly in both odd/even cases;
 *  • ⚠️ missed THE ONLY edge case here D::: with 1 element in the list => head.next==null and head!=null. FOUND IT
 *   but reasoned wrong, decided its correct we return head, didnt put much thought. Actually we were supposed to delete it±!
 *   think about ur edge cases in depth man, SERIOUSLY dry run them each, throw the overconfidence away.
 *
 * Value gained:
 *  • practiced recognizing and solving a linked list middle node problem efficiently with fast&slow pointers pattern.
 */
class DeleteTheMiddleNodeOfALinkedList {

    // TODO: did OK, but could do faster => optionally retry shuffled in 1-2 weeks

    /**
     * goal: remove the n/2th node from the singly linked list
     *
     * design, 1 pass:
     *  1. find the n/2th middle node, and the node before it;
     *   use 2 pointers: fast & slow
     *   slow at the end is always the n/2th middle node
     *   => prevSlow is the node before it
     *  2. connect the previous one to it to the next one after the middle node.
     *
     * Edge cases:
     *  - number of nodes == 1 => delete the only node => slowPrev would still be null, incorrect => early return;
     *  - number of nodes == 2 => delete the second element, correct;
     *  - number of nodes == 3 => delete the second element, correct.
     *
     *  1 -> 2 -> 3 -> 4
     *
     * Time: always O(n)
     *  always exactly n/2 iterations
     * Space: always O(1)
     */
    fun efficient(head: LinkedListNode?): LinkedListNode? {
        if (head?.next == null) return null

        var slow = head
        var fast = head.next
        var slowPrev: LinkedListNode? = null
        while (fast != null) {
            slowPrev = slow
            slow = slow?.next
            fast = fast.next?.next
        }
        slowPrev?.next = slow?.next
        return head
    }
}
