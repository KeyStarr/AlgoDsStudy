package com.keystarr.algorithm.hashtable

/**
 * LC-2260 https://leetcode.com/problems/minimum-consecutive-cards-to-pick-up/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= cards.length <= 10^5;
 *  • 0 <= cards\[i] <= 10^6;
 *  • no explicit time/space.
 *
 * Value gained:
 *  • problem ontology simplification (rephrasing) is a super valuable technique! makes it so much cleaner consistently
 *      to speed up solution tools pattern matching;
 *  • there is a pattern that sometimes the first instinct is to track/save all M indices/occurrences and do 2n cycles
 *      but in fact we can merge both loops, track only the most recent/valuable occurrence => do it cleaner and by a single n cycle.
 */
class MinimumConsecutiveCardsToPickUp {

    /**
     * Problem rephrasing:
     * "Return the length of the shortest subarray that has 2 equal numbers, if there are none - return -1"
     * metric constraint: must have K equal numbers, where K=2
     * min by length.
     *
     * Idea:
     *  - allocate a hashmap matchMap value->MutableList<Int> (indices);
     *  - iterate through all cards, fill the matchMap; time O(n)
     *  - if matchMap empty return -1 else iterate through all matchMap.entries, find two closest indices across all pairs,
     *      return them (else - time O(n)
     *
     * Edge cases:
     *  - cards.length=1 => early return -1;
     *  - two equal numbers are consecutive like [..,3,3,..] => works correctly.
     *
     * Time: always O(n)
     * Space: O(n)
     */
    fun efficient(cards: IntArray): Int {
        if (cards.size == 1) return -1

        val numberToIndMap = mutableMapOf<Int, MutableList<Int>>()
        cards.forEachIndexed { ind, card ->
            if (numberToIndMap.contains(card)) numberToIndMap[card]!!.add(ind) else numberToIndMap[card] =
                mutableListOf(ind)
        }

        var minValidLength = Int.MAX_VALUE
        numberToIndMap.entries.forEach { entry -> // time: // O(n), cause all entry.value.size add up to n
            val numberIndices = entry.value
            val hasMatch = numberIndices.size > 1
            if (hasMatch) {
                for (i in 1 until numberIndices.size) {
                    val subarrayLength = numberIndices[i] - numberIndices[i - 1]
                    if (subarrayLength < minValidLength) minValidLength = subarrayLength
                }
            }
        }

        return if (minValidLength == Int.MAX_VALUE) -1 else minValidLength + 1
    }

    /**
     * Idea - observe that if a certain number appears more than 2 times, and we check every time starting with 2 the
     * length of the subarray, then we need to only store the single most recent index.
     *
     * Time: always O(n)
     * Space: average/worse O(n) but with a const less than [efficient]
     *
     * Discovered thanks to https://leetcode.com/explore/interview/card/leetcodes-interview-crash-course-data-structures-and-algorithms/705/hashing/4645/
     */
    fun efficientCleaner(cards: IntArray): Int {
        if (cards.size == 1) return -1

        val numberToIndMap = mutableMapOf<Int, Int>()
        var minValidLength = Int.MAX_VALUE
        cards.forEachIndexed { ind, card ->
            if (numberToIndMap.contains(card)) {
                val previousInd = numberToIndMap[card]!!
                val subarrayLength = ind - previousInd
                if (subarrayLength < minValidLength) minValidLength = subarrayLength
            }
            numberToIndMap[card] = ind
        }
        return if (minValidLength == Int.MAX_VALUE) -1 else minValidLength + 1
    }
}

fun main() {
    println(
        MinimumConsecutiveCardsToPickUp().efficientCleaner(
            intArrayOf(3, 4, 2, 3, 4, 7)
        )
    )
}
