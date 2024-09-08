package com.keystarr.algorithm.graph.tree.binary.search

import com.keystarr.datastructure.graph.tree.TreeNode

/**
 * 💣 really must fully retry this later, only recognized glimpses of the correct approach!
 * ⭐️ an amazing example of a BST modification type question with a clean recursive case-by-case approach typical for tree type questions
 * LC-450 https://leetcode.com/problems/delete-node-in-a-bst/description/
 * difficulty: medium
 * constraints:
 *  • 0 <= number of nodes <= 10^4;
 *  • -10^5 <= node.val, target <= 10^5;
 *  • each node value is unique;
 *  • root is a valid BST.
 *
 * Final notes:
 *  • gave up after 40 mins - the approach I have taken looked way too overcomplicated [wrongOnlyDelete], so I decided no to even
 *   finish designing it, and felt like I probably won't pivot reasonably fast;
 *  • good thing I stopped, the solution took a whole new direction! Basically I failed to make 3 key decisions:
 *   1. failed to observe that in case root == target has only one child => we can simply put it in place of the root;
 *   2. failed to see that in case we root == target and two children => we can replace value of the root with the required one
 *    instead of actually deleting root and "scraping" and detaching + attaching the other node in its place;
 *   3. failed to understand that we may simply design the algorithm according to the initial problem statement and therefore
 *    in case root has 2 children we can simply call the function recursively to delete the newfound value that we've assigned
 *    to current root!
 *   All these 3 decisions tremendously simplify the solution + last two rely on observation made in #1, which I failed to see!
 *   Should've handled cases logically one-by-one better, step-by-step, done case-by-case reasoning.
 *  • the actual clean efficient solution [deleteAndReplace] is beautiful in its simplicity and recursive step-by-step nature.
 *
 * Value gained:
 *  • practiced solving a BST modification type problem using a clean recursive case-by-case approach;
 *  • 🔥 apparently most tree questions indeed require the best solutions to basically be recursive and answer the original goal
 *   with the original function's signature. Just like LinkedLists with its 1 pass O(n) time O(1) space most of the time being the best characteristics.
 */
class DeleteNodeInABST {

    // TODO: must retry in 1-2 weeks

    /**
     * Fully based on https://www.youtube.com/watch?v=LFzAoJJt92M&ab_channel=NeetCodeIO
     *
     * So, in [wrongOnlyDelete]:
     *  - I correctly assumed that, in case current subtree's root.val == target and root both children
     *   => we need to delete it and replace it with either the largest value node in its left subtree or the smallest
     *    value node in its right subtree;
     *  - but I wrongfully tried to actually "carry over" that smallest/largest node and change its left/right to the
     *   root's left/right children, connecting it to its parent => too complex;
     *  - and I completely missed that in case we have only one child (the other is null) and root == target => we may
     *   simply move either its left or right child in its place as-is, changing nothing else!
     *
     * In [wrongReplaceAndDeleteLeaf] I tried the lightly replacing value instead of hard-deleting and swapping left/right idea,
     *  but not in an optimal way, since I've written some separate algorithm for handling finding the target, and then
     *  to find both either the smallest/largest value and then to actually swap it throughout the entire line down to a leaf,
     *  and delete that leaf in the end.
     *  If I noticed that in case we have exactly either left or right child we could simply swap it in place of the root
     *  => it would've been a lot easier.
     *
     * Here we have key decisions in-place:
     *  1. find the target, but in a modify kinda way => if its in the right subtree, then technically we call the delete algorithm
     *   on it, and it might be deleted => assign the result to the root.right. Vice versa for the left subtree;
     *  2. if root == target:
     *   - if root is a leaf => return null, since we can just delete it (and parent will modify its subtree ref accordingly);
     *   - if root has only one either child => return the only child, since we may simply move it up;
     *   - if root has both children => we may move either the largest node in the left subtree in place of root, or smallest
     *    in right subtree => we chose to only do the smallest in the right one. So we find the smallest and then simply
     *    call delete on it, since it may itself not be a leaf, consider original root=3, target=20:
     *                 3
     *                  20
     *                 15 40
     *                   25 50
     *                    35
     *                   28 37
     *
     * Time: average/worst O(height)
     *  aside from the `findSmallestNode` call we always go down the tree, checking exactly 1 node at every level.
     *  each time we call `findSmallestNode` we effectively find the next target node, and then just go down to it again,
     *  this time with the main removal logic => technically worst case is we call findSmallestNode at the original root node
     *  and then as many times as we reach an actual leaf => we just visit 2*height nodes, cause we check each node then
     *  only exactly twice, and that's the worst case.
     *
     *  best case is when we have root==target and root has only one child => we simply move the other in place of root for O(1) time.
     *  e.g. above example but root=3 target=3
     *
     * Space: O(height) for the callstack
     *  once again findSmallestNode's callstack only adds a const factor to space
     */
    fun deleteAndReplace(root: TreeNode?, target: Int): TreeNode? {
        if (root == null) return null

        when {
            target > root.`val` -> root.right = deleteAndReplace(root.right, target)
            target < root.`val` -> root.left = deleteAndReplace(root.left, target)
            else -> {
                if (root.left == null && root.right == null) return null
                if (root.left == null) return root.right
                if (root.right == null) return root.left

                val smallestNode = findSmallestNode(root.right!!)
                root.`val` = smallestNode.`val`
                root.right = deleteAndReplace(root.right, smallestNode.`val`)
            }
        }
        return root
    }

