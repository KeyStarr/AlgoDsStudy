package algos.set

import java.lang.Integer.min

// https://leetcode.com/problems/intersection-of-two-arrays/description/

/*
    1. declare a result: IntArray
    2. for 1st collection - check if
 */
// O(nums1.size * nums2.size * intersection.size) ~ n^3
fun intersectionSlow(nums1: IntArray, nums2: IntArray): IntArray {
    val resultList = mutableListOf<Int>()
    for (i in nums1) {
        nextI@ for (j in nums2) {
            if (j == i) {
                for (k in resultList) {
                    if (i == k) break@nextI
                }
                resultList.add(i)
            }
        }
    }
    return resultList.toIntArray()
}

// O(n + nlogn + nlogn + n * (logn + logn) = O(2n + 2nlogn + 2nlogn) = O(2n + 4nlogn) = O(nlogn)
// note - once again, if num1 and num2 were already sorted => it would be O(logN) and actually faster than the set
fun intersectionBinarySearch(nums1: IntArray, nums2: IntArray): IntArray {
    val resultArray = IntArray(min(nums1.size, nums2.size)) { -1 } // n
    var arrayInd = 0
    val num1Sorted = nums1.sortedArray() // nlogn
    val num2Sorted = nums2.sortedArray() // nlogn
    for (i in num1Sorted) { // n
        // actually could do an insertion sort instead of looking through the resultArray
        if (num2Sorted.customBinarySearch(i, num2Sorted.size) != null // logn
            && resultArray.customBinarySearch(i, arrayInd) == null // logn
        ) {
            resultArray[arrayInd] = i
            arrayInd++
        }
    }
    return resultArray.copyOfRange(0, arrayInd)
}

// O(2nlogn + n * (logn + 1) = O(2nlogn + nlogn + n) = O(nlogn)
fun intersectionBinarySearchSet(nums1: IntArray, nums2: IntArray): IntArray {
    val num1Sorted = nums1.sortedArray() // nlogn
    val num2Sorted = nums2.sortedArray() // nlogn
    val resultSet = mutableSetOf<Int>()
    for (i in num1Sorted) { // n
        // actually could do an insertion sort instead of looking through the resultArray
        if (num2Sorted.customBinarySearch(i, num2Sorted.size) != null // logn
            && !resultSet.contains(i) // O(1)
        ) {
            resultSet.add(i)
        }
    }
    return resultSet.toIntArray()
}

private fun IntArray.customBinarySearch(target: Int, endWindow: Int): Int? {
    var startInd = 0
    var endInd = endWindow - 1
    while (startInd <= endInd) {
        val midInd = (startInd + endInd) / 2
        val midValue = get(midInd)
        if (target == midValue) {
            return midInd
        } else if (target > midValue) {
            startInd = midInd + 1
        } else {
            endInd = midInd - 1
        }
    }
    return null
}

// O(n)
fun intersectionFast(nums1: IntArray, nums2: IntArray): IntArray {
    val num2Set = nums2.toSet() // n
    val resultSet = mutableSetOf<Int>() // const
    for (i in nums1) if (num2Set.contains(i)) resultSet.add(i) // n (set.contains(..) is O(1)
    return resultSet.toIntArray() // n
}
// fun intersection(nums1: IntArray, nums2: IntArray): IntArray  = nums1.toSet().intersect(nums2.toSet()).toIntArray()

fun main() {
    println(intersectionBinarySearchSet(intArrayOf(2, 2, 3), intArrayOf(4, 4, 2)).joinToString(separator = ", "))
}
