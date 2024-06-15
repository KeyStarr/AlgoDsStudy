package com.keystarr.algorithm.graph

/**
 * LC-1971 https://leetcode.com/problems/find-if-path-exists-in-graph/editorial/
 * difficulty: easy (weird, cause smth like [KeysAndRooms] is very similar yet that one is medium)
 * constraints:
 *  • 1 <= n <= 2 * 10^5;
 *  • 0 <= edges.length <= 2 * 10^5;
 *  • edges\[i].length == 2
 *  • 0 <= to ith, from ith <= n-1;
 *  • 0 <= source, destination <= n - 1
 *  • each edge is unique, no self edges.
 *
 * Final notes:
 *  • implemented [efficient] by myself in 25 mins.
 *
 * Value gained:
 *  • practiced DFS on an undirected cyclic graph.
 */
class FindIfPathExistsInGraph {

    /**
     * Problem rephrase:
     *  - we are given an undirected (potentially) cyclic graph in a form of the "array of edges";
     *  - we have nodes from 0 to n-1;
     *  - there might be disconnected components (that sounds so off);
     * Goal: return true if there's a path from node source to destination.
     *  i.o. since each edge bidirectional, if nodes source and destination are within a single connected component => return true.
     *
     * Idea:
     *  - pre-process array of edges into a map node->edges, taking into account that each edge is bidirectional and present
     *   only once for a unique pair of nodes (meaning, 1-2 should be used for both 1->2 and 1<-2);
     *  - use a set seen;
     *  - return dfs(currentNode=source, map, seen, target)
     *
     * dfs
     *  - base case:
     *      - if currentNode == target return true
     *      - if seen.contains(currentNode) return false
     *  - recursive case:
     *      - edges = map[currentNode]
     *      - iterate through edges:
     *          - dfs(edge[1], map, seen, target)
     *
     * Edge cases:
     *  - n == 1 => then its a source and destination, expected is true => correct;
     *
     * Time: average/worst O(n + e) = O(n) cause worst e=n
     *  we visit each node and use each edge at most once
     * Space: average/worst O(n + e), worst is exactly n, where the entire graph is a line and source and target are
     *  on the opposite ends of it.
     *  (e for the hashmap, n for callstack, and also there would technically be another n for seen)
     */
    fun efficient(numberOfNodes: Int, edges: Array<IntArray>, source: Int, target: Int): Boolean {
        val seen = mutableSetOf<Int>()
        val nodeToEdgesMap = toNodeToEdgeMap(edges)
        return dfs(currentNode = source, nodeToEdgesMap = nodeToEdgesMap, seen = seen, target = target)
    }

    private fun toNodeToEdgeMap(edges: Array<IntArray>) = mutableMapOf<Int, MutableList<Int>>().apply {
        edges.forEach { edge ->
            val startNode = edge[0]
            val endNode = edge[1]
            addEdge(startNode, endNode)
            addEdge(endNode, startNode)
        }
    }

    private fun MutableMap<Int,MutableList<Int>>.addEdge(node: Int, neighbor: Int){
        if (!contains(node)) put(node, mutableListOf())
        getValue(node).add(neighbor)
    }

    private fun dfs(
        currentNode: Int,
        nodeToEdgesMap: Map<Int, List<Int>>,
        seen: MutableSet<Int>,
        target: Int,
    ): Boolean {
        if (currentNode == target) return true

        seen.add(currentNode)
        val edges = nodeToEdgesMap[currentNode] ?: return false
        edges.forEach { neighbor ->
            if (seen.contains(neighbor)) return@forEach

            val reachedTarget = dfs(currentNode = neighbor, nodeToEdgesMap, seen, target)
            if (reachedTarget) return true
        }
        return false
    }
}
