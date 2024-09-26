package com.keystarr.algorithm.dp.fibonacci

/**
 * LC-70 https://leetcode.com/problems/climbing-stairs/description/
 * difficulty: easy (LOL, spent an hour learning here..)
 *
 * Final notes:
 *  • 🏆the first DP question I've solved asking for an amount of combinations;
 *  • huh, so the core algorithm is exactly like Fibonacci (num\[i]=num\[i-1]+num\[i-2]), just with different base cases 🔥
 *  • I got very confused on bottom-up producing the wrong answer on `return results\[stepsToTop]. The reason was obvious
 *   => bottomUp(stepsToTop) is producing the total combinations to reach the top FOR that amount of steps, NOT the
 *    amount of combinations to reach the LAST==stepsToTop step, which, it turned out, I implicitly assumed.
 *  • ⚠️ FAILED 4 SUBMISSIONS! My absolute anti-record for a single leet problem. Gotta be more cautious with DP, double, triple-check!
 *  • funny how exactly the 2 vars bottom-up DP got that specific edge case for input==1 requiring an early return. Didn't
 *   expect that, gotta be cautious with DP bottom-up space optimizations in the future (check for emergent edge cases!!!!);
 *  • 🔥 once again, the problem is Fibonacci-like DP since the computation of the 1D state with startInd=x depends only on
 *   dp(x-1) and dp(x-2) => can be optimized to O(1) space.
 *
 * Value gained:
 *  • gotta be ALWAYS wary of EXACTLY WHAT the goal for the function is in DP, with complete precision, to return the
 *   right answer (from this question, hypothesis - DP might get tricky in that regard);
 *  • first-time practiced both bottom-up and top-down DP on counting the number of valid combinations;
 *  • gotta be extra-careful with DP submissions, triple-check;
 *  • gotta be wary of emergent edge cases with the DP bottom-up space optimizations.
 */
class ClimbingStairs {

    private val stepsToCombinationsMap = mutableMapOf<Int, Int>()

    /**
     * 1. the questions asks for the number of combinations
     * 2. each choice affects future choices
     * => try DP
     *
     * goal: find the amount of all distinct step combinations to climb to reach the top
     * input: rightInd: Int (=stepsToTop on initial call)
     * recurrence relation: dp(rightInd-1) + dp(rightInd-2)
     * base cases:
     *  dp(1) = 1
     *  dp(0) = 1
     * hashmap rightInd->combinationsCount for memoization
     *
     * Time: O(n) cause we compute the answer for n states (cutting of 1 step a time)
     * Space: O(n) for the hashmap, since there are only 2 states needed to compute the answer for dp(rightInd) =>
     *  bottom up could improve the space to O(2)
     */
    fun topDown(stepsToTop: Int): Int {
        if (stepsToTop == 1 || stepsToTop == 2) return stepsToTop

        val cache = stepsToCombinationsMap[stepsToTop]
        if (cache != null) return cache

        val combinationsCount = topDown(stepsToTop - 1) + topDown(stepsToTop - 2)
        return combinationsCount.also { stepsToCombinationsMap[stepsToTop] = it }
    }

    /**
     * Edge cases:
     *  - an unexpected edge case specific to exactly bottom-up with 2 variables:
     *   only if [stepsToTop] == 1, then we must return 1 (prevPrevStepCombs) and not prevStepCombs.
     *   it wasn't the case with the O(n) space bottom-up, cause with array we'd return results[[stepsToTop]-1], which for
     *   [stepsToTop]==1 would be results[0]=1 (base case).
     *
     * Time: O(n)
     * Space: O(1)
     */
    fun bottomUp(stepsToTop: Int): Int {
        var prevPrevStepCombs = 1
        var prevStepCombs = 2
        for (step in 2 until stepsToTop) {
            val buffer = prevStepCombs
            prevStepCombs = prevStepCombs + prevPrevStepCombs
            prevPrevStepCombs = buffer
        }
        return prevStepCombs
    }
}
