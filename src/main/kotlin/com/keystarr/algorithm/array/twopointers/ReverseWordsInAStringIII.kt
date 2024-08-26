package com.keystarr.algorithm.array.twopointers

/**
 * LC-557 https://leetcode.com/problems/reverse-words-in-a-string-iii/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= text.length <= 5 * 10^4;
 *  • text contains only printable ASCII chars;
 *  • text doesn't contain leading/trailing spaces;
 *  • at least one word in text;
 *  • all words are separated by exactly a single space.
 *
 * Final notes:
 *  • done [suboptimal] by myself in 11 mins;
 *  • since it is accepted and there's no follow-up in problem statement => check answer. Don't see a clear approach
 *   to O(1) space, and it's not optimal to invest time in that right now;
 *  • ⚠️ really, should've reasoned and got to [efficient] by myself! Just a couple of steps of reasoning away.
 *   Next time, if I suspect there's a better space complexity on a leet-easy problem => try to reach it myself!
 *
 * Value gained:
 *  • practiced solving a string reversal type problem using two pointers [efficient].
 */
class ReverseWordsInAStringIII {

    // TODO: retry in 1-2 weeks

    /**
     * Goal: reverse the order of characters within each word, but preserve whitespaces and the order of words.
     *
     * Trivial approach:
     *  - iterate through [text] backwards, starting at the end, add each word into a list of string (where string=word);
     *   (we'd get reverse order of words, each word reversed)
     *  - iterate through the words list backwards and build the result string.
     *   (we'd get the reversed reversed order of words = the original order of words, each word reversed)
     *
     * Time: always O(n)
     *  - building a list of reversed words reversed order O(n);
     *  - building the result string O(n), we'd iterate through m words, but we'd have to add each original character
     *  into the StringBuilder  individually, and we have n characters.
     * Space: always O(n)
     *  - for the temp list of words O(n), causes n the total amount of chars across all reversed words
     *
     * ------ can we improve space to O(1) and keep time at O(n)? ------
     *
     * well, technically result is O(n) anyway so the asymptotic space complexity wouldn't change
     * but its a common way of getting at the best solution, to ensure that aside from the result we have as little space
     * as possible, even if it doesn't change the asymptotic complexity (which isn't the way in most real world applications though)
     */
    fun suboptimal(text: String): String {
        val reversedWords = mutableListOf<List<Char>>()
        var currentWord = mutableListOf<Char>()
        for (i in text.length - 1 downTo 0) {
            val char = text[i]
            if (char == ' ') {
                reversedWords.add(currentWord)
                currentWord = mutableListOf()
            } else {
                currentWord.add(char)
            }
        }
        reversedWords.add(currentWord)

        val result = StringBuilder()
        for (i in reversedWords.size - 1 downTo 0) {
            val word = reversedWords[i]
            word.forEach(result::append)
            if (i != 0) result.append(' ')
        }

        return result.toString()
    }

    /**
     * Observations:
     *  - in [suboptimal] actually, when we reach the end of the kth word, we may start reversing it and adding to the result
     *   straight away. We gain no logic benefits from taking all the words together, nothing to wait for.
     *
     * Approach: iterate through [text], once we reach a whitespace, simply iterate backwards until the start of the word
     *  and append each character into the result StringBuilder 🙂
     *
     * Time: always O(n)
     * Space: always O(1)
     *
     * ----
     *
     * Learned thanks to https://leetcode.com/problems/reverse-words-in-a-string-iii/description/
     *
     * Hm, could have I seen it if I invested more time into finding a O(1) space solution? Its a trivial optimization of the
     * [suboptimal], really, just a couple steps of reasoning ahead! But I didn't think of it myself in time. Kinda blocked
     * the thought process, really, didn't even brainstorm, just said "I don't see it" in a couple mins, looked at it
     * as if blindfolded to begin with. Didn't even try after the accept / no explicit follow-up, wow.
     */
    fun efficient(text: String): String {
        val result = StringBuilder()
        var currentWordStart = 0
        for (i in 0..text.length) {
            if (i != text.length && text[i] != ' ') continue

            for (j in (i - 1) downTo currentWordStart) result.append(text[j])
            if (i == text.length) break
            result.append(' ')
            currentWordStart = i + 1
        }
        return result.toString()
    }
}
