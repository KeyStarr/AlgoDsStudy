package com.keystarr.algorithm.graph.djkstra

import java.util.PriorityQueue

/**
 * ⭐️ such a simple statement yet such a hard solution, I'm still not grasping it completely
 * LC-787 https://leetcode.com/problems/cheapest-flights-within-k-stops/description/
 * difficulty: medium (definitely a leet-hard!)
 * constraints:
 *  • 1 <= n=nodesAmount <= 100
 *  • 0 <= adjacencyArray.size <= n*(n-1)/2
 *  • adjacencyArray\[i].size == 3
 *  • 0 <= from ith, to ith < n
 *  • from ith != to ith
 *  • 1 <= price ith <= 10^4
 *  • no multiple flights between cities => exactly one edge between 2 cities?
 *  • 0 <= start, end, pathMaxNodes < nodes
 *  • start != end
 *
 * Final notes:
 *  • failed 6 (SIX!!) submissions. That's the new anti-record, beats even learning DP for me;
 *  • my critical mistake:
 *   just learned the concept and a typical implementation pattern for Dijkstra's, and for this problem ASSUMED (that wasn't in the article)
 *    that we could just run Dijkstra's until the heap is empty (modified with the max stops constraint ofc), and then simply
 *    return `minPathWeights\[finish]`;
 *     => actually we might have the last visit to the `finish` node such that it is less in stops than actual min weight result
 *     => we'd simply overwrite the min weight result with less stops, but more weight => incorrect answer
 *     => actually, we need to return the weight of the first path to reach `finish`. It is GUARANTEED to be the min weight
 *      path, cause each time we reach a new node path weight increases (weight>0) => if we added the final node into the
 *      heap, yet there's a less weight path in the graph, we won't take that node out of the heap before we walk that path fully,
 *      because then at EACH node that path's weight will be less than the added path's weight!
 *     + we add the finish node into the heap ONLY when we actually VISIT the node BEFORE IT (as it's neighbor) =>
 *      if we return on adding, it's wrong, coz it might not be the min path.
 *    🔥PROVED why we must return the path weight of the finish node the first time we REMOVE it out of the heap (not add!).
 *
 *  • it's so weird that we update BOTH the stops AND the min weight if encounter either better stops OR weight. Well we
 *   update both cause we consider it the best combination, and these are, like, 2 properties of a single path, BUT
 *   why update on just EITHER condition met? why not on ONLY less weight OR less stops? Well [trackOnlyMinStops] actually
 *   excludes weight from the rules;
 *
 *  • I had to spend good 3-4 hours and dry-run up to 10 times to understand exactly why we return just when we visit the
 *   finish node first, and I still don't even understand clearly how does "track min stops only" solution works. Neither
 *   the complexities of both solutions. Why so hard for me? A great learning opportunity though.
 *
 * Value gained:
 *  • practiced adapting Dijkstra's algorithm to a min path directed graph problem with an additional constraint on the path;
 *  • TODO: figure out exactly why does [trackOnlyMinStops] work and what and why exactly are the complexities for both solutions.
 *   decided to postpone, since the topic is quite niche for current goals, yet requires so much effort
 */
class CheapestFlightsWithinKStops {

