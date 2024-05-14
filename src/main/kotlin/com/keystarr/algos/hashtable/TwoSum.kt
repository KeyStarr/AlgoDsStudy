package com.keystarr.algos.hashtable

/**
 * LC-1 https://leetcode.com/problems/two-sum/description/
 * constraints:
 *  • 2 <= nums.length <= 10^4;
 *  • -10^9 <= nums\[i] <= 10^9;
 *  • -10^9 <= target <= 10^9;
 *  • only 1 valid answer exists;
 *  • can't use same number twice;
 *  • no explicit time/space constraints.
 *
 * Final note - done an efficient solution by myself [efficient] in 15 mins, but not the most clean version [efficientCleaner].
 */
class TwoSum {

    /**
     * Tools: hashtable.
     * Can there be equal numbers? Assume yes, but if they sum to target, at most 2, cause only 1 valid answer exists.
     * Idea:
     *  • first build a map of diffs: target - number\[i]. Works fine for both negative and positive combinations of target/nums;
     *  • pass through each number and check, if it's in the map and that's not the same index we found =>
     *      2 numbers add up to target => done.
     *
     * Time: always O(n)
     * Space: always O(n)
     */
    fun efficient(nums: IntArray, target: Int): IntArray {
        val diffs = mutableMapOf<Int, Int>()
        nums.forEachIndexed { ind, number -> diffs[target - number] = ind }

        nums.forEachIndexed { ind, number ->
            val pairInd = diffs[number]
            if (ind != pairInd && pairInd != null) return intArrayOf(ind, pairInd)
        }

        return intArrayOf()
    }

    /**
     * Same as [efficient] but do a single pass through each nums element. The idea is that when we find the
     * second number of the pair, the first one's diff from target will already be in the map, always.
     *
     * Time: worst/average O(n)
     * Space: worst/average O(n)
     *
     * Discovered thanks to https://leetcode.com/problems/two-sum/solutions/3619262/3-method-s-c-java-python-beginner-friendly/
     */
    fun efficientCleaner(nums: IntArray, target: Int): IntArray {
        val subtractions = mutableMapOf<Int, Int>()
        nums.forEachIndexed { ind, number ->
            val pairInd = subtractions[number]
            if (pairInd != null) return intArrayOf(ind, pairInd)

            subtractions[target - number] = ind
        }
        throw IllegalStateException("Exactly 1 answer is guaranteed to exist.")
    }
}

fun main() {
    println(TwoSum().efficientCleaner(intArrayOf(3, 2, 4), 6).contentToString())
}
