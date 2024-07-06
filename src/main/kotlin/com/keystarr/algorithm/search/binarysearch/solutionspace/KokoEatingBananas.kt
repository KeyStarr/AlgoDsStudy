package com.keystarr.algorithm.search.binarysearch.solutionspace

import kotlin.math.ceil

/**
 * LC-875 https://leetcode.com/problems/koko-eating-bananas/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= piles.length <= 10^4
 *  • piles.length <= h <= 10^9
 *  • 1 <= piles\[i] <= 10^9
 *
 * Final notes:
 *  • failed THREE submissions! Because of the rounding errors due to using Float for both `middle` and the hours `sum`
 *   => reinforced that Float's integral part is at most 10^7 :) Always check the floating point data type for sufficiency!
 *   (here I assumed it would be close to int, lol)
 *
 * Value gained:
 *  • first time practiced binary search on solution spaces;
 *  • reinforced that max Float is ~10^7 => when converting from Int to Float always check data type sufficiency! 
 */
class KokoEatingBananas {

    /**
     * Solution always exists, cause [maxHours] is at least piles.size, meaning that `bananasPerHour` = piles.max() is always
     *  a valid answer.
     *
     * Goal - find the minimum valid `bananasPerHour`. Valid = all piles\[i] are 0'd within [maxHours] amount of moves.
     *
     * Since we need to find the best valid number out of many combinations => identify the boundaries of the solution space first:
     *  - min `bananasPerHour` candidate = 1, cause since piles are integer there's no sense in considering floating point
     *  answers, and negative/zero are never valid answers;
     *  - max `bananasPerHour` candidate = piles.max(), it doesn't make sense to consider anything larger, since
     *   we minimize the speed and at that speed according to the rules we will already always achieve the solution
     *   in exactly [maxHours] hours.
     *
     * Sub-goal - find and MINimum number of `bananasPerHour` in the range of [1, [piles].max()] such that all [piles]
     *  are zeroed within (less than or equal to) [maxHours].
     * 2 important properties:
     *  - if `bananasPerHour = X` is valid (its hours <= [maxHours]) then all `bananasPerHour` > X are valid also, cause
     *   the amount of hours it takes for any such values is always equals or less than the hours it took for X;
     *  - if `bananasPerHour = Y` is invalid (its hours > [maxHours] then all `bananasPerHour` < Y are invalid too, cause
     *   for any such value the amount of hours is always greater than or equal to Y.
     *
     * Search for an optimal number within the solution space with 2 key space reduction properties
     *  => try the solution space binary search?
     * Design:
     *  - left = 1, right = piles.max(): (finding max is O(n) time))
     *  - perform classic binary search looking for the minimum valid element within the solution space:
     *   (while left < right)
     *   - middle = left + (right - left) / 2
     *   - check validity for a given `middle` (bananasPerHour) via simply iterating through [piles] and summing up the result of
     *    ceil(piles[i] / bananasPerHour) // O(n) time
     *   - if current `middle` (bananasPerHour) is valid, treat it as target < middle, i.o. right=middle
     *   - if middle is invalid its ~ to target > middle, i.o. left=middle+1
     *  - return left // ??? will it be the min valid `bananasPerHour` within the solution space?
     *
     * Edge cases:
     *  - [maxHours] == [piles].length => works correctly, doesn't influence anything in the algo;
     *  - left + right => max 2 numbers sum in the solution space = 10^9 + 10^9-1 => fits into int, could do left+right;
     *  - division => the bananasPerHour we consider is at least 1 => no division by 0, correct;
     *  - piles.length == 1:
     *      - piles[0] == 1 => left = right = 1, while iteration never executes, return 1, correct
     *      - else => the solution space has more than 1 number, so while iterations are executed => correct as-is;
     *  - max hours sum for a given bananasSpeed is when bananasSpeed=1 and piles.length=10^4, each pile being 10^9
     *   => 10^9*10^4=10^13 => doesn't fit into int/float! use long/double
     *  - max bananasPerHour => max pile => 10^9 => won't fit into Float (10^7 integral part is max for float) => use Double too.
     *
     * Time: O(m*logn)
     *  - we try at most logn numbers, where n = piles.max()
     *  - checking whether a single number is valid takes m iterations, where m = piles.size
     * Space: O(1)
     */
    fun efficient(piles: IntArray, maxHours: Int): Int {
        var left = 1
        var right = piles.maxOf { it }
        while (left <= right) {
            val middle = (left + right) / 2
            if (piles.isValidSpeed(bananasPerHour = middle.toDouble(), maxHours)) {
                right = middle - 1
            } else {
                left = middle + 1
            }
        }
        return left
    }

    private fun IntArray.isValidSpeed(bananasPerHour: Double, maxHours: Int): Boolean {
        var sum = 0.0
        forEach { pile -> sum += ceil(pile / bananasPerHour) }
        return sum <= maxHours
    }
}

fun main() {
    println(
        KokoEatingBananas().efficient(
            piles = intArrayOf(1000000000),
            maxHours = 2,
        )
    )
}
