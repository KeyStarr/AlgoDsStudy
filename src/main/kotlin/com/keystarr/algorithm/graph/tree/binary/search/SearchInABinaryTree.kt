package com.keystarr.algorithm.graph.tree.binary.search

import com.keystarr.algorithm.graph.tree.binary.IntBinaryTreeNode

/**
 * LC-700 https://leetcode.com/problems/search-in-a-binary-search-tree/description/
 * difficulty: easy
 * Final notes:
 *  • done [dfs] by myself in 5 mins.
 *
 * Value gained:
 *  • practiced solving a binary tree search problem using DFS.
 */
class SearchInABinaryTree {

    /**
     * target time - O(logn)
     *
     * Time: average/worst O(logn)
     *  worst is logn iterations if the node doesn't exist
     * Space: average/worst O(n)
     *  worst is the tree is in form of a line
     */
    fun dfs(root: IntBinaryTreeNode?, targetValue: Int): IntBinaryTreeNode? {
        if (root == null) return null
        if (root.`val` == targetValue) return root

        return if (targetValue > root.`val`) dfs(root.right, targetValue) else dfs(root.left, targetValue)
    }
}
