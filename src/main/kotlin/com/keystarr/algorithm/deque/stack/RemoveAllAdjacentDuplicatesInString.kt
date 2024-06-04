package com.keystarr.algorithm.deque.stack

/**
 * LC-1047 https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= input.length <= 10^5;
 *  • input consists of lowercase English.
 */
class RemoveAllAdjacentDuplicatesInString {

    /**
     * We only remove characters in the situation when the most recent character equals the new incoming character.
     *  AND by removing the most recent the next incoming may match the previous most recent etc.
     *  => Last in (if valid) first out, can be multiple times.
     *
     * It is inefficient to modify the String each time we encounter a duplicate, cause we copy the string on each
     * mutation => average/worst case would be n mutation => O(n^2) time;
     *
     * We could however simply mark a character as deleted and not physically remove it, or not even mark, but keep
     * and index of the current active portion of the array that represents the intermediate result (excluding those
     * characters that were set but "deleted").
     *
     * Edge cases:
     *  - input.length==1 => always as-is, correct;
     *  - no duplicates => return as-is, correct (reconstructed from the builder);
     *  - all duplicates => return an empty string, correct (builderInd!=0 checked).
     *
     * Time: we do no array copies (due to initial builder capacity = input.length) + we check each element => always O(n)
     * Space: always O(n) where n=input.length due to resultArray (could use ArrayList to minimize space const but would be
     *  same O complexity and worse time const)
     */
    fun array(input: String): String {
        val resultArray = CharArray(input.length)
        var builderInd = -1
        input.forEach { char ->
            if (builderInd != -1 && resultArray[builderInd] == char) {
                builderInd--
            } else {
                builderInd++
                resultArray[builderInd] = char
            }
        }
        return String(resultArray).take(builderInd + 1)
    }

    /**
     * Same as [array] but use [ArrayDeque] as the Stack implementation.
     *
     * Idea:
     * - iterate through input:
     *  - if current character == most recent character in the stack => stack.pop(), go next;
     *  - else add current character into the stack.
     * - convert the stack into a String and return it.
     *
     * Time: always O(n), every resize is amortized O(1) (for kotlin impl of ArrayDeque its double the size)
     * Space: average/worst O(n)
     */
    fun deque(input: String): String {
        val stack = ArrayDeque<Char>()
        input.forEach { char ->
            if (stack.isNotEmpty() && stack.last() == char) {
                stack.removeLast()
            } else {
                stack.addLast(char)
            }
        }
        return stack.joinToString(separator = "")
    }

    /**
     * Use a [StringBuilder], but use [StringBuilder] as the Stack implementation.
     *
     * Notes:
     * - resizing factor is amortized O(1), which is ok, cause [java.lang.StringBuilder] doubles the capacity + 2 once
     *  it is reached;
     * - `deleteCharAt` is O(1) even though under the hood `System.arraycopy(..)` is called, cause we always delete only
     *  the left character => no need to copy those to the right of it (technically I didn't see the sources of that native
     *  method, but that's as good a guess as it goes).
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     */
    fun stringBuilder(input: String) = StringBuilder().apply {
        input.forEach { char ->
            if (isNotEmpty() && last() == char) {
                deleteCharAt(length - 1)
            } else {
                append(char)
            }
        }
    }.toString()
}

fun main() {
    println(RemoveAllAdjacentDuplicatesInString().stringBuilder("accabc"))
}
