package com.keystarr.datastructure.graph.tree

import java.util.*

class IntBinaryTreeNode(
    var `val`: Int,
    var left: IntBinaryTreeNode? = null,
    var right: IntBinaryTreeNode? = null,
) {

    /**
     * Generate the tree back into the leet's environment default format, for debug. Earlier version for visualization,
     * before [treeVisualizedString] hit.
     */
    fun treeToArrayString(): String {
        val builder = StringBuilder("[")
        val queue: Queue<IntBinaryTreeNode?> = LinkedList()
        queue.add(this)
        while (queue.isNotAllNulls()) {
            val node = queue.remove()
            builder.append("${node?.`val`}, ")

            queue.add(node?.left)
            queue.add(node?.right)
        }
        return builder.append("]").toString()
    }

    /**
     * Direct predecessor to [treeVisualizedString], a fallback in case of bugs for visual clarity.
     */
    fun treeToLevelsString(): String {
        val output = StringBuilder()
        val nodesQueue: Queue<TreeNode?> = LinkedList()
        nodesQueue.add(this)
        var currentLevel = 0

        while (nodesQueue.isNotAllNulls()) {
            val levelSize = nodesQueue.size
            repeat(levelSize) {
                val node = nodesQueue.remove()
                output.append("${node?.`val`}, ")
                nodesQueue.add(node?.left)
                nodesQueue.add(node?.right)
            }
            currentLevel++
            output.append("\n")
        }
        return output.toString()
    }

    /**
     * A crutchy 20% effort 80% result kinda attempt at visualizing the binary tree. Useful in problems that require
     *  modification like [com.keystarr.algorithm.graph.tree.binary.search.DeleteNodeInABST] for intermediate/result tree
     *  representation for debugging + in problems where leet's environment doesn't render the tree for some reason.
     *
     * Not the cleanest code, but works on simple cases - good enough for now.
     */
    fun treeVisualizedString(): String {
        val levels = treeToNodeLevels()
        val lastLevel = StringBuilder()
        var prevPositions = mutableListOf<Int>()
        levels.last().forEach {
            prevPositions.add(lastLevel.length)
            lastLevel.append("$it  ")
        }

        val finalOutput = LinkedList<String>().apply { add(lastLevel.toString()) }
        val lastLevelLength = finalOutput.first.length
        for (j in levels.lastIndex - 1 downTo 0) {
            val level = levels[j]

            val builder = StringBuilder()
            repeat(lastLevelLength) { builder.append(" ") }

            val newPositions = mutableListOf<Int>()
            var currentPosInd = 0
            level.forEach { element ->
                var currentInd = (prevPositions[currentPosInd] + prevPositions[currentPosInd + 1]) / 2
                newPositions.add(currentInd)
                "$element  ".forEach { char ->
                    builder[currentInd] = char
                    currentInd++
                }
                currentPosInd += 2
            }
            prevPositions = newPositions

            finalOutput.addFirst(builder.toString())
        }
        return finalOutput.joinToString(separator = "\n")
    }

    private fun treeToNodeLevels(): List<List<String>> {
        val levels = mutableListOf<MutableList<String>>()
        val nodesQueue: Queue<TreeNode?> = LinkedList()
        nodesQueue.add(this)
        var currentLevel = 0

        while (nodesQueue.isNotAllNulls()) {
            val levelSize = nodesQueue.size
            val level = mutableListOf<String>()
            repeat(levelSize) {
                val node = nodesQueue.remove()
                level.add("${node?.`val` ?: "X"}")
                nodesQueue.add(node?.left)
                nodesQueue.add(node?.right)
            }
            levels.add(level)
            currentLevel++
        }
        return levels
    }

    // obviously, could be optimized to O(1), but no real benefit right now
    private fun Queue<IntBinaryTreeNode?>.isNotAllNulls() = any { it != null }

    companion object {

        /**
         * Basically adapted to leetcode's default BT input format. Notice that in case some children values are null,
         *  we don't specify children for it in the future, instead we just skip "excessive" nulls.
         */
        fun treeFrom(nodes: Array<Int?>): IntBinaryTreeNode? {
            if (nodes.isEmpty()) return null

            val root = IntBinaryTreeNode(nodes[0]!!)
            val queue: Queue<IntBinaryTreeNode> = ArrayDeque<IntBinaryTreeNode>().apply { add(root) }
            var currentInd = 1
            while (currentInd < nodes.size) {
                val node = queue.remove()
                if (nodes[currentInd] != null) {
                    node.left = IntBinaryTreeNode(nodes[currentInd]!!)
                    queue.add(node.left)
                }
                currentInd++
                if (currentInd < nodes.size && nodes[currentInd] != null) {
                    node.right = IntBinaryTreeNode(nodes[currentInd]!!)
                    queue.add(node.right)
                }
                currentInd++
            }
            return root
        }
    }
}

// a terrible naming, but required in order to comply to Leet's environment and not change names every time when running the code there
typealias TreeNode = IntBinaryTreeNode