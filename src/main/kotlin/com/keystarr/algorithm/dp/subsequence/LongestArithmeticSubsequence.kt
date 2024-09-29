package com.keystarr.algorithm.dp.subsequence

import kotlin.math.max

/**
 * ⭐️ wow, one of the hardest problems I've ever encountered so far
 * ⭐️ an enigmatic example of a DP problem with a clean (albeit tricky) bottom-up approach yet a complicated top-down one
 *  (found only a few people even attempting it. maybe for a different reason though)
 *
 * LC-1027 https://leetcode.com/problems/longest-arithmetic-subsequence/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= nums.size <= 10^3;
 *  • 0 <= nums\[i] <= 500.
 *
 * Final notes:
 *  • done [greedyBruteForce] at about ~25-30 min mark by myself;
 *  • ⚠️⚠️ failed to come up with an efficient approach under 1h => checked the solution;
 *  • 🏅 its great that I forced myself to stop, since I had to invest 1.5h just to understand the actual efficient solution!
 *   It was very non-trivial to me for some reason;
 *  • 🔥 so sometimes WE CAN actually do DP from both ways! Here either build the subsequence from the firstInd [efficientFirstInd]
 *   or the lastInd [efficientLastInd] => iterate from opposite ends!
 *  • good thing I forced myself to go for [greedyBruteForce] after failing to come up with the efficient approach in ~20 mins;
 *  • 🏆 I actually could have optimized [greedyBruteForce] had I dry ran it on the simplest example possible (say 1,2,3,4 the entire input is a valid AS)
 *   and seen exactly which subproblems duplicate and how to try to cache the results! Which, as an afterthought, is
 *   pretty obvious here [efficientFirstInd] - just try all steps, and check if there's a subsequence with that step already
 *   starting at the secondInd!!!
 *   🔥🔥🔥 why did I not go there? I feel like I simply lacked confidence and gave up. But if I believed in myself, I could have
 *   tried to dry run and actually could have discovered the optimization live
 *
 * Value gained:
 *  • practiced solving a "find the longest valid subsequence in an array" type problem efficiently using bottom-up DP.
 */
class LongestArithmeticSubsequence {

    // TODO: full re-solve in 1-3 weeks

    /**
     * done by myself after [efficientLastInd] as an experiment, to try to understand how I could have optimized from
     * [greedyBruteForce] directly.
     *
     * ------------
     *
     * note that in [greedyBruteForce] we have needless computations of duplicate subproblems:
     *  e.g. nums=[1,2,3,4]
     *  firstNum=1 secondNum=2 => step=1 => sequence length 4 (1,2,3,4)
     *  but then when we have firstNum=2 secondNum=3 => step=1 we count (2,3,4) again, even though we already know
     *   that for the sequence starting with 2 and step 1 the length is 3
     *  => try DP
     *
     * goal: return the maximum subsequence length of nums with the first number at nums\[firstInd] and a step of `step`
     * input state:
     *  - firstInd: Int - index of the first number in the subsequence
     *  - step: Int - the step of the subsequence
     * recurrence relation:
     *  since we don't really do top-down here, lets just do bottom-up straight away
     *
     *  basically we start at the end of the array and try for the firstInd all possible second numbers to try all possible steps.
     *  each dp\[firstInd]\[step] = dp\[secondInd]\[step] for we would have already computed all possible steps for the secondInd
     *  and know all possible subsequences lengths that start with the secondInd
     *
     * i.o. we optimize [greedyBruteForce] by not computing the full subsequence length for every possible subsequence,
     *  instead we traverse backwards and build upon the already made computations using tabulation => bottom-up DP
     *
     * Time: always O(n^2)
     * Space: always O(n*m), where n=nums.size; m=step range
     */
    fun efficientFirstInd(nums: IntArray): Int {
        val cache = Array(size = nums.size) { IntArray(size = 1001) } // endNumber x step
        var totalMaxLen = 0
        for (firstInd in nums.lastIndex downTo 0) {
            for (secondInd in nums.lastIndex downTo (firstInd + 1)) {
                val stepKey = (nums[secondInd] - nums[firstInd]) + 500
                cache[firstInd][stepKey] = if (cache[secondInd][stepKey] != 0) cache[secondInd][stepKey] + 1 else 2
                totalMaxLen = max(cache[firstInd][stepKey], totalMaxLen)
            }
        }
        return totalMaxLen
    }

