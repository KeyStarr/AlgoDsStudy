package com.keystarr.algorithm.graph.tree.binary

/**
 * LC-1325 https://leetcode.com/problems/delete-leaves-with-a-given-value/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= number of nodes <= 3000;
 *  • 1 <= node.value, target <= 1000.
 *
 * Final notes:
 *  • 🏆 done [efficient] by myself in 15 mins, 1st time submit:
 *   • caught quickly the CORE observation: we must consider the node for removal only after both its left and right
 *    subtrees have been processed, cause node itself after these might become a leaf => post-order dfs.
 *   • caught and both the only edge case, for that we might delete the root as well if it becomes a leaf, but we need
 *    to handle that in a special way;
 *   • ⚠️ still failed to see the cleanest version [dfsCleaner], though didnt really try to, after doing [efficient].
 *   💡🔥I start to notice that basically the cleanest binary tree solution is (potentially) always working only with
 *   the current subtree rooted at [root] and its children, nothing else, clearly unlike [efficient], which works with
 *   children of the children => requires a special edge case handling for root.
 *   💡 Cleanliness of a BT problem solution seems to correlate well with LinkedList problems - one might do an efficient
 *    solution, but if its not utilizing some property (only 2 pointers for LL, only root and its direct children for BT)
 *    => its gonna be a little dirty, not the most succinct version.
 *
 * Value gained:
 *  • practiced solving a binary tree problem efficiently using in-order/post-order dfs;
 *  • practiced dry-running a tree question in the text editor (its actually quite nice. Thanks DSA course author for
 *   visual examples of how that's done! its an important skill coz in real remote interview scenario there probably
 *   would be nothing to draw on). And I need to practice that specifically, and on different types of inputs/algos 💡
 */
class DeleteLeavesWithAGivenValue {

    // TODO: did alright, but optionally retry in 1-2 weeks

    /**
     * post-order DFS? post-order for removal condition to trigger after all updates to both left and right subtrees.
     * delete nodes starting from leaves. delete a node if its a leaf and its value equals target recursively.
     *
     *           target = 2
     *              1
     *            2   2
     *           2 3 2 2
     *
     *           result:
     *              1
     *            2
     *             3
     *
     * edge cases:
     *  - all nodes in the tree must be deleted (including root) => after the call to root check whether root must be deleted,
     *   if so, return null.
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     *  worst case is tree is a line => [dfs] callstack size is exactly n
     */
    fun efficient(root: IntBinaryTreeNode, target: Int): IntBinaryTreeNode? {
        dfs(root, target)
        return if (root.isLeaf() && root.`val` == target) null else root
    }

    /**
     * in-order/post-order
     */
    private fun dfs(root: IntBinaryTreeNode, target: Int) {
        root.left?.let { left ->
            dfs(left, target)
            if (left.isLeaf() && left.`val` == target) root.left = null
        }
        root.right?.let { right ->
            dfs(right, target)
            if (right.isLeaf() && right.`val` == target) root.right = null
        }
    }

    private fun IntBinaryTreeNode.isLeaf() = left == null && right == null

    /**
     * Goal - remove all leaves equal to target repeatedly until none are left in the binary tree [root],
     *  return the root after the transformations.
     *
     * Clean post-order dfs.
     *
     * Learned from https://leetcode.com/problems/delete-leaves-with-a-given-value/solutions/484264/java-c-python-recursion-solution/
     */
    fun dfsCleaner(root: IntBinaryTreeNode, target: Int): IntBinaryTreeNode? {
        root.left = root.left?.let { dfsCleaner(it, target) }
        root.right = root.right?.let { dfsCleaner(it, target) }
        return if (root.left == null && root.right == null && root.`val` == target) null else root
    }
}
