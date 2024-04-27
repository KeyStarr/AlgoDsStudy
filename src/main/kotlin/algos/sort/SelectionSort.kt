package algos.sort

// based on the condition of the LC-912 https://leetcode.com/problems/sort-an-array/description/
// without the at most O(nlogn) being required time complexity
class SelectionSort {

    // time: O(n^2) selection sort
    // space: O(3n) => O(n)
    // can't use it for this problem cause its required to be nlogn and checked by time limit, but just to practice
    // that particular alrogithm!
    operator fun invoke(nums: IntArray): IntArray {
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


}

fun main(){
    val result = SelectionSort()(intArrayOf(-1, 2, 0, 99, -99))
    println(result.joinToString(separator = ", "))
}