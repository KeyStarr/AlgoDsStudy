package com.keystarr.algorithm.graph.tree.binary

import kotlin.math.max

/**
 * LC-104 https://leetcode.com/problems/maximum-depth-of-binary-tree/description/
 * difficulty: easy
 * constraints:
 *  • 0 <= number of nodes <= 10^4;
 *  • -100 <= node.value <= 100.
 *
 * Final notes:
 *  • woohoo, submitted 1st try, thanks to dry-running, caught that the base case requires currentDepth -1.
 *
 * Value gained:
 *  • IVE DONE MA FIRST EVER PROBLEM ON GRAPH/TREE/BINARY ON LEETCODE, yaay!
 */
class MaximumDepthOfBinaryTree {

    /**
     * Problem rephrase - find the node with the maximum depth, return the depth.
     *
     * Idea: implement DFS via D&C, go through all nodes and return the maximum depth via backtracking.
     *
     * Edge cases:
     *  - numberOfNodes==0 => 1-1=0, correct;
     *  - numberOfNodes==1 => only the root, 1, correct.
     *
     * Time: average/worst O(n), where n=number of nodes:
     *  - best when the leftmost leaf has the maximum depth;
     *  - worst when the rightmost leaf has the maximum depth;
     *  - average is all in between.
     * Space: always O(m), where m=maximum depth in the tree.
     *
     * ---
     *
     * this is a PREorder DFS => start incrementing `currentDepth` at root, and just always increment
     *   before traversing down the tree => upon visiting any leaf we already have its depth. Then just propagate it
     *   upwards but don't forget to decrement on leaves
     *
     * compared to [postOrderRecursive]:
     *  + more intuitive, since we simply get each node's actual depth during it's call's combination phase;
     *  +- at each leaf we have it's actual depth (if we needed it for other algo, here it doesn't matter, so excessive?)
     *  - a bit ugly to pass a second param `currentDepth` when we can do without it?
     */
    fun preOrderRecursive(root: IntTreeNode?): Int = preOrderRecursiveInternal(root, currentDepth = 1)

    private fun preOrderRecursiveInternal(node: IntTreeNode?, currentDepth: Int): Int {
        // base case
        if (node == null) return currentDepth - 1 // actual leaf depth on current branch

        // recursive case
        // divide is just node.left and node.right
        val nextDepth = currentDepth + 1 // conquer current node
        val leftDepth = preOrderRecursiveInternal(node.left, nextDepth) // conquer left subtree
        val rightDepth = preOrderRecursiveInternal(node.right, nextDepth) // conquer right subtree

        return max(leftDepth, rightDepth) // combine
    }

    /**
     * this is a POSTorder DFS => start incrementing `currentDepth` at leaves, increment it each time we backtrack
     *   => we will have actual maximum depth only at root at the last backtracking (and, during both
     *   traversing and backtracking, we have at no node the length of any leaf unless its depth equals the max depth)
     *
     * compared to [preOrderRecursive]:
     *  + more idiomatic (?), a single param and the result is passed via return only;
     *  - less intuitive (?), cause we basically at each node we get its depth counting from the leaves, and only
     *      at the root note we get the maximum leaf depth (which is OK cause that's the answer).
     */
    fun postOrderRecursive(root: IntTreeNode?): Int = postOrderRecursiveInternal(root)

    private fun postOrderRecursiveInternal(node: IntTreeNode?): Int {
        if (node == null) return 0 // as we backtrack once, we get leaf depth counting from leaves and starting from 1

        val leftDepthFromLeaves = postOrderRecursiveInternal(node.left)
        val rightDepthFromLeaves = postOrderRecursiveInternal(node.right)

        return max(leftDepthFromLeaves, rightDepthFromLeaves) + 1
    }
}

class IntTreeNode(
    var value: Int,
    var left: IntTreeNode? = null,
    var right: IntTreeNode? = null,
)

fun main() {
    println(
        MaximumDepthOfBinaryTree().preOrderRecursive(
            IntTreeNode(
                value = 1,
                left = IntTreeNode(
                    value = 2,
                    right = IntTreeNode(
                        value = 4,
                    )
                ),
                right = IntTreeNode(
                    value = 3,
                    left = IntTreeNode(
                        value = 5,
                        left = IntTreeNode(
                            value = 6,
                        )
                    )
                )
            )
        )
    )
}