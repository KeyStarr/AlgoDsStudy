package com.keystarr.algorithm.graph.tree.binary.search

import com.keystarr.algorithm.graph.tree.binary.IntBinaryTreeNode

/**
 * LC-701 https://leetcode.com/problems/insert-into-a-binary-search-tree/
 * difficulty: medium
 * constraints:
 *  • 0 <= number of nodes <= 10^4;
 *  • -10^8 <= node.value, valueToInsert <= 10^8;
 *  • root is a valid BST;
 *  • valueToInsert doesn't exist in the tree rooted at root.
 *
 * Final notes:
 *  • failed both rightmost and leftmost leaves edge cases :DD basically outlined them both but checked none and submitted...
 *  • took me ~1h for [slow], did [efficient] only after reading the editorial.
 *
 * Value gained:
 *  • mediums do tend to have like 100% at least 1 edge case that requires specific logic to handle it => don't rush!
 *      slow down and dry-run edge cases, even if you're exasperated;
 *  • funny, as in [ValidateBinarySearchTree] in-order DFS gave a solution, but NOT THE BEST ONE IN EITHER CASES!
 *   in [ValidateBinarySearchTree] the best one was by just following the definition of the tree, and here the same.
 *   There it did affect the const but not the big O time complexity, here it basically gives O(logn) on average
 *   which is a HUGE performance boost!
 *  • lots of people commenting on how this task was easy. Why did I struggle so much??
 */
class InsertIntoABinarySearchTree {

    private var prev: IntBinaryTreeNode? = null

    /**
     * Goal - insert the node into [root] in a way that the new tree is still a BST.
     *
     * Leverage the key property of the BST - traverse it in-order => in the sorted order of values, find the first
     * value that [valueToInsert] is greater than
     *
     * -------- attempt #1 --------
     *
     * Cases:
     *  - we can insert [valueToInsert] as a leaf
     *  - we need to insert [valueToInsert] to be a parent of some other nodes
     *      - is there a special case where the original tree's root is the only valid position to insert to?
     *
     * find the closest node value greater than [valueToInsert]?
     *
     * find the subtree where all values in its left subtree are less than [valueToInsert] and all values in its right
     * subtree are greater than [valueToInsert]:
     *  - if current root.value is less than [valueToInsert] => put it as the right child of the [prev]
     *      (rightmost leaf of the left subtree)
     *  - if current root.value is greater than [valueToInsert] => put it as the left child of the [next]
     *      (leftmost leaf of the right subtree)
     *
     * result - abandon
     *
     * -------- attempt #2 --------
     *
     * Idea:
     *  - traverse the tree via in-order DFS
     *  ...
     * Jumped straight into coding.
     *
     * Edge cases:
     *  - root == null => return only the new node with [valueToInsert] as its value;
     *  - valid position to insert is the leftmost leaf ([valueToInsert] is less than all values in [root])
     *      => if prev == null and root.value > [valueToInsert] than we need to insert as the leftmost leaf,
     *      allow for prev == null as the valid case;
     *  - valid position to insert is the rightmost leaf ([valueToInsert] is greater than all values in [root])
     *      => since we are guaranteed to have a place to insert [valueToInsert] and have a valid BST, then answer
     *      always exists, then if we checked each node and haven't inserted => we must insert the value as the rightmost
     *      leaf to the tree.
     *
     * Time: O(n), worst case we visit each node exactly once, average just depends on n;
     * Space: O(n) due to callstack.
     */
    fun slow(root: IntBinaryTreeNode?, valueToInsert: Int): IntBinaryTreeNode {
        if (root == null) return IntBinaryTreeNode(valueToInsert)
        val wasInserted = slowRecursive(root, valueToInsert)
        if (!wasInserted) prev!!.right = IntBinaryTreeNode(valueToInsert)
        return root
    }

    private fun slowRecursive(root: IntBinaryTreeNode?, valueToInsert: Int): Boolean {
        if (root == null) return false

        if (slowRecursive(root.left, valueToInsert)) return true
        if ((prev == null || valueToInsert > prev!!.`val`) && valueToInsert < root.`val`) {
            val newNode = IntBinaryTreeNode(`val` = valueToInsert, left = root.left, right = null)
            root.left = newNode
            return true
        }
        prev = root
        return slowRecursive(root.right, valueToInsert)
    }

    /**
     * Simply follow the definition of the binary tree and perform, basically, the binary search until we find a non-existent
     * node after a leaf => insert there!
     *
     * Time: average O(logn) due to the BST search property, worst O(n) when a tree is a line
     * Space: average O(logn) for callstack, worst O(n)
     *
     * Discovered thanks to the Editorial
     */
    fun efficient(root: IntBinaryTreeNode?, valueToInsert: Int): IntBinaryTreeNode {
        if (root == null) return IntBinaryTreeNode(valueToInsert)

        if (valueToInsert > root.`val`) {
            root.right = efficient(root.right, valueToInsert)
        } else {
            root.left = efficient(root.left, valueToInsert)
        }
        return root
    }
}
