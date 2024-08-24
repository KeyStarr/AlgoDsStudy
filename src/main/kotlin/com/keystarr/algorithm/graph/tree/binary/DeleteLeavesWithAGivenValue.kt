package com.keystarr.algorithm.graph.tree.binary

/**
 * LC-1325 https://leetcode.com/problems/delete-leaves-with-a-given-value/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= number of nodes <= 3000;
 *  • 1 <= node.value, target <= 1000.
 *
 * Final notes:
 *  • 🏆 done [efficient] by myself in 15 mins, 1st time submit:
 *   • caught quickly the CORE observation: we must consider the node for removal only after both its left and right
 *    subtrees have been processed, cause node itself after these might become a leaf => post-order dfs.
 *   • caught and both the only edge case, for that we might delete the root as well if it becomes a leaf, but we need
 *    to handle that in a special way.
 *
 * Value gained:
 *  • practiced solving a binary tree problem efficiently using in-order/post-order dfs;
 *  • practiced dry-running a tree question in the text editor (its actually quite nice. Thanks DSA course author for
 *   visual examples of how that's done! its an important skill coz in real remote interview scenario there probably
 *   would be nothing to draw on). And I need to practice that specifically, and on different types of inputs/algos 💡
 */
class DeleteLeavesWithAGivenValue {

    /**
     * post-order DFS? post-order for removal condition to trigger after all updates to both left and right subtrees.
     * delete nodes starting from leaves. delete a node if its a leaf and its value equals target recursively.
     *
     *           target = 2
     *              1
     *            2   2
     *           2 3 2 2
     *
     *           result:
     *              1
     *            2
     *             3
     *
     * edge cases:
     *  - all nodes in the tree must be deleted (including root) => after the call to root check whether root must be deleted,
     *   if so, return null.
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     *  worst case is tree is a line => [dfs] callstack size is exactly n
     */
    fun efficient(root: IntBinaryTreeNode, target: Int): IntBinaryTreeNode? {
        dfs(root, target)
        return if (root.isLeaf() && root.`val` == target) null else root
    }

    /**
     * in-order/post-order
     */
    private fun dfs(root: IntBinaryTreeNode, target: Int) {
        root.left?.let { left ->
            dfs(left, target)
            if (left.isLeaf() && left.`val` == target) root.left = null
        }
        root.right?.let { right ->
            dfs(right, target)
            if (right.isLeaf() && right.`val` == target) root.right = null
        }
    }

    private fun IntBinaryTreeNode.isLeaf() = left == null && right == null
}
