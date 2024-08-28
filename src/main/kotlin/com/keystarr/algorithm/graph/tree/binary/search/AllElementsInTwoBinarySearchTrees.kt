package com.keystarr.algorithm.graph.tree.binary.search

import com.keystarr.algorithm.graph.tree.binary.IntBinaryTreeNode

/**
 * 💣 done a passable efficient solution, but haven't figured out how to do the clean efficient one => optionally repeat later
 * LC-1305 https://leetcode.com/problems/all-elements-in-two-binary-search-trees/description/
 * difficulty: medium
 * constraints:
 *  • 0 <= number of nodes <= 5000
 *  • -10^5 <= node.value <= 10^5
 *
 * Final notes:
 *  • 🎉 done [twoDfsAndMerge] by myself in 20 mins;
 *  • I remembered the BST sorted property with in-order DFS, formulated the core idea to traverse both trees at the
 *   same time, but... failed to see how to implement it quickly. Then an idea appeared (literally unconsciously) that I
 *   could try traversing both trees separately and then merge the lists.
 *   I jumped on it, since I couldn't see quickly how to implement the 1st approach
 *   => still got the same time/space complexity, but quickly. Even though the 1st approach is probably easier to implement/maintain;
 *   🏅still, I consider this move a success!
 *  • ⚠️ I predict that in a real interview, unless we'd be running out of time, or I'd struggle for a long time, the interviewer
 *   would acknowledge but reject the [twoDfsAndMerge] approach and ask for that "one" pass solution [onePassTwoStacks]. And I couldn't
 *   do it straight away (though I stared at it for, like, 5 mins lol before going into [twoDfsAndMerge].
 *  • 🔥nevermind, maybe it's just me, but [twoDfsAndMerge] is so much cleaner to understand and easier to implement even
 *   during an interview, with worse const but same complexities asymptotically. Is it coz I've practiced merging two sorted
 *   lists in O(n) before? And haven't done iterative in-order DFS on a binary tree ever before?
 *   I guess yea. But I think, without specific constraints from the interviewer, 💡 it's always better to go with a
 *   better known tools, as long as it is efficient asymptotically and is not that more verbose/breakable than the more
 *   optimal one const-wise.
 *
 * Value gained:
 *  • practiced applying the BST sorted in-order DFS traversal property to efficiently solve a two BSTs problem;
 *  • for the first time ever learned in-order ITERATIVE binary tree traversal, first in isolation [com.keystarr.algorithm.graph.tree.binary.search.BinaryTreeInorderTraversal],
 *   then applied it here.
 */
class AllElementsInTwoBinarySearchTrees {

    // TODO: repeat [onePassTwoStacks] in a 1-2 weeks

    // TODO: optionally, understand the cleaner version of 2 stacks 1 pass
    //  https://leetcode.com/problems/all-elements-in-two-binary-search-trees/solutions/464073/c-one-pass-traversal/

