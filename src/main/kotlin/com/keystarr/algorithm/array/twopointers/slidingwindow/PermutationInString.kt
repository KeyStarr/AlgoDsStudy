package com.keystarr.algorithm.array.twopointers.slidingwindow

/**
 * 💣 failed to recognize the sliding window for an efficient solution in 30 mins, but did the suboptimal one
 * LC-567 https://leetcode.com/problems/permutation-in-string/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= s1.length, s2.length <= 10^4
 *  • s1 and s2 consists of lowercase English only
 *
 * Final notes:
 *  • done [traversal] by myself in about 40 mins across two sessions (summed up to that time approximately);
 *  • done [efficient] after reading the idea from Editorial. Come to think of it, should've abstracted the problem to
 *   "return true if s2 contains a valid subarray" => then sliding window would've come to mind faster.
 *   ⚠️ didn't think of sliding window at all;
 *  • I predicted well that there probably would be a better solution, and it turned out to be exactly O(s1.len + s2.len).
 *   Sliding window makes linear indeed, huh.
 *
 * Value gained:
 *  • practiced solving a "check if array contains a valid subarray" efficiently using a sliding window + counting.
 */
class PermutationInString {

    // TODO: reinforce in a week or two

    /**
     * Goal: return true if one of s1 permutations is a substring of s2.
     *
     * substring=subarray
     *
     * idea #1 multi-start "DFS"
     *  - traverse through s2 and find indices of all letters that are contained in s1 = find starting points;
     *  - launch dfs from all of these starting points, each with a "lettersLefts" frequencies state minus the first letter already;
     *   - if we encounter a letter that isn't one of the remaining ones => return false (locally);
     *   - if there are no more letters left => return true (globally).
     *
     * Edge cases:
     *  - s1.length == s2.length == 1 => if same letter, then a single iteration, no inner loop, and true, correct;
     *  - s2.length < s1.length => always early return false.
     *
     * Time: average/worst O(s2.length * s1.length)
     *  - frequencies count for s1 => O(s1.length)
     *  - worst case both s1 and s2 consists of a single letter repeated multiple times => each letter of s2 would be the
     *   starting point, and we'd make on average full s1.length moves forward, so total s2.length * s1.length moves;
     * Space: O(s2.length)
     *  - frequencies array for s1 O(26);
     *  - starting indices list, worst case all letters in s2 so O(s2.length)
     *
     * -------------------------------------------
     *
     * Can we do time O(s2.length + s1.length)? Or at least O(log(s2.length)*s1.length)?
     */
    fun traversal(s1: String, s2: String): Boolean {
        if (s2.length < s1.length) return false

        val startingIndices = mutableListOf<Int>()
        val s1Frequencies = s1.countFrequencies()
        s2.forEachIndexed { ind, letter ->
            val key = letter.toKey()
            if (s1Frequencies[key] > 0) startingIndices.add(ind)
        }

        startingIndices.forEach { startInd ->
            var currentInd = startInd
            val frequenciesLeft = s1Frequencies.clone()
            while (currentInd < s2.length && (currentInd - startInd) < s1.length) {
                val letter = s2[currentInd]
                val key = letter.toKey()
                if (frequenciesLeft[key] > 0) {
                    frequenciesLeft[key]--
                    currentInd++
                } else {
                    break
                }
            }
            if (currentInd - startInd == s1.length) return true
        }

        return false
    }

    /**
     * Observation #1: a substring X of s2 is a permutation of s1 only if frequencies of all letters in X and s1 match exactly.
     * Observation #2: for #1 to be true, it must always be that X.length == s1.length
     *
     * => try sliding window of size s1.length through s2? with letter frequencies updating on the fly:
     *  - expand when leftFrequencies\[s2Letter.int]>0, and decrease that freq
     *  - shrink when leftFrequencies\[key]==0 => two cases:
     *   1. no letter was taken into the window yet, so left == right => do nothing;
     *   2. left < right => we need to "put back" the frequencies of the taken letters.
     *
     *  - if we've just expanded and right-left == s1.length => return true.
     *
     * Time: O(s1.length + s2.length)
     *  - s1 frequency counting is O(s1.length);
     *  - worst amount of iterations is s2.length, if there's no valid substring, or it ends with the very last letter;
     *  - each iteration costs worst O(k), where k=unique letters count, in this problem k=26, so O(26).
     * Space: O(1)
     *  - s1 frequencies array O(k)=O(26)
     *
     *  aabc
     *  aaaabc
     *
     *  eytdusda
     *  asd
     *
     *  left=0
     *  right=0
     *  lf\[e]==0 && 0 <= 0
     *   taken == false => skip
     *   left=1
     *
     *  l=1
     *  r=1
     *  lf\[y]==0 && 1 <= 1
     *      false -> skip
     *      left=2
     *
     * l=3
     * r=3
     * lf\[d]!=0 => skip
     * lf\[d]>0 =>
     *  lf\[d]=0
     *  r-l + 1=1 !=3
     *
     * l=3
     * r=4
     * lf\[u] ==0 && 3 <= 4
     *   lf['d']=1
     *   left=4
     * lf\[u] ==0 && 4 <= 4
     *   4==4 => skip
     *   left=5
     * lf\[u] == 0 => skip
     *
     * l=5
     * r=5
     */
    fun efficient(s1: String, s2: String): Boolean {
        if (s2.length < s1.length) return false

        var left = 0
        val leftFreq = s1.countFrequencies()
        s2.forEachIndexed { right, letter ->
            val key = letter.toKey()
            // enter the loop for left == right too, since if leftFreq[key]==0 then this letter can't be a part of the
            // valid substring => skip it. but if left==right, the s2[right] is always the new letter and wasn't added yet
            // => don't increase the frequency
            while (leftFreq[key] == 0 && left <= right) {
                if (left != right) leftFreq[s2[left] - 'a']++
                left++
            }
            if (leftFreq[key] > 0) {
                leftFreq[key]--
                if (right - left + 1 == s1.length) return true
            }
        }
        return false
    }

    private fun String.countFrequencies(): IntArray {
        val frequencies = IntArray(size = 26)
        forEach {
            val key = it.toKey()
            frequencies[key] = frequencies[key] + 1
        }
        return frequencies
    }

    private fun Char.toKey() = this - 'a'
}

fun main() {
    println(
        PermutationInString().efficient(
            s1 = "adc",
            s2 = "dcda",
        )
    )
}
