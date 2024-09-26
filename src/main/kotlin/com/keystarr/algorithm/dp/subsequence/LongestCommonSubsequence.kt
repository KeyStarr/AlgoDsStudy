package com.keystarr.algorithm.dp.subsequence

import kotlin.math.max

/**
 * 1st encounter, 2nd is [LongestCommonSubsequence2].
 *
 * ⭐️ a very general basic problem beautifully solved with efficient DP.
 * LC-1143 https://leetcode.com/problems/longest-common-subsequence/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= str1.length, str2.length <= 10^3
 *  • str1 and str2 consist of only lowercase English chars
 *
 * Final notes:
 *  • 💡⚠️ tried solving myself, but failed [bruteforce], too slow. O(n^2*m^2) VS O(n*m) time required, way too slow.
 *   so I did inefficient DP, huh? Is it even DP? Well yea, we make 1 step each time of all possible valid combination, right?
 *   AHH I actually just did a bruteforce with optimized const yea)))))
 *  • actual DP [topDownDp] is way more efficient cause we don't make that many calls. Why though? Still don't have it
 *   at my fingertips, like, what EXACTLY is different from the [bruteforce] approach?
 *  • ⚠️ failed 5 submission. New anti-record, huh)) Both anti-records are with DP;
 *  • ⚠️forgot memoization before submitting again, LOL;
 *  • the idea behind the efficient bottom-up DP here is simple: start with the valid subproblem of minimum size
 *   (1 last char of each string), solve it (depends on next chars, which don't exist here so its 0 for them => that initial
 *   subproblem is solvable as-is. basically these base cases are in the result array prefilled (0)) => increase the subproblem
 *   size based on reason. If the chars at current positions in both strings match => find LCE for the subproblem with skipping
 *   these both chars, like, what else can we use to get LCE + this once char? If chars don't match, try both valid steps
 *   and choose the max => either skip the char in the first string, or in the second. Since we're going right to left,
 *   we know already all answers to subproblems without the chars we're skipping
 *   => at each step we're deciding whether to add +1 char to the LCE if chars match, and adding that to the LCE of all
 *   next characters, gradually combining the answer for the full original subproblem = both full strings.
 *  • is it even worth it to dry-run entire cases for DP? I do that for like the 3rd or the 4th problem in a row, but
 *   I feel like I'm expecting an insight, but either it is so subtle I almost don't notice or it's not happening. I rather
 *   enjoy the contrast of monotonic labor with these, but I feel like it's not worth the time investment. Though here
 *   before dry-running the bottom-up approach I didn't clearly see exactly why the answer is in results[0][0] and now I
 *   clearly do (cause it's accumulated result for the full problem).
 *
 * Value gained:
 *  • 🔥first-time solved a multidimensional (2D) DP problem, good boy;
 *  • practiced DP top-down left-to-right and bottom-up right-to-left;
 *  • 🔥⭐️ realized that I may be thinking I'm doing DP but actually doing brute-force. basically elements^2 time (elements=n*m)
 *   certainly hints at that! maybe just slightly with the optimized const;
 *  • REMEMBER TO MEMOIZE BEFORE SUBMITTING!!!!!!!!!!!!!!!!!!!!!!!!
 *  • full dry-running DP so far actually seems to worth it for now.
 */
class LongestCommonSubsequence {

