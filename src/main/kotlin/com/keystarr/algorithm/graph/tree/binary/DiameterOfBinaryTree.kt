package com.keystarr.algorithm.graph.tree.binary

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode
import kotlin.math.max

/**
 * LC-543 https://leetcode.com/problems/diameter-of-binary-tree/description/
 * difficulty: easy (optimal - medium?)
 * constraints:
 *  • 1 <= number of nodes <= 10^4;
 *  • -100 <= node.value <= 100;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • immediately caught the case of the diameter not passing through the root;
 *  • for some reason though that pre-order would be the best approach, tried it => that lead to nowhere, so I abandoned
 *      the idea entirely after (I guess, didn't mark it) 15-25 mins;
 *  • from scratch tried a post-order and implemented [diameterBrute], totalling at about 45 mins;
 *  • improved to [diameterEfficient], totalling at about 1 hour;
 *  • some people in the comments also voice that O(n) time solution is more like a leet-medium!
 *
 * Value gained:
 *  • optimal solution for problems marked leet-easy problems is indeed sometimes a leet-medium, we've had that before;
 *  • despite that, technically, [diameterEfficientClassFieldRecursive] has a better both time and memory const than
 *   [diameterEfficientCleanerRecursive], I would prefer the latter in production unless it was for a performance critical
 *   section. For regular features that's a much cleaner code and maintenance benefit far outweighs the performance benefit
 *   for regular cases here, imho;
 *  • perhaps, it's ok to try some traversal order first, but the goal is then to asap realize if it's the best fit or not,
 *    and switch asap to save time. With practice, I predict that the probability of me choosing the best traversal method
 *    will increase. Here I spent a lotta time on pre-order when post-order turned out to be the best;
 *  • funny how I correctly identified 2 sub-problems, solved both via separate recursive algorithms [diameterBrute] with
 *    time O(n^2), and then combined both into a single recursive algorithm with time O(n) pretty quickly (5-15 mins).
 *    Perhaps, it's ok then during interviews to first come up with brute force and then optimize it -
 *    =>
 *    • MAJOR INSIGHT: I TAKE IT (as we've had that before), SOMETIMES THE EFFICIENT SOLUTION IS THE OPTIMIZATION
 *    OF THE BRUTE FORCE'S SINGLE LOOP/RECURSIVE CALL BY A FACTOR OF X (often x=n, so, say, n^2 becomes n).
 *    • but sometimes, afaik, the efficient solution is a completely different approach from the brute force;
 *    =>
 *    I think THE BEST STRATEGY would be to try to come up with the efficient approach straight ahead in, like, 10 mins,
 *    if I don't see "the light at the end of the tunnel" and interviewer seems stiff
 *      => try to do brute first, and then optimize it (or think of a different way altogether if efficient is different;
 *
 * BEST Value of above summary:
 *  • probably the best problem-solving strategy for interviews:
 *      • ask desired time/complexity, IF MOST EFFICIENT, aim for the efficient solution straight ahead;
 *      • if under 10-15 mins I don't feel that an efficient solution is close, abandon and first try to implement brute-force
 *          (perhaps, ask interview if it's ok, maybe he'd better hint at efficient);
 *      • when done brute, try to optimize it, cause sometimes the efficient will be just reducing the time of the inner iteration
 *          / recursive call (like in this problem). If not, try a different solution altogether, but you've already at least
 *          done something, that's better than no solution at all, right?
 */
class DiameterOfBinaryTree {

