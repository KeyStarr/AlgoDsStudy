package com.keystarr.algorithm.dp.other.graphs

import com.keystarr.datastructure.graph.tree.TreeNode
import kotlin.math.max

/**
 * ⭐️ first ever DP on graphs that I've solved
 * LC-337 https://leetcode.com/problems/house-robber-iii/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= number of nodes <= 10^4;
 *  • 0 <= node.val <= 10^4.
 *
 * Final notes:
 *  • done [topDown] by myself in 25 mins. Made 2 major design flaws => failed a couple of runs;
 *  • reasoned that bottom-up would not be reasonable to implement => was wrong, failed to see the obvious static relation
 *   of [dpEfficient]. TODO: is it bottom-up after all, or is it top-down too??? or is it not DP at all?? seems like just greedy DFS then
 *  • this solution https://leetcode.com/problems/house-robber-iii/solutions/79330/step-by-step-tackling-of-the-problem/
 *   discussed the interesting property of "losing information" as we go down the callstack in [topDown] => if we keep
 *   the info, we avoid duplicating subprograms.
 *   ⚠️ but I wasn't able to understand what exactly that means deep-down, so moved on for now =>
 *    TODO: understand deeper, is there a general principle here? to not loose state => avoid subproblem duplication for DP on tress & graphs in general???
 *
 * Value gained:
 *  • practiced solving a graph DP problem using top-down suboptimal DFS and then optimizing it down to an efficient greedy DFS
 *   (both share the same core principle, but differ in subproblems form and the result format).
 */
class HouseRobberIII {

    // TODO: resolve in 2-3 weeks, try to dig deeper to that potential core optimization principle for DP on graphs displayed here

    /**
     * Note that basically for each node as a root of a subtree we make a choice whether to take it => then consider
     *  both its children as not taking it (find max profit as the sum of these); if we don't take it => consider
     *  both children both taking their nodes or not, whatever is most optimal
     * => if we simply compute for each subtree the 2 results, whether we take the root or not take the root, we may
     *  via a static relation combine these when we backtrack, i.o. visit each node exactly once, perform a clean
     *  post-order DFS.
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     *  worst tree is in shape of a line => callstack then has height of n
     *
     * post-order DFS + greedy
     *
     * -----------------
     *
     * discovered thanks to the top time bar solution on leet
     */
    fun dpEfficient(root: TreeNode): Int {
        val result = dpEfficientDfs(root = root)
        return max(result.rob, result.skip)
    }

    private fun dpEfficientDfs(root: TreeNode?): Result {
        if (root == null) return Result(rob = 0, skip = 0)

        val left = dpEfficientDfs(root.left)
        val right = dpEfficientDfs(root.right)
        return Result(
            rob = root.`val` + left.skip + right.skip,
            skip = max(left.rob, left.skip) + max(right.rob, right.skip),
        )
    }

    /**
     * problem rephrase:
     *  given:
     *   - root: TreeNode of a binary tree, where each node.val is the house profit;
     *  goal: find the best valid combination of nodes (a subset), return the metric
     *   valid = no two directly linked nodes can be taken
     *   best = max total sum (metric) of node.val in the combination
     *
     * we have many choices to make, and at each step each choice affects further choices
     *  (e.g. if we take root, we cant take either root.left or root.right)
     *  + the goal is to find the best combination
     * => try dp?
     *
     * do subproblems overlap?
     *
     *
     * top-down dp
     *
     * goal: the original goal, type Int (max sum)
     * input state:
     *  - root: the root of the current subproblem's input subtree.
     *  - canBeUsed: Boolean (a factor of 2 on state => a const factor on space/time, so OK. but a lot simpler implementation)
     *
     * recurrence relation:
     *  dp(root,canBeUsed) = max(take, skip)
     *      take = if (canBeUsed) root.val + max( dp(root.left, canBeUsed=false), dp(root.right, canBeUsed=true) else 0
     *      skip = max(dp(root.left,canBeUsed=true), dp(root.right,canBeUsed=true))
     *
     * base cases:
     *  - root == null => trivially the max sum is then 0, return 0.
     *
     *
     * ----------------- pure 1D
     * recurrence relation:
     *  dp(root) = max(takeAndLeft, takeAndRight, skipAndLeft, skipAndRight)
     *      takeAndLeft = root.val + max(
     *          max(dp(root.left.left), dp(root.left.right)),
     *          max(dp(root.left.left), dp(root.left.right))
     *      takeAndRight = root.val + max(dp(root.right.left
     * ------------------
     *
     *
     * Time: always O(n)
     *  - worst we have n*2 states, where n = the number of nodes in the tree;
     *  - at each node we do const work (up to 4 calls)
     * Space: always O(n)
     *
     *
     * ------------ optimization
     *
     * I don't like the Pair as a key in the cache since every time we check the cache we create an object (basically every call,
     *  to dp including cache hits!!) but I don't see how to improve that, since we can't use array for nodes, cause
     *  we don't know in advance how many nodes we got. We could though count the nodes in O(n) then use an array cache
     *  for nodes as like a BT representation in an array, that would work, but sounds excessive here to just improve
     *  the time const (implementation complexity vs time const win doesn't balance imho, in the context of the interview)
     *
     * can we do a bottom-up optimized?
     *
     * if we're not changing the direction => we have to do it from the leaves, like, post-order DFS?
     *  base case then node.isLeaf() => return node.val
     *  smth like dp(root) = max(root.val,
     *
     *  no, I don't think it makes sense to do that here, don't see a way to get that "root.val + the next potential ones"
     *   then. Top-down seems optimal.
     *
     *  maybe I'm just lazy
     */
    fun topDown(root: TreeNode): Int = topDownDfs(
        root = root,
        canBeUsed = true,
        cache = mutableMapOf()
    )

    private fun topDownDfs(
        root: TreeNode?,
        canBeUsed: Boolean,
        cache: MutableMap<Pair<TreeNode, Boolean>, Int>,
    ): Int {
        if (root == null) return 0

        val cacheKey = root to canBeUsed
        val cachedResult = cache[cacheKey]
        if (cachedResult != null) return cachedResult

        val take = if (canBeUsed) {
            root.`val` + topDownDfs(root.left, false, cache) + topDownDfs(root.right, false, cache)
        } else 0

        val skip = topDownDfs(root.left, true, cache) + topDownDfs(root.right, true, cache)
        return max(take, skip).also { cache[cacheKey] = it }
    }

    private class Result(
        val rob: Int,
        val skip: Int,
    )
}