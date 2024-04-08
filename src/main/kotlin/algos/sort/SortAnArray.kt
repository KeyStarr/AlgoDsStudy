package algos.sort

// https://leetcode.com/problems/sort-an-array/description/
class SortAnArray {

    // time: O(n^2) selection sort
    // space: O(3n) => O(n)
    // can't use it for this problem cause its required to be nlogn and checked by time limit, but just to practice
    // that particular alrogithm!
    fun selectionSort(nums: IntArray): IntArray {
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
    val result = SortAnArray().selectionSort(intArrayOf(-1, 2, 0, 99, -99))
    println(result.joinToString(separator = ", "))
}