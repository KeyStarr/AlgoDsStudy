package com.keystarr.algorithm.graph

import com.keystarr.datastructure.graph.tree.IntBinaryTreeNode

/**
 * LC-863 https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= number of nodes <= 500
 *  • 0 <= k <= 1000
 *  • 0 <= node.value <= 500
 *  • all node.value are unique
 *  • target is the value of one of the nodes in the tree
 *
 * Value gained:
 *  • wow, the problem was given as a binary tree but actually an efficient and clean way of solving it requires converting
 *   it into an undirected graph => remember that such cases might happen! Kind of an implicit graph then. Not all Binary tree
 *   problems are actually solved via clean binary tree traversal;
 *  • practiced recursive binary tree DFS and graph BFS.
 */
class AllNodesDistanceKInBinaryTree {

    /**
     * Problem decomposition:
     *  1. we can find all nodes in the target's subtree with the distance of k edges;
     *  2. trick: how to find all such nodes that are in the root's subtree and not in the target's subtree?
     *
     * There is at least 1 node in the tree, and target is always one of the tree's nodes.
     *
     * Ideas:
     *  A) devise a tricky algorithm that would perform DFS from target for case 1 and for case 2 it would perform
     *      a smart BFS from root?
     *   if possible, O(n+e) time and O(m)=O(n) space
     *  B) convert the tree into an undirected graph by assigning each node a parent field + perform BFS on that graph
     *      starting from target.
     *   O(n+e) time=O(n), O(m)=O(n) space
     * => so A and B both have same time/space complexities, A possibly has better const, but B is significantly easier to implement
     * => choose B.
     *
     * B.1) convert a binary tree into an undirected graph => use recursive DFS for faster implementation, represent each
     *  via a HashMap for ease;
     * B.2) perform BFS on the resulting graph.
     *  also - when we go up we don't need to add children of the
     *  node, and when we go down we don't need to add parent, but since when removing an item the queue we don't know
     *  whether it was a parent or a child => either use seen OR store in a queue with each element 2 flags: wasParent or wasChild
     *  => the 2nd is better memory const.
     *
     * Edge cases:
     *  - k == 0 => return target.value. Correct as-is;
     *  - only 1 node in the tree => only if k == 0 we return its value otherwise an empty list => correct;
     *  - there are no nodes in the tree with distance k from target => we will traverse the tree the deepest from target,
     *      queue will become empty, and we'll return an empty list => correct;
     *  - tree is shape of a line => correct.
     */
    fun efficient(root: IntBinaryTreeNode?, target: IntBinaryTreeNode?, k: Int): List<Int> {
        val nodeToParentMap = mutableMapOf<IntBinaryTreeNode, IntBinaryTreeNode>()
        dfsMapChildrenToParent(root, nodeToParentMap)
        return bfsFindKDistanceNodes(startNode = target!!, nodeToParentMap = nodeToParentMap, targetDistance = k)
    }

    private fun dfsMapChildrenToParent(
        root: IntBinaryTreeNode?,
        nodeToParentMap: MutableMap<IntBinaryTreeNode, IntBinaryTreeNode>
    ) {
        root?.left?.let { leftChild ->
            nodeToParentMap[leftChild] = root
            dfsMapChildrenToParent(leftChild, nodeToParentMap)
        }
        root?.right?.let { rightChild ->
            nodeToParentMap[rightChild] = root
            dfsMapChildrenToParent(rightChild, nodeToParentMap)
        }
    }

    private fun bfsFindKDistanceNodes(
        startNode: IntBinaryTreeNode,
        nodeToParentMap: Map<IntBinaryTreeNode, IntBinaryTreeNode>,
        targetDistance: Int,
    ): List<Int> {
        val queue = ArrayDeque<IntBinaryTreeNode>().apply { addLast(startNode) }
        val seen = mutableSetOf<Int>().apply { add(startNode.`val`) }
        var currentDistance = 0
        while (queue.isNotEmpty()) {
            if (currentDistance == targetDistance) return queue.map { it.`val` }

            val currentDistanceNodesCount = queue.size
            repeat(currentDistanceNodesCount) {
                val node = queue.removeFirst()
                queue.apply {
                    addIfNotSeen(node.left, seen)
                    addIfNotSeen(node.right, seen)
                    addIfNotSeen(nodeToParentMap[node], seen)
                }
            }
            currentDistance++
        }
        return emptyList()
    }

    private fun ArrayDeque<IntBinaryTreeNode>.addIfNotSeen(
        node: IntBinaryTreeNode?,
        seen: MutableSet<Int>,
    ) {
        if (node == null || seen.contains(node.`val`)) return
        addLast(node)
        seen.add(node.`val`)
    }
}

fun main() {
    val target = IntBinaryTreeNode(
        `val` = 5,
        left = IntBinaryTreeNode(`val` = 6),
        right = IntBinaryTreeNode(
            `val` = 2,
            left = IntBinaryTreeNode(`val` = 7),
            right = IntBinaryTreeNode(`val` = 4)
        )
    )
    println(
        AllNodesDistanceKInBinaryTree().efficient(
            root = IntBinaryTreeNode(
                `val` = 3,
                left = target,
                right = IntBinaryTreeNode(
                    `val` = 1,
                    left = IntBinaryTreeNode(`val` = 0),
                    right = IntBinaryTreeNode(`val` = 8),
                )
            ),
            target = target,
            k = 2,
        )
    )
}
