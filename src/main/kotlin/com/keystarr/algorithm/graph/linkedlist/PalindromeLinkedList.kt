package com.keystarr.algorithm.graph.linkedlist

import com.keystarr.datastructure.graph.linkedlist.ListNode

/**
 * ⭐️🔥 a prime example of a problem where one would have to solve 2 other problems to efficiently come up with a solution during an interview
 *  basically this medium has 3 subproblems: 2 other easy problems AND a new one on top of it, and one has to break the
 *  original problem into these first (which is NOT TRIVIAL), and efficiently solve both "easy" subproblems (which are quite tricky
 *  for those non-experienced in LinkedLists) => there's no way anyone can solve this during a 1h interview in O(n)
 *  and O(1) space who haven't previously solved these two basic subproblems on LinkedLists (or similar LinkedList problems!)
 *
 * LC-234 https://leetcode.com/problems/palindrome-linked-list/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= amount of nodes <= 10^5;
 *  • 0 <= node.value <= 9.
 *
 * Final notes:
 *  • 🏆🔥 done [efficient] by myself in 40 mins:
 *   • designed a trivial approach first, basically went for it, but then realized there's 100% gotta be a cleaner solution
 *    cause storing nodes is rarely the way (even a stack);
 *   • narrowed the problem to: how to solve in specifically O(1) space and/or 1 pass?
 *    => reasoned that we can't use a stack, how to do then? Instantly observed that the definition of palindrome may be
 *     phrased from start and from the end ("outside-in") to the middle OR from the middle to both start and end "inside-out"!!
 *    => recalled that we could find the middle node with O(1) space
 *    => assumed we could find the middle node and then compare from middle to start and from middle to end, though we could do O(1) space
 *    => went to implement but promptly realized its not O(1) space as-is cause to go from middle to start we have to use a stack
 *     in singly LL = that's O(n) space!
 *   • narrow the problem more, reduced to the subproblem: how to compare two halves from the middle with O(1) space?
 *    => instantly it struck me that we could reverse the 2nd half in O(1) space => then reduce the problem to simply 2 LL equality comparison.
 *   🏆 in other words, took a long approach and step-by-step reduced the problem from one subproblem to another, formulated
 *    and solved it => combined solutions with improvements together into the final approach.
 *    step-by-step reasoning + observations + relevant tooling mastery works! also having solved prerequisite problem on the topic helps a ton.
 *
 *  • 💡an interesting example of a LinkedList problem with O(n) time O(1) space solution, but no single pass;
 *
 *  • ⚠️ done everything by myself, BUT still used 1 custom hint: couldn't remember quickly, whether we even can reverse a LL
 *   with O(1) space. Check [ReverseLinkedList] for the complexities => seen that we could => reconstructed the idea by myself.
 *   Interesting how knowing that the solution 100% exists gave me confidence and I cracked it really fast afterward.
 *
 * Value gained:
 *  • practiced solving a complex linked list property calculation type problem utilizing 3 various 2 pointer techniques:
 *  [MiddleOfTheLinkedList], [ReverseLinkedList] and 2 LL equality;
 *  • practiced breaking a murky problem down into unexpectedly familiar subproblems and combining their solution together.
 */
class PalindromeLinkedList {

    // TODO: done decent, but sweated a lot, learned new ideas here => retry full in 1-3 weeks to reinforce new ideas

