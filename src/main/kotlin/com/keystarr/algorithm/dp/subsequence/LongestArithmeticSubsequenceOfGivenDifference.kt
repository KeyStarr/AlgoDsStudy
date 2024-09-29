package com.keystarr.algorithm.dp.subsequence

import kotlin.math.max

/**
 * ⭐️ a unique (so far) example of a DP problem where top-down is only O(n^2), but bottom-up trivially gives O(n) time!
 * LC-1218 https://leetcode.com/problems/longest-arithmetic-subsequence-of-given-difference/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.length <= 10^5;
 *  • -10^4 <= nums\[i], targetDiff <= 10^4.
 *
 * Final notes:
 *  • done [startsAtTopDownDp] in 40 mins, got TLE on 38th/41 tests. Improved the time but only const => submit success at 51 mins :D
 *   But basically its just improved brute force, there obviously is a much better solution asymptotically. Since I'm near
 *   the time limit of 1h, and I don't see any leads to improve the current solution => just learn the official solution then;
 *
 *  • ⚠️⚠️ chose the wrong end to start DP from  => again! Just as in  earlier TODAY.
 *   should've not postponed time estimation => would've caught early on that worst is still O(n^2) and that there's gotta be
 *   a better way => should've invested time in finding that?
 *    ⚠️ I doubt however I'd see the other end approach though since I just fixated upon the chosen one like its the only way to do DP here..
 *
 *  • unlike [com.keystarr.algorithm.dp.multidim.HouseRobberII] where I chose the wrong end to start from, here I just
 *   never done the bottom-up! It turns out that here, no matter from which end we start (either we search for the max length
 *   of the subsequence that STARTS or ENDS with nums\[i]), the best top-down is O(n^2) but TRIVIALLY bottom-up from either
 *   side is O(n)!
 *   => 🔥🔥💡 should've just pushed and done bottom-up, as routinely at least that sometimes gives space optimizations
 *   => here would've discovered a huge time optimization as well!!!
 *
 *  • i.o. done [startsAtTopDownDp] optimized a const but got stuck at O(n^2) and gave up. It turns out that top down from another
 *   direction has basically same time complexity [endsAtTopDownDp], BUT bottom-up from both direction is efficient O(n)
 *   [startsAtBottomUpDp] [endsAtBottomUpDp]!!!
 *
 *  • basically a subproblem of [LongestArithmeticSubsequence] almost as-is.
 *
 * Value gained:
 *  • for the first time encountered a case where bottom-up is trivially drastically faster than top-down! And when starting
 *  from either side here! Made a mistake of stopping at top-down and trying to optimize it instead of the trying the bottom-up straight away.
 *  => 🔥💡 in the future if top-down DP looks too slow: try another side, or just straight away try bottom-up first, and only THEN
 *   change sides!!!
 *
 *  • practiced recognizing solving a "find the best valid combination=subsequence" type problem using a one-dim DP bottom-up, efficiently.
 */
class LongestArithmeticSubsequenceOfGivenDifference {

    // TODO: full resolve in 1-3 weeks, and then again probably!

    /* DP goal - return the max length of the valid subsequence in the subarray nums[:rightInd] that END with the number nums[rightInd] */

    /**
     * Goal: return the length of the longest arithmetic subsequence in nums[:rightInd] that end with number nums\[rightInd] with a step of [targetDiff].
     *  start with rightInd = 0
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     *  worst is all numbers in [nums] are distinct => exactly n entries in the map
     */
    fun endsAtBottomUpDp(nums: IntArray, targetDiff: Int): Int {
        val subseqEndNumToLengthMap = mutableMapOf<Int, Int>()
        var maxLength = 0
        for (leftInd in nums.indices) {
            val currentNum = nums[leftInd]
            val prevNum = currentNum - targetDiff
            val length = subseqEndNumToLengthMap.getOrDefault(prevNum, 0) + 1
            subseqEndNumToLengthMap[currentNum] = length
            maxLength = max(maxLength, length)
        }
        return maxLength
    }

