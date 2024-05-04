package algos.sort

// example from chapter 2 + exercise 2.1.3
// (when I first read the description - wanted to use binary search for index position for each number BUT
// then realized that in worst case we'd have to shift array every time, so it would still be by O n^2 (even for average?))
class InsertionSort {

    // based entirely on the design from CLRS
    // the idea: move all elements that are less than the current one to the right, once left doesn't exist or
    // is less or equal to it => write current element there (if doesn't exist - write into the first slot).
    // time: O(n^2)
    // space: O(1)
    fun ascendingShiftAllGreaterThanKeyRight(nums: IntArray): IntArray {
        for (i in 1 until nums.size) {
            val key = nums[i]
            var j = i - 1
            while (j >= 0 && nums[j] > key) {
                nums[j + 1] = nums[j]
                j--
            }
            nums[j + 1] = key
        }
        return nums
    }

    // my first interpretation, implemented after only reading the cards' metaphor, before checking pseudocode
    // the idea: swap the current element (nums[i]) with it's left neighbor every time it is less than it
    // (each such operation - 2 writes, whereas the original algo from the book is only 1 write (for non-final while iterations))
    fun ascendingSwapAllGreaterWithKey(nums: IntArray): IntArray {
        for (i in 1 until nums.size) {
            for (j in i downTo 1)
                if (nums[j - 1] > nums[j]) {
                    val buffer = nums[j - 1]
                    nums[j - 1] = nums[j]
                    nums[j] = buffer
                }
        }
        return nums
    }

    fun descending(nums: IntArray): IntArray {
        for (i in 1 until nums.size) {
            val key = nums[i]
            var j = i - 1
            while (j >= 0 && key > nums[j]) {
                nums[j + 1] = nums[j]
                j--
            }
            nums[j + 1] = key
        }
        return nums
    }
}

fun main() {
    println(
        InsertionSort().ascendingShiftAllGreaterThanKeyRight(
            nums = intArrayOf(3, 2, 5, 1)
        ).joinToString(),
    )
}
