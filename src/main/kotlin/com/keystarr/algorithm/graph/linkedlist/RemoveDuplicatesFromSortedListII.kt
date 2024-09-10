package com.keystarr.algorithm.graph.linkedlist

import com.keystarr.datastructure.graph.linkedlist.ListNode

/**
 * LC-82 https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/description/
 * difficulty: medium
 * constraints:
 *  • 0 <= number of nodes <= 300;
 *  • -100 <= node.val <= 100;
 *  • the list is sorted ascending by node.val.
 *
 * Final notes:
 *  • 🏆🔥(been struggling with LinkedLists for the one pass lately) done [efficient] by myself in 15 mins;
 *  • made all key design choices by myself to achieve the common 1 pass O(n) time O(1) space clean efficient solution for a LinkedList:
 *   1. noted that the "list is sorted" property sticks out => tried to reason, jump from this: if list is sorted, how can we determine
 *    that the current/next node are duplicates? Turns out its just that all duplicates are adjacent then;
 *   2. realized we need to "scroll through" the duplicates cause we can have many, until we have none;
 *   3. observed that since any node can have a duplicate => we may delete the head => "hook" triggered, recalled that
 *    in case head may be deleted, a good tool to use is the "stub head" pattern to always have a ref for the new head.
 *
 * Value gained:
 *  • practiced solving a linked list modification type question efficiently using a one pass based on the problem's key
 *   property (sorted) and a stub head.
 */
class RemoveDuplicatesFromSortedListII {

    /**
     * "given the SORTED linked list" => probably the sorted property is crucial for the optimal solution here
     *
     * goal = delete ALL nodes with non-distinct values from the original list
     *
     * Since the list is sorted, all duplicate valued nodes are adjacent => if we're at the head, and the next node has
     *  a value equal to head => simply go to the next node, if it does too => go to next. Scroll through the nodes until
     *  either we have a null (end of list) or a node that is not equal to head.value.
     *
     * above is WRONG - we have need to delete ALL duplicate valued nodes
     * but we can adapt it => either keep track of prevNode for the head's parent to delete head as well, or
     *  check head.next.next with head.next, and if its a duplicate, scroll through and later delete head next as well.
     *
     * Edge cases:
     *  - head is a duplicate node => delete it, return the new head (potentially null if list is empty) => use a stub head pattern to simplify the logic.
     *
     * Time: always O(n), we'll visit each node exactly once
     *  - up to n iterations for the main loop
     *  - up to n iterations for the inner loop
     *  across all outer and inner iterations we'll visit all nodes
     * Space: always O(1)
     */
    fun efficient(head: ListNode?): ListNode? {
        val stubHead = ListNode(`val` = -1, next = head)
        var prevNode = stubHead
        var currentNode = head
        while (currentNode != null) {
            val wasDuplicate = currentNode.isDuplicate()
            while (currentNode.isDuplicate()) currentNode.next = currentNode.next?.next
            // at this time, currentNode.next's value is either == null or a value != currentNode.val
            if (wasDuplicate) {
                prevNode.next = currentNode.next // delete the current node too => prevNode doesn't change
            } else {
                prevNode = currentNode // keep the currentNode since it is distinct in the original list => move prevNode pointer to it
            }
            currentNode = currentNode.next
        }
        return stubHead.next // since we'd never delete the stubHead and always start from it as the first node => it's next is guaranteed to be the new head
    }

    private fun ListNode.isDuplicate() = next?.`val` == `val`
}
