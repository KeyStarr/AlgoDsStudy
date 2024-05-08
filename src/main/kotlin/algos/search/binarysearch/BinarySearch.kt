package algos.search.binarysearch

// half a year later
// https://leetcode.com/problems/binary-search/description/
/**
 * Classic binary search on int array.
 * Special almost clear case is LC-1512 https://leetcode.com/problems/number-of-good-pairs/description/
 * Difficulty: easy
 * Constraints:
 *  • 1 <= nums.size <= 10^4;
 *  • -10^4 < nums\[i] < 10^4;
 *  • all integers in nums are unique;
 *  • nums is sorted ascending;
 *  • worst case time <= O(logn);
 *  • no explicit space constraint.
 *
 *  (actually implemented for nums.size == 0 also and for numbers fitting into int in general)
 */
class BinarySearch {

    /**
     * @param nums - must be sorted ascending.
     */
    fun recursive(nums: IntArray, target: Int): Int = recursiveInternal(nums, target, 0, nums.size)

    /**
     * Base case:
     *  - if current subarray has 0 element => [target] is not present in the array => return -1;
     *
     * Recursive case:
     *  divide: compare [target] with the middle element of current subarray, if [target] is greater than search only in
     *      the right half of the subarray, otherwise in the left (always excluding the middle element).
     *  conquer:
     *      - if as the result of comparison the middle element equals [target] => return its index;
     *      - otherwise, call itself on the divided subarray;
     *  combine: trivially propagate either -1 or found [target]'s middleInd as-is up the callstack.
     *
     * Time:
     *  - worst O(logn), cause if [target] isn't present we floor divide the array in two until it's size is 0
     *      => exactly lgn + 1 => O(logn);
     *  - average O(logn), cause on average we have to perform lgn/2 checks which growths proportional to lgn;
     *  - best O(1) if the middle element of the original array is the [target].
     * Space: always O(1), cause we only introduce local variables and no arrays.
     *
     * Recurrence equation for worst case time:
     *  D(n) = 1, cause we trivially update pointers;
     *  T(n) = T(n/2)
     *  C(n) = 1, cause we trivially propagate the resulting index.
     *
     * T(n) =
     *  { 1, n.size = 0
     *  { T(n/2), n.size > 0
     *
     * @param nums - must be sorted ascending;
     * @param startInd - inclusive;
     * @param endInd - exclusive.
     */
    private fun recursiveInternal(nums: IntArray, target: Int, startInd: Int, endInd: Int): Int {
        if (startInd >= endInd) return -1

        val middleInd = (startInd + endInd) / 2
        val middleNum = nums[middleInd]
        if (middleNum == target) return middleInd

        return if (target > middleNum) {
            recursiveInternal(nums, target, middleInd + 1, endInd)
        } else {
            recursiveInternal(nums, target, startInd, middleInd)
        }
    }

    /**
     * The concept is same as [recursiveInternal] but instead of recursive calls we simply go to the next iteration
     * => time and space complexity is the same also.
     */
    fun iterative(nums: IntArray, target: Int): Int {
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
    println(BinarySearch().recursive(intArrayOf(1, 2, 3), 3))
}
