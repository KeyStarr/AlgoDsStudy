package com.keystarr.algorithm.divideandconquer

import com.keystarr.algorithm.linkedlist.ListNode
import com.keystarr.algorithm.linkedlist.formatToEnd
import com.keystarr.algorithm.linkedlist.linkedListOf

// 206 https://leetcode.com/problems/reverse-linked-list/description/
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

    // time: O(N)
    // space: O(1)
    //
    // didn't get it, took some effort to understand, based upon the solution by some person,
    // elaborated on their take a bit:
    // https://leetcode.com/problems/reverse-linked-list/solutions/4905306/reverse-linked-list-easy-solution-with-explanation-iterative-recursive-100-beats/comments/2346821
    //
    // why did I even bother? it is 1 function instead of 2, with 1 arg instead of 2, but is SO MUCH MORE unclear,
    // certainly the first one would be better for prod, cause here is, like, 0 asymptotic performance benefit.
    // What am I spending my life on???
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
