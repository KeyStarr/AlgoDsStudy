package com.keystarr.algorithm.graph.tree.binary

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode
import java.util.*
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
     *  - worst case m=n if the tree is just a single line (basically a singlylinkedlist lol).
     *  - upd: average is O(n) too then
     *  - best: the tree is full, then its max depth is O(logn), cause each layer except leaves adds x2 nodes
     *      (2 for each at the previous layer)
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
    fun preOrderRecursive(root: IntBinaryTreeNode?): Int = preOrderRecursiveInternal(root, currentDepth = 1)

    private fun preOrderRecursiveInternal(node: IntBinaryTreeNode?, currentDepth: Int): Int {
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
     * intuition: for each node we compute its depth as if it's farthest leaf was the root.
     *
     * this is a POSTorder DFS => start incrementing `currentDepth` at leaves, increment it each time we backtrack
     *   => we will have actual maximum depth only at root at the last backtracking (and, during both
     *   traversing and backtracking, we have at no node the length of any leaf unless its depth equals the max depth)
     *
     * same time/space complexity as [preOrderRecursive]
     *
     * compared to [preOrderRecursive]:
     *  + more idiomatic (?), a single param and the result is passed via return only;
     *  - less intuitive (only to inexperienced graph-enjoyers?), cause, for a laymen (me), getting the depth at each
     *      node from the actual root makes more sense.
     */
    fun postOrderRecursive(root: IntBinaryTreeNode?): Int = postOrderRecursiveInternal(root)

    private fun postOrderRecursiveInternal(node: IntBinaryTreeNode?): Int {
        if (node == null) return 0 // as we backtrack once, we get leaf depth counting from leaves and starting from 1

        val leftDepthFromLeaves = postOrderRecursiveInternal(node.left)
        val rightDepthFromLeaves = postOrderRecursiveInternal(node.right)

        return max(leftDepthFromLeaves, rightDepthFromLeaves) + 1
    }

    /**
     * Check right children first implementation (visiting the right subtree first).
     * If we push right to stack first and then left, then the order of traversal would be the same as [preOrderRecursive]
     * and [postOrderRecursive], that is, check left children first (vice versa we could swap left and right calls in
     * recursive to get behavior same as in here).
     *
     * Basically same order of traversal as the recursive DFS (noting the above) BUT no backtracking through all previous
     * nodes, one could say, we jump from one branch to another.
     *
     * Time: always O(n)
     * Space: O(m) (if precisely, here m = max depth - 1)
     * where n & m are same as [preOrderRecursive]
     *
     * TODO: understand, why so much slower on average than both recursive solutions here on leetcode runtime?
     *  is stack based on ArrayDeque so much slower than the callstack (including backtracking through all previous nodes
     *  in the current branch once we reach leaves for recursion)
     */
    fun iterative(root: IntBinaryTreeNode?): Int {
        if (root == null) return 0

        val stack = ArrayDeque<NodeToDepth>().apply { addLast(root to ROOT_DEPTH) }
        var maxDepth = ROOT_DEPTH
        while (stack.isNotEmpty()) {
            // could use destructive declaration and merge 3 lines into 1, but don't want to use data classes here just for this
            val currentPair = stack.removeLast()
            val currentNode = currentPair.node
            val currentDepth = currentPair.depth

            val childrenDepth = currentDepth + 1
            maxDepth = max(currentDepth, maxDepth)
            currentNode.left?.let { stack.addLast(it to childrenDepth) }
            currentNode.right?.let { stack.addLast(it to childrenDepth) }
        }
        return maxDepth
    }
}

private const val ROOT_DEPTH = 1

private class NodeToDepth(
    val node: IntBinaryTreeNode,
    val depth: Int,
)

private infix fun IntBinaryTreeNode.to(depth: Int) = NodeToDepth(this, depth)

fun main() {
    println(
        MaximumDepthOfBinaryTree().preOrderRecursive(
            IntBinaryTreeNode(
                `val` = 1,
                left = IntBinaryTreeNode(
                    `val` = 2,
                    right = IntBinaryTreeNode(
                        `val` = 4,
                    )
                ),
                right = IntBinaryTreeNode(
                    `val` = 3,
                    left = IntBinaryTreeNode(
                        `val` = 5,
                        left = IntBinaryTreeNode(
                            `val` = 6,
                        )
                    )
                )
            )
        )
    )
}
