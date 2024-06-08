package com.keystarr.algorithm.graph.tree.binary

/**
 * LC-100 https://leetcode.com/problems/same-tree/description/
 * difficulty: easy
 * constraints:
 *  • 0 <= number of nodes in both trees <= 100;
 *  • -10^4 <= node.value <= 10^4;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • FAILED the 1st run BECAUSE FORGOT THAT [IntBinaryTreeNode] (which I basically remove to use leetcode's required in-built
 *      node DS) DOESN'T OVERRIDE EQUALS => compares by identity!
 *
 * Value gained:
 *  • when comparing equality ALWAYS CHECK EQUALS THE IMPLEMENTATION!!! Already failed the same way in LinkedList problems.
 *      Basically for trees/linkedlist problems it seems BY DEFAULT EQUALITY IS ALWAYS;
 *      (made iterative in a hurry AND LITERALLY made the same mistake again :D)
 *  • practiced re-implementing recursion into iteration. It seems there is always just no backtracking, right? But the
 *      order is the same, and we use a stack. Is it always the way for tree problems?
 */
class SameTree {

    /**
     * idea - traverse both trees at the same time:
     *  - base case #1: first currentNode == null && secondCurrentNode == null => return true
     *      node after leaf in both
     *  - base case #2: if first current node != second current node => return false;
     *      either two different node at the same position, or one node and another doesn't exist (null)
     *  - check left subtree, if it returns false => return false straight away;
     *  - check right subtree, if it returns false => return false straight away;
     *  - if we made it so far, return true.
     *
     * Edge cases:
     *  - number of nodes in both trees == 0 => true, correct;
     *  - number of nodes in both trees == 1 (only roots) => return true if roots are equal, correct;
     *  - same position, one node exists and another one doesn't (null, after leaf) => false, handled via base case #2.
     *
     * Intuition - [isSameTreeRecursive] will return whether subtrees rooted at [first] and [second] are equal or not.
     *
     * Time: O(n), where n=number of nodes in either tree
     * Space: O(n) due to callstack
     */
    fun isSameTreeRecursive(first: IntBinaryTreeNode?, second: IntBinaryTreeNode?): Boolean {
        if (first == null && second == null) return true // both are a non-existent node after leaf
        if (first?.value != second?.value) return false

        if (!isSameTreeRecursive(first?.left, second?.left)) return false
        if (!isSameTreeRecursive(first?.right, second?.right)) return false

        return true
    }

    /**
     * Same core idea, same complexities, same order of visiting but no backtracking (as usual with iterative re-implementation).
     */
    fun isSameTreeIterative(firstRoot: IntBinaryTreeNode?, secondRoot: IntBinaryTreeNode?): Boolean {
        val stack = ArrayDeque<Pair<IntBinaryTreeNode?, IntBinaryTreeNode?>>()
        stack.addLast(firstRoot to secondRoot)
        while (stack.isNotEmpty()) {
            val (first, second) = stack.removeLast()

            // base cases
            if (first == null && second == null) continue // each is a non-existent node after a leaf
            if (first?.value != second?.value) return false

            // subdivision case?
            stack.addLast(first!!.right to second!!.right)
            stack.addLast(first.left to second.left)
        }
        return true
    }
}

fun main() {
    println(
        SameTree().isSameTreeRecursive(
            first = IntBinaryTreeNode(
                value = 1,
                left = IntBinaryTreeNode(value = 2),
                right = IntBinaryTreeNode(value = 3),
            ),
            second = IntBinaryTreeNode(
                value = 1,
                left = IntBinaryTreeNode(value = 2),
                right = IntBinaryTreeNode(value = 3),
            ),
        )
    )
}
