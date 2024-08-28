package com.keystarr.algorithm.greedy

import kotlin.math.min

/**
 * ⭐️ a stark example of "generate the best valid combination" type problem with a solution more optimal than backtracking
 * LC-1663 https://leetcode.com/problems/smallest-string-with-a-given-numeric-value/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= targetLength <= 10^5;
 *  • targetLength <= targetValue <= 26 * targetLength.
 *
 * Final notes:
 *  • ⚠️ TLE'd on [wrongBacktracking] in 17 mins;
 *   I could feel that this is a wrong approach, given that max targetLength is 10^5, 2^(10^5) is certainly way too inefficient :D
 *   Why did I waste time on backtracking then, why didn't go straight to another approach? After the gym, want to eat =>
 *   maybe I'm just on autopilot more than not rn?
 *   ⚠️ still, I could totally prevent it and DID NOT! Also, I estimated at first worst time to be 26*targetLength and
 *    went like "ah, that's fast enough, only 26 chars here backtracking might work!" BUT MAN HOW COULD I! Yep, probably
 *    low on sugar on something. ⚠️🔥 TOTALLY PREVENTABLE! BACKTRACKING IS NEVER THE ANSWER ON LENGTHS THAT LARGE CAUSE OF EXPONENT RELATIONSHIP WITH LENGTH!!!!
 *  • 🏅⚠️ done [efficient] by myself in 45 mins;
 *  • 🔥🔥 at first didn't see a solution other than backtracking whatsoever, then reduced the possible solution space to
 *   look for time complexity <= n^2, possibly n, and finally that probably there are some tricks/formulas given how narrow
 *   constraints we have on available numbers for generation and both exact length/total value
 *   => tried to reason from the SMALLEST INPUT SIZE, start at right/left, what is the best possible choice we can make
 *    at a SINGLE STEP at the simplest input state? (full leftValue, empty result so far!!!!!)
 *    🌟 what are the best choices for the first small step => build on it.
 *  • if I hadn't gone the backtracking path, wouldn't even design it after checking it wouldn't fit the time constraints
 *   on the max required length => I could save ~17 mins => finish the problem at about 30 mins mark => that would be
 *   perfectly acceptable for a medium => 🔥 try to eliminate tools as early as possible.
 *
 * Value gained:
 *  • solved a "generate the best valid combination" type problem using greedy and not backtracking! greedy based on
 *   a couple of tricky observations given the constraints about the current best choice;
 *  • 🔥REINFORCED that generation problems with max combination length constraint more than a couple dozen IS NEVER BACKTRACKING.
 *   by failing and learning [backtrack] :D so funny I even went for it, oh man.
 */
class SmallestStringWithAGivenNumericValue {

    // TODO: retry in 1-2 weeks

    /**
     * ok, backtracking is wrong => what smart tricks can we use here, some formulas to reduce the possible solutions space
     *  down to linear time or nlogn, at least a square??
     *
     * observation:
     *  - we could compute the minimum string value achievable = [targetLength].
     *   => then ([targetValue] - minValidValue) is the diff that we need to fill by increasing our characters values.
     *  - note: we want characters as small as possible as early as possible
     *   => we could try to generate the string backwards and attempt to reduce the remainder by the most VALID amount
     *    for each position. Valid = we must have at least `positionsLeft` to fill values in the `leftValue`, aside from
     *    that at each step we want to take out as much as we can. If that condition is broken, then we select the current character
     *    to be as large as we can without violating that constraint => fill the rest positions with 'a'.
     *
     * so the main tool here is greedy.
     *
     *
     * tL = 5    tV = 55
     * r = zz    vL = 3
     * 5-2=2
     * 3-2=1
     *
     *
     * tL = 5    tV = 55
     * r = zzaa    vL = 1
     * 5-5=0
     * 1-0=1
     *
     *
     *
     * tL = 3    tV = 27
     * r = yaa     vL = 1
     * minNextValidLeftValue=3-0-1=2
     * maxValidChar=min(26,27-2)=min(26,25)=25 (y)
     *
     * minNextValidLeftValue=3-1-1=1
     * maxValidChar=min(26,2-1)=1 (a)
     * result = aay
     *
     * Time: always O(targetLength)
     *  - we always have exactly [targetLength] iterations;
     *  - each iteration costs O(1);
     *  - builder reversal is O(n) and string building is O(n);
     * Space: if we count the result, O(n) otherwise O(1).
     */
    fun efficient(targetLength: Int, targetValue: Int): String {
        val result = StringBuilder()
        var leftValue = targetValue
        while (result.length != targetLength) {
            val minNextValidLeftValue = (targetLength - result.length - 1)
            val maxValidChar = min(26, leftValue - minNextValidLeftValue)
            result.append('a' + (maxValidChar - 1))
            leftValue -= maxValidChar
        }
        return result.reverse().toString()
    }

