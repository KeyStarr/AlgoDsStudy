package com.keystarr.algorithm.dp.onedim

/**
 * ⭐️ the hardest and most unusual DP problem I've encountered so far
 * ❌ postponed the efficient solution design, couldn't understand it in a reasonable time frame
 * LC-494 https://leetcode.com/problems/target-sum/description/
 * difficulty: medium (imho, leet-hard)
 * constraints:
 *  - 1 <= nums.size <= 20;
 *  - 0 <= nums\[i] <= 10^3;
 *  - 0 <= sum(nums\[i]) <= 10^3;
 *  - -10^3 <= target <= 10^3.
 *
 * Final notes:
 *  - done [bruteBacktracking] pretty quickly by myself, but failed to design a more efficient solution in combined total at 50 mins;
 *  - ⚠️ turned down DP initially, cause I thought that the input state must include both `currentInd` and `leftSum` =>
 *   we'd have too little overlapping subproblems on average to change time complexity drastically;
 *  - it turns out that the efficient solution here is DP bottom-up space-optimized after all, and I still didn't quite get it why and how
 *   => postponed that solution design.
 *
 * Value gained:
 *  -
 */
class TargetSum {

    // TODO: according to https://leetcode.com/problems/target-sum/solutions/97334/java-15-ms-c-3-ms-o-ns-iterative-dp-solution-using-subset-sum-with-explanation/
    //  I should at first study https://leetcode.com/problems/partition-equal-subset-sum/description/
    //  and then ideally the knapsack DP problem
    //  => then use those to understand and design the actual efficient solution here. Since I'm short on time => postpone it

    fun bottomUpDp(nums: IntArray, targetSum: Int): Int {
        val sum = nums.sum()
        val cache = Array(size = nums.size + 1) { IntArray(size = sum * 2 + 1) }
        TODO()
    }

    /* TOP DOWN DP */

    private lateinit var cache: Array<IntArray>
    private lateinit var nums: IntArray
    private var targetSum: Int = -1
    private var sumCacheOffset: Int = -1

    /**
     * can we do faster? say, somewhere between O(n) and O(n^2) time?
     *
     * dp?
     * subproblems actually may overlap, like in the original example. If our input state is dp(currentInd, leftSum)
     * but the chance of it is small => no real impact on asymptotic complexities
     *
     * -------------------------------------------
     *
     * but we do have a binary decision tree basically.
     *
     * 2 4 3 7 2 target = 4
     *
     * -2 4 -3 7 -2
     * 2 4 3 -7 2
     * 2 -4 -3 7 2
     * 2 4 3 -7 2
     *
     *
     * 2 1 1 2 target = 4
     *
     *
     * observation:
     *  - we ALWAYS have to take ALL numbers once;
     *  - what are the cases of when there is more than 1 valid combination possible at all?
     *   - if we have 1 valid combination => sometimes there is a second with same REVERSED sign choices for the components
     *    which cancel out;
     *   -
     *
     * ---------------------------------------------
     *
     * checked the solution - https://www.youtube.com/watch?v=g0npyaQtAQM&ab_channel=NeetCode and tops on leet
     * => huh, it is dp after all! I was wrong when I decided that "chance of overlap is small => no real impact of asymptotic",
     * dead wrong
     *
     * Time:
     * Space:
     */
    fun topDownDp(nums: IntArray, targetSum: Int): Int {
        val sum = nums.sum()
        this.nums = nums
        this.targetSum = targetSum
        this.sumCacheOffset = sum
        this.cache = Array(size = nums.size) { IntArray(size = sum * 2 + 1) { -1 } }
        topDownDpRecursive(
            currentInd = 0,
            currentSum = 0,
        )
        return cache[0][sum]
    }

    /**
     * Goal - return the amount of combinations of nums[currentInd:], each made either positive on negative, such that
     *  the sum equals [leftSum].
     */
    private fun topDownDpRecursive(
        currentInd: Int,
        currentSum: Int,
    ): Int {
        if (currentInd == nums.size) return if (currentSum == targetSum) 1 else 0

        val sumCacheInd = if (currentSum < 0) (currentSum * -1) - 1 else currentSum + sumCacheOffset
        val cached = cache[currentInd][sumCacheInd]
        if (cached != -1) return cached

        val nextInd = currentInd + 1
        val currentNumber = nums[currentInd]
        val totalCombinations = topDownDpRecursive(
            currentInd = nextInd,
            currentSum = currentSum + currentNumber,
        ) + topDownDpRecursive(
            currentInd = nextInd,
            currentSum = currentSum - currentNumber,
        )
        return totalCombinations.also { result -> cache[currentInd][sumCacheInd] = result }
    }

    /* BRUTE FORCE = BACKTRACKING */

    /**
     * Goal - return the amount of all possible valid combinations.
     *  valid = assign signs to ALL numbers such that evaluating an expression amounts to the target
     *
     * brute force - try backtracking, actually generate all possible combinations but instead of saving each increment the counter
     *
     * since we're not required to return all possible combinations => there might be a more efficient solution rather than
     *  actually generating all
     *
     * if we're talking backtracking, how would it go?
     *  - function args:
     *   - currentInd: Int
     *   - currentSum: Int
     *  - how to proceed, when to prune?
     *   - we could pre-calculate sum of all elements and subtract each element we decide to use from it (or simply preprocess into a prefix sum array once from the end)
     *    => bail if (target-currentSum) < leftSum, i.o. given the choices so far, even if we assign + to all remaining elements,
     *     the target sum won't be reached (respecting the target sign)
     *   - otherwise, if it could be reached => proceed
     *
     * Time: always O(2^n)
     * Space: always O(n) for the callstack
     *
     * -----------------
     *
     * actually, we MUST take every number, the only question is => with which sign?
     *  so we have 2 choices for each position => resulting in 2^(nums.size) possible combinations total
     *
     * -------------
     *
     * optimized brute => prune when no matter what choices we make, we can't reach targetSum to be == 0?
     *  if leftSum > 0 => if positive sum of subarray nums[i:size-1] is less than leftSum
     *  if leftSum < 0 => same, but the sum of all values negative
     *
     * Time: average/worst O(2^n)
     * Space: same
     *  - added probably 2 prefix sum arrays for both all positives and all negatives => O(n)
     */
    fun brute(nums: IntArray, target: Int): Int = bruteBacktracking(
        nums = nums,
        currentInd = 0,
        leftSum = target,
        currentSum = 0,
    )

    /**
     * can't prune at leftSum == 0, since we may take further number as positive/negative => leftSum may actually reach 0
     * multiple times
     */
    private fun bruteBacktracking(
        nums: IntArray,
        currentInd: Int,
        leftSum: Int,
        currentSum: Int,
    ): Int {
        if (leftSum == 0 && currentInd == nums.size) {
            return 1
        }
        if (currentInd == nums.size) return 0

        val nextInd = currentInd + 1
        val number = nums[currentInd]

        val countWithPositive = bruteBacktracking(
            nums = nums,
            currentInd = nextInd,
            leftSum = leftSum - number,
            currentSum = currentSum + number,
        )
        val countWithNegative = bruteBacktracking(
            nums = nums,
            currentInd = nextInd,
            leftSum = leftSum + number,
            currentSum = currentSum - number,
        )
        return countWithPositive + countWithNegative
    }
}

fun main() {
    println(
        TargetSum().topDownDp(
            nums = intArrayOf(1, 1, 1, 1, 1),
            targetSum = 3,
        )
    )
}
