package com.keystarr.algos.other

import com.keystarr.algos.search.binarysearch.BinarySearch
import com.keystarr.algos.sort.MergeSort

/**
 * CLRS-2.3.8
 * constraints:
 *  • worst time: O(nlgn);
 *  • none else specified. Assume max default kotlin jvm array size, numbers being integers, any space complexity.
 */
class PairForSumCLRS {

    private val mergeSort = MergeSort()
    private val binarySearch = BinarySearch()

    /**
     * Time: always O(nlgn) (best is only faster by a const);
     * Space: O(n) for array conversion.
     */
    fun weird(numbers: Set<Int>, targetSum: Int): Boolean {
        // always O(n) + O(nlgn)
        val sorted = mergeSort.classic(numbers.toIntArray())
        // worst case O(nlgn)
        sorted.forEach { number ->
            val match = targetSum - number
            val matchInd = binarySearch.recursive(sorted, match)
            if (matchInd != -1) return true
        }
        return false
    }

    /**
     * Intuition: predetermine
     *
     * Time: always O(n)
     * Space: O(n) for an additional set
     */
    fun fast(numbers: Set<Int>, targetSum: Int): Boolean {
        // for each number calculate exactly what pair does it require to sum up to [targetSum]
        val subtractionsSet = mutableSetOf<Int>()
        numbers.forEach { number -> subtractionsSet.add(targetSum - number) }

        numbers.forEach { number -> if (subtractionsSet.contains(number)) return true }
        return false
    }
}
