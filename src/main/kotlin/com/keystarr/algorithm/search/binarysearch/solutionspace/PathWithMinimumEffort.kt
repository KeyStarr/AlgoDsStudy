package com.keystarr.algorithm.search.binarysearch.solutionspace

import java.util.*
import kotlin.math.abs
import kotlin.math.max

/**
 * ⭐️LC-1631 https://leetcode.com/problems/path-with-minimum-effort/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= rows, columns <= 100
 *  • 1 <= heights\[i]\[j] <= 10^6
 *
 * Final notes:
 *  • done [firstAttemptTLE] first, failed a submit due to TLE;
 *  • read one of the solutions, optimized to [suboptimal];
 *  • read further, optimized to [sonWeHaveDijkstrasAtHome];
 *  • finally read the DSA course solution and done [efficientViaBinarySearch];
 *  • I would've never in a million years thought of even attempting a binary search in that kind of problem. It's a graph,
 *   right? Let's do BFS or DFS and optimize visiting nodes based on the path metric. Turns out there's more to it;
 *  • interesting [sonWeHaveDijkstrasAtHome] apparently resembles the classic Dijkstra's algorithm for shortest path within a
 *   weight graph. Need to look into that later and master it better.
 *
 * Value gained:
 *  • learnt a grand surprise lesson: binary search can be applied even to graph problems to get the efficient solution;
 *  • practiced applying binary search on the solution space of the graph best path metric;
 *  • practiced Dijkstra's to solve a problem with unusual path weight calculation rule.
 *
 */
class PathWithMinimumEffort {

    private val allowedDirections = arrayOf(
        intArrayOf(0, 1), // right
        intArrayOf(0, -1), // left
        intArrayOf(-1, 0), // top
        intArrayOf(1, 0), // bottom
    )

    /**
     * Problem rephrase:
     *  - given a matrix, where each cell contains an integer;
     *  - path's effort = MAXIMUM abs diff between two consecutive cells on the path.
     *  - moves: up, down, left, right
     * Goal: find a path from (0,0) to (row-1, column-1) with the minimum effort, return the effort.
     *
     * Matrix => model the task via graphs?
     *
     * Do multi-visit BFS, for each node's visit record the current "pathEffort", update it if the abs diff between
     *  the next and current nodes is more that the current node's pathEffort.
     *  Each time we reach (rows-1, columns-1) => update the global "minPathEffort" variable.
     *  Implement seen via a 2D array of HashSet's (row,column,currentPathEffort)
     * Time: approximately O(n^2*m^2) TODO: figure out and proof the precise time estimate here
     *  - BFS with a single visit per each node is O(nodes+edges), which here would be O(n*m + n*m*4) = always O(n*m)
     *  - but here we allow multiple visits to same node as long as the path context (pathEffort) wasn't seen before
     *   => how many multi-visit are possible average/worst for a given node??? intuitively, seems to depend on n*m,
     *   cause in worst case it scales proportionally to the number of nodes.
     * Space: again, approximately smth like O(n^2*m^2) for multi-visit `seen`
     *
     * Edge cases:
     *  - all cells are at least 1 => no negative numbers => only subtraction, fits into int;
     *  - n==1, m==1, a matrix of 1 element => min effort is always 0 => early return 0 (cause main algo would incorrectly
     *   give Int.MAX_VALUE since the while loop wouldn't be executed even once)
     *  - a matrix of 2 elements => correct always, cause we'd calculate the path exactly toward the second element
     *   and update minPathEffort with it.
     */
    fun firstAttemptTLE(heights: Array<IntArray>): Int {
        val m = heights.size
        val n = heights[0].size
        if (m == 1 && n == 1) return 0

        val queue: Queue<NodeVisit> = ArrayDeque<NodeVisit>().apply {
            add(NodeVisit(row = 0, column = 0, pathEffort = 0))
        }
        val seen = Array(size = m) { Array(size = n) { mutableSetOf<Int>() } }
        seen[0][0].add(0)

        var minPathEffort = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val nodeVisit = queue.remove()
            allowedDirections.forEach { direction ->
                val newRow = nodeVisit.row + direction[0]
                val newColumn = nodeVisit.column + direction[1]
                if (newRow !in heights.indices || newColumn !in heights[0].indices) return@forEach

                val newPathEffort = max(
                    nodeVisit.pathEffort,
                    abs(heights[nodeVisit.row][nodeVisit.column] - heights[newRow][newColumn]),
                )
                if (seen[newRow][newColumn].contains(newPathEffort)) return@forEach

                if (newRow == m - 1 && newColumn == n - 1 && newPathEffort < minPathEffort) {
                    minPathEffort = newPathEffort
                    return@forEach
                }

                queue.add(NodeVisit(row = newRow, column = newColumn, pathEffort = newPathEffort))
                seen[newRow][newColumn].add(newPathEffort)
            }
        }

