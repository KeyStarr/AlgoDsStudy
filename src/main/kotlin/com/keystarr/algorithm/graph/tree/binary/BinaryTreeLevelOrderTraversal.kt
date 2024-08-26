package com.keystarr.algorithm.graph.tree.binary

import java.util.*

/**
 * LC-102 https://leetcode.com/problems/binary-tree-level-order-traversal/description/
 * difficulty: medium
 * constraints:
 *  • 0 <= number of nodes <= 2000;
 *  • -1000 <= node.val <= 1000.
 *
 * Final notes:
 *  • 🏅 done [bfs] by myself in 6 mins;
 *
 * Value gained:
 *  • practiced solving a binary tree problem efficiently using BFS.
 */
class BinaryTreeLevelOrderTraversal {

    /**
     * Level order traversal (left to right) => do BFS.
     *
     * If we had to do DFS, which wouldn't make sense in real world, but still, if we had to do DFS => we could do pre-order,
     *  pass the level of depth in the function params and also pass param levels list of lists => if we wouldn't have
     *  a list at the ind for the current level => we'd create it, otherwise we'd get it and add there both left and right children,
     *  then call dfs on left and right. And we'd have to add root beforehand to results as the first list.
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     */
    fun bfs(root: IntBinaryTreeNode?): List<List<Int>> {
        val queue: Queue<IntBinaryTreeNode> = ArrayDeque()
        root?.let(queue::add)
        val levels = mutableListOf<List<Int>>()
        while (queue.isNotEmpty()) {
            val levelSize = queue.size
            val levelNodes = mutableListOf<Int>()
            repeat(levelSize) {
                val node = queue.remove()
                levelNodes.add(node.`val`)
                node.left?.let(queue::add)
                node.right?.let(queue::add)
            }
            levels.add(levelNodes)
        }
        return levels
    }
}
