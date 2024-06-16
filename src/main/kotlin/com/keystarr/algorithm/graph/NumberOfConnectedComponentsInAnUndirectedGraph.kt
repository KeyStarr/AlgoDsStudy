package com.keystarr.algorithm.graph

/**
 * LC-323 https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= n <= 2000;
 *  - 1 <= edges.length <= 5000;
 *  - edges\[i].length == 2
 *  - 0 <= ai, bi < n and ai < bi;
 *  - all edges are distinct, each edge is undirected (that is, only 1 edge for 2 different nodes, e.g. 1 edge for 2,3 and 3,2).
 *
 * Value gained:
 *  - practiced DFS on an undirected graph given via an array of edges;
 *  - checked editorial - cool, actually we can convert an array of edges instead of a map to simply an adjacency list!!
 *   that should give better const time. That is, only in that kinda problems where nodes are marked from 0 to n.
 */
class NumberOfConnectedComponentsInAnUndirectedGraph {

    /**
     * Problem rephrase:
     *  - we are given an undirected graph in the form of "an array of edges".
     *  Goal: count the number of connected components in the graph
     *
     * Simple idea:
     *  - preprocess into map node->neighbors, add each edge twice - as-is and reversed cause the graph is undirected;
     *  - iterate through 0 until n, traverse from each node using recursive DFS + seen;
     *  - return the number of calls to dfs that added at least one node to seen.
     * Edge cases:
     *  - n == 1, edges == 1 => edge to the single node itself, return 1 => correct (1 dfs call, a single +1 to answer).
     * Time: O(n+e)
     * Space: O(n)
     */
    fun efficient(numberOfNodes: Int, edges: Array<IntArray>): Int {
        val nodeToNeighborsMap = toNodeToNeighborsMap(edges)
        val seen = BooleanArray(numberOfNodes)
        var componentsCount = 0
        repeat(numberOfNodes) { currentNode ->
            val isNewComponent = dfs(currentNode, nodeToNeighborsMap, seen)
            if (isNewComponent) componentsCount++
        }
        return componentsCount
    }

    private fun toNodeToNeighborsMap(edges: Array<IntArray>) = mutableMapOf<Int, MutableList<Int>>().apply {
        edges.forEach { edge ->
            val startNode = edge[0]
            val endNode = edge[1]
            // undirected graph, one record for an edge => add reversed edge too
            getOrPut(startNode) { mutableListOf() }.add(endNode) // refactor
            getOrPut(endNode) { mutableListOf() }.add(startNode)
        }
    }

    /**
     * Goals:
     *  - add all unseen nodes to seen;
     *  - after visiting all unseen nodes return true if added at least one node to seen.
     */
    private fun dfs(
        currentNode: Int,
        nodeToNeighborsMap: Map<Int, MutableList<Int>>,
        seen: BooleanArray,
    ): Boolean {
        if (seen[currentNode]) return false

        seen[currentNode] = true
        val neighbors = nodeToNeighborsMap[currentNode]
        neighbors?.forEach { neighbor -> dfs(neighbor, nodeToNeighborsMap, seen) }
        return true
    }

    // TODO: solve via a Disjoint Set Union
}
