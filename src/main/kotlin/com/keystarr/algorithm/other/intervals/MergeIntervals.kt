package com.keystarr.algorithm.other.intervals

import kotlin.math.max

/**
 * ⭐️ I thought it was straight outa diff array opera, but this problem had many tricks in store (specifically for my brain)
 * LC-56 https://leetcode.com/problems/merge-intervals/submissions/1345923223/
 * difficulty: medium
 * constraints:
 *  • 1 <= intervals.length <= 10^4;
 *  • intervals\[i].length == 2;
 *  • 0 <= start ith <= end ith <= 10^4.
 *
 * Final notes:
 *  • I made an assumption about the input WITHOUT dry-running the given test cases => CODED FULLY AN INCORRECT SOLUTION;
 *  • rofl, 4 failed attempts, a match for my DP experience! But seriously, a wonderful opportunity to learn:
 *   • 1st: tried a number line diff array solution which was super overcomplicated and wrong (or I just didn't finish it);
 *   • 2nd: tried to update the 1st one, failed;
 *   • 3rd: tried the sorting approach but failed, cause missed completely that we consider intervals overlapping even if i1.end == i2.start;
 *   • 4th: fixed the 3rd, but miserably failed to understand that we might merge multiple intervals sorted by the start point,
 *    but the end points of all are unsorted => we must keep track of the max one, and update it on each new interval merge,
 *    instead of just taking the previous one's end upon a gap.
 *  • I spent wayy to much time on this. Basically framing this problem as a straight diff array was wrong! Even though the
 *   actual efficient approach is very much diff array #2 technique => sort and then perform logic, here, overlapping. What
 *   set me off the most was me failing to understand that we might have multiple overlapping intervals with, like, equal or
 *   increasing sorted start points, but with different and essentially unsorted end points => when merging we need to
 *   keep track of the max right point throughout all the intervals we have to merge! and not just set it to the last overlapping
 *   interval upon encountering the gap.
 *
 * Value gained:
 *  • practiced solving an intervals problem;
 *  • learned that diff array is indeed wrong for some interval problems;
 *  • learned the hard-hard way that when merging overlapping intervals one must keep track of the max end :))))
 */
class MergeIntervals {

    /**
     * Designed first, then caught a bunch of errors and fixed in a live mode without noting much, just drawing in Obsidian.
     *
     * Use a dynamic array for results, cause we don't know in advance exactly how many merged intervals we'll get.
     *
     * Time: O(nlogn)
     *  - sorting O(nlogn), where n=[intervals].length
     *  - main loop O(n)
     * Space: O(logn) not including results
     *  - sorting O(logn) quicksort impl
     */
    fun efficient(intervals: Array<IntArray>): Array<IntArray> {
        intervals.sortWith { o1, o2 -> o1[0] - o2[0] } // if permitted, otherwise copy the array

        var toMergeStart = intervals.first()[0]
        var toMergeMaxEnd = intervals.first()[1]
        val mergedIntervals = mutableListOf<IntArray>()
        for (i in 1 until intervals.size) {
            val (currentStart, currentEnd) = intervals[i]
            if (toMergeMaxEnd < currentStart) {
                mergedIntervals.add(intArrayOf(toMergeStart, toMergeMaxEnd))
                toMergeStart = currentStart
                toMergeMaxEnd = currentEnd
            } else {
                if (currentEnd > toMergeMaxEnd) toMergeMaxEnd = currentEnd
            }
        }
        if (toMergeStart != -1) mergedIntervals.add(intArrayOf(toMergeStart, toMergeMaxEnd))
        return mergedIntervals.toTypedArray()
    }