    /**
     * WRONG
     * TLE on 45 out of 47 tests
     * just as with [CoinChange] and [SolvingQuestionsWithBrainpower], keep this for retrospection.
     *
     * -----------------------------------
     *
     * what approach?
     *  - goal: best (max) valid combination (common subsequence in both strings)
     *  - multiple choices, each choice affects further ones (chosen char X in str1, gotta find exactly X in str2 too now)
     *  - possible duplicate subproblems, cause same char may be selected as a part of the subsequence in both strings with
     *   different preceding chars (in different subsequences)
     * => try DP
     *
     * why only lowercase English char? Can we leverage that?
     *
     * top-down
     * - return value: longest common subsequence length
     * - what do we do for 1 step?
     *  choose the same character in both str1 and str2, at the index of the common substring
     * - how do we reduce the problem?
     *  in str1 we consider further choices only starting from the index i
     *  in str2 from the index j
     * - how many options do we have at each step?
     *  maxSubsequenceLength = subsequenceLength
     *  iterate through str1 starting from i, for each char
     *   iterate through str2 and try to find it (could be optimized),
     *    if it exists, call dp(i=str1Ind+1,j=str2Ind+1)
     *    for each such case
     *    if the result is greater than maxSubsequenceLength, write into it
     *  return maxSubsequenceLength + 1
     * => recurrence relation: dp(i,j) = max(dp(i=str1Ind+1,j=str2Ind+1)) + 1
     *  also interesting, for i there might be different j, and for j different i, meaning same char in one string may
     *  match in a subsequence with the same char but on different positions in the other string
     *  => 2D DP
     *
     * - base cases?
     *  i>str1Ind or j>str2Ind => return 0
     *
     * - how to cache?
     *  the result of the call dp(i=X, j=Y) depends on both X and Y => since max X and Y is only worst 1000 => try a 2D array
     *  with worst 10^6 total elements. Array<IntArray>(size=str1.length) { IntArray(size = str2.length) }, where the first index is i and the
     *  second is j.
     *  note that str1.length may be != str.length
     *
     * edge cases:
     *  - no common subsequence with length >0 => return 0, cause "" is always a common valid subsequence
     *
     * Time: average/worst O(n^2*m^2)
     *  - worst we have both str1 and str2 at max length=10^3 and each consisting of the same single character
     *   => there will be 10^3*10^3=10^6 total possible valid subsequences
     *   so, on average, total states count, each we compute only once due to caching would be O(n*m),
     *   where n=str1.length and m=str2.length
     * - for each state we run i through (i..str1.length-1) chars, and each run we run through (j..str2.length-1) chars
     *  => the work for each state (making calls to dp and finding max CS) takes O(n*m) again
     * Space: always O(n*m)
     *  - n*m for caching
     *  - worst callstack height = longest possible subsequence = min(n,m)
     *
     * -----------------
     *
     * can we improve the work at the single state?
     *  how to find efficiently for str1\[newI] an index newJ such that str1\[newI]==str2\[newJ], with newJ starting from j?
     *  could pre-process str2 into a Map<Char,List<Int>> where the value is the list of all indices of the given (key) char.
     *  could also keep the list sorted for binary search => with the added cost of O(str2.length) time and O(str2.length) space
     *  reduce the work at each state to O(str1*log(str2.length)), where str2.length in reality would be much less due to
     *  the binary search being performed only through the indices of the given character, and starting from the index j.
     * => total time then O(n*m*n*log(m)=O(n^2*m*logm)
     *  total space stays O(n*m)
     *
     * (implemented only partially without binary search, so, technically, only improved the original const)
     */
    fun bruteforce(str1: String, str2: String): Int =
        bruteforce(
            i = 0,
            j = 0,
            cache = Array(size = str1.length) { IntArray(size = str2.length) { -1 } },
            str1 = str1,
            str2 = str2.charsToIndices(),
            str2Length = str2.length,
        ) - 1

    private fun String.charsToIndices(): Map<Char, List<Int>> {
        val map = mutableMapOf<Char, MutableList<Int>>()
        forEachIndexed { ind, char -> map.getOrPut(char) { mutableListOf() }.add(ind) }
        return map
    }

    private fun bruteforce(
        i: Int,
        j: Int,
        cache: Array<IntArray>,
        str1: String,
        str2: Map<Char, List<Int>>,
        str2Length: Int,
    ): Int {
        if (i >= str1.length || j >= str2Length) return 1

        val cachedResult = cache[i][j]
        if (cachedResult != -1) return cachedResult

        var maxSubsequenceLength = 0
        for (newI in i until str1.length) {
            val char = str1[newI]
            val str2Indices = str2[char] ?: continue
            for (str2Ind in str2Indices) {
                if (str2Ind < j) continue

                val result = bruteforce(
                    i = newI + 1,
                    j = str2Ind + 1,
                    cache, str1, str2, str2Length,
                )
                if (result > maxSubsequenceLength) maxSubsequenceLength = result
                break
            }
        }
        return (maxSubsequenceLength + 1).also { cache[i][j] = it }
    }

