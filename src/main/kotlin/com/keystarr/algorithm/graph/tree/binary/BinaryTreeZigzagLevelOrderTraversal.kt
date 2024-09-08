package com.keystarr.algorithm.graph.tree.binary

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode
import java.util.*

/**
 * LC-103
 * difficulty: medium
 * constraints:
 *  • 0 <= number of nodes <= 2000;
 *  • -100 <= node.value <= 100.
 *
 * Final notes:
 *  • solved by myself in about 30 mins, but got stuck for 10-20 mins on forming the zigZaggedLevel))))) Was tired, maybe
 *      that played against my hand, but still some of that is a problem with the skill;
 *  • got hanged up for a while cause thinking about reversing the order left/right or how to add queue to answer was
 *      quite confusing - as I reverse this iteration, but next it will be the reverse of reversal... bewildering.
 *      Still, I got through.
 *
 * Value gained:
 *  • if I ever again need to the start of the collection, and the interface of the result is `List<T>`
 *      => USE THE LINKED LIST, NOT ARRAY LIST, C'MON!!!!
 *  • practiced BFS on BT.
 */
class BinaryTreeZigzagLevelOrderTraversal {

    /**
     * We need to visit each node at least once - even those levels that we need not zigzag, cause we still have to
     * add all nodes to the result list + solution is level-oriented rather than depth => BFS.
     *
     * reverse values - on each second iteration
     *
     * Idea:
     *  - answer: List<List<Int>>
     *  - implement BFS, put left then right children into the queue;
     *  - init depth = 0, increment at the end of the while iteration;
     *  - at the start of each iteration:
     *      - if depth % 2 == 0: iterate starting from the tail of the queue, add to array list, add to answer (reverse)
     *      - else: iterate starting from the head of the queue, do the same.
     *  - return answer
     *
     * Edge cases:
     *  - root == null => early return emptyList.
     *
     * Time: always O(n) since we visit each node exactly once and inner while loop is O(1) amortized;
     * Space: O(n)
     */
    fun solution(root: IntBinaryTreeNode?): List<List<Int>> {
        if (root == null) return emptyList()

        val zigZaggedTree = mutableListOf<List<Int>>()
        val queue: Queue<IntBinaryTreeNode> = ArrayDeque<IntBinaryTreeNode>().apply { add(root) }
        var currentDepth = 0
        while (queue.isNotEmpty()) {
            val currentLevelNodesAmount = queue.size

            val zigZaggedLevel = LinkedList<Int>()
            if (currentDepth % 2 == 0) {
                queue.forEach { node -> zigZaggedLevel.add(node.`val`) }
            } else {
                queue.forEach { node -> zigZaggedLevel.addFirst(node.`val`) }
            }
            zigZaggedTree.add(zigZaggedLevel)

            repeat(currentLevelNodesAmount) {
                val currentNode = queue.remove()
                currentNode.left?.let(queue::add)
                currentNode.right?.let(queue::add)
            }

            currentDepth++
        }

        return zigZaggedTree
    }
}

fun main() {
    println(
        BinaryTreeZigzagLevelOrderTraversal().solution(
            IntBinaryTreeNode(
                `val` = 3,
                left = IntBinaryTreeNode(9),
                right = IntBinaryTreeNode(
                    `val` = 20,
                    left = IntBinaryTreeNode(15),
                    right = IntBinaryTreeNode(17)
                ),
            )
        )
    )
}
