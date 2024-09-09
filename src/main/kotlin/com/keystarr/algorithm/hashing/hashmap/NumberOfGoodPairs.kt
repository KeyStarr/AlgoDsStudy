package com.keystarr.algorithm.hashing.hashmap

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

/**
 * ⭐️ a great example of a blind re-solve with a huge improvement in quality
 * LC-1512 https://leetcode.com/problems/number-of-good-pairs/description/
 * difficulty: easy
 * constraints:
 *  - 1 <= nums.size <= 100;
 *  - 1 <= nums\[i] <= 100.
 *
 * Final notes:
 *  - done [efficient] by myself in 15 mins;
 *  - "зацепился", caught the crucial problem property straight away: "i < j" constraint looked weird => observed that
 *   cause of that we simply need to count each distinct pair exactly once;
 *   => inferred then that we just need to count the number of number frequencies, and we can compute each distinct pair
 *    via O(1) time;
 *  - ⚠️ struggled a bit with the formula to compute number of distinct pairs, but DRY RAN 🔥 the computation => recognized
 *   an arithmetic progression with the step of -1 and endNumber=frequencies.lastInd-1 => then remembered and checked the formula
 *   for that through dry running;
 *  - apparently, I've already solved that problem about 3 months ago :D Back then I failed to come up with the O(n) solution
 *   at all, only done O(n^2). Didn't record the time, but probably invested quite some.
 *
 *   This time I HAD NO RECOLLECTION of encountering that problem earlier WHATSOEVER 🙀 that happened before with a few other problems!
 *
 *   But this time I managed to do a O(n) solution in 15 mins 🏆🏆 And I followed a different approach, not the cleanest,
 *    but still an efficient one! Crazy) This time I knew to observe, to reason faster and start from the problem statement
 *    noting sticking out properties of the original problem and trying to find a solution angle from it! And did it 🔥
 *
 * Value gained:
 *  - practiced solving a "valid combinations counting" type problem efficiently using an array for counting and arithmetic progression formula.
 */
class NumberOfGoodPairs2 {

    // TODO: done well, but at max time boundary => retry in 1-2 weeks

    /**
     * Actually, we can do same complexities, but better time const: single pass.
     *
     * Observe: for each new number we encounter, if we've already had X equal numbers to it so far
     *  => the new number forms exactly X pairs (a pair with each of those numbers) => simply add to the goodPairs as we
     *  count the numbers => single pass.
     */
    fun efficientCleaner(nums: IntArray): Int {
        val frequencies = IntArray(size = 101)
        var goodPairs = 0
        nums.forEach { number ->
            val currentFreq = frequencies[number]
            if (currentFreq > 0) goodPairs += currentFreq
            frequencies[number] = currentFreq + 1
        }
        return goodPairs
    }

    /**
     * observation: the "i < j" constraint basically requires just to count each pair of equal elements exactly once
     *
     * e.g. 1 2 1 3 1 1
     *
     * 5 equal numbers => 4 + 3 + 2 + 1 = 10
     * 4 equal numbers (1's above) => 3 + 2 + 1 = 6
     * 3 equal numbers => 2 + 1 = 3
     * 2 equal numbers => 1
     *
     * => approach:
     * 1. count the occurrences of each number;
     * 2. for each occurrence count > 1 => add to the result sum of arithmetic progression (occurrences-1) with step -1.
     *
     * arithmetic progression formula check:
     *  4 * (4 + 1) / 2 = 10
     *  3 * (3 + 1) / 2 = 6
     *  2 * (2 + 1) / 2 = 3
     *  1 * (1 + 1) / 2 = 1
     *
     * 1.sum of arithmetic progression starting from 1 with end number = n with step of 1 => n * (n+1) / 2
     * 2. for us end number n=frequency - 1 (basically the number of good pairs of `frequency` amount of equal numbers)
     *
     * Time: always O(n)
     *  - counting frequencies is O(n);
     *  - result calculation is O(k), where k=amount of distinct numbers, k depends on n => average/worst O(n)
     * Space: always O(101) = O(1)
     *  but if we used a hashmap it'd be O(k)=O(n).
     *
     * --------
     *
     * dry run:
     *  nums = 1,2,3,1,1,3
     *  frequencies = 1:3, 2:1, 3:2
     *  goodPairs = 3 + 1 = 4
     */
    fun efficient(nums: IntArray): Int {
        val frequencies = IntArray(size = 101)
        nums.forEach { number -> frequencies[number] = frequencies[number] + 1 }

        var goodPairs = 0
        frequencies.forEach { freq ->
            if (freq < 2) return@forEach
            val endNumber = freq - 1
            goodPairs += (endNumber * (endNumber + 1)) / 2
        }
        return goodPairs
    }
}

fun main() {
    println(NumberOfGoodPairs().fast(intArrayOf(1, 2, 1, 2)))
}
