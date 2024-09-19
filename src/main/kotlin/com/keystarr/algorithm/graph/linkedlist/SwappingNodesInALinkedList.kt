package com.keystarr.algorithm.graph.linkedlist

import com.keystarr.datastructure.graph.linkedlist.ListNode
import com.keystarr.datastructure.graph.linkedlist.singlyLinkedListOf

/**
 * ⭐️ a great example of a "swap nodes in the singly LL" type problem to showcase just WHY swapping VALUES instead of the
 *  actual nodes is often SO MUCH EASIER.
 *
 * LC-1721 https://leetcode.com/problems/swapping-nodes-in-a-linked-list/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= k <= number of nodes <= 10^5;
 *  • 0 <= node.val <= 100.
 *
 * Final notes:
 *  • ⚠️ missed the part of the problem statement that went "..after swapping THE VALUES.." and swapped the nodes. Well,
 *   the problem's title was about the, khm, nodes and not their values + ⚠️ I FORGOT that in LinkedList swap problems
 *   its sometimes allowed to swap the values not nodes WHICH IS TREMENDOUSLY easier!
 *  • 💡 swapping nodes themselves [efficientSwapNodes] turned out to be a hassle for a couple of reasons:
 *   1. we have to find parents not the nodes themselves => not too bad, but still a slight overhead on the search algo
 *    and a significantly greater variables;
 *   2. the worst: edge cases. EDGE CASES. Like here with swap "kth nodes from start and end" specifically.
 *    first.next === second, second.next==first BOOM goes the trivial swap LL nodes algorithm)))
 *    look at all the conditions in the swap part, I spent like 40 mins obsessively (yea, counterproductive I know) trying
 *    to understand how to handle these edges and finally got it right BUT I still am not that confident on the internals
 *    of these edges, how it works 🙈
 *
 *  • both efficient solutions are basically based on the classic algorithm "find the kth node from the end of the list"
 *   via 2 pointers from [RemoveNthNodeFromEndOfList];
 *
 *  • for the future: when tasked with swapping nodes in a LinkedList ALWAYS ASK IF SWAPPING JUST THE VALUES IS OK!!!!
 *   its MUCH MUCH easier usually.
 *
 * Value gained:
 *  • practiced solving a "swap nodes in the singly LinkedList" type question by both swapping the nodes and values,
 *   using multiple pointers (with fast and slow for finding kth the end);
 *  • 🔥 learned to always consider swapping values in the singly LL first, at least ask if thats ok, since its generally
 *   much easier.
 */
class SwappingNodesInALinkedList {

    // TODO: full resolve after 1-3 weeks

    /**
     * simply find the first node, the second node and swap their values, that's it
     *
     * use a slightly modified algo from the [efficientSwapNodes] for finding both target nodes
     */
    fun efficientSwapValues(head: ListNode?, k: Int): ListNode? {
        // find first target node
        var fast: ListNode? = head
        repeat(k - 1) { fast = fast?.next }
        val firstTarget = fast!!

        // find second target node
        var slow = head
        while (fast?.next != null) {
            slow = slow?.next!!
            fast = fast?.next
        }
        val secondTarget = slow!!

        // swap values
        val temp = firstTarget.`val`
        firstTarget.`val` = secondTarget.`val`
        secondTarget.`val` = temp

        return head
    }

    /**
     * since its a singly LL -> probably the best is one-two pass solution O(n) time and O(1) space
     *
     * to find the kth node from the end we could use 2 pointers:
     *  - init slow = head, fast=scroll from head (k-1) times;
     *   actually the node we point fast to initially IS the kth node from the beginning => we may save both a reference
     *    to it and to its predecessor. Or just its predecessor actually
     *  - move slow and fast by 1 each step until fast == null => then slow is the kth pointer from the end;
     *   actually since we need to move the kth node, we should find the pointer to its parent
     *
     *   so actually scroll fast initially to (k-2) nodes.
     *
     *
     * major cases:
     *  - k < n/2 => kth node from the start is in the 1st half of the list and kth from the end in the 2nd:
     *   - scroll fast (k-2) times from the head to find the first target node's parent;
     *   - scroll fast to (k) times from the head, scroll slow and head until fast.next == null => we found the second target node,
     *
     *  - k > n/2 => we have a reverse situation, kth node from the start is in the 2nd half of the list, and kth node from the end
     *   is in the 1st half. The algorithm laid out above works, its just that the first node is then the one from the end,
     *   and second is from the start (reversed)
     *
     *   => since we need to just swap them, its ok.
     *
     *
     * edge cases:
     *  - k == 1 => set fast to head then, also use stub head? since the head would be changed in that case =>
     *   as-is:
     *    firstTargetParent = stubHead
     *    secondTargetParent = the prev node before the last one
     *    swap ok, new head stubHead.next = the second target
     *   => correct as-is;
     *
     *  - k == nodes amount => fixed, initialized slow to stubHead as well since then the 2nd (!) target node's parent is the  stub head;
     *
     *  - n % 2 == 1 && k == n/2 + 1 => don't swap the node => check for firstParent == secondParent early return head then?
     *   => correct as-is, since all swap algo operations wouldn't change anything, but add an early return for easier maintenance.
     *
     *  - firstTarget.next == secondTarget (n % 2 == 0 && k==n/2) =>
     *  - secondTarget.next == firstTarget (n % 2 == 0 && k==n/2) =>

     *  - nodes amount == 1 =>
     *  - nodes amount == 2 =>
     *
     ** --------------
     *
     *
     * -1 -> 1 -> 2 -> 3
     *
     * k=1
     * firstTargetParent=-1, correct
     *
     * slow=-1  fast = 2
     *
     *
     *
     * k=3
     *
     * fast=2
     *
     * slow=1 fast = null
     *
     *
     * 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7
     *
     * k=5
     *
     * slow = 1
     * fast (after scrolling for 5-2=3) = node 4 (target node is 5)
     *
     *
     *
     *
     */
    fun efficientSwapNodes(head: ListNode, k: Int): ListNode? {
        val stubHead = ListNode(-1, next = head)

        // find first target node
        var fast: ListNode? = stubHead
        repeat(k - 1) { fast = fast?.next }
        val firstTargetParent = fast!!

        // find second target node
        var slow = stubHead
        fast = fast?.next?.next
        while (fast != null) {
            slow = slow.next!!
            fast = fast?.next
        }
        val secondTargetParent = slow

        // swap them
        if (firstTargetParent === secondTargetParent) return head

        val firstTarget = firstTargetParent.next!!
        val secondTarget = secondTargetParent.next!!

        val secondOriginalNext = secondTarget.next
        firstTargetParent.next = if (secondOriginalNext == firstTarget) firstTarget.next else secondTarget
        secondTarget.next = if(firstTarget.next == secondTarget) firstTarget else firstTarget.next

        secondTargetParent.next = firstTarget
        firstTarget.next = if (secondOriginalNext == firstTarget) secondTarget else secondOriginalNext

        return stubHead.next
    }
}

fun main() {
    SwappingNodesInALinkedList().efficientSwapNodes(
        head = singlyLinkedListOf(1, 2)!!,
        k = 2,
    ).debugPrintContents()
}
