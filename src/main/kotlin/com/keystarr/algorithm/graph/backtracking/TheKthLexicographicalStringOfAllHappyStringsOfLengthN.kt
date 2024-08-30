package com.keystarr.algorithm.graph.backtracking

/**
 * ⭐️ a great example of an "ambiguous luring efficient solution" type problem, where one should restrain themselves
 *  to brute first.
 *
 * LC-1415 https://leetcode.com/problems/the-k-th-lexicographical-string-of-all-happy-strings-of-length-n/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= targetLength <= 10;
 *  • 1 <= targetStringPos <= 100.
 *
 * Final notes:
 *  • done [suboptimal] by myself in 25 mins;
 *  • I felt there was a faster solution then backtracking, but given the constraints and past experiences with some problems, khm,
 *   having no reasonable (doable during an interview) solution but brute-force, even though seeming to have one, I went
 *   ahead and done brute backtracking first. And I'm really happy I did! Turns out indeed there are more efficient solutions,
 *   but these are not reasonable to do during an interview, not for my current skill cap anyway.
 *   🔥 IM LEARNING!
 *  • I feel that (my projection + solutions section) backtracking is what would be expected for "OK" pass during an interview
 *   for this problem, but more efficient solutions maybe would be expected to be at least pondered with / explained in outline ❓
 *
 * Value gained:
 *  • practiced solving a "return the kth generated combination" type problem using backtracking for asymptotically suboptimal,
 *   but live-interview optimal solution;
 *  • reinforced the idea of doing a brute force first in questions with ambiguous (but potentially present) faster solutions!
 *   cause some more efficient solutions in these cases require too much work for a live interview setting for my current goals,
 *   and some won't even have one at all.
 */
class TheKthLexicographicalStringOfAllHappyStringsOfLengthN {

    private val validChars = "abc"
    private var targetLength = 0
    private var targetPos = 0
    private var resultsCounter = 0

    // TODO: consider learning/trying O(n*k) time DFS or even O(n) math
    //  https://leetcode.com/problems/the-k-th-lexicographical-string-of-all-happy-strings-of-length-n/solutions/585590/c-java-dfs-and-math/

    /**
     * trivial approach - generate all happy strings of length n in a lexicographically sorted order, and either return
     *  the kth string along the way or "" if the result size turned out to be smaller.
     *
     * problem rephrase:
     *  Goal: return the [targetStringPos]th valid string of all possible valid strings of [targetLength], when ordered
     *   lexicographically ascending.
     *   valid:
     *    1. only 'a' 'b' 'c';
     *    2. no two adjacent characters are same
     *
     * generate all of something => try backtracking (also, n <= 10 gives it away here)
     *
     * let's do without early return first, and optimize then
     *
     * Time: always O(g^tL)
     *  - for each position except first we have (g-1) options, that is all [validChars] except the last one in the string,
     *   so g=validChars.size. In this problem g=2;
     *  - for [targetLength] we have exactly g * ((g-1)^(tL-1)) result strings, the generation would take O(g^targetLength) time.
     * Space: always O(g^tL)
     *  - worst height of callstack is always targetLength.
     *  - results list is as described above.
     *
     * ------------- optimization --------------
     *
     * can we remove the need to keep track of all results in a list?
     * => just use a counter
     *
     * Time: average/worst O(g^tL)
     *  - now we can do early return as we encounter the target string, best is its the first string we've generated,
     *   worst is the target pos is greater than the amount of strings we can generate => we have to generate all.
     * Space: always O(targetLength)
     *  - only for the callstack height.
     */
    fun suboptimal(targetLength: Int, targetStringPos: Int): String {
        this.targetLength = targetLength
        this.targetPos = targetStringPos
        return backtracking(StringBuilder()) ?: ""
    }

    private fun backtracking(current: StringBuilder): String? {
        if (current.length == targetLength) {
            resultsCounter++
            return if (resultsCounter == targetPos) current.toString() else null
        }

        validChars.forEach { candidate ->
            if (current.isNotEmpty() && candidate == current.last()) return@forEach

            current.append(candidate)
            val result = backtracking(current)
            if (result != null) return result
            current.deleteAt(current.length - 1)
        }

        return null
    }
}
