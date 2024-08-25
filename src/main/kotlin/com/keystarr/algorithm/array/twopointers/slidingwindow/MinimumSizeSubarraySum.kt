package com.keystarr.algorithm.array.twopointers.slidingwindow

import kotlin.math.min

/**
 * LC-209 https://leetcode.com/problems/minimum-size-subarray-sum/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= target <= 10^9;
 *  • 1 <= nums.length <= 10^5;
 *  • 1 <= nums\[i] <= 10^4;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • solved via [efficient] by myself in 30 mins;
 *  • leftInd can never exceed the array.size-1 because if and when leftInd=array.size-1 => subarraySum=0 and target>0
 *      => while condition always fails => we never get to array[array.size-1].
 *
 * Value gained:
 *  • this task subverts the trope of classic sliding window "shrink when the array is invalid" => instead we shrink
 *      the same iteration as the array becomes valid! and check for the best subarray inside this very shrinking loop!
 *      => I assume, there are many perfectly crafted subversion variations of common patterns => must be wary and
 *      re-check everything logically, NEVER falldown to automatic habits! Reconsider known patterns rigorously!
 *  • FAILED a submission => missed an edge case when there was NO valid subarray. All about 'em edge cases, huh;
 *  • FAILED THE FIRST implementation! decided to use the prefixsum and only half-way through the implementation realized
 *      we don't need it here. prefix sum is useful for COUNTING valid subarrays! For finding the SINGLE BEST VALID
 *      sliding window is enough! and sum can be achieved via a current subarray sum SINGLE variable, m8;
 *  • FAILED a bunch of runs first. Placed recording the best subarray erroneously outside the shrinking loop =>
 *      only when I dry RAN the algo I realized where it should be (got lucky also that the example input was so illustrative
 *      of that case) => STOP BEING LAZY, DRY RUN mediums ALWAYS before the first run?
 */
class MinimumSizeSubarraySum {

    /**
     * problem rephrase:
     * "find the best valid subarray, return its length:
     *  - valid: sum(subarray) >= target
     *  - best: minimal length"
     *
     * hints:
     *  - find the best valid subarray => sliding window
     *  - validity metric = subarray sum, and we consider subarrays starting from any index => prefix sum
     *
     * target>0
     * nums\[i]>0
     * condition is sum>=target
     * => after kth element when subarray sum is >= target with expanding it will always be that, BUT we search for the
     * minimum length valid subarray => just as soon as the subarray is valid we record its length AND shrink it straight away?
     * cause if we expand it more, the length increases and will never be better than the length we've just recorded anyway
     *
     * Idea:
     *  - precompute prefixSum array;
     *  - leftInd=0, rightInd=0, minLength=0;
     *  - implement a classic sliding window:
     *      - compute subarray [leftInd;rightInd] sum, if its valid => if its length is less than minLength, save it into minLength;
     *      - shrink the window if the subarray is invalid until it becomes valid again;
     *      - expand the subarray (rightInd in the for loop increment).
     *  - return minLength.
     *
     * Edge cases:
     *  - max subarray sum is 10^4*10^5=10^9, fits into Int;
     *  - nums.length==1 => return 1 (might early return);
     *  - nums.length==2 => correct;
     *  - THERE IS NO valid subarray => return 0, handled via default MAX_VALUE check.
     *
     * Time: O(n)
     * Space: O(1)
     */
    fun efficient(target: Int, nums: IntArray): Int {
        var minLength = Int.MAX_VALUE
        var currentSubarraySum = 0
        var leftInd = 0
        nums.forEachIndexed { rightInd, newNumber ->
            currentSubarraySum += newNumber
            if (currentSubarraySum < target) return@forEachIndexed

            while (currentSubarraySum >= target) {
                val currentSubarrayLength = rightInd - leftInd + 1
                if (currentSubarrayLength < minLength) minLength = currentSubarrayLength

                currentSubarraySum -= nums[leftInd]
                leftInd++
            }
        }

        return if (minLength == Int.MAX_VALUE) 0 else minLength
    }

    // TODO: solve the follow up, come up with O(nlogn) time (decided isnt worth it atm)
}

fun main() {
    println(MinimumSizeSubarraySum().efficient(7, intArrayOf(2, 3, 1, 2, 4, 3)))
}


/**
 * Didn't see I've already done that one before => done it in a clean slate! Decided to keep both notes.
 * 30 min => 10 min, nice speed improve, bro!
 *
 * 🔥 And I didn't even remember at all that I've already done this particular problem, not even a though, wow.
 * 3 months ago.
 *
 * --------------------------
 *
 * LC-209 https://leetcode.com/problems/minimum-size-subarray-sum/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= nums.size <= 10^4;
 *  - 1 <= nums\[i] <= 10^4;
 *  - 1 <= targetSum <= 10^9.
 *
 * Final notes:
 *  - done [efficient] by myself in 10 mins;
 *  - postponed the time O(nlogn) one, cause I feel like in near future the probability of such follow-ups for me is minimal.
 *
 * Value gained:
 *  - practiced recognizing and solving a best valid subarray type problem with sliding window.
 */
class MinimumSizeSubarraySumSecondTry {

    // TODO: do the time O(nlogn) solution, I don't think it's worth it rn

    /**
     * problem rephrase:
     *  - given:
     *   - nums: IntArray
     *   - targetSum: Int
     *  Goal: find the best valid subarray, return its length
     *   valid = all elements sum up to exactly target
     *   best = minimum length
     *
     * Approach #1:
     *  best valid subarray => efficiently try subarray candidates => sliding window:
     *   - shrink: sum is >= targetSum
     *   - expand: sum is < targetSum
     *
     * nums = 2 3 1 2 4 3
     * ts=7
     *
     * edge cases:
     *  - sum => max total [nums] sum is 10^9, fits into int;
     *  - there is no valid subarray => return 0, handle with if default value.
     *
     * Time: always O(n)
     * Space: always O(1)
     */
    fun efficient(targetSum: Int, nums: IntArray): Int {
        var left = 0
        var currentSum = 0
        var minValidSize = MIN_VALID_DEFAULT
        nums.forEachIndexed { right, num ->
            currentSum += num
            while (currentSum >= targetSum && left <= right) {
                currentSum -= nums[left]
                minValidSize = min(minValidSize, right - left + 1)
                left++
            }
        }
        return if (minValidSize == MIN_VALID_DEFAULT) 0 else minValidSize
    }

    /**
     * Follow-up: is there a time O(nlogn) solution?
     *
     * nlogn => probably we either sort or do binary search
     *
     * try sorting
     * nums=1 2 2 3 3 4
     * tS=7
     *
     * just sort and do the same algo :D technically O(nlogn) time!
     *
     * -----
     *
     * ok, another actual approach
     *
     * idea:
     *  - sort
     *  - take the ith element, then try to binary search (targetSum-nums\[ith]), if theres no such whole element,
     *   try taking the max element under that value and do the same, then the next max etc
     *
     * Time:
     *  - sorting O(nlogn)
     *  - main loop, worst is all elements are min and targetSum is max =>
     *   take the first element, binary search the next one
     *
     *  take first element, try n-1 elements for second,
     *   try n-2 elements for third etc
     *
     * 1 4 2
     *
     * 2 4 1
     *
     * 3 4
     */
    fun followUp(targetSum: Int, nums: IntArray): Int {
        TODO()
    }
}

private const val MIN_VALID_DEFAULT = Int.MAX_VALUE
