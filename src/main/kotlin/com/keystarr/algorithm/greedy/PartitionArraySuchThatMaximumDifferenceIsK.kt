package com.keystarr.algorithm.greedy

/**
 * LC-2294: https://leetcode.com/problems/partition•array•such•that•maximum•difference•is•k/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.length <= 10^5
 *  • 0 <= nums\[i] <= 10^5
 *  • 0 <= k <= 10^5
 *  
 * Final notes:
 *  • done [efficient] by myself in 20 mins, pretty intuitive. Realized the proof for why the sorting is a correct move
 *   half the way into designing the code :D
 *  • Why is that a medium? 
 *  
 * Value gained:
 *  • practiced recognizing a greedy approach to the problem (asking for a min, huh), designing and implementing it
 *   (indeed, pre•processing the input via sorting seems to be a common tool for greedy algorithms)
 *  • funny, the problem mentioned "subsequences" but actually with other constraints combined we ended up looking for
 *   the "subsets - the original order doesn't matter => be wary of such cases in other problems.
 */
class PartitionArraySuchThatMaximumDifferenceIsK {

    /**
     * Goal: return the minimum number of valid subsequences that it's possible to split [nums] into.
     *  valid: the difference between min and max in a subsequence is <= [maxDiff].
     *
     * Observations:
     *  - there is always a correct answer of splitting [nums] into subsequences of 1 element, cause min k == 0, and
     *   the diff of each such subsequence is 0. So it's at least a last resort.
     *
     * Minimum => try greedy?
     *
     * We can sort the input even though the problem is asking for subsequences, because if we select some numbers from the
     * sorted input, we could select them as a subsequence, just in a different order. But the validity rule of the original
     * subsequence does not depend on the order of the elements, cause its max-min <= k.
     *
     * Idea:
     *  - sort the [nums]
     *  - currentMin = nums[0]
     *  - subsequencesCount = 0
     *  - iterate through nums from 1 to nums.size - 1:
     *   - if nums\[i] - currentMin > k:
     *      - currentMin = nums[i]
     *      - subsequencesCount++
     *  - subsequencesCount++ // cause its either we didn't stop at the last number, or if did, we need to count it itself
     *  - return subsequencesCount
     *
     * Edge cases:
     *  - k == 0 => we group into subsequences only equal numbers, correct. If there are no duplicates, the number of subsequences
     *   will be nums.size (each containing exactly 1 element)
     *  - nums.length == 1 => the loop will be skipped, subsequencesCount = 1 => correct.
     *
     * Time: O(n*logn) for the sorting
     * Space: always O(n) for the sorted array, or we could've done in-place sorting with O(1) space
     *  (or potentially O(logn) temp space used for the sorting)
     */
    fun efficient(nums: IntArray, maxDiff: Int): Int {
        val sortedNums = nums.sorted()
        var currentMin = sortedNums[0]
        var subsequencesCount = 0
        for (i in 1 until sortedNums.size){
            if (sortedNums[i] - currentMin <= maxDiff) continue
            currentMin = sortedNums[i]
            subsequencesCount++
        }
        subsequencesCount++
        return subsequencesCount
    }
}
