package com.keystarr.algorithm.array.twopointers

/**
 * LC-2486 https://leetcode.com/problems/append-characters-to-string-to-make-subsequence/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= text.length, targetSubsequence.length <= 10^5;
 *  • text and targetSubsequence consist only of lowercase English.
 *
 * Final notes:
 *  • implemented [efficient] by myself in 12 mins, good;
 *  • ⚠️ felt not full confidence in the algorithm after designing it, it felt like choosing the first occurrence of target\[i] char
 *  in \[text] might not be always the robust choice => tried to prove it by reasoning, kind of did, but still feel it unsteadily.
 *  Why? Gonna leave it right now, have bigger priorities.
 *
 * Value gained:
 *  • practiced solving a string matching problem using two pointers.
 */
class AppendCharactersToStringToMakeSubsequence {

    /**
     * is it always possible to modify [text] such that to make [targetSubsequence] a subsequence of [text]?
     * yes, worst case is [text] contains no characters in [targetSubsequence] => we simply add the entire [targetSubsequence]
     * to the end of text.
     *
     * approach:
     *  - targetInd = 0
     *  - iterate through [text]:
     *   - if initial\[i] == target\[targetInd]:
     *    - targetInd++
     *    - if (targetInd == target.length) return 0 // [targetSubsequence] is the subsequence of text
     *  // we ran out of chars in [text], but haven't found all [targetSubsequence] characters => append all remaining ones
     *
     * text=aaabaabc
     * tS = abc
     *
     * may it ever be that by starting from the first occurrence of the first [targetSubsequence] char we make a wrong choice
     *  and lose a longer subsequence if we started at some further occurrence of it?
     *  never, because if we have first few chars of the [targetSubsequence] in order in [text] as a subsequence, then they
     *  repeat again, and only then we have the next char => we would count it even if we started with first occurrences.
     *
     * edge cases:
     *  - text.length == 1 && targetSubsequence.length == 1 => if equal return 0 else 1 => correct as-is.
     *
     * Time: average/worst O(n), n=text.length
     *  worst is when [targetSubsequence] is not a subsequence of [text] initially => we make n iterations exactly.
     * Space: always O(1)
     *
     * ----
     *
     * there is no faster solution, since we anyway have to check a proportional number of characters to [text].
     */
    fun efficient(text: String, targetSubsequence: String): Int {
        var targetInd = 0
        text.forEach { char ->
            if (char == targetSubsequence[targetInd]) {
                targetInd++
                if (targetInd == targetSubsequence.length) return 0
            }
        }

        return targetSubsequence.length - targetInd
    }
}
