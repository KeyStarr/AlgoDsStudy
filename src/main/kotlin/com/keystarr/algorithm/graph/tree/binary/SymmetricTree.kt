package com.keystarr.algorithm.graph.tree.binary

import com.keystarr.datastructure.graph.tree.TreeNode

/**
 * LC-101 https://leetcode.com/problems/symmetric-tree/description/
 * difficulty: easy
 * Final notes:
 *  • done [dfsRecursive] by myself in 10 mins;
 *  • very reminiscent of [InvertBinaryTree], since the solution here is too based on a single core observation and is
 *   very succinct, involves swapping left/right subtrees during traversal. And basically if two trees are equal, then
 *   one is the inverted version (per definition of [InvertBinaryTree]) of the other.
 *
 * Value gained:
 *  • practiced solving a two trees comparison type question using DFS based on a single core observation, in one pass.
 */
class SymmetricTree {

    /**
     *                      1
     *                2          2
     *              3  4        4  3
     *             5 6 7 8     8 7 6 5
     *
     * one approach, 2 pass O(n) time O(h) space but with input modification (or O(n) space with new tree creation:
     *  - invert one of the subtrees;
     *  - compare both subtrees trivially for equality.
     *
     * another approach:
     *  - traverse both trees DFS with 2 pointers making a move at the same time, in one tree move left-to-right, in
     *   another right-to-left. If as some points node values are not equal => return false.
     *
     * Time: average/worst O(min(n,m))
     * Space: always O(min(h1,h2)) = O(min(n,m))
     */
    fun dfsRecursive(root: TreeNode): Boolean = dfsRecursive(root1 = root.left, root2 = root.right)

    private fun dfsRecursive(root1: TreeNode?, root2: TreeNode?): Boolean {
        if (root1?.`val` != root2?.`val`) return false
        if (root1 == null || root2 == null) return true

        if (!dfsRecursive(root1 = root1.left, root2 = root2.right) || !dfsRecursive(root1 = root1.right, root2 = root2.left)) return false
        return true
    }
}
