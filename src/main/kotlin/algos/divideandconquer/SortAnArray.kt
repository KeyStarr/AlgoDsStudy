package algos.divideandconquer

import kotlin.math.abs

// LC-912 https://leetcode.com/problems/sort-an-array/description/
// difficulty: medium
// constraints:
//  • 1 <= nums.length <= 5 * 10^4
//  • -5 * 10^4 <= nums[i] <= 5 * 10^4
//  • at most O(nlogn) time complexity
//  • "smallest space complexity possible" - tf? well, hinting at a classic quicksort impl.
class SortAnArray {

    // time:
    //  - single non-base call: O(n) (pivot calc) + O(2n) (arrays creation) + O(n) (partitioning) ≈ O(n)
    //  - depth of recursion: O(logn), cause each time we choose the pivot to be a median
    //      => reduce the problem size by the factor of 2
    // -> O(nlogn) UNLESS all elements are equal to one another (then O(n^2)
    //
    // space:
    //  - single non-base call: O(2n) (partitioning arrays) +
    fun quicksort(nums: IntArray): IntArray = with(nums) {
        println("iteration: ${nums.contentToString()}")

        if (size < 2) return this
        if (size == 2) return if (get(0) <= get(1)) this else intArrayOf(get(1), get(0))

        val pivotInd = choosePivotInd() // O(n)
        val pivot = get(pivotInd)

        val arraySize = size
        val lessThanPivot = IntArray(size = arraySize) // O(n)
        val greaterOrEqualToPivot = IntArray(size = arraySize) // O(n)
        var lessCurrentInd = 0
        var greaterCurrentInd = 0

        // O(n)
        forEachIndexed { ind, number ->
            if (ind == pivotInd) return@forEachIndexed

            if (number >= pivot) {
                greaterOrEqualToPivot[greaterCurrentInd++] = number
            } else {
                lessThanPivot[lessCurrentInd++] = number
            }
        }
        return quicksort(lessThanPivot.copyOf(lessCurrentInd)) +
                intArrayOf(pivot) +
                quicksort(greaterOrEqualToPivot.copyOf(greaterCurrentInd))
    }

    private fun IntArray.partition(): Int {
        TODO("postponed the problem, come back and finish (getting MLE")
    }

    // goal - minimize recursion depth, means - select the median number in the collection to split it half
    // and achieve O(logn) for a single non-base case recursion call
    // time: O(n) + O(n) ≈ O(n)
    // memory: O(1)
    private fun IntArray.choosePivotInd(): Int {
        var max = get(0)
        var min = get(0)
        (1 until size).forEach { ind ->
            val num = get(ind)
            if (num > max) max = num
            if (num < min) min = num
        }

        val average = (max - min) / 2

        var medianInd = -1
        var minAverageDiff = Int.MAX_VALUE
        forEachIndexed { ind, num ->
            val numAverageDiff = abs(average - num)
            if (numAverageDiff < minAverageDiff) {
                medianInd = ind
                minAverageDiff = numAverageDiff
            }
        }
        return medianInd
    }
}

fun main() {
    println(
        SortAnArray().quicksort(intArrayOf(5, 1, 1, 2, 0, 0)).contentToString()
    )
//    println(SortAnArray().quicksort(listOf(3, 1, 2, 5, 4)))
}
