package com.keystarr.algorithm.graph

import kotlin.math.max

/**
 * ⭐️ wow, a graph properties question with NO O(n) or O(nlogk) solution! only a plain brute O(n^2)
 * LC-1615 https://leetcode.com/problems/maximal-network-rank/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= number of nodes <= 100;
 *  • 0 <= edges.size <= n * (n-1)/2
 *  • roads\[i].size == 2, 0 <= roads\[i][0] and [1] <= n-1, no self-edges;
 *  • all edges\[i] are unique sets (each pair of cities has only at most one edge in \[edges]).
 *
 * Final notes:
 *  • designed brute in 10 mins;
 *  • ⚠️⚠️⚠️ done [bruteForce] at 1h 17 mins mark!
 *  • mistakes:
 *   • ⚠️1st understood the problem wrong! interpreted it such that the 2 nodes MUST BE CONNECTED TO EACH OTHER,
 *   which is not the case, we need ANY two nodes [wrongBrute];
 *   • thought then its a top k=2 maxes by edges count problem, but it turns out there are a lot of edge cases because of
 *    the "don't count the road between the nodes if they are connected" requirement. So that approach doesn't work;
 *   • finally tried another heuristic [wrongEfficient] but that failed due to these same rule spawned edge cases.
 *  => wow, a graph metric problem with only a brute n^2 solution, that can happen, and with such a deceivingly simple problem statement...
 *
 * Value gained:
 *  • 🤷 what did we learn, Palmer? Honestly, I'm not sure how I could've done better here. These edges cases cause of that
 *   rule were so deep under the water... how to do better in the future in such situations?
 *  • practiced solving a graph metric calculation problem without full traversal using graph properties.
 */
class MaximalNetworkRank {

    // TODO: retry in 1-2 weeks

    /**
     * Simply transform into node->edges map and try all pairs.
     *
     * Time: always O(n^2 + m), where m=edges.size
     *  - process into map node->neighbors, m iterations with O(1) work each;
     *  - main pairs try out loop, outer loop always has n iterations, inner loop averages at n/2 iterations => always O(n^2) time.
     * Space: always O(n + m)
     *  - map always has n entries and across all lists m*2 values, so takes O(n+m) space.
     */
    fun bruteForce(nodesAmount: Int, edges: Array<IntArray>): Int {
        if (edges.isEmpty()) return 0

        val nodeToNeighbors = Array<MutableList<Int>>(size = 101) { mutableListOf() }
        edges.forEach { edge ->
            val (source, destination) = edge
            nodeToNeighbors[source].add(destination)
            nodeToNeighbors[destination].add(source)
        }

        var maxRank = -1
        nodeToNeighbors.forEachIndexed { node, neighbors ->
            for (otherNode in node + 1 until nodesAmount) {
                val rank = neighbors.size + nodeToNeighbors[otherNode].size + if (neighbors.contains(otherNode)) -1 else 0
                maxRank = max(rank, maxRank)
            }
        }
        return maxRank
    }

    /**
     * problem rephrase:
     *  given:
     *   - array of edges representing an undirected graph. Nodes are labeled from 0 to n-1;
     *   - nodesAmount: Int
     *  goal: find the best valid pair of cities, and return its metric
     *   valid=there is an edge connecting these 2 cities
     *   best=amongst other pairs, these 2 cities have the max sum of all edges connected to them.
     *
     * can we have self-edges?
     * can we have duplicate edges in the array of edges? that is, is edge\[i] a unique set of numbers in edges?
     *
     * probably either way:
     *  transform array of edges into a map node->neighbors => we can traverse efficiently, and can in O(1) time
     *   get the amount of edges for the ith city;
     *
     * brute force:
     *  - iterate through the map, for each node find the neighbor node with the max amount of edges =>
     *   update maxRank with the total amount of edges in these two - 1 (to not count an edge between them twice);
     * Time of just that: always O(n^2)
     *  - n iterations always;
     *  - worst each node has an edge to each => each iteration we'd be doing, even if we start at j=i+1 O(n) work)
     *
     *
     * edge cases:
     *  - edges == 0 => answer is always 0 => correct as-is, though we could early return for O(1) time.
     *
     * ----------------------
     *
     * how to improve time to O(n) or at least O(nlogn)?
     *
     * can traversal be useful?
     * suppose we start at 0th city
     *
     * try DP?
     * topdown, goal - find a pair of directly connected nodes with maximum total sum of unique edges of these two, return the sum
     * input state - current node label, suppose initially 0
     * recurrence relation:
     *  - dp(node) = edges\[node].size + max( forEach node in nodes[node+1:nodes.size)) - 1
     * base case:
     *  node has no edges, or all neighbor nodes were already seen => return node.edges.size
     * memoization: IntArray(size=nodesAmount) ind=node, value=maxRank
     *
     * 0 - 1 - 2
     *  \ /
     *   3
     *
     * seen = 0, 1, 3
     *
     * dp(0) = 2 + max(dp(1), dp(3)) - 1 = 2 + max(4,
     * dp(1) = 3 + max(dp(3), dp(2)) - 1 = 3 + max(2,1) - 1 = 4
     * dp(3) = 2 base case
     * dp(2) = 1 base case
     *
     * NOPE, its not DP
     *
     * ---------------------------
     *              0 - 1 - 2 - 3   7-5-6
     *                      \
     *                       4
     *
     */
    fun wrongBrute(nodesAmount: Int, edges: Array<IntArray>): Int {
        if (edges.isEmpty()) return 0

        val nodeToNeighbors = Array<MutableList<Int>>(size = 101) { mutableListOf() }
        edges.forEach { edge ->
            val (source, destination) = edge
            nodeToNeighbors[source].add(destination)
            nodeToNeighbors[destination].add(source)
        }

        var maxRank = -1
        nodeToNeighbors.forEachIndexed { node, neighbors ->
            for (otherNode in node + 1 until neighbors.size) {
                maxRank = max(neighbors.size + nodeToNeighbors[otherNode].size - 1, maxRank)
            }
        }
        return maxRank
    }