    /**
     * problem rephrase:
     *  - given:
     *   - a weighted directed graph defined via:
     *    - amountOfNodes: Int;
     *    - an adjacency array with weights Array<IntArray>;
     *   - startNode: Int
     *   - endNode: Int
     *   - maxNodes: Int
     *  - goal: return the minimum path weight from [start] to [finish] with at most [maxStops]+2 nodes, or -1 if there's no such route.
     *
     * goal = a path with minimum weight => try Dijkstra's
     * additional constraint = at most [maxNodes], how to check for it?
     *  record for each NodeVisit: node, pathWeight and also nodesVisited, if, upon taking an edge, nodesVisited == [maxNodes] - 1
     *   and node != endNode don't consider using that edge, prune.
     *
     * main body = pure Dijkstra's
     *
     * Edge cases:
     *  - nodesAmount==1 => impossible, cause start != end, problem constraints are wrong;
     *  - no edges from the [start] => return -1, correct;
     *  - max path weight = 100*10^4=10^6 => use Int.MAX for path weight initial.
     *
     * // TODO: figure out, clarify complexities
     * Time:
     * Space:
     */
    fun trackMinStopsAndWeight(
        nodesAmount: Int,
        adjacencyArray: Array<IntArray>,
        start: Int,
        finish: Int,
        maxStops: Int,
    ): Int {
        val nodeToEdgesMap = adjacencyArray.toMap()
        val minHeap = PriorityQueue<NodeVisit> { o1, o2 -> o1.pathWeight - o2.pathWeight }.apply {
            add(NodeVisit(node = start, pathWeight = 0, stops = 0))
        }
        val minPathWeights = Array(size = nodesAmount) {
            PathMetrics(
                weight = PATH_WEIGHT_DEFAULT,
                stops = Int.MAX_VALUE,
            )
        }.apply { set(start, PathMetrics(weight = 0, stops = 0)) }

        while (minHeap.isNotEmpty()) {
            val visit = minHeap.remove()
            if (visit.node == finish) return visit.pathWeight

            val prevMetrics = minPathWeights[visit.node]
            if (visit.pathWeight > prevMetrics.weight && visit.stops > prevMetrics.stops) continue

            val edges = nodeToEdgesMap[visit.node] ?: continue
            val isOnlyEnd = visit.stops == maxStops
            val newStops = visit.stops + 1
            edges.forEach { edge ->
                val next = edge.end
                if (isOnlyEnd && next != finish) return@forEach

                val newPathWeight = visit.pathWeight + edge.weight
                val nextPathMetrics = minPathWeights[next]
                if (newPathWeight >= nextPathMetrics.weight && newStops >= nextPathMetrics.stops) return@forEach
                nextPathMetrics.apply {
                    weight = newPathWeight
                    stops = newStops
                }
                minHeap.add(NodeVisit(node = next, pathWeight = newPathWeight, stops = newStops))
            }
        }

        return -1
    }

    /**
     * The official Editorial solution, I dry ran it, but I'm still not sure why it works - why can we only track min stops
     *  and just process nodes in the order of ascending pathWeight => why does it give us the correct solution?
     * TODO: revisit and figure it out finally
     */
    fun trackOnlyMinStops(
        nodesAmount: Int,
        adjacencyArray: Array<IntArray>,
        start: Int,
        end: Int,
        maxStops: Int,
    ): Int {
        val nodeToEdgesMap = adjacencyArray.toMap()
        val minHeap = PriorityQueue<NodeVisit> { o1, o2 -> o1.pathWeight - o2.pathWeight }.apply {
            add(NodeVisit(node = start, pathWeight = 0, stops = 0))
        }
        val minStops = Array(size = nodesAmount) { PATH_MIN_NODES_DEFAULT }.apply { set(start, 0) }

        while (minHeap.isNotEmpty()) {
            val visit = minHeap.remove()

            if (visit.node == end) return visit.pathWeight
            if (visit.stops > maxStops || visit.stops > minStops[visit.node]) continue
            minStops[visit.node] = visit.stops

            val edges = nodeToEdgesMap[visit.node] ?: continue
            val newStops = visit.stops + 1
            edges.forEach { edge ->
                minHeap.add(NodeVisit(node = edge.end, pathWeight = visit.pathWeight + edge.weight, stops = newStops))
            }
        }

        return -1
    }

    private fun Array<IntArray>.toMap(): Map<Int, List<Edge>> = mutableMapOf<Int, MutableList<Edge>>().also { map ->
        forEach { edge ->
            val (start, end, weight) = edge
            if (!map.contains(start)) map[start] = mutableListOf()
            map.getValue(start).add(Edge(end = end, weight = weight))
        }
    }

    private class Edge(val end: Int, val weight: Int)

    private class NodeVisit(val node: Int, val pathWeight: Int, val stops: Int)

    private class PathMetrics(var weight: Int, var stops: Int)
}

private const val PATH_MIN_NODES_DEFAULT = Int.MAX_VALUE
private const val PATH_WEIGHT_DEFAULT = Int.MAX_VALUE

fun main() {
    println(
        CheapestFlightsWithinKStops().trackMinStopsAndWeight(
//            nodesAmount = 9,
//            adjacencyArray = arrayOf(
//                intArrayOf(0,1,1),intArrayOf(1,2,1),intArrayOf(2,3,1),intArrayOf(3,7,1),intArrayOf(0,4,3),intArrayOf(4,5,3),intArrayOf(5,7,3),intArrayOf(0,6,5),intArrayOf(6,7,100),intArrayOf(7,8,1)
//            ),
//            start = 0,
//            finish = 8,
//            maxStops = 3,
            nodesAmount = 3,
            adjacencyArray = arrayOf(
                intArrayOf(0, 1, 100),
                intArrayOf(1, 2, 100),
                intArrayOf(0, 2, 500),
            ),
            start = 0,
            finish = 2,
            maxStops = 0,
        )
    )
}
