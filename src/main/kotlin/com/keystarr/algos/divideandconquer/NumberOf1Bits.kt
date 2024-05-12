package com.keystarr.algos.divideandconquer

/*
 * LC-191 https://leetcode.com/problems/number-of-1-bits/description/
 * difficulty: easy
 * constraints:
 *  0 <= n <= 2^31 - 1 (text says n > 0, but test case #3 is n = 0)
 * no explicit time/space constraints
 */
class NumberOf1Bits {

    // intuition - implement a partial binary conversion algorithm, only record the amount of set bits.
    // tools - divide&conquer, recursion.
    // time: O(logn)
    // space: O(1)
    // TODO: why for n = Long.MAX_VALUE does this take 8x times slower than bitwise???
    //  e.g. recursive took 10958 ns, bitwise took 1334 ns
    fun recursive(n: Int): Int = if (n == 0) 0 else recursiveIteration(n)

    private fun recursiveIteration(n: Int): Int {
        if (n == 1) return 1
        return n % 2 + recursiveIteration(n / 2)
    }

    // intuition - use kotlin standard tools to manipulate the already existing binary representation of the number
    // time: O(logn)
    // space: O(1)
    fun bitwise(n: Int): Int {
        var setBitsCount = 0
        var remainingNumber = n
        while (remainingNumber > 0) {
            setBitsCount += remainingNumber and 1
            remainingNumber = remainingNumber.ushr(1)
        }
        return setBitsCount
    }
}

fun main() {
    println(NumberOf1Bits().recursive(Int.MAX_VALUE))
}
