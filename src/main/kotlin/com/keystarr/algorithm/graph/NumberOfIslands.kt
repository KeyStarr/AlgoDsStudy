package com.keystarr.algorithm.graph

/**
 * LC-200 https://leetcode.com/problems/number-of-islands/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= grid.length, grid\[i].length <= 300;
 *  • grid\[i]\[j] is either 0 or 1.
 *
 * Final notes:
 *  • implemented by myself without looking at the course's solution;
 *  • failed 1st submission due to a stupid indexing error (be more attentive? not all matrix are squared! I meant to
 *      use the right boundary for width though, but still mistyped I guess, overlooked?
 *
 * Value gained:
 *  • cool, despite a different input data representation, this problems and [NumberOfProvinces] have the same goal:
 *   "return the number of connected components in an undirected graph"!. The power of the right onthology, "dressing
 *   the problem in the right words", heh?)
 *   [NumberOfProvinces] is an "adjacency matrix" graph representation type
 *   this one is just called a "matrix" afaik
 *  • practiced DFS on the undirected graph with "matrix" input representation type;
 *  • a minor lesson - sometimes matrix can be a square, sometimes height!=width, verify each time, bro;
 *  • TODO: is [suboptimal] actually an efficient solution? how does it compare with the Union Find one?
 */
class NumberOfIslands {

    /**
     * Problem rephrase:
     *  - we have an undirected graph represented by a matrix, where each element is a node;
     *  - each node's edges are defined as it's top, bottom left and right neighbour elements in the matrix;
     *   (i.o. edges are defined by problem statement, not given directly in the input, input is all about nodes)
     *  - if left/right/top/bottom of a node X is 1, than there is an undirected edge between these two nodes. If it's a
     *   0, it means there is no node there => no outgoing edge in that direction for X;
     *  - max in-degree and out-degree of any node is 4, min are 0 (both);
     *  Goal: "count and return the number of connected components in an undirected graph"
     *
     * Graph as a matrix (of nodes):
     *  - may convert into a hashmap and apply recursive DFS;
     *  - may use as-is.
     *
     * Idea - traverse the graph via recursive DFS, use the matrix as-is:
     *  - numberOfComponents = 0
     *  - seen = HashSet()
     *  - iterate through rows elements in the matrix:
     *      - iterate through columns in the row:
     *          - val isNewComponent = dfs(grid, seen, i, j)
     *          - if (isNewComponent) numberOfComponents++
     *  - return numberOfComponents
     *
     * Edge cases:
     *  - rows.length == 1, columns.length == 1 i.o. a single element in the matrix (0,0);
     *
     * Time: average/worst O(m*n + 4 * (m*n) = O(5*m*n) = O(m*n)
     *  where n = width of the matrix, m = height of the matrix
     *  - iterate through each element O(n^2);
     *  - the number across all calls to [dfs] for all elements sums up to roughly O(4 * n^2), cause for each element,
     *   except elements on the edges, we make exactly 4 new calls to dfs.
     *
     * Space: average/worst O(m*n + g) = O(m*n) with g being at most n.
     *  - we use `seen` set which contains exactly the amount of 1's elements, so O(m*n) space;
     *  - recursion callstack is O(g), g defined in [dfs]
     *
     * -----
     *
     * potential for improvement:
     *  - check for seen some other way which doesn't involve creating a String object;
     *      - `seen` could be implemented instead via a 2D array of 300x300.
     *      - could avoid using set altogether by setting a visited node to grid[i][j] = 0
     *       (but input modification is a bad practice generally)
     */
    fun suboptimal(grid: Array<CharArray>): Int {
        var numberOfComponents = 0
        val seen = HashSet<String>()
        for (i in grid.indices) {
            for (j in grid[0].indices) {
                val isNewComponent = dfs(grid, seen, i, j)
                if (isNewComponent) numberOfComponents++
            }
        }
        return numberOfComponents
    }

    /**
     * dfs(grid, seen, i, j) returns Boolean
     *
     * Goals:
     *  - return true if the element of the graph at grid\[i]\[j] is a part of yet unseen component of the graph;
     *  - add all newly visited nodes into [seen].
     *
     * Base case:
     *   - if (grid\[i]\[j] == 0) return false
     *   - if seen.contains("$i$j") => return false
     * Recursive case:
     *   - seen.add("$i$j")
     *   - if (i > 0) dfs(grid, seen, i - 1, j) // top
     *   - if (i < grid.size - 1) dfs(grid, seen, i + 1, j) // bottom
     *   - if (j > 0) dfs(grid, seen, i, j - 1) // left
     *   - if (j < grid[0].size - 1) dfs(grid, seen, i, j + 1)  // right
     *   - return true
     *
     * Edge cases:
     *  - top/bottom/left/right neighbor element to current doesn't exist in the matrix (out of boundary)
     *      => handle via i and j boundary checks.
     *
     * Time: O(k) = O(m*n), where k - number of edges in the component which has "$i$j";
     *  Cause we check each edge of the graph's connected component which has the node identified as "$i$j", a single call
     *  for 1 edge is always O(1).
     *  Worst k = 4*m*n (a matrix full of 1's, i.o. each element is a node, each node has ~4 links = 4n), average k depends on n*m.
     *
     * Space: O(g) = O(m*n), where g - the longest path through the component without any seen nodes cause g depends on n*m.
     */
    private fun dfs(grid: Array<CharArray>, seen: MutableSet<String>, i: Int, j: Int): Boolean {
        val nodeId = "$i,$j"
        if (grid[i][j] == '0' || seen.contains(nodeId)) return false

        seen.add(nodeId)
        if (i != 0) dfs(grid, seen, i - 1, j) // top
        if (i != grid.size - 1) dfs(grid, seen, i + 1, j) // bottom
        if (j != 0) dfs(grid, seen, i, j - 1) // left
        if (j != grid[0].size - 1) dfs(grid, seen, i, j + 1) // right
        return true
    }

    // TODO: implement a cleaner [suboptimal] with seen as a 2D array and directions as a config-array (like in the course)
    // TODO: implement an efficient solution
    // TODO: implement a Union Find solution
}

fun main() {
    println(
        NumberOfIslands().suboptimal(
            arrayOf(
                charArrayOf('1'),
                charArrayOf('1')
            )
        )
    )
}
