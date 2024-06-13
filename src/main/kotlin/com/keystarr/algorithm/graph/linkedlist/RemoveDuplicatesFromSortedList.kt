package com.keystarr.algorithm.graph.linkedlist

/**
 * LC-83 https://leetcode.com/problems/remove-duplicates-from-sorted-list/description/
 * difficulty: easy
 * constraints:
 *  • 0 <= number of nodes <= 300;
 *  • -100 <= Node.val <= 100;
 *  • the list is sorted ascending;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • done [efficient] by myself in 25 mins. Spent quite some time checking the edge cases AND STILL failed 1 submission,
 *      with the edge case being duplicates at the end of the list.
 *
 * Value gained:
 *  • wasted 5 mins or so designing a brute force solution, definitely wasn't worth it! better than being stuck, but
 *      should've caught on the "slow/fast" pointers straight on;
 *  • Arrays/HashSet/Hashmap - any additional collection is never an optimal solution for LinkedList interview problems??
 *  • [efficientCleaner] is so elegant and beautiful, basically it's the two pointer approach, but it makes use of the
 *      property OF THIS PARTICULAR problem (makes use of the moment!) that "fast" pointer always = slow.next!
 *  • once again, the 'skipping the elements' part may be implemented via an inner `while` OR simply an `if/else`:
 *      one branch for skipping (in this case with deletion of the nodes), another for moving forward! I've seen that before
 *      but that time I also implemented the inner loop solution and didn't see this elegant 2 branch one.
 */
class RemoveDuplicatesFromSortedList {

    /**
     * Hint - curiously the list is "sorted", how can we make use of that?
     *
     * Brute force idea:
     *  - iterate through the list;
     *  - add each node in the hashset (hash computed by value of the node);
     *  - for each node check if its already in the set, if so, delete it from the list;
     *  - return head.
     * Time: always O(n)
     * Space: worst/average O(n)
     *
     * -------------  What would solution be for Space O(1)? -------------
     *
     * Hint: a SORTED LinkedList + Space O(1) => Two Pointers fast & slow??
     * Idea:
     *  - initialize slow=head, fast=head.next (no point pointing fast at head, cause we look for duplicates only, and
     *      the node itself is not a duplicate to itself);
     *  - iterate fast != null:
     *      - while(slow==fast):
     *          - delete node at fast from the list;
     *          - fast=fast.next (using a temp buff var).
     *      - slow=slow.next
     *      - fast=fast.next
     * - return head
     *
     * (algo property - fast will always be 1 node ahead of the slow)
     *
     * 1 2 2 2 3 4
     *
     * 2 2
     * 2 1
     *
     * Edge cases:
     *  - number of nodes = 0 => return null;
     *  - number of nodes = 1 => return head, correct;
     *  - number of nodes = 2 =>
     *      - if they are duplicates, remove the second, return head, correct;
     *      - if not duplicates, just return head, correct;
     *  - duplicates are at the end, e.g. [1 2 4 4] => slow.next after the inner while is null, so slow=null => had a CRASH
     *      due to !!, fixed.
     *
     * Time: O(n)
     * Space: O(1)
     */
    fun efficient(head: ListNode?): ListNode? {
        if (head == null) return null

        var slow: ListNode? = head
        var fast = head.next
        while (fast != null) {
            // delete duplicates, fast is still always 1 node ahead of slow
            while (slow!!.value == fast?.value) {
                slow.next = fast.next
                fast = fast.next
            }
            slow = slow.next
            fast = fast?.next
        }

        return head
    }

    /**
     * Same general idea and complexities as [efficient], but less code.
     * Basically, replace "fast" pointer by just using slow.next directly.
     *
     * 7 7 7 8 9 9
     *
     * Discovered thanks to:
     * https://leetcode.com/problems/remove-duplicates-from-sorted-list/editorial/
     */
    fun efficientCleaner(head: ListNode?): ListNode? {
        var currentNode = head
        while (currentNode?.next != null) {
            if (currentNode.value == currentNode.next!!.value) {
                currentNode.next = currentNode.next?.next
            } else {
                currentNode = currentNode.next
            }
        }
        return head
    }
}

fun main() {
    RemoveDuplicatesFromSortedList().efficient(
        ListNode(
            value = 1,
            next = ListNode(
                value = 1,
                next = ListNode(
                    value = 2,
                    next = null,
                )
            )
        )
    ).debugPrintContents()
}

fun ListNode?.debugPrintContents() {
    var currentNode = this
    while (currentNode != null) {
        println(currentNode)
        currentNode = currentNode.next
    }
}
