package com.keystarr.algorithm.hashing.hashmap

/**
 * LC-3005 https://leetcode.com/problems/count-elements-with-maximum-frequency/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.size <= 100;
 *  • 1 <= nums\[i] <= 100.
 *
 * Final notes:
 *  • done 2 pass [efficient] by myself in 8 mins, 1st run submit. Final 1 pass [efficient] in 10 mins;
 *  • restrained my premature optimization urges and designed/implemented the trivial 2 pass approach first, then I could
 *   feel that there could've been a 1 pass solution => assumed so and found it => optimized to 1 pass!
 *
 * Value gained:
 *  • practiced solving a meta-counting type problem using a counting array and max finding 1-pass.
 */
class CountElementsWithMaximumFrequency {

    // TODO: create an actual "counting" package and move it along with other problems like that there?

    /**
     * trivial approach:
     *  - count the frequencies of elements in [nums] using IntArray or a hashmap, recording the maximum frequency;
     *  - sum up frequencies which are equal to max.
     *
     * Time: always O(n)
     * Space: always O(k)=O(101)=O(1) for int array (k=amount of possible numbers)
     *  or hashmap average/worst O(n), since worst is all numbers are distinct => n entries in the map
     *
     * ---------------- optimization
     *
     * I don't think we could reduce the space, since we have to know all frequencies to determine
     *  the max, and to sum up maxes.
     *
     * probably we could 1 pass, no?
     *
     * 1 pass: can we count the number of maxFrequencies on-the-fly as we count the frequencies, just as we find the maxFrequency that way?
     * yes => as soon as we encounter a new maxFreq we can set maxFreqCount to 1, its guaranteed we've never seen that frequency before
     *  if we encounter another freq == current maxFreq, increment the maxFreqCount
     * its guaranteed we'll count all maxFreq occurrences that way in 1 pass
     * time/space complexities are the same
     */
    fun efficient(nums: IntArray): Int {
        val frequencies = IntArray(size = 101)
        var maxFrequency = 0
        var maxFrequencyCount = 0
        nums.forEach { number ->
            val newFrequency = frequencies[number] + 1
            frequencies[number] = newFrequency
            if (newFrequency > maxFrequency) {
                maxFrequency = newFrequency
                maxFrequencyCount = 1
            } else if (newFrequency == maxFrequency) maxFrequencyCount++
        }
        return maxFrequency * maxFrequencyCount
    }
}
