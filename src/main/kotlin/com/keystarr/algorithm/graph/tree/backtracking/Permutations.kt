package com.keystarr.algorithm.graph.tree.backtracking

/**
 * LC-46 https://leetcode.com/problems/permutations/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.length <=  6
 *  • -10 <= nums\[i] <= 10
 *  • all integers of nums are unique
 *  
 * Final notes:  
 *  • done [bfs] to understand better what the optimization with [dfs] would be => didn't get it :D
 *  • time complexity is really hard to estimate, at least right now for me;
 *  • [bfs] is definitely slower than [dfs] cause in [bfs] we perform multiple right-shift of elements = array copies
 *   when inserting the new number in the middle, and [dfs] we only append to the end / remove from the end which
 *   never requires an array copy;
 *  • so, here we "PRUNE" the paths which lead to non-distinct or even just combinations (with duplicate values) permutations
 *   by `if (usedNums[ind]) return@forEachIndexed`, i.o. adding to the current permutation only numbers which not were
 *   not yet used in the current path!!!
 * 
 * Value gained:
 *  • awesome, so basically the "backtracking" pattern is a special case of tree DFS;
 *  • practiced recognizing and using backtracking to solve a problem efficiently;
 */
class Permutations {

    /**
     * goal: return all permutations from [nums] (including the original)
     *
     * naive idea - create all permutations via "generations":
     *  - init intermediateResult: Queue<List<Int>> with just listOf(nums[0])
     *  - iterate through nums from 1st to last:
     *      - iterate through all intermediateResult available only at this moment and insert currentNum into every
     *       slot in the current intermediate result => add each result back into intermediateResults.
     * i.o. use each number from [nums] exactly once, add it into every available slot it can have, build from bottom (1 number only)
     *  to top (all numbers are used).
     *
     * Time: factorial something? Certainly at least an order of magnitude slower than n^k TODO: estimate precisely (couldn't do it in a reasonable time)
     *  - outer loop takes n=nums.size-1 iterations
     *  - inner loop gets progressively larger, first its 2 iterations, than its 6 etc
     *  => total amount of iterations is ???
     *  - everytime we add into the list, we have to shift-right the elements in it, which in general costs => ???
     * Space: max space used is O(n!) at the last generation => the total amount of possible permutations
     *
     * (couldn't figure out by myself in reasonable time, so learnt the idea from https://leetcode.com/problems/permutations/solutions/18255/share-my-short-iterative-java-solution/)
     */
    fun bfs(nums: IntArray): List<List<Int>> {
        val intermediateResults = ArrayDeque<List<Int>>()
        intermediateResults.add(listOf(nums[0]))
        for (i in 1 until nums.size) {
            val currentNum = nums[i]
            var previousGenerationSize = intermediateResults.size
            while (previousGenerationSize > 0) {
                val prevGenList = intermediateResults.removeFirst()
                repeat(prevGenList.size + 1) { j ->
                    val newList = ArrayList(prevGenList)
                    newList.add(j, currentNum)
                    intermediateResults.addLast(newList)
                }
                previousGenerationSize--
            }
        }
        return intermediateResults.map { it }
    }

    /**
     * the goal is to generate all of something matching a constraint + numbers ranges are small (6 nums at most) => try backtracking?
     *
     * use the default backtracking pattern.
     *
     * Time: approximately O(n!*n) TODO: why isn't that precise? what's the precise estimation here and why?
     *  basically we traverse DFS the tree where we have n! leaves, the amount of edges on each level varies, and the
     *  amount of children varies as well => O(n+e) => what would be the total nodes count and total edges count?
     *
     *  (wrong?)
     *  - we start with a tree with root, then add nums.size nodes = make that amount of calls to backtrack. Then
     *   each of those nodes makes (nums.size-1) calls to backtrack etc until the backtrack call on leaves which hits the base case
     *   => approximately we have (n-1)*(n-2)*...*1 ~ n! calls to backtrack
     *  - each call costs:
     *   - base case: O(n) to copy the list
     *   - recursive case:
     *    - contains check is O(1)
     *    - num add to current and set is amortized O(1)
     *    - inner loop we already counted as the total backtrack calls count
     *
     * Space: O(n!*n)
     *  - total results lists size is n! and each list takes n space = O(n!*n)
     *  - recursion stack = height of the tree = n+1
     */
    fun dfs(nums: IntArray): List<List<Int>> = mutableListOf<List<Int>>().apply {
        backtrack(
            current = ArrayList(nums.size),
            usedNums = BooleanArray(size = nums.size),
            results = this,
            nums = nums,
        )
    }

    private fun backtrack(
        current: MutableList<Int>,
        usedNums: BooleanArray,
        results: MutableList<List<Int>>,
        nums: IntArray,
    ) {
        if (current.size == nums.size) {
            results.add(ArrayList(current))
            return
        }

        nums.forEachIndexed { ind, num ->
            if (usedNums[ind]) return@forEachIndexed
            current.add(num)
            usedNums[ind] = true
            backtrack(current, usedNums, results, nums)
            current.removeLast()
            usedNums[ind] = false
        }
    }
}

fun main() {
    println(
        Permutations().dfs(
            intArrayOf(1, 2, 3)
        )
    )
}
