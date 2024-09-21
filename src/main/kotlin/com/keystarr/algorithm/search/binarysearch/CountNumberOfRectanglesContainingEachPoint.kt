package com.keystarr.algorithm.search.binarysearch

/**
 * LC-2250 https://leetcode.com/problems/count-number-of-rectangles-containing-each-point/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= rectangles.size, points.size <= 5 * 10^4;
 *  • rectangles\[i].size == points\[j].size == 2;
 *  • 1 <= width, x <= 10^9;
 *  • 1 <= height, y <= 100;
 *  • all the rectangles and points are unique.
 *
 * Final notes:
 *  • ⚠️ 30 min mark and no efficient solution in sight (don't know where to dig) => try hint #1;
 *  • ⚠️ solved at an about 1h mark with the hint #1 used;
 *  • ⚠️⚠️ was really not confident with the leftmost binary search on insertion index algorithm (need more practice? understand it deeper in general, play with?);
 *  • reasoned by myself very close to the actual efficient solution:
 *
 *   what I got right:
 *    - that we have to somehow pre-process the input such that we can, for each point, efficiently count all valid rectangles;
 *    - that probably "efficiently calculate" means that we need to find a valid rectangle for the current point such that
 *     we can count all rectangles further after it in the array as the valid ones.
 *
 *   where I went wrong:
 *    - I fixated upon finding a solution that would adhere to the above properties but such that we'd process each point
 *     in O(1) time, i.o. that we'd somehow pre-process rectangles in a way that finding 1 rectangle would mean all further ones
 *     are valid.
 *     That turned out to be a dead-end due to that the may be height but width may not be, and in fact the width may be different
 *     valid/invalid for all valid heights
 *
 *    what I should have probably done better:
 *     - I noticed that 100 max height was weird given 10^9 max width, but didn't do anything with it
 *      => 🔥 I should've given it more thought, and should've relaxed the constraints I've set for myself and went for a
 *      less optimal solution, it was actually 1 step away, if I just tried to apply that principle above but for all
 *      widths of given valid heights 🔥
 *
 *  • well, not sure that I could've done it right though. And not exactly sure how to generalize this for the future.
 *
 * Value gained:
 *  • practiced solving a "find all valid combinations" type problem using the leftmost on duplicates insertion point binary search.
 */
class CountNumberOfRectanglesContainingEachPoint {

    // TODO: full re-solve in 1-3 weeks

