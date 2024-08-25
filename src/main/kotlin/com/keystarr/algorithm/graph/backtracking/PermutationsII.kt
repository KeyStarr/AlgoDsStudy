package com.keystarr.algorithm.graph.backtracking

/**
 * LC-47 https://leetcode.com/problems/permutations-ii/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.size <= 8;
 *  • -10 <= nums\[i] <= 10.
 *
 * Final notes:
 *  • done [efficient] by myself in 30 mins;
 *  • a very straight forward backtracking problem with a single core observation = that we must consider only unique numbers
 *   not used in the current combination for the current position.
 *
 * Value gained:
 *  • practiced recognizing and solving a permutations problem efficiently via backtracking DFS.
 */
class PermutationsII {

    private val results = mutableListOf<List<Int>>()
    private lateinit var nums: IntArray

    /**
     * generate all of something => consider backtracking
     *
     * numbers may have duplicates => why is that important?
     *
     * input = 2 2 2 2
     * answer = [[2 2 2 2]]
     *
     *
     * input = 1 2 3 4
     * [1 2 3 4], [2 1 3 4], [3 1 3 4], [4 1 3 4]
     *
     * 4 * 3 * 2 = 24 options for 4 elements
     *
     * input = 1 2 3
     * 2 * 3 = 6 permutations
     * [1 2 3], [1 3 2], [2 1 3], [2 3 1], [3 1 2], [3 2 1]
     *
     * input = 1 1 3
     * [1 1 3], [1 3 1], [3 1 1]
     *
     * observation: when we consider elements to take the ith position, we need to consider X number exactly 1 for that
     *  position and all further combinations, no need to check its duplicates for that position, cause further combinations
     *  would be same and previous choices would be same => all resulting permutations when considering a duplicate would, khm, duplicate
     *  the ones already generated with a number equal to X on that position.
     *
     * => prune all the duplicate permutations with duplicate numbers by considering for each position exactly one number of same value.
     * how?
     *  A. sort [nums], scroll through numbers until the num is unique; +O(nlogn) time factor
     *  B. add each tried number in current [backtracking] call to a call-local set => skip each such number's duplicates
     *  for the current position (in the current call). +O(n) space factor
     *
     * how to mark used numbers?
     *  A. convert the set to an array + keep a pointer to the current ind within the set; same O(n) space, a matter of const
     *  B. use ordered set, tree set.
     *
     * Time: TODO: understand deeper
     * Space: O(n)
     */
    fun efficient(nums: IntArray): List<List<Int>> {
        this.nums = nums
        backtracking(mutableListOf(), BooleanArray(size = nums.size))
        return results
    }

    private fun backtracking(
        currentPermutation: MutableList<Int>,
        usedNums: BooleanArray,
    ) {
        if (currentPermutation.size == nums.size) {
            results.add(ArrayList(currentPermutation))
            return
        }

        val posTriedNums = mutableSetOf<Int>() // could declare outside and reuse via clear for optimal GC
        nums.forEachIndexed { ind, number ->
            if (usedNums[ind]) return@forEachIndexed
            if (posTriedNums.contains(number)) return@forEachIndexed

            currentPermutation.add(number)
            usedNums[ind] = true
            backtracking(currentPermutation, usedNums)
            currentPermutation.removeLast()
            usedNums[ind] = false
            posTriedNums.add(number)
        }
    }
}
