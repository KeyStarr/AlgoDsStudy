package com.keystarr.algorithm.graph.djkstra

import java.util.PriorityQueue

/**
 * LC-743 https://leetcode.com/problems/network-delay-time/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nodesAmount <= 100;
 *  • 1 <= edges.size <= 6000;
 *  • 0 <= edges\[i].weight <= 100;
 *  • no self edges, all edges are unique.
 *
 * Final notes:
 *  • 1st approach => I UNDERSTOOD THE PROBLEM'S STATEMENT WRONG, don't even remember now what it was;
 *  • 2nd approach => AGAIN I UNDERSTOOD THE PROBLEM'S STATEMENT WRONG, this time I thought we must reach all nodes
 *   within a single path)))) (actually, I assumed that for the 1st approach as well)
 *  • finally I gave up and read the solution, didn't understand why it'd work => dry ran the algo on a single example
 *  from the problem => realized that we can actually reach nodes on multiple paths 🤦🤦🤦
 *
 * Value gained:
 *  • how do I minimize such crude misunderstanding of the problem statement in the future?
 *   I guess here what happened is that I solved too many problems on graphs which asked for a metric of a single best
 *   path, so I inherently assumed for this problem that we have to reach all nodes in a single traversal! When in reality
 *   it, kind of muddily, states that we might take as many paths as we want "We will send a signal from a given node k".
 *   In hindsight, the problem doesn't very clearly state this multi-path solution property.
 *
 *  • practiced recognizing and using a Dijkstra's algorithm for an efficient solution to the weighted graph problem;
 */
class NetworkDelayTime {

    /**
     * problem rephrase:
     *  - given:
     *   - a directed graph:
     *    - nodesAmount: Int, nodes are labeled from 1 to n
     *    - an adjacency array of weighted edges, edges\[i]={source,destination,weight}. Weight=the amount of time it takes for
     *     a signal to reach destination from source.
     *   - startNode: Int
     *  - goal: return the minimum path weight towards the node with the max path weight amongst others, but only if all nodes
     *   are reachable from [startNode]. otherwise return -1
     *   (i.o. return the path weight towards the node with the max path weight amongst all nodes with minimized path weights, or -1)
     *
     * algorithm design:
     * 1. min path weight => Dijkstra's?
     * we need to visit all nodes, BUT NOT IN A SINGLE PATH. however, for each node we want to know only the minimum path
     *  weight it takes to reach it => certainly try Dijkstra's.
     * 2. how to determine that some nodes weren't reached? iterate through `minWeights` at the end of traversal, return
     *  -1 if any node has its default path weight value.
     *
     * Time: average/worst O((nodes+edges)*log(nodes))
     *  - dijkstra's traversal, not always all nodes might be reachable from start node [startNode], but in the worst case
     *   all nodes are => the traversal takes O((nodes + edges)*log(nodes))), and on average it depends on it;
     *  - determining -1 and finding max takes O(n).
     * Space: always O(n)
     *  - heap in worst case at first has O(n) elements where each node is connected with each, then the number depends on it;
     *  - minPathWeights always takes O(n)
     */
    fun efficient(nodesAmount: Int, adjacencyArray: Array<IntArray>, startNode: Int): Int {
        val nodeToEdgesMap = mutableMapOf<Int, MutableList<Edge>>()
        adjacencyArray.forEach { edge ->
            val (start, end, weight) = edge
            if (!nodeToEdgesMap.contains(start)) nodeToEdgesMap[start] = mutableListOf()
            nodeToEdgesMap.getValue(start).add(Edge(destination = end, weight = weight))
        }

        val minHeap = PriorityQueue<NodeVisit> { o1, o2 -> o1.pathWeight - o2.pathWeight }.apply {
            add(NodeVisit(node = startNode, pathWeight = 0))
        }
        val minPathWeights = IntArray(size = nodesAmount + 1) { DEFAULT_PATH_WEIGHT }.apply {
            set(startNode, 0)
        }

        while (minHeap.isNotEmpty()) {
            val visit = minHeap.remove()
            if (visit.pathWeight > minPathWeights[visit.node]) continue

            val edges = nodeToEdgesMap[visit.node] ?: continue
            edges.forEach { edge ->
                val newPathWeight = visit.pathWeight + edge.weight
                if (newPathWeight >= minPathWeights[edge.destination]) return@forEach
                minPathWeights[edge.destination] = newPathWeight
                minHeap.add(NodeVisit(node = edge.destination, pathWeight = newPathWeight))
            }
        }

        var maxMinPathWeight = -1
        for (i in 1..nodesAmount) {
            val weight = minPathWeights[i]
            if (weight == DEFAULT_PATH_WEIGHT) return -1
            if (weight > maxMinPathWeight) maxMinPathWeight = weight
        }
        return maxMinPathWeight
    }

