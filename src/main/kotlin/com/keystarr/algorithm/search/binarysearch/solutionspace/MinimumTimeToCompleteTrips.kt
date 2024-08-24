package com.keystarr.algorithm.search.binarysearch.solutionspace

/**
 * LC-2187 https://leetcode.com/problems/minimum-time-to-complete-trips/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= times.size <= 10^5;
 *  • 1 <= time\[i], totalTrips <= 10^7.
 *
 * Final notes:
 *  • 🏅 done [efficient] by myself in 40 mins, 1st try submit;
 *   recognized a BSoSS in a simulation type problem myself 🏅! (been having trouble with recognition of BSoSS in general lately)
 *   ⚠️ should do faster though, like 20 mins is a target for binary search on solution space mediums. But is OK for now.
 *    spent a lot of time understanding the half elimination properties, like what these should be, no less or no greater and why,
 *    and exactly what the checking resul would be, which side to go => definitely need more practice designing binary
 *    search on solution spaces solutions 🔥 for consistent interview results;
 *  • ⚠️ still feel uncertainty over the edge conditions (<=, +1 where) for binary search on solution spaces, but was able
 *   to half-intuitively dry run and see which way to put, though didn't prove decisively. Need to put in more work to internalize
 *   and understand that, for consistent top interview timings follow-up answers, but not now.
 *
 * Value gained:
 *  • the problem category might be named as "simulation" - we need to find the min time mark when the simulation achieves
 *   the target metric, and the metric may be updated with each unit of time passed.
 *   => practiced solving a simulation problem on finding the minimum with a binary search on solution spaces.
 *
 * ----
 *
 * Don't think we need to re-solve since I've confidently enough and quickly enough recognized the problem, and quickly
 * enough solved it, though with kinda shaky proofs for boundary cases. And could've done faster, but its ok
 * => note that I can improve on these aspects of binary search on solution spaces, but accept this for now.
 */
class MinimumTimeToCompleteTrips {

    /**
     * problem rephrase:
     *  - given:
     *   - time: IntArray, where time\[i] is the amount of time required for the ith bus to complete 1 trip;
     *   - totalTrips: Int, the target amount of trips.
     *  - rules for a step: with each unit of time all buses make progress by exactly 1, and when some bus completes the trip,
     *   it starts a new trip straight away with the same time\[i] cost.
     *  Goal: return the minimum amount of time required to complete >= totalTrips across all buses.
     *
     * times=1,2,3
     * targetTrips=5
     *
     * unit of time
     * 1 => 0 1 2, tripsDone=1
     * 2 => 0 0 1, tripsDone=3
     * 3 => 0 1 0, tripsDone=5, 5 == target => return 3
     *
     * minimum and kinda combination? try binary search on solution spaces? for time O(nlogn) space O(1)
     *
     * might there be a time O(n) solution?
     *  iterate through [times], how many trips does bus ith need to make for us to achieve the goal?
     *  weird question, since other buses are moving too, how are we gonna account for them?
     *  5*1=5 hours for the 0th bus
     *  5*2=10 hours for the 1st bus
     *  don't think there's a way to tell at the ith bus, to somehow in O(1) update the answer, since all buses move
     *  independently.
     *
     * -----
     *
     * brute force:
     *  counters = IntArray(size=times.size)
     *  while (currentTrips < targetTrips):
     *   counters.forEach:
     *    if counter == 0:
     *     currentTrips++
     *     counter=times[i]
     *   timePassed++
     * Time: average/worst O((m/n)*k), worst is O(k^2)
     *  worst a single slowest bus possible and max number of trips => m * k, where m=the bus time, k=number of trips
     *  n=number of buses, times.size
     * Space: O(n)
     *
     * ----
     *
     * improve => try binary search on solution spaces
     *
     * - answer max/min boundaries:
     *  min: 1
     *   assume all buses trip time is the min (1), and we have exactly totalTrips amount of buses (no more makes sense,
     *    cause trip time for a bus is an integer) => min possible answer is 1 hour
     *  max:
     *   assume we have the slowest buses (time for each is 10^7 for 1 trip) and max number of trips (10^7) and min
     *   number of buses (1) => 10^7 * 10^7 = 10^14 hours
     *
     *   can we reduce it based on the input somehow?
     *   well we could find the fastest bus in the input, and then max would be if we'd just run only this bus totalTrips.
     *   worst then is still that same above, on average it would be bounded.
     *
     * - half elimination properties:
     *  iteration goal: can we achieve no less than targetTrips in X hours?
     *  - if yes => for any value greater than X it's also true, since the number of trips would either stay same or only grow;
     *   => reduce, go left
     *  - if no => for any value less than X its also false, since the number of trips would either stay same or only reduce.
     *   => increase, go right
     *
     *  how do we do check it?
     *   times=1,2,3 targetTrips=5
     *   suppose l=1 r=times.min()*targetTrips=5
     *   m=5/1=5
     *
     *   is it possible to achieve 5 in equal or greater (no less) than X=5 hours?
     *   5/1=5   5/2=2   5/3=1
     *   totalTrips=8 > target YES, in that time we can complete no less than target trips
     *
     *   yep, checked in O(n) time.
     *
     * => binary search on solution spaces is possible here.
     *
     * Edge cases:
     *  - product => worst result is a single slowest bus and max target trips => 10^7 * 10^7 = 10^14 => need Long for result to fit.
     *
     * goal->min
     * --+++
     *
     * Time: average/worst O(n * log(m*k)), where m=slowest bus trip time, k=[targetTrips].
     *  - each binary search iteration costs O(n) time;
     *  - how many iterations can we have? worst is the min hours value is on the edge, so its log2(right), and
     *   worst right = single slowest bus possible and max target trips possible => O(m*k), where m=bus time, k=target trips
     * Space: always O(1)
     */
    fun efficient(times: IntArray, targetTrips: Int): Long {
        var left = 1L
        var right = times.minOf { it } * targetTrips.toLong()
        while (left <= right) {
            val middle = left + (right - left) / 2
            if (times.totalTripsAtHour(hourMark = middle) >= targetTrips) {
                right = middle - 1
            } else {
                left = middle + 1
            }
        }
        return left
    }

    private fun IntArray.totalTripsAtHour(hourMark: Long): Long {
        var totalTrips = 0L
        forEach { tripHours -> totalTrips += hourMark / tripHours }
        return totalTrips
    }
}
