package com.keystarr.algorithm.array.twopointers

/**
 * LC-344 https://leetcode.com/problems/reverse-string/description/
 * constraints:
 *  • 1 <= s.length <= 10^5;
 *  • s\[i] is ASCII;
 *  • space: O(1);
 *  • no explicit time.
 *
 * Final note - done an efficient solution myself in 5 mins.
 */
class ReverseString {

    /**
     * Tools: Two Pointers.
     * Idea: swap 2 elements each iteration, starting from the edges.
     *
     * Time: always O(n)
     * Space: always O(1)
     */
    fun efficient(input: CharArray) {
        var left = 0
        var right = input.size - 1
        while (left < right) {
            val buff = input[left]
            input[left] = input[right]
            input[right] = buff

            left++
            right--
        }
    }
}

fun main() {
    val input = "a12hf0".toCharArray()
    ReverseString().efficient(input)
    println(input)
}