    /**
    ------------------------ second approach
     *
     * how to do better than O(n^3) time?
     *  two stages for improvement
     *   1. how to the start the subsequence (optimally select first 2 elements);
     *   2. how to select all further elements once the step is established.
     *
     * maybe DP after all?
     *
     * try DP where state is the first and the second numbers?
     *
     * what if we try DP where we only have startInd number and the step?
     *
     * for i in nums.indices:
     *  for step in 0..500:
     *   dp(startInd=i+1,target=nums\[i]+step, step)
     *
     * dp(startInd,target,step) = max(take, skip)
     *  take = if (nums\[startInd] == target) 1 + dp(startInd+1,step) else 0
     *  skip = dp(startInd=startInd+1, step)
     *
     * (violating the greedy rule of first, but OK try)
     *
     * Time: O(n^2*m^3)
     *  - n*m subsequences starts;
     *  - ~n*m*m states;
     *  - each state takes O(1) work.
     * Space: O(n*m^2)
     *
     * oh my what have I designed. basically the same as the first but much much worse since we violate that simple greedy rule.
     *  if we have the start of the subsequence established there is literally NO NEED in DP, its just straight forward
     *  plain greedy DFS.
     *
     * -------------------------- third approach
     *
     * can we somehow make DP WITH the subsequence start embedded?
     *
     * note that basically it makes sense to try each possible step exactly once for each distinct nums\[i]
     * but well the 1st approach is already based on that
     *
     * or some other radically different approach?
     *
     *
     * for i in nums.indices:
     *  for step in -500..500:
     *   last=nums\[i]
     *   for (j in i+1..nums.lastIndex):
     *      if (nums[j] != last+step
     *
     * oh, also note that step can be negative => we have step: [-500:500], so worst m~n
     * => this will be like O(n*m*n) ~ O(n^3) time asymptotically same as [greedyBruteForce]
     *
     * ------------------------
     *
     * DP goal: what's the longest AS ending with endInd?
     * input state:
     *  endInd: Int
     *  step: Int
     *
     * dp(startInd,step) = dp[]
     *
     *
     * for lastInd in 0..nums.lastIndex:
     *     for prevLastInd in (lastInd-1) downTo 0:
     *         step = nums[lastInd] - nums[prevLastInd]
     *         dp[nums[lastInd]][step] = dp[nums[prevLastInd]] + 1
     *
     * ------------------------- an actual efficient approach
     *
     * Discovered and understood thanks to https://www.youtube.com/watch?v=_xIkHvSZpDE&t=145s&ab_channel=C0deSutra and
     * https://www.youtube.com/watch?v=-NIlLdVKBFs&ab_channel=HappyCoding and top solutions on leet.
     *
     * Since I did brute with firstInd/secondInd it was quite hard to understand at first, for this is an opposite solution
     * building sequence-wise.
     *
     * DP goal: return the max length of the subsequence of [nums] that ends with nums\[lastInd] and has a step of `step`.
     * input state:
     *  lastInd: Int - the index of the last number in the subsequence
     *  step: Int - the step of the subsequence
     *
     * We start from the beginning of [nums] and then for each lastInd we try all possible steps i.o. try all possible
     *  prevLastInd and compute the step based on those
     *  => dp\[lastInd]\[step] = if (dp\[prevLastInd]\[step] > 0) dp\[prevLastInd]\[step] + 1 else 2
     *
     * Time: always O(n^2)
     * Space: always O(n*m)
     */
    fun efficientLastInd(nums: IntArray): Int {
        val cache = Array(size = nums.size) { IntArray(size = 1001) } // endNumber x step
        var totalMaxLen = 0
        for (lastInd in 1..nums.lastIndex) {
            for (prevLastInd in 0 until lastInd) {
                val stepKey = (nums[lastInd] - nums[prevLastInd]) + 500
                cache[lastInd][stepKey] = if (cache[prevLastInd][stepKey] != 0) cache[prevLastInd][stepKey] + 1 else 2
                totalMaxLen = max(cache[lastInd][stepKey], totalMaxLen)
            }
        }
        return totalMaxLen
    }

