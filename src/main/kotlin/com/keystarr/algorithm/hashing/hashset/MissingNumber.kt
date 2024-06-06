package com.keystarr.algorithm.hashing.hashset

/**
 * LC-268 https://leetcode.com/problems/missing-number/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= n <= 10^4;
 *  • 0 <= nums\[i] <= nums.size;
 *  • nums is distinct.
 *
 * Final notes:
 *  • arrived at [suboptimal] by myself within 5-10 mins;
 *  • done [efficient] by myself with another 10 mins (20 mins total);
 *  • [efficient] wow, I'm amazed at how I didn't see this solution at all, until I have laid out my reasoning and narrowed
 *      the scope that way. The diffused thinking at work, but I don't feel it? Kind feel like it's not me who came up with it.
 *      Like, it's not my achievement. Why? Because it is!
 */
class MissingNumber {

    /**
     * Decided to preserve my line of reasoning, for funsies in the future.
     *
     * Brute:
     * - convert array into a set
     * - fill a second set from 0 to n
     * - check all numbers from the second set => if the number is not present in the first one => return it.
     * Time: O(n)
     * Space: O(n^2)
     *
     * Const space, suboptimal time:
     * - sort the array
     * - iterate from start to end, if diff between element ith and i+1 is exactly 2 => return nums\[i]+1
     * Time: O(nlogn)
     * Space: O(1)
     *
     * Suboptimal space, [suboptimal] implements this one:
     * - convert array into a set
     * - check numbers from the range, that is, run a loop with i=[0:n] check if each number is present in the set, if not,
     *  return it.
     * Time: O(n)
     * Space: O(n)
     */
    fun suboptimal(nums: IntArray): Int {
        val set = nums.toSet()
        for (i in 0..nums.size) if (!set.contains(i)) return i
        throw IllegalStateException("There must be at least 1 solution")
    }

    /**
     * - somehow don't use any data structures that require n space like array/hashset of n elements
     * - and don't sort
     *
     * goal - check each number at most C times and determine the missing one
     * ...
     * boom, my brain gave the solution! Find the missing number by checking the sums.
     *
     * Time: O(n)
     * Space: O(1)
     */
    fun efficient(nums: IntArray): Int {
        val numsSum = nums.sum()
        var fullSum = 0
        for (i in 0..nums.size) fullSum += i
        return fullSum - numsSum
    }

    // TODO: revisit the XOR solution, didn't get it straight away, not a priority rn
}
