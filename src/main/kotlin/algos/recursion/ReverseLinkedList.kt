package algos.recursion

import algos.linkedlist.ListNode
import algos.linkedlist.formatToEnd
import algos.linkedlist.linkedListOf

// LC-206
// https://leetcode.com/problems/reverse-linked-list/
class ReverseLinkedList {

    // time: O(n)
    // space: O(1)
    fun iterative(head: ListNode?): ListNode? {
        var previous: ListNode? = null
        var current = head
        while (current != null) {
            val originalNext = current.next
            current.next = previous
            previous = current
            current = originalNext
        }
        return previous
    }

    // time: O(N)
    // space: O(1), but a bit more due to the callstack
    // this solution is dumb - not using the benefits of recursion at all:
    // - not clearer to understand;
    // - not accumulating the result when unwrapping the callstack;
    //
    // basically just keeping the "previous" and "current" variables of the iterative solution in the callstack
    // even though we don't need those at all after the execution.
    fun dumbRecursive(head: ListNode?): ListNode? = dumbRecursiveIteration(previous = null, current = head)

    private fun dumbRecursiveIteration(previous: ListNode?, current: ListNode?): ListNode? =
        if (current != null) {
            val originalNext = current.next
            current.next = previous
            dumbRecursiveIteration(current, originalNext)
        } else previous // the original final node (new beginning node)

    // didn't get it, took some effort to understand, based upon the solution by some person,
    // elaborated on their take a bit:
    // https://leetcode.com/problems/reverse-linked-list/solutions/4905306/reverse-linked-list-easy-solution-with-explanation-iterative-recursive-100-beats/comments/2346821
    fun elegantRecursive(current: ListNode?): ListNode? {
        if (current?.next == null) return current

        val newHead = elegantRecursive(current.next)

        val next = current.next
        next?.next = current
        current.next = null

        return newHead
    }
}

fun main() {
    println(
        ReverseLinkedList().elegantRecursive(
            linkedListOf(1, 2)
        )?.formatToEnd()
    )
}
