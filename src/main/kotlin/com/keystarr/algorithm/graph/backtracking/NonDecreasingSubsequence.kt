package com.keystarr.algorithm.graph.backtracking

import java.util.*

/**
 * LC-491 https://leetcode.com/problems/non-decreasing-subsequences/description/
 * difficulty: medium
 * Final notes:
 *  - ⚠️⚠️ failed to come up with a working solution by myself in 1h;
 *  - its so weird, this problem is quite default for a backtracking type problems, yet I've failed to come up with a solution!
 *   Thought about backtracking especially after seeing the constraints (n <= 15!!) but still tried to go for the "most optimal route"
 *   straight away and though I've seen duplicating subproblems - could try DP [topDownDp] => failed miserably;
 *  - I still think it is doable with DP, but probably it's just harder, might as well try later. Welp, should've tried
 *   to go back to backtracking to AT LEAST do a brute force solution when the "most optimal" route didn't work out;
 *  - 🔥🔥 same ol' tricks for backtracking as with subsets: use a seen set to not try same numbers for the same position
 *   + consider only number to the right of the current, both to avoid duplicates.
 *
 * Value gained:
 *  - practiced efficiently solving a "generate all valid combinations=subsequences" type problem using backtracking;
 *  - 💡 reinforced TO NOT OVERCOMMIT FOR to the most optimal route! Try brute force first if things don't work out in 10-20 minutes!
 *   here though I was quite convinced that I was doing the right thing up until, like, the 40th minute :D
 */
class NonDecreasingSubsequence {

    // TODO: retry in 1-2 weeks

    // TODO (optional): try DP here, compare asymptotic complexities and maintainability

    /**
     * goal = generate all possible valid subsequences of [nums].
     *  valid = constraints:
     *   1. non-decreasing;
     *   2. size > 1;
     *   3. distinct.
     *
     * tricky part - how to avoid duplicates?
     *
     * for the problem asks for generating all of something => try backtracking for optimized brute force
     *
     * approach - for each position of the potential subsequence consider all possible options, but:
     *  1. don't same number multiple times for a position, as that would lead to duplicates;
     *  2. as we go to add more numbers, consider only numbers to the right of the last picked number's index;
     *  3. respect the non-decreasing constraint;
     *  4. add any intermediate/final subsequence of size > 1.
     *
     * Time: average/worst O(2^n)
     *  - total iterations to generate all subsequences (without any pruning) would be 2^n, since the algorithm then forms a
     *   decision tree, where for every number we decide whether to take it or not;
     *  - pruning provides optimization, yet worst case is [nums] is entirely a strictly increasing subsequence with distinct
     *   numbers => neither duplicate number same pos nor non-decreasing pruning executes => 2^n iterations.
     * Space: average/worst O(n)
     *  - worst height of the callstack is n;
     *  - seen set grows up to n.
     *
     * ---------------
     *
     * also could've used IntArray(size=200) instead of the hashset for potentially a slightly better time const
     */
    fun backtracking(nums: IntArray) = mutableListOf<List<Int>>().apply {
        backtrackingRecursive(
            startInd = 0,
            current = mutableListOf(),
            results = this,
            nums = nums,
        )
    }

    private fun backtrackingRecursive(
        startInd: Int,
        current: MutableList<Int>,
        results: MutableList<List<Int>>,
        nums: IntArray,
    ) {
        if (current.size > 1) results.add(ArrayList(current))
        if (startInd == nums.size) return

        val seen = mutableSetOf<Int>()
        for (i in startInd until nums.size) {
            val newNumber = nums[i]
            if (seen.contains(newNumber) || (current.isNotEmpty() && current.last() > newNumber)) continue

            current.add(newNumber)
            backtrackingRecursive(startInd = i + 1, current, results, nums)
            current.removeLast()
            seen.add(newNumber)
        }
    }

    /**
     * observations:
     *  1. for each number we decide whether we take it or not (preserving order);
     *  2. we can start from any number, and subsequence is constrained by each choice => i.o. each choice affects further choices
     *
     * goal is to "generate all of something"
     *
     * backtracking? do suproblems overlap though? yea they do
     *  e.g. 4,6,7,7 if we start either from 4 or 6, when we reach the first 7 the question is "return all non-decr subsequences
     *   from [7,7]" and to each we can add either 4 or 6, and it doesn't matter which numbers came before as long as the max of them
     *   is <= to 7.
     *
     * => try dp
     *
     * top-down
     *
     * - goal: return all non-decreasing subsequences of the array
     *  return value List<List<Int>>
     * - input states:
     *  - leftInd: Int
     * - recurrence relation:
     *  dp(leftInd) = list
     *   for nextInd in leftInd+1..nums.size-1:
     *    val list = dp(nextInd).clone()
     *    for (i in list.indices)
     *     if (list[i] >= nums[leftInd) list.add(list[i].addFirst(nums[leftInd])
     *
     * ------------------
     *
     * submit fail, problem: how to generate all possible subsequences using all duplicate numbers YET not repeat any of the subsequences?
     * major case: we have more than 2 duplicate elements
     * e.g. 1 1 1
     *
     * leftInd=2 => [[1]]
     * leftInd=1 => [[1, 1 1]]
     * leftInd=0 => [[1 1, 1 1 1]]
     *
     * => ah-huh, so if have any nextNumber == currentNumber then don't add currentNumber as a subsequence of 1 size,
     *  since we've already considered all subsequences starting with that? wrong
     *
     *
     *
     * try alternative DP?
     * goal - return all non-decreasing subsequences of nums\[:rightInd] that END with the number nums\[rightInd]
     *
     *
     *
     */
    fun solution(nums: IntArray): List<List<Int>> {
        val cache = Array<List<List<Int>>?>(size = nums.size) { null }
        topDownDp(
            leftInd = 0,
            nums = nums,
            cache = cache,
        )

        val result = mutableListOf<List<Int>>()
        cache.forEach { subsequences ->
            subsequences?.forEach { subseq ->
                if (subseq.size > 1) result.add(subseq)
            }
        }
        return result
    }

    /**
     * goal - return all non-decreasing subsequences of nums\[leftInd:] that start with the number nums\[leftInd]
     */
    private fun topDownDp(
        leftInd: Int,
        nums: IntArray,
        cache: Array<List<List<Int>>?>,
    ): List<List<Int>> {
        if (leftInd == nums.size) return emptyList()

        val cachedResult = cache[leftInd]
        if (cachedResult != null) return cachedResult

        val currentNumber = nums[leftInd]
        val subsequences = mutableListOf<List<Int>>()
        subsequences.add(linkedListOf(currentNumber))

        val numbersSeen = mutableSetOf<Int>()
        for (nextInd in (leftInd + 1) until nums.size) {
            val nextNumber = nums[nextInd]
            if (nextNumber < currentNumber || numbersSeen.contains(nextNumber)) continue

            val nextSubsequences = topDownDp(leftInd = nextInd, nums, cache)
            nextSubsequences.forEach { subseq ->
                val newSubseq = LinkedList(subseq).apply { addFirst(currentNumber) }
                subsequences.add(newSubseq)
            }
            numbersSeen.add(nextNumber)
        }

        return subsequences.also { cache[leftInd] = subsequences }
    }
}

private fun linkedListOf(vararg nums: Int): LinkedList<Int> {
    val list = LinkedList<Int>()
    nums.forEach { list.add(it) }
    return list
}

fun main() {
    println(
        NonDecreasingSubsequence().solution(
            nums = intArrayOf(1, 1, 1, 2),
        )
    )
}