    /**
     * WRONG, fails 54th/92 test case NPE. Didn't figure it out in time - the input is too large to reasonably debug.
     *
     * ----
     *
     * what if we follow a similar reasoning to [wrongOnlyDelete], but instead of deleting nodes we replace the values?
     */
    fun wrongReplaceAndDeleteLeaf(root: TreeNode?, target: Int): TreeNode? {
        if (root == null || (root.left == null && root.right == null && root.`val` == target)) return null

        println(root.treeVisualizedString())

        val targetNode = findTarget(root, target) ?: return root
        var nodeToDelete: TreeNode? = targetNode
        while (nodeToDelete?.left != null || nodeToDelete?.right != null) {
            val newNodeToDelete =
                nodeToDelete.left?.let { findLargestNode(it) } ?: findSmallestNode(nodeToDelete.right!!)
            nodeToDelete.`val` = newNodeToDelete!!.`val`
            nodeToDelete = newNodeToDelete
        }

        println(root.treeVisualizedString())
        deleteExistingLeaf(root, nodeToDelete!!.`val`)
        return root
    }

    private fun findTarget(root: TreeNode, target: Int): TreeNode? =
        when {
            root.`val` == target -> root
            target > root.`val` -> root.right?.let { findTarget(it, target) }
            else -> root.left?.let { findTarget(it, target) }
        }

    private fun findLargestNode(root: TreeNode): TreeNode = root.right?.let(::findLargestNode) ?: root

    private fun findSmallestNode(root: TreeNode): TreeNode = root.left?.let(::findLargestNode) ?: root

    private fun deleteExistingLeaf(root: TreeNode, target: Int) {
        when {
            root.left?.`val` == target && root.left?.isLeaf() == true -> root.left = null
            root.right?.`val` == target && root.right?.isLeaf() == true -> root.right = null
            else -> {
                if (target > root.`val`) deleteExistingLeaf(root.right!!, target) else deleteExistingLeaf(
                    root.left!!,
                    target
                )
            }
        }
    }

    private fun TreeNode.isLeaf() = left == null && right == null

    /**
     * major cases for NTD = the node to be deleted:
     *
     *  1. NTD is a leaf
     *
     *  2. NTD is not a leaf:
     *   then there is a valid answer if we take a node either from the left or from the right subtrees and place it on NTD's position
     *   => the only question is then the amount of work to be done, but that would only impact the cost asymptotically, so can be
     *   optimized later
     *
     *   basically the rule then is - choose any existing subtree and:
     *    - if its left => take the largest (rightmost) node in that subtree, place it at the current NTD's position and
     *     perform the delete algorithm on it;
     *    - if its right => take the smallest (leftmost) node in that subtree and do the same.
     *
     * edge cases:
     *  - NTD is the original root =>
     *
     *
     * Time:
     * Space:
     */
    fun wrongOnlyDelete(root: TreeNode, target: Int): TreeNode {
        if (root.`val` == target) {
            throw NotImplementedError()
        }

        val targetParent = findTargetParent(root, target)

        var nodeToDeleteParent = targetParent
        var isDeleteLeft = targetParent.left?.`val` == target
        var nodeToDelete = if (isDeleteLeft) targetParent.left!! else targetParent.right!!
        var originalLeft = nodeToDelete.left
        var originalRight = nodeToDelete.right
        while (nodeToDelete.left != null || nodeToDelete.right != null) {
            val isNewDeleteLeft = nodeToDelete.left != null
            val replacementNodeParent =
                if (isNewDeleteLeft) findLargestNodeParent(nodeToDelete.left!!) else findSmallestNodeParent(nodeToDelete.right!!)
            val replacementNode = replacementNodeParent.right ?: replacementNodeParent.left!!

            val replacementLeft = replacementNode.left
            val replacementRight = replacementNode.right

            replacementNode.left = originalLeft
            replacementNode.right = originalRight

            if (isDeleteLeft) nodeToDeleteParent.left = replacementNode else nodeToDeleteParent.right = replacementNode

            isDeleteLeft = isNewDeleteLeft
            nodeToDeleteParent = replacementNodeParent
            nodeToDelete = replacementNode
            originalLeft = replacementLeft
            originalRight = replacementRight
        }

        throw NotImplementedError()
    }

    private fun findTargetParent(root: TreeNode, target: Int): TreeNode {
        if (root.left?.`val` == target || root.right?.`val` == target) return root

        return if (target > root.`val`) findTargetParent(root.right!!, target) else findTargetParent(
            root.left!!,
            target
        )
    }

    private fun findLargestNodeParent(root: TreeNode): TreeNode {
        throw NotImplementedError()
    }

    private fun findSmallestNodeParent(root: TreeNode): TreeNode {
        throw NotImplementedError()
    }
}

fun main() {
    val input = TreeNode.treeFrom(arrayOf(5, 3, 6, 2, 4, null, 7))
    println("input:")
    println(input?.treeVisualizedString())
    println("\noutput:")
    println(
        DeleteNodeInABST().deleteAndReplace(
            root = input,
            target = 3,
        )?.treeVisualizedString()
    )
}
