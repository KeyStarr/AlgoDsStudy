package com.keystarr.algorithm.graph.tree.binary

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode

/**
 * LC-236 https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= number of nodes <= 10^5;
 *  • -10^9 <= node.value <= 10^9;
 *  • all node.value are unique;
 *  • first != second;
 *  • both first and second exist in the tree;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • spent 2-2.5h trying to implement it myself. Tried both iterative and recursive, even with hints from the editorial.
 *      Failed miserably;
 *  • only implemented [recursive] after studying the editorial for, like, 1 hour (UNDERSTOOD ONLY AFTER DRY-RUNNING,
 *      before it felt like gibberish).
 *
 * Value gained:
 *  • don't understand/stuck => by default, just dry-run;
 *  • apparently IT IS OKAY if deep in the recursion RECURSIVE FUNCTION'S GOAL MIGHT BE NOT TO FIND THE ACTUAL RESULT
 *      (relative to the current subtree, not the original one), AND if the first call always returns the required result
 *      (relative to the original tree), IT IS A VALID TECHNIQUE.
 *      Фух;
 *  • maybe, to save time, ALWAYS UNDERSTAND FIRST the explanation of the solution during the article-phase of the topic?
 *      don't just attempt to solve it yourself based on a few hints? Maybe it will not compromise the quality of the
 *      studying but just save A LOT OF TIME?
 */
class LowestCommonAncestorOfABinaryTree {

    /**
     * Design the function thinking about current node (any node, really) being the tree itself with root at node!
     *
     * Goal of [recursive] -> return the lowest common ancestor (LCA) of both [first] and [second].
     *
     * HOWEVER, deep in the recursion, if one node is not present in the current subtree [root], but another is =>
     *  WE RETURN JUST THE PRESENT TARGET.
     * => technically (if I'm not wrong), the result of the first call in the stack will always be LCA, BUT deep in the
     *  recursion THE FUNCTION MAY RETURN NOT THE LCA!!!
     * And that's okay. Hm.
     *
     * Must handle 4 cases:
     *  - [root] is the original tree root => always returns an LCA of [first] and [second].
     *  - [root] is a root of subtree of the original tree:
     *   - if [first] and [second] are in the subtree => returns their LCA;
     *   - if only one of them is present => returns this present target, either [first] or [second];
     *   - if none are present => returns null.
     *
     * Implementation:
     *
     *  - base case:
     *      - if (root == null) return null // a non-existent node after a leaf, no first&second in that subtree
     *      - if (root == first || root == second) return root
     *
     *  - recursive case:
     *      - leftNode = recursive(root.left, first, second)
     *      - rightNode = recursive(root.right, first, second)
     *
     *      // since node.value are unique, and first/second are defined by node.value:
     *      //  1) if first node was in the left/right subtree, then it can't be in the right/left subtree;
     *      //  2) if second node was in the left/right subtree, it can't be in the right/left.
     *      // i.o. either first and second can only be in one subtree (left or right) at once, or be in none
     *      // => if both calls to left and right subtrees returned not null, then we found both first in second
     *      //  (valid combinations are left=first/second and right=second/first)
     *      - if (leftNode != null && rightNode != null) return root
     *      - if (leftNode == null && rightNode == null) return null // neither is in the current subtree rooted at [root]
     *      // if we found only one node in the subtree rooted at [root], try backtracking it, perhaps, at any previous
     *      // (larger) subtrees another target node would be found in its other subtree.
     *      - else return leftNode ?: rightNode
     *
     * Time: average/worst O(n), at worst we visit each node in [root] tree once (excluding backtracking);
     *  (where n=number of nodes in the tree)
     * Space: average/worst O(n) due to callstack.
     */
    fun recursive(root: IntBinaryTreeNode?, first: IntBinaryTreeNode?, second: IntBinaryTreeNode?): IntBinaryTreeNode? {
        if (root == null) return null // a non-existing tree, LCA trivially doesn't exist
        if (root == first || root == second) return root // ?????

        val leftNode = recursive(root.left, first, second)
        val rightNode = recursive(root.right, first, second)
        if (leftNode != null && rightNode != null) return root // both targets are in different subtrees of root => root is LCA
        if (leftNode == null && rightNode == null) return null // root isn't a target, and target are not in both it's subtrees

        // we found 1 target in the current subtree rooted at [root] => backtrack it, the other target must be in
        // one of the larger subtrees, in a different subtree of one of them
        return leftNode ?: rightNode
    }
}

fun main() {
    val algo = LowestCommonAncestorOfABinaryTree()
    println(algo.directChildrenCase())

}

private fun LowestCommonAncestorOfABinaryTree.directChildrenCase() = recursive(
    root = IntBinaryTreeNode(
        `val` = 3,
        left = IntBinaryTreeNode(
            5,
            left = IntBinaryTreeNode(`val` = 6),
            right = IntBinaryTreeNode(
                `val` = 2,
                left = IntBinaryTreeNode(`val` = 7),
                right = IntBinaryTreeNode(`val` = 4)
            )
        ),
        right = IntBinaryTreeNode(
            `val` = 1,
            left = IntBinaryTreeNode(0),
            right = IntBinaryTreeNode(8),
        )
    ),
    first = IntBinaryTreeNode(5),
    second = IntBinaryTreeNode(1),
)?.`val`

private fun LowestCommonAncestorOfABinaryTree.directChildrenMinNodes() = recursive(
    root = IntBinaryTreeNode(
        `val` = 1,
        left = IntBinaryTreeNode(2)
    ),
    first = IntBinaryTreeNode(1),
    second = IntBinaryTreeNode(2),
)?.`val`
