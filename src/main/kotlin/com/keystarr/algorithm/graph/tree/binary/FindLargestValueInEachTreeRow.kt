package com.keystarr.algorithm.graph.tree.binary

import java.util.ArrayDeque
import java.util.Queue

/**
 * LC-535 https://leetcode.com/problems/find-largest-value-in-each-tree-row/description/
 * difficulty: medium (I feel that was closer to leet-easy)
 * constraints:
 *  • 0 <= number of nodes <= 10^4;
 *  • -2^31 <= node.value <= 2^31;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • solved by myself, submit 1st time [bfs] in 15 mins.
 *
 * Value gained:
 *  • practiced implementing BFS from scratch and reinforced recognizing the pattern of when BFS is faster/easier to
 *      implement and understand then dfs with both having same time/space complexities.
 */
class FindLargestValueInEachTreeRow {

    /**
     * Since we'd have to visit each node in both bfs and dfs here anyway, choose bfs, because the actual problem
     * solution concerns levels foremost than depth => bfs.
     *
     * Idea:
     *  - result: List<Int>
     *  - queue: Queue<TreeNode>
     *  - queue.add(root)
     *  - while queue.isNotEmpty():
     *      - currentLevelNodesAmount = queue.size
     *      - maxValue = Int.MIN
     *      - repeat(currentLevelNodesAmount):
     *          - currentNode = queue.remove()
     *          - if (currentNode.value > maxValue) maxValue = currentNode.value
     *          - currentNode.left?.let(queue::add)
     *          - currentNode.right.let(queue::add)
     *      - result.add(maxValue)
     *  - return result
     *
     * Edge cases:
     *  - root == null => early return emptyList();
     *  - number of nodes == 1 => correct as-is.
     *
     * Time: O(n) cause we visit each node exactly once, while inner iteration is amortized O(1)
     * Space: O(n)
     */
    fun bfs(root: IntBinaryTreeNode?): List<Int> {
        if (root == null) return emptyList()

        val result = ArrayList<Int>()
        val queue: Queue<IntBinaryTreeNode> = ArrayDeque<IntBinaryTreeNode>().apply { add(root) }
        while (queue.isNotEmpty()) {
            val currentLevelNodesAmount = queue.size
            var currentLevelMaxValue = Int.MIN_VALUE
            repeat(currentLevelNodesAmount) {
                val currentNode = queue.remove()
                if (currentNode.value > currentLevelMaxValue) currentLevelMaxValue = currentNode.value
                currentNode?.left?.let(queue::add)
                currentNode?.right?.let(queue::add)
            }
            result.add(currentLevelMaxValue)
        }

        return result
    }

    // TODO: solve via dfs for funsies later
}
