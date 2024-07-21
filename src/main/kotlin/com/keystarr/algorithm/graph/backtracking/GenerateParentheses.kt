package com.keystarr.algorithm.graph.backtracking

/**
 * LC-22 https://leetcode.com/problems/generate-parentheses/
 * difficulty: medium
 * constraints:
 *  • 1 <= n <= 8.
 *
 * Final notes:
 *  • cool, backtracking pattern as-is yet again. It kills generation problems, at least some;
 *  • here pruning = generation of next nodes in the decision tree was the biggest part for adaption;
 *
 * Value gained:
 *  • practiced backtracking on all combinations generation problem.
 */
class GenerateParentheses {

    /**
     * generate all valid combinations => try backtracking
     *
     * pruning/available neighbors:
     *  - when openBracketsCount > 0 => we can put either ")" or "(", otherwise only an open bracket;
     *  - stop when endBracketCount==n;
     *
     *
     * edge cases:
     *  - n == 1 => just a single combination, correct;
     *  - n == 2 => 2, correct.
     *
     * Time: average/worst O(2^n)
     *  - since each a valid pair of parenthesis consist of exactly 2 elements, each combination will have the length
     *   of exactly n*2 => decision tree height is exactly n*2 with all laves at the last level
     *  - at each node we'll have at most 2 next nodes, but sometimes just 1 if we used up all opening brackets,
     *   so average/worst = 2^(n*2) = O(2^n)
     * Space: O(n) if we don't include the results space
     *  - height of the callstack is n*2
     *  - results size worst/average O(2^n), but actually less
     */
    fun solution(n: Int) = mutableListOf<String>().apply {
        backtrack(
            current = "",
            openedBrackets = 0,
            closedBrackets = 0,
            n = n,
            results = this,
        )
    }

    private fun backtrack(
        current: String,
        openedBrackets: Int,
        closedBrackets: Int,
        n: Int,
        results: MutableList<String>,
    ) {
        if (openedBrackets == n && closedBrackets == n) {
            results.add(current)
            return
        }

        if (openedBrackets - closedBrackets > 0) {
            backtrack("$current)", openedBrackets, closedBrackets + 1, n, results)
        }
        if (openedBrackets != n) {
            backtrack("$current(", openedBrackets + 1, closedBrackets, n, results)
        }
    }
}
