package com.keystarr.algorithm.dp.multidim

import kotlin.math.max

/**
 * ⭐️ [LongestCommonSubsequence] and [UncrossedLines] are a stark example of the same problem described very differently
 * LC-1035 https://leetcode.com/problems/uncrossed-lines/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums1.length, nums2.length <= 500;
 *  • 1 <= nums1\[i], nums2\[j] <= 2000.
 *
 * Final notes:
 *  • ⚠️ done [slowTopDownDp] by myself in 45 mins, but gave up on finding a better solution (actually, just stopped there since it passed
 *   and there were no follow-ups in the problem description;
 *  • 🏅 WENT FOR BRUUUTE, forced myself past the fixation on the most optimal solution choices early on!!
 *   => did brute, checked that its too slow => drew via dry running and noticed that subproblems are actually overlapping
 *   and the main rule how to update i and j from which we can make matches still => tried DP, checked via numbers and
 *   estimated that it will pass based on the worst case
 *   🏅also stumbled on time estimation but forced myself from "i just dont see it" to "what would be the worst?" and simply
 *    went from there! got the time right => got the green flag for DP passing. Also estimated the time with the inner loop
 *    correctly.
 *
 *  • 🔥 basically the same problem as [LongestCommonSubsequence] but a different form/story and constraints.
 *   Unfortunately, even though I did catch that we used each element once and could use only further elements once the match
 *   was made => I failed to interpret the required combination as a "subsequence".
 *
 *   what I got right:
 *    - made 1st core observation: if we match nums1\[i] with nums2\[j] => we can only consider further matches from
 *     nums1\[i+1:] and nums2\[j+1:];
 *    - one of the solutions is DP since the problem can be decomposed and subproblems may overlap.
 *
 *   where I went wrong:
 *    - failed to recognize the goal combination of nums1 and nums2 elements as a subsequence;
 *   - went for the wrong DP approach, exactly how I did 2 freaking times in both [LongestCommonSubsequence] and [LongestCommonSubsequence2].
 *
 * Value gained:
 *  • practiced solving a doppelganger problem, story meaning just a masked longest subsequence type question: both with
 *   a space-optimized efficient bottom-up and another too slow dp approach.
 */
class UncrossedLines {

    // TODO: full re-solve in 1-2 weeks (mandatory)

