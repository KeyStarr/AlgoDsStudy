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

fun main() {
    println(findKthPositiveSlow(intArrayOf(2), k = 1))
}
