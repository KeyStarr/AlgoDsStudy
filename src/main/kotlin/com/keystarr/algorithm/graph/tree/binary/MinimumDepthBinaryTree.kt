package com.keystarr.algorithm.graph.tree.binary

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode
import kotlin.math.min

/**
 * LC-111 https://leetcode.com/problems/minimum-depth-of-binary-tree/description/
 * difficulty: easy
 * constraints:
 *  • 0 <= number of nodes <= 10^5;
 *  • -10^3 <= node.value <= 10^3;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • failed 1st submission, got [recursive] accepted with the 2nd. The problem was that I've missed the edge case
 *   that if current subtree's [root] has only one child, then we must not consider the path that ends with [root]
 *   (as I did, since I used returned 0 as a valid depth). Reason - branch in BT always ends with a leaf, and [root]
 *   in such case is not a leaf since it has 1 child.
 *
 * Value gained:
 *  • practiced solving binary tree with DFS:
 *  • fell into a potentially common pitfall of considering paths for branches when [root] isn't a leaf, ending with [root]!
 *      and devised a solution.
 */
class MinimumDepthBinaryTree {

    /**
     * Goal - return the minimum branch length.
     *
     * Best Depth => DFS.
     * Idea - count the depth starting from the leaves, at each call get both the distance from left and right towards the
     * closest leaf and return the minimum of both.
     *
     * - base case:
     *  - root == null => return 0 // current subtree doesn't exist
     *  - root.left == null && root.right == null => return 1 // a subtree with a single node (a leaf of the original tree)
     * - recursive case:
     *  - leftDepth = recursive(root.left)
     *  - rightDepth = recursive(root.right)
     *  - return min(leftDepth, rightDepth) + 1
     *
     * edge cases:
     *  - original [root] == null => return 0, correct;
     *  - if one [root] children is null (for any subtree) and the other one isn't, then return minDepth of an existing
     *   child + 1. Cause [root] then is NOT A LEAF and therefore there is no branch that ends with it.
     *
     * Time: always O(n), we always visit each node once;
     * Space: O(n) due to callstack (actually, at most the height of original tree)
     */
    fun recursive(root: IntBinaryTreeNode?): Int {
        if (root == null) return 0
        if (root.left == null && root.right == null) return 1

        val leftSubtreeMinDepth = recursive(root.left)
        val rightSubtreeMinDepth = recursive(root.right)
        return when {
            leftSubtreeMinDepth != 0 && rightSubtreeMinDepth != 0 ->  min(leftSubtreeMinDepth, rightSubtreeMinDepth)
            leftSubtreeMinDepth != 0 -> leftSubtreeMinDepth
            else -> rightSubtreeMinDepth
        } + 1
    }
}