    /**
     * Brute force:
     *  - iterate through all points:
     *   - iterate through all rectangles, if a rectangle contains the points => result\[i]++
     * Time: always O(n*m)
     * Space: always O(1) if we don't count the result
     *
     * -------- how to optimize?
     *
     * can we somehow pre-process rectangles, so that if some minimum rectangle X contains point Y then we know in O(1) time
     *  all other rectangles that contain said point?
     *
     * all rectangles share the bottom-left corner to be (0,0) =>
     *
     *
     * when does rectangle X contain rectangle Y?
     * ah, rectangles may intersect though
     *
     * e.g. (w=1,h=3) and (w=2, h=1)
     *
     * ---
     *   |
     * --|-----
     *   |    |
     *
     * can we try greedy here?
     *
     *  - if rectangle X contains rectangle Y => we can preprocess and in O(1) time get all fully contained rectangles
     *   for some point;
     *  - but what to do if rectangle X only intersects with rectangle Y?
     *   then there is some intersection rectangle such that if the point is inside it => we count both rectangles
     *   and there area 2 rectangles such that they are non-intersecting, individual
     *
     * ------
     * can we pre-process all rectangles according to the observations made above? so that for each point we can in O(1) time
     *  yield all rectangles that contain it?
     *
     * try sorting rectangles non-decreasing by their area?
     *
     * ----
     *
     * try sorting rectangles by their width, and secondary order by their height + sort points by the same principle?
     *  then if a point X is contained in the rectangle Y, then this point is contained in all further rectangles, since
     *  each further rectangle will have width and height no less than Y?
     *
     *  WRONG, since width is guaranteed to be no less but height MAY be less
     *
     * r = [1,2] [2,3] [2,5]
     * p = [1,4] [2,1]
     *
     *
     * basically for a point with x=X and y=Y we need to find all rectangles such that r.width >= X and r.height >= Y
     * how to do that in O(1) time?
     *
     * ------ try another angle - can we group points somehow, preprocess them and iterate through the rectangles instead?
     *
     * same problem here, we can have points [2, 3] [4 1]
     * I don't think we can find a rule such as "if point X is contained within the current rectangle, then all further points
     *  are contained in it as well" due to the potential height/width difference
     *
     * -----------------------------------------------------------
     *
     * taken hint #1 - max height/y is only 100 => we can for each point iterate through all heights, what?
     *
     * I suppose we could pre-process rectangles into a map/array height -> list of widths sorted
     * then we could, as we take a point with height = H, we might find all rectangles with no less than this height
     * then we could through all heightToWidths\[j], j=[H, size-1] do binary search, find the width no less than the point's
     * width and count+=heightsToWidths\[j].size - g
     *
     * Time: average/worst O(mlogm + n*100*logm) = O(m*logm + n*logm) = O(logm(n+m)), m=rectangles.size and n=points.size
     *  - preprocessing:
     *   - sort rectangles by their width O(mlogm);
     *   - add to the lists of heightToWidths O(m)
     *  - we iterate through n points;
     *      - for each point we do up to 100 iterations;
     *       - for each such iteration we do binary search on a size ~m (rectangles) => logm
     *        ! binary search for either the leftmost index or the insertion point, since:
     *         - if point.x < widths[0]: means that all rectangles in widths contains the point, we need to count them all;
     *         - if point.x > widths.last(): no rectangles in widths contain the point, we should return the size as the insertion point
     *          => count += size - bs(target) = size - size = 0, correct
     *         - anything in between: leftmost index since count += size - bs(target) would then count all the rectangles
     *          with the equal width and all the ones with the greater width as well.
     * Space: always O(m)
     *  - heightsToWidths array has always 101 elements and across all lists in it all items sum up to n => O(m);
     *  - results we don't count;
     *  - sorting rectangles O(logm)
     *
     * ----- optimization
     *
     * further, also if we do the same preprocessing with the points:
     *  - sort the points by their width O(nlogn);
     *  - add to the lists of heightToWidths O(n).
     *
     * I don't think this makes this any better, since we'd already have O(nlogn+mlogm) then not even going further
     * => the solution above is the best we can do
     */
    fun efficient(rectangles: Array<IntArray>, points: Array<IntArray>): IntArray {
        rectangles.sortWith { r1, r2 -> r1[0] - r2[0] } // sort rectangles by width

        val heightToWidths = Array(size = 101) { mutableListOf<Int>() }
        rectangles.forEach { rectangle ->
            val (width, height) = rectangle
            heightToWidths[height].add(width)
        }

        val results = IntArray(size = points.size)
        points.forEachIndexed { ind, point ->
            val (x, y) = point
            var count = 0
            for (height in y until heightToWidths.size) {
                val widths = heightToWidths[height]
                val minValidWidthInd = widths.binarySearch(target = x)
                if (minValidWidthInd == -1) continue
                count += widths.size - minValidWidthInd
            }
            results[ind] = count
        }
        return results
    }

    /**
     * @return either the leftmost index of an element equal to [target] or the insertion index
     *  (edges: if less than all => 0, if greater than all => size)
     */
    private fun List<Int>.binarySearch(target: Int): Int {
        var left = 0
        var right = size
        while (left < right) {
            val middleInd = left + (right - left) / 2
            val middleNum = get(middleInd)
            when {
                target <= middleNum -> right = middleInd
                else -> left = middleInd + 1
            }
        }
        return right
    }
}

fun main() {
    println(
        CountNumberOfRectanglesContainingEachPoint().efficient(
            rectangles = arrayOf(
                intArrayOf(1, 2),
                intArrayOf(2, 3),
                intArrayOf(2, 5),
            ),
            points = arrayOf(
                intArrayOf(2, 1),
                intArrayOf(1, 4),
            )
        ).contentToString()
    )
}
