package com.keystarr.algorithm.deque.stack

/**
 * LC-844 https://leetcode.com/problems/backspace-string-compare/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= first.length, second.length <= 200;
 *  • first and second are lowercase English and '#'.
 */
class BackspaceStringCompare {

    /**
     * Problem rephrase:
     * "when we encounter # in a string (either [first] or [second]) remove the previous non-removed character if it exists.
     * return true if after such processing both strings are equal"
     *
     * Hint: first character to be deleted, upon encountering #, is the last most recent non-removed one => LIFO.
     *
     * Idea (perform processing. the simplest turned out to be the best, had another more complicated one which crumbled!):
     *  - create firstProcessed: StringBuilder and secondProcessed: StringBuilder;
     *  - process each first and second strings the same, iterate over the string:
     *      - if currentChar == '#' => remove the last char from the builder;
     *      - else append currentChar to the builder.
     *  - return true if firstProcessed==secondProcessed.
     *
     * Edge cases:
     *  - first.length=1, second.length=1 => nothing special;
     *  - first.first() or second.first() == '#' => handle via extra check;
     *  - '#' in the middle, but due to processing current builder is empty => do nothing! MISSED.
     *
     * Time: O(n+m), either O(n) or O(m) whichever is the largest
     * Space: O(n+m), basically too, either O(n) or O(m)
     */
    fun solution(first: String, second: String) = processBackspaces(first).contentEquals(processBackspaces(second))

    private fun processBackspaces(input: String) = StringBuilder().apply {
        input.forEach { char -> if (char != '#') append(char) else if (isNotEmpty()) deleteCharAt(length - 1) }
    }
}

fun main() {
    println(BackspaceStringCompare().solution("y#fo##f", "y#f#o##f"))
}
