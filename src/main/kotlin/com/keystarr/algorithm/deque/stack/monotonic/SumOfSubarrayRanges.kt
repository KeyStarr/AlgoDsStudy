package com.keystarr.algorithm.deque.stack.monotonic

/**
 * LC-2104 https://leetcode.com/problems/sum-of-subarray-ranges/description/
 * difficulty: medium (HAH, easily leet-hard!)
 * constraints:
 *  • 1 <= nums.size <= 1000
 *  • -10^9 <= nums\[i] <= 10^9
 *
 * Final notes:
 *  • ⚠️ couldn't figure out the O(n) time straight away, so done [brute] first, at 17 mins mark. And actually good thing
 *   I stopped trying so soon, which is usually non-effective practice, but here the problem was extremely hard for me rn;
 *  • funny, I've done a successful submit => and feel like I'm pulled away from the problem to just check the solution
 *   and not even bother with finding a better one myself. But its honestly weird why [brute] is in 100%/100% time/space bracket.
 *   Maybe that follow up is a trap and there's no better one;
 *  • thanks to awesome guys in the comments, I first solved [SumOfSubarrayMinimums],
 *   which, as it turns out, is literally (entirely) the core component for solving this problem;
 *  • ⚠️ even AFTER solving that other problem I couldn't figure out the solution in here in, like, 20 mins. Thanks to
 *   https://www.youtube.com/watch?v=pe-G2-RbFOk, I finally understood it, *facepalm* how simple but tricky that last step was.
 *   Like, literally metanoia happened in my head at some point during that vid;
 *  • SO, EVEN THOUGH LC-907 WAS ALREADY challenging enough, this problem added even more on top of it! That heavy load
 *   would've been impossible for me to carry in a real interview right now, just WOW.
 *
 * Value gained:
 *  • practiced solving an extremely tricky monotonic stack problem efficiently.
 */
class SumOfSubarrayRanges {

    // TODO: retry in 1-2 weeks, failed to both recognize the monotonic stack here and struggled to understand the solution

    /**
     * problem rephrase:
     *  - given: array of integers
     *  - goal: the sum of metrics of all possible subarrays
     *   metric = diff between the max and the min in the subarray
     *
     * observation: no need to check subarrays of 1 number, cause its metric is always 0
     *
     * all possible subarrays => 2 pointers / sliding window?
     * or do we not need to check ALL possible subarrays?
     *
     * observation: for any subarray containing the global max/min the metric will always be computed using those elements.
     *
     * repeatedly getting max/min?
     *
     * 1 3 2 -1
     *
     * monotonic stack?
     * 3 2 -1
     * -1
     *
     * -----
     *
     * brute force: generate all possible subarrays and compute the metric for each, sum
     *
     * edge cases:
     *  - max sum is when all numbers are max => 1000 * 10^9 = 10^12 => use long for sum
     *
     * Time: always O(n^2)
     *  - outer loop is always n iterations
     *  - inner loop is worst at first n-1 iterations, then n-2 etc
     * Space: always O(1)
     */
    fun brute(nums: IntArray): Long {
        var sum = 0L
        nums.forEachIndexed { i, anchor ->
            var min = anchor
            var max = anchor
            for (j in i + 1 until nums.size) {
                val num = nums[j]
                if (num < min) min = num
                if (num > max) max = num
                sum += max - min
            }
        }
        return sum
    }

    /**
     * goal: given integers array, return the sum of metrics for all subarrays
     *  subarray metric = (max - min)
     *
     * well, we already know how to find the sum of minimums in all subarrays thanks to [SumOfSubarrayMinimums]
     * => since we don't actually care about the metric for any specific subarray, and only want to know the sum of metrics,
     *  we could simply find the sum of maximums of all subarrays and from that subtract the sum of all minimums of all subarrays
     * => each individual sub-array's min and max are accounted for in the resulting equation, even though these are not matched
     *  exactly one against another
     *
     * THIS PROBLEM ISSSS.. something.
     *
     * Design:
     *  1. find the sum of max of all subarrays;
     *  2. find the sum of min of all subarrays;
     *  3. return 1) - 2)
     *
     * Edge cases:
     *  - sum of maxes => maximum number of subarrays is when nums.size=10^3 = 10^3 * (10^3-1)/2 ~ 10^6 and the maximum
     *   element possible is 10^9 => max sum of maxes = 10^6 * 10^9 = 10^15 => use Long for that;
     *  - max sum of mins is actually the same, for the worst case when all elements are max possible, 10^9.
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     */
    fun efficient(nums: IntArray): Long {
        val maxesSum = nums.sumOfAllSubarraysMinMax(stackProperty = { stackTop, new -> stackTop >= new })
        val minsSum = nums.sumOfAllSubarraysMinMax(stackProperty = { stackTop, new -> stackTop <= new })
        return maxesSum - minsSum
    }

    /**
     * Algorithm 1to1 from [SumOfSubarrayMinimums].
     * Core algo idea - for each nums\[i] calculate the amount of all subarrays where it is max/max, multiply by nums\[i] and add to the sum.
     *
     * For min stack is non-decreasing, for max non-increasing.
     */
    private fun IntArray.sumOfAllSubarraysMinMax(stackProperty: (stackTop: Int, new: Int) -> Boolean): Long {
        val stack = ArrayList<Int>()
        var sum = 0L
        for (rightInd in 0..size) {
            while (stack.isNotEmpty() && (rightInd == size || !stackProperty(get(stack.last()), get(rightInd)))) {
                val removedNumInd = stack.removeLast()
                val removedNum = get(removedNumInd)
                val elementsToRightWithRemoved = rightInd - removedNumInd
                val elementsToLeft = removedNumInd - if (stack.isNotEmpty()) stack.last() + 1 else 0
                val numOfSubs = elementsToRightWithRemoved + elementsToRightWithRemoved * elementsToLeft.toLong()
                sum += numOfSubs * removedNum
            }
            stack.add(rightInd)
        }
        return sum
    }
}

fun main() {
    println(
        SumOfSubarrayRanges().efficient(
            intArrayOf(1, 2, 3),
        )
    )
}
