package com.keystarr.algorithm.hashtable

/**
 * LC-1426 https://leetcode.com/problems/counting-elements/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.length <= 1000;
 *  • 0 <= nums\[i] <= 1000;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • I was overconfident and didn't test the first implementation => failed the submission because I went counting
 *      validPairs for each nums value => if a number forming a validPair was there Y times, I counted it Y*Y times!
 *      Needed to check whether a value has a pair only 1 and then just add the amount of its duplicates to the result if it did.
 *      Lesson => test the code before submission at least on all major cases even for easy problems?
 *  • implemented [efficient] by myself in 15 mins, but not [efficientCleaner].
 */
class CountingElements {

    /**
     * Idea:
     *  - convert nums into hashmap, associate number by count of its occurrences
     *  - iterate through nums, for each nums\[i] check nums\[i]+1 existence via hashmap, if it does exist, then
     *      increment the total count by map[nums[i]].
     *
     * No real edge cases.
     *
     * Time: O(n)
     * Space: O(n)
     */
    fun efficient(nums: IntArray): Int {
        val numberOccurMap = mutableMapOf<Int, Int>()
        nums.forEach {
            numberOccurMap[it] = if (numberOccurMap.contains(it)) numberOccurMap[it]!! + 1 else 1
        }

        var validPairsCount = 0
        numberOccurMap.entries.forEach {
            if (numberOccurMap.contains(it.key + 1)) validPairsCount += it.value
        }

        return validPairsCount
    }

    /**
     * Idea: the core is same as [efficient], but here instead of counting in advance how many occurrences does each
     * number have we only simplify checking if its present at all to O(1), and then count valid pairs by iterating through
     * all numbers in [nums].
     *
     * Time: O(n)
     * Space: O(n)
     *
     * Discovered thanks to https://leetcode.com/problems/counting-elements/editorial/
     */
    fun efficientCleaner(nums: IntArray): Int {
        val set = nums.toSet()
        var validPairsCount = 0
        for (num in nums) if (set.contains(num + 1)) validPairsCount++
        return validPairsCount
    }
}

fun main() {
    println(
        CountingElements().efficient(
            nums = intArrayOf(),
        )
    )
}
