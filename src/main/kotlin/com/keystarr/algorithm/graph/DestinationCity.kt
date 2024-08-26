package com.keystarr.algorithm.graph

/**
 * ⭐️ a great example of a tricky graph characteristic problem without traversal, but with a clever trick one has to acutely observe
 * LC-1436 https://leetcode.com/problems/destination-city/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= paths.size <= 100;
 *  • paths\[i].size == 2
 *  • 1 <= paths\[0].length, paths\[1].length <= 10;
 *  • paths\[0] != paths\[1];
 *  • all strings consists of lowercase and uppercase English and space char.
 *
 * Final notes:
 *  • done [suboptimal] by myself in 15 mins;
 *  • yet again an example of a harder leet-easy problem, where to get the best solution one would have to make
 *   acute observations. Here - that we can use just do 2 passes and via exclusion get 1 set. There seems to be an entire
 *   category of problems like this. Didn't see approaches to [efficient] in 10 mins => went to check the solution ⚠️.
 *
 * Value gained:
 *  • practiced solving a directed graph problem using graph properties, without traversal.
 */
class DestinationCity {

    // TODO: retry in 1-2 weeks

    /**
     * given:
     *  - paths: List<List<String>> - an array of edges, modelling a directed graph always in form of a line, with no loops;
     *  goal: return the only node with 0 outbound edges (the "destination" node, the end node in the line)
     *
     * the answer is guaranteed to exist due to the guaranteed shape of the graph.
     *
     * observations:
     *  - due to the graph being a line, each node except the last appears exactly once in some edges\[i] at [0] position
     *   => the end node is the only one that doesn't
     *
     * =>
     * 1. since we have only 100 nodes we could allocate set hasOutbound;
     * 2. iterate through [edges] and set hasOutbound for [0];
     * 3. iterate through hasOutbound and return the only value that is false.
     *
     * Time: always O(m+n)
     *  - adding a node to a set is O(k), where k=node label length, worst k=10, so its O(10)=O(1);
     *  - we make m iterations, where m=edges.size, and for each iteration perform 3 adds O(3*k)=O(1);
     *  - iterate through nodes, n iterations, worst is n iterations since target is the last node, each iterations costs for checking
     *   in the set O(k)=O(1).
     * Space: O(n)
     */
    fun suboptimal(edges: List<List<String>>): String {
        val hasOutbound = mutableSetOf<String>()
        val nodes = mutableSetOf<String>()
        edges.forEach { edge ->
            val (source, destination) = edge
            hasOutbound.add(source)
            nodes.add(source)
            nodes.add(destination)
        }
        return nodes.first { node -> !hasOutbound.contains(node) }
    }

    /**
     * can we improve time to O(m) or space to O(1)?
     *
     * time: don't iterate through nodes
     * space: don't save all nodes that we have, just do with edges
     *
     * => maybe we can even solve in one pass through edges without any temp collections?
     *
     * ----
     *
     * observations:
     *  - each node except the first one appears exactly once as the destination of some edge => we don't need
     *   the first one, and we never need the source node of an edge, since its always not the target (as target has no
     *   outbound edges) => add only the destination node into the set;
     *  - if we added some node as destination previously into the set, and now we encounter an edge with it being the source
     *   => remove that node from the set.
     * ==> as we've processed all edges, we'd have exactly 1 node in the set, the target. Since every node except it appears
     *  exactly once as the source node.
     * => use 1 set only
     *
     * Time: always O(m)
     * Space: average/worst O(n)
     *
     * Learned thanks to https://leetcode.com/problems/destination-city/solutions/609868/java-tricky-4-lines-using-one-set/
     */
    fun efficient(edges: List<List<String>>): String {
        val destinationsWithoutSource = mutableSetOf<String>()
        edges.forEach { destinationsWithoutSource.add(it[1]) }
        edges.forEach { destinationsWithoutSource.remove(it[0]) }
        return destinationsWithoutSource.first()
    }
}

fun main() {
    println(
        DestinationCity().efficient(
            edges = listOf(
                listOf("B", "C"),
                listOf("D", "B"),
                listOf("C", "A"),
            )
        )
    )
}
