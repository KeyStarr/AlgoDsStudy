package com.keystarr.algorithm.array.intervals.diffarray

/**
 * LC-252 https://leetcode.com/problems/meeting-rooms/
 * ⚠️unlike other prem example problems in the course this one is LOCKED without an actual prem => couldn't submit,
 *  just solved based on the abbreviated statement in the course's article.
 *
 * no constraints known.
 *
 * Final notes:
 *  • submit not available yet => don't know if the solution is actually correct;
 *  • is [diffArrayTimeLine] actually a diff array pattern, or is calling it that a stretch?
 *  • if constraints are such that max(time) would be relatively small (<=10^9) yet invervals.size would be much bigger
 *   => [diffArrayTimeLine] would be a better choice. E.g. max time is 10^4, yet max intervals is 10^8, though it wouldn't make
 *   much sense would it, since theres at most 10^4 non-overlapping meetings we could do. Otherwise, [diffArraySort] would
 *   be a better choice;
 *  • actually not sure about that reasoning above, but its ok for now, the topic is quite niche and we have bigger priorities.
 *
 * Value gained:
 *  • practiced using diff array both approaches on an interval problem for an efficient solution.
 */
class MeetingRooms {

    // TODO: when acquired Leet-Premium, submit the solution

    /**
     * an abbreviated problem statement from the course:
     * "Given an array of meeting times intervals where intervals\[i] = [start, end] indicates the
     * 𝑖th meeting runs from [start, end), determine if one person could attend all meetings.
     *
     * For example, given intervals = [[0, 30], [5, 10], [15, 20]], return false. If you attend the [0, 30] meeting,
     * then you cannot attend the other two.
     * "
     *
     * no access to full problem statement for now
     * => assume the end of the interval is non-inclusive, so "the ith meeting lasts UP TO the end hour"
     *
     * -----------
     *
     * approach #1
     * try a number line? (basically a diff array #1 with mapping diffs onto the number line)
     * if max start/end are reasonably low => we could iterate through the [intervals] and mark their start/end on the timeline,
     *  then iterate over the timeline and if one meeting wasnt yet ended and another starts => return false, otherwise
     *  if we tried all intervals return false.
     * Time: O(m), where m=max(end)
     * Space: O(m) for the timeline array
     *
     * Problem: if, say, one meeting starts at the ith hour and another ends, then timeline\[i] == 0, and we wouldn't
     * be able to tell whether a meeting started or ended there.
     * => we need 2 vars for each hour to track whether a meeting starts/ends there. If multiple meetings do, we don't care
     * about that.
     */
    fun diffArrayTimeLine(intervals: Array<IntArray>): Boolean {
        val maxTime = intervals.maxOf { it[1] }
        val timeline = IntArray(size = maxTime + 1)
        intervals.forEach { interval ->
            val (start, end) = interval
            timeline[start] += 1
            timeline[end] -= 1
        }
        var meetingsInProgress = 0
        timeline.forEach { diff ->
            meetingsInProgress += diff
            if (meetingsInProgress > 1)  return false
        }
        return true
    }

    /**
     * also assume the end of the interval is non-inclusive, so "the ith meeting lasts UP TO the end hour"
     *
     * approach #2, diff array but sorting the diffs (would be better than the 1st one if max(end) would be quite large, like, >10^9)
     * 1. sort the [intervals] by the `start` ascending;
     * 2. iterate through the sortedIntervals, if the current interval starts earlier than the previous intervals ends => return false;
     * 3. if we've gone through all, return true.
     *
     * Time: O(nlogn), where n=[intervals].size, sorting dominates time complexity;
     * Space: O(n) for the sorted intervals (or O(1) if in-place sorting the input array)
     */
    fun diffArraySort(intervals: Array<IntArray>): Boolean {
        intervals.sortWith { o1: IntArray, o2: IntArray -> o1[0] - o2[0] }

        var previousEnd = -1 // assuming min start value is >= 0
        intervals.forEach { interval ->
            val (start, end) = interval
            if (start < previousEnd) return false
            previousEnd = end
        }

        return true
    }
}

fun main() {
    println(
        MeetingRooms().diffArrayTimeLine(
            intervals = arrayOf(
                intArrayOf(19, 30),
                intArrayOf(5, 10),
                intArrayOf(10, 20),
            )
        )
    )
}
