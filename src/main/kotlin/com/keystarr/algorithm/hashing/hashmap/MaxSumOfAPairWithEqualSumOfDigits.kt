package com.keystarr.algorithm.hashing.hashmap

/**
 * LC-2342 https://leetcode.com/problems/max-sum-of-a-pair-with-equal-sum-of-digits/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.length <= 10^5;
 *  • 1 <= nums\[i] <= 10^9.
 *
 * Value gained:
 *  • practiced using hashmap as occurrence counter, funny, the pattern is the same as in the [MinimumConsecutiveCardsToPickUp],
 *      same single pass/hashmap for the most recent occurrence only. Conditions are different, and a few other details;
 *  • at design stage I reasoned that largest sum of two numbers wouldn't fit into Int, but it did! Cause 2 * 10^9 still fits
 *      into Int, bro!
 */
class MaxSumOfAPairWithEqualSumOfDigits {

    /**
     * Problem rephrase: "find the best valid subarray:
     *  - validity: i != j && sumOfDigits(nums\[i]=nums\[j]);
     *  - best: max(nums\[i]+nums\[j])."
     *
     * Intuition - go through nums, compute sum of digits of nums\[i] on-the-fly and find it's pair via a hashmap,
     * where we store sumOfDigits to index, and update it only if the number is greater than the previous one.
     * And update maxSum (answer) by max(numbers\[previousInd]+numbers\[i], maxSum).
     *
     * Idea:
     * - allocate a hashmap digitsSumToIndMap<Int,Int>;
     * - iterate through all nums:
     *  - calculate sumOfDigits = sum of digits for nums\[i];
     *  - previousInd = digitsSumToIndMap\[sumOfDigits];
     *  - if digitsSumToIndMap contains sumOfDigits, then maxValue=max(nums\[previousInd]+nums\[i]], maxValue);
     *      - if (nums\[i] > nums\[previousInd]) digitsSumToIndMap\[sumOfDigits]=i
     *  - else digitsSumToIndMap[\sumOfDigits]=i;
     *  - return maxSum or -1 if no sum was calculated.
     *
     * Edge cases:
     *  - nums.length=1 => return -1;
     *  - nums.length=2 => works correctly.
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     */
    fun solution(nums: IntArray): Int {
        val digitsSumToNumberMap = mutableMapOf<Int, Int>()
        var maxPairSum = -1
        nums.forEachIndexed { ind, number ->
            val sumOfDigits = number.sumOfDigits() // worst time O(9) => O(1)
            if (digitsSumToNumberMap.contains(sumOfDigits)) {
                val previousNumber = digitsSumToNumberMap[sumOfDigits]!!
                val pairSum = previousNumber + number
                if (pairSum > maxPairSum) maxPairSum = pairSum
                if (number > previousNumber) digitsSumToNumberMap[sumOfDigits] = number
            } else {
                digitsSumToNumberMap[sumOfDigits] = number
            }
        }
        return maxPairSum
    }

    private fun Int.sumOfDigits(): Int {
        var sum = 0
        var number = this
        while (number > 0) {
            sum += number % 10
            number /= 10
        }
        return sum
    }
}