    /**
     * - what is a step?
     *  unlike [bruteforce], try simply considering str1\[i] and str2\[j], and move only 1 character at a time:
     *   - if str1\[i] == str2\[j] => return dp(i+1,j+1) + 1
     *    i.o. discard both characters in a single move and increase the result subsequence by 1
     *   - otherwise try separately discarding str1\[i] and str2\[j]
     *
     * - how do we reduce the problem?
     *  by discarding 1 char for both strings, either at the same time, or individually
     *
     * - how many options at 1 step, how to choose?
     *  if chars don't match, we have either dp(i+1,j) or dp(i,j+1) to contribute to the answer. since we're asked for
     *  the max LCS => choose max between both
     *
     * => recurrence relation:
     *  if (str1\[i] == str2\[j]) return dp(i+1,j+1) + 1
     *  else return max( dp(i+1,j), dp(i,j+1)
     *
     * - base cases?
     *  since we're starting i and j from 0 and increasing each always by 1 at a single step =>
     *  i == str1.length || j == str2.length
     *
     * - memoization?
     *  the sub-problem is defined via both i and j, and both worst lengths are 1000 => try a 2D array of size 1000*1000
     *
     * Edge cases:
     *  - both strings have no common subsequence => return 0, cause '' is always a valid common subsequence => correct
     *
     * Time: O(n*m)
     *  - if chars match, we make only 1 recurrent call, otherwise 2 calls. Worst amount of calls is when both strings
     *   have no LCE with length >0 => we try n*m states, where n=str1.length and m=str2.length
     *  - work done at each state is O(1)
     * Space: O(n*m)
     *  - cache is n*m
     *  - worst callstack height is m or n
     */
    fun topDownDp(str1: String, str2: String): Int = topDownDp(
        i = 0,
        j = 0,
        cache = Array(size = str1.length) { IntArray(size = str2.length) { -1 } },
        str1 = str1,
        str2 = str2,
    )

    private fun topDownDp(
        i: Int,
        j: Int,
        cache: Array<IntArray>,
        str1: String,
        str2: String,
    ): Int {
        if (i == str1.length || j == str2.length) return 0

        val cachedResult = cache[i][j]
        if (cachedResult != -1) return cachedResult

        val maxCommonSubsequence = if (str1[i] == str2[j]) {
            topDownDp(i + 1, j + 1, cache, str1, str2) + 1
        } else {
            max(topDownDp(i + 1, j, cache, str1, str2), topDownDp(i, j + 1, cache, str1, str2))
        }
        cache[i][j] = maxCommonSubsequence
        return maxCommonSubsequence
    }

    fun bottomUp(str1: String, str2: String): Int {
        val results = Array(size = str1.length + 1) { IntArray(size = str2.length + 1) }
        for (i in str1.length - 1 downTo 0) {
            for (j in str2.length - 1 downTo 0) {
                val newI = i + 1
                val newJ = j + 1
                if (str1[i] == str2[j]) {
                    results[i][j] = results[newI][newJ] + 1
                } else {
                    results[i][j] = max(results[newI][j], results[i][newJ])
                }
            }
        }
        return results[0][0]
    }
}

/**
 * Context: failed to solve [UncrossedLines], learned that it is fundamentally the same problem as this one and tried to
 *  first re-solve this one (~2 months after the first time I solved it).
 *
 * Final notes:
 *  • ⚠️ failed to solve it under ~1h AGAIN! AGAIN made the SAME mistake of going too literal on DP and designing an inefficient
 *   DP algorithm [dpSlow]:
 *
 *    what I got right:
 *     • realized that problem's efficient solution is DP since the subproblems may overlap.
 *
 *    where I went wrong:
 *     • came up with an inherently slow DP approach, cause we had to look for matches of the same characters many-many times
 *      without caching.
 *
 *  • ⚠️ AGAIN had a hard time understanding [dpEfficient], had to full dry-run it and take a break for the design to settle in;
 *  • well, now I'm sure that [dpSlow] is actually DP but just super slow, and I'm pretty sure I understand exactly how [dpEfficient]
 *   works deeply as I've set my mind on these rails the 2nd time;
 *  • 🔥 when will I learn??)))) What does this approach feel like black magic still?
 *   ⚠️ did I not retain that other approach cause I had trouble the first time and didn't reinforce it with a re-solve
 *    after some time???? Or just didn't go deep enough into understanding it?
 *
 * Value gained:
 *  • practiced solving a tricky multi-dim DP problem via an efficient bottom-up solution;
 *  • learned that, apparently, if I don't reinforce a failed problem via a re-solve after in some close timeframe
 *   => I fail to retain the approach. Or maybe that I didn't dig deep enough into it the first time 🤔
 *
 * did I not understand it deep enough? how to tell?
 */
class LongestCommonSubsequence2 {

    // TODO: full re-solve in 1-2 weeks MANDATORY (or do u want the 3rd fail????))

    /**
     * Also note that actually for every ith row we only use the cache for the (i+1)th row => we can reduce the space
     * from O(n*m) to O(m)
     *
     * Time: always O(n*m)
     * Space: always O(m)
     */
    fun dpEfficient(text1: String, text2: String): Int {
        val rowSize = text2.length + 1
        var prevRow = IntArray(size = rowSize)
        var currentRow = IntArray(size = rowSize)
        for (i in text1.lastIndex downTo 0) {
            for (j in text2.lastIndex downTo 0) {
                currentRow[j] = if (text1[i] == text2[j]) {
                    prevRow[j + 1] + 1
                } else {
                    max(prevRow[j], currentRow[j + 1])
                }
            }
            val temp = prevRow
            prevRow = currentRow
            currentRow = temp
        }
        return prevRow[0]
    }

