package com.keystarr.algorithm.dp.knapsack.zero_one

/**
 * ⭐️ the hardest and most unusual DP problem I've encountered so far
 * ❌ postponed the efficient solution design, couldn't understand it in a reasonable time frame (bottom-up with subset sum)
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

/**
 * Re-solved when finally got around to learn the abstract patterns for "0-1 Knapsack" and the "Unbounded Knapsack",
 *  this one being a prime example for the latter.
 *
 * Still decided to skip the bottom-up subset sum solution, since I feel like its out of scope for these patterns purely,
 *  and it surely isn't the bottleneck on our current path rn.
 */
class TargetSum2 {

    /**
     * problem rephrase:
     *  goal: return the number of valid combinations that can be built from [nums]
     *   valid:
     *    - all elements sum up to [targetSum];
     *    - each element from [nums] MUST be included only once, with its original sign or the inverse;
     *    - each combination is distinct by signs chosen for each index.
     *
     * try dp
     * basically we can either take or skip any element, and have some metric the combination must fit  => 0-1 knapsack?
     *
     * input state:
     *  - startInd: Int
     *  - leftSum: Int
     *
     * recurrence relation:
     *  dp(startInd, leftSum) = positive + negative
     *      positive = dp(startInd+1, leftSum - nums[startInd])
     *      negative = dp(startInd+1, leftSum + nums[startInd])
     *
     *  rationale: we try every DISTINCT combination possible. We make sure its distinct cause within the context of each single path
     *    through the decision tree we consider every element exactly once in both its possible states (with a given leftSum)
     *
     *    we can't prune if leftSum doesn't match the sign of [targetSum] since values in the combination can be both
     *    negative and positive => leftSum can change sign across a single path.
     *
     * base cases:
     *  - startInd == nums.size - 1:
     *      - leftSum == 0 => return 1; (we must use ALL elements!)
     *      - else return 0.
     *
     * Time: average O(n*k), worst is O(2^n)
     *  - we have n*k states where n=nums.size since we can arrive at any startInd with any leftSum. k=boundaries for
     *   the leftSum, which actually are: worst positive is 20*10^3=2*10^4, worst negative is then -2*10^4 => given the dispersion
     *   of values its better to use a hashmap than the array for the top-down;
     *
     *  - the decision tree has the width of 2^n since we have 2 branches at every node, and the height of n
     *   worst unique states is then 2^n, but we may not compute all states due to startInd+leftSum overlapping.
     *   so basically we compute up to O(n*k) states, never more than this, and worst n*k=2^n (all intermediate
     *   combinations possible turned out to be unique);
     *
     *  - at each state we do O(1) work.
     *
     * Space: average/worst O(n*k), worst O(2^n)
     *  since we'll use a map due to the values dispersion and huge k values spaces - actually the storage used will be
     *  less than n*k, but worst is, when all intermediate combinations are unique => we store O(2^n) basically we cache a state for every node.
     *
     * ----------------
     *
     * bottom-up as-is is technically possible, but, at least from the first look: we'd have to compute all theoretically
     *  possible states of leftSum which could vary in [-2*10^4;2*10^4] which is incredibly slower time const compared to the
     *  top down since then we'd only compute those which actually exist.
     *
     * BUT with bottom-up we'd optimize space to O(m), since current row depends only on the next row's results
     *
     * => no point to implement bottom-up here given the constraints, education purposes - effort/value doesnt do well here for me
     *
     * (aside from that other efficient bottom-up subset sum DP)
     */
    fun topDownDp(nums: IntArray, targetSum: Int): Int = topDownDpRecursive(
        startInd = 0,
        leftSum = targetSum,
        cache = Array(size = nums.size) { mutableMapOf() },
        nums = nums,
    )

    private fun topDownDpRecursive(
        startInd: Int,
        leftSum: Int,
        cache: Array<MutableMap<Int, Int>>,
        nums: IntArray,
    ): Int {
        if (startInd == nums.size) return if (leftSum == 0) 1 else 0

        val cachedResult = cache[startInd][leftSum]
        if (cachedResult != null) return cachedResult

        val nextInd = startInd + 1
        val positive = topDownDpRecursive(startInd = nextInd, leftSum = leftSum - nums[startInd], cache, nums)
        val negative = topDownDpRecursive(startInd = nextInd, leftSum = leftSum + nums[startInd], cache, nums)
        return (positive + negative).also { cache[startInd][leftSum] = it }
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
