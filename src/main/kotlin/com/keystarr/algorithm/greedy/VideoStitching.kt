package com.keystarr.algorithm.greedy

import kotlin.math.max
import kotlin.math.min

/**
 * 💣 RETRY FULL, overcomplicated the solution, failed to do it in reasonable time
 * LC-1024 https://leetcode.com/problems/video-stitching/description/
 * constraints:
 *  • 1 <= clips.size <= 100
 *  • 0 <= start ith <= end ith <= 100
 *  • 1 <= time <= 100
 *
 * Final notes:
 *  • ⚠️ 1h50m still no solution, but I'm sure I'm on the right path, just figuring out the subproblems too long. By the
 *   best practices, I should've just dead end stopped at the 1h mark. I'm sure what I'm doing is at least not the clean
 *   efficient solution, cause it's too complicated, though has the expected usual efficient time complexity for interval problems;
 *  • what have I just done... [abomination] works, submit 7th submit was a success. 6 failed edge cases though 🤦
 *    and it is so overcomplicated, I would have never done it during an interview, and even if I did, it would've been
 *    horrible. I just kept writing, I couldn't stop;
 *  • actual greedy approach is super great, intuitive and simple. I should've reconsidered my initial approach when
 *   it got too complicated, should've dropped the idea of finding the "unique" parts and come up with a solution via
 *   a single loop after sorting => greedy is usually like that.
 *   done [greedy] after learning it from https://leetcode.com/problems/video-stitching/solutions/269988/c-java-6-lines-o-n-log-n/
 *
 * Value gained:
 *  • 🔥🔥lesson learned, new strategy updates:
 *      1. when practicing leetcode, not, like, learning a completely new problem => ALWAYS CAP AT 1h. CANT SOLVE IN 1H
 *       => ALWAYS STOP AND READ THE SOLUTION. NO MATTER HOW CLOSE YOU WERE.
 *      2. if can't come up with an approach in 30 mins => READ THE HINT
 *  • overcomplicated solutions ARE NEVER useful. Not usable in production, obviously, not feasible during interviews
 *   AND DON'T HELP LEARNING! A waste of time;
 *  • practiced greedy on intervals!
 *  • ⭐️ [abomination] is a great example of how an incorrect initial approach "let's find unique intervals first!" that actually leads
 *   to a CORRECT solution traps the design into the never-ending cycle of overcomplicated mess.
 */
class VideoStitching {

    // TODO: retry fully
    // TODO: understand O(n) time https://leetcode.com/problems/video-stitching/solutions/270680/c-o-n-no-sorting-greedy-explained/

    /**
     * A simple greedy approach:
     *  1. as usual with greedy and with intervals in particular => sort the intervals ascending, by start;
     *  2. until we have the entire event duration, iterate through the remaining clips that start up to the current
     *   stitching end, and pick the interval with the farthest end.
     *   - why? we minimize the amount of clips, but the entire [0,eventDuration] interval must be filled
     *    => we start with stitchingEnd=0, which means that we have no clips yet, but we must start stitching from the even start = 0
     *    => we must fill the 0th second, but we want to use the minimum number of clips + there might be multiple clips that contain it
     *    => out of all matching clips, pick the one which will fill the biggest timeline of the event = the one with the
     *     farthest end, but it must start within the current stitching range.
     *
     * If, after picking the max timeline fill valid clip, the newMaxEnd==stitchingEnd, then we've found no clips that
     *  started in the current timeline that we've already filled, so there are no gaps, and extended further from it
     *  => there's a gap, and the entire even can not be reconstructed.
     *
     * Time: always O(nlogn)
     *  - sorting O(nlogn)
     *  - stitching loop O(n)
     *   we are using each clip exactly once (inner while body), but we may consider it twice (reaching the termination
     *    condition of the inner while, and on the next iteration checking it again) => the inner loop iterations across
     *     all out loop worst sum up to c*n iterations
     * Space: always O(logn) for sorting
     */
    fun greedy(clips: Array<IntArray>, eventDuration: Int): Int {
        clips.sortBy { it[0] }

        var stitchingEnd = 0
        var newMaxEnd = 0
        var clipInd = 0
        var clipsUsed = 0
        while (stitchingEnd < eventDuration) {
            while (clipInd < clips.size && clips[clipInd][0] <= stitchingEnd) {
                newMaxEnd = max(clips[clipInd][1], newMaxEnd)
                clipInd++
            }
            if (stitchingEnd == newMaxEnd) return -1
            stitchingEnd = newMaxEnd
            clipsUsed++
        }
        return clipsUsed
    }

