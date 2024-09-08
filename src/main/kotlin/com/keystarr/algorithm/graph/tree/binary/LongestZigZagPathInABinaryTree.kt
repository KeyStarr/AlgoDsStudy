package com.keystarr.algorithm.graph.tree.binary

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode
import kotlin.math.max

/**
 * ⭐️💣 retry full, recognized tools ok (except when went for DP), but completely failed to design the solution with it.
 * a great example of a tricky binary tree problem
 *
 * LC-1372 https://leetcode.com/problems/longest-zigzag-path-in-a-binary-tree/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= number of nodes <= 5 * 10^4;
 *  • 1 <= node.value <= 100.
 *
 * Final notes:
 *  • at first thought it's a pure DFS problem. Tried to come up with a solution in multiple ways, failed cause couldn't
 *   quite pin down the correct recurrence relation, as soon as I've modelled beyond root, I either got wrong result or
 *   have missed some valid path;
 *  • went to do other stuff, realized that it seems like DP, cause if we model recurrence relation the way I did in [wrong]
 *   => at least 2 subproblems overlap at each node as root if it has children;
 *  • tried DP, invested in total 2h, but failed on 17th test case;
 *  • 🔥⚠️ the longest time (4h) I've ever spent to understand the solution to an algorithmic problem. A great learning opportunity.
 *   For some reason I felt my brain crashing when I tried to jiggle all this left-right zigZag directions. It felt like
 *   I was trying to solve a problem by designing in by looking in the mirror to what I was drawing;
 *  • read 2 solutions, didn't understand why they work. Watched a youtube explanation, didn't understand it either.
 *   Stumbled by chance to an obscure solution different from top ones, and finally understood it by extensive dry-running!
 *  • [wrong] is obviously redundant, cause it violates the optimization principle laid out in [efficient]. But for some
 *   reason if we adjust it to that principle, it still fails. Still don't understand why;
 *  • 💡key mistake I've made: I fixated upon the bottom-up solution, i.o. counting the path edges from the leaves, and
 *   completely missed the idea that it's mich easier to count top to bottom, cause we might zero out the edges count,
 *   on the way down! Should've at least tried to count edges for the simple DFS top-bottom when failed to see a bottom-top
 *   solution;
 *  • bottom-up was incorrect cause the max path length could've been on a path start not at root, but as we accumulated
 *   the answer to top, we lost the information of whether it was a part of the zigzag path or not (returned just Int)
 *   => might've added +1 edges on the zigZag path to it, which was not the part of that max path.
 *    Later I've returned Pair<Int,Boolean> where Boolean was whether the answer is a part of zig zag path or not,
 *     but that didn't work also! But that still wasn't correct, and I still don't understand why.
 *
 * Value gained:
 *  • encountered a meager medium problem which blew my mind the hardest, ever so far. Kept composure and kept trying,
 *   eventually after a great deal of effort and constantly trying new approaches understood a solution => a great example
 *   of "smart" perseverance;
 *  • learned that it might be useful to actually consider both bottom-up and top-down DFS answer accumulation, if I can't
 *   make the first chosen approach to work.
 */
class LongestZigZagPathInABinaryTree {

    // TODO: retry fully after a week or two
    //  (optionally try to understand why optimized [wrong] is incorrect)

    /**
     * Key principle: we count the amount of edges on both the path in which a node is part of the valid zigZag path,
     *  and when we start a path from it.
     *
     * We have only 2 variables, because each node except root is always a part of the zigZag path, where its parent
     *  was visited with the direction opposite to that node's position relative to its parent => that path has always
     *  at least 1 edge (from parent to the child node) => we need to only try each node on 2 paths:
     *   - a valid zigZag path containing this node;
     *   - starting the path at that node, but with a direction only opposite to the direction of the zigZag path
     *    (cause same direction will always give a lesser answer).
     *
     * Design:
     *  1. if we choose root.left child, then the edges count on the zigZag path is the max of:
     *   A. 1 + the so far number of edges on the path, where root was the right child (to satisfy the zigzag constraint);
     *   B. or we can start the zigzag path at root.left node:
     *    we don't need to check it with the same direction as A,
     *     cause such zigzag path which started before or node's parent (A) will always have at least 1 edge more, and we
     *     maximize edges;
     *    => we only try to start the path at root.left with the direction opposite to its position in its parent subtree.
     *
     * Time: always O(nodes + edges)
     * Space: average/worst O(n)
     *  worst callstack height is n where the tree is in form of a line.
     */
    fun efficient(root: IntBinaryTreeNode?): Int = wrongDfs(root = root, leftChild = 0, rightChild = 0)

