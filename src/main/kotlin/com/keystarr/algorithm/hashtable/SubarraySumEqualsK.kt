package com.keystarr.algorithm.hashtable

/**
 * LC-560 https://leetcode.com/problems/subarray-sum-equals-k/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.length <= 2*10^4;
 *  • -1000 <= nums\[i] <= 1000;
 *  • -10^7 <= k <= 10^7;
 *  • no explicit time/space.
 */
class SubarraySumEqualsK {

    /**
     * Hints:
     *  - count the number of valid subarrays by sum => prefix sum;
     *  - strict validity constraint + negative numbers in the array => hashmap.
     *
     * Idea: calculate prefix sum on-the-fly, save it into a map for constant access and count all same ones.
     * Each iteration count all map[currentSum-targetSum] as valid subarrays.
     *
     * Edge cases:
     *  - since nums\[i] can be negative => we might have multiple identical prefix sums => use hashmap to store and O(1)
     *      access the amount of times this prefixSum was seen before (if it adds up targetSum with currentSum, then
     *      all these instances where it was seen are valid subarrays);
     *  - to include the first element into subarray sums calculation via subtraction add the first prefixSum as 0 (with count 1);
     *  - nums.length = 1 and nums[0]=k => thanks to map[0]=1 we would count this case as-is;
     *  - k<=0 => nothing changes, works as is.
     *
     *  Time: always O(n)
     *  Space: worst/average O(n), cause even all prefix sums may be distinct.
     */
    fun suboptimal(nums: IntArray, targetSum: Int): Int {
        val prefixSumCountsMap = mutableMapOf<Int, Int>()
        prefixSumCountsMap[0] = 1

        var currentSum = 0
        var validSubarraysCount = 0
        nums.forEach {
            currentSum += it
            validSubarraysCount += prefixSumCountsMap.getOrDefault(currentSum - targetSum, 0)
            prefixSumCountsMap[currentSum] = prefixSumCountsMap.getOrDefault(currentSum, 0) + 1
        }
        return validSubarraysCount
    }

    // TODO: solved [suboptimal] while practising hashmap, but there is a time O(n) space O(1) solution with other tools => do it!
}