    /**
     * -------- problem rephrase ----------
     *
     * Given:
     *  - clips: an array of intervals, where clips\[i]=[start ith, end ith) integers, denoting the start and end of the
     *   clip in seconds (end not inclusive)
     *  - time: integer, the number of seconds the entire even lasted;
     *  - step rule: we can split the clips into subarrays in any valid way.
     *
     * Goal: return the minimum number of clips required to build the entire interval for an event [0..time).
     *  i.o. the minimum number of intervals to build the entire interval [0..time) without gaps.
     *
     * -------- approach ----------
     *
     * - intervals => try sorting first by start?
     *
     * observation #1: we must take intervals which have unique parts of the event (those which never occur in other invervals);
     * observation #2: out of remaining intervals we must take those which have the max length of the remaining interval
     *   to be filled. Remaining = changing each time we take a new interval.
     *
     * -------- design ----------
     *
     * - sort the intervals by start;
     * - add to result all intervals with unique parts
     * - compute the missing intervals
     * - greedily fill the missing intervals
     *  while remainingInterval != 0:
     *   - find the biggest overlapping interval in intervals with the remaining interval;
     *   - add it into result;
     *   - add remaining parts of the missing interval back into the missing list.
     *
     * observation #3: if there are multiple missing intervals initially, then its guaranteed that there are intervals
     *  which have unique parts in between those => and we have already added them to the answer => there is no interval
     *  which covers more than 1 missing area => for each missing interval we just have to find the interval which overlaps
     *  it the most (repeatedly).
     *
     * observation #4: when we've found the interval which overlaps with missingInterval\[i] the most, if it doesn't fully
     *  cover it, then we must add the remaining parts back into the missing list. However, we don't need to mark the chosen
     *  interval as used, cause it doesn't contain the remaining missing parts that we've added back.
     *  So the property of "pick a single missing interval = find the biggest overlapping interval with it, its the best
     *   step to the answer" we've defined in o.3 still holds.
     *
     * edge cases:
     *  - clips.length == 1 =>
     *  - any interval has start ith == end ith =>
     *  - time == 1 =>
     *  - when is the stitching impossible? when we have any gaps in the input.
     *   what is a gap? maxEndSoFar < currentStart, if there is any such in the sorted input intervals => return -1
     *  - clip\[i].end or even clip\[i].start > [eventDuration] =>
     *
     * Time: O(nlogn + eventLength * n)
     *  - sorting O(nlogn)
     *  - iteration to find unique ones O(n)
     *  - while loop for remaining O(eventLength * n)
     *   - how many iterations? worst we have all items in [intervals] of length 1 => with one iteration we reduce always
     *    the remaining interval with the minimum value, by 1 + we had no unique intervals => we do exactly one iteration
     *    for 1 unit of length of the even interval => O(eventLength)
     *   - find the biggest interval that overlaps with the remaining O(n)
     * Space: average O(n + eventDuration)
     *  - worst uniqueIntervals is n
     *  - worst missing intervals is c*eventDuration
     */
    fun abomination(clips: Array<IntArray>, eventDuration: Int): Int {
        clips.sortBy { it[0] }
        if (clips[0][0] > 0) return -1

        // 1 find all intervals with unique parts
        val uniqueIntervals = mutableListOf<IntArray>()
        var maxEndSoFar = 0
        for (i in 0 until clips.size - 1) {
            val current = clips[i]
            val (currentStart, currentEnd) = current
            if (currentStart > eventDuration) break
            if (currentStart > maxEndSoFar) return -1

            // current contains a unique part from currentStart to next.start (start)
            val nextStart = clips[i + 1][0]
            if ((currentStart == maxEndSoFar && nextStart != currentStart)
                || (maxEndSoFar < currentEnd && nextStart > maxEndSoFar)
            ) uniqueIntervals.add(current)

            if (currentEnd > maxEndSoFar) maxEndSoFar = currentEnd
        }
        val lastClip = clips.last()
        if ((max(maxEndSoFar, lastClip[1]) < eventDuration)
            || (maxEndSoFar < min(lastClip[0], eventDuration))
        ) return -1
        if (maxEndSoFar < eventDuration && maxEndSoFar < lastClip[1] && lastClip[0] < eventDuration) uniqueIntervals.add(
            lastClip
        )

        // 2 compute missing intervals
        val missingIntervals = mutableListOf<IntArray>()
        maxEndSoFar = 0
        uniqueIntervals.forEach { interval ->
            val (currentStart, currentEnd) = interval
            if (maxEndSoFar < currentStart) missingIntervals.add(
                intArrayOf(
                    maxEndSoFar,
                    min(currentStart, eventDuration)
                )
            )
            if (currentEnd > maxEndSoFar) maxEndSoFar = currentEnd
        }
        if (maxEndSoFar < eventDuration) missingIntervals.add(intArrayOf(maxEndSoFar, eventDuration))
        if (missingIntervals.isEmpty()) return uniqueIntervals.size

        // 3 greedily fill the missing intervals with the minimum amount of remaining intervals
        var missingInd = 0
        var intervalsUsed = 0
        while (missingInd != missingIntervals.size) {
            val missingInterval = missingIntervals[missingInd]

            // find the interval with most overlap with the missingInterval
            var maxOverlap = 0
            var maxInterval: IntArray? = null
            clips.forEach { interval ->
                val overlap = min(interval[1], missingInterval[1]) - max(interval[0], missingInterval[0])
                if (overlap > maxOverlap) {
                    maxOverlap = overlap
                    maxInterval = interval
                }
            }
            println("chosen maxInterval ${maxInterval.contentToString()}")

            val leftRemaining = intArrayOf(missingInterval[0], maxInterval!![0])
            val rightRemaining = intArrayOf(maxInterval!![1], missingInterval[1])
            if (leftRemaining[0] < leftRemaining[1]) missingIntervals.add(leftRemaining)
            if (rightRemaining[0] < rightRemaining[1]) missingIntervals.add(rightRemaining)

            intervalsUsed++
            missingInd++
        }

        return uniqueIntervals.size + intervalsUsed
    }
}

