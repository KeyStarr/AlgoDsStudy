package com.keystarr.algorithm.graph.tree.binary.search

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode
import com.keystarr.datastructure.graph.tree.TreeNode

/**
 * ⭐️ a remarkable BST problem:
 *  1. the first graph/tree problem I encounter that, for an efficient solution, requires graph to list conversion;
 *  2. beautifully forces to consider whether the BST is balanced or not, it's the key property of the problem on path
 *   to an efficient solution! From what BST I've encountered before - that didn't matter.
 *
 * LC-2476 https://leetcode.com/problems/closest-nodes-queries-in-a-binary-search-tree/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= node.val, queries\[i] <= 10^6;
 *  • 1 <= nodes amount == queries.size <= 10^5.
 *
 * Final notes:
 *  • done [wrongTLE] by myself in 35 mins, failed 34th/35 test case and failed to optimize => read the hints and the discussion
 *   section, realized what the trick was => done the rest of [efficient] by myself;
 *  • after learning the concept, done [efficient] by myself in another 35 mins. ⚠️ spent most time on [findClosestValues]
 *   trying to make sure that we  always grab the right numbers. Didn't see / try to see or prove the valid algo abstractly,
 *   instead implemented the default binary search algo and then dry ran to find out which numbers to select
 *   => ⚠️ failed the 1st submit, but done the 2nd.
 *
 * Value gained:
 *  • 🔥 apparently, if multiple queries are required, it's extremely important to check whether BST is guaranteed to be balanced.
 *   If not => its only efficient to either convert it into a balanced BST or to an array and work with that. Sounds like a super
 *   niche problem type, but anyway it is beautiful, for it was totally deducible from the problem's statement and my knowledge
 *   of the BST. Anyway, I failed and learned that lesson, but ⚠️ I'm not quite sure how to generalize it, I feel like such
 *   problem types may exist for other tools, but these are probably way too specific => not sure what we've learned here;
 *  • 🔥🔥 apparently, efficient solutions for tree/graph type problems MAY ACTUALLY REQUIRE CONVERTING THE GRAPH INTO A LIST!
 *   for generally it's neither the most efficient, nor the cleanest approach and is super frowned upon for even attempting.
 *  • practiced solving efficiently a unique BST type question using conversion to an array and binary search;
 */
class ClosestNodesQueriesInABinarySearchTree {

    // TODO: retry in 1-2 weeks

    // TODO (optional): solve via converting the BST into a balanced one (for O(n) time) to broaden my tooling horizons (+ for funsies)

    /**
     * Observe that the BST [root] MAY be unbalanced => worst time is O(n*m). We may optimize significantly to worst O(n + m * logm) by either:
     *  1. balancing the tree;
     *  2. building a sorted array of tree's values and performing the binary search on it.
     *
     * Choose #2 for simplicity:
     *  - do in-order dfs and build the sorted array;
     *  - implement the binary search with correctly finding the closest below/above integers.
     *
     * Time: average/worst O(n + m * logn)
     * Space: always O(treeHeight + n) = O(n), for worst treeHeight = n (if tree is in shape of a line)
     */
    fun efficient(root: TreeNode, queries: List<Int>): List<List<Int>> {
        val sortedValues = mutableListOf<Int>().apply { buildSortedValuesList(root = root, sortedValues = this) }
        return mutableListOf<List<Int>>().apply {
            queries.forEach { query -> add(findClosestValues(query = query, sortedValues = sortedValues)) }
        }
    }

    /**
     * in-order DFS, for [root] is a valid BST (and may be unbalanced)
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     */
    private fun buildSortedValuesList(
        root: TreeNode?,
        sortedValues: MutableList<Int>,
    ) {
        if (root == null) return

        root.left?.let { buildSortedValuesList(root = it, sortedValues) }
        sortedValues.add(root.`val`)
        root.right?.let { buildSortedValuesList(root = it, sortedValues) }
    }

    /**
     * 1 2 4 6 9
     *
     * query = 3
     *
     * l=0 r=4 -> mI=2, mN=4
     * 3 < 4 -> r=mI-1=1
     *
     * l=0 r=1 -> mI=0, mN=1
     * 3 > 1 -> l=mI+1=1
     *
     * l=1 r=1 -> mI=1, mN=2
     * 3 > 2 -> l=2
     *
     * => r points at below, r+1 above
     *
     * ----
     *
     * query = 0
     *
     * l=0 r=4 -> mI=2
     * 0 < 4 -> r=mI-1=1
     *
     * l=0 r=1 -> mI=0
     * 0 < 1 -> r=mI-1=-1
     *
     * => r points at below (doesn't exist -1), r+1 above
     *
     * ----
     *
     * query = 12
     *
     * l=0 r=4 -> mI=2
     * 12 > 4 -> l=3
     *
     * l=3 r=4 -> mI=3
     * 12 > 6 -> l=4
     *
     * l=4 r=4 -> mI=4
     * 12 > 9 -> l=5
     *
     * => r points at below (doesn't exist -1), r+1 above
     *
     * Time: average/worst O(logn)
     * Space: always O(1)
     */
    private fun findClosestValues(query: Int, sortedValues: MutableList<Int>): List<Int> {
        var left = 0
        var right = sortedValues.lastIndex
        while (left <= right) {
            val middle = left + (right - left) / 2
            val middleNum = sortedValues[middle]
            when {
                query == middleNum -> return listOf(query, query)
                query < middleNum -> right = middle - 1
                else -> left = middle + 1
            }
        }
        return listOf(
            if (right != -1) sortedValues[right] else DEFAULT_VAL,
            if (right != sortedValues.lastIndex) sortedValues[right + 1] else DEFAULT_VAL,
        )
    }

