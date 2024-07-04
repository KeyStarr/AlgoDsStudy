package com.keystarr.algorithm.search.binarysearch

import kotlin.math.ceil

/**
 * LC-2300 https://leetcode.com/problems/successful-pairs-of-spells-and-potions/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= n=spells.length, m=potions.length <= 10^5
 *  • 1 <= spells\[i], potions\[i] <= 10^5
 *  • 1 <= success <= 10^10
 *
 * Final notes:
 *  • cool, understanding the leftmost/rightmost if equal ind binary search variations came in useful here;
 *  • indeed, even if the input isn't sorted, binary search can still be useful, so consider sorting the input in case
 *   search is needed;
 *  • here is a fun form of search too, basically just determining what boundary we need, capitalize on the property
 *   of the product with numbers that are >= 1.
 *
 * Value gained:
 *  • practiced recognizing and using binary search on not pre•sorted collections, with (a bit) masked search sub•problem;
 *  • practiced implementing the leftmost element/insertion index binary search variations.
 */
class SuccessfulPairsOfSpellsAndPotions {

    /**
     * Problem rephrase:
     *  - given: array spells, array potions of lengths n and m
     *  - int target
     * Goal: return array result where, each result\[i] is the amount of potions such that spell\[i] * potion >= target
     *
     * find all available integers such that the product with X is greater than target
     * => search all such integers linearly, then time is O(n*m)
     * => improve linear search with sorting potions and performing binary search for the lowest boundary (all integers
     *  above it would, trivially, fit the criteria) => O(logm * (n + m))
     *
     * Design:
     *  - sort [potions] ascending;
     *  - results = IntArray(size = spells.size)
     *  - iterate through [spells]:
     *      - binary search the leftmost number that equals ceil(target/potions[i]) or the leftmost insertion index
     *       if there's no such number;
     *      - results[i] = potions.size - (leftMostInsertInd + 1)
     *  - return results
     *
     * Edge cases:
     *  - multiplication => max is 10^5*10^5=10^10 => use Long for product storage, if any;
     *  - look for target/spells\[i] => but ceil division cause spells\[i]*X must be at least [target];
     *  - there are multiple numbers equal to target*spells\[i] => binary search the leftmost number in that case;
     *  - spell\[i] >= target => results\[i] = potions.size, cause all potions\[j] are at least 1, so the product
     *   will always be >= target.
     *
     * Time: O(logm * (n + m)) where m = number of potions, n = number of spells
     * Space: O(1) if in-place sorting, otherwise O(m)
     */
    fun efficient(spells: IntArray, potions: IntArray, target: Long): IntArray {
        potions.sort()
        val results = IntArray(size = spells.size)
        spells.forEachIndexed { ind, spell ->
            if (spell >= target) {
                results[ind] = potions.size
                return@forEachIndexed
            }

            val leftMostInd = potions.binarySearchLeftmostInd(target = ceil(target.toDouble() / spell).toLong())
            results[ind] = potions.size - leftMostInd
        }

        return results
    }

    /**
     * Goal - find leftmost insertion point for [target]:
     *  - if [target] is present => return the leftmost ind of a number == target;
     *  - otherwise return the index of the first element that is greater than target.
     *
     * 1 2 2 2 3 4 5 (7 nums)
     * t = 2
     *
     * m=3, 2 == 2 => go left => r = 3 - 1 = 2
     * m=1, 2 == 2 => go left => r = 2-1=1
     * m=0, 2 > 1 => go right => l = 1
     * answer: 1
     *
     * 1 3 4 5 6 (5 nums)
     * t = 2
     * m=2, 2 < 4 => go left => r = 2-1=1
     * m=0, 2 > 1 => go right => l=1
     * answer: 1
     *
     * 1 2 3 4 5 (5 nums)
     * t = 3
     * m=2, 3 == 3 => go left => r=1
     * m=0, 3 > 1 => go right => l=1
     * result: 1 => WRONG
     * m=2, 3 == 3 => go left => r=m=2
     * m=1, 3 > 2 => go right => l=2
     * result: 2
     */
    private fun IntArray.binarySearchLeftmostInd(target: Long): Int {
        var left = 0
        var right = size
        while (left < right) {
            val middle = left + (right - left) / 2 // avoid using long
            val middleElement = get(middle)
            if (target <= middleElement) {
                right = middle
            } else {
                left = middle + 1
            }
        }
        return left
    }
}

fun main() {
    println(
        SuccessfulPairsOfSpellsAndPotions().efficient(
            spells = intArrayOf(5, 1, 3),
            potions = intArrayOf(1, 2, 3, 4, 5),
            target = 7,
        ).contentToString()
    )
}
