package com.keystarr.algorithm.graph.tree.binary

import com.keystarr.datastructure.graph.tree.TreeNode
import java.util.*

/**
 * LC-637 https://leetcode.com/problems/average-of-levels-in-binary-tree/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= number of nodes <= 10^4;
 *  • -2^31 <= node.val <= 2^31 - 1.
 *
 * Final notes:
 *  • 🏅 done [bfs] by myself in 6 mins;
 *  • the problem's focus on levels trivially prompts the use of BFS. Averages finding is trivial as well;
 *  • 💡 the only catch was that the sum may exceed int, and to deduce that one had to approximate the max sum of any level in the BT
 *   (which I probably didn't do a very good job at, but nevertheless good enough);
 *   ⚠️ didn't prove quite well that the sum may exceed Int, but its good enough for now => but for sure is a potential
 *   area for improvement.
 *
 * Value gained:
 *  • practiced solving a binary tree metric calculation type problem using BFS.
 */
class AverageOfLevelsInBinaryTree {

    /**
     * We need to compute a metric for each level => for level-order traversal BFS is better.
     *
     * edge cases:
     *  - sum => max sum = 10^9 * ~10^2 = 10^11 => use Long for sum and double for the division result.
     *
     * Time: always O(nodes)
     * Space: always O(height) = O(n) since height depends on n
     */
    fun bfs(root: TreeNode): DoubleArray {
        val queue: Queue<TreeNode> = ArrayDeque<TreeNode>().apply { add(root) }
        val averages = mutableListOf<Double>()
        while (queue.isNotEmpty()) {
            val levelSize = queue.size
            var levelSum = 0L
            repeat(levelSize) {
                val node = queue.remove()
                levelSum += node.`val`
                node.left?.let(queue::add)
                node.right?.let(queue::add)
            }
            averages.add(levelSum / levelSize.toDouble())
        }
        return averages.toDoubleArray()
    }
}
