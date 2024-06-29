package com.keystarr.algorithm.greedy

import kotlin.math.min

/**
 * LC-1710 https://leetcode.com/problems/maximum•units•on•a•truck/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= boxTypes.length <= 1000
 *  • 1 <= numberOfBoxes, numberOfUnitsPerBox <= 1000
 *  • 1 <= boxesLimit <= 10^6
 *
 * Final notes:
 *  • done [suboptimal] by myself in 20 mins (⚠️⚠️too much for an easy problem!!)
 *  • during the implementation I've realized an error in the design and spent time correcting both the draft of the
 *   algo in the docs AND the actual implementation in the same way => maybe I should drop writing such detailed pseudocode
 *   at least for easy/medium problems? Just describe the algo roughly with words only, with no logical operators and such details
 *   whatsoever?
 *
 * Value gained:
 *  • practiced recognizing and solving a greedy problem;
 *  • 💡try describing the algo in the docs ONLY via words and 1-2 abstraction levels higher than now, see if it will give
 *   sufficient precision for the communication with the interviewer/complexities approximation and faster implementation,
 *   yet save time on both coding and correcting errors.
 */
class MaximumUnitsOnATruck {

    /**
     * Problem rephrase:
     *  - given are:
     *   - Array<IntArray>, for each array\[i] array\[i][0] is number of boxes and array\[i][1] is the number
     *    of units per each such box;
     *   - boxesLimit
     * Goal: return the maximum total number of units such that the number of boxes used doesn't exceed boxes limit
     *
     * Maximum + the description resembles rules for a single move => try greedy
     * What would be the locally optimal move?
     * since all boxes take exactly 1 measure of total space, and we maximize the total amount of boxes contents (units)
     * => greedily every step choose the box out of the remaining ones with the highest unit count (until boxes used exceed boxesLimit)
     *
     * Algorithm:
     *  - sort descending the [boxTypes] by the boxTypes\[i][1] (units count in a box)
     *  - boxesLeftToUse = boxesLimit
     *  - totalUnits = 0
     *  - while i < boxTypes.size && boxesLeftToUse > 0:
     *   - boxType = boxTypes\[i]
     *   - boxesToUse = min(boxType[0], boxesLeftToUse)
     *   - totalUnits += boxesToUse * boxType[1]
     *   - boxesLeftToUse -= boxesToUse
     *   - return totalUnits
     *  - return totalUnits // boxesLimit is greater than the total number of all boxes
     *
     * Edge cases:
     *  - boxesLimit is greater than the total number of boxes => use all the boxes, stop iterating if there are no boxes
     *   left to use, correct;
     *  - sum of units => max number of total units is 1000 * 1000 * 1000 = 10^9 => fits into Int, ok.
     *
     * Time: always O(nlogn)
     *  - sorting always O(nlogn), where n=boxesType.size
     *  - counting loop average/worst O(n)
     * Space: always O(1) if in-place sorting, otherwise O(n) for the sorted array
     */
    fun suboptimal(boxTypes: Array<IntArray>, boxesLimit: Int): Int {
        boxTypes.sortByDescending { it[1] }
        var boxesLeftToUse = boxesLimit
        var totalUnits = 0
        var currentInd = 0
        while (currentInd < boxTypes.size && boxesLeftToUse > 0) {
            val maxUnitsBoxType = boxTypes[currentInd]
            val boxesToUse = min(maxUnitsBoxType[0], boxesLeftToUse)
            totalUnits += boxesToUse * maxUnitsBoxType[1]
            boxesLeftToUse -= boxesToUse
            currentInd++
        }
        return totalUnits
    }

    // TODO: solve for O(n) time via count sort and/or bucket sort?
}