    /* WRONG: binary search on the tree itself => TLE 34th out of 35 tests */

    /**
     * problem rephrase:
     *  basically, for each number, find a pair:
     *   - the closest number from below to it (equal is valid);
     *   - the closest number from above to it (equal is valid).
     *  or -1 for either if such a number doesn't exist
     *
     * observation: we could launch 2 DFS for both target numbers, but probably we could optimize the const and find
     *  both numbers in ~1 DFS.
     *
     * are node values in the BST unique or not? if not, what is the rule for storing equal values - only left/only right or how?
     *
     * 2 major cases, the queries\[i] number:
     *  1. is present in the BST => simply set both target values to it;
     *  2. isn't present:
     *   - suppose query=5 in the original example
     *    we end up at the node.val=4 < 5 => thats the value from below, and the value from above is the first value
     *    that is greater than 5 as we backtrack;
     *   - suppose query=7:
     *    we end up at node.val=9 > 7 => thats the value from above, and the value from above is the first value that is
     *     LESS than 7 as we backtrack => 6.
     *   - suppose query=0:
     *    end up at node=1, value from above, and we'll find no value from below as we backtrack => edge case, write -1;
     *   - suppose query = 16:
     *    end up at node=15, value from below, and we'll find no value from above as we backtrack.
     *
     *
     * Edge cases:
     *  - number of nodes == 2 => correct as-is.
     *     1
     *      \
     *       10
     *
     *  - all node values in the tree are equal => doesn't make a difference, correct as-is as we'd traverse still in the correct direction,
     *   and it would neither impact above nor below (and if it == query logic is correct too).
     *
     * Time: average/worst O(m * n), since worst m==n => average/worst O(n^2)
     *  ⚠️ first assumed that average/worst is O(m * logn), m=queries.size, n=nodes amount in the tree
     *   => WRONG, worst BST case if its not balanced is when the tree is a shape of a line (still a valid BST though)
     *    and the target value to search for is larger than all values in the tree => each iteration we take exactly one
     *    step and we'll end up checking all nodes => O(n)!
     *  =>
     *   - best O(1) (query the root itself)
     *   - if tree is balanced => average/worst O(logn), since BS on a balanced BST is guaranteed to be worst O(logn) time;
     *   - if tree is not balanced => average/worst O(n), since worst is O(n) for tree in form of a line.
     * Space: if we don't count the result, then O(height of the tree) (with map optimization its actually O(height + m)
     *
     * ------------------------
     *
     * TLE 34th/35 test case - why? A bug / how can we optimize?
     *
     * I doubt that's a bug in the logic, apparently its just not fast enough.
     *
     * A few obvious optimizations:
     *  1. return IntArray straight out of [dfs] to reduce the time const, minimize objects creation and GC;
     *   => still TLE 34th/35
     *
     *  2. since [queries] aren't guaranteed to be distinct, we may store answers in the map as we go and, if we've seen
     *   such a query before, pop out the cached answer => worst time still the same, but time const in some cases would be less
     *   (at the cost of the space const)
     *   => still TLE 34th/35
     *
     * => rn don't know where to look next. Already 35 mins => at least check hints.
     *
     */
    fun wrongTLE(root: TreeNode, queries: List<Int>): List<List<Int>> {
        val answers = mutableListOf<List<Int>>()
        val answersMap = mutableMapOf<Int, List<Int>>()
        queries.forEach { query ->
            answers.add(answersMap[query] ?: dfs(root = root, query = query).apply { answersMap[query] = this })
        }
        return answers
    }

    private fun dfs(
        root: TreeNode,
        query: Int,
    ): MutableList<Int> {
        val value = root.`val`
        if (value == query) return mutableListOf(query, query)

        return if (query < value) {
            if (root.left != null) {
                val answer = dfs(root = root.left!!, query = query)
                if (answer.onlyBelow()) answer.apply { set(1, value) } else answer
            } else {
                mutableListOf(DEFAULT_VAL, value)
            }
        } else {
            if (root.right != null) {
                val answer = dfs(root = root.right!!, query = query)
                if (answer.onlyAbove()) answer.apply { set(0, value) } else answer
            } else {
                mutableListOf(value, DEFAULT_VAL)
            }
        }
    }

    private fun List<Int>.onlyBelow() = get(0) != DEFAULT_VAL && get(1) == DEFAULT_VAL

    private fun List<Int>.onlyAbove() = get(1) != DEFAULT_VAL && get(0) == DEFAULT_VAL
}

private const val DEFAULT_VAL = -1

fun main() {
    println(
        ClosestNodesQueriesInABinarySearchTree().efficient(
            root = IntBinaryTreeNode.treeFrom(arrayOf(16, 14, null, 4, 15, 1))!!,
            queries = listOf(2),
        )
    )
}
