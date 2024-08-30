package com.keystarr.algorithm.array.twopointers

/**
 * LC-2540 https://leetcode.com/problems/minimum-common-value/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums1.size, nums2.size <= 10^5;
 *  • 1 <= nums1\[i], nums2\[j] <= 10^9;
 *  • both arrays are SORTED NON-DECREASING.
 *
 * Final notes:
 *  • done [efficient] by myself in 10 mins;
 *  • ⚠️⚠️⚠️ for 5 mins I WAS SOLVING THE WRONG PROBLEM due to not-focusing enough! Got my mind drifting away.
 *   Sorted property of input makes a crucial difference here. Felt my focus drifting before starting the problem,
 *   probably has to do with sleep schedule and not eating today yet;
 *  • technically there is a binary search solution as well which is better on some input states, but I feel like
 *   it's not worth it rn.
 *
 * Value gained:
 *  • practiced recognizing and solving a 2 array property type problem efficiently using 2 pointers.
 */
class MinimumCommonValue {

    /**
     * goal: return the minimum common integer of both arrays.
     *
     * trivial approach => sort both arrays, use two pointers to pass through both, return upon encountering the first common number.
     * Time: always O(nlogn + mlogm)
     * Space: always O(logn + logm) for sorting
     *
     * -----------------
     *
     * Can we do better? Can we do O(n+m) time?
     *
     * We can add either array into a set, and pass through another array => as soon as we encounter an element present in the
     *  first (through set.contains), update the common minimum
     * Time: always O(n+m)
     *  - add into a set: either O(n) or O(m);
     *  - find minimum: either O(m) or O(n)
     * Space: always O(n) or O(m)
     *  - set takes either O(n) or O(m) space.
     *
     * If we add into a set the smaller of arrays => we'd get potentially better space const, but since we'd have to iterate
     *  along the entire longer one => potentially worse time const. Though arguably converting a longer array into a set
     *  could take more time const impact due to hashing impact on const.
     *
     * But asymptotically it doesn't matter.
     *
     * --------------------
     *
     * 🤦‍️ BOTH ARRAY ARE SORTED NON-DECREASING!!!!! HOW COULD I MISS THAT!!!!!!!!!!!!!!!!
     * literally 2 pointers, just iterate....
     *
     * edge cases:
     * - there is no common number between arrays => return -1.
     *
     * Time: average/worst O(n + m)
     * Space: always O(1)
     */
    fun efficient(nums1: IntArray, nums2: IntArray): Int {
        var nums1Ind = 0
        var nums2Ind = 0
        while (nums1Ind < nums1.size && nums2Ind < nums2.size) {
            val num1 = nums1[nums1Ind]
            val num2 = nums2[nums2Ind]
            when {
                num1 == num2 -> return num1
                num1 > num2 -> nums2Ind++
                else -> nums1Ind++
            }
        }
        return -1
    }
}
