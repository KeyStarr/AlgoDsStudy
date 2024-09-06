package com.keystarr.algorithm.graph.tree.binary.search

import com.keystarr.algorithm.graph.tree.binary.IntBinaryTreeNode

/**
 * LC-235 https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= number of nodes <= 10^5;
 *  • -10^9 <= node.val <= 10^9;
 *  • all node values are unique;
 *  • p != q;
 *  • both p and q always exist in the tree.
 *
 * Final notes:
 *  • done [efficient] by myself in 35 mins;
 *  • I correctly narrowed the problem down to:
 *   • make use of the BST nodes by values organization core property;
 *   • come up with a ruleset to do a custom binary search in O(log(nodes)) time.
 *  • tested different hypothesis as to what the rule may be - when to go left/right, but started first with trying to reason
 *   when is it that we have found the node. Failed for a while, only after 20 mins found the correct rule, basically it just
 *   appeared in my head after much experimentation => diffused thinking at work 🔥!
 *
 * Value gained:
 *  • practiced solving a BST property calculation type problem using a customized binary search based on the BST core property.
 */
class LowestCommonAncestorOfABinarySearchTree {

    /**
     * why is it important that we are given exactly a binary SEARCH tree?
     *
     * hm, we make use of the BST's key order property and try to find the target node in O(log(nodes)) time!
     *
     * what are the rules for search - when to go left, when to go right, when have we found it?
     *  - the target node:
     *   - always has to be (but not enough):
     *      - in case the LCA isn't p or q:
     *       - target > min(p,q)
     *       - target < max(p,q)
     *       (strictly, cause by problem statement all values in the tree are unique)
     *      - in case it is => either condition is equals.
     *     - LCA must be on the level of depth no lower than the lowest of p and q;
     *     -
     *
     * basically we have two core cases:
     *  1. either p or q is LCA;
     *   -
     *
     *  2. LCA is some other node.
     *   -
     *
     * ---------------------
     *
     * approach #2:
     *  - find both p and q;
     *  - backtrack from both simultaneously until we find a node that is greater than min(p,q) and less than max(p,q).
     *
     * ---------------------
     *
     * approach #3 - custom binary search:
     *  - traverse the tree from root;
     *  - at each node, if:
     *   - root.val < p && root.val < q => go right;
     *   - root.val > p && root.val > q => go left;
     *   - root.val > p && root.val < q => return root (2nd major case, LCA is neither p nor q)
     *   - root.val == p || root.val == q => return root (1st major case, LCA is either p or q).
     *
     * Time: average/worst O(nodes)
     * Space: average/worst O(nodes)
     */
    fun efficient(root: IntBinaryTreeNode, p: IntBinaryTreeNode, q: IntBinaryTreeNode): IntBinaryTreeNode {
        var currentNode: IntBinaryTreeNode? = root
        while (currentNode != null) {
            val value = currentNode.`val`
            currentNode = when {
                value < p.`val` && value < q.`val` -> currentNode.right
                value > p.`val` && value > q.`val` -> currentNode.left
                else -> return currentNode // either (value == p.`val` || value == q.`val`) or (value > minValue && value < maxValue)
            }
        }
        throw IllegalStateException("Answer is guaranteed to exist")
    }
}
