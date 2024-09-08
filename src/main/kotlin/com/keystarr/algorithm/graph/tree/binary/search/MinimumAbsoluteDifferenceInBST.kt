package com.keystarr.algorithm.graph.tree.binary.search

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode
import kotlin.math.min

/**
 * LC-530 https://leetcode.com/problems/minimum-absolute-difference-in-bst/description/
 * difficulty: easy
 * constraints:
 *  • 2 <= number of nodes <= 10^4;
 *  • 0 <= node.value <= 10^5.
 *
 * Final notes:
 *  • FAILED to solve it in 1.5-2h. Found brute-force, but failed to come up with efficient recursive DFS with local variables
 *      only (didn't consider using public fields at all).
 *
 * Value gained:
 *  • if problem input is a BST => consider leveraging its DFS in-order traversing property;
 *  • if I can't find a solution using local vars recursive DFS => try public variables, class fields! That might be OK!
 *  • apparently adding nodes to an array is ok too, like the official solution goes in the leet DSA course. But it looks
 *      quite ugly though, like, is it really OK to solve tree/graph related problems by adding all nodes to an array
 *      first with correct order and then processing them? Doesn't feel that way.
 *      TODO: clarify that
 */
class MinimumAbsoluteDifferenceInBST {

    private var minDiff = Int.MAX_VALUE
    private var prev: Int? = null

    /**
     * Goal - find minimum absolute diff between values of two different nodes.
     *
     * Leverage BST's property that using in-order traversal we visit nodes in the sorted order.
     * Once we come back from root's left subtree;
     *  - update [prev] to [root];
     *  - compute minDiff based on [prev] and [root].value, upon which [prev] would either be:
     *      - if left subtree exists, the rightmost node in root's left subtree;
     *      - else root's parent (since we didn't update prev, cause we visited no nodes in between its parent and root)
     *
     * Discovered thanks to https://leetcode.com/problems/minimum-absolute-difference-in-bst/solutions/585431/java-accepted-solution-inorder-with-clear-explanation/
     */
    fun dfs(root: IntBinaryTreeNode?): Int {
        if (root == null) return Int.MAX_VALUE
        if (minDiff == 1) return minDiff // can't get any better

        dfs(root.left)

        if (prev != null) minDiff = min(root.`val` - prev!!, minDiff)
        prev = root.`val`

        dfs(root.right)

        return minDiff
    }

    // TODO: write DFS using local variables only. It must work! Couldn't do it in 1h though
}
