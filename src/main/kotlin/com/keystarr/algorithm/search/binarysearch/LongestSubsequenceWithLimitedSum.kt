package com.keystarr.algorithm.search.binarysearch

/**
 * ⭐️LC-2389: https://leetcode.com/problems/longest-subsequence-with-limited-sum/description/
 * difficulty: easy, evidently for brute force (efficient is definitely leet-medium)
 * constraints:
 *  • 1 <= nums.length, queries.length <= 1000
 *  • 1 <= nums\[i], queries\[i] <= 10^6
 *
 * Final notes:
 *  • amazing how that trick I've learnt doing other task helped here: the problem states that we need to find the
 *   best valid SUBSEQUENCE (so order is important) but the validity criteria is the sum of elements => so actually
 *   we might look through the BEST VALID SUBSETS => which opens up the way to sorting, prefix sum and binary search!!!
 *  • how beautifully the 2 major tools (prefix sum and binary search) came together here!
 *
 * Value gained:
 *  • practiced recognizing and implementing the insertion binary search pattern, with the modification of returning the
 *   max element less than the target in case there was no direct match;
 *  • practiced solving "the best valid subsequence" kind of problem using prefix sum;
 *  • practiced recognizing the "best valid subsequence + validity criteria doesn't require order == best valid subset" trick.
 */
class LongestSubsequenceWithLimitedSum {

    // TODO: re-solve

    /**
     * goal - for each queries\[i] find the best valid subsequence:
     *  valid = sum of the subsequence elements is less than or equal to queries\[i] => order doesn't matter =>
     *  actually we could treat subsequence as the SUBSET;
     *  best (optimization criteria) = maximum size.
     *
     * since the sum of the subset should be less than or equal to queries\[i] and we maximize the amount of elements
     * in the subset => each step take the smallest element until we reach the limit + since we need to do it for many
     * queries => sort + prefix sum?
     *
     * prefix sum - cause the problem then is to find the best valid subarray in prefixSum. Best = max size, valid =
     *  sum of elements is less than or equal to queries\[i].
     *
     * Idea #1:
     *  - sort nums ascending; // time O(nlogn)
     *  - compute prefix sum for nums; // time O(n) space O(n);
     *  - results IntArray(size=queries.size)
     *  - iterate through queries: O(m*logn)
     *      - find the maximum element in prefixSum array such that it is less than or equal to queries[i],
     *       use binary search for that (insertion pattern);
     *      - results[i] = foundInd + 1
     *  - return results
     *
     * Edge cases:
     *  - binary search must find either queries\[i] or the max element of prefixSum that is less than queries\[i]
     *   => there will be no duplicate elements cause nums\[i] >= 1 => can actually use just the regular search pattern
     *   with lefIndex in case of not found;
     *  - the regular binary search insertion pattern, if there's no match to target, return the index of insertion
     *   => the first element that is greater than the target, if it exists. We need the opposite, to find the first
     *   element that is less than the target in that case!
     *  - max index sum in binary search is 10^6 + 10^6 - 1 => fits into int;
     *  - all elements in nums are greater than queries\[i] => then prefixSum[0] > query => results\[i] = 0
     *
     * Time: O(nlogn + n + m*logn) = O(nlogn + m*logn) = O(logn*(m+n))
     * Space: O(n) for prefix sums array
     */
    fun efficient(nums: IntArray, queries: IntArray): IntArray {
        nums.sort()

        val prefixSums = IntArray(nums.size)
        prefixSums[0] = nums[0]
        for (i in 1 until nums.size) prefixSums[i] = prefixSums[i - 1] + nums[i]

        val results = IntArray(queries.size)
        queries.forEachIndexed { ind, query ->
            if (query < prefixSums[0]) { // O(1) instead of O(logn)
                results[ind] = 0
                return@forEachIndexed
            }

            val foundInd = prefixSums.binarySearch(target = query)
            results[ind] = foundInd + 1
        }
        return results
    }

    /**
     * Goal - return the index of the maximum element in prefixSum array such that it is less than or equal to queries\[i].
     * (the answer is guaranteed to exist, cause we filter out the case when query < prefixSum[0] as a pre-condition)
     */
    private fun IntArray.binarySearch(target: Int): Int {
        var left = 0
        var right = size
        while (left < right) {
            val mid = (left + right) / 2
            val midElement = get(mid)
            when {
                target == midElement -> return mid // we found the subset of the original array nums that sums up to exactly queries[i]
                target < midElement -> right = mid
                else -> left = mid + 1
            }
        }
        // target wasn't found => return the index of the max element that is less than the target
        // (left by default is the min element that is greater than the target)
        // (remember - there are no duplicates in prefixSum array in this particular problem)
        return left - 1
    }
}
