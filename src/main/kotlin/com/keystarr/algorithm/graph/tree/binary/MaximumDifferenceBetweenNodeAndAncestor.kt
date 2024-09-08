package com.keystarr.algorithm.graph.tree.binary

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode
import kotlin.math.max
import kotlin.math.min

/**
 * LC-1026 https://leetcode.com/problems/maximum-difference-between-node-and-ancestor/editorial/
 * difficulty: medium
 * constraints:
 *  • 2 <= number of nodes <= 5000;
 *  • 0 <= node.value <= 10^5;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • implemented [preOrderRecursive] by myself, submitted 1st attempt;
 *  • tried doing [postOrderRecursive], but couldn't figure out how. Check solutions, haven't found such in editorial/a few
 *      top community discussions;
 *
 * Value gained:
 *  • I do understand why [postOrderRecursive] fails with a specific category of input data, but can't formally model it.
 *   TODO: revisit later, prove why [postOrderRecursive] is incorrect, really understand!
 *  • => apparently, sometimes a specific X-order solution might be better than others/
 *  • => settling for additional temp variable in recursive solutions of trees/graphs is OK? seems so. editorial
 *      goes like this, and, ahem, gpt chipped in to confirm this.
 */
class MaximumDifferenceBetweenNodeAndAncestor {

    /**
     * a valid answer always exists since we have at least 2 nodes.
     *
     * sub-problems
     *  - keep track of min and max values in the current branch;
     *  - as we reach a leaf, compute the diff and return it (then it is the max V in the current branch);
     *
     * DFS
     *
     * a recursive function which works with the current subtree [root]:
     *  - base case:
     *      - if (root == null) return -1 // a non-existent subtree (original tree's node after a leaf)
     *  - recursive case:
     *      // conquer current
     *      - newMax = max(root.value, prevMax)
     *      - newMin = min(root.value, prevMin)
     *      - currentDiff = newMax - newMin
     *      // divide & conquer left
     *      - leftSubtreeMaxDiff = maxAncestorDiffInternal(root.left, newMin, newMax)
     *      // divide & conquer right
     *      - rightSubtreeMaxDiff = maxAncestorDiffInternal(root.right, newMin, newMax)
     *      // combine
     *      - return max(leftSubtreeMaxV, rightSubtreeMaxV, currentDiff)
     *
     * variation (double code for diff computation, BUT don't compute currentDiff before we reach the leaf):
     *  - remove currentDiff
     *  - add the 2nd base case:
     *      - if (root.left == null && root.right == null) return max(prevMax, root.value) - min(prevMax.root.value)
     *
     * edge cases:
     *  - [root] has only 1 child (tree of 2 nodes) => don't go down that path, handled via null check for both;
     *  - max/min initialization => we have at least 2 nodes, so both will always be overwritten (when set to Int.max/min);
     *
     * Time: always O(n), we have to visit each node once;
     * Space: O(n) due to callstack.
     */
    fun preOrderRecursive(root: IntBinaryTreeNode?): Int = preOrderRecursiveInternal(
        root = root!!,
        prevMin = Int.MAX_VALUE,
        prevMax = Int.MIN_VALUE,
    )

    /**
     * Goal - find the maximum ancestor difference between the current subtree and the difference ([prevMax] - [prevMin]).
     */
    private fun preOrderRecursiveInternal(root: IntBinaryTreeNode?, prevMin: Int, prevMax: Int): Int {
        if (root == null) return prevMax - prevMin  // base case #1

        val newMax = max(prevMax, root.`val`)
        val newMin = min(prevMin, root.`val`)
        val leftSubtreeMaxDiff = preOrderRecursiveInternal(root.left, newMin, newMax)
        val rightSubtreeMaxDiff = preOrderRecursiveInternal(root.right, newMin, newMax)
        return max(leftSubtreeMaxDiff, rightSubtreeMaxDiff)
    }


    // TODO: why does that work? ChatGPT 4o made it when I explained the requirements. Why does [postOrderRecursive] fail
    //  and this works?
    fun maxAncestorDiff(root: IntBinaryTreeNode?): Int {
        if (root == null) return 0
        return postOrderRecursiveInternal(root).maxDiff
    }

    private fun postOrderRecursiveInternal(root: IntBinaryTreeNode?): Result {
        if (root == null) return Result(Int.MAX_VALUE, Int.MIN_VALUE, 0)

        val leftResult = postOrderRecursiveInternal(root.left)
        val rightResult = postOrderRecursiveInternal(root.right)

        val currentMin = min(root.`val`, min(leftResult.minVal, rightResult.minVal))
        val currentMax = max(root.`val`, max(leftResult.maxVal, rightResult.maxVal))

        val currentDiff = max(
            root.`val` - currentMin,
            currentMax - root.`val`
        )

        val maxDiff = max(currentDiff, max(leftResult.maxDiff, rightResult.maxDiff))

        return Result(currentMin, currentMax, maxDiff)
    }

    data class Result(val minVal: Int, val maxVal: Int, val maxDiff: Int)


    // submit fail
    fun postOrderRecursive(root: IntBinaryTreeNode?): Int {
        val (min, max) = findMaxAndMin(root)
        return max - min
    }

    /**
     * Goal - find both maximum and minimum node values in the tree rooted at [root].
     * Idea:
     *  - use DFS;
     *  - go to the bottom of a branch (down until a leaf);
     *  - start computing max/min and return them. Basically we perform computation on the current node only as we backtrack.
     * => at root we will compute and return the max/min of the subtree being the entire tree.
     *
     * Edge cases:
     *  - root == null => -1, correct (here will never happen);
     *  - number of nodes == 1 => its value both times, correct (here will never happen).
     *
     * Time: always O(n) cause we visit each node once;
     * Space: always O(n), to be precise, the height of the tree.
     *
     * But for each subtree (a single call) we compute max and min for time O(1) space O(1).
     */
    private fun findMaxAndMin(root: IntBinaryTreeNode?): Pair<Int, Int> {
        if (root == null) return -1 to -1
        if (root.left == null && root.right == null) return 0 to root.`val`

        val leftMinMax = findMaxAndMin(root.left)
        val rightMinMax = findMaxAndMin(root.right)
        val leftDiff = leftMinMax.diff()
        val rightDiff = rightMinMax.diff()
        val bestMinMax = if (max(leftDiff, rightDiff) == leftDiff) leftMinMax else rightMinMax

        return min(bestMinMax.first, root.`val`) to max(bestMinMax.second, root.`val`)
    }

    private fun Pair<Int, Int>.diff() = second - first
}
