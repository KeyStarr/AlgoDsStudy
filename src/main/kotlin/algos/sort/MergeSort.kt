package algos.sort

class MergeSort {

    fun naive(numbers: IntArray): IntArray = naiveRecursion(numbers, 0, numbers.size)

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

    private fun mergeAscending(
        numbers: IntArray,
        firstLeftInd: Int,
        firstRightInd: Int,
        secondLeftInd: Int,
        secondRightInd: Int,
    ) {
        val left = numbers.copyOfRange(firstLeftInd, firstRightInd)
        val right = numbers.copyOfRange(secondLeftInd, secondRightInd)

        var leftInd = 0
        var rightInd = 0
        var resultInd = firstLeftInd

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

        val nonEmptyPile = if (leftInd == left.size) right else left
        var nonEmptyPileInd = if (leftInd == left.size) rightInd else leftInd
        while (nonEmptyPileInd < nonEmptyPile.size) {
            numbers[resultInd] = nonEmptyPile[nonEmptyPileInd]
            nonEmptyPileInd++
            resultInd++
        }
    }
}

fun main() {
    println(
        MergeSort().naive(
            intArrayOf(3, 2, 1),
        ).contentToString()
    )
}
