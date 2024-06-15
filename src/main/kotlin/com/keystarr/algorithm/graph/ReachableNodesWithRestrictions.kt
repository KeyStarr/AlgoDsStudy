package com.keystarr.algorithm.graph

/**
 * LC-2368 https://leetcode.com/problems/reachable-nodes-with-restrictions/description/
 * difficulty: medium
 * constraints:
 *  - 2 <= n <= 10^5;
 *  - edges.length == n-1;
 *  - edges\[i].length == 2, 0 <= ai bi < n, ai != bi
 *  - edges represent a valid tree
 *  - 1 <= restricted.length < n
 *  - 1 <= restricted\[i] < n, all such values are unique
 *
 * Final notes:
 *  - solved via [efficient] in about 30-40 mins by myself.
 *
 * Value gained:
 *  - practiced DFS on an undirected tree;
 *  - reinforced that if a graph is an undirected tree, for traversal we don't need to record all seen nodes, just
 *   keep track of the previous one each time, so we avoid going back.
 */
class ReachableNodesWithRestrictions {

    /**
     * Problem rephrase:
     *  - we are given an undirected tree represented via "an array of edges";
     *  - nodes are labeled from 0 to n-1;
     *  - we have n-1 edges;
     *  - 0 IS NOT restricted;
     * Goal: return the maximum number of nodes that we can visit without going through the restricted nodes,
     *  STARTING from node 0.
     *
     * tree = two nodes are connected by exactly 1 path:
     *  => the entire graph is its single connected component?
     *  => the graph is acyclic, we don't need `seen`, just eliminate going back to prev node, and we'll only go forward.
     *
     * maximum number of nodes => traversal => try recursive DFS
     * restricted nodes => to check for restricted use a pre-processed set
     *
     * Idea:
     *  - pre-process:
     *      - add all restricted nodes into the restrictedSet;
     *      - convert the array of edges into a node->neighbors map, processing each edge in a bidirectional manner;
     *          (adding twice. cause we travel from 0, but we don't know at this stage which order the edges are in,
     *           in terms of that what is the original edge direction, from 0 or to 0 in the tree, in the way it was
     *           specified (eg 6,5 in the 2nd example if we wouldn't add it's reversed version then once we'd reach
     *           node 5 traversing from 0, we wouldn't be able to traverse to 6 from there, cause only 6 would have
     *           a direct path to 5, and not 5 to 6!)
     *  - return dfs(currentNode=0, prevNode = -1 nodeToEdgesMap, restrictedSet)
     *
     * dfs(currentNode, prevNode, nodeToEdgesMap, restrictedSet): Int
     * goal: count the amount of nodes we can traverse to from \[currentNode], excluding the direction to \[prevNode]
     *  - base case:
     *      - if (restrictedSet.contains(currentNode)) return 0
     *  - recursive case:
     *      - nodesCount = 1
     *      - neighbors = nodeToEdgesMap[currentNode]
     *      - iterate through neighbors:
     *          - if neighbor == prevNode => continue
     *          - nodesCount += dfs(neighbor, currentNode, nodeToEdgesMap, restrictedSet)
     *      - return nodesCount
     *
     * Edge cases:
     *  - 2 nodes and 1 edge, 1 restricted (node 1) => correct, just count 0, output 1.
     *
     * Time: O(n+e), worst e=n-1, so average/worst O(n)
     *  - we visit each node and each edge at most once, skipping those that are restricted or are blocked by restricted nodes.
     * Space: O(n+e+r) = average/worst O(n)
     *  - callstack size is the amount of nodes we traverse one after another via a line, so worst is n;
     *  - map contains all edges, so e;
     *  - r = restricted nodes size, worst is n-1.
     */
    fun efficient(numberOfNodes: Int, edges: Array<IntArray>, restricted: IntArray): Int {
        val restrictedSet = restricted.toSet()
        val nodeToNeighborsMap = toNodeToNeighborsMap(edges)
        return dfs(currentNode = 0, prevNode = -1, nodeToNeighborsMap, restrictedSet)
    }

    private fun toNodeToNeighborsMap(edges: Array<IntArray>) = mutableMapOf<Int, MutableList<Int>>().apply {
        edges.forEach { edge ->
            val startNode = edge[0]
            val endNode = edge[1]
            getOrPut(startNode) { mutableListOf() }.add(endNode)
            getOrPut(endNode) { mutableListOf() }.add(startNode)
        }
    }

    private fun dfs(
        currentNode: Int,
        prevNode: Int,
        nodeToEdgesMap: Map<Int, List<Int>>,
        restrictedSet: Set<Int>
    ): Int {
        var nodesCount = 1
        val neighbors = nodeToEdgesMap[currentNode]
        neighbors?.forEach { neighbor ->
            if (restrictedSet.contains(neighbor) || neighbor == prevNode) return@forEach
            nodesCount += dfs(currentNode = neighbor, prevNode = currentNode, nodeToEdgesMap, restrictedSet)
        }
        return nodesCount
    }

    // TODO: solve via a Disjoint Set Union
}
