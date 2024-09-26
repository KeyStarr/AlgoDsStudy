package com.keystarr.algorithm.dp.other.onedim

/**
 * LC-300 https://leetcode.com/problems/longest-increasing-subsequence/description/
 * difficulty: medium
 *
 * Final notes:
 *  • 🔥💡super funny how I "failed" to find the DP solution, decided to find a brute-force then first and basically
 *   designed [bottomUpDp] without realizing that it is DP :D Went to lookup the solution with better time complexity
 *   and got confused seeing my very solution labeled as DP. Now I clearly see though that it is DP, since we solve
 *   subproblems continuously aggregating them into bigger subproblems, and reuse solutions for smaller ones to get
 *   at the bigger ones + each choice we make indeed affects further choices;
 *  • WHY does the course's author and one of the top solutions generally recommend to start solving any DP problem
 *   top-down, and I CONTINUOUSLY (here especially!) find it easier to both start and implement bottom-up? Like here
 *   it's even less code and far less confusing in terms of the recursion callstack for me.
 *
 * Value gained:
 *  • consciously solved the first DP problem ever with the variable amount of calls in the recurrence equation, yay 🔥
 *  • practiced both bottom-up and top-down DP approaches.
 */
class LongestIncreasingSubsequence {

    private val listToRightIndMap = mutableMapOf<Int, Int>()

    /**
     * The note is visual here, in Obsidian. Can't optimize to O(k) space since here at most we need O(n-1) results in cache
     * and that number varies.
     *
     * Edge cases: STRICTLY increasing. So strictly less is the only valid previous number for the current number.
     *
     * Time: always O(n^2)
     * Space: always O(n)
     */
    fun bottomUpDp(nums: IntArray): Int {
        // only the lis length since the last element in the lis[i] must be nums[i]
        val lisForInd = IntArray(size = nums.size)
        var maxLis = Int.MIN_VALUE
        nums.forEachIndexed { i, numToAdd ->
            var currentLis = 0
            for (j in 0 until i) {
                if (nums[j] < numToAdd && lisForInd[j] > currentLis) currentLis = lisForInd[j]
            }
            lisForInd[i] = ++currentLis // count the number itself as added
            if (currentLis > maxLis) maxLis = currentLis
        }
        return maxLis
    }

    /**
     * Same logic as [bottomUpDp] - for each call, for all subarrays [0, where the nums\[rightInd] < if current number],
     * "find" THE LIS, find the longest among them, and add the current number to it. Edge case - there is no previous
     * number with which the current number forms a subsequence => just 1 then.
     * =>
     * to avoid redundant computations, as always, use memoization and save the LIS for each number into a HashMap.
     * we could use IntArray(size=2500), but I wager the map would be a better choice generally due to how much space we'd
     * waste on smaller input sizes.
     *
     * Same complexities as [bottomUpDp]:
     *  Time: O(n^2)
     *  Space: O(n) even with the callstack, obv.
     */
    fun topDownDp(nums: IntArray): Int {
        var maxLisLength = 0
        for (i in nums.size - 1 downTo 0) {
            val lisLength = topDownDp(rightInd = i, nums)
            if (lisLength > maxLisLength) maxLisLength = lisLength
        }
        return maxLisLength
    }

    private fun topDownDp(rightInd: Int, nums: IntArray): Int {
        if (rightInd == 0) return 1

        val numberToAdd = nums[rightInd]
        var maxLisLength = 0
        for (prevRightInd in (rightInd - 1) downTo 0) {
            if (nums[prevRightInd] >= numberToAdd) continue // cant form a valid subsequence

            val prevLisLength = listToRightIndMap[prevRightInd] ?: topDownDp(prevRightInd, nums)
            listToRightIndMap[prevRightInd] = prevLisLength
            if (prevLisLength > maxLisLength) maxLisLength = prevLisLength
        }
        return maxLisLength + 1
    }
}

fun main() {
    println(
        LongestIncreasingSubsequence().topDownDp(intArrayOf(1, 3, 6, 7, 9, 4, 10, 5, 6))
    )
}
