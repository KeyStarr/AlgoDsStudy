package com.keystarr.algorithm.sort

// algorithm design from CLRS 2.3
class MergeSort {

    fun classic(numbers: IntArray): IntArray = naiveRecursion(numbers, 0, numbers.size)

    /**
     * technique - divide & conquer.
     *
     * base case:
     *  0 or 1 => return itself.
     *
     * recursive case:
     *  divide: split into even halves by pointers;
     *  conquer: call itself on each half (with correct pointers);
     *  combine: merge halves into a single ascending array, write straight into the original array on relevant indices.
     *
     * time:
     *  log(n) iterations
     *  O(n) for merge on each iteration
     *  => O(nlogn)
     *
     * space:
     *   one array of O(n) size for each merge operation, deallocate after finish => no stacking
     *   (could really allocate it just once with size n and pass it via args to each recursive call, for extra efficiency)
     *
     * @param startInd - inclusive;
     * @param endInd - exclusive.
     */
    private fun naiveRecursion(numbers: IntArray, startInd: Int, endInd: Int): IntArray {
        if (endInd - startInd < 2) return numbers

        val middleInd = (startInd + endInd) / 2
        naiveRecursion(numbers, startInd, middleInd)
        naiveRecursion(numbers, middleInd, endInd)
        mergeAscending(
            numbers = numbers,
            firstLeftInd = startInd,
            firstRightInd = middleInd,
            secondLeftInd = middleInd,
            secondRightInd = endInd,
        )

        return numbers
    }

    /**
     * time: O(n + n) = O(n)
     * space: O(n) due to left and right arrays allocation
     */
    private fun mergeAscending(
        numbers: IntArray,
        firstLeftInd: Int,
        firstRightInd: Int,
        secondLeftInd: Int,
        secondRightInd: Int,
    ) {
        // both copies together take O(n) time
        val left = numbers.copyOfRange(firstLeftInd, firstRightInd)
        val right = numbers.copyOfRange(secondLeftInd, secondRightInd)

        var leftInd = 0
        var rightInd = 0
        var resultInd = firstLeftInd

        // both while loops together take O(n) time (we take each element from current subarray once)
        while (leftInd < left.size && rightInd < right.size) {
            val currentLeft = left[leftInd]
            val currentRight = right[rightInd]
            if (currentLeft < currentRight) {
                numbers[resultInd] = currentLeft
                leftInd++
            } else {
                numbers[resultInd] = currentRight
                rightInd++
            }
            resultInd++
        }

        val isLeftNonEmpty = leftInd == left.size
        val nonEmptyHalf = if (isLeftNonEmpty) right else left
        var nonEmptyHalfInd = if (isLeftNonEmpty) rightInd else leftInd
        while (nonEmptyHalfInd < nonEmptyHalf.size) {
            numbers[resultInd] = nonEmptyHalf[nonEmptyHalfInd]
            nonEmptyHalfInd++
            resultInd++
        }
    }
}

fun main() {
    println(
        MergeSort().classic(
            intArrayOf(3, 2, 1),
        ).contentToString()
    )
}