    /**
     * ⚠️ gave up after 50m (being very tired also) => checked my July solution [LongestCommonSubsequence] => caught the "a-ha!" moment. Again. Lol.
     *
     * -------------
     *
     * leave the goal the same: return the length of the longest common subsequence of text1\[start1:] and text\[start2:]
     * BUT change the recurrence relation:
     *  dp(i,j) = if (text1\[i] == text2\[j]) dp(i+1,j+1) + 1   else max(dp(i+1,j), dp(i,j+1)
     *
     * motivation is very simple:
     *  1. if letters match, we always take them into the subsequence (since we maximize its length) => we can no longer
     *   consider both letters => move both pointers next;
     *
     *   (we always take the earliest match for some letter k cause it guarantees the max available further choices. and if we
     *   have duplicates of k further, then taking any reduces choices without any benefit)
     *
     *  2. if letters don't match => try moving either pointer. that way we will consider all possibilities for finding a match
     *   for text1[i+1] and text2[j+1] and the next.
     *
     * Time: always O(n*m)
     *  - n*m states;
     *  - O(1) work for each state.
     * Space: always O(n*m)
     *
     * go for the bottom-up straight-away
     */
    fun dpSuboptimal(text1: String, text2: String): Int {
        val cache = Array(size = text1.length + 1) { IntArray(size = text2.length + 1) }
        for (i in text1.lastIndex downTo 0) {
            for (j in text2.lastIndex downTo 0) {
                cache[i][j] = if (text1[i] == text2[j]) {
                    cache[i + 1][j + 1] + 1
                } else {
                    max(
                        cache[i + 1][j],
                        cache[i][j + 1],
                    )
                }
            }
        }
        return cache[0][0]
    }

    /**
     * wrong design
     *
     * ----
     *
     * can we do O(n*m) time dp?
     *
     * change the goal?
     *  new goal: return the length of hte LCS for text1\[start1], text2\[start2] such that it must start with text1\[start1].
     *
     * - work for a single state:
     *  - find the first letter in text2[start2:] == text1\[start1], say with index j. Only the first, cause if further in text2
     *   there are more text1\[start1], say one is at the index g > k, then we loose access to all elements text2[k+1:g)
     *   yet if we chose j then we'd have access to all elements text2[g+1:] => greedily choose the first occurrence
     *   to be efficient and preserve the max amount of further options.
     * - select the max LCS among all starting positions in [text1] since, say, we could start with start1=0th and
     *  have an LCS of only length 1, but start at 1th and have a better result, e.g.:
     *    abcde
     *    bcdea
     *
     * Time: O(n*m^2)
     *
     */
    fun dpWrong(text1: String, text2: String): Int {
        val cache = Array(size = text1.length + 1) { IntArray(size = text2.length + 1) }
        var totalMaxLength = 0
        for (start1 in text1.lastIndex downTo 0) {
            for (start2 in text2.lastIndex downTo 0) {
                for (j in start2 until text2.length) {
                    if (text1[start1] != text2[j]) continue
                    cache[start1][start2] = cache[start1 + 1][j + 1] + 1
                    totalMaxLength = max(cache[start1][start2], totalMaxLength)
                    break
                }
            }
        }
        return totalMaxLength
    }

    /**
     * we try starting a subsequence from each matching letter => try all matching for the next etc
     * DP start1, start2
     *
     * dp goal: return the longest subsequence length for strings text1\[start1:], text2\[start2:]
     *
     * Time: O(n^2 * m^2)
     *  - n*m states;
     *  - for each state we do ~n*m work.
     * => TLE 42th/47
     */
    fun dpSlow(text1: String, text2: String): Int {
        val cache = Array(size = text1.length + 1) { IntArray(size = text2.length + 1) }
        for (start1 in text1.lastIndex downTo 0) {
            for (start2 in text2.lastIndex downTo 0) {
                var maxLength = 0
                for (i in start1 until text1.length) {
                    for (j in start2 until text2.length) {
                        if (text1[i] != text2[j]) continue
                        maxLength = max(cache[i + 1][j + 1] + 1, maxLength)
                        break
                    }
                }
                cache[start1][start2] = maxLength
            }
        }
        return cache[0][0]
    }
}

fun main() {
    println(
        LongestCommonSubsequence().bottomUp(
            str1 = "abcdefghij",
            str2 = "ace",
        )
    )
}
