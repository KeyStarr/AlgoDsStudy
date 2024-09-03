package com.keystarr.algorithm.array.twopointers

/**
 * LC-283 https://leetcode.com/problems/move-zeroes/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.size <= 10^4;
 *  • 2^31 <= nums\[i] <= 2^31 - 1.
 *
 * Final notes:
 *  • 🏆 done [efficient] by myself in 12 mins:
 *   • inferred the tricky part straight ahead, reduced the solution space to shifting;
 *   • quickly designed the shifting formula via dry-running and observing 🔥 worked great, first tried to design abstractly
 *    in the head, but failed => dry-running on concrete examples wins;
 *  • found and wrote down the edge case of 0 not being the 1st element BUT failed to account for it with that special case return!!!!
 *   ⚠️ failed the 1st submission.
 *
 * Value gained:
 *  • practiced solving an array in-place modification type problem efficiently using a tricky formula with swapping.
 *   technically it's like two pointers, but we calculate the 2nd pointer on-the-fly, don't keep it saved into a var
 */
class MoveZeroes {

    /**
     * The obvious approach would be to get indices of zeroes, then delete all zeroes by indices from the array and append
     *  required zeroes count to the end => but that would be O(n^2) time and O(n^2) space, since the number of zeroes
     *  in "the middle" of the array is up to n-1 and each deletion from the middle is O(n) due to array reallocation
     *
     * How to do that in O(n) time, in-place, without removing elements from the array?
     *  => shifting
     *
     * Approach #2:
     *  iterate through the array:
     *   - when we encounter a zero, increase the counter;
     *   - as we encounter an element that is not a zero => write zero to the current index, and write the element to the
     *    index of the array = currentInd - zerosCounter.
     *
     * Edge cases:
     *  - there are elements before the first zero => leave them as-is => CHECK ZEROES COUNT and return, don't do modifications if its 0;
     *  - nums.size == 1 => works correct as-is if it's a 0 or non-zero, either way.
     *
     * Time: always O(n)
     * Space: always O(1)
     *
     * ex 1
     * 0 1 0 3 0 12
     *
     * i=5
     * zeroesCount=3
     * 1 3 12 0 0 0
     * 5-3=2
     *
     *
     * ex 2
     * 1 0 0 3 0 12
     * 1 3 0 0 0 12
     *
     * -----
     * probably there is also an alternative single pass solution with explicitly keeping track of the insertion index
     * by focusing not on the zeros count, but numbers count
     */
    fun efficient(nums: IntArray) {
        var zeroesCount = 0
        nums.forEachIndexed { ind, number ->
            if (number == 0) {
                zeroesCount++
            } else if (zeroesCount != 0) {
                nums[ind - zeroesCount] = number
                nums[ind] = 0
            }
        }
    }
}
