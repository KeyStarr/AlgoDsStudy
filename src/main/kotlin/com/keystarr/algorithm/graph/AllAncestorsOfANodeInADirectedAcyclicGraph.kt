package com.keystarr.algorithm.graph

/**
 * ⭐️ what a peculiar graph traversal type problem, a singular goal and a unique approach
 * LC-2192 https://leetcode.com/problems/all-ancestors-of-a-node-in-a-directed-acyclic-graph/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= nodesAmount <= 10^3;
 *  - 0 <= edges.size <= min(2000, n * (n-1)/2) (cause the graph is acyclic each pair of nodes can have at most 1 (unidirectional) edge);
 *  - no duplicate edges, no self-edges.
 *
 * Final notes:
 *  - 🏅 done [efficient] by myself in 33 mins;
 *  - retrace, what has just happened:
 *   1. interpreted the problem incorrectly, went for the inbound edges approach;
 *   2. finally understood the goal => still went for the inbound edges approach
 *   3. in the middle realized that, since we need all lists sorted ascending, DFS would actually probably be better
 *   since no sorting is required => went for it [efficient]
 *  - all the while maintained solid reasoning and had full understanding of what I was doing and why;
 *  - what a tricky problem, even if I understood the goal correctly initially. 🔥 I thought there must be a better approach
 *   than brute n^2, but, just considered the experience of [MaximalNetworkRank] and decided that, well, actually this
 *   problem looks like it might have only a slightly optimized brute force as its best => lets just try brute first then.
 *   => turns out its the optimal solution!
 *    though if asked if there were any better solutions I guess I still had to think, >97% beats time gave it away here though.
 *
 * Value gained:
 *  - practiced solving a graph properties calculation type problem efficiently using a tricky DFS.
 */
class AllAncestorsOfANodeInADirectedAcyclicGraph {

    // TODO: all is good, but a little unstable, new angle in this problem => must as well reinforce, so repeat in 1-2 weeks

    // TODO: try topological sorting after learning it

    /**
     * problem rephrase:
     *  - given:
     *   - edges: IntArray, a directed acyclic graph represented via an array of edges;
     *   - nodesAmount: Int, nodes are labeled from 0 to nodesAmount.
     * Goal: for each node find the list of all its ancestors, return the list where results\[i] is the ancestors of
     * the ith node, sorted in ascending order (e.g. results[0] is ancestors for the 0th node)
     *
     * When is node Y an ancestor of node X?
     *  only when there is edges\[k][0] == Y and edges\[k][1] == X
     *
     * self edges? duplicate edges?
     *
     * trivial approach #1:
     *  - create map node->ancestors
     *  - iterate through [edges], map\[destination].add(source);
     *  - map.entries.toList().sortedAscending by key.
     * Time: O(m+nlogn), where n=nodesAmount
     *
     * can we do faster?
     *
     * trivial approach #2:
     *  - process [edges] into inbound edges map, node->ancestors;
     *  - iterate through (0 until [nodesAmount]) and results.add(map\[node])
     * Time: O(m+n)
     * Space: O(nodes+edges)
     *  all ancestors across all entries sum up to edges.size
     *
     * can we do even better?
     *
     * edge cases:
     *  - edges.size == 0 => no ancestors, emptyList for each node in the results => correct, we'd add empty lists then.
     *
     * --------------------
     *
     * !!!! INTERPRETED THE PROBLEM STATEMENT WRONG:
     *  ancestor != a node with inbound edge
     *  ancestor for node X is ANY node Y such that from Y there is a path to X.
     *
     * trivial approach #3:
     *  - process [edges] into inbound edges map, node->ancestors;
     *  - iterate through (0 until [nodesAmount]):
     *   - find all ancestors for the current ith node:
     *    - find all direct "parents" of the
     *   -
     *
     * wait, there's a better way
     *
     * ----------------------
     *
     * another approach #4:
     *  - process [edges] into node->outbound edges map;
     *  - iterate through (0 until [nodesAmount]):
     *   - DFS from the current ith node to all nodes we can reach, add the ith node to all such nodes lists in results as an ancestor
     *    => also maintain the sorted ascending order within lists as well (judging by examples its required within also).
     *
     * note: because the graph is acyclic and directed, and we have no duplicate edges, there can be only one edge
     *  in [edges] between nodes X and Y.
     *
     * Time: O(n^2 + m)    wait    NOPE, its really O(n*(n+m) + m) = O(n^2 + n*m) cause each DFS costs on average O(n+m) corrected thanks to Editorial
     *  - building node to edges array is O(n+m);
     *  - main loop:
     *   - exactly [nodesAmount] iterations;
     *   - each iteration we launch DFS:
     *    - worst DFS is when from currentNode there is a path to all other nodes in the graph => O(nodes + edges) time;
     *    - can we have a DAG such that from each node there is a path (not necessarily direct) to every other node?
     *     e.g. 0 -> 1
     *          1 -> 3 -> 0 => that would mean we have a cycle in the graph!
     *     => impossible.
     *     worst is from 0th node we have paths to [1:nodesAmount), from 1st node paths to [2:nodesAmount),
     *     from 2nd to [3:nodesAmount) etc.
     *     => so in such a worst case graph: first DFS costs O(n), second O(n-1), third O(n-2), average DFS cost then is O(n/2)
     *    => n iterations, each costs average O(n/2) => main loops costs O(n^2)
     *    ----------
     *    PARTLY WRONG => nodes amount reasoning for DFS is correct, but we also count edges as part of DFS time complexity
     *    its hard to say how many edges exactly we'd get on average, but its certainly proportional to m
     *    => each DFS asymptotic cost is nodes amount proportional to n and edges amount proportional to m => O(n+m)
     *
     * Space: O(n+m), where n=[nodesAmount], m=[edges].size
     *  - n entries and across all edges lists exactly m=edges.size values;
     *  - seen array allocating multiple times, but at once only holding exactly n values;
     *  - results space isnt considered as is customary.
     */
    fun efficient(nodesAmount: Int, edges: Array<IntArray>): List<List<Int>> {
        val nodeToEdges = Array(size = nodesAmount) { mutableListOf<Int>() }
        edges.forEach { edge ->
            val (source, destination) = edge
            nodeToEdges[source].add(destination)
        }

        val results = Array<MutableList<Int>>(size = nodesAmount) { mutableListOf() }
        (0 until nodesAmount).forEach { ancestor ->
            val seen = BooleanArray(size = nodesAmount)
            seen[ancestor] = true
            dfs(currentNode = ancestor, ancestor = ancestor, nodeToEdges, results, seen)
        }
        return results.toList()
    }

    private fun dfs(
        currentNode: Int,
        ancestor: Int,
        nodeToEdges: Array<MutableList<Int>>,
        results: Array<MutableList<Int>>,
        seen: BooleanArray,
    ) {
        nodeToEdges[currentNode].forEach { destination ->
            if (seen[destination]) return@forEach

            results[destination].add(ancestor)
            seen[destination] = true
            dfs(currentNode = destination, ancestor, nodeToEdges, results, seen)
        }
    }
}

fun main() {
    println(
        AllAncestorsOfANodeInADirectedAcyclicGraph().efficient(
            nodesAmount = 8,
            arrayOf(
                intArrayOf(0, 3),
                intArrayOf(0, 4),
                intArrayOf(1, 3),
                intArrayOf(2, 4),
                intArrayOf(2, 7),
                intArrayOf(3, 5),
                intArrayOf(3, 6),
                intArrayOf(3, 7),
                intArrayOf(4, 6),
            )
        )
    )
}