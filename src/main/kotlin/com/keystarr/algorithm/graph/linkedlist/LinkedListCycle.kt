package com.keystarr.algorithm.graph.linkedlist

/**
 * LC-141 https://leetcode.com/problems/linked-list-cycle/description/
 * difficulty: easy
 * constraints:
 *  • 0 <= nodes amount <= 10^4;
 *  • -10^5 <= Node.val <= 10^5;
 *  • no explicit time/space.
 *
 * value gained:
 *  • Floyd's cycle detection algo, huh. One really wouldn't be able to solve these problems in a reasonable amount of time
 *      on the spot without knowing these patterns though, indeed;
 *  • very slick and elegant [efficient]! still would like to prove it though, but empirical evidence and the claim
 *      will be OK for now.
 */
class LinkedListCycle {

    /**
     * Problem rephrase:
     * "return true if there is a node in the list which `next` field refers to a node, reachable via it's `previous` field"
     *
     * brute force:
     * - iterate through nodes:
     *  - check if the node is in the hashset => if it is, return true;
     *  - save each visited node's reference into the HashSet;
     * - if reached the last node => return false.
     *
     * edge cases:
     *  - head=null or head.next=null => return false;
     *  - tail.next=null => return false;
     *  - head.next=head => return false.
     *
     *  3 -> 2 -> 5 -> 2
     *
     *  iteration to set contents:
     *  iteration 0: 3
     *  iteration 1: 3 2
     *  iteration 2: 3 2 5, 2 found => return false
     *
     * Time: O(n)
     * Space: O(n)
     */
    fun suboptimal(head: ListNode?): Boolean {
        val set = mutableSetOf<ListNode>()
        var currentNode = head
        while (currentNode != null && currentNode.next != null) {
            set.add(currentNode)
            if (set.contains(currentNode.next)) return true
            currentNode = currentNode.next
        }
        return false
    }

    /**
     * LinkedList => probably an elegant trick => try two pointers slow/fast
     *
     * Idea:
     * - iterate through the linked list:
     *  - use two pointers: slow=slow.next and fast=fast.next.next;
     *  - if fast reaches the end of the list, that is, fast.next.next=null => return false;
     *  - if fast==slow, and it is not the first iteration, then we've run into a loop => return true.
     *
     * If there's a cycle, then eventually fast and slow will always meet. Though I don't understand rn how is that,
     * couldn't come up with a quick proof.
     *
     * Turns out it's the Floyd's cycle detection algorithm:
     * https://en.wikipedia.org/wiki/Cycle_detection
     *
     * Time: O(n) ???
     * Space: O(1)
     *
     * Discovered thanks to the Leetcode DSA course.
     *
     * TODO: prove and come up and really understand the formula of detection, after how many iterations will slow and fast meet.
     */
    fun efficient(head: ListNode?): Boolean {
        var fast = head?.next?.next
        var slow = head?.next
        while (fast?.next?.next != null) {
            if (fast == slow) return true
            fast = fast.next?.next
            slow = slow?.next
        }
        return false
    }
}

fun main() {
    val fourth = ListNode(
        value = -4,
        next = null,
    )
    val third = ListNode(
        value = 0,
        next = fourth,
    )
    val second = ListNode(
        value = 2,
        next = third,
    )
    val first = ListNode(
        value = 3,
        next = second,
    )
//    fourth.next = second
    println(LinkedListCycle().suboptimal(first))
}