    /**
     * Goal - return the length of the longest zigZag path in the tree rooted at [root].
     *
     * dfs(root = root.left, leftChild = rightChild + 1, rightChild = 0)
     *  - the child is left relative to root, so it is a part of a valid zigzag path which includes the root which was
     *   traversed to as a right child of its parent => add the number of edges so far on that path + 1 for that new edge
     *   we've used to traverse to root.left;
     *  - the child is left => there is no valid zigzag path, such that the child was traversed to as the right child.
     *
     * dfs(root = root.right, leftChild = 0, rightChild = leftChild + 1)
     * same idea here
     *
     * @param leftChild - the number of edges on the valid zigZag path such that [root] was the left child of its parent;
     * @param rightChild -the number of edges on the valid zigZag path such that [root] was the right child of its parent.
     */
    private fun wrongDfs(root: IntBinaryTreeNode?, leftChild: Int, rightChild: Int): Int =
        if (root == null) {
            // -1, cause we added 1 edge on the zigzag path when we traversed to this non-existent node
            // the zigZag path is either leftChild or rightChild, and it is always at least 1, and the other one is always 0
            max(leftChild, rightChild) - 1
        } else {
            max(
                wrongDfs(root = root.left, leftChild = rightChild + 1, rightChild = 0),
                wrongDfs(root = root.right, leftChild = 0, rightChild = leftChild + 1),
            )
        }

    /**
     * TLE on 51st/58 test
     *
     * -------------------------
     *
     * problem rephrase:
     *  - given is a binary tree defined via [root];
     *  - traversal rule: start at any node, choose initial direction either left or right, and every 1 node alternate the direction;
     *  - goal: return edges count along the best (longest) valid path.
     *
     * we can start at any node, but since we maximize the number of edges along the path + we have a binary tree =>
     * it's always better to start at the root, cause there's a path from root to every node, and root is the lowest
     * node = max amount of edges to any other node as a starting point (if we move only down, as we do here)
     * WRONG
     * if we start only at root, we don't have access to all paths due to the nature of zigzags. To try all paths we have to
     * also consider starting at other nodes than root.
     *
     * A simple DFS would do:
     * - choose root as the starting point;
     * - dfs takes as input:
     *  - node: TreeNode?
     *  - edgesCount: Int
     * - we make 2 calls to dfs: max(dfs(node=root,isLeft=true,edges=0), dfs(node=root,isLeft=false))
     * - if edgesCount
     *
     * ---------------------
     *
     * to try all paths efficiently we'd have to do a DFS like that:
     *  dfs(root, goLeft=true), which would look like:
     *   - max(dfs(root.left, goLeft=false) + 1, max(dfs(root.right,goLeft=true),dfs(root.right,goLeft=false)))
     *  dfs(root, isLeft=false):
     *   - max(dfs(root.right, goLeft=true) + 1, max(dfs(root.left,goLeft=true),dfs(root.left,goLeft=false)))
     *
     *
     *  i.o. we try to find max path from root across both options from it going left or going right. And from root
     *   if the child's direction matches the argument direction we add the max path using that child launched in the direction
     *   +1 for the edge from root to that child, cause then its the path that start at root.
     *   Also, we consider the right child, we can't use it in the current zigzag path, but we can start a zigzag path from there,
     *    and also try it from both launching it with left and right directions, same as root.
     *   => one can clearly see that we have 2 calls to dfs(root.right,goLeft=true), and 2 calls to dfs(root.left,goLeft=false)
     *   => suproblems depend on the solutions of other suproblems, which sometimes overlap
     *   => DP
     *
     * - dp function goal:
     *  - find the longest zigzag path from root, given the direction isLeft;
     *  - return value the length of the path, Int
     * - input state:
     *  - node: Int
     *  - goLeft: Boolean
     * - recurrence relation, try top-down first cause its usually easier (loops configuration for inner state vars):
     *  dp(node, goLeft):
     *   zigZagChild=if(goLeft) root.left else root.right
     *   otherChild= if(goLeft) root.right else root.left
     *   return max(dfs(zigZagChild, goLeft=!goLeft) + 1, max(dfs(otherChild,goLeft=true),dfs(otherChild,goLeft=false)))
     * - memoization:
     *  we have 5*10^4*2=10^5 states => it's not efficient to use an array cause we don't know the amount of nodes => use a
     *  map node->State, where Results(goLeftMaxPath: Int, goRightMaxPath: Int)
     * - base cases:
     *  root.left == null && root.right == null => no edges use to, return 0.
     *
     * HOW TO IDENTIFY THE NODE?!??!?!?!?!?!?!?!?!? values are not unique!!!!
     *
     *
     * Edge cases:
     *  - number of nodes == 1 => always return 0, dp base case, correct.
     *
     * Time: always O(nodes+edges)
     *  - basic dfs is O(nodes+edges)
     *  - here we might visit the same node at most 2 times => O(2*(nodes+edges)
     *   why?
     * Space: always O(nodes)
     *  - dp callstack height = binary tree height = O(k) but k depends on n so worst/average O(n) (worst is n, if a tree is a line);
     *  - memoization for a hashmap O(n)
     */
    fun wrong(root: IntBinaryTreeNode): Int = max(
        wrongDfs(root = root, goLeft = true, edges = 0),
        wrongDfs(root = root, goLeft = false, edges = 0),
    )

