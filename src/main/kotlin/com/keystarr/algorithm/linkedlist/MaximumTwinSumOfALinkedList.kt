package com.keystarr.algorithm.linkedlist

import kotlin.math.max

/**
 * LC-2130 https://leetcode.com/problems/maximum-twin-sum-of-a-linked-list/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= number of nodes <= 10^5, only even;
 *  • 1 <= Node.val <= 10^5;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • implemented [recursiveSuboptimal] by myself, discovered [iterativeEfficient] thanks to the leetcode DSA course explanation.
 *
 * Value gained:
 *  • interestingly enough, implemented [recursiveSuboptimal] sorta intuitively, skipped the usual procedure of designing in pseudocode
 *      first and then edge cases and complexities estimation, only then implementing. Instead, went straight onto
 *      implementation cause I had this "feeling" that I already know how it's gonna go, didn't even do a rigorous
 *      edge cases analysis => submitted successfully 1st try. Might it be useful to go like that sometimes during real
 *      interviews? (with meta-commentary why, ofc)
 *  • practiced combining multiple common linear list algos (finding the middle node + reversing the list) to achieve
 *      a solution within a single bigger algorithm. Is that dynamic programming??
 *  • practiced recursion on linear lists, here it is much more elegant than [iterativeEfficient] though less space efficient
 */
class MaximumTwinSumOfALinkedList {

    /**
     * goal rephrase: "return the maximum valid subset sum"
     * hint: the amount of nodes is always even.
     * hint: linked list, elegant solution => two pointer?
     *
     * intuition: traverse the list until the tail, then, when unwrapping the callstack, start in-parallel traversing
     *  the list from the head onward, and calculate the twin sums like current.value+recursive(..).value;
     *
     * in other words, make two pointers, one at start, another at end (thanks to callstack unwrapping) and move them
     * towards each other
     *
     * naive, D&C:
     *  - base case: current==null => return the head;
     *  - recursive case:
     *      - divide: progress forward, recursive(current);
     *      - conquer: calculate the sum of current.value (ith node from end) and returnedNode.value (ith note from start),
     *          if it's bigger than the current max => assign it to max;
     *      - combine: return returnedNode.next (progress the node from start forward to match with the twin
     *          `current` node from end of the previous call).
     *
     * Time: O(n), though we make unnecessary n/2 checks for duplicate sums (ith node from start and end just get reversed);
     * Space: O(n) due to the stack?
     */
    fun recursiveSuboptimal(head: ListNode): Int {
        val (_, maxSum) = recursiveInternal(head, head)
        return maxSum
    }

    private fun recursiveInternal(current: ListNode?, head: ListNode): Pair<ListNode?, Int> {
        if (current == null) return head to 0

        val (twinFromBeginning, lastMaxSum) = recursiveInternal(current.next, head)
        val currentSum = twinFromBeginning!!.value + current.value
        val maxSum = max(currentSum, lastMaxSum)
        return twinFromBeginning.next to maxSum
    }

    /**
     * Idea:
     *  - find the second middle node (the size is always even) - classic LL reverse algo; time O(n/2)
     *  - reverse the linked list starting from the second middle node; time O(n/2)
     *  - init slow=head, fast=secondMiddleNode (the original tail);
     *  - while slow!=secondMiddleNode: time O(n/2)
     *      - maxSum=max(slow.value+fast.value, maxSum)
     *      - slow=slow.next, fast=fast.next
     *  - return maxSum
     *
     * Edge cases: none, since we always have even an amount of nodes, and the min amount is 2.
     *
     * Time: O(n)
     * Space: O(1)
     */
    fun iterativeEfficient(head: ListNode): Int {
        // find the second middle node
        var slow: ListNode? = head
        var fast: ListNode? = head
        while (fast != null) {
            slow = slow!!.next
            fast = fast.next?.next
        }

        // reverse the second half of the list
        var prev: ListNode? = null
        val secondMiddleNode = slow!!
        var current: ListNode? = secondMiddleNode
        while (current != null) {
            val originalNext = current.next
            current.next = prev
            prev = current
            current = originalNext
        }

        // find the max twin sum
        val secondHalfNewHead = prev
        var twinFromStart: ListNode? = head
        var twinFromEnd: ListNode? = secondHalfNewHead!!
        var maxSum = 0
        while (twinFromStart != null && twinFromEnd != null) {
            val sum = twinFromStart.value + twinFromEnd.value
            if (sum > maxSum) maxSum = sum
            twinFromStart = twinFromStart.next
            twinFromEnd = twinFromEnd.next
        }
        return maxSum
    }
}

fun main() {
    println(
        MaximumTwinSumOfALinkedList().iterativeEfficient(
            ListNode(
                value = 1,
                next = ListNode(
                    value = 2,
                )
            )
        )
    )
}
