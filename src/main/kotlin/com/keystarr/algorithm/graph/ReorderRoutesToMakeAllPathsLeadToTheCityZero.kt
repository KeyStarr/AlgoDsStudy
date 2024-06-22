package com.keystarr.algorithm.graph

/**
 * LC-1466 https://leetcode.com/problems/reorder-routes-to-make-all-paths-lead-to-the-city-zero/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= 5 * 10^4;
 *  • edges.length == n-1;
 *  • edges\[i].length == 2;
 *  • 0 <= a_i, b_i <= n-1;
 *  • a_i != b_i.
 *
 * Final notes:
 *  • was exhausted, almost arrived at the correct solution by myself in 1h, had all the major components right, BUT
 *   though that 0th node is always either a tail or a head, missed the major case that it could be in the middle of nodes;
 *  • then for some reason decided that one node, say, 0, can be connected to multiple nodes, like, 0->2, 0->3, 0<-4.
 *   Problem states "one way to travel between two different cities", so that makes sense doesn't it?? Turns out it can't
 *   happen, and I don't really understand why;
 *  • anyhoo, solved the problem the next day finally. At some points I've even lost my wits and started questioning whether
 *   trees (DS) may have 1 root or multiple roots... it turns out they can! How did I never stumble upon that before??
 *   Turns out by default in SE contexts "a ROOTED tree" is implied, but a tree generally, in graph theory, may have multiple roots;
 *
 * Value gained:
 *  • IT'S SUPER important TO REALLY understand all the major cases of input? Like what the restrictions are =>
 *   HOW MANY DIFFERENT EQUIVALENCE cases of forms of the input are there? Could've caught the "0 in the middle" case
 *   straight away and save myself a lot of time;
 *  • cool, I came up with some pretty unique moves myself here, like, turn the graph from directed into undirected,
 *   on the spot. Can I do it, if required, during an interview?
 *  • practiced solving a graph problem via DFS and the "array of edges" as input.
 */
class ReorderRoutesToMakeAllPathsLeadToTheCityZero {

    /**
     * Problem rephrase:
     *  - we have a directed graph;
     *  - graph input representation is "adjacency list" = an array of edges;
     *  - each node is identified with a number from 0 to n-1;
     *  - there's only 1 edge between 2 different nodes.
     *  Goal: return the minimum number of edges direction flips after which there is a path from each node to the
     *   node with id 0.
     *
     * The answer is guaranteed to exist (we got at least 2 nodes).
     *
     * Why the MINIMUM number of nodes? Are there multiple ways to flip nodes to achieve the main goal??
     * I don't think so, since each 2 adjacent nodes have only 1 edge between them => the minimum is just flip
     * each wrong-direction-facing edge once.
     *
     * Adjacency list => pre-process into a hashmap nodeId->neighbors? I feel like there is a simpler solution here,
     * let's explore other ideas first.
     *
     * The trick - adjacent nodes might be in any order.
     *
     * Idea:
     *  - pre-process [numberOfNodes] into a hashmap nodeId->Pair<neighborId, isOriginal>:
     *      - insert each edge twice, as-is (with isOriginal=true) and reversing its direction (isOriginal=false)
     *  - numberOfFlips = 0
     *  - find the starting point: map[0];
     *  - if !map[0].second => numberOfFlips++
     *  - use a recursive dfs, pass 0 as the node's id:
     *      goal: count the sum of flips required to get a path from every node to 0th node by reversing only existing edges.
     *      base case:
     *       - if (currentNode != 0 && map[currentNode].size == 1) return 0 // tail
     *      recursive case:
     *       - val edges = map[currentNodeId]
     *       - val nextEdge = if (edges[0].first != prevNode) edges[0] else edges[1]
     *       - val sumOfFlips = dfs(map, nextEdge.first, currentNode)
     *       - return if (!nextEdge.isOriginal) sumOfFlips + 1 else sumOfFlips
     *
     * Edge cases:
     *  - no notable ones, just maybe implementation in general.
     *
     * Time: O(n+n) = O(n)
     *  - pre-processing O(e) = O(n), where e=number of edges, in this problem e=n-1, where n=number of nodes;
     *  - dfs, we visit each node exactly once => O(n).
     * Space: O(n+n) = O(n)
     *  - hashmap where there's an entry for each node and each node has a value paired to it as 2 variables => O(n)
     *  - dfs callstack, we visit each node once => O(n)
     */
    fun solution(numberOfNodes: Int, edges: Array<IntArray>): Int {
        val nodeToEdgesMap = undirectedNodeToNeighborMap(edges)
        return dfs(nodesToEdges = nodeToEdgesMap, currentNode = 0, prevNode = -1)
    }

    private fun undirectedNodeToNeighborMap(edges: Array<IntArray>): Map<Int, MutableList<Edge>> {
        fun MutableMap<Int, MutableList<Edge>>.insertEdge(node: Int, neighbor: Int, isOriginal: Boolean) {
            if (!contains(node)) put(node, ArrayList(2))
            getValue(node).add(Edge(neighbor, isOriginal))
        }

        val map = mutableMapOf<Int, MutableList<Edge>>()
        edges.forEach { edge ->
            val firstNode = edge[0]
            val secondNode = edge[1]
            map.insertEdge(firstNode, secondNode, true)
            map.insertEdge(secondNode, firstNode, false)
        }

        return map
    }

    private fun dfs(nodesToEdges: Map<Int, List<Edge>>, currentNode: Int, prevNode: Int): Int {
        val edges = nodesToEdges.getValue(currentNode)
        var sumOfFlips = 0
        for (nextEdge in edges) {
            if (nextEdge.neighbor == prevNode) continue
            sumOfFlips += dfs(nodesToEdges, nextEdge.neighbor, currentNode)
            sumOfFlips += if (nextEdge.isOriginal) 1 else 0
        }
        return sumOfFlips
    }
}

class Edge(
    val neighbor: Int,
    val isOriginal: Boolean,
)

fun main() {
    println(
        ReorderRoutesToMakeAllPathsLeadToTheCityZero().solution(
            numberOfNodes = 6,
            edges = arrayOf(
                intArrayOf(0, 1),
                intArrayOf(4, 0),
                intArrayOf(1, 3),
                intArrayOf(2, 3),
                intArrayOf(4, 5),
            )
        )
    )
}
