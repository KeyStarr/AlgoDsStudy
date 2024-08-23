package com.keystarr.algorithm.deque.stack.monotonic

/**
 * 💣 went to practice exactly the monotonic stack, but failed to come up with a solution by myself => retry
 * ⭐ an amazing example of a hard monotonic stack problem with an additional advanced trick required. Though it looks super innocent.
 * LC-907 https://leetcode.com/problems/sum-of-subarray-minimums/submissions/1365910751/
 * difficulty: medium (imho, 100% leet-hard!)
 * constraints:
 *  • 1 <= nums.size <= 3 * 10^4;
 *  • 1 <= nums\[i] <= 3 * 10^4.
 *
 * Final notes:
 *  • in total the efficient solution required great grasp on 4 tools:
 *   • composure and solid problem-solving reasoning: to narrow down the problem to count all subarrays in which ith element is the min;
 *   • array mastery: to come up with the formula to count all subarrays that contain the Xth element;
 *   • monotonic stack: recognize that this subproblem can be efficiently incrementally  solved with a monotonic non-decreasing stack
 *    as we process the input array, and finish the remains in the stack;
 *   • modular arithmetic: to correctly update the answer each time using a modulo sum formula.
 *  • ⚠️️️️️️️️⚠️ I failed to solve the problem in 30 mins efficiently on my own:
 *   • narrowed it down very close, to count the subarrays containing the element;
 *   • but miserably failed to come up with a formula for subarray counting! Even after
 *    watching https://www.youtube.com/watch?v=aX1F2-DrBkQ struggled to understand it, then finally got it a few days later
 *    (maybe me being sick influenced the process);
 *   • cause I failed with the formula, didn't proceed to actually design the monotonic stack application.
 *
 * Value gained:
 *  • learned for the first time and learned to derive the formula for counting exactly how many subarrays of array Y contain element X;
 *  • practiced recognizing and using a monotonic stack to efficiently solve a problem with multiple tricky components to the solution,
 *   on subarrays.
 */
class SumOfSubarrayMinimums {

    // TODO: retry in 1-2 weeks

