package com.keystarr.algorithm.linkedlist

/**
 * LC-92 https://leetcode.com/problems/reverse-linked-list-ii/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= the number of nodes <= 500;
 *  • -500 <= Node.value <= 500;
 *  • 1 <= left <= right <= the number of nodes;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • solved by myself in 45 mins (so long!). 3 sub-problems, plenty of edge cases to check and plenty of code to write.
 *      hm, sounds no ok for interview, right?
 *  • didn't think recursive would worth the effort, so skipped it.
 *
 * Value gained:
 *  • failed 1 submission :( erroneously assumed that we need to return preOriginalSubListHead if it exists ONLY
 *      cause dry ran on the case where there was only 1 node before the sublist :((( actually it's the original head
 *      that we want to return but only if it's not in the sublist (trivially leftInd!=0), otherwise it's the new sublist
 *      head that we need.
 *      =>
 *      lesson: BE MORE CAUTIOUS, DOUBLE CHECK AND DRY RUN on SEVERAL key cases WHICH NODE TO RETURN in linkedlist problems;
 *
 *  • I think I need more practice with identifying the sub-problems of such complex linkedlist medium's
 *      + identifying the edge cases + quickly implementing the common algos like reversing of the list.
 */
class ReverseLinkedListII {

    /**
     * Problem rephrase:
     * "perform a partial in-place reverse of the linked list from index left to right, both inclusive, return
     * the resulting head"
     *
     * Hint: LinkedList reverse -> two pointers
     *
     * Idea:
     *  (find node at index leftInd)
     *  - init current=head, prev=null, currentInd=0;
     *  - leftInd=left-1, rightInd=right-1;
     *  - iterate while currentInd!=leftInd, each iteration also prev=current, current=current.next;
     *  - subListTail=current
     *  - preSubListTail=prev
     *
     *  - reverse the nodes from leftInd to rightInd:
     *      - prev=null;
     *      - iterate until currentInd<=right:
     *          - originalNext=current.next
     *          - current.next=prev
     *          - prev=current
     *          - current=originalNext
     *  - subListHead=prev
     *  - afterSubListTail=current
     *
     *  (link the reversed portion of the list back into the list)
     *  - if (preSubListTail!=null) preSubListTail.next=subListHead
     *  - if (afterSubListTail!=null) subListTail.next=afterSubListTail
     *  (return result)
     *  - return if (preSubListTail!=null) preSubListTail else subListHead
     *
     * Edge cases:
     *  - number of nodes=1 => early return head;
     *  - leftInd=rightInd (sublist of 1 element) => reverse nothing, early return head;
     *  - rightInd-leftInd+1 = the number of nodes => correct.
     *  - number of nodes=2 => perform reversion only if sublist is the entire list => correct.
     *
     * Time: O(n)
     * Space: O(1)
     */
    fun solution(head: ListNode, left: Int, right: Int): ListNode {
        if (head.next == null) return head
        if (left == right) return head

        val leftInd = left - 1
        val rightInd = right - 1

        var current: ListNode? = head
        var prev: ListNode? = null
        var currentInd = 0

        // find leftInd node
        while (currentInd != leftInd) {
            prev = current
            current = current!!.next!!
            currentInd++
        }
        val newSubListTail = current
        val preOriginalSubListHead = prev

        // reverse nodes from leftInd to rightInd
        prev = null
        while (currentInd <= rightInd) {
            val originalNext = current!!.next
            current.next = prev
            prev = current
            current = originalNext
            currentInd++
        }
        val newSubListHead = prev!!
        val afterOriginalSubListTail = current

        // link the reversed sublist back into the list
        if (preOriginalSubListHead != null) preOriginalSubListHead.next = newSubListHead
        if (afterOriginalSubListTail != null) newSubListTail!!.next = afterOriginalSubListTail

        return if (leftInd == 0) newSubListHead else head
    }
}

fun main() {
    ReverseLinkedListII().solution(
        head = ListNode(
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
        ),
        left = 2,
        right = 3,
    ).debugPrintContents()
}
