package com.keystarr.algorithm.graph.backtracking

import kotlin.math.pow

/**
 * LC-78 https://leetcode.com/problems/subsets/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.length <= 10;
 *  • -10 <= nums\[i] <= 10;
 *  • all numbers in [set] are unique.
 *
 * Final notes:
 *  • solved via [bitwise] by myself (after googling subsets count formula), but did [backtracking] only after learning
 *   key design decisions from the course;
 *  • so in [backtracking] indeed the same backtracking pattern fits, with:
 *   • pruning being traversing only to the nodes-numbers that come after the current number in the original array;
 *   • adding subsets at each node, each subset is the current state of the subset we keep track of;
 *   • indeed the same as in [Permutations], add the current number on the forward movement and remove on backtracking;
 *  • [bitwise] is more efficient than [backtracking], but it seems that iterative probably will be even more efficient here.
 *
 * Value gained:
 *  • learnt a model of reasoning about subsets such that we either take each element or not => a binary decision, 2 properties follow:
 *   • the total number of subsets is 2^nl
 *   • subset generation can be simply implemented bitwise [bitwise].
 *  • practiced recognizing and implement backtracking to solve the problem.
 */
class Subsets {

    /**
     * goal: return all the possible subsets of the [set] (including the empty one).
     * subset differences from permutations:
     *  - length may differ
     *  - same digits different order (different permutation) => duplicate subset
     *
     * backtracking, recursive DFS:
     *  - at each node add current nodes encountered to results as a valid subset;
     *  - traverse only to those numbers that are after the current number in the original set.
     *   (because we have already considered all subsets which include all previous numbers and the current number!
     *    this ain't permutation => just reordering yields duplicates)
     *
     * Time: O(n*2^n)
     *  - since each call to [backtrack] we add a single valid subset to results => the number of calls equals to
     *   the number of subsets => 2^n
     *  - the cost of each call depends on n, cause inner loop has up to n iterations (and cause we make a copy
     *   of the list to add it into result)
     * Space: O(n*2^n)
     *  - height of the tree is n
     *  - the number of subsets is 2^n, each subset's size depends on n => total result takes space O(n*2^n)
     */
    fun backtracking(nums: IntArray): List<List<Int>> = mutableListOf<List<Int>>().apply {
        backtrack(
            current = mutableListOf(),
            currentInd = 0,
            results = this,
            nums = nums,
        )
    }

    private fun backtrack(
        current: MutableList<Int>,
        currentInd: Int,
        results: MutableList<List<Int>>,
        nums: IntArray,
    ) {
        results.add(ArrayList(current))
        (currentInd until nums.size).forEach { ind ->
            current.add(nums[ind])
            backtrack(current = current, currentInd = ind + 1, results, nums)
            current.removeLast()
        }
    }

    /**
     * One way to look at constructing all possible subsets from a set is that for each subset we decide for each element
     * of the original set whether we take it (1) or not (0) => we have 2^n subsets
     * => we can literally put that description into an algorithm with bitwise shift right / taking the lowest bit.
     *
     * Time: worst is O(2^n*4), so here time is always O(2^n)
     *  - outer loop has exactly 2^n iterations
     *  - inner loop (a single subset construction) has at most 31 iterations in general, but in this problem max n = 10 => 4 bits => 4 iterations
     *   - each such iteration costs O(1) (bitwise is O(1) and adding into an array list is amortized O(1)
     * Space: O(n*2^n), cause it's the total number of subsets from the set of size n, each subset's size depends on n
     */
    fun bitwise(nums: IntArray): List<List<Int>> {
        val n = nums.size
        val subsetsTarget = 2.0.pow(n).toInt()
        val results = mutableListOf<List<Int>>()

        repeat(subsetsTarget) { i ->
            val subset = mutableListOf<Int>()
            var currentIndShift = i
            var shiftsCount = 0
            while (currentIndShift != 0) {
                val bit = currentIndShift.takeLowestOneBit()
                currentIndShift = currentIndShift shr 1
                shiftsCount++
                if (bit == 1) subset.add(nums[n - shiftsCount])
            }
            results.add(subset)
        }
        return results
    }

    // TODO: implement iterative
}

fun main() {
    println(
        Subsets().backtracking(
            nums = intArrayOf(1, 2, 3)
        )
    )
}
