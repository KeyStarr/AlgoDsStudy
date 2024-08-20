package com.keystarr.algorithm.dp.onedim

/**
 * https://leetcode.com/problems/largest-divisible-subset/description/
 * LC-368 https://leetcode.com/problems/largest-divisible-subset/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.size <= 1000;
 *  • 1 <= nums\[i] <= 2 * 10^9.
 *
 * Final notes:
 *  • done via [topDown] by myself in 53 mins (oh, so close);
 *  • 🎉🎉 made all the core observations and design choices by myself:
 *    - %==0 simply means that one number is a factor of another
 *     => we don't need to check division both ways if we SORT 🔥 the numbers;
 *    - if we take element ith, there might be multiple exclusive choices for the next element jth, cause different numbers
 *     might share the prime factor of the ith number, but add different factors to it (e.g. ith = 3, jth might 12 (*4) or 15 (*5))
 *    - each step we make a choice + choices affects further choices => try DP?
 *    - if the numbers are sorted, and we have chosen the ith number, then for any number jth that ith is a factor of,
 *      all previous numbers would also be factors of the jth number (their prime factors numbers are less than ith but still overlap)!
 *     => when we make a choice for ith, we need to solve the subproblem with the input nums[i:nums.size-1]
 *    - subproblems may overlap, consider (3, 4, 5, 8, 12, 15, 45) e.g. we can get both to 15 both from 3 and from 5, but the
 *     max subset size starting at 15 isn't affected by these previous choices
 *     => it is DP 100%;
 *    - the rest: design via the DP framework (goal and return value, state, recursive case, base cases, memoization).
 *  • 🏆 one of the first DP problems I've diagnosed correctly (almost) in the wild!!
 *  • top solutions look kinda different, but still involve sorting and DP. Checked Neetcode - https://www.youtube.com/watch?v=LeRU6irRoW0
 *   he's did the same as I here. Decided to skip for now, but maybe there are a few optimizations I could crack, verify later.
 *
 * Value gained:
 *  • practiced RECOGNIZING and solving a one-dimensional DP problem with multiple tricks.
 */
class LargestDivisibleSubset {

    // TODO: is there a more optimal solution? I feel like there might be some more room for optimization here, can we do it for O(n)?