    /**
     * retrospective note: when at least one valid answer exists, the result string will always have as many trailing 'z' as possible,
     * then either 'a' or a character ['b':'z'] and optionally a prefix of 'a'.
     *
     * --------
     *
     * can we reduce time const => avoid string reversal at the end?
     *
     * we can apply the same logic as [efficient], but generate the string in-order left-to-right!
     *
     * 1. greedy goal for each step then => append to the string the char with minimum possible value
     * 2. if (leftValue - 1 - nextPositionsLeft * 26 > 0) we must choose not 'a' but the valid minimum character
     *  which then is leftValue - nextPositionsLeft * 26
     *  so currentChar = 'a' + min(1, leftValue - nextPositionsLeft * 26)
     *   if second is negative, then
     *
     *
     * tL = 3    tV = 27
     * r = aa     vL = 25
     * nextPositionsLeft = 3-0-1=2
     * minValidChar = 1 or 27 - 2 * 26 = 1
     *
     * nextPositionsLeft = 3-1-1=1
     * minValidChar = 1 or 26 - 26 = 1
     *
     * nextPositionsLeft = 0
     * minValidChar = 1 or 25 = 25
     *
     *
     *
     * tL = 3    tV = 30
     * r =      vL = 30
     * nextPositionsLeft = 2
     * minValidChar = 1 or 30-26*2 = 1
     *
     * nextPositionsLeft = 1
     * minValidChar = 1 or 30-26 = 4
     * ----------
     *
     * => same complexities as [efficient], but better time const (no builder reversal).
     *
     */
    fun efficientCleaner(targetLength: Int, targetValue: Int): String {
        val result = StringBuilder()
        var leftValue = targetValue
        while (result.length != targetLength) {
            val nextPositionsLeft = targetLength - result.length - 1
            val valueToFillWithAllNextMaxes = leftValue - nextPositionsLeft * 26
            val minValidChar = if (valueToFillWithAllNextMaxes <= 0) 1 else valueToFillWithAllNextMaxes
            result.append('a' + minValidChar - 1)
            leftValue -= minValidChar
        }
        return result.toString()
    }


    /* wrong backtracking */


    /**
     * WOW, wrong => TLE on 13th/94 testcases.
     *
     * -------------
     *
     * problem rephrase:
     *  - given:
     *   - targetLength: Int
     *   - targetValue: Int
     *  Goal: generate and return the best valid string
     *   valid = length equals [targetLength] and numeric value (sum of all chars number values, only English lowercase, a is 1)
     *    equals [targetValue]
     *   best = smallest lexicographically
     *
     * are only English lowercase chars allowed in the output string? Not specified directly in the problem, no one to ask,
     *  assume so judging by the examples and the general direction.
     *
     * approach: try backtracking? worst amount of combinations to try is 26 * 10^5, which is fairly reasonable.
     *  - since we want the lexicographically smallest string => for each position try lowercase English characters in alphabetical order;
     *  - keep track of the valueLeft: Int, prune if:
     *   - valueLeft < 0
     *   - length == targetLength
     *  - if valueLeft == 0 return the answer. Each step we prioritized the smallest possible numerically characters =>
     *   and increased the char value only backwards => the result is the lexicographically smallest valid string.
     *
     * edge cases:
     *  - min possible valid string value is targetLength * 1 and max possible is targetLength * 26 => if targetValue is
     *   either less than targetLength or greater than targetLength * 26, there is no valid answer
     *   => such input is impossible according to input constraints
     *   => AT LEAST ONE VALID STRING IS GUARANTEED TO EXIST => answer always exists.
     *
     * Time: average/worst O(targetLength)
     *  - worst is the only valid string is 'z' * targetLength => we would have tried g ^ [targetLength] combinations then,
     *   where g = amount of allowed characters, here g=26, so time is O(26*targetLength)=O(targetLength)
     * Space: O(targetLength)
     *  - worst callstack height is [targetLength];
     *  - result stringbuilder max length is [targetLength]
     */
    fun wrongBacktracking(targetLength: Int, targetValue: Int): String =
        backtrack(current = StringBuilder(), leftValue = targetValue, targetLength = targetLength)!!

    private fun backtrack(current: StringBuilder, leftValue: Int, targetLength: Int): String? {
        if (current.length == targetLength && leftValue == 0) return current.toString()
        if (current.length == targetLength || leftValue <= 0) return null

        repeat(26) { offset ->
            val newChar = 'a' + offset
            current.append(newChar)
            val result = backtrack(current, leftValue - (offset + 1), targetLength)
            if (result != null) return result
            current.deleteAt(current.length - 1)
        }

        return null
    }
}

fun main() {
    println(
        SmallestStringWithAGivenNumericValue().efficientCleaner(targetLength = 3, targetValue = 27)
    )
}
