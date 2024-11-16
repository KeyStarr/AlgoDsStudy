package com.keystarr.algorithm.array.twopointers.slidingwindow

/**
 * LC-3254 https://leetcode.com/problems/find-the-power-of-k-size-subarrays-i/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= subarraySize <= nums.size <= 500;
 *  • 1 <= nums\[i] <= 10^5.
 *
 * Final notes:
 *  • 🔥 done [efficient] by myself as a Leet-daily in 38 mins, after 1.5 months of no algo problems;
 *  • made crucial observations early:
 *   - that rightmost element trivially is the metric if the subarray is valid;
 *   - recognized the general fixed sliding window approach;
 *   - => the problem narrows to determining if the subarray is valid or not efficiently as we move the fixed sliding window.
 *    (and a couple others such as that if the property is broken on expansion => we have to start left at right + that
 *    n-k last elements are -1 result always);
 *
 *  • came up with a brute force after couldnt find an efficient one in ~20 mins => that boosted my confidence and helped
 *   to confidently narrow the problem down to that inner iteration optimization;
 *  • arrived at the optimization by trying to implement it live with 2 solution iterations.
 *
 * Value gained:
 *  • solved a fixed sliding window problem with via an unusual structure; of moving both left and right pointers;
 *  • practiced a narrow-down solution approach based on a chain of core observations + brute-forced first, then optimized;
 *  • problem statement rephrase + examples study helped A LOT since "consecutive" subarray constraint wasn't clear at all,
 *   and the statement is delivered in a super overcomplicated manner.
 */
class FindThePowerOfKSizeSubarraysI {

    /**
     * problem rephrase:
     *  given:
     *   - nums: IntArray
     *   - subarraySize: Int
     *  goal: compute metric of all valid subarrays
     *   valid: subarrays of size == k
     *   metric: if the subarray forms a subsequence with step +1
     *
     * observations:
     *  A. basically we need to efficiently compute the metric for all valid subarrays;
     *   we can trivially make a loop to try all candidate subarrays, but how to efficiently compute the metric for each?
     *  B. apparently if the "consecutive/sorted" property is broken => it can only be right then with the next element
     *  C. the last k-1 elements are always going to have -1 as the result (cause invalid subarray size);
     *  D. cause ascending => trivially the max number will be the last number in the subarray, always.
     *
     * approach: brute
     *  1. try all valid subarray candidates; O(n-k+1)
     *  2. for each compute the property for the entire subarray every time. O(k)
     *
     *  Time: O((n-k)*k)
     *
     *  will fit under 1 sec cause max is 500 elements
     *
     * ---------------- approach: efficient
     *
     * can we optimize the inner iteration? how can we judge the new state of the property based on the element
     *  that is excluded + element that is added each iteration?
     *
     * since the max element would always be the last (ascending), narrow the problem even more
     *  => how to tell if the property holds in O(1) given that we added a new element?
     *  we may simply compare it with the element before it
     *
     *
     * fixed size sliding window
     *
     *
     * 1 2 3 2 1 4
     * 1 2
     *   2 3
     *       2
     *         1
     *         1 4
     *
     *
     * edge cases:
     *  - [subarraySize] == 1 => always trivially return the array itself via O(1) time;
     *  - nums.size == 1 == k => first edge case;
     *  - nums.size == 2, k == 2 same as k == nums.size => only if the entire array is valid then return its last element
     *   => as-is doesn't enter the loop => rewrite, expand then check the property for the subarray [left:right], not the right+1
     *
     * Time: always O(n)
     *  - both left and right at most visit all (nums-subarraySize+1) elements;
     *  - work done at each iteration is O(1).
     * Space: always O(1) not counting the results array.
     */
    fun efficient(nums: IntArray, subarraySize: Int): IntArray {
        if (subarraySize == 1) return nums

        val n = nums.size
        val results = IntArray(size = n - subarraySize + 1) { -1 }

        var left = 0
        var right = 1
        while (right < n && left <= n - subarraySize) {
            if (nums[right] - nums[right - 1] == 1) {
                if (right - left + 1 == subarraySize) {
                    results[left] = nums[right]
                    left++
                    right++
                } else {
                    right++
                }
            } else {
                left = right
                right++
            }
        }

        return results
    }
}

fun main() {
    println(
        FindThePowerOfKSizeSubarraysI().efficient(
            nums = intArrayOf(1,2,3,4,3,2,5),
            subarraySize = 3,
        ).contentToString()
    )
}