    /**
     * given: set of DISTINCT positive integers
     * goal - find the best valid subset
     *  valid = every pair of numbers are divisible without a remainder (either i,j or j,i)
     *  best = max size
     *
     * every pair is divisible without a remainder => either nums\[i] is a multiple of nums\[j], or the opposite is true
     * => each number is a multiple of numbers less than it, and is a factor for the multiple of the numbers larger than it
     *  in the subset
     *
     * brute force:
     *  - take number ith into the subset
     *  -
     *
     * 3 9 14 28 56
     *
     * 3 9 12 24
     * (3) (3 3) (3 2 2) (2 3 2 2)
     *
     * 2 3 9 12 24 27
     *
     * 3 4 5 10
     *
     *
     * 3 4 9 12 24
     *
     *
     * suppose we've taken number ith
     * now it affects further choices, we have (n-k) elements left to choose from, and we can only choose elements that
     *  %==0 on each already chosen element.
     *
     * ------------------------
     *
     * assume input is SORTED ascending
     *
     * observation:
     *  - we have 0 elements => we take ith element;
     *  - then we take the first element (jth) after ith, which divides %ith==0. Then only such an element would fit that would
     *   %jth==0 (doesn't need to check any previous elements, since they all have common multiples, we just add prime factors
     *   as we go further).
     *  - we may have multiple choices for the jth element, so each time we make the choice we affect further choices
     *   => we need to try all such combinations.
     *
     * do subproblems actually overlap?
     * can we choose the same jth element in multiple different combinations = with different previously chosen elements?
     * yes we can, if there would be multiple choices with same X prime factors, but different numbers adding different prime
     * factor into the mix
     * => DP.
     *
     * goal: return the largest valid subset of the array numbers[leftInd:numbers.size-1]
     *  valid = such that %==0 for each two numbers (in either order)
     * state:
     *  leftInd pointing at the sorted [numbers]
     * recursive case:
     *  maxList
     *  for (j in leftInd until numbers.size):
     *   if (numbers\[j] % numbers\[leftInd] == 0):
     *    list=dp(leftInd=j)
     *    if(list.size>maxList.size) maxList=list
     *  maxList.addFirst(numbers[0])
     *  return maxList
     *
     * base case:
     *  leftInd == numbers.size-1 => return list(numbers\[leftInd])
     *
     * memoization: Array<List<Int>
     *
     * ofc we must start DP from each number first being the first choice!
     *
     * Edge cases:
     *  - nums.size == 1 => always return the list with it => base case, correct.
     *
     * Time: always O(nlogn)
     * Space: O(n)
     *  - cache always has at the end n lists, each list contains up to n elements, worst all elements are equal, so
     *   (n-index) elements, but average O(n) still.
     *  => average/worst O(n^2)?
     *  actually no, thanks to https://www.youtube.com/watch?v=LeRU6irRoW0 I've learned that, indeed, min factor to grow
     *  is 2 and 2^32 is our boundary for Int => we can AT MOST have 32 numbers in a list => we have n lists each up to 32 elements
     *  => space is O(n)!
     *
     * -------------------
     * we can't optimize time, cause we'd need to check each element at least once always to consider including it, but
     * there might a better space solution (not dp though, since we result of the function is a list, and we need to clone
     * lists anyway to keep them separate)
     */
    fun topDown(nums: IntArray): List<Int> {
        nums.sort()

        var maxSubset = emptyList<Int>()
        val cache = mutableMapOf<Int, List<Int>>()
        for (leftInd in nums.indices) {
            val subset = dp(
                leftInd = leftInd,
                cache = cache,
                nums = nums,
            )
            if (subset.size > maxSubset.size) maxSubset = subset
        }
        return maxSubset
    }

    private fun dp(
        leftInd: Int,
        cache: MutableMap<Int, List<Int>>,
        nums: IntArray,
    ): List<Int> {
        val lastInd = nums.size - 1
        if (leftInd == lastInd) return listOf(nums[lastInd])

        val cachedResult = cache[leftInd]
        if (cachedResult != null) return cachedResult

        var maxSubset = emptyList<Int>()
        for (j in leftInd + 1 until nums.size) {
            if (nums[j] % nums[leftInd] == 0) {
                val subset = dp(leftInd = j, cache = cache, nums = nums)
                if (subset.size > maxSubset.size) maxSubset = subset
            }
        }
        return (listOf(nums[leftInd]) + maxSubset).also { result ->
            cache[leftInd] = result
        }
    }

    /**
     * Same idea => complexities as [topDown].
     *
     * How to improve space complexity from O(n^2)?
     *
     * If when computing for some leftInd we encounter jth num which is divisible by nums\[leftInd]
     *  => we take for dp\[leftInd] all nums from dp\[j] subset, but then we no longer need it in the cache, cause
     *  then any number less than nums\[leftInd] which would be divisible by nums\[leftInd] would always get all numbers
     *  from dp\[leftInd]
     *
     * how to reduce space required knowing that?
     */
    fun bottomUp(nums: IntArray): List<Int> {
        nums.sort()

        val dp = mutableMapOf<Int, List<Int>>()
        var totalMaxSubset = emptyList<Int>()
        for (leftInd in (nums.size - 1 downTo 0)) {
            var maxNextSubset = emptyList<Int>()
            for (j in leftInd + 1 until nums.size) {
                if (nums[j] % nums[leftInd] == 0) {
                    val subset = dp.getValue(j)
                    if (subset.size > maxNextSubset.size) maxNextSubset = subset
                }
            }
            val result = mutableListOf(nums[leftInd]).apply { addAll(maxNextSubset) }
            dp[leftInd] = result
            if (result.size > totalMaxSubset.size) totalMaxSubset = result
        }
        return totalMaxSubset
    }
}

fun main() {
    println(
        LargestDivisibleSubset().bottomUp(
            nums = intArrayOf(1, 2, 3),
        )
    )
}
