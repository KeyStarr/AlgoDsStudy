package com.keystarr.algorithm.search.binarysearch.solutionspace

/**
 * ⭐️LC-1231 https://leetcode.com/problems/divide-chocolate/
 * difficulty: HARD
 * constraints:
 *  • 0 <= k < sweetness.length <= 10^4;
 *  • 1 <= sweetness\[i] <= 10^5.
 *
 * Final notes:
 *  • couldn't find a solution, even a brute force one, by myself in ~1.5h. Failed to figure out how to check whether
 *   for the value X it's possible to  split the array into valid subarrays;
 *  • solved via [efficient] after learning the key ideas from the Editorial (great explanation there btw!);
 *  • turns out that checking whether there is a valid split available is a super simple algorithm, intuitively I feel
 *   that it is correct, but rationally though I still can't explain really why it works, can't prove it (haven't
 *   deliberately tried though).
 *
 * Value gained:
 *  • learnt of that new idea about the applicability of binary search on solution spaces - apparently we may guess
 *   approximate answers, and it's ok, like here. Like, it doesn't matter that there might be no actual min subarray sum
 *   for an intermediate candidate X value, since we will terminate with the real max possible answer (the max valid
 *   min subarray sum always equals to a valid sum after split, because any value after it will be invalid, and since
 *   we go right if the split is valid, we will end up exactly at this real border);
 * • practiced applying binary search on solution spaces.
 */
class DivideChocolate {

    /**
     * Problem rephrase:
     *  - given are the int array of [sweetness] and [friendsCount];
     *  - we must cut [sweetness] into [friendsCount]+1 subarrays;
     * goal: find the maximum possible sum of elements of a subarray after the cut, with it being the minimum sum across
     *  all subarrays during the current cut.
     *
     * it is always possible to cut [sweetness] into [friendsCount] + 1 subarrays, cause k < sweetness.length
     *
     * k < sweetness.length - 1 => there are multiple possible ways of cutting => how to select the best one?
     *
     * brute force: try all combinations, find the best one
     * how to compute all possible combinations of splitting?
     *  - create splitters: IntArray(friendsCount+1)
     *  - fill all splitters with indices starting from 0, with a step of 1
     *  - iterate through splitters, find the subarray with the min sum and write it into `minSubarrayMaxSum`
     *  - brute force all combinations:
     *   - iterate through [splitters.size-1;0] :
     *    - move the index splitters[i] until it reaches the sweetness.size;
     *    - update the minSubarrayMaxSum if it is changing.
     *  - return minSubarrayMaxSum
     *
     * try binary search?
     *  optimization criteria: MAX
     *  min boundary: sweetness.min()
     *  max boundary: the sum of (sweetness.size-friendsCount) maxes from [sweetness]
     *  elimination properties:
     *   - if there is a valid min subarray across all after the division, with sum X => there is no point in trying
     *    any number lower than X, cause we're maximizing X. But there might be such a valid subarray with the sum greater
     *    than X, though there might not be. But it doesn't mean that subarrays with the sum lower than X even exist though,
     *    it just means there's no point in searching for such.
     *   -
     *
     * Since we're talking subarray sum, why don't we try prefixSum first?
     * => create the prefixSum of [sweetness]
     *
     *
     * the size of the target subarray is:
     *  max = sweetness.length-[friendsCount] (all friends get 1 element subarrays)
     *  min = 1 (it is guaranteed to exist)
     *
     * suppose we do a sliding window, and we try to find the best valid target subarray!
     * - init: left = 0, right = 0
     * - how to check whether there exists a valid split of [sweetness] such that the current subarray is a subarray with the min sum?
     * - when size is less than maxSize => expand
     *
     *
     *
     * Edge cases:
     *  - k == sweetness.length - 1 => than there is exactly 1 valid way to cut into subarrays with each subarray length of 1
     *   element => return sweetness.min();
     *  - k == 0 => return the sum of all elements;
     *  - k == 1 =>
     *
     * Time:
     * Space:
     */
    fun solution() {
        // failed to come up with the solution myself, left the original notes for later retrospection
    }

