package com.keystarr.algorithm.graph.tree.binary

import java.util.*
import kotlin.collections.ArrayList

/**
 * LC-199 https://leetcode.com/problems/binary-tree-right-side-view/description/
 * difficulty: medium
 * constraints:
 *  • 0 <= number of nodes <= 100;
 *  • -100 <= node.value <= 100.
 *
 * Final notes:
 *  • wow I got tricked!!! For the first 5 mins I legit decided that the solution is simply "traverse through the tree,
 *    always go right child while it exists"))))) It felt wrong cause leet-medium can't be that simple => then It struck me;
 *    (hello diffused thinking, gotta thank ya)
 *  • implemented both [bfs] and [dfs] by myself. FAILED a few initial runs though, but 1st time actual submit both.
 *
 * Value gained:
 *  • leet-mediums often have at least one TRICK up their sleeves! Watch out. Really invest into figuring out all MAJOR
 *      cases first, expect and reveal trickery;
 * • funny, both [dfs] and [bfs] have same time complexities! But:
 *  • [bfs] is easier to understand and would be faster and easier to implement during an interview;
 *  • [dfs] is more concise.
 *  => so for this problem for real interviews [bfs] is the choice.
 */
class BinaryTreeRightSideView {

    /**
     * Problem rephrase: "return the value of rightmost nodes on all layers, ordered top to bottom".
     *
     * Rightmost node on each layer may be in either subtree of current [root], so we have to check both.
     *
     * Why use BFS? Why not DFS? I feel it, but it's hard to put into words.
     *
     * THE TRICK is that the rightmost node in a level of the tree might be a node of any subtree that has a root at the
     *  previous level! Therefore, to find which node is actually rightmost we have to, if using BFS, add ALL the nodes
     *  of the level to the queue and ONLY THEN find out which of them is the rightmost.
     *
     * Use BFS:
     *  - create results: List<Int>; // we don't know the depth of the tree therefore cant compute max width, have to use a dynamic array
     *  - create a queue;
     *  - queue.addLast(root);
     *  - currentLevel=0
     *  - while queue.isNotEmpty():
     *      - currentLevelNodesAmount = queue.size
     *      - results[currentLevel] = queue.first().value // the rightmost child of the level will always be first in the queue
     *      - repeat(currentLevelNodesAmount):
     *          - currentNode = queue.removeFirst()
     *          - currentNode.right?.let { queue.addLast(it) }
     *          - currentNode.left?.let { queue.addLast(it) }
     *      - currentLevel++
     *  - return results
     *
     * Edge cases:
     *  - root == null => return emptyList()
     *  - a tree with only right children all the way down to the leaf (a line) => answer is all values of the tree
     *      exactly in the order of how they are linked => correct, queue has at most 1 element at the end of each iteration;
     *  - a tree in which rightmost nodes are in different branches => correct, cause we simply add ALL nodes of the current
     *      level irrespective of what branches these are on to the queue, in order right-left.
     *
     * Time: always O(n) since we visit each node exactly once and one node handling takes O(1) time
     * Space: O(n) or precisely the max amount of nodes in the level, which depends on n
     */
    fun bfs(root: IntBinaryTreeNode?): List<Int> {
        if (root == null) return emptyList()

        val result = ArrayList<Int>()
        val queue: Queue<IntBinaryTreeNode> = ArrayDeque<IntBinaryTreeNode>().apply { add(root) }
        while (queue.isNotEmpty()) {
            val currentLevelNodesAmount = queue.size
            result.add(queue.first().`val`)
            repeat(currentLevelNodesAmount) {
                val currentNode = queue.remove()
                currentNode.right?.let(queue::add)
                currentNode.left?.let(queue::add)
            }
        }

        return result
    }

    /**
     *
     * DFS, use a recursive internal function.
     *
     * For the subtree at [root] if it's the rightmost node at the [currentDepth] level of the original tree =>
     * add it into [results]. Otherwise, make a step towards finding the actual rightmost node.
     *
     * information required at each call:
     *  - root
     *  - prevDepth
     *  - results: ArrayList<Int>
     *
     * base case:
     *  - root == null => return;
     *
     * recursive case:
     *  - currentDepth = prevDepth + 1
     *  // the first node that we've encountered on currentDepth, from the right = rightmost node of the current level
     *  - if (currentDepth == results.size) results.add(root.value)
     *  - dfsRecursive(root.right, currentDepth, results)
     *  - dfsRecursive(root.left, currentDepth, results)
     *  - return
     *
     * Trick - at each call results.size - 1 is the last level that we have found the answer for. Therefore,
     *  when we encounter the first node which is below that level, its the rightmost node on that level, cause we
     *  traverse prioritizing the right subtree over left.
     *
     * Edge cases:
     *  - root == null => empty list, correct as-is;
     *  - a tree with only right children all the way down to the leaf (a line) => answer is all values of the tree
     *      exactly in the order of how they are linked => correct, we always go right until the leaf and then backtrack
     *      without doing any modification to the results arraylist;
     *  - a tree in which rightmost nodes are in different branches => correct due to current depth/results.size mechanic.
     *
     * Time: always O(n) cause we visit each node in the tree. Even if it's the perfect binary tree, and we basically
     *  got the results full by just going to the right child at each call until we reached the node after leaves =>
     *  we still visit the ENTIRE left tree and just "do nothing" there (and all left subtrees of the root.right subtree first
     *  ofc)
     * Space: O(n) or precisely at most the tree's height
     */
    fun dfs(root: IntBinaryTreeNode?): List<Int> = ArrayList<Int>().also { results ->
        dfsRecursive(root = root, prevDepth = -1, results = results)
    }

    private fun dfsRecursive(root: IntBinaryTreeNode?, prevDepth: Int, results: ArrayList<Int>) {
        if (root == null) return

        val currentDepth = prevDepth + 1
        if (currentDepth == results.size) results.add(root.`val`)
        dfsRecursive(root.right, currentDepth, results)
        dfsRecursive(root.left, currentDepth, results)
    }
}
