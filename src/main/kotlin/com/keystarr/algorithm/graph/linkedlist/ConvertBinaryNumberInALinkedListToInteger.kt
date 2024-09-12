package com.keystarr.algorithm.graph.linkedlist

import com.keystarr.datastructure.graph.linkedlist.LinkedListNode
import com.keystarr.datastructure.graph.linkedlist.singlyLinkedListOf

/**
 * LC-1290 https://leetcode.com/problems/convert-binary-number-in-a-linked-list-to-integer/description/
 * difficulty: easy
 * constraints:
 *  • 0 < number of nodes <= 30;
 *  • node.value is either 0 or 1.
 *
 * Final notes:
 *  • at first interpreted the problem wrong! again went to solve the wrong problem!
 *  • gave up at 30 mins: generated 3 approaches, 2 of which are asymptotically best but two pass. Couldn't find the
 *   time O(n) space O(1), even though I know there is one. Probably clever two pointers?
 *  • the solution was as I predicted, one pass O(n) time and O(1) space. I knew how to convert the number in the decimal
 *   system in one pass highest-to-lowest digits, but didn't know the concept holds for any numeric system => learned that.
 *
 * Value gained:
 *  • learned the general algorithm for highest to lowest digits number construction for any numeric system;
 *  • practiced solving a linked list problem cleanly efficiently.
 */
class ConvertBinaryNumberInALinkedListToInteger {

    // TODO: retry in 2-3 weeks

    /**
     * Use the property of any numeric system for converting digits to a number from highest to lowest digits:
     *  number = number * {base} + {newDigit}
     *
     * base = the base of the numeric system
     * newDigit = the ith digit in the input
     * => one pass, O(n) time and O(1) space as predicted
     *
     * learned thanks to https://leetcode.com/problems/convert-binary-number-in-a-linked-list-to-integer/solutions/629087/detailed-explanation-java-faster-than-100-00/
     */
    fun efficient(head: LinkedListNode?): Int {
        var number = 0
        var currentNode = head
        while (currentNode != null){
            number = number * 2 + currentNode.`val`
            currentNode = currentNode.next
        }
        return number
    }

    /**
     * "number" - Int or not?
     *
     * -------- backwards ---------
     *
     * approach #1:
     *  - one pass: reverse the linked list;
     *  - second pass, build the number:
     *   - twoPower = shl twoPower
     *   - number += if (val == 1) twoPower else 0
     *
     * Time: always O(n)
     * Space: always O(1)
     *
     * approach #2:
     *  same as #1 but don't reverse the list, just use recursion, return value is the number
     *
     * Time: always O(n)
     * Space: always O(n) for the callstack
     *
     * -------- frontal ----------
     *
     * 0110
     * expected=6
     *
     * approach #3:
     *  - one pass: count the number of nodes
     *  - twoPower = pow(2,nodesAmount-1)
     *  - second pass:
     *   - if (val == 1) number += twoPower
     *   - twoPower = shr twoPower
     *
     * Time: always O(n)
     * Space: always O(1)
     *
     * -----------------
     *
     * 3rd approach is the best:
     *  - 2nd is worst asymptotically due to space;
     *  - 1st and 3rd are both 2 pass with const space, but 1st modified the input and the 3rd doesn't.
     *
     * But there's gotta be a better solution than all these!
     * 1 pass O(n) time and O(1) space
     *
     * => it's gotta be frontal
     *  but to build the number from the highest bits we MUST know the number of bits, mustn't we?
     */

    /**
     * approach #2 implementation, out of curiosity
     */
    fun suboptimal(head: LinkedListNode?): Int = recursion(head)[0]

    /**
     * goal - return the decimal number representation of the linked list with the highest bit at [head] as [0] and the
     *  2^(highest bit) as [1].
     */
    private fun recursion(head: LinkedListNode?): IntArray {
        if (head!!.next == null) return intArrayOf(if (head.`val` == 1) 1 else 0, 1)

        val resultSoFar = recursion(head.next)
        resultSoFar[1] = resultSoFar[1] shl 1
        if (head.`val` == 1) resultSoFar[0] += resultSoFar[1]
        return resultSoFar
    }
}

fun main() {
    println(
        ConvertBinaryNumberInALinkedListToInteger().suboptimal(singlyLinkedListOf(1))
    )
}
