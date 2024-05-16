package com.keystarr.algorithm.search.binarysearch

// https://leetcode.com/problems/fair-candy-swap/description/

// O(n^2)
fun fairCandySwapSlow(aliceSizes: IntArray, bobSizes: IntArray): IntArray {
    val aliceTotal = aliceSizes.sum()
    val bobTotal = bobSizes.sum()
    for (aliceBoxCandiesCount in aliceSizes) {
        val bobCandiesCountTarget = (bobTotal - aliceTotal) / 2 + aliceBoxCandiesCount
        for (bobBoxCandiesCount in bobSizes) {
            if (bobBoxCandiesCount == bobCandiesCountTarget) {
                return intArrayOf(aliceBoxCandiesCount, bobBoxCandiesCount)
            }
        }
    }
    throw IllegalStateException("there's a bug smwh, the answer is guaranteed to exist")
}

//        val bobGuessMatch = (bobTotal + aliceBoxCandiesCount) - aliceRaw


// O(n + n + nlogn + nlogn) = O(2n + 2nlogn) = O(n + nlogn) = O(nlogn)
// (note - if bobSizes would've been sorted already => logn => EVENT FASTER than the Set approach)
fun fairCandySwapBinarySearch(aliceSizes: IntArray, bobSizes: IntArray): IntArray {
    val aliceTotal = aliceSizes.sum() // O(n)
    val bobTotal = bobSizes.sum() // O(n)
    val bobSorted = bobSizes.sorted() // O(nlogn)
    // O(nlogn)
    for (aliceBoxCandiesCount in aliceSizes) {
        val bobBoxCandiesTarget = (bobTotal - aliceTotal) / 2 + aliceBoxCandiesCount

        var startInd = 0
        var endInd = bobSorted.size - 1
        while (startInd <= endInd) {
            val midInd = (startInd + endInd) / 2
            val midBobElement = bobSorted[midInd]
            if (midBobElement == bobBoxCandiesTarget) {
                return intArrayOf(aliceBoxCandiesCount, midBobElement)
            } else if (midBobElement > bobBoxCandiesTarget) {
                endInd = midInd - 1
            } else {
                startInd = midInd + 1
            }
        }
    }
    throw IllegalStateException("there's a bug smwh, the answer is guaranteed to exist")
}

// O(n)
fun fairCandySwapFastest(aliceSizes: IntArray, bobSizes: IntArray): IntArray {
    val aliceTotal = aliceSizes.sum() // n
    val bobTotal = bobSizes.sum() // n
    val bobSizesSet = bobSizes.toSet() // n
    // n
    for (aliceBoxCandiesCount in aliceSizes) {
        val bobBoxCandiesTarget = (bobTotal - aliceTotal) / 2 + aliceBoxCandiesCount
        if (bobSizesSet.contains(bobBoxCandiesTarget)) {
            return intArrayOf(aliceBoxCandiesCount, bobBoxCandiesTarget)
        }
    }
    throw IllegalStateException("there's a bug smwh, the answer is guaranteed to exist")
}

fun main() {
    println(fairCandySwapBinarySearch(intArrayOf(1, 2, 5), intArrayOf(2, 4)).joinToString(separator = ", "))
}
