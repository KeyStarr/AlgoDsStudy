package com.keystarr.algorithm.deque

/**
 * LC-496 https://leetcode.com/problems/next-greater-element-i/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums1.size <= nums2.size <= 10^3;
 *  • 0 <= nums\[i], nums2\[i] <= 10^4;
 *  • nums1 and nums2 both (irrespective of each other) contain distinct numbers;
 *  • all numbers from nums1 appear in nums2 (each exactly once);
 *  • no explicit time/space.
 *
 * Final notes:
 *  • the problem itself allowed for O(n^2) brute force time (which is considered leet-easy) BUT the follow-up for
 *      O(n) time IS DEFINITELY A LEET-MEDIUM!
 *  • failed 4 runs (2 of them submits!!!!!!!!!!!!!!!!):
 *      • 1st: FORGOT TO MAP NUMBER TO nums1 ind in order to fill the result\[i] correctly!
 *      • 2nd: FORGOT THAT nums2 may have numbers that ARE NOT present in nums1;
 *      • 3rd & 4th: INCORRECTLY decided to NOT add to stack numbers that nums1 doesn't contain - it turns out, these are
 *          still needed as possible answers for previous numbers that nums1 does contain. BUT we indeed to handle a
 *          case when the number popped from the stack IS NOT present in nums1 => simply don't calculate an answer for it then;
 *
 * Value gained:
 *  • practiced recognizing/implementing the monotonically sorted stack pattern;
 *  • practiced using a map as A PART of the solution;
 *  • realized a cool insight: started with converting nums1 into a map, BUT realized along the way that it's better to
 *      convert nums2! Otherwise, monotonically sorted stack/deque just didn't fit. I'm not sure if I would've even
 *      though of using it though if I hadn't known already that it was a part of the solution;
 *  • couldn't see an efficient solution straight away, so elaborated a great reasoning (sounds generalizable!):
 *      • described a brute force approach first;
 *      • identified what's the bottleneck time-wise, what part must be reduced from a factor of O(n) to O(1);
 *      • tried dry-running when felt stuck;
 *      • reversed the approach, cause it turned out that it's better start from nums2, and still - got both the
 *          converting into the map and using a monotonic stack!
 *      probably is a good way to start in the future when stack
 */
class NextGreaterElementI {

    /**
     * problem rephrase:
     * "for each number in nums1 return the next greater NUMBER from nums2, which is to the right of such index j
     * that nums1\[i] == nums2\[j]. if there's none, answer is -1.
     * return an array of such answers"
     *
     * note: both nums1 and nums2 contain only distinct numbers, nums1 is a subset of nums2
     *
     * how to improve time O(n^2) to O(n)?
     * => how to search for each nums1\[i] an equal number in nums2 (1) AND the next greater number after it, all in O(1)? (2)
     *
     * let's decompose:
     *  1. find equal number to nums1\[i] in nums2: we could turn nums2 into a map number->ind O(n)
     *      and find each such index in O(1);
     *  2.
     * NOPE not gonna work
     *
     * Efficient idea:
     * - convert the nums1 (!!) to map number->ind;
     * - iterate through nums2:
     *  - while stack.isNotEmpty() && currentNumber > stack.last():
     *      - nums1Ind = stack.removeLast()
     *      - result[nums1Ind] = currentNumber
     *  - stack.addLast(currentNumber)
     * - while stack.isNotEmpty():
     *  - nums1Ind = stack.removeLast()
     *  - result\[nums1Ind] = -1
     * - return result
     *
     * Edge cases:
     *  - nums1.size=1 && nums2.size=1 => correct;
     *  - nums1.size == nums2.size => correct.
     *
     * Time: always O(n), cause we add and pop each number in nums2 to the stack only once, the inner loop is amortized O(1).
     *  the second outer while is just dealing with numbers which were not popped following the same rule, so in a way it's
     *  together with the inner while, and it basically is amortized O(1) too.
     * Space: a map of always nums1.size + stack of average/worst nums2.size + result of nums1.size =>
     *  O(2*nums1.size + nums2.size) = O(nums2.size) cause nums1.size may be less than nums2.size but never greater.
     */
    fun efficient(nums1: IntArray, nums2: IntArray): IntArray {
        val nums1ToIndMap = mutableMapOf<Int, Int>()
        nums1.forEachIndexed { ind, num -> nums1ToIndMap[num] = ind }

        val stack = ArrayDeque<Int>()
        val result = IntArray(nums1.size)
        nums2.forEach { number ->
            while (stack.isNotEmpty() && number > stack.last()) {
                val nums1Ind = nums1ToIndMap[stack.removeLast()] ?: continue
                result[nums1Ind] = number
            }
            stack.addLast(number)
        }

        while (stack.isNotEmpty()) {
            val num1Ind = nums1ToIndMap[stack.removeLast()]?: continue
            result[num1Ind] = -1
        }

        return result
    }
}
