package com.keystarr.algorithm.graph.linkedlist

/**
 * ⭐️ an excellent linked list two pointers technique flexibility example
 * LC-19 https://leetcode.com/problems/remove-nth-node-from-end-of-list/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= k <= number of nodes <= 30;
 *  • 0 <= node.value <= 100.
 *
 * Final notes;
 *  • ⚠️ failed to produce the clean solution by myself in 30 mins. Went for two pointers, but got stuck on `fast.next.next`
 *   and didn't consider other initial distance / step possibilities => missed the solution;
 *  • it seems that counting the amount of nodes + adding nodes into some temp array are a strict no-no for linked list
 *   problems from what I've seen so far => should've stopped early at design stage in [dirty] and rejected it;
 *  • interesting how [MiddleOfTheLinkedList] is a special case of finding the kth element from the end problem,
 *   there the target (if 2 middles find the 1st) is (n/2+1)th element from the end, but the algorithm required is significantly
 *   different, since there we don't have the k!
 *
 * Value gained:
 *  • learned that in LinkedList problems for a clean one pass O(1) space solution two pointers may require have custom
 *   init logic, and custom exact step logic (like, fast being 3x of the slow, or just 1 step ahead but inited at X steps ahead
 *   like here etc);
 *  • practiced solving a LinkedList problem cleanly with 2 pointers.
 */
class RemoveNthNodeFromEndOfList {

    // TODO: failed to produce the clean solution, retry in 1-2 weeks

    /**
     * Core idea - if we always keep the distance of [k] between slow and fast pointers, and advance them by 1 each step,
     *  then when faster will be outa bounds, slower will be exactly the kth element from the end (cause its always the
     *  kth element from the fast, and fast is then the end).
     *
     * Alteration: since we are removing => actually find the k+1th element from the end. For that stop when the fast pointer's
     *  next is the end, meaning that fast is the element before end => slow pointer is the k+1th element from the end,
     *  exactly what we needed.
     *
     * Edge cases:
     *  - k and head is such that we need to remove the current head => only then after initing fast == null => return head.next.
     *  special case is 1 element in the list, i.o. head.next == null => return head.next, which is null, correct.
     */
    fun clean(head: LinkedListNode, k: Int): LinkedListNode? {
        var slow = head
        var fast: LinkedListNode? = head
        repeat(k) { fast = fast?.next }

        if (fast == null) return head.next
        while (fast?.next != null) {
            slow = slow.next!!
            fast = fast?.next
        }

        slow.next = slow.next?.next
        return head
    }

    /**
     * given: SINGLY linked list.
     *
     * trivial solution:
     * - 1 pass: count the number of nodes in the list, say, via a fast pointer (n/2 iterations);
     * - 2 pass: find the (n-k)th node, delete it.
     *
     * follow-up - can we do in 1 pass?
     *
     *  cases of kth element AND fast&slow:
     *
     *   1. in the second half:
     *    if we do fast&slow pointers, fast will reach the end, and we'll get the exact amount of iterations needed
     *    to reach the kth node from the end from the slow pointer, which would be at n/2.
     *    ✅ one pass
     *
     *   2. in the first half:
     *    if we did fast&slow, as fast reached end we know then that we've already passed the target element.
     *    we can't get back to it, since the list is singly linked
     *    => only break the loop and do the 2nd pass, find it.
     *    ❌ one pass => 2 pass
     *
     *   3. exactly the n/2th element:
     *    we'd learn that as the fast pointer reached the end => slow points exactly at the target
     *    perhaps, keep prevSlow in order to delete it then without the 2nd pass.
     *    ✅ one pass
     *
     * can we improve case #2?
     *  save all nodes that slow passes into a dynamic array => space complexity grows up to n/2, but we'll be able to get
     *   the (n-k)th node in O(1) time as fast reached end.
     *
     * is space complexity growth OK? no one to ask, no such constraints on the problem itself, but looks weird for
     * a linked list problem => ah, try it anyway for efficiency.
     *
     * edge cases:
     *  - 1 element in the list, head.next == null => early return null
     *  - k == number of nodes, first node => remove the first node, return head.next then! => early return in the 2nd half head.next;
     *  - k==1, last node => correct as-is.
     *
     * Time: always O(n), each element is visited at most twice (only for those touched with the fast pointer)
     * Space: average/worst O(n)
     *  worst slowVisitedNodes size is n/2
     *
     * ----------
     *
     * obviously overcomplicated, there gotta be a clean one pass concept
     */
    fun dirty(head: LinkedListNode?, k: Int): LinkedListNode? {
        if (head?.next == null) return null

        var slow = head
        var fast = head.next
        val slowVisitedNodes = mutableListOf<LinkedListNode>().apply { add(slow!!) }
        var nodesAmount = 1
        while (fast != null) {
            slow = slow?.next
            nodesAmount += if (fast.next == null) 1 else 2
            fast = fast.next?.next
            slow?.let(slowVisitedNodes::add)
        }

        val targetInd = nodesAmount - k
        if (targetInd == 0) return head.next

        if (targetInd <= slowVisitedNodes.size - 1) {
            slowVisitedNodes[targetInd - 1].next = slowVisitedNodes[targetInd].next
            return head
        }

        var currentInd = nodesAmount / 2
        var prevSlow = slow
        while (currentInd != targetInd) {
            prevSlow = slow
            slow = slow?.next
            currentInd++
        }
        prevSlow?.next = slow?.next
        return head
    }
}

fun main() {
    println(
        RemoveNthNodeFromEndOfList().dirty(
            head = LinkedListNode(
                `val` = 1,
                next = LinkedListNode(
                    `val` = 2,
                    next = LinkedListNode(
                        `val` = 3,
                        next = LinkedListNode(
                            `val` = 4,
                            next = LinkedListNode(
                                `val` = 5,
                            )
                        )
                    )
                )
            ),
            k = 2,
        )
    )
}
