package com.keystarr.algorithm.array.intervals

/**
 * ⭐️ LC-57 https://leetcode.com/problems/insert-interval/editorial/
 * difficulty: medium (imho, should be a leet-hard!)
 * constraints:
 *  • 0 <= intervals.length <= 10^4;
 *  • intervals\[i].size == newIntervals.size == 2;
 *  • 0 <= start ith <= end ith <= 10^5 (for newInterval too);
 *  • intervals is sorted by start ith, ascending;
 *
 * Final notes:
 *  • it's crazy how simple this problem is and how difficult it is to make this work. Kinda reminds me of [com.keystarr.algorithm.graph.backtracking],
 *   but if one understands backtracking, that one is actually OK. This one is really hard for me right now;
 *  • sooo many edge cases! look at [abomination]. At least with that approach, and one other I tried. Editorial seems
 *   quite wordy as well, but top java submission does seem to be a lot easier.
 *
 * Value gained:
 *  • realized I suck at interval problems right now :) And need to put in more work to get this topic straight.
 *   A great learning opportunity!
 */
class InsertInterval {

    fun elegant(intervals: Array<IntArray>, newInterval: IntArray): Array<IntArray> {
        TODO("Skipped for now, cause I estimate that takes a lotta effort, and I have bigger priorities rn")
    }

    /**
     * The craziest part is that this Frankenstein works and I even understand how :)
     * A successful submit.
     *
     * ------------------------------
     *
     * problem rephrase:
     *  - given:
     *      - an array of intervals: start/end, sorted ascending by start, non-overlapping
     *      - newInterval.
     *  - goal: insert newInterval such that resulting array is still sorted ascending by start, non-overlapping
     *      => merge overlapping intervals if necessary.
     *
     * 2 major cases:
     *  1. we need to merge some intervals (can be more than 1, can be even all intervals we have);
     *  2. we don't need to merge, just insert.
     *
     * how to decide which intervals to merge?
     *  - how do we define overlapping? is end inclusive or exclusive, do [1,3] and [3,4] overlap or not? apparently yes
     *   judging by example 2
     *  - leftmost interval to merge (if any): the first interval which `end` is >= to newInterval.start
     *  - rightmost interval to merge (if any): the first which `start` is >= newInterval.start but <= newInterval.end
     *  - and we need to merge all intervals in between these 2.
     *
     * since input is not non-overlapping => there can be exactly 1 interval which
     *  newInterval.start >= interval\[k].start && newInterval.start <= interval\[k].end
     *
     * but if there's none, we need the last rightmost interval index with interval.end < newInterval.start
     *
     * same for the newInterval.end. And it can be the same interval for both.
     *
     * Edge cases:
     *  - there's no leftmost interval to merge =>
     *  - there's no rightmost intervals to merge =>
     *  - there are no intervals to merge at all =>
     *      - either intervals.size == 0 =>
     *      - or intervals.size > 0 =>
     *  - we have to merge all intervals =>
     *  - some interval has start == end =>
     *  - newInterval is entirely inside an interval in the input =>
     *
     * Time: O(n)
     *  - find indices of the intervals to merge O(n);
     *  - modify / recreate the array with these intervals merged and new one inserted O(n);
     * Space: O(n) for the new result array
     */
    fun abomination(intervals: Array<IntArray>, newInterval: IntArray): Array<IntArray> {
        if (intervals.isEmpty() || newInterval[0] <= intervals[0][0] && newInterval[1] >= intervals.last()[1]) {
            return arrayOf(newInterval)
        }

        var leftMostInclusive = -1
        var leftMostExclusive = -1
        var rightMostInclusive = -1

        val (newStart, newEnd) = newInterval
        for (i in intervals.indices) {
            val (start, end) = intervals[i]

            if (newStart in start..end) leftMostInclusive = i
            if (newEnd in start..end) rightMostInclusive = i

            if (newStart > end) leftMostExclusive = i
        }

        if (leftMostInclusive == -1 && rightMostInclusive == -1) {
            // just insert
            val result = mutableListOf<IntArray>()
            intervals.forEachIndexed { ind, interval ->
                if (ind - 1 == leftMostExclusive) result.add(newInterval)
                if (!(interval[0] >= newStart && interval[1] <= newEnd)) result.add(interval)
            }
            if (leftMostExclusive == intervals.size - 1) result.add(newInterval)
            return result.toTypedArray()
        } else {
            // smart merge
            val actualStart = if (leftMostInclusive != -1) intervals[leftMostInclusive][0] else newStart
            val actualEnd = if (rightMostInclusive != -1) intervals[rightMostInclusive][1] else newEnd
            val mergedInterval = intArrayOf(actualStart, actualEnd)
            val result = mutableListOf<IntArray>()
            intervals.forEachIndexed { ind, interval ->
                if (leftMostInclusive != -1) {
                    if (ind == leftMostInclusive) result.add(mergedInterval)
                } else if (ind - 1 == leftMostExclusive) {
                    result.add(mergedInterval)
                }

                val (start, end) = interval
                if (!(start in actualStart..actualEnd || end in actualStart..actualEnd)) result.add(interval)
            }
            return result.toTypedArray()
        }
    }
}

fun main() {
    println(
        InsertInterval().elegant(
            intervals = arrayOf(
                intArrayOf(1, 2), intArrayOf(3, 5), intArrayOf(6, 7), intArrayOf(8, 10), intArrayOf(12, 16)
            ),
            newInterval = intArrayOf(4, 8),
        ).contentDeepToString()
    )
}
