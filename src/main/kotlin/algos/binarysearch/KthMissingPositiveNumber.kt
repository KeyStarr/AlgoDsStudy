package algos.binarysearch

// https://leetcode.com/problems/kth-missing-positive-number/

// O(n)
fun findKthPositiveSlow(arr: IntArray, k: Int): Int {
    if (k < arr[0]) return k

    var missingCount = arr[0] - 1
    for (i in 0 until arr.size - 1) {
        val prevMissingCount = missingCount
        missingCount += arr[i + 1] - arr[i] - 1
        if (missingCount >= k) {
            return arr[i] + (k - prevMissingCount)
        }
    }

    return arr[arr.size - 1] + (k - missingCount)
}

// O(logn)
/**
 * Quite a complex set of ideas:
 * 1. trick - for number of index X of [arr] we can tell exactly how many numbers are missing before it in arr
 *  missingNumbersCountBeforeX = (arr[X] - 1 - X), reason:
 *      * arr[X] - 1 = exactly how many numbers would ideally be behind current number if none were missing;
 *      * - X = index of the element is exactly how many numbers are actually behind current number in [arr].
 *
 * 2. trick - we can't apply simple binary search rules of matching elements here, cause elements we look for are
 *  actually NOT inside the collection. What we can search for are the start and the end of INTERVALS in which our missing
 *  number would be. [arr] is guaranteed to have at least the start number of the interval in which the target missing
 *  number is located => let's move start and end indices based on whether an interval of arr[startInd] and arr[starInd + 1]
 *  numbers contain our missing Kth number of not.
 *
 * 3. we got an array of sorted ints, a task to find a target int (albeit a missing one), and a rule by which we can
 *  tell if any given number of the array is either less or greater than the number that we are looking for (though
 *  we compare not the actual target number value, but the amount of missing numbers before a given number)
 *  => we got all ingredients ready to apply the binary search!
 *  There's a but, though, cause intervals from p.2 may be complicated:
 *      * best case - [arr] contains the start and the end numbers of the interval in which the Kth number is.
 *          Example: arr = [1, 4, 6], k = 2. Missing numbers within [arr] are 2, 3, 5, the second = the answer is 3.
 *      * a tricky case - the upper bound of the interval has a subset of continuous M numbers before it. In that case
 *          it's harder to find the actual upper bound of the interval.
 *          Example: arr = [1, 3, 4, 5, 6, 7, 8, 9, 10], k = 1. Answer = 2.
 *       * other special case - the Kth number is outside of the [arr], so [arr] contains only the lower bound. Fix it
 *       by considering an upper bound of arr[startInd + 1] in this case to be Int.MAX_INT.
 *          Example: arr = [1, 3], k = 100. Answer = 102.
 * =>
 * The implementation below. A binary search that tries to find an interval of (arr[startInd], arr[startInd + 1]) such that
 * the missing Kth number would be inside it.
 */
fun findKthPositiveFastButComplex(arr: IntArray, k: Int): Int {
    if (k < arr[0]) return k

    var startInd = 0
    var endInd = arr.size - 1

    while (startInd <= endInd) {
        val middleInd = (startInd + endInd) / 2
        println("hop: start $startInd end $endInd and middleInd $middleInd")
        val missingNumbersCountBeforeMiddle = arr[middleInd] - 1 - middleInd
        val missingNumbersCountBeforeMiddlePlusOne =
            (if (middleInd == arr.size - 1) Int.MAX_VALUE else arr[middleInd + 1]) - 1 - (middleInd + 1)
        if (missingNumbersCountBeforeMiddle < k) {
            if (missingNumbersCountBeforeMiddlePlusOne >= k) {
                return arr[middleInd] + (k - missingNumbersCountBeforeMiddle)
            } else {
                startInd = middleInd + 1
            }
        } else {
            endInd = middleInd - 1
        }
    }
    throw java.lang.IllegalStateException("shouldn't be here - a bug")
}

fun main() {
    println(findKthPositiveFastButComplex(intArrayOf(1, 3), 100))
}