    /**
     * Let's try a non-frontal approach - how to check that the value X is a valid answer? (not guaranteed to be optimal though)
     * Iterate through [sweetness] from 0th index, sum the elements we encounter and, once we reach the sum >= X, "cut"
     * the piece, take that subarray and start the process again with the next. If all resulting subarrays have the sum
     * of >= X, then X is a valid min sweetness of a piece that "could" exist. But there may not be a piece with that
     * exact value:
     *  - if there's a piece with exactly sum equals X => it's a candidate for answer;
     *  - if not => the actual min sum of the subarrays is a candidate for the answer.
     *
     * How to find the maximum possible minimum subarray sum with a valid split?
     * Try binary search on that solution space:
     *  min boundary = sweetness.min(), cause theres no valid possible subarray with the total sum less than that;
     *  max boundary = sweetness.sum() / (friendsCount + 1), cause each friend must have some and we must have less than
     *   everybody => max possible sweetness we can have is guaranteed no greater than that value!
     *  elimination properties:
     *   - if [sweetness] can be split such that all subarrays sums are no less than X => that very split is valid
     *    for any value less than X;
     *   - if [sweetness] can't be split with all subarrays sums no less than X => there's no valid split of [sweetness]
     *    for any value greater than X.
     * => binary search clear to go!
     *
     * Design:
     *  - left=sweetness.min(), right=sweetness.sum()
     *  - while(left <= right):
     *   - compute mid (minSubarrayMaxSumCandidate)
     *   - if isValidSplit([sweetness], minSubarrayMaxSumCandidate):
     *    - left = mid + 1
     *   - else:
     *    - right = mid - 1
     *  - return left // cause the best valid minSum is guaranteed to be the at the right edge of the valid values, we will always find it
     *
     * Edge cases:
     *  - [friendsCount] == 0 => can early return sweetness.sum() via O(n) time, but it'd work correctly as-is;
     *  - [friendsCount] == 1 => general algorithm is OK;
     *  - [friendsCount] == [sweetness].length - 1 => the answer is sweetness.min(), could do early return via O(n) time,
     *   but it's ok;
     *  - sum => max possible sum is 10^4*10^5=10^9 => fits into int
     *
     * Time: O(n*logk)
     *  - isValidSplit takes O(n)
     *  - binary search O(logk), where k=sweetness.sum()
     * Space: O(1)
     */
    fun efficient(sweetness: IntArray, friendsCount: Int): Int {
        var left = sweetness.minOf { it }
        val targetSubarraysCount = friendsCount + 1
        var right = sweetness.sum() / targetSubarraysCount

        while (left <= right) {
            val mid = left + (right - left) / 2
            if (sweetness.isValidSplit(mid, targetSubarraysCount)) left = mid + 1 else right = mid - 1
        }

        return right // cause we always finish when left exceeds right by 1 => right=left-1 is the max valid answer
    }

    /**
     * Goal: return true if it's possible to split IntArray in a way that each subarray has a sum GREATER OR EQUAL to [minSumAllowed].
     */
    private fun IntArray.isValidSplit(minSumAllowed: Int, targetSubarraysCount: Int): Boolean {
        var currentSubarraySum = 0
        var subarraysCount = 0
        forEach { sweetness ->
            currentSubarraySum += sweetness
            if (currentSubarraySum >= minSumAllowed) {
                subarraysCount++
                currentSubarraySum = 0
            }
            if (subarraysCount == targetSubarraysCount) return true
        }
        return false
    }
}

fun main() {
    println(
        DivideChocolate().efficient(
            sweetness = intArrayOf(5, 6, 7, 8, 9, 1, 2, 3, 4),
            friendsCount = 8,
        )
    )
}
