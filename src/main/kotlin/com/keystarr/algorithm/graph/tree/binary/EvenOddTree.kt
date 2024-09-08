package com.keystarr.algorithm.graph.tree.binary

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode
import java.util.*

/**
 * LC-1609 https://leetcode.com/problems/even-odd-tree/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= number of nodes <= 10^5
 *  • 1 <= node.value <= 10^6
 *
 * Final notes:
 *  • 🎉 done [bfs] by myself in 12 mins, 1st time submit;
 *  • classified the tools needed for an efficient solution in a few minutes, cause the problem is straight forward
 *   and the statement gives it away: level-focus traversal => BFS with implementation of special conditions.
 *
 * Value gained:
 *  • practiced BFS on a binary tree type problem with special conditions implementation.
 */
class EvenOddTree {

    /**
     * given: binary tree
     * - level-focused problem => do DFS
     * - keep track of the current level via counter, verify all nodes based on the conditions given based on the current level
     * - if any condition is violated upon checking => early return false
     * - at the end of the traversal, if havent returned in action => return true
     *
     * Time: average/worst O(nodes+edges)
     * Space: O(2^k)
     *  - worst queue size depends on the max level size => 2^k, where k=depth of the tree
     */
    fun bfs(root: IntBinaryTreeNode): Boolean {
        val queue: Queue<IntBinaryTreeNode> = ArrayDeque()
        queue.add(root)
        var isLevelEven = true
        while (queue.isNotEmpty()) {
            val levelNodesSize = queue.size
            var prevValue = if (isLevelEven) 0 else Int.MAX_VALUE
            repeat(levelNodesSize) {
                val node = queue.remove()
                if (isLevelEven) {
                    if (node.`val` % 2 != 1 || prevValue >= node.`val`) return false
                } else {
                    if (node.`val` % 2 != 0 || prevValue <= node.`val`) return false
                }
                node.left?.let(queue::add)
                node.right?.let(queue::add)
                prevValue = node.`val`
            }
            isLevelEven = !isLevelEven
        }
        return true
    }
}
