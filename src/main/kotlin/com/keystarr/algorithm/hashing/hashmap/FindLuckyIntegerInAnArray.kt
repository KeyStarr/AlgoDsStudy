package com.keystarr.algorithm.hashing.hashmap

/**
 * LC-1394 https://leetcode.com/problems/find-lucky-integer-in-an-array/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.size <= 500;
 *  • 1 <= nums\[i] <= 500.
 *
 * Final notes:
 *  • solved via [efficient] in 7 mins;
 *  • ⚠️ failed an edge case of i=0, 0==frequencies[0] since the min nums\[i]==1, huh. Should've been more careful! A pitfall
 *   specifically for the counting array approach;
 *  • didn't discover [efficientCleaner] by myself in 15 mins => need to retry later to reinforce. But it obviously makes sense;
 *  • 🔥 despite for this problem having a typical counting structure there is no 1 pass solution! All problems are unique, huh,
 *   and predictions from experience can only get one so far => 💡 rely on experience, but perceive each new problem PHENOMENOLOGICALLY!!
 *
 * Value gained:
 *  • practiced solving a meta-counting type problem using a counting array and a small optimization based on the counting array
 *   frequencies sorted by numbers property.
 */
class FindLuckyIntegerInAnArray {

    // TODO: did well, but not perfect => retry in 1-2 weeks

    /**
     * Core idea same as [efficient], but observe: we need the largest valid number + WE ALREADY HAVE FREQUENCIES SORTED
     *  BY NUMBERS ASCENDING thanks to numbers being indices in [nums] => simply iterate from the end of [nums] until 1 (since min nums\[i]==1)
     *  and return on first valid number (which would be the largest, trivially)
     *  => return -1 no valid number was found.
     *
     * ---------
     *
     * that's the optimization for [efficient] I was looking for. 1 pass here, all problems are unique, huh. But still a reduced
     *  2nd pass.
     *
     * Discovered thanks to https://leetcode.com/problems/find-lucky-integer-in-an-array/solutions/557113/3-approaches-2-variations/
     */
    fun efficientCleaner(nums: IntArray): Int {
        val frequencies = IntArray(size = 501)
        nums.forEach { number -> frequencies[number] = frequencies[number] + 1 }

        for (number in frequencies.lastIndex downTo 1) {
            val frequency = frequencies[number]
            if (number == frequency) return number
        }
        return -1
    }

    /**
     * trivial approach:
     *  - 1st pass: count numbers frequencies;
     *  - 2nd pass: iterate through frequencies, find the largest valid number;
     *  - return the found one or -1.
     *
     * Time: always O(n)
     * Space: O(k)=O(501)=O(1) for counting array, for map its average/worst O(n)
     *
     * ------- optimization
     *
     * we can't reduce space cause we need to know final frequencies of elements
     * => can we reduce time const to a single pass?
     *
     * we could, as we count, if number == new frequency update the maxValidNumber encountered so far, but the problem with
     *  that is if we do that, but later encounter same number again => its not valid anymore => we've lost the previous max
     *  valid number which, may not have anymore duplicates in the array
     *  keeping track of the last X maxes and removing any dynamically would not be worth it, I think
     */
    fun efficient(nums: IntArray): Int {
        val frequencies = IntArray(size = 501)
        nums.forEach { number -> frequencies[number] = frequencies[number] + 1 }

        var maxValidNumber = -1
        for (number in 1 until frequencies.size) {
            val frequency = frequencies[number]
            if (number == frequency && number > maxValidNumber) maxValidNumber = number
        }
        return maxValidNumber
    }
}
