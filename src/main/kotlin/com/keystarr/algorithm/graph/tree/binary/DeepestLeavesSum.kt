package com.keystarr.algorithm.graph.tree.binary

import java.util.ArrayDeque
import java.util.Queue

/**
 * LC-1302
 * difficulty: medium
 * constraints:
 *  • 1 <= number of nodes <= 10^4;
 *  • 1 <= node.value <= 100;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • implemented [bfs] in 15 mins, 1st time submit.
 *
 * Value gained:
 *  • [bfsCleaner] might be a follow-up, but is it even worth it? Yea, if we have a large tree we might save quite some
 *      computations, but then again, each iteration we allocate a new object and a new array (for the deque) => isn't
 *      that more expensive than simply summing up the values?
 *  • practiced recognizing and solving a BFS-first (kinda) problem.
 */
class DeepestLeavesSum {

    /**
     * BFS - iterate until the last level, return its sum (time O(n), space O(n));
     * DFS - we'd have to check every node anyway, so same time/space complexities.
     *
     * Since the solution is level-first over depth => BFS will probably be easier to implement with same time/space
     * big O complexities => BFS.
     *
     * Idea, implement BFS:
     *  - in the inner loop (popping all current level nodes from the queue) calculate the sum of all values in the current
     *      level as we pop each node of it;
     *  - at the end of the outer loop, return the current sum (of the latest = deepest level).
     *
     * Edge cases:
     *   - number of nodes == 1 => correct as-is;
     *   - no special dependencies on nodes order/subtrees organization;
     *   - 10^4 * 10^2 => 10^6 max sum, fits into Int.
     *
     * Time: O(n)
     * Space: O(n)
     */
    fun bfs(root: IntBinaryTreeNode): Int {
        val currentLevelNodesQueue: Queue<IntBinaryTreeNode> = ArrayDeque<IntBinaryTreeNode>().apply { add(root) }
        var latestLevelValuesSum = 0
        while (currentLevelNodesQueue.isNotEmpty()) {
            latestLevelValuesSum = 0
            repeat(currentLevelNodesQueue.size) {
                val currentNode = currentLevelNodesQueue.remove()
                latestLevelValuesSum += currentNode.value
                currentNode.left?.let(currentLevelNodesQueue::add)
                currentNode.right?.let(currentLevelNodesQueue::add)
            }
        }
        return latestLevelValuesSum
    }

    /**
     * Core idea and complexities same as [bfs] but compute the sum of the elements only at the actual last level.
     * How to determine what level is last? At the start of each iteration, recall the loop invariant, we have
     * all nodes of the current level in the currentLevelNodesQueue => so clone that deque into a variable at that time.
     * =>
     * On last iteration we will have empty currentLevelNodesQueue and previousLevelNodesQueue will have all nodes of the
     * last level.
     */
    fun bfsCleaner(root: IntBinaryTreeNode): Int {
        val currentLevelNodesQueue = ArrayDeque<IntBinaryTreeNode>().apply { add(root) }
        var previousLevelNodesQueue = ArrayDeque<IntBinaryTreeNode>()
        while (currentLevelNodesQueue.isNotEmpty()) {
            previousLevelNodesQueue = currentLevelNodesQueue.clone()
            repeat(currentLevelNodesQueue.size) {
                val currentNode = currentLevelNodesQueue.remove()
                currentNode.left?.let(currentLevelNodesQueue::add)
                currentNode.right?.let(currentLevelNodesQueue::add)
            }
        }

        var latestLevelValuesSum = 0
        while (previousLevelNodesQueue.isNotEmpty()) latestLevelValuesSum += previousLevelNodesQueue.remove().value
        return latestLevelValuesSum
    }

    // TODO: implement DFS later for funsies
}

fun main() {
    println(DeepestLeavesSum().bfsCleaner(IntBinaryTreeNode(1)))
}
