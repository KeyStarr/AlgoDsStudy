package com.keystarr.algorithm.array.prefixsum

import kotlin.math.max

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

/**
 * Context: was an example in the course and also ended up the bonus problems list => resolved it just to check my skills.
 *
 * Done [efficient] by myself in 5 mins. 🔥 Heh, got the 5 mins I strived for in the [FindTheHighestAltitude] notes :D
 *
 * No real value gained, turns out I still remember this particular problem :D Reasoned from the ground up though, running sum is
 *  pretty obvious here. Though checking is valuable on itself.
 */
class FindTheHighestAltitude2 {

    /**
     * each element is a diff in altitude => do a running sum and record the maximum encountered
     *
     * Time: always O(n)
     * Space: always O(1)
     *
     * cant do faster since each point is a diff in altitude and further can be positive => we need to check all
     */
    fun efficient(gain: IntArray): Int {
        var currentSum = 0 // we start at the altitude == 0, gain[0] is the first point (diff) after it
        var maxAltitude = 0
        gain.forEach {
            currentSum += it
            maxAltitude = max(currentSum, maxAltitude)
        }
        return maxAltitude
    }
}
