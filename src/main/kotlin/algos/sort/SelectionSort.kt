package algos.sort

// based on the condition of the LC-912 https://leetcode.com/problems/sort-an-array/description/
// without the at most O(nlogn) being required time complexity
class SelectionSort {

    // time: O(n^2) selection sort
    // space: O(3n) => O(n)
    // can't use it for this problem cause its required to be nlogn and checked by time limit, but just to practice
    // that particular algorithm.
    fun naive(nums: IntArray): IntArray {
        val takenPositions = BooleanArray(nums.size)
        val newNums = IntArray(nums.size)

        var currentInd = nums.size
        repeat(nums.size) {
            var currentMax = Int.MIN_VALUE
            var currentMaxInd = -1

            nums.forEachIndexed { ind, num ->
                if (takenPositions[ind]) return@forEachIndexed

                if (num > currentMax) {
                    currentMax = num
                    currentMaxInd = ind
                }
            }

            takenPositions[currentMaxInd] = true
            newNums[--currentInd] = currentMax
        }

        return newNums
    }

    // came back to selection sort, now studying CLRS Chapter 2.
    // idea: search the min in the subarray of numbers[i:numbers.size-2], swap numbers[i] and numbers[minInd]
    // worst case time O(n^2), memory O(1)
    fun spaceEfficient(numbers: IntArray): IntArray {
        for (i in 0 until numbers.size - 1) {
            var minInd = i
            for (j in (i + 1) until numbers.size) {
                if (numbers[j] < numbers[minInd]) minInd = j
            }

            val buff = numbers[i]
            numbers[i] = numbers[minInd]
            numbers[minInd] = buff
        }
        return numbers
    }
}

fun main() {
    val result = SelectionSort().spaceEfficient(intArrayOf(-1, 2, 0, 99, -99))
    println(result.contentToString())
}