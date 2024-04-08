package algos.linkedlist

// https://leetcode.com/problems/middle-of-the-linked-list/description/
// constraints:
//  - no explicit time
//  - no explicit memory
//  - input size < 100 nodes
class MiddleOfLinkedList {

    // time: O(n), space: O(1)
    fun slow(head: ListNode?): ListNode? {
        if (head == null) return null

        val size = head.countSize()
        return head.findNode(index = size / 2)
    }

    private fun ListNode.countSize(currentSize: Int = 1): Int =
        next?.let { next?.countSize(currentSize + 1) } ?: currentSize

    private fun ListNode.findNode(index: Int, currentIndex: Int = 0): ListNode? =
        if (currentIndex == index) this else next?.findNode(index, currentIndex + 1)
}

fun main() {
    val input = linkedListOf(1, 2)
    val result = MiddleOfLinkedList().slow(input)
    println(result?.formatToEnd())
}

private fun linkedListOf(vararg nums: Int): ListNode? {
    if (nums.isEmpty()) return null
    return ListNode(nums[0]).apply {
        var currentNode = this
        (1 until nums.size).forEach { ind ->
            currentNode.next = ListNode(nums[ind])
            currentNode = currentNode.next!!
        }
    }
}

private fun ListNode.formatToEnd(output: StringBuilder = StringBuilder()): String {
    output.append("$`val`, ")
    return next?.formatToEnd(output) ?: output.toString()
}

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}