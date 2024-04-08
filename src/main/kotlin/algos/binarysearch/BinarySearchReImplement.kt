package algos.binarysearch

// half a year later
// https://leetcode.com/problems/binary-search/description/
class BinarySearchReImplement {

    fun search(nums: IntArray, target: Int): Int {
        if (nums.isEmpty()) return -1

        var start = 0
        var end = nums.size - 1
        while (start <= end) {
            val currentInd = start + (end - start) / 2
            val currentNumber = nums[currentInd]
            when {
                currentNumber == target -> return currentInd
                currentNumber > target -> end = currentInd - 1
                else -> start = currentInd + 1
            }
        }
        return -1
    }
}

fun main() {
    println(BinarySearchReImplement().search(intArrayOf(3), 7))
}