package com.keystarr.algorithm.graph.tree.binary.search

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode
import kotlin.math.abs

/**
 * LC-270 https://leetcode.com/problems/closest-binary-search-tree-value/description/
 * difficulty: easy (fr?)
 * constraints:
 *  • 1 <= number of nodes <= 10^4;
 *  • 0 <= node.value  <= 10^9;
 *  • -10^9 <= target <= 10^9.
 *
 * Final notes:
 *  • FAILED 2 edge cases BOTH on two candidate values with equal distance to target. Like, I FOUND in advance that this is
 *   an edge case and though I covered it, but I didn't. Same happened in [InsertIntoABinarySearchTree]!!
 *  • ~30-40 mins for solution.
 *
 * Value gained:
 *  • dry run edge cases, bro. At least always for BSTs. Evidently you ain't good enough yet to do that in that head of yours!
 *  • post-order DFS leveraging the BST definition worked fine here;
 *  • practiced BST. I still struggle with BST problems, though I did learn a lot from all problems I've failed at so far.
 *      BSTs might need more practice!
 */
class ClosestBinarySearchTreeValue {

    /**
     * - interesting, nodes are >0 but the target can be negative.
     * - >1 nodes => a valid answer always exists.
     *
     * Idea:
     *  - traverse the tree using recursive DFS in the form of binary search;
     *  - once we reach the node after a leaf, backtrack and on the way back (post-order) compute the diff between the
     *      current root and target. If currentDiff is the smallest, backtrack it to the previous call etc;
     *  => the first call will return the node.value with the smallest diff to target (it will guaranteed lie be one of
     *  the nodes we visit during the traversal)
     *
     * We have to traverse down to the leaves, can't make early decisions.
     *
     * Edge cases:
     *  - target equals to some node's value => diff will be 0, we will catch that;
     *  - 2 values have same absolute distance to target => we will find the smallest, cause we try each direct greater and smaller;
     *  - target is greater than all values in the tree => handled;
     *  - target is smaller than all values in the tree => handled.
     *
     * Time: average O(logn) worst O(n)
     * Space: average O(logn) worst O(n)
     * both worst when the tree is a "line"
     *
     * --------
     *
     * Optimization room - how to compute only the 2 nodes that are both directly less and greater than target,
     *  don't compute for any other?
     */
    fun solution(root: IntBinaryTreeNode?, target: Double): Int = recursiveInternal(root, target)!!.first

    private fun recursiveInternal(root: IntBinaryTreeNode?, target: Double): Pair<Int, Double>? {
        if (root == null) return null

        val closestValueToDiff = if (target > root.`val`) {
            recursiveInternal(root.right, target)
        } else {
            recursiveInternal(root.left, target)
        }

        val currentDiff = abs(target - root.`val`)
        return if (closestValueToDiff == null
            || currentDiff < closestValueToDiff.second
            || (currentDiff == closestValueToDiff.second && root.`val` < closestValueToDiff.first)
        ) root.`val` to currentDiff else closestValueToDiff
    }

    /**
     * Attempted to untangle that [recursiveInternal] conditions mess, but it ain't much better.
     * Same core idea and complexities.
     */
    fun solutionCleaner(root: IntBinaryTreeNode?, target: Double): Int = recursiveCleanerInternal(root!!, target).first

    private fun recursiveCleanerInternal(root: IntBinaryTreeNode, target: Double): Pair<Int, Double> {
        val nextNode = if (target > root.`val`) root.right else root.left
        val currentDiff = abs(target - root.`val`)
        if (nextNode == null) return root.`val` to currentDiff

        val closestValueToDiff = recursiveCleanerInternal(nextNode, target)

        return if (currentDiff < closestValueToDiff.second
            || (currentDiff == closestValueToDiff.second && root.`val` < closestValueToDiff.first)
        ) root.`val` to currentDiff else closestValueToDiff
    }

    // TODO: how to compute only 1 or 2 closest nodes and not compute the diff of any other with least overhead?
}

fun main() {
    println(
        ClosestBinarySearchTreeValue().solution(
            root = IntBinaryTreeNode(
                `val` = 4,
                left = IntBinaryTreeNode(
                    `val` = 2,
                    left = IntBinaryTreeNode(1),
                    right = IntBinaryTreeNode(3),
                ),
                right = IntBinaryTreeNode(
                    `val` = 5
                )
            ),
            target = 4.5,
        )
    )
}
