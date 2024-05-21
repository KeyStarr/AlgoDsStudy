package com.keystarr.algorithm.hashtable

/**
 * LC-1248 https://leetcode.com/problems/count-number-of-nice-subarrays/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.length <= 50_000;
 *  • 1 <= nums\[i] <= 10^5;
 *  • 1 <= k <= nums.length.
 */
class CountNumberOfNiceSubarrays {

    /**
     * Tools: Prefix Sum, HashMap.
     *
     * Idea:
     *  - allocate the prefixOddCount map, fill it with map[0] = 1 to count subarrays including the first element easier;
     *  - iterate through [nums]:
     *      - if current number is odd, increase the odd numbers count;
     *      - if map[currentOddCount-targetSum] has a value, then we found the amount of subarrays which have
     *          exactly targetSum odd numbers. There may be multiple, cause nums[i] may be even => that's why we use a hashmap.
     *          Add this value to validArraysCount;
     *      - if map[currentOddCount] exists, increase it or put 1.
     *
     * Edge cases:
     *  - k=1 => nothing special, works correctly;
     *  - nums.length=1 => if it's odd, we count it correctly, if its even then it's ok too;
     *
     * Time: O(n)
     * Space: O(n)
     */
    fun suboptimal(nums: IntArray, targetOddCount: Int): Int {
        val prefixOddCountMap = mutableMapOf(0 to 1)
        var currentOddCount = 0
        var validSubarrayCount = 0
        for (number in nums) {
            if (number and 1 == 1) currentOddCount++
            validSubarrayCount += prefixOddCountMap[currentOddCount - targetOddCount] ?: 0
            prefixOddCountMap[currentOddCount] = prefixOddCountMap.getOrDefault(currentOddCount, 0) + 1
        }
        return validSubarrayCount
    }

    // TODO: revisit and understand O(1) space solution
    //  https://leetcode.com/problems/count-number-of-nice-subarrays/solutions/419378/java-c-python-sliding-window-o-1-space/
}
