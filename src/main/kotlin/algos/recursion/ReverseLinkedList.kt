package algos.recursion

import algos.linkedlist.ListNode
import algos.linkedlist.formatToEnd
import algos.linkedlist.linkedListOf

// https://leetcode.com/problems/reverse-linked-list/submissions/1228341401/
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

    // TODO: fix, doesnt work
    fun elegantRecursive(current: ListNode?): ListNode? {
        if (current?.next == null) return current

        val previous = elegantRecursive(current.next)!!

        previous.next = current
        current.next = null

        return previous
    }

    // TODO: why does this work?????
    // https://leetcode.com/problems/reverse-linked-list/solutions/4905306/reverse-linked-list-easy-solution-with-explanation-iterative-recursive-100-beats/
    fun reverseList(head: ListNode?): ListNode? {
        if (head?.next == null) return head

        val newHead = reverseList(head.next)

        val front = head.next
        front!!.next = head
        head.next = null

        return newHead
    }
}

fun main() {
    println(
        ReverseLinkedList().elegantRecursive(
            linkedListOf(1, 2, 3)
        )?.formatToEnd()
    )
}
