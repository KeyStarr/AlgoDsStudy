package com.keystarr.algorithm.graph

/**
 * LC-547 https://leetcode.com/problems/number-of-provinces/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= number of nodes (n) <= 200;
 *  • n == isConnected.length;
 *  • n == isConnected.length\[i];
 *  • isConnected\[i]\[j] is either 0 or 1;
 *  • isConnected\[i]\[i] == 1 (i.o. the node itself is connected to itself);
 *  • isConnected\[i]\[j] == isConnected\[j]\[i] (i.o. each edge is undirected??).
 *
 * Final notes:
 *  • consciously solved my 1st graph problem ever!!!!! Wo-hoo!!!!
 *  • read all components of the idea from the course and implemented it by myself, re-checked.
 *
 * Value gained:
 *  • interesting, do graph problems typically require solving multiple subproblems? Like, here we have 4 distinct algorithms,
 *    and tools like HashMap, recursion, graph DFS, static&dynamic arrays working together for the solution;
 *  • TODO: is that solution even the most efficient?
 *  • wow, time/space complexity is pretty not obvious with all recursive calls across all graph's connected components
 *   basically always summing up to the number of edges in the graph;
 *  • practiced solving adjacent matrix type graphs via DFS.
 */
class NumberOfProvinces {

    /**
     * Problem rephrase:
     * - we have an undirected graph with possible multiple connected components;
     * - the graph is represented via the "adjacency matrix";
     *  (the description of edges [isConnected], where if an element isConnected\[i]\[j] == 1
     *  it means that there are edges both between node ith and jth and jth and ith)
     * - goal: "find the number of connected components in the graph".
     *
     * Tools:
     * - adjacency matrix => either pre-process or use as-is. (pre-process time O(n))
     * - undirected => might be cycles => use `seen` to avoid cycling. Less than 201 nodes => just use a BooleanArray;
     * - number of components => we need to visit each node => try recursive DFS. (time O(n))
     *
     * Edge cases:
     *  - a connected component with 1 node => ArrayList in map entry for it would empty, but we need to count it still as +1
     *      => as soon as we see a new node not present in the map we put it into the map with an empty list, so it's guaranteed
     *      to be present. And when iterating through map's entries we will need to handle that to be a +1;
     *  - all connected components are single nodes => works ok with that single case above handled;
     *  - when iterating through nodesToEdges map encounter a nodeInd which was already seen => its connected component
     *      is already counted => must not increment connectedComponentsCount =>
     *          - return true if dfs added any nodes to seen => the original currentNode was a part of an unseen before
     *              connected component? (most efficient)
     *          - compute seen contentHashCode before and after to see if it changed (bigger const);
     *  - since all edges are undirected, there are cycles => seen lets us avoid cycling;
     *  - since node with itself has a 1 in the matrix => don't add the node itself as an edge for itself when pre-processing.
     *
     * Total Time: always O(n^2 + e) = O(n^2)
     *  (technically it's O(n*m) but m=n in this problem. n = width of the matrix, m = height)
     *  (worst e=n^2, that is, each element in the [isConnected] is 1 => still O(n^2))
     *  (best e=0, that is, graph consists of as many components as there are nodes, there are no edges => still O(n^2))
     *
     * Total Space: average O(n + g + e) = O(n), worst O(n^2)
     *  (worst g=n, that is, the graph has a single component, and we build the [dfs] recursion stack up to n, traverse
     *  the graph via a single initial dfs call (including subsequent internal recursion, ofc))
     *  (best g=0, same case is best time => still O(n))
     *  (worst/best e is given above for time, worst e = n^2 gives worst space O(n^2) cause we store all these edges
     *   across arrays in the map)
     */
    fun solution(isConnected: Array<IntArray>): Int {
        val nodesToEdges = adjacentMatrixToMap(isConnected)
        return countConnectedComponents(nodesToEdges)
    }

