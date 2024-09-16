package com.keystarr.algorithm.graph.tree.binary

import com.keystarr.datastructure.graph.tree.TreeNode

/**
 * ⭐️⭐️ a stark example of a "prefix sum" being the key to an efficient solution for not an array input (binary tree here)
 *
 * LC-437 https://leetcode.com/problems/path-sum-iii/description/
 * difficulty: medium (one of the hardest mediums I've encountered, with so much going on)
 * constraints:
 *  • 0 <= number of nodes <= 10^3;
 *  • -10^9 <= node.val <= 10^9;
 *  • -10^3 <= targetSum <= 10^3.
 *
 * Final notes:
 *  • at first I first the efficient solution was DP since we have we need to count all valid combinations essentially (valid tree paths,
 *   including "partial" paths), and the subproblems overlap, so I went for [topDownDp]. Spent about 30-40 mins there and failed;
 *  • then tried just a brute force. Couldn't figure it out at first, so went into the discussion section cause I sort of just
 *   gave up. Picked up a clue that one of the solutions involves 2 recursion functions. At this point spent ~1.5h at this problem;
 *  • did [wrongBruteDfs] the following day in about 30 mins. Failed a submit, dry ran and understood the error, redesigned
 *   the approach by myself and did [bruteDfsCountAllValidPaths] at about 1h mark;
 *  • gave up and checked the solution finally, learned of the core concept using the prefix sum on the tree =>
 *   designed the rest by myself and done [efficientPrefixSum] at about 3h time mark (total invested into this problem);
 *  • also I was so overwhelmed with that main solution that I failed the trivial edge case of sum being Long :D (failed a submit)
 *  • i.o. failed to come up both with even a brute force solution, let alone the efficient one, and spent quite some
 *   time to get on board with both.
 *
 * Value gained:
 *  • learned that the prefix sum tool is an efficient way to count all valid partial sums (of consecutive portions of some
 *   sequences of elements) not for just arrays, but really any consecutive collections of elements, including trees (which, in retrospect, is super
 *   obvious. I could've recognized the tool here if I abstracted the goal right as "count all valid consecutive parts of the collection
 *   valid = target sum";
 *  • practiced solving a "count all valid full/partial paths in the binary tree" type problem.
 */
class PathSumIII {

    // TODO: definitely a full resolve in 1-2 weeks, both brute then efficient

    /**
     * Let's go back to the first observation made in [topDownDp] - subproblems overlap!
     *
     * e.g. tree = 1 -> 2 -> 3 -> 4
     *  as we start from 1, we need the all valid paths count from 3 that had leftSum = X at the call
     *  it so might happen that 1.val==2.val, then as we start either from 1 or from 2 leftSum at 3 would be equal
     *  and other more complex cases might arise with a similar outcome of node/sum duplication.
     *
     * input state:
     *  - root: TreeNode
     *  - leftSum: Int
     *
     * time would then be though still O(n*s), where can be anywhere in [-10^12;10^12]
     *
     * ------------------------- nope, there's a better one
     *
     * basically we need to count all partial sums of the tree which == [targetSum]. How to efficiently compute a partial
     *  sum for each subtree? We know how to efficiently compute a partial sum for each subarray => prefix sum. Why not use it here?
     *
     * Core concept:
     *  - iterate through the tree in a single DFS;
     *   - add each node to the current sum => compute the current prefix sum;
     *   - after computing the new prefix sum, store it in the dictionary. 🔥 but along with its frequency, since negative values
     *    are allowed => we might have multiple equal prefix sums;
     *   - at each node compute exactly what any previous prefix sum must have been in order for some part of the tree
     *    ending at the current node to sum up to [targetSum] => check the prefixSum dictionary, if there was one =>
     *    such a part (a continuous part of the tree starting at some previous visited node and ending at the current node)
     *    exists (maybe even multiple such parts/paths) => add all occurrences to the total count then;
     *   - ❗️ as we backtrack, remove the current prefix sum from the tree, as for every node we consider paths that only go
     *    from nodes at lower levels to higher levels, with level only steadily increasing.
     *
     * the case when root.val == targetSum is covered as then simply (newSum - targetSum) would equal to the prefix sum
     *  at the previous node => count paths of length == 1 node as well.
     *  e.g.
     *  targetSum = 2
     *      1
     *     2 3
     *
     * at 2 currentSum = 3, and in the map there is 1:1 => currentSum-targetSum =3-2=1 map[1] ==  1
     *
     * edge cases:
     *  - sum => max sum = 10^3 * 10^9 = 10^12 => use Long for sum.
     *
     * Time: always O(n)
     *  we visit each node exactly once => have n [efficientDfs] calls, it just being a regular dfs.
     * Space: average/worst O(n)
     *  worst map size is when at each node we have a distinct sum => n+1 entries
     *
     * ---------------
     *
     * Learned the core idea thanks to https://leetcode.com/problems/path-sum-iii/solutions/141424/python-step-by-step-walk-through-easy-to-understand-two-solutions-comparison/
     */
    fun efficientPrefixSum(root: TreeNode?, targetSum: Int): Int =
        efficientDfs(
            root = root,
            currentSum = 0L,
            targetSum = targetSum,
            prefixSumToFreqMap = mutableMapOf(0L to 1), // to count paths that start from root (when entire currentSum == targetSum)
        )

