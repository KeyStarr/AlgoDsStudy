package com.keystarr.algorithm.array.twopointers.slidingwindow

import kotlin.math.abs
import kotlin.math.max

/**
 * LC-1208 https://leetcode.com/problems/get-equal-substrings-within-budget/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= w1 == w2 <= 10^5;
 *  • 0 <= maxCost <= 10^6;
 *  • w1 and w2 consist only of lowercase English.
 *
 * Final notes:
 *  • 🏆 done [efficient] by myself in 10 mins: abstracted the problem statement and learned straight away that we need
 *   the best valid subarray => recognized the hook to sliding window;
 *  • when read problem statement at first, felt the PTSD trigger from [com.keystarr.algorithm.graph.IsomorphicStrings],
 *   but powered through, good.
 *
 * Value gained:
 *  • practiced recognizing and solving the best valid substring problem using a sliding window.
 */
class GetEqualSubstringsWithinBudget {

    /**
     * problem rephrase:
     *  - given:
     *   - w1: String
     *   - w2: String
     *   - maxCost: Int
     *  - rules:
     *   - we can transform w1\[i] into w2\[i], but at the cost of abs(w1\[i]-w2\[i])
     *  Goal: find the best valid substring in w1, return its metric
     *   valid = all characters in the w1 substring match chars in w2 on corresponding positions, the sum of costs for all
     *    transformed chars is <= maxCost
     *
     * best valid subarray => try sliding window
     *  shrink:
     *   if adding current char (including that it can be transformed) exceeds [maxCost] => shrink until we can include it
     *  expand:
     *   add current char updating the cost
     *
     * edge cases:
     *  - maxCost == 0 => return the longest equal common matching pos substring of w1 and w2 => if any char differs, we'd add the cost >0
     *   to the currentCost, it'd exceed maxCost => we'd move left immediately to left=right+1; but if equal, we'd count
     *   that substring => correct;
     *  - w1.length == w2.length == 1 => correct.
     *
     * Time: always O(n)
     * Space: always O(1)
     */
    fun efficient(w1: String, w2: String, maxCost: Int): Int {
        var left = 0
        var currentCost = 0
        var maxValidLength = 0
        w1.forEachIndexed { right, c1 ->
            val c2 = w2[right]
            currentCost += abs(c1 - c2)
            while (currentCost > maxCost) {
                currentCost -= abs(w1[left] - w2[left])
                left++
            }
            maxValidLength = max(right - left + 1, maxValidLength)
        }
        return maxValidLength
    }
}
