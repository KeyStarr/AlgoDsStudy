package com.keystarr.algorithm.graph.backtracking

import com.keystarr.algorithm.graph.tree.binary.IntBinaryTreeNode

/**
 * LC-113 https://leetcode.com/problems/path-sum-ii/description/
 * difficulty: medium
 * constraints:
 *  • 0 <= the number of nodes <= 5000;
 *  • -1000 <= node.value <= 1000;
 *  • -1000 <= targetSum <= 1000.
 *
 * Final notes:
 *  • done [efficient] by myself in 25 mins (should've been quicker, really);
 *  • recognized backtracking straight-away, like 2 mins after I've read problem statement I've had the core solution
 *   in my head. "all of something", especially "all nodes in the path / all valid paths" for graph problems is a usual
 *   sign of backtracking required;
 *  • surprisingly, failed like 3-5 initial runs (not submissions):
 *   • ⚠️ major error: automatically designed the dfs base case to be `root == null` and added path to the list there
 *    BUT THAT'S WRONG, since a leaf would have 2 children matching that case => the path ending with that leaf would
 *    be added twice instead of once!
 *     => THINK of base cases for recursion ALWAYS adapting to the problem at hand. BFS and DFS included!
 *   • messed up some variable order and passing, since we have like 3 similar sum variables in the same local context;
 *   • something else, but it doesn't matter. Caught the only edge case though!
 *
 * Value gained:
 *  • practiced recognizing and solving a binary tree problem efficiently with backtracking dfs.
 */
class PathSumII {

    /**
     * All of something = all valid paths, each represented as a list of node values along it
     * => try DFS + backtracking.
     *
     * Very straightforward => go ahead to the implementation.
     *
     * Edge cases:
     *  - number of nodes == 0 => always return an empty list => edge case with targetSum == 0, still return an emptyList,
     *   but we would add one empty path list to it => early return;
     *
     * Time: always O(nodes)
     * Space: O(k) = average/worst O(n), where k = tree height => worst k=n
     */
    fun efficient(root: IntBinaryTreeNode?, targetSum: Int): List<List<Int>> =
        if (root == null) {
            emptyList()
        } else {
            mutableListOf<List<Int>>().apply {
                dfs(
                    root = root,
                    targetSum = targetSum,
                    currentPath = mutableListOf(),
                    currentPathSum = 0,
                    validPaths = this,
                )
            }
        }

    private fun dfs(
        root: IntBinaryTreeNode,
        targetSum: Int,
        currentPath: MutableList<Int>,
        currentPathSum: Int,
        validPaths: MutableList<List<Int>>,
    ) {
        currentPath.add(root.`val`)
        val newSum = currentPathSum + root.`val`
        if (root.left == null && root.right == null) {
            if (newSum == targetSum) validPaths.add(ArrayList(currentPath))
            currentPath.removeLast()
            return
        }

        root.left?.let { dfs(root = it, targetSum, currentPath, newSum, validPaths) }
        root.right?.let { dfs(root = it, targetSum, currentPath, newSum, validPaths) }
        currentPath.removeLast()
    }
}