        return minPathEffort
    }

    /**
     * A simple optimization based on the common observation for matrix-based graph problems:
     * if we arrive at the node X with minimization metric (path effort) being greater than or equal to a visit to that
     * same node X we've already made => we should not add that visit to the queue, as no path context influences further
     * choices from that node X.
     *
     * Best case is we visit each node only once, but worst case is that each visit to the node comes from a path with
     * less path effort => we visit the node the same amount of times as in [firstAttemptTLE]?
     *
     * Time: ??? how to even estimate that here? worst case seems to be same as in [firstAttemptTLE], so same?
     * Space: same here
     */
    fun suboptimal(heights: Array<IntArray>): Int {
        val m = heights.size
        val n = heights[0].size
        if (m == 1 && n == 1) return 0

        val queue: Queue<NodeVisit> = ArrayDeque<NodeVisit>().apply {
            add(NodeVisit(row = 0, column = 0, pathEffort = 0))
        }
        val seen = Array(size = m) { IntArray(size = n) { Int.MAX_VALUE } }
        seen[0][0] = 0

        var minPathEffort = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val nodeVisit = queue.remove()
            allowedDirections.forEach { direction ->
                val newRow = nodeVisit.row + direction[0]
                val newColumn = nodeVisit.column + direction[1]
                if (newRow !in heights.indices || newColumn !in heights[0].indices) return@forEach

                val newPathEffort = max(
                    nodeVisit.pathEffort,
                    abs(heights[nodeVisit.row][nodeVisit.column] - heights[newRow][newColumn]),
                )
                if (newPathEffort >= seen[newRow][newColumn]) return@forEach

                if (newRow == m - 1 && newColumn == n - 1 && newPathEffort < minPathEffort) {
                    minPathEffort = newPathEffort
                    return@forEach
                }

                queue.add(NodeVisit(row = newRow, column = newColumn, pathEffort = newPathEffort))
                seen[newRow][newColumn] = newPathEffort
            }
        }

        return minPathEffort
    }

    /**
     * retrospective: only Dijkstra's inspired, cause we do indeed prune if effort to reach a node is greater/equals to
     *  the effort we've already encountered to reach that node, BUT we don't use min heap => pop out the min node each time, just the next one in the order of insertion.
     *
     * so this is just a BFS with multi-path context and quite a complicated time complexity, coz we could technically
     *  traverse from the same node multiple times! At first we'd visit it with some effort X, and later with effort Y<X etc
     *
     * so this is not efficient, its not Dijkstra's
     *
     * -----
     *
     * Further optimization from [suboptimal] => at each step consider remove only the node with the best path context
     * metric. It doesn't mean though that the first time we arrive at the end node it'll be by the shortest path,
     * cause the best path effort locally may later turn into even the worst one, and the worse one locally can become
     * better with further progress.
     *
     * In fact, worst case is that the best path is actually the worst one until the very last node, where it becomes
     * the best one => we consider it the last, after considering each other path (though, of course, visiting each node
     * the minimum number of times due to seen prioritizing the min path effort)
     *
     * Time: ??? TODO: how to even estimate that here?
     * Space: ???
     */
    fun sonWeHaveDijkstrasAtHome(heights: Array<IntArray>): Int {
        val m = heights.size
        val n = heights[0].size
        if (m == 1 && n == 1) return 0

        val queue: Queue<NodeVisit> = PriorityQueue { o1, o2 -> o1.pathEffort - o2.pathEffort }
        queue.add(NodeVisit(row = 0, column = 0, pathEffort = 0))
        val seen = Array(size = m) { IntArray(size = n) { Int.MAX_VALUE } }
        seen[0][0] = 0

        var minPathEffort = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val nodeVisit = queue.remove()
            allowedDirections.forEach { direction ->
                val newRow = nodeVisit.row + direction[0]
                val newColumn = nodeVisit.column + direction[1]
                if (newRow !in heights.indices || newColumn !in heights[0].indices) return@forEach

                val newPathEffort = max(
                    nodeVisit.pathEffort,
                    abs(heights[nodeVisit.row][nodeVisit.column] - heights[newRow][newColumn]),
                )
                if (newPathEffort >= seen[newRow][newColumn]) return@forEach

                if (newRow == m - 1 && newColumn == n - 1 && newPathEffort < minPathEffort) {
                    minPathEffort = newPathEffort
                    return@forEach
                }

                queue.add(NodeVisit(row = newRow, column = newColumn, pathEffort = newPathEffort))
                seen[newRow][newColumn] = newPathEffort
            }
        }

        return minPathEffort
    }

    /**
     * - single source node, single end node;
     * - when we reach the ith node, the only metric of the path before that actually matters what is the current path's effort?
     *  the further directions=edges we can take are always the same (except pruning by minEffort, but that's irrelevant now);
     * - goal: a path with minimum characteristic reminiscent of a weight.
     * => try Dijkstra's?
     *
     * Algorithm:
     *  - init:
     *   - minHeap with node->effort;
     *   - minEfforts, where minEfforts\[i] is the min effort encountered for the ith node across all paths.
     *  - at each step minHeap.pop() and add only those neighbors into the heap to which the path effort is less than
     *   the path effort previously encountered;
     *   - after popping check first that the current node's path effort is still the least encountered by comparing the one
     *    from the heap with `minEfforts[i]`. If not - prune current path;
     *   - if we encounter the end node => don't continue.
     * - return minEfforts\[endNode]
     *
     * Edge cases:
     *  - max path effort = maxHeight - minHeight = 10^6 - 0 = 10^6 < Int.MAX_VALUE, could init minEfforts array with MAX_VALUE's;
     *  - m==n==1, i.o. a single node graph => we'd fail, cause we'd have no neighbors to go to and never update minEfforts
     *   array => always early return 0.
     *
     * Time: O((nodes + edges) * log(nodes))
     *  - we check all nodes exactly once, and we try each edge exactly once => O(nodes + edges);
     *  - try edge = add node to heap if current path effort is the least we've seen for that node = adding/removing
     *   costs O(logk), worst k=nodes (each node has an edge to each), and at first we'd add all nodes to the heap for O(nlogn),
     *   on average the amount of nodes we'd actually add into the heap at each node varies, but depends on O(nodes).
     * Space: O(nodes)
     *  - minEfforts array O(nodes)
     *  - heap worst O(nodes)
     */
    fun actualDijkstras(heights: Array<IntArray>): Int {
        val m = heights.size
        val n = heights[0].size
        if (m == 1 && n == 1) return 0

        val minHeap = PriorityQueue<NodeVisitDijkstra> { o1, o2 -> o1.effort - o2.effort }.apply {
            add(NodeVisitDijkstra(row = 0, column = 0, effort = 0, lastHeight = heights[0][0]))
        }

        val minEfforts = Array(size = m) { IntArray(size = n) { Int.MAX_VALUE } }
        while (minHeap.isNotEmpty()) {
            val visit = minHeap.remove()
            if (visit.effort > minEfforts[visit.row][visit.column]) continue

            allowedDirections.forEach { direction ->
                val newRow = visit.row + direction[0]
                val newColumn = visit.column + direction[1]

                if (newRow !in heights.indices || newColumn !in heights[0].indices) return@forEach

                val newHeight = heights[newRow][newColumn]
                val newEffort = max(abs(newHeight - visit.lastHeight), visit.effort)
                if (newEffort >= minEfforts[newRow][newColumn]) return@forEach
                minEfforts[newRow][newColumn] = newEffort

                if (newRow == m - 1 && newColumn == n - 1) return@forEach
                minHeap.add(
                    NodeVisitDijkstra(
                        row = newRow,
                        column = newColumn,
                        effort = newEffort,
                        lastHeight = newHeight,
                    )
                )
            }
        }

        return minEfforts[m - 1][n - 1]
    }

    private class NodeVisitDijkstra(
        val row: Int,
        val column: Int,
        val effort: Int,
        val lastHeight: Int,
    )

    // TODO: solve VIA A PURE DIJKSTRA'S AFTER FINISHING THE COURSE. or is efficientDijkstra exactly that? if so - why? prove

    /**
     * Goal => find the path with the best metric.
     * What are possible solutions = best metrics?
     *  low boundary: 0 (any path where all numbers are equal), really though heights.secondMin() - heights.min(), but
     *   it's not that practical to find 2 mins, easier to just start with a 0;
     *  high boundary: heights.max(), though really heights.max()-heights.min().
     *
     * We search for the minimum path effort in that given solution space => try a solution space binary search?
     *  Checking binary search applicability criteria (minimization):
     *  - if a path with at most effort X is possible, then it's also possible for all values greater than X (at least
     *   that very same path);
     *  - if there's NO path with at most effort X (there are no paths, or all paths require greater effort)
     *   => there are no paths with effort less than X.
     * => binary search on that solution space is applicable.
     *
     * Design:
     *  - init high and low boundary variables;
     *  - launch a binary search while left <= right and for each middle value perform a DFS:
     *      (use the classic "find an existing element" binary search pattern, not the insertion, cause the solution
     *       is guaranteed to exist in our solutions space)
     *   - if a path with effort at most `middle` exists => go to the left half;
     *   - else => go to the right half.
     *
     * goal - return true if there's a path through heights with effort less than maxEffort.
     * dfs(heights: IntArray, maxEffort: Int): Boolean
     *
     * Total Time: average/worst O(n*m*logk)
     *  - binary: search average/worst O(logk), where k=heights.max()-heights.min()
     *  - DFS: always O(nodes+e)=O(n*m+n*m*4)=O(n*m)
     * Total Space: O(n*m)
     *  - DFS for a single maxEffort costs average/worst O(g), where g = the deepest path in the graph, and average/worst
     *   depends on n*m, so O(n*m)
     */
    fun efficientViaBinarySearch(heights: Array<IntArray>): Int {
        val min = heights.minOf { it.minOf { it } }
        val max = heights.maxOf { it.maxOf { it } }

        var left = 0
        var right = max - min
        while (left <= right) {
            val middle = left + (right - left) / 2
            val doesPathExist = doesPathExistDfs(
                heights = heights,
                seen = Array(size = heights.size) { BooleanArray(size = heights[0].size) },
                maxEffort = middle,
                currentRow = 0,
                currentColumn = 0,
            )
            if (doesPathExist) {
                right = middle - 1
            } else {
                left = middle + 1
            }
        }
        return left
    }

    private fun doesPathExistDfs(
        heights: Array<IntArray>,
        seen: Array<BooleanArray>,
        maxEffort: Int,
        currentRow: Int,
        currentColumn: Int,
    ): Boolean {
        if (currentRow == heights.size - 1 && currentColumn == heights[0].size - 1) return true

        val currentHeight = heights[currentRow][currentColumn]
        for (direction in allowedDirections) {
            val newRow = currentRow + direction[0]
            val newColumn = currentColumn + direction[1]
            if (newRow !in heights.indices || newColumn !in heights[0].indices || seen[newRow][newColumn]) continue

            val newHeight = heights[newRow][newColumn]
            if (abs(currentHeight - newHeight) <= maxEffort) {
                seen[newRow][newColumn] = true
                val doesExist = doesPathExistDfs(heights, seen, maxEffort, newRow, newColumn)
                if (doesExist) return true
            }
        }
        return false
    }

    class NodeVisit(
        val row: Int,
        val column: Int,
        val pathEffort: Int,
    )
}

fun main() {
    println(
        PathWithMinimumEffort().actualDijkstras(
            heights = arrayOf(
                intArrayOf(1, 2, 2),
                intArrayOf(3, 8, 2),
                intArrayOf(5, 3, 5),
            )
//            heights = arrayOf(
//                intArrayOf(1, 2, 1, 1, 1),
//                intArrayOf(1, 2, 1, 2, 1),
//                intArrayOf(1, 2, 1, 2, 1),
//                intArrayOf(1, 2, 1, 2, 1),
//                intArrayOf(1, 1, 1, 2, 1),
//            )
        )
    )
}