    /**
     * - goal: return the length of the longest arithmetic subsequence in nums[:rightInd] that ends with number nums\[rightInd] with a step of [targetDiff].
     *  start with rightInd = nums.size - 1 (full problem's input)
     *
     *  return value: Int, length of the best subsequence
     * - input state: rightInd Int, denotes the right boundary of the subarray of nums starting from nums.size-1, which is the input
     *  of the current subproblem;
     * - recurrence relation:
     *  dp(rightInd) = for i in 0 until rightInd, if nums\[i] == (nums\[rightInd] - targetDiff) dp(rightInd = currentInd) + 1
     * - base cases:
     *  rightInd == 0 => return 1, in the subarray of nums[0] that single number forms only a valid subsequence with itself;
     *
     * Time: average/worst O(n^2)
     *  worst case is there are no 2 numbers in [nums] such that the diff between right and left of them == [targetDiff]
     *   => for each number we check on average n/2 numbers to the left of it
     *   => n numbers, n/2 average => O(n^2)
     *  best case is that all numbers in [nums] form a valid subsequence => for dp(rightInd=nums.size-1) we compute the result
     *   for each number => of further iterations we simply read from cache => O(n)
     *  => average is in the middle of these, depends on input patterns. The more valid subsequences we have, the more time
     *   we save.
     * Space: O(n)
     */
    fun endsAtTopDownDp(nums: IntArray, targetDiff: Int) {}


    /* DP goal - return the max length of the valid subsequence in the subarray nums[leftInd:] that STARTS with the number nums[leftInd] */

    /**
     * goal: return the maximum longest subsequence of the subarray nums[leftInd:] that STARTS with the number nums\[leftInd]
     *  such that the diff between all adjacent elements is exactly [targetDiff].
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     *  worst is all numbers in [nums] are distinct => n entries in the map
     */
    fun startsAtBottomUpDp(nums: IntArray, targetDiff: Int): Int {
        val map = mutableMapOf<Int, Int>()
        var maxLength = 0
        for (leftInd in nums.lastIndex downTo 0) {
            val currentNumber = nums[leftInd]
            val nextNumber = currentNumber + targetDiff
            val length = map.getOrDefault(nextNumber, 0) + 1
            map[currentNumber] = length
            maxLength = max(length, maxLength)
        }
        return maxLength
    }

    /* same goal but top down */

    private lateinit var nums: IntArray
    private lateinit var cache: IntArray
    private var targetDiff: Int = -1

    /**
     * Can we improve [startsAtTopDownDp]?
     *
     * The part of the algorithm which gives the additional factor of n is trying to find the first occurrence of targetNextValue,
     *  on average/worst it takes O(n) time as we have to manually check all elements to the right of the current one if there is no match
     * => can we optimize that?
     *
     * we could pre-process [nums] into number -> List<Int> of indices of occurrences of that number in [nums].
     * then as we need the number X, we do positions\[X] and binary search a position that is greater than current leftInd
     * => Time: O(nlogn), actually O(nlogk) where k=average number of occurrences of each number, but k depends on n. logn for binary search
     *  Space: O(n + n + n) = O(n)
     *   - in the positions hashmap we'd have g entries, g=number of distinct numbers in the [nums], worst is g=n, and
     *    across all positions lists we'd have always exactly n values
     */
    fun startsAtSuboptimalTopDown() {}