fun main() {
    println(
        VideoStitching().abomination(
            clips = arrayOf(
                intArrayOf(0, 5),
                intArrayOf(1, 6),
                intArrayOf(2, 7),
                intArrayOf(3, 8),
                intArrayOf(4, 9),
                intArrayOf(5, 10),
                intArrayOf(6, 11),
                intArrayOf(7, 12),
                intArrayOf(8, 13),
                intArrayOf(9, 14),
                intArrayOf(10, 15),
                intArrayOf(11, 16),
                intArrayOf(12, 17),
                intArrayOf(13, 18),
                intArrayOf(14, 19),
                intArrayOf(15, 20),
                intArrayOf(16, 21),
                intArrayOf(17, 22),
                intArrayOf(18, 23),
                intArrayOf(19, 24),
                intArrayOf(20, 25),
                intArrayOf(21, 26),
                intArrayOf(22, 27),
                intArrayOf(23, 28),
                intArrayOf(24, 29),
                intArrayOf(25, 30),
                intArrayOf(26, 31),
                intArrayOf(27, 32),
                intArrayOf(28, 33),
                intArrayOf(29, 34),
                intArrayOf(30, 35),
                intArrayOf(31, 36),
                intArrayOf(32, 37),
                intArrayOf(33, 38),
                intArrayOf(34, 39),
                intArrayOf(35, 40),
                intArrayOf(36, 41),
                intArrayOf(37, 42),
                intArrayOf(38, 43),
                intArrayOf(39, 44),
                intArrayOf(40, 45),
                intArrayOf(41, 46),
                intArrayOf(42, 47),
                intArrayOf(43, 48),
                intArrayOf(44, 49),
                intArrayOf(45, 50),
                intArrayOf(46, 51),
                intArrayOf(47, 52),
                intArrayOf(48, 53),
                intArrayOf(49, 54),
            ),
            eventDuration = 50,
        )
    )
}