    private fun wrongDfs(root: IntBinaryTreeNode?, goLeft: Boolean, edges: Int): Int {
        if (root == null) return edges

        val zigZagChild = if (goLeft) root.left else root.right
        val otherChild = if (goLeft) root.right else root.left

        val zigZagMax = zigZagChild?.let { wrongDfs(root = zigZagChild, goLeft = !goLeft, edges = edges + 1) } ?: edges

        val otherMax = max(
            wrongDfs(root = otherChild, goLeft = true, edges = 0),
            wrongDfs(root = otherChild, goLeft = false, edges = 0),
        )
        return max(
            zigZagMax,
            otherMax
        )
    }
}

fun main() {
    println(
        LongestZigZagPathInABinaryTree().efficient(
//            root = IntBinaryTreeNode(
//                `val` = 1,
//                left = IntBinaryTreeNode(
//                    `val` = 1,
//                    right = IntBinaryTreeNode(
//                        `val` = 1,
//                        left = IntBinaryTreeNode(
//                            `val` = 1,
//                            right = IntBinaryTreeNode(`val` = 1),
//                        ),
//                        right = IntBinaryTreeNode(`val` = 1)
//                    )
//                ),
//                right = IntBinaryTreeNode(`val` = 1)
//            )
//            root = IntBinaryTreeNode(
//                `val` = 1,
//                right = IntBinaryTreeNode(
//                    `val` = 2,
//                    left = IntBinaryTreeNode(`val` = 3),
//                    right = IntBinaryTreeNode(
//                        `val` = 4,
//                        right = IntBinaryTreeNode(`val` = 5),
//                    ),
//                ),
//            )
            root = IntBinaryTreeNode(
                `val` = 1,
                right = IntBinaryTreeNode(
                    `val` = 2,
                    left = IntBinaryTreeNode(`val` = 3),
                    right = IntBinaryTreeNode(
                        `val` = 4,
                        left = IntBinaryTreeNode(
                            5,
                            right = IntBinaryTreeNode(
                                6,
                                right = IntBinaryTreeNode(7)
                            )
                        ),
                        right = IntBinaryTreeNode(`val` = 8),
                    ),
                ),
            )
        )
    )
}
