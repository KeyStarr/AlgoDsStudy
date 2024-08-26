package com.keystarr.algorithm.graph.linkedlist

/**
 * LC-24 https://leetcode.com/problems/swap-nodes-in-pairs/description/
 * difficulty: medium
 * constraints:
 *  • 0 <= number of nodes <= 100;
 *  • 0 <= Node.val <= 100;
 *  • values of nodes must not be changed, only nodes order (via next) may be changed;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • dry-running (with code in-place already) revealed the error => fixed BUT after I wrote the code for the first design!
 *      thought prev.next=originalNext would be always enough, but actually unless we're at the last iteration and the
 *      list.size is odd, then we will rearrange the next pair => have to link directly in advance of the rearrangement
 *      to the originalNext.next;
 *  • FAILED 1 submission, erroneously handled the case of odd list size (thought prev.next=originalNext.next is enough,
 *      but in the case of odd size we don't rearrange the next pair, so have to link to originalNext);
 *  • solved via [efficient] by myself in 50 mins, SO MANY EDGE CASES, MAAN;
 *  • interestingly enough, the solution in the course starts prev=null, head=1st node; but it looks about the same elegant;
 *  • indeed, isolation a single step and modelling that helps tremendously to solve linked list problems. Kinda of like
 *      D&C right?
 *
 * Value gained:
 *  • BE EXTREMELY WARY of Linked List mediums AND ALWAYS DRY RUN at least 3 key cases (?);
 *  • for some reason I was surprised that we return not head as the answer, due to head being rearranged;
 *  • practiced using 2 ADJACENT pointers to modify the linked list in O(n) time O(1) space;
 */
class SwapNodesInPairs {

    /**
     * Hint: LinkedList => two pointers, slow&fast?
     *
     * Problem rephrase: "swap every 2 adjacent nodes, each node can be used in swapping at most once".
     *
     * Idea:
     *  - current=head?.next, previous=head;
     *  - while current != null (on the previous iteration current was, or on this one prev is the tail):
     *      - originalNext=current.next
     *      - current.next = prev (reverse the link between current and prev)
     *      - prev.next=if(originalNext.next!=null) originalNext.next else original.next
     *          (link to the future next node in advance of the rearrangement of the next pair)
     *          (special case - if list.size is odd, then the tail would eventually be originalNext + it wouldn't have a pair
     *           => we need to link to originalNext. otherwise tail is originalNext.next => it will be rearranged to the
     *           left => link to originalNext.next).
     *      - move current and prev to the next pair.
     *  - return head.
     *
     * Edge cases:
     *  - return pre-saved HEAD.NEXT! cause the first pair will always be rearranged if number of nodes > 1,
     *      and if we return head.next AFTER the loop => it already will be rewritten => point at the next pair;
     *  - number of nodes = 0 => return null, early return cause of head.next;
     *  - number of nodes = 1 => return head, early return cause of head.next;
     *  - number of nodes is odd => tail will eventually be originalNext, no rearrange => at the last iteration link prev to it;
     *  - number of nodes is even => tail will eventually be originalNext.next, will be rearranged left => link to it.
     *
     * Time: always O(n)
     * Space: always O(1)
     */
    fun efficient(head: LinkedListNode?): LinkedListNode? {
        if (head == null) return null
        if (head.next == null) return head

        val newHead = head.next

        var current = head.next
        var prev = head
        while (current != null) {
            val originalNext = current.next
            current.next = prev
            prev?.next = if (originalNext?.next != null) originalNext.next else originalNext

            current = originalNext?.next
            prev = originalNext
        }
        return newHead
    }
}

fun main() {
    SwapNodesInPairs().efficient(
        LinkedListNode(
            `val` = 1,
            next = LinkedListNode(
                `val` = 2,
                next = LinkedListNode(
                    `val` = 3,
                    next = null,
//                    next = ListNode(
//                        value = 4,
//                        next = null,
//                    )
                )
            )
        )
    ).debugPrintContents()
}