    /**
     * WOW, I understood the question wrong!
     *
     * its actually
     *
     * goal: find ANY best pair of cities, and return its metric
     *  best=amongst other pairs, these 2 cities have the max sum of all edges connected to them.
     * => simply find 2 cities with max edges
     * and if both are connected, return c1.edges.size + c2.edges.size - 1
     *
     * => approach:
     *  1. count edges using IntArray node->edges count;
     *  2. use minHeap to find top k (k=2) max edges nodes.
     *
     * Edge cases:
     *  - edges.size == 0 => correct as-is, but may return 0 for O(1) time;
     *
     *
     *  - there is 1 node and multiple nodes with either same max edges count, or next max edges count => choose such 2 nodes
     *   that they are both not connected, if there are no such, choose either of these.
     *
     *   can we account for that before finding 2 max edges nodes?
     *   if we preserve the info about exact neighbors.
     *   =>
     *   find the node with max edges count AND find the next max edges count city that is not connected to it +
     *    find max edges count city that is connected to it => choose the max of these two?
     *
     *    edge case: if max edges node has X edges and all max2 edges Y nodes are connected to it AND some 2 max2 nodes
     *    are not connected to each other and X-Y=1, then pair X+Y is X+Y-1 edges and Y+Y is (X-1)+Y edges = choose either one.
     *    its ok
     * ---------------------
     *
     * WRONG, since top1 and top2 nodes by edges might be connected => their rank must then be subtracted -1
     * AND there might be other, e.g. equal node to top1 by edges size yet not connected to top2. or equal to top2 node
     * not connected to top1.
     * =>
     * approach #3:
     *  - find max edges node;
     *  - find max edges node other than it that is connected to it, and another max edges that is not connected to it;
     *  - return max edges node edgesSize + (either not connected or (connected - 1) whichever is max)
     *
     * => WRONG
     * 0 - 2
     *  \
     *   1
     * 0: 2,1
     * 1: 0
     * 2: 0
     *
     * most edges = 0
     * most edges other connected = 1
     * most edges other not connected = null
     * return 0.edges + 1.edges - 1 = 2 + 1 - 1 = 2
     * correct
     *
     *
     * 2 - 3 - 0 - 4 - 1
     *
     * 0:2, 1:1, 2:1, 3:2, 4:4
     *
     * most edges = 0 (cause out of all 2 edges nodes 0 happens to be the first one to be found in the max search loop)
     * most edges other connected = 3
     * most edges other not connected = 2
     * actual = 0.edges + max(3.size - 1, 2.size) = 2 + max(1,1)=3
     * expected = 3.edges + 4.edges = 2 + 2 = 4 (3 and 4 nodes are not connected!)
     *
     * so if we have multiple max edges nodes, and it so happens to be that some max of these are not connected, these are the answer
     *  but we might find out of those the one that is connected to another max wrongly
     * => we could remedy that by checking, if we have multiple max edge nodes, if there are at least 2 that are not connected,
     *  then pick them as the answer, but worst case that would take O(n^2) if all nodes values are equal and each is connected
     *  to each except the last which is not connected to just one node => we'd have to check each node with each for connections
     * => generally no point, same worst as [bruteForce] and too complex
     */
    fun wrongEfficient(nodesAmount: Int, edges: Array<IntArray>): Int {
        if (edges.isEmpty()) return 0

        val nodeToNeighbors = Array<MutableList<Int>>(size = 101) { mutableListOf() }
        edges.forEach { edge ->
            val (source, destination) = edge
            nodeToNeighbors[source].add(destination)
            nodeToNeighbors[destination].add(source)
        }

        var maxEdgesNode = 0
        nodeToNeighbors.forEachIndexed { node, neighbors ->
            if (neighbors.size > nodeToNeighbors[maxEdgesNode].size) maxEdgesNode = node
        }

        var maxNotConnectedEdgesNode: Int? = null
        nodeToNeighbors.forEachIndexed { node, neighbors ->
            if (node != maxEdgesNode
                && !nodeToNeighbors[maxEdgesNode].contains(node)
                && neighbors.size > (maxNotConnectedEdgesNode?.let { nodeToNeighbors[it].size } ?: 0)
            ) {
                maxNotConnectedEdgesNode = node
            }
        }

        var maxConnectedEdgesNode = nodeToNeighbors[maxEdgesNode].first()
        nodeToNeighbors[maxEdgesNode].forEach { node ->
            if (nodeToNeighbors[node].size > nodeToNeighbors[maxConnectedEdgesNode].size) maxConnectedEdgesNode = node
        }

        val maxNotConnectedEdges = maxNotConnectedEdgesNode?.let { nodeToNeighbors[it].size } ?: 0
        val maxConnectedEdges = nodeToNeighbors[maxConnectedEdgesNode].size
        return nodeToNeighbors[maxEdgesNode].size + max(maxConnectedEdges - 1, maxNotConnectedEdges)
    }
}

fun main() {
    println(
        MaximalNetworkRank().wrongEfficient(
            nodesAmount = 5,
            edges = arrayOf(
                intArrayOf(2, 3),
                intArrayOf(0, 3),
                intArrayOf(0, 4),
                intArrayOf(4, 1),
            ),
        )
    )
}
