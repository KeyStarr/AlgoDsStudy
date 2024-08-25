package com.keystarr.algorithm.graph

/**
 * ✨an unusual problem goal/solution path for a graph, just using the inbound/outbound edges concept, no traversal
 * LC-997 https://leetcode.com/problems/find-the-town-judge/description/
 * difficulty: easy (I'd say leet-medium since its kinda an implicit graph with an unusual goal)
 * constraints:
 *  • 1 <= nodesAmount <= 100;
 *  • 0 <= edges.size <= 10^4;
 *  • edges\[i]][0] != edges\[i]][1], i.o. no self-edges;
 *  • ALL EDGES ARE UNIQUE;
 *  • 1 <= edges\[i][0], edges\[i][1] <= n;
 *  • edges\[i].size == 2.
 *
 * Final notes:
 *  • done [map] by myself in 20 mins;
 *  • ⚠️ had to re-read the problem statement and rephrase the goal twice:
 *   • 1st read I thought the goal was to find the node with n-1 OUTBOUND edges;
 *   • 2nd read I interpreted the goal to find the node with n-1 INBOUND edges;
 *   • AND ONLY ON THE 3rd read did I finally get the goal right: find the node with n-1 INBOUND edges and 0 OUTBOUND edges!
 *    hilarious. I really shouldn't rush that much! But I am literally on a timer, so I pattern-match asap...
 *  • ⚠️ literally phrased a question of whether there might be duplicate edges in the input and found no info on it...
 *   but its clearly boldly printed in the problem statement constraints :DDDD "All the pairs of trust are unique."
 *   am I tired or something? losing focus, why am I getting obvious blind spots here 2 huge in a row? ⚠️⚠️
 *   did [efficient] when finally catching the answer to that question. Would've went straight for it if seen it at first.
 *  • 🏆 found the only edge case by myself correctly straight-away, hehe. Its always good checking
 *   🔥 the input constraint-based  edges just in case at least, aside from the problem-specific input edge states.
 *
 * Value gained:
 *  • practiced solving a directed graph problem efficiently using just counting the inbound/outbound edges.
 */
class FindTheTownJudge {

    // TODO: optionally repeat after 1-2 weeks

    /**
     * problem rephrase:
     *  - given:
     *   - trust: a graph in form of the array of edges, each node is labeled from 1 to n;
     *   - n: Int, the number of nodes;
     *  Goal: find the valid node, return its label or -1
     *   - there's guaranteed exactly either 0 or 1 target nodes;
     *   - valid:
     *    - inbound edges == edges.size - 1;
     *    - outbound edges == 0.
     *
     * can we have self-edges? can we have cycles in the graph? apparently we might have cycles in the graph.
     *
     * are edges pairs distinct? can we have edges\[i]==edges\[j]?
     *  no one to ask, no such info in the problem statement => assume we might have that
     *
     * if we didn't have that, if all edges would be distinct, we mightve simply created array(size=n), iterate through [edges]
     *  and increment edgeCounters\[edges\[i][1]]++ and if that value would reach n-1 => just return i.
     *  in other words count inbound edges for each node
     *
     * but since apparently edges duplicate => count actual distinct inbound edges then via a hashmap
     * approach:
     *  1. iterate through [edges], map[edges\[i][1]].put(edges\[i][0]);
     *  2. iterate through the map, if any entry.value == n-1 => return entry.key
     *   otherwise on termination return -1.
     *
     * i.o. transform [edges] into inbound distinct edges for each node, and find the node with [nodesAmount]-1 distinct edges,
     *  if there's none, return -1 or its label.
     *
     * Edge cases:
     *  - edges.size == 0 && nodesAmount > 0 => we have at least one node, but no edges => the property "all other nodes have
     *   edges to the node" if we have exactly one node is trivially true? if we have more than one node is false.
     *
     * quick fix: also build a map with node->outbound edges
     *
     * Time: average/worst O(n+m)
     *  - building the inbound/outbound map is O(m), where m=edges.size;
     *  - main loop is worst we don't have the target node and each node has at least 1 inbound edge => n iterations, where n=nodesAmount, O(n).
     * Space: average/worst O(n+m)
     *  - worst is each node has an edge to each (except itself), i.o. each node has exactly n-1 inbound and n-1 outbound edges;
     *  - each edge from [edges] is present exactly once in a list of some entry in both maps, so we have 2*m additional space
     *   for all edges total in both maps.
     *
     * ----------
     *
     * alternative:
     *  - if the valid node exists, then there's no edges\[i][0]==targetNode. But there might also be other nodes with 0 outbound edges!
     *
     * we could use IntArray for perf boost for counting edges (space is then O(n) and time const is less) instead of storing
     *  actual edges BUT ONLY IF WE HAVE NO DUPLICATE EDGES, and for that we have no info in the problem statement, we might have some!
     */
    fun map(nodesAmount: Int, edges: Array<IntArray>): Int {
        if (nodesAmount == 1 && edges.isEmpty()) return 1

        val nodeToInboundMap = mutableMapOf<Int, MutableList<Int>>()
        val nodeToOutboundMap = mutableMapOf<Int, MutableList<Int>>()
        edges.forEach { edge ->
            val (source, destination) = edge

            if (!nodeToInboundMap.contains(destination)) nodeToInboundMap[destination] = mutableListOf()
            nodeToInboundMap.getValue(destination).add(source)

            if (!nodeToOutboundMap.contains(source)) nodeToOutboundMap[source] = mutableListOf()
            nodeToOutboundMap.getValue(source).add(destination)
        }

        val targetInboundSize = nodesAmount - 1
        nodeToInboundMap.entries.forEach { entry ->
            val (node, inboundEdges) = entry
            if (inboundEdges.size == targetInboundSize && !nodeToOutboundMap.contains(node)) return node
        }
        return -1
    }

    /**
     * Re-read the problem statement => indeed there are guaranteed no duplicate edges
     * => use simple 2 IntArrays to count edges.
     *
     * Time: average/worst O(m+n),
     * Space: O(n)
     */
    fun efficient(nodesAmount: Int, edges: Array<IntArray>): Int {
        if (nodesAmount == 1 && edges.isEmpty()) return 1

        val sizeFromOne = nodesAmount + 1
        val outboundEdgesCount = IntArray(size = sizeFromOne)
        val inboundEdgesCount = IntArray(size = sizeFromOne)
        edges.forEach { edge ->
            val (source, destination) = edge
            outboundEdgesCount[source]++
            inboundEdgesCount[destination ]++
        }

        val targetInbound = nodesAmount - 1
        outboundEdgesCount.forEachIndexed { node, outboundCount ->
            if (outboundCount == 0 && inboundEdgesCount[node] == targetInbound) return node
        }
        return -1
    }
}

fun main() {
    println(
        FindTheTownJudge().map(
            nodesAmount = 2,
            edges = arrayOf(intArrayOf(1, 2)),
        )
    )
}
