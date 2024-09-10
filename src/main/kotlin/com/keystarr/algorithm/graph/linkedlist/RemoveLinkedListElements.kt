package com.keystarr.algorithm.graph.linkedlist

import com.keystarr.datastructure.graph.linkedlist.LinkedListNode

/**
 * LC-203 https://leetcode.com/problems/remove-linked-list-elements/description/
 * difficulty: easy
 * constraints:
 *  • 0 <= number of nodes <= 10^4;
 *  • 1 <= node.value <= 50;
 *  • 0 <= targetValue <= 50.
 *
 * Final notes:
 *  • done [efficient] by myself in 10 mins.
 *
 * Value gained:
 *  • practiced solving a LinkedList modification type problem using a stubHead pattern;
 *  • 🏆consciously solved 1st LinkedList problem ever using the "stub head" pattern for the cleanest solution.
 */
class RemoveLinkedListElements {

    // TODO: repeat in 2 weeks

    /**
     * goal: remove all nodes that equal target from the LL and return the new head
     *
     * edge cases:
     *  - empty list => return null, correct as-is;
     *  - head.val == [targetValue] => remove head => special case, if at the end, correct;
     *  - all nodes val == [targetValue] => remove all nodes => we'd remove all nodes after head, then the head, correct;
     *  - there is no node with [targetValue] => no mods, return head => correct as-is.
     *
     * Time: always O(n)
     * Space: always O(1)
     */
    fun efficient(head: LinkedListNode?, targetValue: Int): LinkedListNode? {
        var currentNode = head
        while (currentNode?.next != null) {
            if (currentNode.next?.`val` == targetValue) {
                currentNode.next = currentNode.next?.next
            } else {
                currentNode = currentNode.next
            }
        }
        return if (head?.`val` == targetValue) head.next else head
    }

    /**
     * Same core algo as [efficient], but use the "stub head" pattern to get rid of the special logic regarding the head
     *  of the list, treat it just as any other node => the new head after transformations is always trivially the stubHead.next.
     *
     * Learned thanks to https://leetcode.com/problems/remove-linked-list-elements/solutions/158651/simple-python-solution-with-explanation-single-pointer-dummy-head/
     */
    fun efficientCleaner(head: LinkedListNode?, targetValue: Int): LinkedListNode? {
        val stubHead = LinkedListNode(`val` = -1, next = head)
        var currentNode: LinkedListNode? = stubHead
        while (currentNode?.next != null) {
            if (currentNode.next?.`val` == targetValue) {
                currentNode.next = currentNode.next?.next
            } else {
                currentNode = currentNode.next
            }
        }
        return stubHead.next
    }
}

fun main() {
    println(
        RemoveLinkedListElements().efficient(
            head = LinkedListNode(
                `val` = 7,
                next = LinkedListNode(
                    `val` = 7,
                    next = LinkedListNode(
                        `val` = 7,
                    )
                ),
            ),
            targetValue = 7,
        )
    )
}
