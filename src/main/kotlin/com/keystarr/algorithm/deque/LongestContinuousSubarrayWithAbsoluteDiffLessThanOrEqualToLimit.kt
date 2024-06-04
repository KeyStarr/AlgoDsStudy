package com.keystarr.algorithm.deque

/**
 * (what a problem name though)
 *
 * LC-1438 https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.size <= 10^5;
 *  • 1 <= nums\[i] <= 10^9;
 *  • 0 <= limit <= 10^9.
 *
 * Final notes:
 *  • I quickly realized that we need to optimize via tracking the max/min BUT got stuck trying to work with the BOUNDARIES
 *      and not the max/min numbers themselves that determine those boundaries!!!! basically:
 *      - minNumber determines the maxBoundary;
 *      - maxNumber determines the minBoundary.
 *    only realized that by reading hints from the course text, also made use of the hint to use 2 deques: for mins and for maxes;
 *  • figured out the rest myself, but all that took me, like, 2h! It's ok rn though, because it's still the learning phase
 *      with this pattern.
 *
 * Value gained:
 *  • not sure how to wrap that max/min into a lesson. Maybe that if I see that the problems needs tracking of the
 *      maximums/minimums in a sliding window => probably use monotonically sorted deque, at least try it out?
 *  • AGAIN failed 3 runs because FORGOT TO TURN INDICES BACK TO THE NUMBERS as in [SlidingWindowMaximum] => if I keep
 *      track of indices, REMEMBER TO TURN THEM BACK INTO NUMBERS WHEN NEEDED! (for operations on numbers: comparisons etc.)
 *  • practiced for the first time using a monotonic Deque to keep track of minimums and first time using 2 Deques for tracking
 *      BOTH maximums and minimums in the sliding window!
 */
class LongestContinuousSubarrayWithAbsoluteDiffLessThanOrEqualToLimit {

    /**
     * Finding the best valid subarray => sliding window:
     *  validity = ABSOLUTE difference between any two elements <= limit;
     *  optimization criteria = max size (the longest)
     *
     * the array is invalid if the absolute diff between its min and max > limit => keep track of max and min ind positions
     * in the current subarray.
     *
     * Idea:
     *  - add the new element into both maxDeque and minDeque per monotonically sorted deque pattern;
     *  - if new element is either new max or min in the current subarray => maintain current subarray to be valid:
     *      - if new element is max, then minDeque.removeLast() the min deque and startInd=polledInd+1 until the limit is satisfied;
     *      - if new element is min, then maxDeque.removeLast() the max deque and startInd=polledInd+1 until the limit is satisfied;
     *  - once its valid, if current subarray size is more than maxSize, update maxSize;
     * - return maxSize.
     *
     * Edge cases:
     *  - all number in [nums] are equal => return nums.size, correct;
     *  - limit==0 => return the subarray of longest consecutive equal numbers, correct.
     *
     * Time: always O(n) since we go through endInd being each number in [nums], but each number can be added and popped
     *  only once from both minDeque and maxDeque.
     * Space: O(n) since both deques in terms of the size depend on nums.size
     *
     * Tools:
     *  - sliding window, with shrinking via startInd until the current subarray is valid again, BUT 2 different
     *      movement strategies;
     *  - monotonically sorted Queue, BUT 2 of each kind: one non-descending (for tracking min) and one non-ascending
     *      (for tracking max).
     */
    fun solution(nums: IntArray, limit: Int): Int {
        val minIndDeque = ArrayDeque<Int>()
        val maxIndDeque = ArrayDeque<Int>()

        var startInd = 0
        var maxSize = 1
        for (endInd in nums.indices) {
            val newNumber = nums[endInd]

            while (minIndDeque.isNotEmpty() && nums[minIndDeque.last()] > newNumber) minIndDeque.removeLast()
            minIndDeque.addLast(endInd)

            while (maxIndDeque.isNotEmpty() && nums[maxIndDeque.last()] < newNumber) maxIndDeque.removeLast()
            maxIndDeque.addLast(endInd)

            val isNewMin = minIndDeque.first() == endInd
            if (isNewMin) {
                while (maxIndDeque.isNotEmpty() && nums[maxIndDeque.first()] - newNumber > limit) {
                    val lastMaxInd = maxIndDeque.removeFirst()
                    startInd = lastMaxInd + 1
                }
            }

            val isNewMax = maxIndDeque.first() == endInd
            if (isNewMax) {
                while (minIndDeque.isNotEmpty() && newNumber - nums[minIndDeque.first()] > limit) {
                    val lastMinInd = minIndDeque.removeFirst()
                    startInd = lastMinInd + 1
                }
            }

            val currentSize = endInd - startInd + 1
            if (currentSize > maxSize) maxSize = currentSize
        }

        return maxSize
    }

    // TODO: refactor later, try merging last to while loops together and make it such that we don't need to check
    //  for emptiness of both deques OR even replace last two whiles with only ifs and prove why that is correct!
    //  (check https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/solutions/609771/java-c-python-deques-o-n/)
}

fun main() {
    println(
        LongestContinuousSubarrayWithAbsoluteDiffLessThanOrEqualToLimit().solution(
            nums = intArrayOf(4, 2, 2, 2, 4, 4, 2, 2),
            limit = 0,
        )
    )
}
