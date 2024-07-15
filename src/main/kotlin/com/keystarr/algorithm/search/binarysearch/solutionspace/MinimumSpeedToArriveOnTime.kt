package com.keystarr.algorithm.search.binarysearch.solutionspace

import kotlin.math.ceil
import kotlin.math.max

/**
 * LC-1870 https://leetcode.com/problems/minimum-speed-to-arrive-on-time/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= trainDistances.size <= 10^5;
 *  • 1 <= trainDistances\[i] <= 10^5;
 *  • 1 <= maxHours <= 10^9;
 *  • at most two digits after the decimal point in `maxHours`.
 *
 * Final notes:
 *  • it's interesting how here the "no valid answer" case can be checked for just O(1) time. And if we don't handle
 *   it that way, then binary search wouldn't properly work as the standard template.
 *
 * Value gained:
 *  • practiced binary search on solution spaces.
 */
class MinimumSpeedToArriveOnTime {

    /**
     * Problem rephrase:
     *  - given `maxHours: Double`, `trainDistances: IntArray`;
     *  - each train can only depart at an integer hour;
     *  - all trains must be used.
     * Goal - find the minimum positive integer speed for all trains such that we reach the destination
     *  at most `maxHour` hours, or -1 if it's impossible.
     *
     * Goal = minimum, the solution is one from the space => try binary search on solution space?
     *
     *  - boundaries:
     *   - min speed = 1, since 0 or negative never reaches destination, and we consider only integers;
     *   - max speed = Int.MAX
     *      - for most cases `trainDistances.max()` would be enough since then each distance BUT THE LAST will be crossed
     *       for exactly under 1 hour. And the last is crossed for `trainDistances.last() / speed.toFloat()`;
     *      - edge case, the last distance must be minimized beyond trainDistances.max():
     *       - consider dist = [2 10 3], maxHours = 2.1. Speed=10 not enough, cause hours then = 2.3, but speed = 100 is ok;
     *       - `maxHours` min precision is .01 => consider max boundary to be
     *        max(trainDistances.max(), trainDistances.last() / 0.01)
     *   and the last distance
     *    (and we can't do better, cause trains depart only at integer hours.
     *
     *  - binary search criteria:
     *   - if speed X is a valid solution (the commute hours are at most `maxHours`) => then all speeds higher than it
     *    are valid too, but we don't need them since we minimize;
     *   - if speed Y is not a valid solution => then all speeds lower than it are invalid also, cause hours would only
     *    grow with speed being changed.
     *
     *  - how long to check if a speed is valid?
     *   - go through all `trainDistances` and do `ceil(trainDistance/speed)`, until the last, at the last do trainDistance/speed
     *    preserving the decimal;
     *   => Time O(n), Space O(1) of that inner loop.
     *
     * =>  binary search on solutions space is applicable.
     *
     * Design:
     *  - init pointers, left=1, right=max(trainDistances.max(), trainDistances.last() / 0.01);
     *  - do binary search within that space:
     *   - check whether the speed is valid by the algorithm above;
     *   - if the speed is valid, go left, otherwise go right.
     *  - return left.
     *
     * Edge cases:
     *  - `trainDistances.max()` is not a valid answer, the hours frame is so slim that the decimal of hours for the
     *   last train distance is the deciding factor => make max boundary max(trainDistances.max(), trainDistances.last() / 0.01);
     *  - trainDistance/speed division must be ceil;
     *  - trainDistances.last()/speed has decimal below .01 => no problem, the main thing is that it is under maxHours
     *   which has at most 2 decimal digits;
     *  - maxSpeed is 10^5 * 10^2 = 10^7, fits into Float.
     *
     * 2 10 3, hour = 2.1
     * s=10 => 1 + 1 + 0.3 = 2.3 WRONG
     * s = 100 => 1 + 1 + 0.03 => 2.03  RIGHT
     *
     * Time: O(n*logk)
     *  - isSpeedValid takes O(n)
     *  - average/worst iterations is O(logk), where k = max(trainDistances.max(), trainDistances.last() / 0.01))
     * Space: O(1)
     */
    fun efficient(trainDistances: IntArray, maxHours: Double): Int {
        var left = 1
        var right = max(trainDistances.maxOf { it }, trainDistances.last() * 100) + 1
        var lastValidSpeed = -1
        while (left <= right) {
            val middle = left + (right - left) / 2
            val isValid = trainDistances.isSpeedValid(speed = middle.toDouble(), maxHours = maxHours)
            if (isValid) {
                lastValidSpeed = middle
                right = middle - 1
            } else {
                left = middle + 1
            }
        }
        return lastValidSpeed
    }

    /**
     * When is it impossible to find a speed to arrive at the destination under at most [maxHours]?
     * Each train except from the last takes at the minimum 1 hour, the last takes distance/speed with decimal hours,
     * the minimum to consider is under 0.01, since maxHours min decimal part is 0.01
     *
     * So the fastest we can go (min hours) is = (train.size - 1) + 0.01
     *
     * If [maxHours] < (trainDistances.size - 0.99) => return -1
     *
     * Therefore, since we do all cases where there's no valid answer in O(1), we can just return the left pointer since
     * it will be guaranteed the min valid speed => get rid of the [lastValidSpeed] variable as well.
     */
    fun efficientCleaner(trainDistances: IntArray, maxHours: Double): Int {
        if (maxHours < trainDistances.size - 0.99) return -1

        var left = 1
        var right = max(trainDistances.maxOf { it }, trainDistances.last() * 100) + 1
        while (left <= right) {
            val middle = left + (right - left) / 2
            val isValid = trainDistances.isSpeedValid(speed = middle.toDouble(), maxHours = maxHours)
            if (isValid) {
                right = middle - 1
            } else {
                left = middle + 1
            }
        }
        return left
    }

    private fun IntArray.isSpeedValid(speed: Double, maxHours: Double): Boolean {
        var totalHours = 0.0
        for (i in 0 until size - 1) {
            val distance = this[i]
            totalHours += ceil(distance / speed).toInt()
        }
        totalHours += get(size - 1) / speed
        return totalHours <= maxHours
    }
}

fun main() {
    println(
        MinimumSpeedToArriveOnTime().efficientCleaner(
//            trainDistances = intArrayOf(6, 10, 5, 1, 8, 9, 2),
//            maxHours = 34.0,
            trainDistances = intArrayOf(1, 3, 2),
            maxHours = 1.9,
        )
        // s=3 => 1, 1, 2/3=0.66 => 2.66
    )
}
