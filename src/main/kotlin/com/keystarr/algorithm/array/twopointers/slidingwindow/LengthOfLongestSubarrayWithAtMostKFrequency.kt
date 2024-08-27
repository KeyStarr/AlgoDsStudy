package com.keystarr.algorithm.array.twopointers.slidingwindow

import kotlin.math.max

/**
 * LC-2958 https://leetcode.com/problems/length-of-longest-subarray-with-at-most-k-frequency/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.size <= 10^5;
 *  • 1 <= nums\[i] <= 10^9;
 *  • 1 <= maxFreq <= nums.size.
 *
 * Final notes:
 *  • done [efficient] by myself in 15 mins;
 *  • decided to skip the improved const sliding window for now, since we have bigger priorities and a chance of it
 *   being useful in the near future I estimate as low. This is good enough interleaving sliding window + counting practice.
 *
 * Value gained:
 *  • practiced recognizing and solving a problem on finding the best valid subarray using a sliding window and frequency
 *   counting using a map.
 */
class LengthOfLongestSubarrayWithAtMostKFrequency {

    // TODO: understand the 2nd improved const solution without inner loops (editorial / top solutions)

    /**
     * problem rephrase:
     *  - given:
     *   - nums: IntArray
     *   - k: Int
     *  Goal: find the best valid subarray, return its length
     *   valid = frequency of all elements RELATIVE TO THIS SUBARRAY <= [maxFreq]
     *   best = max length
     *
     * Best valid subarray => try sliding window?
     *  freqMap - frequencies of all elements in the CURRENT subarray
     *
     *  for each new element - add to the frequency of the new element in freqMap (of the current subarray)
     *  shrink:
     *   freq[nums\[i]] > [maxFreq]
     *   we cant take that element => we must get rid of elements to the left until the frequency is within bounds
     *   while shrinking decrease the frequency in freqMap of the corresponding element
     *  expand:
     *   update the max length
     *
     * nums=1,2,3,1,2,3,1,2  maxFreq = 2
     * freq: [1:2, 2:2, 3:2]
     * l=2   r=7   mL = 6
     *
     * basically we skip elements from the set of prohibited elements (with freq <= maxFreq)
     *
     * edge cases:
     *  - max frequency of an element = max nums size all same element = 10^5 => fits into Int;
     *  - maxFreq == 1.
     *
     * note: that we will always have at least a valid subarray of size 1, since maxFreq==1, so the number itself only is
     *  always a valid subarray => left will never exceed right
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     *  - worst is when maxFreq==nums.size, nums.size is max=10^5 and all numbers are distinct => we have exactly n entries
     *   in the map at the end.
     */
    fun efficient(nums: IntArray, maxFreq: Int): Int {
        val freqMap = mutableMapOf<Int, Int>()
        var left = 0
        var maxValidSize = 0
        nums.forEachIndexed { right, num ->
            freqMap[num] = freqMap.getOrDefault(num, 0) + 1
            while (freqMap.getValue(num) > maxFreq) {
                freqMap[nums[left]] = freqMap.getValue(nums[left]) - 1
                left++
            }
            maxValidSize = max(right - left + 1, maxValidSize)
        }
        return maxValidSize
    }
}