    private fun efficientDfs(
        root: TreeNode?,
        currentSum: Long,
        targetSum: Int,
        prefixSumToFreqMap: MutableMap<Long, Int>,
    ): Int {
        if (root == null) return 0

        val newSum = currentSum + root.`val`
        prefixSumToFreqMap[newSum] = prefixSumToFreqMap.getOrDefault(newSum, 0) + 1

        val pathsEndingAtFurtherNodesCount = efficientDfs(root.left, newSum, targetSum, prefixSumToFreqMap) +
                efficientDfs(root.right, newSum, targetSum, prefixSumToFreqMap)

        prefixSumToFreqMap[newSum] = prefixSumToFreqMap.getValue(newSum) - 1

        val pathsEndingAtCurrentNodeCount = prefixSumToFreqMap[newSum - targetSum] ?: 0
        return pathsEndingAtCurrentNodeCount + pathsEndingAtFurtherNodesCount
    }

    /**
     * Based on [wrongBrute] but with duplication fixed: start at each node separately, and try to count all possible valid
     *  paths based from it => sum up all paths across all starting points.
     *
     * edge cases:
     *  - sum => max sum = 10^3 * 10^9 = 10^12 => use Long for sum
     *
     * Time: O(n^2)
     *  - [bruteDfsCountAllValidPaths] gives exactly O(nodes) calls;
     *  - each of these makes a call to [dfsCountValidPathsOnlyFrom], and that one on average makes ~n/2 calls, so technically
     *   each call to it costs O(n).
     * Space: average/worst O(n)
     *  - max callstack height for both [bruteDfsCountAllValidPaths] and [dfsCountValidPathsOnlyFrom] is O(n) when tree is in
     *   a shape of a line.
     */
    fun bruteDfsCountAllValidPaths(root: TreeNode?, targetSum: Int): Int {
        if (root == null) return 0

        // if we just take the current node
        val validPathsFromRootCount = dfsCountValidPathsOnlyFrom(root = root, leftSum = targetSum.toLong())
        return validPathsFromRootCount + bruteDfsCountAllValidPaths(root.left, targetSum) + bruteDfsCountAllValidPaths(
            root.right,
            targetSum
        )
    }

    private fun dfsCountValidPathsOnlyFrom(root: TreeNode?, leftSum: Long): Int {
        if (root == null) return 0

        // take it and continue both left and right = 2 new paths
        val updatedSum = leftSum - root.`val`
        val currentCount = if (updatedSum == 0L) 1 else 0
        return currentCount + dfsCountValidPathsOnlyFrom(root.left, updatedSum) + dfsCountValidPathsOnlyFrom(
            root.right,
            updatedSum
        )
    }

    /**
     * WRONG - general idea is correct, but we add up all 3 cases in a single recursive call, since some calls would duplicate => incorrect result.
     *  (duplication reason: say we have a tree 1 -> 2 -> 3. Both paths "take 1, take 2 => skip at start at 3" and
     *   "skip 1 start and start at 2, skip 2 and start at 3" lead to the same exact call)
     *
     * -------------------
     *
     * for each node we have 3 choices:
     *  1. take it and stop the path there;
     *  2. take it and continue both left and right (2 new different paths);
     *  3. start the path from either left or right (2 new different paths).
     *
     */
    fun wrongBrute(root: TreeNode?, targetSum: Int): Int = wrongBruteDfs(root, 0, targetSum)

    private fun wrongBruteDfs(
        root: TreeNode?,
        currentSum: Int,
        targetSum: Int,
    ): Int {
        if (root == null) return 0

        val takeCurrentSum = currentSum + root.`val`

        // 1 take it and stop the path
        val currentCount = if (takeCurrentSum == targetSum) 1 else 0

        // 2 take it and continue both left and right = 2 new paths
        val continueCount =
            wrongBruteDfs(root.left, takeCurrentSum, targetSum) + wrongBruteDfs(root.right, takeCurrentSum, targetSum)

        // 3 skip it, start the path from left and right = 2 new paths
        val skipCount = wrongBruteDfs(root.left, 0, targetSum) + wrongBruteDfs(root.right, 0, targetSum)

        return currentCount + continueCount + skipCount
    }

