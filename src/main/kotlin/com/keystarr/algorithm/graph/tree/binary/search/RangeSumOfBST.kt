package com.keystarr.algorithm.graph.tree.binary.search

import com.keystarr.algorithm.graph.tree.binary.IntBinaryTreeNode

/**
 * LC-938 https://leetcode.com/problems/range-sum-of-bst/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= number of nodes <= 2 * 10^4;
 *  • 1 <= node.value <= 10^5;
 *  • 1 <= low <= high <= 10^5;
 *  • all node.value are unique.
 */
class RangeSumOfBST {

    /**
     * Goal - for a binary search subtree rooted at [root] calculate the sum of all values within range [low] and [high] (both inclusive).
     *
     * Use recursive DFS.
     *
     * base case:
     *  - if (root == null) return 0
     *
     * recursive case:
     *  - val leftSubtreeValidSum = if (root.value < low) 0 else dfs(root.left, low, high)
     *  - val rightSubtreeValidSum = if (root.value > high) 0 else dfs(root.right, low, high)
     *  - val currentValidSum = if (root.value >= low && root.value <= high) root.value else 0
     *  - return leftSubtreeSum + rightSubtreeSum + if (root.value >= low && root.value <= high) root.value else 0
     *
     * alternative version of recursive case (same idea):
     *  - if (root.value > high) return dfs(root.left, low, high)
     *  - if (root.value < low) return dfs(root.right, low, high)
     *  - return dfs(root.left, low, high) + dfs(root.right, low, high) + node.value
     *
     * optional (not for all cases) optimization - keep a count of nodes visited, once it exceeds high-low =>
     * hit the base case, return, stop the algorithm altogether.
     *
     * Edge cases:
     *  - low == high => we can sum only 1 node, works correctly, but with that optimization above would be faster if the
     *      node in question would not be at the opposite deepest end of the tree;
     *  - number of nodes == 1 => correct.
     *
     * Time: average/worst O(n) with just a better const than a simple DFS without making use of the BST's key properties.
     *  TODO: but why isn't at least average more like O(m*logn)?
     * Space: O(n) due to callstack.
     */
    fun dfs(root: IntBinaryTreeNode?, low: Int, high: Int): Int {
        if (root == null) return 0

        if (root.`val` > high) return dfs(root.left, low, high)
        if (root.`val` < low) return dfs(root.right, low, high)
        return dfs(root.left, low, high) + dfs(root.right, low, high) + root.`val`
    }
}
