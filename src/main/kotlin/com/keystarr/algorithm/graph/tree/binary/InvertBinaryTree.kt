package com.keystarr.algorithm.graph.tree.binary

import com.keystarr.datastructure.graph.tree.TreeNode
import java.util.*

/**
 * LC-226 https://leetcode.com/problems/invert-binary-tree/description/
 * difficulty: easy
 * constraints:
 *  • 0 <= number of node <= 100;
 *  • -100 <= node.val <= 100.
 *
 * Final notes:
 *  • done [dfs] by myself in 5 mins;
 *  • thought for a sec that [bfs] here would follow somehow a different logic based on the same levels = same nodes but inverted
 *   observation, but no, basically the easiest way to go about it was swapping left/right just like [dfs].
 *
 * Value gained:
 *  • practiced solving a binary tree modification type question efficiently using both bfs and dfs.
 */
class InvertBinaryTree {

    /**
     * inverted BT here = mirrored, right-to-left, all nodes remain on the same level.
     * initial idea: DFS - for each subtree, swap left and right nodes.
     *
     * Time: always O(nodes)
     * Space: always O(height) = average/worst O(n)
     */
    fun dfs(root: TreeNode?): TreeNode? {
        if (root == null) return null

        val temp = root.left
        root.left = root.right
        root.right = temp

        dfs(root.left)
        dfs(root.right)

        return root
    }

    /**
     * observe that all nodes in the inverted tree remain on the same levels, just that order is inverted.
     * but basically we could do the same as logic as [dfs].
     *
     * Time: always O(nodes)
     * Space: always O(2^height)
     */
    fun bfs(root: TreeNode?): TreeNode? {
        if (root == null) return null

        val queue: Queue<TreeNode> = ArrayDeque()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.remove()

            val temp = node.left
            node.left = node.right
            node.right = temp

            node.left?.let(queue::add)
            node.right?.let(queue::add)
        }
        return root
    }
}