    /**
     * subsequence and 10^5 max numbers size => how to approach this?
     *
     * probably optimal solution should be either O(n) or O(nlogn)
     *
     * observe:
     *  1. if diff > 0 we have a strictly increasing subsequence;
     *  2. if diff < 0 => strictly decreasing;
     *  3. if diff == 0 => the longest subsequence of equal elements.
     *
     * how to choose the first element?
     *
     * e.g. 3 5 6 7 8 10 12, diff=2
     *
     * 3 5 7
     * 6 8 10 12
     *
     * brute force:
     *  - start with each element once, then choose the next element with the required diff, then again => update maxLength
     *   if resulting subsequence length is the max encountered
     * Time: average/worst O(n^2)
     *  worst is when all adjacent elements in nums have diff [targetDiff], then we do approximately n*n/2 iterations
     * Space O(1)
     *
     * ---------- how to optimize? ----------
     *
     *  basically goal = find the best valid combination, return the metric
     *   valid = a valid subsequence (only elements from [nums], a single element can be used exactly once, order) with
     *   adjacent elements diff of exactly [targetDiff]
     *   metric = length
     *   best = max length
     *
     * observation: actually, we don't need to return the subsequence => we just need the length of longest!
     *
     * what if we sort [nums]? no can do - order matters, since the constraint is for adjacent elements in the original order
     *
     * may subproblems overlap?
     *
     *  consider nums=3 5 6 7 8 10 12, targetDiff=2
     *
     *  one valid subsequence is: 6 8 10 12
     *   we discover it as we start from 6
     *   note: when we start from 8, since targetDiff is the same, we already know that the longest subsequence of nums[4:6] is 2,
     *    as we would have calculated it when we tried it for 6
     *  => subproblems do overlap => try dp!
     *
     * top-down DP:
     *  - goal: return the maximum longest subsequence of the subarray nums[leftInd:] starting from the number nums\[leftInd] (!!!!! what i've missed to see)
     *   such that the diff between all adjacent elements is exactly [targetDiff];
     *   return value: max length, integer
     *  - input state:
     *   leftInd: Int - denotes the start of the subarray of [nums] that is the current suproblem's input;
     *  - recurrence equation:
     *   dp(lI) = if nums has value == lI+targetDiff => dp(nextIndex) + 1 else 1
     *  - base cases:
     *   lI == nums.size => return 0
     *
     * nums=3 6 8 5 10 7 12, targetDiff=2
     *
     * dp= 0:3,3:2, 5:1
     *
     * dp(lI=0)
     * targetNext=3+2=5
     *  nI=1, 6 != 5
     *  nI=2, 8 != 5
     *  nI=3, 5==5
     * dp(0) = dp(3) + 1 = 2 + 1 = 3
     *
     * dp(lI=3)
     * targetNext=5+2=7
     *  nI=4, 10 !=5
     *  nI=5, 7==7
     * dp(3) = dp(5) + 1 = 1 + 1 = 2
     *
     * dp(lI=5)
     *  targetNext=7+2=9
     *  nI=6, 12 != 5
     *  nI=7, base case, return 0
     *  dp(5) = 1
     *
     * edge cases:
     *  - nums.length == 1 => expected is always return 1 => correct as-is, loop's body will never be entered in [startsAtTopDownRecursive].
     *
     * Time: ⚠️ average/worst O(n^2) still same as brute, only a better const on average.
     *  (🏆 HAH, postponed time estimation coz it looked complex, didn't know where to start => 🔥focused attention through the "worst case" mode, and it all clicked!)
     *  worst case is there are no 2 numbers in [nums] such that the diff between them == [targetDiff] => we launch [startsAtTopDownRecursive]
     *  with each number, and check every other number to the right of it => still O(n^2)
     * Space: always O(n)
     *
     *
     * => WRONG - TLE at 38th/41 test case
     *
     * --------
     * How to optimize [startsAtTopDownDp] even further, to actually get below O(n^2) time?
     *
     * binary search on solution spaces? no, cause suppose target length no less/no greater than X, how to check it?
     *  we have to start at each number => O(n^2) time, doesn't make sense
     *
     * DP could still be optimized for equal numbers => if we've already encountered a number X, computed the result for it,
     *  we should skip all numbers equal to it! Proof is laid out at [startsAtTopDownRecursive]:
     *   a duplicate number to a previously encountered one can never have greater valid subsequence length)
     *
     * => that still only improves the const, worst is still O(n^2) time.
     * => LOL, submit success :D with 2482 ms time, where 423 ms is the best))))))))))))))
     */
    fun startsAtTopDownDp(nums: IntArray, targetDiff: Int): Int {
        this.nums = nums
        this.cache = IntArray(size = nums.size) { -1 }
        this.targetDiff = targetDiff
        val seen = mutableSetOf<Int>()
        var maxLength = 1
        for (leftInd in nums.indices) {
            val currentNumber = nums[leftInd]
            if (seen.contains(currentNumber)) continue

            maxLength = max(maxLength, startsAtTopDownRecursive(leftInd = leftInd))
            seen.add(currentNumber)
        }
        return maxLength
    }

    /**
     * Always stop at and choose the first matching number with [targetDiff], cause
     *
     * what if there are multiple numbers that equal to currentNum + targetDiff?
     *  always choose the first one, cause if further there are any numbers that equal to nextNum+targetDiff:
     *   - after all equal nextNumber => we may choose that nextNext number picking whatever duplicate as the nextNumber;
     *   - in between nextNumber duplicates => we want to select the first one, so we could choose that nextNext number and not loose it.
     *    if there are duplicates of that nextNext number if we choose the first nextNumber we will have access to all these
     *    nextNext number duplicates, and we should choose the first of those then, following the same logic recursively.
     *
     *  nums= 10 7 12 10 14 12 14 16 12 10, targetDiff=2
     *
     */
    private fun startsAtTopDownRecursive(leftInd: Int): Int {
        if (leftInd == nums.size) return 0

        val cached = cache[leftInd]
        if (cached != -1) return cached

        val currentNum = nums[leftInd]
        for (nextInd in (leftInd + 1)..nums.lastIndex) {
            if (nums[nextInd] - currentNum == targetDiff) {
                val maxLength = startsAtTopDownRecursive(leftInd = nextInd) + 1
                cache[leftInd] = maxLength
                return maxLength
            }
        }

        val maxLength = 1
        cache[leftInd] = maxLength
        return maxLength
    }
}

fun main() {
    println(
        LongestArithmeticSubsequenceOfGivenDifference().endsAtBottomUpDp(
            nums = intArrayOf(1, 2, 3, 4),
            targetDiff = 1,
        )
    )
}
