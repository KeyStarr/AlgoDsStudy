package com.keystarr.algorithm.array.twopointers

/**
 * LC-392 https://leetcode.com/problems/is-subsequence/description/
 * constraints:
 *  • 0 <= s.length <= 100;
 *  • 0 <= t.length <= 10^4;
 *  • s and t - only lowercase English letters;
 *  • no explicit time/space.
 *
 * Final note - done an efficient solution by myself [efficient] in 5 mins.
 */
class IsSubsequence {

    /**
     * Tools: Two Pointers.
     *
     * Time: worst/average O(t)
     * Space: always O(1)
     */
    fun efficient(potentialSub: String, original: String): Boolean {
        var subInd = 0
        var originalInd = 0

        while (subInd < potentialSub.length && originalInd < original.length) {
            if (potentialSub[subInd] == original[originalInd]) subInd++
            originalInd++
        }

        return subInd == potentialSub.length
    }
}