    /**
     * WRONG, misunderstood the problem statement. I thought we must reach all nodes exactly in 1 path through the graph,
     *  cause it's the usual rule in graph problems!
     *
     * -------
     *
     * problem rephrase:
     *  - given:
     *   - a directed graph:
     *    - nodesAmount: Int, nodes are labeled from 1 to n
     *    - an adjacency array of weighted edges, edges\[i]={source,destination,weight}. Weight=the amount of time it takes for
     *     a signal to reach destination from source.
     *   - startNode: Int
     *  - goal: return the minimum valid path weight or -1 if that's impossible.
     *      valid = path that visits all nodes starting from [startNode]
     *
     * what approach to use?
     *  - we certainly need graph traversal => how to visit all nodes efficiently while finding the minimum total path weight,
     *   but visiting all nodes?
     *  - weights => Dijkstra's? basically visit node X only if current path weight to it is greater than the path weight
     *   we've already taken to it previously. we still have the same edges from X node regardless of the prior context;
     *  - how to determine when not all nodes received the signal? idea - keep a track of how many nodes we've visited
     *   across the path to each node. When comparing the
     *
     *
     *   just either count the amount of unique nodes we encounter, or at the end simply iterate through `maxEfforts` array
     *   and return -1 if any node still has its default value.
     *
     * Edge cases:
     *  - nodesAmount == 1 =>
     *  - edges.size == 1 =>
     *
     * Time:
     * Space:
     */
    fun wrong(nodesAmount: Int, adjacencyArray: Array<IntArray>, startNode: Int): Int {
        val nodeToEdgesMap = mutableMapOf<Int, MutableList<Edge>>()
        adjacencyArray.forEach { edge ->
            val (start, end, weight) = edge
            if (!nodeToEdgesMap.contains(start)) nodeToEdgesMap[start] = mutableListOf()
            nodeToEdgesMap.getValue(start).add(Edge(destination = end, weight = weight))
        }

        val maxHeap = PriorityQueue<NodeVisit> { o1, o2 -> o2.pathWeight - o1.pathWeight }.apply {
            add(NodeVisit(node = startNode, pathWeight = 0))
        }
        val maxPathWeights = IntArray(size = nodesAmount + 1) { DEFAULT_PATH_WEIGHT }
        while (maxHeap.isNotEmpty()) {
            val visit = maxHeap.remove()
            if (visit.pathWeight < maxPathWeights[visit.node]) continue

            val edges = nodeToEdgesMap[visit.node] ?: continue
            edges.forEach { edge ->
                val newPathWeight = visit.pathWeight + edge.weight
                if (newPathWeight <= maxPathWeights[edge.destination]) return@forEach
                maxPathWeights[edge.destination] = newPathWeight
                maxHeap.add(NodeVisit(node = edge.destination, pathWeight = newPathWeight))
            }
        }


        return maxPathWeights[nodesAmount]
    }

    private class Edge(val destination: Int, val weight: Int)

    private class NodeVisit(val node: Int, val pathWeight: Int)
}

private const val DEFAULT_PATH_WEIGHT = Int.MAX_VALUE

fun main() {
    println(
        NetworkDelayTime().efficient(
            nodesAmount = 4,
            adjacencyArray = arrayOf(
                intArrayOf(2, 1, 1),
                intArrayOf(2, 3, 1),
                intArrayOf(3, 4, 1),
            ),
            startNode = 2,
        )
    )
}
