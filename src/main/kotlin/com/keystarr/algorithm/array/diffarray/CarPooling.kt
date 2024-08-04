package com.keystarr.algorithm.array.diffarray

import java.util.*

/**
 * LC-1094 https://leetcode.com/problems/car-pooling/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= trips.length <= 10^3
 *  • trips\[i].length == 3
 *  • 1 <= numPassengers(ith) <= 100
 *  • 0 <= from(ith) < to(ith) <= 10^3
 *  • 1 <= capacity <= 10^5
 *
 * Final notes:
 *  • ⚠️⚠️ done [plainSimulation] with 4 failed submissions D: first problem of the day)) gotta be more careful:
 *   • forgot to check if the queue is empty before peeking into it => RuntimeException;
 *   • mistyped the sorting rule for trips, instead of trips[1] (to) wrote trips[2] (from). Meant `to`, ofc;
 *  • done [diffArray] after reading the entire solution concept and dry-running it (didn't understand it before the dry run)
 *   from the DSA course.
 *
 * Value gained:
 *  • 🏆 solved my first problem on the diff array;
 *  • practiced a solution using a minHeap.
 */
class CarPooling {

    /**
     * we don't make choices here, so might just run the problem statement as a simulation, efficiently, on a given input
     *
     * we could sort the array by starting point
     * iterate through the array from the starting point:
     *  trips = trips.sortBy { it.end }
     *  totalCurrentPassengers = trips[0].passengers
     *  activePickups = PriorityQueue<PassengersToEnd>() // minHeap by the trip end value
     *  for i in 1 until trips.size:
     *   trip = trips\[i]
     *   while trip.from >= activePickups.peek().end:
     *    currentPassengers -= activePickups.remove().passengers
     *   totalCurrentPassengers += trip.passengers
     *   if (totalCurrentPassengers > capacity) return false
     *   activePickups.add(trip.passengers to trip.end)
     *  return true
     *
     * Edge cases:
     *  - sum => max total passengers count is 100*10^3 = 10^5 => fits into int
     *  - capacity == 1 => nothing special, correct;
     *  - trips.size == 1 => nothing special, correct
     *
     * Time: always O(nlogn)
     *  - sorting trips O(n*logn)
     *  - main loop:
     *   - priority queue add and remove O(logn)
     *   - inner loop at most O(n) across all outer iterations
     *   - outer loop O(n)
     *   => O(nlogn)
     * Space: average/worst O(n), cause worst we have capacity > totalPassengersCount => we add n elements to the heap.
     */
    fun plainSimulation(trips: Array<IntArray>, capacity: Int): Boolean {
        trips.sortBy { it[1] }
        var totalCurrentPassengers = 0
        val activePickups = PriorityQueue<IntArray> { o1, o2 -> o1[2] - o2[2] } // minHeap by the trip end value
        for (trip in trips) {
            while (activePickups.isNotEmpty() && trip[1] >= activePickups.peek()[2]) {
                totalCurrentPassengers -= activePickups.remove()[0]
            }
            totalCurrentPassengers += trip[0]
            if (totalCurrentPassengers > capacity) return false
            activePickups.add(trip)
        }
        return true
    }

    /**
     * Time: average/worst O(n+m)=O(max(n,m)), where:
     *  - n=trips.size
     *  - m=farthest right value of the trip
     *  (best is when the first value is beyond [capacity], so basically time is simply O(n))
     * Space: always O(m)
     */
    fun diffArray(trips: Array<IntArray>, capacity: Int): Boolean {
        val farthest = trips.maxOf { it[2] }
        val diffArray = IntArray(size = farthest + 1)
        trips.forEach { trip ->
            val (valueChange, left, right) = trip
            diffArray[right] -= valueChange
            diffArray[left] += valueChange
        }

        var currentTotalValue = 0
        diffArray.forEach { valueDiff ->
            currentTotalValue += valueDiff
            if (currentTotalValue > capacity) return false
        }

        return true
    }
}

fun main() {
    println(
        CarPooling().plainSimulation(
            trips = arrayOf(
                intArrayOf(9, 3, 6),
                intArrayOf(8, 1, 7),
                intArrayOf(6, 6, 8),
                intArrayOf(8, 4, 9),
                intArrayOf(4, 2, 9)
            ),
            capacity = 28,
        )
    )
}
