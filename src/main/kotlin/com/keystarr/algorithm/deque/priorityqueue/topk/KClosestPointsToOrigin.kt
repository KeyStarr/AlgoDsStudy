package com.keystarr.algorithm.deque.priorityqueue.topk

import java.util.PriorityQueue

/**
 * LC-973 https://leetcode.com/problems/k-closest-points-to-origin/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= k <= points.length <= 10^4
 *  - -10^4 <= xi, yi <= 10^4
 *
 * Final notes:
 *  - interesting, [maxHeapLessSpace] took 657 ms, [maxHeap] took 717 ms out of 1 submission each => is my assumption
 *   about preprocessing having a better time const wrong?
 *   60 ms seems beyond leet runtime fluctuations and its a diff between 60% percentile and 30% percentile of fastest
 *   solutions. Will figure out later;
 * - actually in-place priority calculation [maxHeapLessSpace] is significantly less code!
 * - done [maxHeap] by myself in ~20 mins.
 *
 * Value gained:
 *  - practiced recognizing the pattern, and solving a top k problem with a maxHeap;
 *  - got more evidence in favor of possibly avoiding (at least when its a matter of O(1) re-computation) pre-processing
 *   for such problems and computing the priority each time in the Comparator.
 */
class KClosestPointsToOrigin {

    private val originPoint = intArrayOf(0, 0)

    /**
     * (suboptimal)
     * Goal - find top k (closest) points to the point (0,0) on a 2D plane closest BY the Euclidean distance
     *  => "return k points with minimum Euclidean distances from (0,0)"
     *
     * Top k => try a heap.
     *
     * Idea:
     *  - pre-process: calculate Euclidean distances for all [points] to (0,0), store as PointWithDistance(point, distance)
     *   we can skip the sqrt cause the arguments will always be here positive integers, so whichever is larger, then its
     *   sqrt would be larger too
     *  - maxHeap = PriorityQueue { o1, o2 -> o2 - o1 } // for descending sorting => max heap
     *  - iterate through distances:
     *      - maxHeap.add(item)
     *      - if (maxHeap.size > k) maxHeap.remove()
     *  - return maxHeap.map { it.point } // cause any order
     *
     * Edge cases:
     *  - inserting the item with the same distance as the top of the heap (heap.size == k AND distances\[i].distance == heap.peek())
     *   => no order for preference is specified, only that "there's guaranteed to be a unique solution"
     *   => assume that we can then take either one (input will never lead to that case????)
     *  - max distance without sqrt is for (10^4, 10^4) => (10^4)^2 + (10^4)^2 = 2 * 10^8 => fits into Int.
     *
     * Time: always O(n * logk)
     * Space: O(n) // for pre-processing, `distances`
     *
     * ---------
     * alternative directions:
     *  - we could skip pre-processing and calculate the Euclidean distance on the fly, in the comparator, but
     *   it would result in a bigger time const. How much? we'd done at most O(logk) comparisons for both add remove, right?
     *  - could use a map distance->point (Map<Int, IntArray>) and a Heap<Int> (of distances) instead of PointWithDistance,
     *   but that would, I predict, have a slightly bigger time const coz of hashing (object are created there too, entries)
     */
    fun maxHeap(points: Array<IntArray>, k: Int): Array<IntArray> {
        val distances = ArrayList<PointWithDistance>(points.size)
        points.forEach { point ->
            distances.add(
                PointWithDistance(
                    point = point,
                    distance = originPoint.distanceToPointWithoutSqrt(point),
                )
            )
        }

        val maxHeap = PriorityQueue<PointWithDistance> { o1, o2 -> o2.distance - o1.distance }
        distances.forEach {
            maxHeap.add(it)
            if (maxHeap.size > k) maxHeap.remove()
        }

        val array = Array<IntArray?>(size = k) { null }
        maxHeap.forEachIndexed { ind, item -> array[ind] = item.point }
        return array as Array<IntArray>
    }

    /**
     * (suboptimal)
     * Time: same as [maxHeap], always O(n*logk) but I think there's at least slightly bigger time const here cause
     *  we potentially re-compute distances for same point multiple times (when adding to / removing from a heap =>
     *  the heap rearranges internally to maintain the heap property => does comparisons again)
     * Space: O(k) only the heap
     */
    fun maxHeapLessSpace(points: Array<IntArray>, k: Int): Array<IntArray> {
        val maxHeap = PriorityQueue<IntArray> { o1, o2 ->
            o2.distanceToPointWithoutSqrt(originPoint) - o1.distanceToPointWithoutSqrt(originPoint)
        }
        points.forEach {
            maxHeap.add(it)
            if (maxHeap.size > k) maxHeap.remove()
        }

        val array = Array<IntArray?>(size = k) { null }
        maxHeap.forEachIndexed { ind, item -> array[ind] = item }
        return array as Array<IntArray>
    }

    private fun IntArray.distanceToPointWithoutSqrt(other: IntArray): Int {
        val xDistance = this[0] - other[0]
        val yDistance = this[1] - other[1]
        return xDistance * xDistance + yDistance * yDistance
    }

    private class PointWithDistance(
        val point: IntArray,
        val distance: Int, // Euclidean but without sqrt applied
    )

    // TODO: solve efficiently at least with a binary search
}

fun main() {
    println(
        KClosestPointsToOrigin().maxHeapLessSpace(
            points = arrayOf(
                intArrayOf(1, 3),
                intArrayOf(-2, 2),
            ),
            k = 1,
        ).contentDeepToString()
    )
}