    /**
     * Goal - find the longest path between two nodes in a tree, return its length.
     * Path's length - the number of edges between start and end nodes (amount of nodes in the path - 1)
     *
     * Two general cases:
     *  - the diameter passes through the root;
     *  - the diameter doesn't pass through the root.
     *
     * Intuition - traverse from leaves, goal - for each subtree find the path with the maximum edges in it.
     *
     * Observation - at either node, if does have a parent, we must choose only one child to add to the path.
     *  => the answer would be to find the longest path TO THE LEAVES (the longest branch) in the left subtree
     *  of the original root, and same for the right subtree, and choose the max, return it + 1 (for edge from root
     *  to the child).
     * => 3 sub-problems:
     *  - if (root.left == null && root.right == null) return 0
     *  - find the longest BRANCH in the left subtree rooted at original:
     *      - root.left;
     *      - root.right.
     *  - return leftSubtreeMaxPath + rightSubtreeMaxPath + 1.
     *
     * Time: O(n)
     * Space: O(n)
     *
     * how to find the longest BRANCH in the tree (counting edges)? depth = DFS
     * i.o. find the height of the tree
     *  - base case:
     *      - root == null, return 0 (non-existent subtree, no edges to children, useful for when parent has 1 child only,
     *          another falls here);
     *       - a leaf, return 0 for the leaf (it has no edges to children);
     *  - recursive case:
     *      - val leftSubtreeMaxPath = findLongestPath(root.left)
     *      - val rightSubtreeMaxPath = findLongestPath(root.right)
     *      // plus one for the edge to the child
     *      // max cause we choose only one path, either to left or to right;
     *      - return max(leftSubtreeMaxPath, rightSubtreeMaxPath) + 1
     * Time: O(n), where n=number of nodes in the subtree, we visit each node once
     * Space: O(n), basically the max length of a branch of the subtree (subtree's height)
     *
     * Edge cases:
     *  treeDiameter()
     *      - number of nodes == 1 => early return 0, correct;
     *  treeHeight()
     *      - number of nodes == 1 => base case, 0, correct;
     *      - root == null => base case, 0, correct.
     *
     * Time: O(n^2) cause we compute the subtree height rooted at each node, which takes O(n) time
     * Space: O(n)
     */

    fun diameterBrute(root: IntBinaryTreeNode?): Int {
        if (root?.left == null && root?.right == null) return 0 // number of nodes either 1 (root is a leaf) or 0

        val diameterLeft = diameterBrute(root.left)
        val diameterRight = diameterBrute(root.right)

        val childrenEdges = if (root.left == null || root.right == null) 1 else 2

        val leftSubtreeHeight = treeHeight(root.left)
        val rightSubtreeHeight = treeHeight(root.right)
        val currentDiameter = leftSubtreeHeight + rightSubtreeHeight + childrenEdges

        return max(max(diameterLeft, diameterRight), currentDiameter)
    }

    private fun treeHeight(root: IntBinaryTreeNode?): Int {
        if (root?.left == null && root?.right == null) return 0

        val leftSubtreeHeight = treeHeight(root.left)
        val rightSubtreeHeight = treeHeight(root.right)
        return max(leftSubtreeHeight, rightSubtreeHeight) + 1
    }

    /**
     * Idea: same idea as [diameterBrute] but for each subtree [root] compute both diameter and height in a single
     * call => reduce tree height computation complexity to O(1) (basically when backtracking just reuse the result of
     * the height computation from the children, don't recompute from scratch)
     *
     * Time: always O(n), cause we visit each node once, and to handle 1 node time is O(1)
     * Space: always O(n), the height of tree rooted at [root].
     */
    fun diameterEfficient(root: IntBinaryTreeNode?): Int = diameterEfficientCleanerRecursive(root).diameter

    private fun diameterEfficientRecursive(root: IntBinaryTreeNode?): Result {
        // number of nodes either 1 (root is a leaf) or 0
        if (root?.left == null && root?.right == null) return Result(height = 0, diameter = 0)

        val (leftHeight, leftDiameter) = diameterEfficientRecursive(root.left)
        val (rightHeight, rightDiameter) = diameterEfficientRecursive(root.right)

        val childrenEdges = if (root.left == null || root.right == null) 1 else 2
        val currentDiameter = leftHeight + rightHeight + childrenEdges
        val maxDiameter = max(max(leftDiameter, rightDiameter), currentDiameter)

        val currentHeight = max(leftHeight, rightHeight) + 1
        return Result(height = currentHeight, diameter = maxDiameter)
    }