    /**
     * Post-summary, (almost) FULL SOLUTION RATIONALE STEPS:
     *
     *  goal: an array of integers, return the modulo of the sum of minimums across all subarrays;
     *
     *  1. we could try to design the solution around the concept of a single subarray, i.o. "what is the minimum for each subarray?
     *   bluntly, but that requires us to generate each subarray, which works but is O(n^2) time;
     *
     *  2. for an efficient solution, we should kinda reverse the focus of the question from subarrays to elements,
     *   to check each element once
     *              => in how many subarrays is ith element the minimum?
     *   trivially, since we're talking subarrays, we can only move sequentially left from the ith element and right from it
     *   => then the range in which there might even be subarrays that contain ith element as the min is bounded:
     *    - to the left by j = the leftmost index, inclusive, for which nums[j:i) > nums[i] is true;
     *    - to the right by k = the rightmost index, exclusive, for which nums(i;k) > nums[i] is true.
     *              => the longest subarray of nums in which nums\[i] is the min element is nums[j;k)
     *
     *  3.          => OK, but how many subarrays in the array nums[j;k) actually contain the ith number?
     *   - if we have consider only the array nums[i;k), that is, only elements to the right of the ith number
     *    => to contain the ith, each subarray must start with the ith number
     *    => we have exactly (k-i) subarrays that contain the ith number in that array
     *   - but in nums[j;k) we also have elements left to the ith number, which is nums[j;i), how to account for them?
     *   - well, we know how many subarrays we have to the right, and to each of them we can add, at first, the i-1th element,
     *    then, again to each, we may add the i-2th element etc, until we finally add the jth element
     *    => if we consider elements left to the ith, we have exactly new (i-j)*(k-i) subarrays
     *   => in total, we have all subarrays only with the rightmost elements and the ith + all subarrays with those AND also with the leftmost elements
     *   => the total amount of subarrays that contain the ith element
     *                                      = (k-i) + (k-i)*(i-j)
     *
     *  4. process numbers using the monotonic non-decreasing stack, each time we pop:
     *   - we found the k for the popped number, and we already have the i and j for it => compute the number of subarrays
     *    for it * number, add to the result using modular arithmetics;
     *   - and we must update the j for the current number, which now equals j for k.
     *    note: for i=0 j=0
     *   - if the loop terminated AND the stack isn't empty => pop it until its empty, apply the same logic as above to each
     *    element, only k=numbers.size for each then.
     *
     * 5. worst numOfSubs * ith number ~ 10^13, so compute it using a Long, add result to it and take a modulo with [DIVISOR] only then
     *  => standard modular sum.
     *
     *
     * Learned from https://www.youtube.com/watch?v=aX1F2-DrBkQ.
     *
     * ----------------
     *
     * problem rephrase:
     *  - given: an array of integers
     *  Goal: return the modulo of the sum of minimums across all subarrays.
     *
     * 3 1 2 4
     *
     *
     * 5 4 3 2 1
     *
     * 1 2 3 4 5
     *
     * how many subarrays contain element X?
     *
     * observations:
     *  1. for all subarrays of length 1 the minimum is the only element present;
     *  2. global minimum is the minimum for all subarrays containing it;
     *  3. next minimum after the global is the minimum for all subarrays that contain it and don't contain the global minimum
     *    => ith minimum in the array is the minimum for all subarrays that contain it and don't contain any minimums before it [0:i)
     *
     * -----------
     *
     * how many subarrays contain element X?
     *  => learned a formula
     * next question: what are the boundaries of a subarray in which, for each subarray containing the ith element,
     * ith element is the minimum?
     *
     * well, as much to the left and to the right as we can go where (j-1) and (k+1) are less than the ith element
     *
     * Edge cases:
     *  - worst `numsOfSubs * removedItem.number` is when removedItem.number is worst = 3 * 10^4, and
     *   numsOfSubs is worst => the middle element and worst size => (3*10^4)/2 -1 elements to the right and (3*10^4)/2 to the right => ~2* 10^9
     *   => 3*10^4 + 2*10^9 ~ 10^13 => either use Long for the temp sum or do modular arithmetics as well.
     *  - nums non-decreasing => compute at the end of the loop
     *
     * Time: always O(n)
     *  - across all loops each element of [nums] is exactly once added, once removed from the stack, both of which operations are O(1)
     * Space: average/worst O(n)
     *  - worst stack.size = nums.size if [nums] is monotonically non-decreasing
     */
    fun efficient(nums: IntArray): Int {
        val stack = ArrayList<Item>() // non-decreasing monotonic stack
        var result = 0
        nums.forEachIndexed { ind, number ->
            var leftInd = ind // the left index (inclusive) of a subarray of nums in which we count all subarrays that include the [number]
            while (stack.isNotEmpty() && number < stack.last().number) {
                val removedItem = stack.removeLast()
                result = result.updateResult(removedItem, rightIndExclusive = ind)
                leftInd = removedItem.leftInd
            }
            stack.add(Item(number = number, index = ind, leftInd = leftInd))
        }

        while (stack.isNotEmpty()) {
            val removedItem = stack.removeLast()
            result = result.updateResult(removedItem, rightIndExclusive = nums.size)
        }

        return result
    }

    private fun Int.updateResult(removedItem: Item, rightIndExclusive: Int): Int {
        val elementsToRightWithRemoved = rightIndExclusive - removedItem.index
        val elementsToLeft = removedItem.index - removedItem.leftInd
        val numOfSubs: Long = elementsToRightWithRemoved + elementsToRightWithRemoved * elementsToLeft.toLong()
        return ((this + numOfSubs * removedItem.number) % DIVISOR).toInt()
    }

    private class Item(
        val number: Int,
        val index: Int,
        // the leftmost boundary (inclusive) of the subarray of [nums] in which the [number] is the min element
        val leftInd: Int,
    )
}

private const val DIVISOR: Int = 1_000_000_000 + 7

/*
3 2 1

3 = 3
3 2 = 2
3 2 1 = 1
2 = 2
2 1 = 1
1 = 1
exp ans 3+4+3=10
*/
fun main() {
    println(
        SumOfSubarrayMinimums().efficient(
            intArrayOf(3, 2, 1),
        )
    )
}
