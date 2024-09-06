package com.keystarr.algorithm.greedy

/**
 * LC-1323: https://leetcode.com/problems/maximum-69-number/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= num <= 10^4
 *  • num consists of only 6 and 9 digits
 *
 * Final notes:
 *  • compare [strings] and [onlyIntegers]. Why did I do the latter? What am I spending my life on?
 *  • actually we could avoid computing the num of digits in [onlyIntegers] and simply go through all digits starting from
 *   the first one, and then return the highest one.
 *
 * Value gained:
 *  • practiced recognizing and solving the problem via greedy;
 *  • practiced multiple ways to work through int's digits.
 */
class Maximum69Number {

    /**
     * Maximum + the description resembles a single move => try greedy.
     *
     * What would be the locally optimal decision each step?
     * In order to maximize the number we need to flip the first 6 that takes in the highest position.
     *
     * i.o. for both algos: greedily try check if the highest non-checked digit is 6, if so, convert it to 9 and return.
     *
     * -------------------
     *
     * Algorithm:
     *  - convert the number into a string;
     *  - iterate through the string from the start, find the first 6, replace it via 9;
     *  - convert the resulting string back into the integer.
     *
     * Edge cases:
     *  - there are no 6 in the [num] => return the number itself => works correct as-is.
     *
     * Time: always O(n) where n=number of digits in the [num]
     * Space: O(n) for the intermediate string
     */
    fun strings(num: Int): Int {
        val numStr = num.toString()
        return numStr.replaceFirst("6", "9").toInt()
    }

    /**
     * Alternative, try using only integers:
     *  - compute the amount of digits in the [num] by dividing it by 10 until it becomes 0;
     *  - repeatedly floor divide [num] by 10 starting from pow(10, (numOfDigits-1)), as soon as we encounter the
     *   6, return num + 3*pow(10,numOfDigits-i)
     *
     * Edge cases:
     *  - the only 6 is the last digit =>
     *
     * Time: always O(n)
     * Space: O(1)
     */
    fun onlyIntegers(num: Int): Int {
        val numOfDigits = num.getNumberOfDigits()
        var currentDigitDigger = 1
        repeat(numOfDigits - 1) { currentDigitDigger *= 10 } // only up to 2-3 times
        repeat(numOfDigits) {
            val currentDigit = (num / currentDigitDigger) % 10
            if (currentDigit == 6) return num + 3 * currentDigitDigger
            currentDigitDigger /= 10
        }
        return num
    }

    private fun Int.getNumberOfDigits(): Int {
        var currentNum = this
        var numOfDigits = 0
        while (currentNum > 0) {
            currentNum /= 10
            numOfDigits++
        }
        return numOfDigits
    }


    fun onlyIntegersSimpler(num: Int): Int {
        var highestSixPos = -1
        var currentNum = num
        var operationCounter = 0
        while (currentNum > 0) {
            val digit = currentNum % 10
            if (digit == 6) highestSixPos = operationCounter
            currentNum /= 10
            operationCounter++
        }
        return if (highestSixPos != -1) {
            var finalMultiplier = 1
            repeat(highestSixPos) { finalMultiplier *= 10 } // at most 3 times, better than pow+double conversion?
            num + 3 * finalMultiplier
        } else num
    }
}

fun main() {
    println(Maximum69Number().onlyIntegersSimpler(num = 9669))
}