    /**
     * goal: return true if the linked list is a palindrome
     *
     * a palindrome linked list is such that all list\[i] == list\[j] with i=0..(n/2-1) and j=(n-1)..(n/2)
     *
     * major cases, the amount of nodes is:
     *  - even:
     *  - odd:
     *
     * trivial approach:
     *  - declare currentFromStart=head
     *  - use iterative + stack / recursion => roll until the last node + compare currentFromStart to currentNode, if not
     *   equals => return false. Move currentFromStart to its next every time
     *
     * Time: always O(n)
     * Space: always O(n)
     *
     * but its O(n) space and 2 pass => may there be an actual O(1) space 1 pass solution?
     *
     * ---------- efficient approach (1st attempt)
     *
     * can we do either O(1) space or 1 pass or even both?
     *
     * we could find the middle of linked list and from there launch two pointers one left another right!
     *
     * technically its 1 pass, we'd visit first half twice, but the second time would ve at the same time as we move right,
     *  so we'd have exactly n iterations across both loops => 1 pass.
     *
     * use 2 pointers fast and slow to find the middle of LL
     *
     * even eg: 1 2 3 2 1
     * odd:     1 2 2 1
     *
     * slow=head
     * fast=head.next
     *
     * slow=slow.next
     * fast=fast.next.next
     *
     * while(fast.next != null)
     * => slow is the first middle node if there are two (even amount), or the only middle node (odd nodes amount)
     *
     * OK, how to then roll left/right - check for palindrome?
     *
     * oh, but how would we travel left? Here we'd still have to store all values of the first half in the stack then
     *  which is still O(n) space!
     *
     * Time: always O(n)
     * Space: always O(n/2)=O(n), just a better const than the trivial approach
     *
     * ------------- actual efficient approach (2nd successful attempt)
     *
     * hm, can we reverse the second half of the list then?
     * if we could => find the middle node + reverse the second half of the list + iterate from the start and the middle
     *  towards the end => compare.
     *
     * is it OK to modify input? assume yes
     *
     * how do we reverse though with O(1) space?
     *  trivial reversal is recursion which is O(n) space
     *  is it even possible?
     *
     *  simply as we go reverse directions and return the last node as the new head!
     *
     * Edge cases:
     *  - number of nodes == 1 => always return true => first loop is never entered + crash on reverse(slow.next!!) since
     *   slow.next == null => do an early return;
     *  - number of nodes == 2 => slow=head, fast=head.next + reversedSecondHalfHead=head.next + the result is determined by
     *  the comparison of the head to head.next => correct;
     *  - number of nodes == 3 => slow = head.next (2nd node), fast=head.next.next (3rd node) + reversedSecondHalfHead=head.next.next (3rd node)
     *   + the result is determined by head.val == head.next.next.val => correct.
     *
     * Time: always O(n)
     *  - find the 1st middle node: n/2 iterations => always O(n);
     *  - reverse the second half of the list: n/2 iterations => always O(n);
     *  - compare the first half of the list and the 2nd half of the list reversed: n/2 iterations => average/worst O(n).
     *   best is the first comparison is false => O(1).
     * Space: always O(1)
     *  2 pointers for finding the middle node + 2 pointers for the reversal + 2 pointers for the lists equality comparison
     */
    fun efficient(head: ListNode): Boolean {
        if (head.next == null) return true

        val firstMiddleNode = findFirstMiddleNode(head)
        val reversedSecondHalfHead = reverse(firstMiddleNode.next!!)

        // compare the first half with the reversed second half of the list
        var fromStart = head
        var fromMiddle: ListNode? = reversedSecondHalfHead // reverse the 2nd part of the list
        while (fromMiddle != null) {
            if (fromStart.`val` != fromMiddle.`val`) return false
            fromStart = fromStart.next!!
            fromMiddle = fromMiddle.next
        }
        return true
    }

    /**
     * Termination: slow = the 1st middle node if even, the only if odd.
     *
     * Almost pure [MiddleOfTheLinkedList]!
     *
     * Time: always O(n), where n = numbers of nodes in [head]
     * Space: always O(1)
     */
    private fun findFirstMiddleNode(head: ListNode): ListNode {
        var slow: ListNode = head
        var fast: ListNode? = head.next
        while (fast?.next != null) {
            slow = slow.next!!
            fast = fast.next?.next
        }
        return slow
    }

    /**
     * Edge cases:
     *  - amount of nodes == 1 => prevNode=null currentNode=head initially, one iteration and head.next=null, prevNode=head
     *   => the loop termination property is still true as prevNode == last node in the list
     *
     * 🔥 basically an almost pure [ReverseLinkedList] as a subproblem for this one))))
     *
     * Time: always O(n), where n=number of nodes in the LL [head]
     * Space: always O(1)
     */
    private fun reverse(head: ListNode): ListNode? {
        var prevNode: ListNode? = null
        var currentNode: ListNode? = head
        while (currentNode != null) {
            val originalNext = currentNode.next
            currentNode.next = prevNode
            prevNode = currentNode
            currentNode = originalNext
        }
        return prevNode // always the last node in the list
    }
}
