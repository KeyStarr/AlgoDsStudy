package com.keystarr.algorithm

import java.util.PriorityQueue

/**
 *
 *
 * given: we have exactly [numberOfSeats] seats, each labeled from 1 to [numberOfSeats].
 * constraints:
 *  - 1 <= numberOfSeats <= 10^5;
 *  - 1 <= seatNumber <= numberOfSeats;
 *  - for each [reserve] call its guaranteed we have at least one unreserved seat;
 *  - for each [unreserve] call its guaranteed the seat [seatNumber] will be reserved;
 *  - at most 10^5 calls to both methods.
 *
 * trivial: use a BooleanArray(size = numberOfSeats):
 *  - [reserve] is we have to iterate through [availableSeatsMinHeap] and find the first available one, worst is n iterations
 *   so average/worst O(n) time;
 *  - [unreserve] is simple random access => O(1) time always;
 *  - in total O(n) space always.
 *
 * --------
 *
 * can we improve reserve time to O(logn) or even pure/amortized O(1)?
 *
 * in [reserve] we need to continuously get the next min element out of the available ones, in [unreserve] we add a new
 *  element into the available ones => so the collection is modified live
 *  => we need to repeatedly get mins from a live modified collection
 *  => use a minHeap!
 *
 * Heap:
 *  1. we may generate [numberOfSeats] numbers and add all into the min heap => heapify binary heap for O(n) time;
 *  2. [reserve] remove the min element from the heap. Worst is we have all elements so O(logn) time;
 *  3. [unreserve] add the element back into the heap. Worst is we have n-1 elements so O(logn) time.
 *  => #1 across all calls that heapify is kinda amortized? worst we have 10^5 to reserve and 10^5 available seats,
 *   so each tick of heapify could be associated with a single call giving O(1) amortized time for the heapify across all calls.
 *  => resulting time is O(logn) for each [reserve] and O(logn) for each [unreserve].
 *   and resulting space is O(n) always, cause we always start with that space.
 *
 * across all calls, m=max number of calls (across all methods)
 * Total Time: O(n + m*logn)
 *  - n for the initial heapify;
 *  - either method costs average/worst O(logn) for a single call.
 * Total Space: O(n)
 *
 *
 * ---------------
 *
 * approach #3 without pre-init
 *
 * we can only reserve the MINIMUM seat, not a random seat! And only unreserve ANY reserved seat so the fractured
 * seats reserve state WILL ALWAYS be below some minimum X seat. There are never fractured seats fragments above some value X
 * => keep track of that minimum, and store all values below it in the heap as they return.
 *
 * Time: O(m*logn)
 * Space: O(n)
 *
 *
 * Final notes:
 *  - designed approaches 1 and 2 and implemented the 2nd in 15 mins;
 *  -
 *
 * Value gained:
 *  -
 */
class SeatReservationManager(private val numberOfSeats: Int) {

    private val availableSeatsMinHeap = PriorityQueue((1..numberOfSeats).toList())

    // TODO: solve with the ordered set

    /**
     * Goal: return the smallest number unreserved seat
     *
     */
    fun reserve(): Int = availableSeatsMinHeap.remove()

    /**
     * Goal: return the [seatNumber] seat into the available pool.
     */
    fun unreserve(seatNumber: Int) {
        availableSeatsMinHeap.add(seatNumber)
    }
}