    /**
     * Core idea the same as [efficient], but the specifics are different:
     *  - commit the first interval into the answers straight-away;
     *  - if the interval is overlapping with the last in answer => just update the last answer interval's end to
     *   the max of (last interval's end, the end of the current interval) = get the full overlapping one;
     *  - if it's not overlapping with the last one => just add it straight-away.
     *
     * loop invariant: results array has all merged overlapping intervals up to the (i-1)th interval from [intervals].
     *  init: before the first iteration i=0 => (i-1)=-1 and results is empty, trivially there are no intervals;
     *  maintenance: before the ith iteration begins, indeed we have all overlapping intervals up to the ith merged and in results,
     *   cause even if there are multiple overlapping intervals, we just update the results.last()'s end on each;
     *  termination: when i==[intervals].size, then we have all intervals [0,intervals.size-1] merged and in results.
     *
     * Same complexities as [efficient].
     */
    fun efficientCleaner(intervals: Array<IntArray>): Array<IntArray> {
        intervals.sortWith { o1, o2 -> o1[0] - o2[0] }

        val mergedIntervals = mutableListOf<IntArray>()
        intervals.forEach { interval ->
            if (mergedIntervals.isEmpty() || interval[0] > mergedIntervals.last()[1]) {
                mergedIntervals.add(intArrayOf(interval[0], interval[1]))
            } else {
                val lastMergedInterval = mergedIntervals.last()
                lastMergedInterval[1] = max(lastMergedInterval[1], interval[1])
            }
        }
        return mergedIntervals.toTypedArray()
    }

    /**
     * WRONG,
     *
     * -------------
     *
     * 1. dealing with the intervals => try diff array
     * 2. max interval end value == 10^4 => could try diff array on a number line
     * 3. goal is to return an array of the merged intervals
     *
     * approach #1 diff array with a number line
     *
     * if number line => we could then just iterate through it and build the merged intervals array via O(n) time
     *
     * INCORRECT, actually the first approach doesn't work since at least with prefix sum we can't clearly distinguish a case
     *  of a single 1 interval spanning (number line value == 1) and 2 intervals, one starting and another ending on the same position
     *  (number line value == 1 too)
     *  we could have numberLine: Array<IntArray> where [0] would be the total value, [1] would be if any interval has ended there
     *  => then if line[0]==1 && line[1] != -1 we have to commit the current interval to results, cause it ended without overlapping
     *   and start a new one, otherwise continue!
     *
     * and if the total value on position is >1 then there's definitely overlapping, and we keep on
     * if total value == 0 => theres no interval there, so we break the current interval if we keep track of any.
     *
     * Time: O(m)
     * Space O(m) if not counting the result space
     *
     * ---------
     *
     * approach #2 diff array with sorted intervals
     *
     *  if sorted intervals => we'd iterate through the intervals and build it as we go:
     *   if ith interval start < (i-1)th interval record the start interval if not yet recorded end then continue to the next one
     *   finally when that condition is false, add to the result intervals array the interval with the original start and current end.
     *
     *  Time: O(nlogn)
     *  Space: O(logn) if in-place sorting and not counting the result array. logn for quicksort intermediate space required
     *
     * ---------
     *
     * how to choose:
     *  1. since max(m)==max(n)==10^4, then approach #1 would be faster on average, but approach #2 would have less space on average
     *  2. and approach #2 is much simple for maintenance than the #1 one
     * => in real prod we'd choose depending on the entire requirements context, the choice could vary.
     */
    fun diffArrayNumberLine(intervals: Array<IntArray>): Array<IntArray> {
        val maxValue = intervals.maxOf { it[1] }
        val numberLine = Array(size = maxValue + 2) { IntArray(size = 3) }
        intervals.forEach { interval ->
            val (start, end) = interval
            val inclusiveEnd = end + 1
            numberLine[start][0] += 1
            numberLine[inclusiveEnd][0] -= 1
            numberLine[start][1] = INT_TRUE
            numberLine[inclusiveEnd][2] = INT_TRUE
        }

        var totalValue = 0
        var currentIntervalStart = -1
        val mergedIntervals = mutableListOf<IntArray>()
        numberLine.forEachIndexed { position, meta ->
            val (valueDiff, startFlag, endFlag) = meta
            totalValue += valueDiff

            if (currentIntervalStart == -1) {
                if (totalValue > 0) currentIntervalStart = position
            } else if (totalValue == 0 || (totalValue == 1 && startFlag == INT_TRUE && endFlag == INT_TRUE)) {
                mergedIntervals.add(intArrayOf(currentIntervalStart, position - 1))
                currentIntervalStart = if (totalValue == 1) position else -1
            }
        }

        return mergedIntervals.toTypedArray()
    }
}

private const val INT_TRUE = 1

fun main() {
    println(
        MergeIntervals().efficientCleaner(
            intervals = arrayOf(intArrayOf(1, 4), intArrayOf(1, 3)),
        ).contentDeepToString()
    )
}
