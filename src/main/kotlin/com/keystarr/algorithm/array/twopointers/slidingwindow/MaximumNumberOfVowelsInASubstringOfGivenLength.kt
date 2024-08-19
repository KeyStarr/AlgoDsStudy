package com.keystarr.algorithm.array.twopointers.slidingwindow

/**
 * LC-1456 https://leetcode.com/problems/maximum-number-of-vowels-in-a-substring-of-given-length/
 * difficulty: medium
 * constraints:
 *  • 1 <= text.length <= 10^5;
 *  • text consists of lowercase English;
 *  • 1 <= subarrayLength <= text.length.
 *
 * Final notes:
 *  • solved via [efficient] by myself in 15 mins;
 *  • failed 1st submit cause didn't read the problem statement fully, ignoring that 'y' isn't regarded as a vowel O_o;
 *  • failed 2nd submit!! ⚠️ cause bounded right index by (until text.length-subarrayLength), which is a boundary FOR THE LEFT
 *   INDEX NOT THE RIGHT INDEX :D
 *
 * Value gained:
 *  • practiced recognizing and solving efficiently a problem on "best valid subarray" with a fixed length sliding window.
 */
class MaximumNumberOfVowelsInASubstringOfGivenLength {

    private val vowelSet = setOf('a', 'e', 'u', 'i', 'o')

    /**
     * problem rephrase:
     *  Goal: find the best valid subarray, return the number of values in it.
     *   valid = of length [subarrayLength]
     *   best = max number of vowels
     *
     * best valid subarray => try sliding window. valid constraint = length => try fixed sliding window.
     *  shrink: decrease vowel counter on a vowel
     *  expand: increase vowel counter on a vowel
     *  remember the max counter, return
     *
     * edge cases:
     *  - text.length == 1 && subarrayLength == 1 => skip second loop, first one determines the answer => correct
     *
     * Time: always O(n-k)
     * Space: O(1)
     */
    fun efficient(text: String, subarrayLength: Int): Int {
        var vowelCounter = 0
        for (ind in 0 until subarrayLength) if (vowelSet.contains(text[ind])) vowelCounter++

        var maxVowels = vowelCounter
        for ((left, right) in (subarrayLength until text.length).withIndex()) {
            if (vowelSet.contains(text[left])) vowelCounter--
            if (vowelSet.contains(text[right])) vowelCounter++
            if (vowelCounter > maxVowels) maxVowels = vowelCounter
        }

        return maxVowels
    }
}