    /**
     * Goal - pre-process the graph into a map.
     *
     * Time: O(n^2) cause we have to check each edge, total edges are n^2;
     * Space: O(n + e) for the map. Actually, exactly θ(n+e), cause we want each node, even if it's out-degree is 0, to be present.
     *  e - total number of edges in the graph. Because each node gives an entry to the map, and each edge is present
     *   exactly once in an array of some node's entry in the map.
     */
    private fun adjacentMatrixToMap(adjacentMatrix: Array<IntArray>): Map<Int, List<Int>> {
        val nodesToEdges = HashMap<Int, MutableList<Int>>(adjacentMatrix.size)
        adjacentMatrix.forEachIndexed { nodeInd, potentialNeighbors ->
            nodesToEdges[nodeInd] = ArrayList()
            potentialNeighbors.forEachIndexed { otherNodeInd, isNeighbor ->
                if (otherNodeInd != nodeInd && isNeighbor == 1) nodesToEdges.getValue(nodeInd).add(otherNodeInd)
            }
        }
        return nodesToEdges
    }

    /**
     * Goal - count connected components in the graph represented by [nodesToEdges].
     *
     * Time: average/worst is exactly θ(e+n), where:
     *  e = the total number of edges in the graph.
     *  n = total number of nodes in the graph
     *
     *  Reason: the loops + recursion check each edge and each node in the graph exactly ONCE:
     *   - we check each node via an iteration of the loop in here;
     *   - we check each edge of a given node in [dfs], for each node in here.
     *   => we check all edges across all connected components in the graph plus the amount of single node component, each - once.
     *
     * Space: O(201 + g) = O(g), where g is defined in [dfs].
     */
    private fun countConnectedComponents(nodesToEdges: Map<Int, List<Int>>): Int {
        val seen = BooleanArray(201)
        var connectedComponentsCount = 0
        nodesToEdges.entries.forEach { entry ->
            val isNewComponent = dfs(entry.key, nodesToEdges, seen)
            if (isNewComponent) connectedComponentsCount++
        }
        return connectedComponentsCount
    }

    /**
     * Goals:
     *  - return true if [currentNode] is a part of a previously unseen connected component;
     *  - if true, also add all nodes of the connected component that includes [currentNode] into [seen].
     *
     * Means: traverse all possible directions starting from [currentNode] that are not [seen], add each newly visited
     * into [seen] => traverse and mark as seen all nodes in the connected component with [currentNode].
     *
     * Tool - recursive DFS.
     * Base case:
     *  - the node is already seen => return;
     * Recursive case:
     *  - add current node to seen;
     *  - iterate through all destination nodes in nodesToEdges\[currentNode], visit each via dfs.
     *
     * Edge cases:
     *  - a connected component with 1 node => the node itself is guaranteed to not have been seen yet then, we add
     *      it into 'seen' always.
     *
     * Note: we can check if seen\[newNode] inside the loop and avoid calls to the [dfs] for this, which would be faster
     *  time const. But, imho, current way is cleaner for comprehension (standard D&C form).
     *
     * Time: always O(k), where k = number of edges in the graph's connected component with [currentNode] in it. Because
     *  we basically check each edge, even if we don't go through it (if the neighbor connected by it was already seen);
     * Space: average/worst O(g), where g = the longest path in the graph's connected component with [currentNode]
     *  that has any links to yet unseen nodes. Basically O(k), cause g depends on k.
     *      Best is O(1) when we have only 1 or 2 nodes in the connected component.
     */
    private fun dfs(
        currentNode: Int,
        nodesToEdges: Map<Int, List<Int>>,
        seen: BooleanArray
    ): Boolean {
        if (seen[currentNode]) return false

        seen[currentNode] = true
        nodesToEdges.getValue(currentNode).forEach { newNode -> dfs(newNode, nodesToEdges, seen) }
        return true
    }

    // TODO: understand and solve via https://leetcode.com/problems/number-of-provinces/solutions/101338/neat-dfs-java-solution/
    // TODO: solve via BFS
    // TODO: solve via iterative DFS
    // TODO: solve via Union Find
}

fun main() {
    println(
        NumberOfProvinces().solution(
            arrayOf(
                intArrayOf(1, 1, 0),
                intArrayOf(1, 1, 0),
                intArrayOf(0, 0, 1)
            )
        )
    )
}