    /**
     * Same core idea and complexities as [diameterEfficient] but:
     *  - define "height of the tree" not through the number of edges but the number of NODES (+1);
     *  - ONCE AGAIN (we've had that before!) handle only a single base case of non-existent [root], don't make
     *   leaf a base case! it is automatically correctly handled by the logic
     *   => the leaf base case IS REDUNDANT, simply remove it.
     */
    private fun diameterEfficientCleanerRecursive(root: IntBinaryTreeNode?): Result {
        if (root == null) return Result(height = 0, diameter = 0)

        val (leftHeight, leftDiameter) = diameterEfficientCleanerRecursive(root.left)
        val (rightHeight, rightDiameter) = diameterEfficientCleanerRecursive(root.right)

        val currentDiameter = leftHeight + rightHeight
        val maxDiameter = max(max(leftDiameter, rightDiameter), currentDiameter)

        val currentHeight = max(leftHeight, rightHeight) + 1
        return Result(height = currentHeight, diameter = maxDiameter)
    }

    data class Result(
        val height: Int,
        val diameter: Int,
    )

    // nasty code style, but it's convenient here
    private var maxDiameter: Int = -1

    fun diameterEfficientClassField(root: IntBinaryTreeNode?): Int =
        diameterEfficientClassFieldRecursive(root).let { maxDiameter }

    /**
     * Same as [diameterEfficientCleanerRecursive] but try, for learning, make the function dirty (but avoid creating objects
     * of [Result] each call!) and declare maxDiameter as a class field.
     *
     * Function goal - still determine the diameter of the original [root] BUT the answer is in [maxDiameter] this time,
     * and the return value is an intermediate internal value (height of the tree).
     */
    private fun diameterEfficientClassFieldRecursive(root: IntBinaryTreeNode?): Int {
        if (root == null) return 0

        val leftHeight = diameterEfficientClassFieldRecursive(root.left)
        val rightHeight = diameterEfficientClassFieldRecursive(root.right)

        val currentDiameter = leftHeight + rightHeight
        val currentHeight = max(leftHeight, rightHeight) + 1

        maxDiameter = max(maxDiameter, currentDiameter)
        return currentHeight
    }
}

fun main() {
    println(
        DiameterOfBinaryTree().diameterEfficient(
            IntBinaryTreeNode(
                `val` = 2,
                left = IntBinaryTreeNode(
                    `val` = 1,
                    left = IntBinaryTreeNode(3),
                ),
                right = IntBinaryTreeNode(
                    `val` = 4,
                    left = IntBinaryTreeNode(5),
                )
            )
        )
    )
}

// initial pre-order approach
//
//* DFS pre-order, recursive:
//*  - base case:
//*      - root == null => return 0
//*      - if (root.left == null && root.right == null) => return 0
//*  - recursive case:
//*      - leftSubtreePathMaxEdges = postOrderDiameter(root.left)
//*      - rightSubtreePathMaxEdges = postOrderDiameter(root.right)
//*      - childrenEdges = if (root.left == null || root.right == null) 1 else 2
//*      - leftSubtreeMaxEdges + rightSubtreeMaxEdges + childrenEdges // for the edge from current root to either child
//* DFS post-order, recursive:
//*  - base case:
//*      - root == null => return 0
//*      - if (root.left == null && root.right == null) => return 0
//*  - recursive case:
//*      - leftSubtreePathMaxEdges = postOrderDiameter(root.left)
//*      - rightSubtreePathMaxEdges = postOrderDiameter(root.right)
//*      - childrenEdges = if (root.left == null || root.right == null) 1 else 2
//*      - leftSubtreeMaxEdges + rightSubtreeMaxEdges + childrenEdges // for the edge from current root to either child
//*
//* Edge case:
//*  - don't count the original root's edge? it doesnt have a parent

