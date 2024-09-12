package com.keystarr.algorithm.graph.linkedlist

import com.keystarr.datastructure.graph.linkedlist.ListNode
import com.keystarr.datastructure.graph.linkedlist.singlyLinkedListOf

/**
 * LC-2074 https://leetcode.com/problems/reverse-nodes-in-even-length-groups/description/
 * difficulty: medium (100% leet-hard)
 * constraints:
 *  • 1 <= number of nodes <= 10^5;
 *  • 0 <= node.val <= 10^5.
 *
 * Final notes:
 *  • done [wrongInPlace] by myself in about 30 mins and only after a failed submission realized
 *   ⚠️ that I misinterpreted the goal. That last group reversal based solely on whether its even or not condition
 *   complicates things tenfold;
 *  • skimmed all the hints, didn't quickly understand what these mean exactly;
 *  • check discussion, went for [withTempList] after learned that many other folks struggled as well with the in-place solution.
 *   I do agree the O(1) space here is leet-hard, and hards are not our focus right now;
 *  • ⚠️ done [withTempList] at the 1h25m mark in total;
 *  • still, I don't feel it makes sense to schedule this problem for later resolve, since it is out of the current practice
 *   scope => just mark it as done and to later use it for the hard practice, add to that list.
 *
 * Value gained:
 *  • practiced solving a LinkedList leet-hard question with O(n) time but O(n) space, left O(1) space solution for the future session.
 */
class ReverseNodesInEvenLengthGroups {

    // TODO: try to do the O(n) time O(1) space solution. Postponed cause its clearly more of a leet-hard, and now
    //  the focus is solely on medium interleaving practice
    //  https://leetcode.com/problems/reverse-nodes-in-even-length-groups/solutions/1577372/constant-memory/

    /**
     *
     * Same idea as [wrongInPlace] but fix these edge cases for the last group - if current group is the last, then
     *  whether we reverse it or not is determined solely by its size; otherwise by its number.
     *
     * To implement that we always remember last group's end node and save all nodes of the current group into a list,
     *  and decide whether to reverse it or not only when we learn whether its the last group or not.
     *
     * Time: always O(n)
     *  - outer loop visit each node exactly once;
     *  - inner reverse loop visit some nodes, but each that it does exactly once => only attributes to a const;
     *  => each node is visited either 1 or 2 times.
     * Space: O(n)
     *  - size of the current group list depends on the amount of nodes in the list. We could probably come up with a more
     *   precise formula for the largest arithmetic sequence number for the total sequence sum of no greater than X,
     *   but it would be a const factor to n I presume => just estimate the space to be O(n).
     */
    fun withTempList(head: ListNode): ListNode {
        var group = 1
        var inGroup = 0

        var currentNode: ListNode? = head
        var prevNode: ListNode?

        var lastGroupEnd = currentNode
        val currentGroup = mutableListOf<ListNode>()
        while (currentNode != null) {
            currentGroup.add(currentNode)

            prevNode = currentNode
            currentNode = prevNode.next

            inGroup++
            if (inGroup == group || currentNode == null) {
                inGroup = 0
                group++
                if ((currentNode == null && currentGroup.size % 2 == 0) || (currentNode != null && group % 2 == 1)) {
                    lastGroupEnd?.next = currentGroup.last()
                    currentGroup.first().next = currentNode
                    for (i in 1..currentGroup.lastIndex) currentGroup[i].next = currentGroup[i - 1]
                    lastGroupEnd = currentGroup.first()
                } else {
                    lastGroupEnd = prevNode
                }
                currentGroup.clear()
            }
        }

        return head
    }

    /**
     *
     * ⚠️ WRONG! Actual goal: reverse each EVEN-LENGTH group!
     *  - the last group's number may be odd in order but its LENGTH MAY BE EVEN;
     *  - the last group's number may be even in order, but its LENGTH MAY BE ODD.
     *
     * ----
     *
     * problem rephrase:
     *  - given:
     *   - a head of a special linked list. All nodes form groups which start from the first node, the length of each
     *    subsequent group equal to the length of the previous one + 1 (lengths = an arithmetic sequence with step = 1),
     *    and the 1st groups length = 1;
     *   - note: last group may have fewer elements than its number in the sequence.
     *  - goal: reverse each EVEN group in the list individually, return the head of the modified list.
     *
     *
     *
     * Observe: since we reverse groups individually and the 1st group always has 1 element => head of the LL never changes.
     *
     * Approach - reverse all nodes only in EVEN groups as we go along, respecting the group connections and last group's
     *  special size constraint (note: 1st group is odd, count from 1)
     *
     * Caution: last group may be an even number group
     *
     *
     * input: 1 -> 2 -> 3 -> 4
     * live mod: 1 -> 3 -> 2 -> 4
     * expected: 1 -> 3 -> 2 -> 4
     *
     * edge cases:
     *  - amount of nodes == 1 =>
     *  - last group has 0 nodes =>
     *  - last group in order is even, but its size is odd =>
     *   how can we learn in advance that the current group is last AND its size?
     *   I can see only if we simply iterate forward and save each node into a temp list, then make a decision to reverse
     *   that list or not base on it being the even group OR the final group of even size
     *
     *  - last group in order is odd, but its size is even =>
     *
     * Time:
     * Space:
     */
    fun wrongInPlace(head: ListNode): ListNode {
        var currentNode: ListNode? = head
        var group = 1
        var inGroup = 0

        var oddGroupEnd = currentNode
        var newEvenGroupEnd = currentNode
        var prevNode: ListNode? = null
        while (currentNode != null) {
            if (group % 2 == 0) {
                val originalNext = currentNode.next
                currentNode.next = prevNode
                prevNode = currentNode
                currentNode = originalNext
            } else {
                prevNode = currentNode
                currentNode = prevNode.next
            }

            inGroup++
            if (inGroup == group || currentNode == null) {
                inGroup = 0
                group++
                if (group % 2 == 0) {
                    oddGroupEnd = prevNode
                    newEvenGroupEnd = currentNode
                } else {
                    newEvenGroupEnd?.next = currentNode
                    oddGroupEnd?.next = prevNode
                }
            }
        }

        return head
    }
}

fun main() {
    ReverseNodesInEvenLengthGroups().withTempList(
        head = singlyLinkedListOf(4, 3, 0, 5, 1, 2, 7, 8, 6)!!,
    ).debugPrintContents()
}
