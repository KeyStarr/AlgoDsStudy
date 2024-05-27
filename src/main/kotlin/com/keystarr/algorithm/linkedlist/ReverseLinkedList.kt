package com.keystarr.algorithm.linkedlist

/**
 * LC-206 https://leetcode.com/problems/reverse-linked-list/description/
 * difficulty: easy
 * constraints:
 *  • 0 <= the number of nodes <= 5000;
 *  • -5000 <= Node.val <= 5000.
 *
 * Value gained:
 *  • soooo funny, I've solved this very problem 1.5 months ago [com.keystarr.algorithm.divideandconquer.ReverseLinkedList]
 *   BUT I completely forgot the elegant [recursive] AND the elegant [iterativeCleaner] solutions!!!
 *  • I EVEN STRUGGLED TO IMPLEMENT THE [iterative] FOR, LIKE, AN HOUR! Made a break and only then came up with that
 *   ugly version (reached a point first when I couldn't understand what was going on and felt a strong itch to start
 *   submitting after random changes (always a screaming sign to stop, never do that!));
 *  • came up with [iterativeCleaner] ONLY after I've looked into [com.keystarr.algorithm.divideandconquer.ReverseLinkedList],
 *   and actually implemented [recursive] as elegant straight away only because I've accidentally cheated and peeked at the
 *   previous solution in [com.keystarr.algorithm.divideandconquer.ReverseLinkedList];
 *  • key value from the experience:
 *      • after all, DO ANKI BASED SPACED REPETITION FOR KEY ALGORITHMS?!?!?!?!? Including re-solving them every X months?!??!?!
 *
 *  • didn't see it back then, but now I do: [iterativeCleaner] actually makes use of 2-pointers fast&slow which is so common
 *      for elegant LinkedList interview problem solutions!
 *  • now that I've learnt loop invariant at least on the basic level I was actually able to prove both [iterative] and
 *   [iterativeCleaner] solutions => FIX A FEW BUGS BEFORE EVEN RUNNING and be 99.8% confident about the solution. Cool,
 *   learning this fundamental technique sorta is paying off??? I LOVED the feeling of that I'm in control, precisely know
 *   what I'm doing.
 */
class ReverseLinkedList {

    /**
     * Tools: D&C
     *
     * Idea:
     *  - base case: current.next == null => return current (the original tail => new head);
     *  - recursive case:
     *      - divide: `reverse(current.next)`, that is, exclude the current node from the problem set;
     *      - conquer: since we've passed the base case, then current node is not the oldtail/newhead => current.next != null
     *          => we can reverse the link between the current node and next one: current.next.next=current, that is,
     *          reverse linking for 2 adjacent nodes (and do that for each 2 adjacent nodes);
     *      - combine: propagate the newHead down the entire callstack and return it, cause that's the answer.
     * - fix: oldHead.next=null, cause since oldHead had no node before it, the conquer step was not done for it and it
     *  still references the old next node.
     *
     * Edge cases:
     *  - oldHead.next=null.
     *
     * Time: O(n)
     * Space: O(1), though the const is impacted by the callstack.
     */
    fun recursive(head: ListNode?): ListNode? = recursiveInternal(head).also { head?.next = null }

    private fun recursiveInternal(current: ListNode?): ListNode? {
        if (current?.next == null) return current

        val newHead = recursiveInternal(current.next)!!

        // reverse the link between the current 2 adjacent nodes
        val newPrevious = current.next!!
        newPrevious.next = current

        // propagate the newHead (oldTail) down the callstack, cause that's the answer
        return newHead
    }

    /**
     * Idea:
     *  - init:
     *      - currentNode=head
     *      - originalNext=head.next
     *      - head.next=null (!! as if we started from the node before head, to preserve the loop invariant)
     *  - head.next=null
     *  - while originalNext != null (after the tail):
     *      - newPrev=originalNext
     *      - originalNext=originalNext.next
     *      - reverse the link between newPrev and currentNode
     *      - currentNode=newPrev
     *  - return currentNode (oldTail=newHead)
     *
     * [1 2 3 4]
     *
     * loop invariant: all nodes before currentNode have reversed links (note: head.next=null as a special case of reversing)
     *
     * Edge cases:
     *  - head=null => return null, correct;
     *  - a single node (head!=null && head.next=null) => return head, correct;
     *  - (not really an edge case, but to prove by induction sorta) two nodes => correct.
     *
     * Time: always O(n)
     * Space: always O(1)
     */
    fun iterative(head: ListNode?): ListNode? {
        var currentNode = head
        var originalNext = head?.next

        head?.next = null

        while (originalNext != null) {
            val newPrev = originalNext
            originalNext = originalNext.next

            newPrev.next = currentNode
            currentNode = newPrev
        }

        return currentNode
    }

    /**
     * Core idea same as [iterative], same complexities, but cleaner. Instead of pointing at head and next, point at
     * previous=null and current=head at the start, and then make a single node step forward for both pointers from there.
     *
     * Tools: funny, 2 pointers again! Slow=previous, fast=current!!
     *
     * Loop invariant: all nodes before current have reversed links
     *  - init: no nodes, trivially correct;
     *  - maintenance:
     *      - if list.size>1 =>
     *          - for 1st iteration we reverse the link for the 1st node: 1st.next=null, and move currentNode=2nd;
     *          - at the start of the 2nd iteration currentNode=2nd node and there's one node before the 2nd, which
     *              has its link reversed => correct;
     *          - during the 2nd iteration we reverse link for the 2nd node: 2nd.next=1st, and move currentNode=3rd;
     *          - at the start of the 3rd iteration there are 2 nodes (1st and 2nd) and both have reversed links;
     *          => all subsequent iterations follow the same pattern, proven correct by induction;
     *  - termination:
     *      - if list.size==0 => there were no iterations, return null straight away;
     *      - if list.size>0 => we stop when current=null, which is guaranteed to happen when previous=tail if there
     *          are no cycle in the list; then, since we've reversed the link for tail at the previous iteration (since
     *          we've moved to currentNode=tail.next=null) => all nodes in the list have reversed links => correct.
     * Proven.
     *
     * Time: O(n)
     * Space: O(1)
     */
    fun iterativeCleaner(head: ListNode?): ListNode? {
        var previous: ListNode? = null
        var current = head
        while (current != null) {
            val originalNext = current.next
            current.next = previous
            previous = current
            current = originalNext
        }
        return previous
    }
}

fun main() {
    ReverseLinkedList().iterative(
        ListNode(
            value = 1,
            next = ListNode(
                value = 2,
                next = ListNode(
                    value = 3,
                    next = ListNode(
                        value = 4,
                        next = null,
                    )
                )
            )
        )
    ).debugPrintContents()
}
