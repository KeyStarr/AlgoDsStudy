package com.keystarr.algorithm.graph.tree.binary

/**
 * LC-872 https://leetcode.com/problems/leaf-similar-trees/
 * difficulty: medium
 * constraints:
 *  • 1 <= number of nodes <= 200;
 *  • 0 <= node.value <= 200.
 *
 * Final notes:
 *  • did BFS first, though it was clearly not a path to a reasonable solution here! Should've focused more;
 *  • done [dfsIterative] by myself in ~45 mins. Iterative DFS is very rarely an optimal solution (in my experience so far), didn't
 *   think of it for a long time;
 *  • simple bruteforce would be to add nodes of both trees into lists and compare these, it would have
 *   average/worst same asymptotic complexities as [dfsIterative] but have a worse const.
 *
 * Value gained:
 *  • practiced solving a multiple binary tree traversal type problem using an iterative DFS;
 *  • apparently, if the goal is to compute some common property of multiple trees - iterative traversal is the path
 *   to a clean solution, unless we compare each node to node?
 */
class LeafSimilarTrees {

    // TODO: retry in 1-2 weeks

    /**
     * with bfs we visit leaves in the order of levels of depth left-to-right or right-to-left
     * with dfs we visit leaves just simply left-to-right, which is what we actually need
     * => do one pass through both trees, find the first leaf if one tree, then fast the first in the second etc
     *  handle edge cases with trees of non-equal leaves count
     *
     * do iterative DFS, since its easier to progress either one or the other tree then
     *
     * Time: average/worst O(n+m), where n=nodes amount in [root1] and m=nodes amount in [root2]
     *  worst is trees are leaf similar -> check all nodes exactly once in both trees
     * Space: average/worst O(n+m), worst is both trees are in shape of a line
     *
     * ----
     *
     * Totally could be DRIed, refactored, but no point right now.
     */
    fun dfsIterative(root1: IntBinaryTreeNode, root2: IntBinaryTreeNode): Boolean {
        val stack1 = mutableListOf<IntBinaryTreeNode>().apply { add(root1) }
        val stack2 = mutableListOf<IntBinaryTreeNode>().apply { add(root2) }

        var leavesCount1 = 0
        var lastLeaf1: IntBinaryTreeNode? = null
        var leavesCount2 = 0
        var lastLeaf2: IntBinaryTreeNode? = null
        while (stack1.isNotEmpty() || stack2.isNotEmpty()) {
            if (lastLeaf1 != null && leavesCount1 == leavesCount2 && lastLeaf1.`val` != lastLeaf2?.`val`) return false
            if ((stack1.isEmpty() && leavesCount1 <= leavesCount2) || (stack2.isEmpty() && leavesCount1 >= leavesCount2)) return false

            if (leavesCount1 <= leavesCount2) {
                val node1 = stack1.removeLast()
                if (node1.left == null && node1.right == null) {
                    lastLeaf1 = node1
                    leavesCount1++
                } else {
                    node1.left?.let(stack1::add)
                    node1.right?.let(stack1::add)
                }
            } else {
                val node2 = stack2.removeLast()
                if (node2.left == null && node2.right == null) {
                    lastLeaf2 = node2
                    leavesCount2++
                } else {
                    node2.left?.let(stack2::add)
                    node2.right?.let(stack2::add)
                }
            }
        }
        return lastLeaf1?.`val` == lastLeaf2?.`val` // if last nodes in both trees to be visited were leaves, in case they differ
    }
}

fun main() {
    println(
        LeafSimilarTrees().dfsIterative(
//                root1 = IntBinaryTreeNode.treeFrom(arrayOf(3, 5, 1, 6, 2, 9, 8, null, null, 7, 4))!!,
//                root2 = IntBinaryTreeNode.treeFrom(
//                    arrayOf(
//                        3,
//                        5,
//                        1,
//                        6,
//                        7,
//                        4,
//                        2,
//                        null,
//                        null,
//                        null,
//                        null,
//                        null,
//                        null,
//                        9,
//                        8
//                    )
//                )!!,
//            root1 = IntBinaryTreeNode.treeFrom(arrayOf(1,2,3))!!,
//            root2 = IntBinaryTreeNode.treeFrom(arrayOf(1,2,3))!!,
            root1 = IntBinaryTreeNode(1),
            root2 = IntBinaryTreeNode(2),
        )
    )
}
