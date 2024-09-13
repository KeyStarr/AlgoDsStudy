package com.keystarr.algorithm.array.twopointers.slidingwindow

/**
 * ⭐️ a good example of the classic "count valid subarrays" problem with an unexpected twist which changes the algo quite a bit.
 *  got me good
 *
 * LC-930 https://leetcode.com/problems/binary-subarrays-with-sum/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.size <= 3 * 10^4;
 *  • nums\[i] is either 0 or 1;
 *  • 0 <= targetSum <= nums.size.
 *
 * Final notes:
 *  • ⚠️ done [suboptimalBetter] by myself in 45 mins (across 2 sessions with a dinner break);
 *  • ⚠️⚠️ deeply understood the [efficient] solution only at the 1h30m mark of total time invested into the problem;
 *  • I've made the key observation by myself quite early on, that we must count the prefix of zeroes for the current subarray
 *   and increment the valid subarrays count according to that, based [suboptimal] already on that.
 *   But it did cost me quite some time, as I first tried pure sliding window, then prefix sum + sliding window,
 *   then combined and at the FOURTH design attempt I realized the catch here.
 *
 * Value gained:
 *  • practiced solving a "count valid subarrays" type problem with an unusual twist using a sliding window.
 */
class BinarySubarraysWithSum {

    // TODO: retry in 1-2 weeks

    /**
     * goal - count all valid subarrays
     *  valid = total sum of numbers == targetSum
     *
     * we could try just the sliding window
     *  shrink:
     *   - when current sum > targetSum => move leftInd right and decrease sum by nums\[leftInd]
     *  expand:
     *   - add nums\[rightInd] to the sum;
     *   - update subarrays count if the sum == targetSum.
     *
     * I think the interesting part about that problem is that we may have targetSum == 0 and have multiple subarrays
     *  adhere to it since we may have multiple subsequent 0's
     *
     * edge cases:
     *  - sum => max sum = 3* 10^4 * 1 => fits into Int;
     *  - if targetSum == 0 and nums\[rightInd] == 1 => we move leftInd until its == rightInd+1 => make sure to not increase
     *   validSubarraysCount in that case, since sum is technically == [targetSum] but the subarray (rightInd+1,rightInd) doesn't exist.
     *
     * input = 0,0,0,0,0
     *
     * --------------------
     *
     * nope, let's try prefix sum + sliding window then.
     *
     * - compute prefix sum for [nums];
     * - do sliding window on the prefix sum array:
     *  shrink:
     *   - if current subarray sum > target => move leftInd right.
     *  expand:
     *   - compute the current subarray sum, if it equals to target => add to the validSubarraysCount subarrayLength;
     *    (why - cause the new element forms exactly subarrayLength subarrays with the current subarray)
     *
     *
     * aha! the fact that the validity criteria for the subarray is the sum match + we can have 0 elements is the tricky part:
     *  =>
     *  - by expanding the array with 0 elements OR by shrinking for 0 elements we don't change the sum;
     *  - if targetSum == 0 we technically just count the number of all subarrays of the parts of [nums] that consist of 0's only.
     *
     * => observation: when we find the target sum, increase the amount of valid subarrays by the amount of all subarrays
     *  that can be formed from the current main subarray by removing zeroes from its prefix
     *
     * 1, 0, 1, 0, 1
     *
     * valid subarrays:
     * 1 0 1
     * 1 0 1 0
     * 0 1 0 1
     * 1 0 1
     *
     *
     * Time: average/worst O(n^2)
     *  worst is when all nums are zeros => rightInd we do nums[:rightInd] iterations => O(n^2)
     * Space: always O(1)
     */
    fun suboptimal(nums: IntArray, targetSum: Int): Int {
        var leftInd = 0
        var validSubarraysCount = 0
        var sum = 0
        for (rightInd in nums.indices) {
            sum += nums[rightInd]

            while (sum > targetSum) {
                sum -= nums[leftInd]
                leftInd++
            }
            if (leftInd > rightInd) continue

            var prefixZeroes = 0
            var ind = leftInd
            while (ind <= rightInd) if (nums[ind++] == 0) prefixZeroes++ else break

            if (sum == targetSum) validSubarraysCount += prefixZeroes + if (prefixZeroes != rightInd - leftInd + 1) 1 else 0
        }
        return validSubarraysCount
    }

