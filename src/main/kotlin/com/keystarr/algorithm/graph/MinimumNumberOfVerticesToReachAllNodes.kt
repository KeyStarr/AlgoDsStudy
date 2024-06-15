package com.keystarr.algorithm.graph

/**
 * LC-1557 https://leetcode.com/problems/minimum-number-of-vertices-to-reach-all-nodes/
 * difficulty: medium
 * constraints:
 *  - 2 <= n <= 10^5;
 *  - 1 <= edges.length <= min(10^5, n * ((n-1)/2));
 *  - edges\[i].length == 2
 *  - 0 <= from ith, to ith < n;
 *  - all pairs from ith, to ith are distinct.
 *
 * Final notes:
 *  • done [wrong] by myself in about 30-40 minutes, failed. Checked the course and implemented [efficient] after
 *   understanding the premise explanation.
 *
 * Value gained:
 *  • uh-huh, so DFS + seen is not always the answer :))))
 *  • a very smart use of in-degree here - sometimes the key to an efficient solution it seems could be an effective use
 *   of a key property, solid understanding and a unique perspective relevant to the problem;
 *  • connected components in the graph
 */
class MinimumNumberOfVerticesToReachAllNodes {

    /**
     * Problem rephrase:
     *  - we are given a directed acyclic graph, represented via "an array of edges";
     * Goal: find all nodes which are not accessible from other nodes, i.o., have an in-degree of 0.
     *  (cause if a node is reachable via at least once other node, we would simply return that node, cause we aim
     *  for the minimum amount (here - take 1 instead of 2))
     *
     * Idea:
     *  - allocate nodesNonZeroInDegree = BooleanArray(size = numberOfNodes)
     *  - iterate through edges:
     *       - nodesInDegree[edge[1]] = true
     *  - iterate through the array again and add each node which has a value of false;
     *  - return those nodes.
     *
     * Edge cases:
     *  - at least 2 nodes and 1 edge between them => works ok;
     *  - multiple connected components => ok, we'll find in each all nodes with in-degree of 0;
     *  - multiple overlapping connected components (sharing common nodes) => ok, we'll find in each only a single node
     *   with in-degree of 0;
     *  - a single connected component => ok, since the graph is acyclic there must be a "root", in this case,
     *   a single node with in-degree of 1.
     *
     * Time: O(n)
     * Space: O(n)
     */
    fun efficient(numberOfNodes: Int, edges: List<List<Int>>): List<Int> {
        val nodesWithInDegree = BooleanArray(size = numberOfNodes)
        edges.forEach { edge -> nodesWithInDegree[edge[1]] = true }
        return ArrayList<Int>().apply {
            nodesWithInDegree.forEachIndexed { node, hasInDegree -> if (!hasInDegree) add(node) }
        }
    }

    /**
     * FAILED - incorrect.
     * I made an assumption that connected components do not overlap, meaning, that if we start at any node in the
     * component, than we will guaranteed traverse all nodes of only that component, and no nodes of that component
     * can be visited by starting from a node of another component.
     *
     * But it turns out that components can overlap, and therefore if we start in a subcomponent, which is a part
     * of some larger component, we'd erroneously add one of its nodes into the answer, while actually we need just the
     * outer component's (if it's not a subcomponent itself) node.
     *
     * TODO: do I understand the reason of failure here correctly? revisit
     *
     * ---------------------------
     *
     * Problem rephrase:
     *  - given a directed acyclic graph via "an array of edges";
     *  - solution is guaranteed to exist;
     *  - at least 1 edge, at least 2 nodes.
     * Goal: return any 1 node from each connected component of the graph.
     *
     * Idea #1:
     *  - pre-process edges array into a map node->edges;
     *  - seen = HashSet()
     *  - minimumVerticesList = ArrayList()
     *  - iterate through 0 until numberOfNodes:
     *      - if seen.contains(entry.key) continue
     *      - dfs(entry.key, map, seen)
     *      - minimumVerticesList.add(entry.key)
     *      - if (seen.size == n) break
     *  - return minimumVerticesList
     *
     *
     * Trick - there is a case when, if we start from X node, we can't reach node Z. But if we start from Y node,
     *  we can reach both X and Z.
     *
     * Time: always O(n+e), average O(n), worst O(n^2)
     *  - we visit each node exactly once;
     *  - we use each edge exactly once;
     *  - worst e = n*(n-1)/2 = ~n^2
     *
     * Space: O(n)
     *  - seen at most size n;
     *  - dfs callstack at most size n (if the entire graph is in a form of a line and has a single connected component);
     *  - minimumVerticesLise worst size n, if each node has only an edge to itself (judging by constraints - that's possible
     *      cause there is no from!=to rule).
     *
     */
    fun wrong(numberOfNodes: Int, edges: List<List<Int>>): List<Int> {
        val seen = mutableSetOf<Int>()
        val nodeToEdgesMap = toNodeToEdgesMap(edges)
        val minimumVerticesList = mutableListOf<Int>()
        for (currentNode in 0 until numberOfNodes) {
            if (seen.contains(currentNode)) continue
            dfs(currentNode, nodeToEdgesMap, seen)
            minimumVerticesList.add(currentNode)
            if (seen.size == numberOfNodes) break
        }
        return minimumVerticesList
    }

    private fun toNodeToEdgesMap(edges: List<List<Int>>) = mutableMapOf<Int, MutableList<Int>>().apply {
        edges.forEach { edge ->
            val startNode = edge[0]
            val endNode = edge[1]
            if (!contains(startNode)) put(startNode, mutableListOf())
            getValue(startNode).add(endNode)
        }
    }

    private fun dfs(currentNode: Int, nodeToEdgesMap: Map<Int, MutableList<Int>>, seen: MutableSet<Int>) {
        val edges = nodeToEdgesMap[currentNode] ?: return
        edges.forEach { neighbor ->
            if (seen.contains(neighbor)) return@forEach // technically - a base case
            seen.add(neighbor)
            dfs(neighbor, nodeToEdgesMap, seen)
        }
    }

}
