package com.keystarr.algorithm.array.twopointers

import kotlin.math.max
import kotlin.math.min

/**
 * 💣⭐️ RETRY LATER, failed by myself. Great problem for the best pair of 2 elements on 2 pointers.
 * LC-11 https://leetcode.com/problems/container-with-most-water/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= heights.sizes <= 10^5
 *  • 0 <= height\[i] <= 10^4
 *
 * Final notes:
 *  • ⚠️ came up with a brute-force on the spot, but couldn't find an efficient solution in 50 mins => gave up and read the solution
 *   => didn't even understand it => spent another 1h with breaks for diffused thinking to understand it. A combination of
 *   trying 5-6 different explanations and dry-running the solution algorithm helped to FINALLY clearly understand why the
 *   core idea of the solution works 🔥;
 *  • correctly evaluated that problem to require a solution of finding the best combination=pair of elements +
 *   tried all known techniques to me for optimizing that BUT FORGOT ABOUT 2 POINTERS, TO EVEN TRY IT HERE!
 *  • in total with understanding the right solution invested 2h.
 *
 * Value gained:
 *  • reinforced that 2 pointers is a good candidate approach to try for problems asking for "the best pair" of elements;
 *   => in the future when faced with "find the best pair of elements", consider giving "2 pointers" a try, a priority.
 *  • practiced solving a 2 pointers problem.
 */
class ContainerWithMostWater {

    // TODO: retry solving by myself in 1-2 weeks, the intuition is there already

    /**
     * problem rephrase:
     *  - given:
     *   - height IntArray, which defines lines that are drawn on the 2D plane. Each line's endpoints are (i,0) and (i,height\[i])
     *  - goal: return the max valid area between any two lines.
     *   valid = it is an area of a rectangle with coordinates:
     *    left bottom (k,0)
     *    top bottom (k, min(height[k],height[g]))
     *    right bottom (g,0)
     *    right top (g, min(height[k], height[g]))
     *
     * what factors do influence the result to max?
     *  - height of the common line = min(height\[k], height\[g])
     *  - width = distance by X between the lines = g-k
     *
     * approach:
     *  - the problem asks to find the best (max) combination of elements according to some validity (max rectangle area) rules;
     *
     *  => we could try a brute force, simply try each line with each, compute the area for each and return the max result
     *   time: O(n^2)
     *   space: O(1)
     *
     *
     *  => how to optimize the selection process? say we have the ith line - how to in O(1) or O(logn) time know exactly
     *   which other lines give max answer for it?
     *
     *  sort the heights? but the width matters too (the index)
     *
     *  can we do binary search on solution spaces?
     *   - what is max boundary?
     *    max width is (n-0)
     *    max COMMON height is heights.max()
     *   - what is min boundary?
     *    min width is 1, cause i strictly is non-increasing
     *    min COMMON height is heights.min(), in general min is 0
     *   - do half elimination properties stand?
     *   assume we've sorted heights and kept the original indices as the "x" variable, sorted via "y" first "x" later
     *   so x(x=9,y=1),(x=7,y=39),x(7,y=4) etc
     *
     *    assume we try to look for area = (heights.max() - heights.min)/2
     *    we take heights[middle] as the first line
     *    then we iterate through the heights and find the current max area
     *
     *    if targetArea > currentMaxArea => does it mean that target can't be left of it?
     *
     *
     * pre-process, utilize a hashmap?
     *  if we have gth line, with x=g, y=height\[g], then what we be the best match for it?
     *  it would be a line k with either minimum or maximum x
     *  and minimum or maximum y
     *  no, it could be x and y something in the middle but still the best area
     *
     * -----------------------------
     *
     * Proceeded to implement the brute cause couldn't come up with a faster solution in 20 mins
     * Result => as to be expected, TLE on a case near the end.
     *
     * Edge cases:
     *  - all heights\[i] == 0 => return 0 => correct, cause we compute height by min;
     *  - at least 2 lines = 2 elements in heights => we'll always have an answer.
     *
     * Time: O(n^2)
     * Space: O(1)
     */
    fun brute(heights: IntArray): Int {
        var maxArea = -1
        heights.forEachIndexed { x, y ->
            for (otherX in x + 1 until heights.size) {
                if (x == otherX) continue

                val otherY = heights[otherX]
                val area = (otherX - x) * min(otherY, y)
                if (area > maxArea) maxArea = area
            }
        }
        return maxArea
    }

    /**
     * we have line with x=i and y=heights\[i]
     *
     * how to find the other line which gives max area in O(1) or O(logn) time?
     *
     * area = width * height
     *
     * max height for heights\[i] are all lines which >= heights\[i], or if none, the max height line below it
     * max width for the ith line is the absolute farthest line by x (farthest from either left or right)
     *
     * max height may not have max width and vice versa
     * =>
     * - binary search on solution spaces (take a line, assume an area and try to find it) doesn't seem to work, cause I couldn't come
     *  up with a sorting that guarantees the "eliminate half" properties of that solution space;
     * - couldn't come up with any kinda pre-processing, to compute something for each line and, say, store it in a hashmap
     *  as a potential match => and then iterate through and find the best match for that particular line if it exists;
     * - don't seem to recognize any more tools that I know of to efficiently finding the best pair of elements.
     * 50 mins total solution time
     *
     * --------------------------------------
     *
     * read submissions, watched a couple of videos and AFTER, like, a solid 40 mins in silence allowing diffused thinking
     * to do its work, staring at the ceiling with a stoned gaze... I came back, re-read a couple of explanations, dry-ran
     * ...AND IT CLICKED!
     *
     * Of course, obviously!
     *
     * Use two pointers, init at both ends of the array:
     * 1. if heights\[left] < height\[right] then:
     *  - if there was a bigger line with ind > right, and if there was no bigger line than left with ind < left, then
     *   current combination would have been impossible, cause left is current checked lines from left max and its still
     *   less than right => we would have moved left only and have never moved right yet;
     *  - as to lines with ind < right, there's no point to check current left with them, because the height will be at most
     *   left (as its now a bottlenect) and the width will only decrease => all pairs with current left and any ind < right
     *   will guaranteed have less area (TL;DR: at most same height, strictly less width;
     * 2. same core idea for the case of heights\[left] > height\[right], only vice versa for the right;
     * 3. if heights\[left] == heights\[right] => neither (left,right-1) or (left+1,right) would be the answer, cause both
     *  will always have square less than current, because the height is capped at current height\[left]=height\[right],
     *  and the width will only be less.
     *
     * Time: always O(n)
     * Space: always O(1)
     */
    fun efficient(heights: IntArray): Int {
        var left = 0
        var right = heights.size - 1
        var maxArea = 0
        while (left < right) {
            val leftHeight = heights[left]
            val rightHeight = heights[right]
            val area = (right - left) * min(leftHeight, rightHeight)
            maxArea = max(area, maxArea)
            when {
                leftHeight == rightHeight -> {
                    right--
                    left++
                }
                leftHeight < rightHeight -> left++
                else -> right--
            }
        }
        return maxArea
    }
}

fun main() {
    println(
        ContainerWithMostWater().efficient(heights = intArrayOf(1, 8, 6, 2, 5, 4, 8, 3, 7))
    )
}
