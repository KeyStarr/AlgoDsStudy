package com.keystarr.algorithm.graph

import java.util.ArrayDeque
import java.util.Queue

/**
 * LC-1129 https://leetcode.com/problems/shortest-path-with-alternating-colors/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= n <= 100
 *  • 0 <= redEdges.length, blueEdges.length <= 400
 *  • redEdges\[i].length == blueEdges\[j].length == 2
 *
 * Value gained:
 *  • (!!) practiced and reinforced the pattern of that a node can be visited multiple times, BUT only with different states
 *   of the context specific to the path. Same idea as in [ShortestPathInAGridWithObstaclesElimination];
 *  • practiced graph BFS with tracking distance to each node via [NodeVariant] state in the Queue instead of the out of
 *   `while` single variable like `currentDistance` and no inner for loop.
 */
class ShortestPathWithAlternatingColors {

    /**
     * Problem rephrase:
     * "Given 2 arrays of directed edges which describe edges between [0..n-1] nodes, for each node find the shortest valid
     * path length from 0 to it, fill all such lengths into the result array. If path doesn't exist from 0 to X, then set -1.
     * Valid path = starts from 0 end with node X, edges of 2 arrays alternate along the way!!!"
     *
     * Directed graph, represented via 2 arrays of edges.
     * There are also self edges => we do need them! We can use a self-edge to technically "alternate colors" and then
     * use the path to next node of a different color!!
     *
     * What are parallel edges??
     *
     * Shortest path => use BFS. Array of edges => pre-process into a HashMap.
     *
     * Idea:
     *  - pre-process 2 arrays into a single map node->edges, HashMap<Int,Edge>, where Edge=(targetNode: Int, isRed: Boolean)
     *  - start BFS at 0:
     *      - the first time we reach a node we set result[node]=currentDistance
     *      - seen is a 2D array, 3rd dimension being the color of the last edge that we've used to get there. Cause it is
     *      a new state then and limits the future path differently.
     *      - for each node in a queue we store distance distance=the number of edges it took to get there AND the last edge's color.
     *
     * Edge cases:
     *  - result[0] is always 0;
     *  - n == 1 => return [0], correct as-is, but could add an early return for clarity;
     *  - redEdges.length==0 and blueEdges.length>0 => we can only reach nodes one step away from 0, the result for others will be -1;
     *  - redEdges.length>0 and blueEdges.length==0 => same as with the previous case;
     *  - there are NO edges (redEdges.length == 0 and blueEdges.length == 0) => return result of size n filled with 0's => correct;
     *  - there is a valid path between 0 and X only if a self-edge is used => we use self-edges also and to do that allow
     *      visiting nodes twice if using edges of both colors once to it.
     */
    fun solution(numberOfNodes: Int, redEdges: Array<IntArray>, blueEdges: Array<IntArray>): IntArray {
        val nodeToEdgesMap = mutableMapOf<Int, MutableList<Edge>>().apply {
            fill(redEdges, isRed = true)
            fill(blueEdges, isRed = false)
        }

        // init with -1, so nodes we don't reach already have correct answer
        val result = IntArray(size = numberOfNodes) { -1 }.apply { set(0, 0) }
        val queue: Queue<NodeVariant> = ArrayDeque()
        // 0 is red isSeen, 1 is blue isSeen
        val seen = Array(size = numberOfNodes) { BooleanArray(2) }.apply {
            val zeroNodeStates = get(0)
            zeroNodeStates[0] = true
            zeroNodeStates[1] = true
        }

        val edgesFromZero = nodeToEdgesMap[0] ?: return result
        edgesFromZero.forEach { edge ->
            queue.add(
                NodeVariant(
                    edge.targetNode,
                    edgesTaken = 1,
                    wasLastEdgeRed = edge.isRed
                )
            )
        }

        while (queue.isNotEmpty()) {
            val nodeVariant = queue.remove()
            val node = nodeVariant.node

            if (result[node] == -1) result[node] = nodeVariant.edgesTaken

            val edges = nodeToEdgesMap[node]
            edges?.forEach { edge ->
                if (edge.isRed == nodeVariant.wasLastEdgeRed) return@forEach
                val isRedInd = if (edge.isRed) 1 else 0
                if (seen[edge.targetNode][isRedInd]) return@forEach

                queue.add(
                    NodeVariant(
                        node = edge.targetNode,
                        edgesTaken = nodeVariant.edgesTaken + 1,
                        wasLastEdgeRed = edge.isRed,
                    )
                )
                seen[edge.targetNode][isRedInd] = true
            }
        }

        return result
    }

    private fun MutableMap<Int, MutableList<Edge>>.fill(edges: Array<IntArray>, isRed: Boolean) {
        edges.forEach { edge ->
            val startNode = edge[0]
            val endNode = edge[1]
            getOrPut(startNode) { mutableListOf() }.add(Edge(targetNode = endNode, isRed = isRed))
        }
    }

    private class Edge(
        val targetNode: Int,
        val isRed: Boolean,
    )

    private class NodeVariant(
        val node: Int,
        val edgesTaken: Int,
        val wasLastEdgeRed: Boolean,
    )
}

fun main() {
    println(
        ShortestPathWithAlternatingColors().solution(
            numberOfNodes = 3,
            redEdges = arrayOf(intArrayOf(0, 1)),
            blueEdges = arrayOf(intArrayOf(2, 1)),
        ).contentToString()
    )
}
