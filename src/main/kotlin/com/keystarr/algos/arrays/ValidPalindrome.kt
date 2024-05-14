package com.keystarr.algos.arrays

/**
 * LC-125 https://leetcode.com/problems/valid-palindrome/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= s.length <= 2 * 10^5;
 *  • s\[i] is valid ASCII;
 *  • no definition for "alphanumeric" given in terms of different alphabets, assume only English letters and arabic digits;
 *  • no explicit for time and space.
 *
 * Final note - done an efficient solution by myself [efficient] in 40 mins, but not the most clean version [efficientCleaner].
 */
class ValidPalindrome {

    /**
     * Tools: two-pointers.
     *
     * Time: O(n)
     * Space: O(1)
     */
    fun efficient(input: String): Boolean {
        var startInd = 0
        var endInd = input.length - 1
        while (startInd < endInd) {
            while (!input[startInd].isValid() && startInd < endInd) startInd++
            while (!input[endInd].isValid() && startInd < endInd) endInd--

            if (input[startInd].lowercaseChar() != input[endInd].lowercaseChar()) return false

            startInd++
            endInd--
        }
        return true
    }

    /**
     * Same as [efficient] but simplified the invalid characters skipping mechanism.
     *
     * Discovered that thanks to https://leetcode.com/problems/valid-palindrome/solutions/3165353/beats-96-9-well-explained-code-in-java/
     */
    fun efficientCleaner(input: String): Boolean {
        var startInd = 0
        var endInd = input.length - 1
        while (startInd < endInd) {
            when {
                !input[startInd].isValid() -> startInd++
                !input[endInd].isValid() -> endInd--
                else -> {
                    if (input[startInd].lowercaseChar() != input[endInd].lowercaseChar()) return false
                    startInd++
                    endInd--
                }
            }
        }
        return true
    }

    private fun Char.isValid() = isEnglishLetter() || isArabicDigit()

    private fun Char.isEnglishLetter() = code in (65..90) || code in (97..122)

    private fun Char.isArabicDigit() = code in (48..57)
}

fun main() {
    println(ValidPalindrome().efficientCleaner("  $#@$@##@$"))
}