    /**
     * Additional constraint - a pass through two trees at once => in-traversal adding to results, no merge at the end.
     *
     * Core = iterative binary tree DFS, basically same from [com.keystarr.algorithm.graph.tree.binary.search.BinaryTreeInorderTraversal].
     * The rest in general is exactly how I laid out in [twoDfsAndMerge], but couldn't implement quickly due to never having
     * implemented iterative in-order dfs on a binary tree before. And now I have, so:
     *  - initialize both currentRoot1 and currentRoot2 with corresponding tree roots;
     *  - initially, as usual with iterative in-order DFS on a BT: go through the left nodes until a node after a leaf
     *   for both trees, add all nodes encountered into the stack1 and stack2;
     *  - if stack1.top() isnt empty and has a value less than stack2.top():
     *   - currentRoot1 = stack1.remove()
     *   - values.add(currentRoot1.val)
     *   - currentRoot1=currentRoot1.right
     *   as we normally do with in-order DFS, once we've visited a node, go to its right subtree
     *   the second tree's currentRoot2 then is still == null and stack2.top() is the leftmost node in its current undiscovered
     *   subtree or null if we've checked all nodes in tree1;
     *  - else same but for the second tree
     *   then the first tree's currentRoot1 is always == null, and the top of its stack is the leftmost node in its current
     *   undiscovered subtree, or null if we've checked all nodes in tree2.
     *
     * Edge cases:
     *  - one tree has more nodes than the other => handled via the isEmpty() checks in the outer while loop and
     *   the inner elements comparison conditions.
     *
     * Time: always O(n+m)
     * Space: average/worst O(n+m)
     *
     * Kudos to https://leetcode.com/problems/all-elements-in-two-binary-search-trees/solutions/1719941/c-best-explanation-naive-and-optimal/
     * for the reference for this solution.
     */
    fun onePassTwoStacks(root1: IntBinaryTreeNode?, root2: IntBinaryTreeNode?): List<Int> {
        val stack1 = ArrayDeque<IntBinaryTreeNode>()
        val stack2 = ArrayDeque<IntBinaryTreeNode>()
        var currentRoot1 = root1
        var currentRoot2 = root2
        val sortedNums = mutableListOf<Int>()
        while (currentRoot1 != null || stack1.isNotEmpty() || currentRoot2 != null || stack2.isNotEmpty()) {
            while (currentRoot1 != null) {
                stack1.add(currentRoot1)
                currentRoot1 = currentRoot1.left
            }
            while (currentRoot2 != null) {
                stack2.add(currentRoot2)
                currentRoot2 = currentRoot2.left
            }
            // we always have at least 1 node in at least 1 stack here:
            // - the outer loop termination condition guarantees than we have at least 1 node left in either stacks, or root vars;
            // - if both stacks were empty, then either root was at least not null => after 2 whiles, at least one stack got a new node in.
            if (stack2.isEmpty() || (stack1.isNotEmpty() && stack1.last().`val` <= stack2.last().`val`)) {
                currentRoot1 = stack1.removeLast()
                sortedNums.add(currentRoot1.`val`)
                currentRoot1 = currentRoot1.right
            } else {
                currentRoot2 = stack2.removeLast()
                sortedNums.add(currentRoot2.`val`)
                currentRoot2 = currentRoot2.right
            }
        }
        return sortedNums
    }

    /**
     * Binary Search Tree => in-order DFS traversal gives us a sorted ascending visiting order (by node.value)
     * +
     * Goal - return a list with all ints from both trees sorted ascending
     * =>
     * visit both trees DFS in-order, at each step:
     *  if both trees have values => take the lesser value of both trees, and progress only that tree.
     *  else => take the single tree's value and progress it.
     *
     * ------
     *
     * perhaps a bigger const but simpler to implement?
     * - traverse the first tree in-order => get all its elements sorted asc O(n)
     * - traverse the second tree in-order => get all its elements sorted asc O(n)
     * => merge the two lists O(n)
     *
     * edge cases:
     *  - both trees are empty => merge sort takes care of that.
     *
     * Time: always O(n+m)
     *  - first tree dfs O(n), second tree dfs O(m)
     *  - merge sort O(n+m)
     * Space: always O(n+m)
     *  - first tree max callstack height, worst O(n)
     *  - second tree max callstack height, worst O(m)
     *  - first tree number O(n), second O(m)
     *  - results O(n+m)
     *
     * may improve time const, but won't improve time asymptotically, since we need to consider each element of both
     * trees at least once to put it into the resulting list
     */
    fun twoDfsAndMerge(firstRoot: IntBinaryTreeNode?, secondRoot: IntBinaryTreeNode?): List<Int> {
        val firstNumbers = getSortedNumbers(firstRoot)
        val secondNumbers = getSortedNumbers(secondRoot)
        return firstNumbers.merge(secondNumbers)
    }

    private fun getSortedNumbers(root: IntBinaryTreeNode?) = mutableListOf<Int>().apply {
        dfs(root = root, sortedNumbers = this)
    }

    private fun dfs(
        root: IntBinaryTreeNode?,
        sortedNumbers: MutableList<Int>
    ) {
        if (root == null) return

        dfs(root.left, sortedNumbers)
        sortedNumbers.add(root.`val`)
        dfs(root.right, sortedNumbers)
    }

    // there probably is a stdlib tool for that, but imagined the interviewer asked me to implement it myself
    // and in general for merge sort practice, haven't done it in a while
    private fun List<Int>.merge(other: List<Int>): List<Int> {
        val result = mutableListOf<Int>()
        var ind = 0
        var otherInd = 0
        while (ind < size && otherInd < other.size) {
            val first = this[ind]
            val second = other[otherInd]
            result.add(
                if (first <= second) {
                    ind++
                    first
                } else {
                    otherInd++
                    second
                }
            )
        }
        while (ind < size) result.add(this[ind++])
        while (otherInd < other.size) result.add(other[otherInd++])
        return result
    }
}
