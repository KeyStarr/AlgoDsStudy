package com.keystarr.algorithm.greedy

/**
 * LC-1338 https://leetcode.com/problems/reduce-array-size-to-the-half/description/
 * difficulty: medium
 * constraints:
 *  - 2 <= numbers.size <= 10^5
 *  - numbers.size is always even
 *  - 1 <= numbers\[i] <= 10^5
 *
 * Final notes:
 *  - why is that a medium? Very similar to [HowManyApplesCanYouPutIntoTheBasket] which is marked as easy.
 *   For the bucket sort solution for O(n) time?
 *  - solved in 20-25 mins: ⚠️why so slow?? Might need more practice with greedy problems;
 *  - seems like not rarely some of these greedy problems can be solved for O(n) time via a bucket/count sort.
     Might make it a priority in the future to learn these, sounds like a legit follow-up during the interview.
 *
 * Value gained:
 *  - practiced recognizing and solving a problem via greedy;
 *  - practiced counting via the hashmap.
 */
class ReduceArraySizeToTheHalf {

    /**
     * Problem rephrase:
     *  - given is the array of integers [numbers], no sorting is guaranteed, duplicate numbers are allowed;
     *  - a move: choose an integer present and remove all its occurrences from the [numbers].
     * Goal: return the minimum number of distinct numbers removed (each counted once, the number of their occurrences is of no matter)
     *  such that the total size of [numbers] is reduced by >= numbers.size
     * (size is always even)
     *
     * goal is finding a minimum + description resembles a move => try greedy
     * most optimal local strategy for a step?
     *  locally, we'd reduce the [numbers] size by the max amount if we remove all occurs of a number with the max
     *  amount of occurrences.
     * => each step greedily remove the number with the max amount of occurrences (out of those remaining)
     *
     * Algorithm:
     *  - pre-process, count the numbers frequencies using a HashMap;
     *  - sort the frequencies descending;
     *  - iterate from the start through sorted frequencies, decrease the newArraySize is less than or equal to numbers.size,
     *   return the total number of frequencies removed.
     *
     * Edge cases:
     *  - count, sum => check max frequency of a number, it's if all numbers are it, so 10^5 => fits into it;
     *  - numbers.size == 2 => if different numbers, we'd remove one and return 1, correct; if 2 of the same number, we'd remove it
     *   and still return 1, correct;
     *  - all numbers are same => one removal empties the array, return 1, correct.
     *
     * Time: always O(nlogn)
     *  - count via a HashMap always O(n)
     *  - sort frequencies always O(nlogn)
     *  - main loop average/worst O(n), best is O(1) if its just the max frequency number removed
     * Space: always O(k) for the hashmap, where k = number of distinct numbers in [numbers], k depends on n so average/worst O(n)
     */
    fun suboptimal(numbers: IntArray): Int {
        val numberCountMap = mutableMapOf<Int, Int>()
        numbers.forEach { number ->
            numberCountMap[number] = numberCountMap.getOrDefault(number, 0) + 1
        }
        val frequenciesDescending = numberCountMap.values.sortedDescending()

        var newArraySize = numbers.size
        val targetSize = numbers.size / 2
        var distinctNumbersRemoved = 0
        for (maxFrequency in frequenciesDescending) {
            newArraySize -= maxFrequency
            distinctNumbersRemoved++
            if (newArraySize <= targetSize) break
        }

        return distinctNumbersRemoved
    }

    // TODO: solve via a bucket sort for O(n) time
}

fun main() {
    println(ReduceArraySizeToTheHalf().suboptimal(intArrayOf(3, 3, 3, 3, 5, 5, 5, 2, 2, 7)))
}
