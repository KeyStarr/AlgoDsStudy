package com.keystarr.algorithm.stack

import com.keystarr.datastructure.stack.simple.BackwardsSinglyLinkedListStack

/**
 * LC-20 https://leetcode.com/problems/valid-parentheses/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= input.length <= 10^4;
 *  • input consists of parentheses only: (), [], {}.
 */
class ValidParentheses {

    /**
     * Hint - checking for a valid pair of characters in the string => Stack?
     *
     * Since brackets must be closed in the order of them being opened => indeed use a stack.
     * (if currentChar=closing bracket => it's only valid if the top of the stack is an opening bracket of the same type)
     *
     * Idea:
     *  - create bracketsStack;
     *  - iterate through [input]:
     *      - if the current character is the closing bracket, check if bracketsStack.pop() is the opening bracket of the same type:
     *          - yes => continue;
     *          - no => return false.
     *      - if the current character is the opening bracket, bracketsStack.push(currentChar)
     *  - if the loop terminated without returning => return true.
     *
     * Edge cases:
     *  - input.length % 2 == 1 => return false cause it means we guaranteed have at least 1 bracket without a pair.
     *
     * Time: average/worst O(n) cause stack operations and bracket matching are all O(1);
     * Space: average/worst O(k), where k - is the largest number of consecutive opened brackets in the [input] (basically O(n)).
     */
    fun efficient(input: String): Boolean {
        if (input.length % 2 == 1) return false

        val stack = BackwardsSinglyLinkedListStack<Char>()
        val openingBrackets = mapOf('(' to ')', '[' to ']', '{' to '}')
        input.forEach { char ->
            if (openingBrackets.contains(char)) {
                stack.push(char)
            } else {
                val lastOpenedBracket = stack.pop()
                val requiredClosingBracket = openingBrackets[lastOpenedBracket]
                if (char != requiredClosingBracket) return false
            }
        }
        return stack.peek() == null
    }
}

fun main() {
    println(ValidParentheses().efficient("()"))
}
