package com.keystarr.algorithm.graph.tree.backtracking

/**
 * ⭐LC-7797 https://leetcode.com/problems/all-paths-from-source-to-target/description/
 * difficulty: medium
 * constraints:
 *  • n == graph.length
 *  • 2 <= n <= 15
 *  • 0 <= graph\[i]\[j] < n
 *  • graph\[i]\[j] != i (no self edges)
 *  • all elements in graph\[i] are unique
 *  • the input graph is always a DAG.
 *
 * Final notes:
 *  • oh, DFS without seen! Here the problem asks for all paths from one node to another => every time there's an edge
 *   to some node, we must take it, no matter how many times we've visited that node previously. Coz every such time the
 *   path•context is different, and the path-context in this problem is which nodes we had visited so far, with the
 *   original visit order => it's always different. Never seen such a problem before;
 *  • moreover, the problem asks to return all paths and not just, say, a number of paths => we need to actually construct
 *   the path as we traverse nodes. And the efficient way to do this is to use only one instance of the current path
 *   and copy it to the result once the end is reached => i.o. backtracking;
 *  • as in [Combinations], [Permutations] and [Subsets] estimating the time here is hard. Actually here time estimation
 *   is even harder than designing the solution itself for me right now D::::
 *
 * Value gained:
 *  • cool, sometimes it's efficient to use DFS/BFS without seen, sometimes path•context is different for every node visit;
 *  • indeed, backtracking generalizes to graphs, it's a good tool not just for trees;
 *  • practiced recognizing and designing a backtracking solution on an abstract directed acyclic graph problem.
 */
class AllPathsFromSourceToTarget {

    /**
     * goal: return all possible paths from node 0 to node n-1 in a directed acyclic graph.
     * graph input type: array of edges
     *
     * all valid paths in a DIRECTED ACYCLIC graph (no loops!) => try DFS without seen.
     *
     * Time: O(2^n*n) TODO: why exactly? understand deeper / try to actually prove
     * Space: O(numberOfPaths) = O(2^n)
     */
    fun solution(graph: Array<IntArray>) = mutableListOf<List<Int>>().apply {
        dfs(
            currentNode = 0,
            currentPath = mutableListOf(0),
            endNode = graph.size - 1,
            edges = graph,
            resultPaths = this,
        )
    }

    /**
     * Return all the paths from node 0 to node n-1.
     */
    private fun dfs(
        currentNode: Int,
        currentPath: MutableList<Int>,
        endNode: Int,
        edges: Array<IntArray>,
        resultPaths: MutableList<List<Int>>,
    ) {
        if (currentNode == endNode) {
            resultPaths.add(ArrayList(currentPath))
            return
        }

        edges[currentNode].forEach { destinationNode ->
            currentPath.add(destinationNode)
            dfs(currentNode = destinationNode, currentPath, endNode, edges, resultPaths)
            currentPath.removeLast()
        }
    }
}

fun main() {
    println(
        AllPathsFromSourceToTarget().solution(
            graph = arrayOf(intArrayOf(1, 2), intArrayOf(3), intArrayOf(3), intArrayOf())
        )
    )
}
