package com.keystarr.algorithm.graph.tree.binary.binarysearchtree

import com.keystarr.algorithm.graph.tree.binary.IntBinaryTreeNode

/**
 * LC-98 https://leetcode.com/problems/validate-binary-search-tree/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= number of nodes <= 10^4;
 *  • -2^31 <= node.value <= 2^31-1.
 *
 * Final notes:
 *  • funny, leveraged the same property as in the [MinimumAbsoluteDifferenceInBST];
 *  • solved efficiently by [isValidBstViaSortedProp] 1st submit in about 15-20 mins;
 *  • failed [isValidBstViaDefinition] on case where there were Int.MIN and Int.MAX in the node values!!!
 *
 * Value gained:
 *  • confirmed that, indeed, if a tree can be traversed in-order and all values would appear in the sorted order, that is indeed BST,
 *   so BST has this property and if a tree has this property it is a BST; TODO: prove it formally! why is that so exactly?
 *  • practiced BST in-order and pre-order sorted traversal;
 *  • funny how both the definition AND the key property of the BST can both be used to validate BST = prove that the tree is BST,
 *      one follows directly from another then, both ways (if for all subtrees in the tree the following is true:
 *      root.value is greater than all nodes values in the left subtree, and less than all node values in the right subtree;
 *      then the tree is a BST and therefore can be traversed in sorted order via DFS in-order. And vice versa);
 *  • I had a strong intuition that -2^31 and 2^31-1 ARE indeed int.min and int.max (haven't touched these in a while)
 *      but didn't check it and failed with [isValidBstViaDefinition]! I guess I got confused by the power of 2's representation,
 *      => check extremes in any form in the future, it's easy if you just sit down and focus on it (4 bytes lol).
 */
class ValidateBinarySearchTree {

    private var prevValue: Int? = null

    /**
     * Goal - return true if the subtree rooted at [root] is a valid Binary Search Tree.
     *
     * Hint: BST => leverage in-order sorted traversal.
     *
     * Idea - do in-order DFS:
     * - base case:
     *  - root == null => return true
     *
     * - recursive case:
     *  - val isLeftValid = dfs(root.left)
     *  - if (!isLeftValid) return false
     *  - if root.value <= prev:
     *      - return false, don't even check the right node;
     *  - prev = root.value
     *  - return dfs(root.right)
     *
     * Edge cases:
     *  - node values can't be equal, cause BST requires strictly less/greater.
     *
     * Time: O(n) cause we visit each node exactly once;
     * Space: O(n) due to callstack.
     */
    fun isValidBstViaSortedProp(root: IntBinaryTreeNode?): Boolean {
        if (root == null) return true

        if (!isValidBstViaSortedProp(root.left)) return false

        if (prevValue != null && prevValue!! >= root.`val`) return false
        prevValue = root.`val`

        return isValidBstViaSortedProp(root.right)
    }

    /**
     * Pre-order DFS based on the definition. The tree is BST only if all of its subtrees are themselves BSTs.
     * Same complexities as [isValidBstViaSortedProp].
     */
    fun isValidBstViaDefinition(root: IntBinaryTreeNode?): Boolean =
        isValidBstViaDefinitionRecursive(root = root, minBound = null, maxBound = null)

    /**
     * [minBound] and [maxBound] are exclusive.
     *
     * Edge cases:
     *  - Int.MIN_VALUE and INT.MAX value are within the allowed node.value input range
     *   (Int.MIN_VALUE = -2^31 and Int.MAX_VALUE = 2^31) => can't use them as to signify "any" boundary =>
     *   - either use Long for [minBound] and [maxBound] and Long.MIN/Long.MAX
     *   - or null
     *
     * Discovered thanks to the DSA course official solution.
     */
    private fun isValidBstViaDefinitionRecursive(root: IntBinaryTreeNode?, minBound: Int?, maxBound: Int?): Boolean {
        if (root == null) return true
        if ((minBound != null && root.`val` <= minBound) || (maxBound != null && root.`val` >= maxBound)) return false

        if (!isValidBstViaDefinitionRecursive(root.left, minBound, root.`val`)) return false
        return isValidBstViaDefinitionRecursive(root.right, root.`val`, maxBound)
    }
}
