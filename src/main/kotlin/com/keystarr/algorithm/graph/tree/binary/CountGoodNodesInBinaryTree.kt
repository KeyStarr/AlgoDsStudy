package com.keystarr.algorithm.graph.tree.binary

import kotlin.math.max

/**
 * LC-1448 https://leetcode.com/problems/count-good-nodes-in-binary-tree/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= number of nodes <= 10^5;
 *  • -10^4 <= node.value <= 10^4;
 *  • no explicit time/space.
 *
 * Value gained:
 *  • apparently for tree/graph problems it might be useful to make some vars top level like [goodNodesCount] here;
 *  • practiced pre-order & post-order DFS;
 *  • encountered a mix (?) of pre-order and post-order actually [efficientCleaner];
 *  • hypothesis - it's cleaner to rarely/never use fields outside the function for recursion? In case of trees we
 *      just need to figure out how to return the intermediate/final straight away answer from the function?
 */
class CountGoodNodesInBinaryTree {

    // since we visit the root always, just make sure it's always counted as +1
    private var goodNodesCount = 0

    /**
     * observations:
     *  - root itself is always a good node, and number of nodes > 0, so init the count = 1;
     *
     * Idea:
     *  - use DFS;
     *  - keep track of the maximum node's value in the current branch (passed via function parameter);
     *  - if current node's value is greater than it => increment the total good nodes count (can be kept as a class field).
     *
     * Edge cases:
     *  - "no node greater" => we count if less OR EQUAL;
     *  - number of nodes == 1 => always return 1, correct.
     *
     * Tools: pre-order DFS + binary tree
     *
     * Time: always O(n) cause we have to visit each node always;
     * Space: always O(n) because of the callstack.
     */
    fun efficient(root: IntBinaryTreeNode): Int {
        preOrderRecursive(node = root, currentMaxValue = Int.MIN_VALUE)
        return goodNodesCount
    }

    private fun preOrderRecursive(node: IntBinaryTreeNode?, currentMaxValue: Int) {
        if (node == null) return // a non-existing node after the leaf

        var newMax = currentMaxValue
        if (node.`val` >= currentMaxValue) {
            goodNodesCount++
            newMax = node.`val`
        }

        preOrderRecursive(node.left, newMax)
        preOrderRecursive(node.right, newMax)
    }

    /**
     * Same core idea complexities as [efficient].
     * Intuition - [postOrderRecursive] now returns good nodes count for the tree rooted at `node`.
     *  (a subtree of a tree with root=[root]). We don't need the class field [goodNodesCount] then.
     *
     * Actually, it's like a mix of preorder (find new max val at current node so far) AND preorder (add +1 for current node
     * once we've visited both children) DFS.
     */
    fun efficientCleaner(root: IntBinaryTreeNode): Int = postOrderRecursive(root, Int.MIN_VALUE)

    private fun postOrderRecursive(node: IntBinaryTreeNode?, currentMax: Int): Int {
        if (node == null) return 0 // a non-existing node after a leaf

        val newMax = max(node.`val`, currentMax)
        val goodNodesLeft = postOrderRecursive(node.left, newMax)
        val goodNodesRight = postOrderRecursive(node.right, newMax)

        var goodNodesInCurrentSubtree = goodNodesLeft + goodNodesRight
        if (node.`val` >= currentMax) goodNodesInCurrentSubtree += 1

        return goodNodesInCurrentSubtree
    }
}
