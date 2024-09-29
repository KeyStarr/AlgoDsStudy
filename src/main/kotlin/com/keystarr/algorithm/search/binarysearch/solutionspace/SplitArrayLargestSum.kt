package com.keystarr.algorithm.search.binarysearch.solutionspace

/**
 * ⭐️LC-410 https://leetcode.com/problems/split-array-largest-sum/description/
 * difficulty: HARD
 * constraints:
 *  • 1 <= nums.length <= 1000
 *  • 0 <= nums\[i] <= 10^6
 *  • 1 <= k <= min(50, nums.length)
 *
 * Final notes:
 *  • done via [efficient] by myself in ~50 mins (to prove binary search applicability and to adapt the pattern here);
 *  • the core of the problem is the same as [DivideChocolate], but the goal is reversed (here it's minimized largest sum,
 *   there it's maximized smallest sum).
 *
 * Value gained:
 *  • during an interview, I'm confident with 146% probability, that is it possible to solve a problem on binary search
 *   solution spaces ONLY having learnt and really practiced the binary search solution spaces pattern numerous times!
 *   No probable way to come up with it on the spot. Period. The amount of people expressing pain in the editorial supports
 *   that model;
 *  • practiced binary search on solution spaces with the goal on minimization;
 *  • I'm starting to REALLY see both intuitively and rationally the pattern here with when it's applicable to use binary
 *   search on a solution space and when exactly and why we terminate (---+++ or +++--- and where is left, where is right here!)
 */
class SplitArrayLargestSum {

    /**
     * Goal - find the minimum sum of the subarray with the largest sum after a valid split.
     *  (valid split = [nums] is split into exactly [subarraysCount] subarrays, each non-empty)
     *
     * evaluate binary search on solution spaces:
     *  loose boundaries:
     *   - min=0, lowest possible, in extreme case the entire array is 0's
     *   - max=nums.sum(), highest possible, in extreme case when k==1
     *  more strict boundaries:
     *   (wouldn't matter too much cause the iteration amount is a log, but could improve the time const)
     *  elimination properties:
     *   - if it is possible to split [nums] into [subarraysCount] subarrays such that the largest subarray sum is less or
     *   equal to X => then that very split is also valid for any value greater than X
     *   (all subarrays sums would still be trivially no greater than that Y>X)
     *  - if it's not possible to valid split [nums] into [subarraysCount] subarrays => after splitting there always is
     *   a subarray with sum greater than X => it's not possible to valid split [nums] with any value lower than X
     * => binary search is applicable.
     *
     * Design:
     *  - l=0, r=nums.sum() // could be narrowed
     *  - while l <= r:
     *   - mid = (l+r)/2
     *   - if isValidSplit(nums, mid):
     *    - r=mid-1
     *   - else:
     *    - l=mid+1
     *  - return l // we exit always when r is the max invalid value and l is the min valid value
     *
     * isValidSplit(..) - iterate through [nums] and sum elements, when current would-be sum exceeds `maxSum`, increase
     * subarraysCount and clear currentSum. Return true if the valid split is possible.
     *
     * edge cases:
     *  - nums.length == 1 => [subarraysCount] can only be 1 => correct as-is, finish at nums[0] (though could early return to do in a O(1);
     *  - [subarraysCount] == 1 => the answer is nums.sum() => correct-as-is;
     *  - all nums\[i] == 0 => answer is always 0 => correct as-is;
     *  - only some nums\[i]==0 => its ok, these just don't impact the intermediate subarray sum;
     *  - max sum is 10^6 * 10^3 => 10^9, fits into int.
     *
     * Time: O(n*logk), where k=nums.sum() but could be narrowed a bit
     * Space: O(1)
     */
    fun efficient(nums: IntArray, subarraysCount: Int): Int {
        var left = 0
        var right = nums.sum()

        while (left <= right) {
            val mid = (left + right) / 2
            if (nums.isValidSplit(subarraysCount, mid)) right = mid - 1 else left = mid + 1
        }

        return left // we always terminate when right = maximum invalid value and left = minimum valid value and
    }

    /**
     * Goal: return true if it's possible to split IntArray into exactly [targetSubarrayCount] such that the sum of
     * all is LESS or EQUAL to [maxSum].
     */
    private fun IntArray.isValidSplit(targetSubarrayCount: Int, maxSum: Int): Boolean {
        var currentSubarraySum = 0
        var subarrayCount = 0
        forEach { number ->
            if (number > maxSum) return false // any subarray with that value would violate the condition

            currentSubarraySum += number
            if (currentSubarraySum > maxSum) {
                currentSubarraySum = number
                subarrayCount++
                if (subarrayCount > targetSubarrayCount) return false
            }
        }
        // since [targetSubarrayCount] is always less or equal to IntArray.size => then even if currently we have
        // [subarrayCount] much less than [targetSubarrayCount], we could've still split in a way to match targetSubarrayCount
        // with sums less than [maxSum]. +1 coz the last subarray is always uncounted for in the loop
        return subarrayCount + 1 <= targetSubarrayCount
    }
}

fun main() {
    println(
        SplitArrayLargestSum().efficient(
            nums = intArrayOf(1,2,3,4,5),
            subarraysCount = 2,
        )
    )
}
