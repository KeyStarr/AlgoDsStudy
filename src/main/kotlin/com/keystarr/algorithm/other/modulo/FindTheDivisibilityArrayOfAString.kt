package com.keystarr.algorithm.other.modulo

/**
 * LC-2575 https://leetcode.com/problems/find-the-divisibility-array-of-a-string/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= word.length <= 10^5;
 *  • word consists of only 0..9;
 *  • 1 <= divisor <= 10^9.
 *
 * Final notes:
 *  • couldn't solve by myself in 10-15 mins, formed the question on what we need to do ("how can we break...") but
 *   didn't see the solution. Turns out we have to just start not from the right but from the left, the problem itself
 *   nudges us into that direction. And just use the formula `num{i} = (num{i-1} * 10 + digit{i}} % divisor` for constructing
 *   and computing the modulo of the new number via a modulo sum property;
 *  • a great problem to practice modular arithmetic tricks, this could really save some resources in prod.
 *
 * Value gained:
 *  • practiced modular arithmetics;
 *  • practiced constructing the number and computing the answer starting with the highest order digits using modulo
 *   property with the sum.
 */
class FindTheDivisibilityArrayOfAString {

    /**
     * given:
     *  - word: a string of digits; divisor integer
     *  - goal: for each sub-number word[0:i], if its divisible by [divisor], set 1 else 0, return the array of results for
     *   all such possible sub-numbers of the [word].
     *
     * since [word] can be a very huge number + we only need, for each sub-number, to tell if it's divisible => try using
     * modulo properties of partial modulo?
     *
     * word= 998244353 m= 3
     *
     * how can we break the word down into either a sum or a product of elements that either fit into Int or Long?
     * 9 = 0 * 10 + 9
     * 99 = 9 * 10 + 9
     * 998 = 99 * 10 + 8
     * 9982 = 998 * 10 + 2
     * ...
     * nextNumber = prevNumber * 10 + newDigit
     *
     * since we need only the result of the modulo => we can for `nextNumber` store only it's result for the modulo
     * number{i} = (number{i-1} * 10 + digit{i}) % divisor
     *
     * edge cases:
     *  - word.length == 1 => correct as-is;
     *  - will (number{i-1} * 10 + digit{i}) ever exceed Int? if divisor = 10^9 and we have word.length=10^5, we will
     *   easily reach the Int bound and exceed it eventually. number{i-1} can reach up to 10^9 and then we have *10 and
     *   get 10^10 => store that sum in a Long
     *
     * Time: O(n), n = [word].length
     * Space: O(1) if we don't count the result
     */
    fun efficient(word: String, divisor: Int): IntArray {
        val result = IntArray(size = word.length)
        var intermediate = 0L
        word.forEachIndexed { ind, char ->
            val digit = char - '0'
            intermediate = (intermediate * 10L + digit) % divisor
            result[ind] = if (intermediate == 0L) 1 else 0
        }
        return result
    }
}
