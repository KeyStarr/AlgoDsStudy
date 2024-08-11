package com.keystarr.algorithm.array.twopointers.slidingwindow

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
