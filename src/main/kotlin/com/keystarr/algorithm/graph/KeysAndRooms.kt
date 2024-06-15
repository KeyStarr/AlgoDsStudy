package com.keystarr.algorithm.graph

/**
 * LC-841 https://leetcode.com/problems/keys-and-rooms/description/
 * difficulty: medium
 * constraints:
 *  - n == rooms.length
 *  - 2 <= n <= 1000
 *  - 0 <= rooms\[i].length <= 1000
 *  - 1 <= sum(rooms\[i].length) <= 3000
 *  - 0 <= rooms\[i]\[j] < n
 *  - all values in rooms\[i] are unique
 *
 * Final notes:
 *  - done [efficient] by myself in 30 mins;
 *
 * Value gained:
 *  - practiced solving graph problems with DFS given "an adjacency list";
 *  - oh, the power of putting the problem in the right onthology, in the right terms! All that story boils down to
 *   simply checking whether the graph which can potentially directed/undirected edges and cycles consists of a single
 *   connected component or not.
 */
class KeysAndRooms {

    /**
     * Problem rephrase:
     *  - room = node, keys = edges to other nodes;
     *  - we have a graph with directed and potentially undirected edges, potentially cyclic;
     *  - input is in the form of adjacency list (for each ith node we are given all edges from it to other nodes);
     *  - we start from node 0 (the only unlocked room at the beginning).
     * Goal: "return true if we can visit all nodes in the graph starting from the node 0".
     * i.o. "return true if connected component of the graph that includes 0 contains all nodes in the graph =
     *  return true if the graph has only 1 connected component"
     *
     * Idea - recursive DFS + seen HashSet(size=1000)
     *  - base case:
     *      - if seen.contains(node) == true => return
     *  - recursive case:
     *      - seen.add(node)
     *      - iterate through edges[node], nextNode:
     *          - dfs(nextNode)
     * ---------------
     * - seen = HashSet() // optionally use a BooleanArray and then count the actual seen nodes via counting elements with 1
     * - dfs(node=0, seen)
     * - return seen.size == rooms.size
     *
     * Edge cases:
     *  - can room contain a key to itself? => its ok, it will be in seen then, so we'd skip it;
     *  - why restrict total number of edges to 3000? => see no problem in that;
     *  - 0 edges for some node => ok, we'll count it as seen and backtrack;
     *  - min nodes amount is 2.
     *
     * Time: average/worst O(n+e)=O(n), where n = number of nodes, e = number of edges
     *  - cause we visit each node at most once and try each edge at most once
     *  - if node/edge is inaccessible from 0 => we don't visit it. But still the amount of nodes and edges we visit
     *   depends on n and e;
     *  - worst e=3n => O(4n) => O(n).
     * Space: average/worst O(n) for the callstack (seen contributes +n, so we let it go)
     */
    fun efficient(allNodeEdges: List<List<Int>>): Boolean {
        val seen = mutableSetOf<Int>()
        dfs(0, allNodeEdges, seen)
        return seen.size == allNodeEdges.size
    }

    private fun dfs(currentNode: Int, allNodeEdges: List<List<Int>>, seen: MutableSet<Int>) {
        if (seen.contains(currentNode)) return

        seen.add(currentNode)
        allNodeEdges[currentNode].forEach { neighbor -> dfs(neighbor, allNodeEdges, seen) }
    }
}