    /**
     * Observe:
     *  1. if we draw a line from nums1\[i] to nums2\[j] => we may consider only drawing lines further from nums1[i+1:] nums2[j+1];
     *  2. we may use any element up to once (up to 1 line connecting to it);
     *  3. we may draw lines between not consecutive elements, but, really, any elements of the two arrays as long the
     *   intersection constraint isn't broken.
     *
     * => rephrase the goal: "return the length of the longest common SUBSEQUENCE"
     *  - after we pair elements we may only use further ones in both arrays;
     *  - each pairs adds up to the count;
     *  - we can pair any two equal elements in both arrays respecting previous choices.
     *
     * so basically its the same as [LongestCommonSubsequence]
     *
     * 2 5 1 2 5
     * 10 5 2 1 5 2
     *
     * Design - dp
     *
     * goal: return the longest common subsequence of nums1\[i:] and nums2[j:]
     * recurrence relation:
     *  dp(i,j) = if (nums1\[i] == nums2\[j]) dp(i+1,j+1) + 1       else max(dp(i+1,j), dp(i,j+1))
     *
     * Time: always O(n*m)
     * Space: always O(m)
     */
    fun efficient(nums1: IntArray, nums2: IntArray): Int {
        val rowSize = nums2.size + 1
        var prevRow = IntArray(size = rowSize)
        var currentRow = IntArray(size = rowSize)
        for (i in nums1.lastIndex downTo 0) {
            for (j in nums2.lastIndex downTo 0) {
                currentRow[j] = if (nums1[i] == nums2[j]) {
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
     * order matters => probably no sorting/otherwise reordering
     *
     * we need the max amount of connecting lines => when can we not draw a line, when do the lines intersect?
     *
     * we can't draw a line between numbers i and j if:
     *  - there already is a line to either of these numbers;
     *  - there is a line that either:
     *   - starts at i2 < i and ends at j2 > j;
     *   - or starts at j2 < j and ends at i2 > i.
     *
     * try greedy?
     *  tried quickly to reason - can't see a simple local choice to make such that it will always be optimal.
     *  since e.g. we might try "if there's nums2\[j] such that it == nums1\[i]" simply match i with the earliest j,
     *  but i can be = 0 and j = nums2.size-1 and we'd cross out all other options, and actually there may be a combination
     *  of more than one valid pair in between of these.
     *
     * so not greedy, try else
     *
     * ----------
     *
     * what would brute be?
     *
     * - across all possible options find max:
     *  - iterate through nums1:
     *   - iterate through nums2:
     *    - try to find the first not used number in nums2 such that it equals to num1:
     *     dfs(start1=i+1, start2=j+1);
     *
     * observation: maximum possible number of pairs is min(n,m) if one array is a subarray of another
     *  cause we can use each number at most once and the best line is the one that disables the min number of elements
     *  => the straight line when i==j => and we have min(n,m) straight lines then
     *
     * Time:
     *  worst is nums1==nums2 and consists of duplicates of a single number
     *  so we have a decision tree with width n and height n => O(n^n) though much less in practice, but 500^500 solution is never going to pass
     *
     *
     * (i=0,j=1) or (i=1, j=0) or (i=0, j=2)
     *
     *
     * 1  2  3
     *   /
     * 2  1  1
     *
     *
     * (1,0) (2,1) (3,2)
     * 1  2  3  4  5
     *   / /  /  /
     * 2  3  4  5
     *
     *
     * 1  2  3
     * |  |  |
     * 1  2  3
     *
     *
     * 1  2  3  4  5
     *      /    \  \
     * 2  3  1  5  4  5
     *
     * ----------------------------------------------
     *
     * DP? actually some subproblems overlap, and each subproblem is defined simply via startI and startJ
     *  => if we use memoization, then the maximum number of states to check would be n*m = 500*500=25 * 10^4 totally reasonable
     *
     * observation (based on drawing): the largest (i+1) and largest (j+1) such that i and j have lines connected to them are the
     *  min indices we may consider in respective arrays for making pairs (the earliest pair would be (i+1,j+1))
     *
     * top-down
     *
     * goal: return the maximum count of valid pairs we can make, integer
     *
     * input states:
     *  startI: Int - the minimum ith index of a number from nums1 that we may consider
     *  startJ: Int
     *
     * recurrence relation:
     *  dp(startI, startJ) = max(
     *                        for i in startI until nums1.size:
     *                          for j in startJ until nums2.size:
     *                           dp(i+1,j+1) + 1
     *                       )
     *
     * base cases:
     *  - i == nums1.size || j == nums2.size => return 0
     *
     *
     * Time: O(n^2 * m^2)
     *  - max is n*m states;
     *  - for each state we iterate the number of times that depends on n*m.
     *  so worst is we have 500^4 = 5 * 10^8, will pass
     * Space:
     *
     *
     * ------ optimization
     *
     * maybe we can even optimize this for the bottom-up?
     *
     * observation: if we have nums1\[startI]==nums2\[startJ] its always the best choice to make that pair and not consider
     *  any others, since it disables the least amount of further options => add early break
     * =>
     *
     * ACTUALLY, note that for every i we make a decision whether to make a pair with it or not.
     *
     * If we decide make a pair with the current 'i'
     *  => we always want to make a pair with the first valid j, since that always reduces further options minimally
     *  => break after the first nums1\[i] == nums2\[j]
     * If we decide to not make a pair with it, we might consider making a pair with the next i.
     *
     *
     * ---
     * can we optimize finding the earliest j such that nums1\[i] == nums2\[j]? since we do it a bunch of times
     *
     * we might simply preprocess Array<Int, List<Int>> i->sorted list of all j such that nums1\[i] == nums2\[j]
     * and then simply do binary search on that list, find the first j that is >= startJ
     *  then each iteration would cost n*logm
     *  => time would be O(n^2*m*logm)
     *
     */
    fun slowTopDownDp(nums1: IntArray, nums2: IntArray) {
        slowTopDownDpRecursive(
            startI = 0,
            startJ = 0,
            cache = Array(size = nums1.size) { IntArray(size = nums2.size) { EMPTY_CACHE_VALUE } },
            nums1 = nums1,
            nums2 = nums2,
        )
    }

    private fun slowTopDownDpRecursive(
        startI: Int,
        startJ: Int,
        cache: Array<IntArray>,
        nums1: IntArray,
        nums2: IntArray,
    ): Int {
        if (startI == nums1.size || startJ == nums2.size) return 0

        val cachedResult = cache[startI][startJ]
        if (cachedResult != EMPTY_CACHE_VALUE) return cachedResult

        var maxPairs = 0
        for (i in startI until nums1.size) {
            for (j in startJ until nums2.size) {
                if (nums1[i] != nums2[j]) continue

                maxPairs = max(
                    slowTopDownDpRecursive(
                        startI = i + 1,
                        startJ = j + 1,
                        cache = cache,
                        nums1 = nums1,
                        nums2 = nums2,
                    ) + 1,
                    maxPairs
                )
                break
            }
        }
        return maxPairs.also { result -> cache[startI][startJ] = result }
    }
}

private const val EMPTY_CACHE_VALUE = -1

fun main() {
    println(
        UncrossedLines().slowTopDownDp(
            nums1 = intArrayOf(1, 4, 2),
            nums2 = intArrayOf(1, 2, 4),
        )
    )
}
