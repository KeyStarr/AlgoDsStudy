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
     */
    fun recursiveInternal(root: IntTreeNode?): Int = recursiveInternal(root, currentDepth = 1)

    private fun recursiveInternal(node: IntTreeNode?, currentDepth: Int): Int {
        // base case
        if (node == null) return currentDepth - 1

        // recursive case
        // divide is just node.left and node.right
        val nextDepth = currentDepth + 1 // conquer current node
        val leftDepth = recursiveInternal(node.left, nextDepth) // conquer left subtree
        val rightDepth = recursiveInternal(node.right, nextDepth) // conquer right subtree

        return max(leftDepth, rightDepth) // combine
    }
}

class IntTreeNode(
    var value: Int,
    var left: IntTreeNode? = null,
    var right: IntTreeNode? = null,
)

fun main() {
    println(
        MaximumDepthOfBinaryTree().recursiveInternal(
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