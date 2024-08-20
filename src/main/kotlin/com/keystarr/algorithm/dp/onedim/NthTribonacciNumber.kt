package com.keystarr.algorithm.dp.onedim

/**
 * LC-1137 https://leetcode.com/problems/n-th-tribonacci-number/description/
 * difficulty: easy
 * constraints:
 *  • 0 <= n <= 37;
 *  • the answer is guaranteed to fit into Int.
 *
 * Final notes:
 *  • done [solution] by myself in 15 mins.
 *   overengineered a little going for the general solution, but what for? here we hardcode prev is OK, for easy more then;
 *  • a very basic DP bottom-up problem. Cause I'm familiar enough with DP and practiced Fibonacci with DP particularly
 *   => I was able to jump straight to the space-optimized bottom-up DP solution (instead of the regular walk of top-down then
 *    space suboptimal bottom-up).
 *
 * Value gained:
 *  • practiced recognizing and solving a DP problem with a bottom-up space-optimized solution straight-away;
 *  • stopped myself from getting too abstract and focused on the specific of the problem at hand => saved time and practiced KISS.
 */
class NthTribonacciNumber {

    /**
     * We just need to know the 3 previous Tribonacci numbers => use an array/3 variables to keep track of those.
     * Technically: bottom-up DP (subproblems depend on other suproblems)
     *
     * main loop invariant: sum is the k+i tribonacci number, where init k=3
     *  init: k+0=3, sum is the 4th tribonacci number which 0+1+1=2 => correct
     *
     * Edge cases:
     *  - n < 3 => return the base cases.
     *
     * Time: always O(n)
     * Space: always O(1)
     */
    fun solution(n: Int): Int {
        val prevNumbers = IntArray(size = 3).apply {
            set(0, 0)
            set(1, 1)
            set(2, 1)
        }
        if (n < 3) return prevNumbers[n]

        var sum = 2
        repeat(n - 2) {
            val newNumber = sum
            sum += newNumber - prevNumbers[0]
            prevNumbers[0] = prevNumbers[1]
            prevNumbers[1] = prevNumbers[2]
            prevNumbers[2] = newNumber
        }

        return prevNumbers[2]
    }
}