    /**
     * how to improve [suboptimal] time? ideally to O(n)
     *
     * a factor of n - zeroes prefix counting. Can we reduce it down to O(1)?
     *
     * 1, 0, 1, 0, 1 tS=2
     *
     * 1 0 0 1 tS=1
     *
     * as we encounter 1's store their indices in a queue? and pop once we shrink => front of the queue is always the
     *  first 1 in the current subarray, if it exists => prefix of 0's length is trivially computed then
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     *  worst case is all numbers are 1's => queue grows to n elements
     */
    fun suboptimalBetter(nums: IntArray, targetSum: Int): Int {
        var leftInd = 0
        var validSubarraysCount = 0
        var sum = 0
        val onesIndQueue = ArrayDeque<Int>()
        for (rightInd in nums.indices) {
            val newNumber = nums[rightInd]
            sum += newNumber
            if (newNumber == 1) onesIndQueue.addLast(rightInd)

            while (sum > targetSum) {
                val currentNumber = nums[leftInd]
                sum -= currentNumber
                if (currentNumber == 1) onesIndQueue.removeFirst()
                leftInd++
            }
            if (leftInd > rightInd) continue
            if (sum != targetSum) continue

            val subarraySize = rightInd - leftInd + 1
            val prefixZeroesSize = if (onesIndQueue.isNotEmpty() && onesIndQueue.first() in leftInd..rightInd) {
                onesIndQueue.first() - leftInd
            } else {
                subarraySize
            }
            validSubarraysCount += prefixZeroesSize + if (prefixZeroesSize != subarraySize) 1 else 0
        }
        return validSubarraysCount
    }

    /**
     * Can we optimize space to O(1) => do O(n) time O(1) space?
     *  1. either find O(1) space O(1) time way to compute zeroes prefix in [suboptimalBetter];
     *  2. or do a different approach altogether.
     *
     * try #1:
     *  I don't think that's possible, since we have to know the next exact ones location and 1s can be anywhere in [nums]
     *
     * since we're at 50 mins, I don't think it's optimal to try finding another approach if it exists => check the answers.
     *
     * --------------------- actual answer
     *
     * turns out that all the core design choices are correct, except that we can go approach #1 and indeed simply count zeroes prefix
     *  as we go with O(1) space
     *
     * if sum == targetSum simply "collect" the current zeroes prefix:
     *  while nums\[leftInd]==0 move leftInd  and increase the zeroes prefix counter
     * =>
     *  - while we expand, the prefix remains the same, and we use it to count all new subarrays with the new number;
     *  - when we shrink, we anyway would have skipped all zeroes in the prefix, since only 1's reduce the current sum
     *   => its convenient that out leftInd is already either at the first 1 in the current subarray or at the last element,
     *    since we need to make no to 1 step in order to match the target sum again
     *
     * Time: always O(n), since both leftInd and rightInd only move forward => both point to each element at most once;
     * Space: always O(1)
     *
     * learned thanks to https://leetcode.com/problems/binary-subarrays-with-sum/editorial/
     */
    fun efficient(nums: IntArray, targetSum: Int): Int {
        var leftInd = 0
        var validSubarraysCount = 0
        var sum = 0
        var zeroPrefixSize = 0
        for (rightInd in nums.indices) {
            val newNumber = nums[rightInd]
            sum += newNumber

            while (sum > targetSum) {
                // upon first iteration here nums[leftInd] always equals 1 => gotta zero out zeroPrefixSize before the next index
                if (nums[leftInd] == 1) zeroPrefixSize = 0
                sum -= nums[leftInd]
                leftInd++
            }
            if (leftInd > rightInd) continue
            if (sum != targetSum) continue

            // leftInd < rightInd not equals to avoid array out of bounds AND simplify the subarrays counting
            while (leftInd < rightInd && nums[leftInd] == 0) {
                zeroPrefixSize++
                leftInd++
            }
            validSubarraysCount += zeroPrefixSize + 1
        }
        return validSubarraysCount
    }
}

fun main() {
    println(
        BinarySubarraysWithSum().efficient(
            nums = intArrayOf(0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0),
            targetSum = 5,
        )
    )
}
