package com.keystarr.algorithm.graph

import java.util.*

/**
 * LC-542 https://leetcode.com/problems/01-matrix/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= matrix.length, matrix[0].length <= 10^4;
 *  • 1 <= total number of nodes <= 10^4;
 *  • mat\[i]\[j] is either 0 or 1;
 *  • there is at least one 0 in the matrix.
 *
 * Value gained:
 *  • wow, the trick to the problem is a counter-intuitive twist, kinda like [com.keystarr.algorithm.hashing.hashmap.TwoSum],
 *   in that we start from sorta the opposite of what the problem is asking us to do (two sum: find two numbers that add
 *   up to target => we pre-compute diffs and look for a diff. here: fill each cell with distance to closest 0 => but
 *   we don't start finding the closest 0 from each cell, instead we start from 0's themselves and update all cells around them
 *   with current distance)
 *  • WOW, IT CAN BE USEFUL TO START BFS FROM not just one, but MULTIPLE STARTING NODES! never thought of that;
 *  • solved the problem via a form of pre-processing - found the 0's first.
 */
class O1Matrix {

    private val allowedDirections = arrayOf(
        intArrayOf(-1, 0), // top
        intArrayOf(1, 0), // bottom
        intArrayOf(0, -1), // left
        intArrayOf(0, 1), // right
    )

    /**
     * Input - a binary matrix, where each element is a node, and edges are direct bounds to nearest cells (no diagonal,
     * only top,left,right,bottom)
     *
     * Goal - return a matrix of same m*n size, where result\[i]\[j] is the distance to the nearest 0 in the original
     * matrix from element (i,j).
     *
     * Problem decomposition:
     *  - if matrix\[i]\[j] == 0 => 0
     *  - else => find the distance to the nearest 0.
     *
     * Naive - iterate through each element of the original matrix, perform BFS from it until we reach a 0 and fill the
     * distance to the result matrix.
     * Time: O(m*n * (m*n + e)) = O(m^2*n^2)
     *  - we need to check m*n elements
     *  - for each element BFS cost is O(m*n + e), worst e=4*m*n
     *
     * --------------------------------------------
     *
     * How can we improve finding the nearest 0 for each element to O(1)? Pre-process somehow?
     *
     * (checked the DSA course article)
     *
     * Idea:
     *  - find all 0's first;
     *  - perform BFS at the same time from all 0's, that is, visit all nodes with distance G from each respective 0 element
     *      in a single outer while iteration:
     *      - if some node is already seen => don't go there, don't override;
     *      - fill result[i][j] with currentDistance for each actually visited node.
     *  => since we propagate from each 0, then it is guaranteed that each node will be filled with distance to it's closest 0.
     *
     * Loop invariant for while IF we also would fill result elements for each element which is a 0:
     *  "before the start of each while iteration all '1' in the matrix with distance less than `currentDistance` from all
     *  0 elements are filled in `result` with the distance to their closest 0's".
     *  init: currentDistance<0 => no elements have that distance => result is empty
     *  maintenance:
     *      currentDistance<1 then all 0's elements are filled with 0 in result
     *      currentDistance<2 then also 1's with distance of 1 to 0's are filled with 1 in result
     *      etc
     *  termination:
     *      queue is empty => we have visited each element once => all elements in the result are filled with the distance
     *      to their closest 0's.
     *
     * Edge cases:
     *  - min number of nodes: 1 =>
     *  - all elements except one are 1's =>
     *  - all elements are 0's =>
     *  - there is always at least one 0 => a valid answer exists for each node.
     *
     * Time: always O(n*m+e) = O(n*m)
     *  - pre-processing=finding all 0's always takes O(n*m) time
     *  - then we visit each node exactly once, and try each edge exactly once
     *  - e = number of edges is at most ~4*n*m
     * Space: always O(n*m) for seen
     */
    fun efficient(matrix: Array<IntArray>): Array<IntArray> {
        val queue: Queue<Node> = ArrayDeque()
        val seen = Array(size = matrix.size) { BooleanArray(size = matrix[0].size) }
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                if (matrix[i][j] == 0) {
                    queue.add(Node(i = i, j = j))
                    seen[i][j] = true
                }
            }
        }

        var currentDistance = 0
        while (queue.isNotEmpty()) {
            val currentDistanceNodesAmount = queue.size
            repeat(currentDistanceNodesAmount) {
                val node = queue.remove()
                matrix[node.i][node.j] = currentDistance
                allowedDirections.forEach { direction ->
                    val newI = node.i + direction[0]
                    val newJ = node.j + direction[1]
                    if (matrix.doesCellExist(newI, newJ) && !seen[newI][newJ]) {
                        queue.add(Node(newI, newJ))
                        seen[newI][newJ] = true
                    }
                }
            }
            currentDistance++
        }

        return matrix
    }

    private fun Array<IntArray>.doesCellExist(i: Int, j: Int) = (i in indices) && (j in get(0).indices)

    private class Node(
        val i: Int,
        val j: Int,
    )
}

fun main() {
    println(
        O1Matrix().efficient(
            arrayOf(
                intArrayOf(0, 0, 0),
                intArrayOf(0, 1, 0),
                intArrayOf(0, 0, 0),
            )
        ).contentDeepToString()
    )
}
