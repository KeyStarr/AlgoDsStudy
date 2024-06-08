package com.keystarr.algorithm.graph.tree.binary

/**
 * LC-112 https://leetcode.com/problems/path-sum/description/
 * difficulty: easy
 * constraints:
 *  • 0 <= number of nodes <= 5000;
 *  • -10^3 <= node.value <= 10^3;
 *  • -10^3 <= targetSum <= 10^3;
 *  • no explicit time/space.
 */
class PathSum {

    /**
     * problem rephrase:
     * "return true if there is a branch such that the sum of values of all it's nodes equals to targetSum"
     *
     * find THE FIRST valid branch + binary tree => DFS?
     *
     * idea:
     *  - pre-order DFS, pass sum as the variable currentSum to each function call and compute it pre-traversal with current node;
     *  - at each leaf currentSum = it's branch sum, and we can compare it to target sum, if so, return true;
     *  - if left subtree exists, check it, if it returns true, straight backtrack and return true out of algo;
     *  - if right exists, check it, if it returns true, backtrack out of the algo;
     *  - base case = leaf, return currentSum+node.value == targetSum.
     *
     * Edge cases:
     *  - number of nodes == 0 => return false, correct.
     *
     * Time: average/worst O(n)
     * Space: average/worst O(n)
     *
     */
    fun efficient(root: IntBinaryTreeNode?, targetSum: Int) =
        if (root == null) false else internalRecursive(root, targetSum, 0)

    private fun internalRecursive(node: IntBinaryTreeNode, targetSum: Int, previousSum: Int): Boolean {
        val currentSum = previousSum + node.value
        if (node.left == null && node.right == null) return currentSum == targetSum // leaf, base case

        if (node.left != null) {
            val isMatch = internalRecursive(node.left!!, targetSum, currentSum)
            if (isMatch) return true
        }
        if (node.right != null) {
            val isMatch = internalRecursive(node.right!!, targetSum, currentSum)
            return isMatch
        }

        // left != null but no a match + right == null OR left == null and right != null but not a match
        return false
    }

    /**
     * Same core idea and complexities as [efficient] but:
     *  - use only 1 variable for sum instead of passing both target and currentSum.
     *      Intuition - if pass currentSum=targetSum to root, and then at each node we subtract node's value from currentSum,
     *       then at the leaves, if current branch's sum equals targetSum, currentSum would be 0;
     *  - don't use local variables to check for whether the left or the right subtrees lead to the target branch.
     */
    fun efficientCleaner(root: IntBinaryTreeNode?, targetSum: Int): Boolean =
        efficientCleanerRecursive(node = root, remainderAfterParent = targetSum)

    private fun efficientCleanerRecursive(node: IntBinaryTreeNode?, remainderAfterParent: Int): Boolean =
        when {
            node == null -> false // base case #1, either root or the non-existing node after a leaf
            node.left == null && node.right == null -> remainderAfterParent - node.value == 0 // base case #2, leaf
            else -> { // recursive case
                val currentRemainder = remainderAfterParent - node.value
                efficientCleanerRecursive(node.left, currentRemainder) || efficientCleanerRecursive(node.right, currentRemainder)
        }
    }
}

fun main() {
    println(
        PathSum().efficient(
            root = IntBinaryTreeNode(
                value = 1,
                left = IntBinaryTreeNode(value = 2)
            ),
            targetSum = 1,
        )
    )
}
