package com.keystarr.algorithm.hashtable

/**
 * LC-1133 https://leetcode.com/problems/largest-unique-number/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.length <= 2000;
 *  • 0 <= nums\[i] <= 1000;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • implemented [optimalTimeHashmap] in 10 mins WITH TWO ERRORS!!!!!!
 *      • forgot that we need to find the LARGEST valid number => returned the first one (facepalm);
 *      • didn't take into account the case of -1, like, at first did, but after the largest fix - forgot (facepalm x2).
 *  • basically I got overconfident cause the problem looked so easy and stupidly missed on some cases!
 *      Learn a lesson here - treat every problem as A NEW ONE no matter how many times ive seen it before AND TAKE IT
 *      WITH FULL SERIOUSNESS EVERY TIME!
 *  • => OUTLINE ALL BRANCHES / STEPS IN PSEUDOCODE FIRST, EVERY TIME, even for EASY problems!!!!
 *  • as is the usual case with problems on counting, the array-as-counters pattern provides a faster const for time,
 *      but takes more space on average;
 *  • implemented [optimalSpace] with a bunch of errors due to i-1/i+1 caused edge cases. 2 hours in a session, kinda
 *     blurred focus I guess, so that's ok? Still, I have to be more patient and model more rigorously by myself BEFORE submitting!
 *     Even EASY ones! To build the habit for the primary goal - interviews!
 */
class LargestUniqueNumber {

    /**
     * Idea: hashmap number->count + return the largest entry key with value == 1 or -1.
     * Edge cases:
     *  - theres no single number => return -1 after the loop.
     * Time: O(n)
     * Space: O(n)
     */
    fun optimalTimeHashmap(nums: IntArray): Int {
        val numberCountMap = mutableMapOf<Int, Int>()
        nums.forEach { number -> numberCountMap[number] = numberCountMap.getOrDefault(number, 0) + 1 }
        var largestAnswer = -1
        numberCountMap.entries.forEach { entry ->
            if (entry.value == 1 && entry.key > largestAnswer) largestAnswer = entry.key
        }
        return largestAnswer
    }

    /**
     * Idea: use an array.size=2001, indices are numbers values are counters + return the largest number with counter == 1 or -1.
     * Edge cases:
     *  - theres no single number => return -1 after the loop.
     * Time: O(n)
     * Space: O(2001)
     */
    fun optimalTimeArray(nums: IntArray): Int {
        val numberCounts = IntArray(size = 2001)
        var largestNumber = -1
        nums.forEach { number ->
            numberCounts[number] = numberCounts[number] + 1
            if (number > largestNumber) largestNumber = number
        }
        for (number in largestNumber downTo 0) {
            val count = numberCounts[number]
            if (count == 1) return number
        }
        return -1
    }

    /**
     * Imagine a follow-up - how do solve it via space O(1)?
     *
     * Idea:
     * - sort the array in-place;
     * - find the largest single number which has a diff of 1 to its neighbours:
     *  iterate from i = nums.size - 2 to 1, if nums\[i+1] - nums\[i] != 0 && nums\[i] - nums\[i] != 0 => return nums\[i];
     * - if the cycle finished without returning, return -1.
     *
     * Edge cases:
     *  - there's no single number => return -1 after the loop;
     *  - iterate from i = nums.size -2, end with 1 to avoid outOfBounds;
     *  - nums.size<3 => handled;
     *  - nums[size-1] is the answer => handled;
     *  - nums[0] is the answer => handled.
     *
     * Time: O(nlogn) due to sorting;
     * Space: O(1), no collection allocation.
     */
    fun optimalSpace(nums: IntArray): Int {
        nums.sort()
        for (i in nums.size - 1 downTo 0) {
            val elementAfterIsntEqual = i == nums.size - 1 || nums[i + 1] - nums[i] != 0
            val elementBeforeIsntEqual = i == 0 || nums[i] - nums[i - 1] != 0
            if (elementAfterIsntEqual && elementBeforeIsntEqual) return nums[i]
        }
        return -1
    }
}

fun main() {
    println(LargestUniqueNumber().optimalSpace(intArrayOf(99)))
}
