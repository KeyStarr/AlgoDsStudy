package com.keystarr.algorithm.graph.tree.binary.search

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode

/**
 * LC-94 https://leetcode.com/problems/binary-tree-inorder-traversal/description/
 * difficulty: easy
 * constraints:
 *  • 0 <= number of nodes <= 100;
 *  • -100 <= node.value <= 100.
 *
 * Value gained:
 *  • recalled [recursive] with dry running on the side, and practiced an iterative in-order binary tree dfs [iterative],
 *   never done the iterative one in that form. Needed it for [com.keystarr.algorithm.AllElementsInTwoBinarySearchTrees],
 *   as a component to a clean efficient solution, so decided to build that component here isolated first.
 */
class BinaryTreeInorderTraversal {

    /**
     * plain inorder dfs
     */
    fun recursive(root: IntBinaryTreeNode?): List<Int> = mutableListOf<Int>().apply { dfs(root, this) }

    private fun dfs(
        root: IntBinaryTreeNode?,
        values: MutableList<Int>,
    ) {
        if (root == null) return

        dfs(root.left, values)
        values.add(root.`val`)
        dfs(root.right, values)
    }

    fun iterative(root: IntBinaryTreeNode?): List<Int> {
        if (root == null) return emptyList()

        val stack = ArrayDeque<IntBinaryTreeNode>()
        var currentRoot = root
        val values = mutableListOf<Int>()
        while (currentRoot != null || stack.isNotEmpty()) {
            while (currentRoot != null) {
                stack.addLast(currentRoot)
                currentRoot = currentRoot.left
            }
            currentRoot = stack.removeLast()
            values.add(currentRoot.`val`)
            currentRoot = currentRoot.right
        }
        return values
    }
}
