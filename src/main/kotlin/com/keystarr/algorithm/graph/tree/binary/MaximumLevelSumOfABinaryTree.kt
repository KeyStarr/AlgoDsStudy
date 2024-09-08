package com.keystarr.algorithm.graph.tree.binary

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode
import java.util.*

/**
 * LC-1161 https://leetcode.com/problems/maximum-level-sum-of-a-binary-tree/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= number of nodes <= 10^4;
 *  • -10^5 <= node.value <= 10^5.
 *
 * Final notes:
 *  • done [efficient] by myself in 7 mins;
 *  • a very straight-forward problem for the BFS solution. DFS would be more tricky but not much - checked Editorial:
 *   just compute the sum of all levels, then find the smallest level max sum. Huh, I probably would've tried to go for a
 *   "smart" solution straight away and attempt to find the max sum as we go, without that temp all level sums list.
 *   Really, go for easier solutions first if its a matter of like n factor of space, you can always optimize later.
 *
 * Value gained:
 *  • practiced solving a level-oriented binary tree property calculation type problem via BFS.
 */
class MaximumLevelSumOfABinaryTree {

    /**
     * approach:
     *  - level sum => BFS;
     *  - update max only if current level sum is greater than the maxSum (cause we need the smallest level on max sum duplicates)
     * note: level labelling start from 1
     *
     *
     * edge cases:
     *  - sum => max sum = 10^4*10^5=10^9 => fits into int;
     *
     * Time: always O(nodes)
     * Space: O(nodes)
     *  worst is 2^(maxLevel-1), but technically is proportional to number of nodes
     */
    fun efficient(root: IntBinaryTreeNode): Int {
        val queue: Queue<IntBinaryTreeNode> = ArrayDeque<IntBinaryTreeNode>().apply { add(root) }
        var maxSum = Int.MIN_VALUE
        var maxSumLevel = -1
        var currentLevel = 1
        while (queue.isNotEmpty()) {
            val levelSize = queue.size
            var levelSum = 0
            repeat(levelSize) {
                val node = queue.remove()
                levelSum += node.`val`
                node.left?.let(queue::add)
                node.right?.let(queue::add)
            }
            if (levelSum > maxSum) {
                maxSum = levelSum
                maxSumLevel = currentLevel
            }
            currentLevel++
        }
        return maxSumLevel
    }
}