    /**
     * one way to look at it - for each node as we go down we decide whether to take it or not. if we take it it adds up
     *  to the sum. As soon as we encounter targetSum => stop the current traversal + record it
     *
     * we make multiple choices and each choice limits further choices => try dp?
     *
     * top down
     *
     * - goal: return the number of paths across which node values sum up to [targetSum] in the tree [root];
     *  return value: integer
     *
     * - input state:
     *  - currentNode: TreeNode
     *  - currentSum: Int
     *
     * - recurrence relation:
     *  dp(node) = dp(currentNode.left, currentSum+currentNode.val) + dp(currentNode.left, currentSum)
     *   + dp(currentNode.right, currentSum+currentNode.val) + dp(currentNode.right, currentSum)
     *
     * - base cases:
     *  XX currentSum > targetSum -> return
     *   NOPE since the values can be negative!
     *  XX currentSum == targetSum -> record and return
     *   NOPE since the values can be == 0 or negative! meaning we might get it again
     *  root == null -> return (a non-existent subtree trivially has 0 matching paths always)
     *
     *
     * that's way too slow, no?
     *
     * basically at each node we do 4 branches, cause we consider each child with 2 different sum states.
     *
     * let's try it, if its correct but too slow we might try to optimize, try bottom-up etc
     *
     * edge cases:
     *  - do we count paths with only 1 node? I suppose so, cause technically it matches the description (though not obvious)
     *
     * Time: O(n * s), where s is all possible currentSum states: [-10^12:10^12]
     *
     * XX welp, THAT'S WRONG => java's map internal constraints will fail as we exceed Int.MAX number of elements
     *  if we have more than 10^9 distinct elements basically!
     *
     *  hm, but how many values can we actually realistically have though from the range of [-10^12:10^12]?
     *
     *
     * => try bottom up, maybe we can reduce the space?
     *
     */
    fun topDownDp(root: TreeNode?, targetSum: Int): Int =
        root?.let { topDownRecursive(root, targetSum, mutableMapOf()) } ?: 0

    private fun topDownRecursive(
        root: TreeNode,
        leftSum: Int,
        cache: MutableMap<TreeNode, MutableMap<Int, Int>>, // that's wrong obviously, since our state is 2D
    ): Int {
        if (root.left == null && root.right == null) {
            return (if (leftSum == 0) 1 else 0) + (if (leftSum - root.`val` == 0) 1 else 0)
        }

        val cachedResult = cache[root]?.get(leftSum)
        if (cachedResult != null) return cachedResult

        val takeCurrentSum = leftSum - root.`val`
        val leftCount = root.left?.let { left ->
            topDownRecursive(left, takeCurrentSum, cache) + topDownRecursive(left, leftSum, cache)
        } ?: 0
        val rightCount = root.right?.let { right ->
            topDownRecursive(right, takeCurrentSum, cache) + topDownRecursive(right, leftSum, cache)
        } ?: 0
        val currentCount = if (takeCurrentSum == 0) 1 else 0

        return (currentCount + leftCount + rightCount).also { result ->
            if (cache[root] == null) cache[root] = mutableMapOf()
            cache.getValue(root)[leftSum] = result
        }
    }

    /**
     * if we do bottom-up conventionally from [topDownDp] we'd have to travel from the leaves which isn't convenient.
     * try reversing direction of [topDownDp] => changing the goal?
     *
     * goal - return the number of valid paths in the tree of which [root] is a leaf?
     *
     * then for the original root its whether root.val == targetSum
     * for the original root.left its whether we took the original root.val + root.left or havent took the original root
     *  then its just root.left == targetSum etc
     *
     * and we could collect results just the same, sum it all up.
     *
     * core observation:
     * subproblems overlap cause we might visit same node having taken/skipped previous nodes in various ways and still
     *  arrive at it with the same sum, since node.val can be negative, zero and positive!
     *
     * what would the memoization be like then?
     *
     *
     *
     *
     */
    fun bottomUp(root: TreeNode?, targetSum: Int) {}
}

fun main() {
    val root = TreeNode.treeFrom(arrayOf(0, 0, 0))
    println(root?.treeVisualizedString())

    println()
    println(
        PathSumIII().efficientPrefixSum(
            root = root,
            targetSum = 0,
        )
    )
}