    /**
     * problem rephrase:
     *  given:
     *   - nums: IntArray
     *  goal: find the best valid combination
     *   combination = subsequence;
     *   valid = arithmetic, i.o. subseq\[i] = subseq\[i-1] is const throughout;
     *   best = max length.
     *
     * we have many choices to make, each choice affects further choices (for if we select elements X and Y, we must find Z such that Z=Y-X)
     * subproblems may overlap => try DP
     *
     * top-down DP
     *
     * goal: the original goal
     * input state:
     *  - startInd: Int
     *  - last: Int
     *  - step: In
     * recurrence relation:
     *  dp(startInd,last,step) = max(take,skip)
     *   take = if (nums\[startInd] - last == step) 1 + dp(startInd+1,nums\[startInd],step) else 0
     *   skip = dp(startInd+1, last,step)
     * base cases:
     *  - startInd == nums.size => return 0
     *
     * Time: average/worst O(n*m*k) = O(2500 * n) = O(n)
     *  - n*m*k states where n=nums.size, m=500, step=500;
     *  - O(1) work at each state;
     * Space: average/worst O(n*m*k)
     *
     * worst time is 1000*500*500=25*10^7 => will fit under 1 sec
     *
     * ----- optimization
     *
     * there might be a better recurrence relation to reduce the amount of states while keeping the work at each step minimal
     * current is just the simplest way to start with
     *
     * actually, its best for us to simply take the first matching element for a subsequence with the start == X
     * since if there are multiple valid next elements, if we take the first we can always consider all next elements
     * as if take any of the latter X's duplicates, but not vice versa, some next options may be lost if we take a further X
     * => greedily take the first valid element every time
     *
     * oh, the catch is that we need 2 elements to start a subsequence, to identify the step. or 1 element and a step.
     *
     * we can try all 2 elements possible for the start => then always greedily choose the next valid element
     *
     * use sets since we greedily choose the first next element in the subsequence, there's no point in trying further duplicates
     * see [dfs] for further rationale
     *
     * =>
     * ------------------------ greedy bruteforce
     *
     * approach:
     *  - try starting every possible valid subsequence (trying every pair of 2 elements to start with);
     *  - for every possible subsequence start choose all possible next elements by the greedy rule: choose the first valid element.
     *
     * Edge cases:
     *  - nums.size == 2 => third loop's body will never be entered => a length of 2 will always be counter, which is corrected,
     *   expected is always return 2 (since any 2 elements form a valid arithmetic sequence).
     *
     * Time: average/worst O(n^3)
     *  all three loops have ~n iterations
     *  (10^3)^3=10^9, fits under 1 sec
     *
     * Space: average/worst O(n)
     *  both sets take up to n elements
     *
     * actually we may make a single set startSet with Pair<Int,Int> for the first two elements to reduce the time const more
     */
    fun greedyBruteForce(nums: IntArray): Int {
        var maxLength = 0
        val firstSeen = mutableSetOf<Int>()
        for (firstInd in 0..nums.lastIndex) {
            val first = nums[firstInd]
            if (firstSeen.contains(first)) continue

            val secondSeen = mutableSetOf<Int>()
            for (secondInd in (firstInd + 1)..nums.lastIndex) {
                val second = nums[secondInd]
                if (secondSeen.contains(second)) continue

                val step = second - first
                var last = second
                var length = 2
                for (nextInd in (secondInd + 1)..nums.lastIndex) {
                    val next = nums[nextInd]
                    if (next - last != step) continue
                    last = next
                    length++
                }
                maxLength = max(length, maxLength)
            }
            firstSeen.add(first)
        }
        return maxLength
    }
}

fun main() {
    val input = intArrayOf(27, 23, 27, 31, 35)
    println(LongestArithmeticSubsequence().efficientFirstInd(input))
    println(LongestArithmeticSubsequence().efficientLastInd(input))
}
