package com.keystarr.algorithm.array.prefixsum

/**
 * LC-1732 https://leetcode.com/problems/find-the-highest-altitude/description/
 * difficulty: easy
 * constraints:
 *  • n == gain.length;
 *  • 1 <= n <= 100;
 *  • -100 <= gain\[i] <= 100.
 *
 * Final notes:
 *  • solved [efficient] by myself in 10 mins (should strive for 5 mins for these though!).
 *
 * Value gained:
 *  • practiced, repeated prefix-sum after awhile;
 *  • FAILED the first submission (shame on me!) cause ignored the "starts at 0 altitude" bit at first for some reason;
 *      => BE MORE ATTENTIVE especially when rephrasing the problem, double-check.
 *  • apparently, if the problem is to find the best valid subarray, but always from 0th index, we don't need the prefixSum
 *      array => just use the currentPrefixSum variable (cause then we don't need to check arbitrary subarrays sums by
 *      subtracting prefixsum at j and i!). Ive solved such problems before but haven't generalized.
 *  TODO: upd the prefix sum solution guidelines
 */
class FindTheHighestAltitude {

    /**
     * Problem rephrase:
     * "find the max sum of the subarray starting from 0th index"
     *
     * Hint: sum of the subarray => prefix sum
     *
     * Idea:
     *  - since the biker starts at altitude 0 => init maxSum=0;
     *  - iterate through the [gain]:
     *      - compute current prefixSum, save into the variable currentPrefixSum;
     *      - maxSum=max(currentPrefixSum,maxSum);
     *  - return maxSum
     *
     * Edge cases:
     *  - n==1 => return gain[0], correct.
     *
     * Time: O(n)
     * Space: O(1)
     */
    fun efficient(gain: IntArray): Int {
        var maxSum = 0
        var currentPrefixSum = 0
        gain.forEach { altitudeChange ->
            currentPrefixSum += altitudeChange
            if (currentPrefixSum > maxSum) maxSum = currentPrefixSum
        }
        return maxSum
    }
}
