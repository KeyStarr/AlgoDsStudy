package com.keystarr.algorithm.sort

import kotlin.math.max

// example from chapter 2 + exercise 2.1.3
// (when I first read the description - wanted to use binary search for index position for each number BUT
// then realized that in worst case we'd have to shift array every time, so it would still be by O n^2 (even for average?))
class InsertionSort {

    /**
     * Based entirely on the design from CLRS-2.1.
     * The idea: move all elements that are less than the current one to the right, once left doesn't exist or
     * is less or equal to it => write current element there (if doesn't exist - write into the first slot).
     *
     * Time: worst and average O(n^2), best O(n).
     * Space: always O(1).
     */
    fun ascendingClassicIterative(nums: IntArray): IntArray {
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

    /**
     * CLRS-2.3.5
     */
    fun ascendingClassicRecursive(nums: IntArray): IntArray =
        nums.apply { ascendingClassicRecursiveInner(nums = nums, endInd = nums.size) }

    /**
     * Idea - same as [ascendingClassicIterative] but build a callstack from the end and then unwrap it in very much
     * the same way as the iterative, that is, insert the ith element into subarray[0:i-1], first recursive call to unwrap
     * would be the 2nd element and after it each to unwrap takes the next element.
     *
     * Base case: 1 element => return it as-is.
     *
     * Recursive case:
     *  divide - call itself on current subarray without last element;
     *  conquer - compare current call's last element with all elements to the left of it until an element is less or
     *      equal to it, while copying each that is greater to right; then insert current last element into the place before
     *      that less or equal element or 0 if we exceeded array's left boundary.
     *  combine - return the resulting array.
     *
     * Time: worst and average O(n^2) cause n-1 recursive calls, each taking O(n);
     *  best O(n) for an already ascending array (no shifts/inserts then).
     * Space: always O(1), only local vars, passing the same array reference.
     *
     * @param endInd - exclusive.
     */
    private fun ascendingClassicRecursiveInner(nums: IntArray, endInd: Int) {
        // TODO: endInd = 0 only if nums.size initially is 0 => where best to check for it,
        //  here or just once in [ascendingClassicRecursive]?
        if (endInd < 2) return

        val elementToInsertInd = endInd - 1
        val elementToInsert = nums[endInd - 1]
        ascendingClassicRecursiveInner(nums, elementToInsertInd)

        var i = max(0, elementToInsertInd - 1)
        while (i >= 0 && nums[i] > elementToInsert) {
            nums[i + 1] = nums[i]
            i--
        }
        nums[i + 1] = elementToInsert
    }

    /**
     * My first interpretation, implemented after only reading the cards' metaphor, before checking pseudocode.
     * Idea: swap the current element (nums\[i]) with it's left neighbor every time the neighbor is less than it.
     * Each swap takes 2 writes, whereas the original algo from the book is only 1 write (for non-final while iterations).
     */
    fun ascendingSwapAllGreaterWithKeyIterative(nums: IntArray): IntArray {
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

    /**
     * Same as [ascendingClassicIterative], only change the condition for key to be greater than nums\[j].
     */
    fun descendingClassic(nums: IntArray): IntArray {
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
        InsertionSort().ascendingClassicIterative(
            nums = intArrayOf(3, 2, 5, 1)
        ).joinToString(),
    )
}
