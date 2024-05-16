package com.keystarr.algorithm.hashtable

// LC-1512 https://leetcode.com/problems/number-of-good-pairs/description/
// difficulty: easy
// constraints:
//  * 1 <= nums.length <= 100
//  * 1 <= nums[i] <= 100
// * no time/space constraints
class NumberOfGoodPairs {

    // time: O(n^2)
    // space: O(1)
    fun naive(nums: IntArray): Int {
        var goodPairsCount = 0
        nums.forEachIndexed { i, num1 ->
            nums.forEachIndexed { j, num2 ->
                if (i < j && num1 == num2) {
                    goodPairsCount++
                }
            }
        }
        return goodPairsCount
    }

    // still same O complexity, but nicer - until a certain growth point significantly faster than naive
    fun naiveFaster(nums: IntArray): Int {
        var goodPairsCount = 0
        for (i in nums.indices) {
            for (j in (i + 1 until nums.size)) {
                if (nums[i] == nums[j]) goodPairsCount++
            }
        }
        return goodPairsCount
    }


    // time: O(n), space: O(n)
    // based on https://leetcode.com/problems/number-of-good-pairs/solutions/1457646/java-story-based-0ms-single-pass-easy-to-understand-simple-hashmap/
    // uh-huh, so hashmaps are good for single pass unique items count based scenarios
    fun fast(nums: IntArray): Int {
        // space: O(n)
        val uniqueNumberRepetitions = mutableMapOf<Int, Int>()
        var goodPairsCount = 0
        // time: O(n)
        nums.forEach { number ->
            val repetitions = uniqueNumberRepetitions[number]
            if (repetitions != null) {
                goodPairsCount += repetitions
                uniqueNumberRepetitions[number] = repetitions + 1
            } else {
                uniqueNumberRepetitions[number] = 1
            }
        }
        return goodPairsCount
    }

    /*
    Just for the record - I have failed with 2 approaches to come up with a solution faster than O(n^2) time:
        1. tried to O(nlogn) by sorting & doing binary search.
            failed cause we need to calculate ALL distinct pairs of same numbers => I'd count only
            one pair per one number at first. And to mark found numbers as counted to rerun binary search until
            it gave -1 for current number would be too laborsome (there's gotta be a better solution!)
        2. a dumb but ambitious idea:
            2.1) add all nums into a hashmap and count all collisions per key;
                turns out theres no HashMap implementation in kotlin/java stdlibs that would give access
                to collision count or to buckets directly => I even implemented my own terrible custom Hashmap;
            2.2) sum up the number of good pairs by the formula for each key-collisionsCount:
                (customized distinct permutations, feels off though)
                ((collisionsCount!)/((collisionsCount - 1)!) / 2)
                Obviously, here I hit a wall and crashed, cause worst case would be 100! and that
                is terribly slow, not to mention integer overflow...

    Turns out we coulda just done a single pass and count the sum via simple arithmetics for O(n)....
     */
}

fun main() {
    println(NumberOfGoodPairs().fast(intArrayOf(1, 2, 1, 2)))
}
