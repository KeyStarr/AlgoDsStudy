package com.keystarr.algorithm.graph.tree

import java.util.*
import kotlin.math.max

/**
 * LC-1376 https://leetcode.com/problems/time-needed-to-inform-all-employees/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nodesAmount == managers.size == informTime.size <= 10^5;
 *  • 0 <= rootInd < nodesAmount;
 *  • if the ith node has no children informTime\[i] == 0;
 *  • 0 <= informTime\[i] <= 1000.
 *
 * Final notes:
 *  • failed the 1st submission due to the absolutely wrong approach! I just pattern matched and went on to solve, even
 *   WROTE that the goal is to find the total cost sum across all paths... when in reality we traverse the tree in parallel,
 *   through all paths => obviously, the goal is to return the max path cost (since all other paths would be finished by then);
 *  • done [dfsFromRoot] and [bfsFromRoot] by myself in ~50 mins 😱. Due to that first confusion mostly, and optimized quite
 *   a bit, at first implemented dfs as to "return all paths costs" and added them into the list at leaves, which clearly is
 *   not necessary, just return the max path cost for each subtree;
 *
 * Value gained:
 *  • practiced recognizing and using DFS and BFS for an efficient solution of the tree traversal problem.
 */
class TimeNeededToInformAllEmployees {


    // TODO: implement a bottom-up either DFS or BFS (without converting managers into a hashmap), for funsies
    // TODO: what do these top solutions (from time bars) do to have twice less runtime then the 3 of my submissions?
    // since accepted, move on for now

    /**
     * problem rephrase:
     *  - given:
     *   - a valid weighted tree, represented via an array, where the index is the child and value is the parent. The root has value -1;
     *    so, a node can have many children, but 1 or no parent (only root);
     *   - nodes are labeled from 0 to n-1;
     *   - informTime: Int, where informTime\[i] is the cost to traverse from the ith node to all its children at the same time
     *    in parallel paths;
     *  - goal: return the maximum path cost. Cause by then all other paths will be traversed, since we traverse all them in parallel.
     *
     * since we have a valid tree, each node except the root has a parent and all nodes except the leaves have
     *  at least one child, and root is defined via [rootInd] => all nodes can be visited, that's guaranteed
     *
     * also since input is a valid tree => we have exactly 1 path towards each leaf => no need for dijkstra's, just either DFS
     * or BFS will do.
     *
     * Do DFS cause its faster to implement.
     * Transform managers into the hashmap for ease of implementation and clarity. Could do as-is though, but it would be more
     *  complicated, yet the same asymptotic time/space complexity.
     *
     * Edge cases:
     *  - nodesAmount == 1 => always return 0 => correct;
     *  - sum => max cost = 10^5*10^3=10^8 => fits into Int.
     *
     * Time: O(nodes + edges)
     *  - map construction is O(nodes)
     * Space: O(nodes + edges)
     *  - map takes O(nodes + edges)
     *  - dfs callstack is the depth of the tree O(k), worst is O(n).
     */
    fun topDownDfs(nodesAmount: Int, rootInd: Int, managers: IntArray, informTime: IntArray): Int {
        val nodeToChildrenMap = managers.toParentToChildrenMap()
        return dfsFromRoot(
            node = rootInd,
            nodeToChildrenMap = nodeToChildrenMap,
            costs = informTime,
        )
    }

    /**
     * Goal - return the maximum path cost in the tree rooted at [node].
     */
    private fun dfsFromRoot(
        node: Int,
        nodeToChildrenMap: Map<Int, List<Int>>,
        costs: IntArray,
    ): Int {
        val nextTraverseCost = costs[node]
        if (nextTraverseCost == 0) return 0

        var maxPathCost = -1
        nodeToChildrenMap[node]?.forEach { child ->
            val childMaxPathCost = dfsFromRoot(node = child, nodeToChildrenMap, costs)
            maxPathCost = max(childMaxPathCost, maxPathCost)
        }
        return maxPathCost + costs[node]
    }

    /**
     * Goal - return the maximum path cost.
     *
     * Time: always O(nodes + edges)
     * Space: always O(nodes + edges)
     */
    fun bfsFromRoot(nodesAmount: Int, rootInd: Int, managers: IntArray, informTime: IntArray): Int {
        val nodeToChildrenMap = managers.toParentToChildrenMap()
        val queue: Queue<NodeVisit> = ArrayDeque<NodeVisit>().apply { add(NodeVisit(node = rootInd, costSoFar = 0)) }
        var maxPathCost = -1
        while (queue.isNotEmpty()) {
            val visit = queue.remove()
            val children = nodeToChildrenMap[visit.node]
            if (children != null) {
                children.forEach { child ->
                    queue.add(NodeVisit(node = child, costSoFar = visit.costSoFar + informTime[visit.node]))
                }
            } else if (visit.costSoFar > maxPathCost) maxPathCost = visit.costSoFar
        }
        return maxPathCost
    }

    private fun IntArray.toParentToChildrenMap() = mutableMapOf<Int, MutableList<Int>>().also { map ->
        forEachIndexed { child, parent ->
            if (!map.contains(parent)) map[parent] = mutableListOf()
            map.getValue(parent).add(child)
        }
    }

    private class NodeVisit(val node: Int, val costSoFar: Int)
}

fun main() {
    println(
        TimeNeededToInformAllEmployees().bfsFromRoot(
            nodesAmount = 6,
            rootInd = 2,
            managers = intArrayOf(2, 2, -1, 2, 2),
            informTime = intArrayOf(0, 0, 1, 0, 0, 0),
        )
    )
}
