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
 *  • implemented [hashmap] in 10 mins WITH TWO ERRORS!!!!!!
 *      • forgot that we need to find the LARGEST valid number => returned the first one (facepalm);
 *      • didn't take into account the case of -1, like, at first did, but after the largest fix - forgot (facepalm x2).
 *  • basically I got overconfident cause the problem looked so easy and stupidly missed on some cases!
 *      Learn a lesson here - treat every problem as A NEW ONE no matter how many times ive seen it before AND TAKE IT
 *      WITH FULL SERIOUSNESS EVERY TIME!
 *  • => OUTLINE ALL BRANCHES / STEPS IN PSEUDOCODE FIRST, EVERY TIME, even for EASY problems!!!!
 *  • as is the usual case with problems on counting, the array-as-counters pattern provides a faster const for time,
 *      but takes more space on average.
 */
class LargestUniqueNumber {

    /**
     * Idea #1: hashmap number->count + return the largest entry key with value == 1 or -1.
     * No real edge cases.
     * Time: O(n)
     * Space: O(n)
     */
    fun hashmap(nums: IntArray): Int {
        val numberCountMap = mutableMapOf<Int, Int>()
        nums.forEach { number -> numberCountMap[number] = numberCountMap.getOrDefault(number, 0) + 1 }
        var largestAnswer = -1
        numberCountMap.entries.forEach { entry ->
            if (entry.value == 1 && entry.key > largestAnswer) largestAnswer = entry.key
        }
        return largestAnswer
    }

    /**
     * Idea #2: use an array.size=2001, indices are numbers values are counters + return the largest number with counter == 1 or -1.
     * No real edge cases.
     * Time: O(n)
     * Space: O(2001)
     */
    fun array(nums: IntArray): Int {
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
}

fun main(){
    println(LargestUniqueNumber().array(intArrayOf(5,7,3,9,4,9,8,3,1)))
}
