package com.keystarr.algorithm.stack

/**
 * LC-1544 https://leetcode.com/problems/make-the-string-great/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= input.length <= 100;
 *  • input contains only lower and upper case English.
 *
 * Final notes:
 *  • done [suboptimal] in 15 mins (why so long???).
 *
 * Value gained:
 *  • practiced recognizing problems with LIFO;
 *  • practiced StringBuilder as Stack, the algo pattern is same as [RemoveAllAdjacentDuplicatesInString];
 *  • FAILED A SUBMISSION, though about BUT when implementing missed the case that when adjacent chars are same letter
 *      with same case THAT'S A VALID PAIR! Done sorta muscle-memory both lowercase()... pay ATTENTION to specifics of
 *      the task at hand, MAKE IT SPECIAL even if it seems like you know it all! (use phenomenology!)
 */
class MakeTheStringGreat {

    /**
     * Problem rephrase:
     * "Process string in a way, that no two adjacent characters are the same English letter in reverse case".
     *
     * Hint: adjacent + removing chars from a string + when removing, new invalid pairs might emerge => LIFO? Stack?
     * (upon removing a pair => removing the current most recent char we must check the one previous to it, which is
     * now the most recent, with a new char etc going literally last in first out for already visited chars)
     *
     * Idea:
     *  - create a stack `result`;
     *  - iterate through input:
     *      - if currentChar.lowercase()==stack.peek().lowercase():
     *          - stack.pop();
     *          - continue.
     *      - else stack.push(currentChar).
     *  - convert stack into String and return it.
     *
     * Use StringBuilder, cause we work with single chars => simplified String conversion at the end (better const then,
     * say, LinkedList).
     *
     * Edge cases:
     *  - input.length==1 => always return itself, works correctly.
     *
     * Time: always O(n)
     * Space: worst/average O(n)
     */
    fun suboptimal(input: String): String {
        val result = StringBuilder()
        input.forEach { char ->
            if (result.isNotEmpty() && char.reverseCase() == result.last()) {
                result.deleteCharAt(result.length - 1)
            } else {
                result.append(char)
            }
        }
        return result.toString()
    }

    private fun Char.reverseCase() = if (isUpperCase()) lowercaseChar() else uppercaseChar()

    // TODO: implement an efficient time O(n) space O(1) solution (not a focus now)
}
