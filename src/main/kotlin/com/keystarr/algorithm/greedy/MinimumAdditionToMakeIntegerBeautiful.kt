package com.keystarr.algorithm.greedy

/**
 * LC-2457 https://leetcode.com/problems/minimum-addition-to-make-integer-beautiful/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= base <= 10^12;
 *  • 1 <= targetDigitsSum <= 150;
 *  • the input is such that the answer always exists.
 *
 * Final notes:
 *  • ⚠️⚠️ done by myself in 50 mins with 3 (!!) failed submissions ⚠️:
 *   • identified to try greedy straight away, tried to come up with the rule;
 *   • sketched a design, headed to implementation => design had critical flaws, fixed them on the go;
 *   • ⚠️ failed 1st: missed that we could have a number with multiple 9 in a row, say 1999, then when we do +1 we have
 *    to pass the 1 to the higher order digits through all consecutive 9's;
 *   • ⚠️ failed 3rd: forgot to make some variable Long ('power' I think) => overflow.
 *
 *  • 🏆 I figured the greedy rule by myself under 1h, even somewhat proved it by reasoning as to how exactly can we
 *   reduce the digits sum and went from there, great first basis reasoning! Even though I felt lost at the start,
 *   with what to begin with, totally didn't see the full solution straight away even the major parts of it 🔥
 *
 *  • ⚠️ I thought straight away to work with the list of digits. That turns out to be the best time complexity (slightly, by logn), but
 *   is a lot more complicated than [lessSpaceAndCleanButSlower] => how could have I went for a simpler solution first?
 *   a lot of folks in the solutions section it seems went for it straight away, why not I? O(log(n)^2) is totally adequate here
 *   and we could've transformed it into [fasterButComplex] if needs be (probably wouldn't need to even on an interview!)
 *   [lessSpaceAndCleanButSlower] would be a much better stepping stone to begin with.
 *
 * Value gained:
 *  • practiced solving "the best minimum number transformation" type problem using greedy and light arithmetics;
 *  • 🔥 got another case in favor of "go for the promising to be on the path to optimal, but maybe suboptimal on itself
 *   yet clean and simple solution rather than the optimal one straight away!". KISS, bro.
 *
 *   ⚠️ though I still not sure exactly what action point to draw from here, I haven't even considered the [lessSpaceAndCleanButSlower]
 *    at any point.
 */
class MinimumAdditionToMakeIntegerBeautiful {

    // TODO: full resolve in 2-3 weeks, try to simplify this time first (but with the optimal core idea)

    /**
     * problem rephrase:
     *  given:
     *   - base: Int
     *   - maxDigitsSum: Int
     *   - the input is such that an answer is always possible
     *
     *  goal: find the best valid integer
     *   valid:
     *    - the sum of its digits is at most [maxDigitsSum]
     *    - its built by adding some non-negative number to base.
     *   best = minimum possible
     *
     *
     * base=467  tS=6
     * 467+33=500
     *
     * aside from the edge case => `base` digits sum is ALWAYS greater than `maxDigitsSum` => we need to reduce it,
     *  and with the minimum amount of steps + we must reduce it by increasing the number
     *
     * when is the digits sum reduced? 399 (21) + 1 = 400 (4)
     *  when the digit overflows, transfers 1 to the next order digit and becomes a 0.
     *
     * since its guaranteed answer is always possible, then its always possible to at least add to the base such that
     *  all digits but the fist one are 0's (but it may not be the best option)
     *
     * basically we can try to calculate just how many minimum digits we need to turn into zeroes, taking into account
     * that higher order digits are increased by 1 then
     *
     * if we turned one digit into a 0 (and thus increased the next higher order digit by 1) and still the number isn't valid
     *  => the only way to reduce the number is to turn the next order digit into a 0. Since if we increase from the current position
     *   we'd only increase the minimum order digit and its already the min possible (0), we won't be able to reduce then
     *
     * approach:
     *  - compute the initial base digits sum;
     *   (edge: if its valid => early return 0)
     *  -
     *
     *
     * edge cases:
     *  - base is valid => return 0 (constraint was non-negative, so correct)
     *
     * b=381  tS=4
     * expected=29
     *
     * +9
     * 390
     *
     * b=190 ts=1
     * expected=810
     *
     * addition=0
     * currentSum=10
     *
     * 0: skip
     *
     * 1:
     * addition += (10-9)*10=10
     * currentSum += 10-9+1=2
     * (200)
     *
     * 2:
     * addition += (10-2)*100=800
     * currentSum += 2 -
     *
     * ----
     *
     * it got to be greedy
     *
     * greedy rule: go through digits of [base] starting from the lowest
     *  and each step increase the [base] such that the current digit becomes 0 while all before it remain 0.
     *
     * Time: average/worst O(logn)
     *  - main loop has up to m iterations;
     *  - inner loop at worst across all outer iterations has up to m iterations.
     * Space: always O(logn)
     */
    fun fasterButComplex(base: Long, maxDigitsSum: Int): Long {
        val digits = base.toDigitsSafe()
        var currentSum = digits.sum()
        if (currentSum <= maxDigitsSum) return 0

        var addition = 0L
        var power = 1L
        for (i in digits.lastIndex downTo 0) {
            val currentDigit = digits[i]
            if (currentDigit != 0L) {
                if (digits[i - 1] == 9L) {
                    var j = i - 1
                    while (digits[j] == 9L) {
                        digits[j] = 0
                        currentSum -= 9
                        j--
                    }
                    digits[j] = digits[j] + 1
                    currentSum += 1
                } else {
                    digits[i - 1] = digits[i - 1] + 1
                    currentSum += 1
                }
                digits[i] = 0
                currentSum -= currentDigit
                addition += (10 - currentDigit) * power
                if (currentSum <= maxDigitsSum) return addition
            }
            power *= 10
        }
        throw IllegalStateException("The answer is guaranteed to exist")
    }

    private fun Long.toDigitsSafe(): MutableList<Long> {
        val digits = mutableListOf<Long>()
        var left = this
        while (left > 0) {
            digits.add(left % 10)
            left /= 10
        }
        digits.add(0)
        return digits.apply { reverse() }
    }

    /**
     * Do same core idea as [fasterButComplex] but way simpler - don't store digits and update the digit sum in-place, simply
     *  modify the number and re-compute the digits sum every step.
     *
     * But the greedy rule remains the same, just different general implementation details.
     *
     * 200000 t=2
     *
     * Edge case:
     *  - base is already valid => we never enter loop's body => number-base==0, correct.
     *
     * Time: O(log(n)^2)
     *  - digits sum computation takes O(log10(n)) time, where n=base;
     *  - worst main loop has log10(n) iterations, work at each iteration is O(log10(n)) to check the precondition.
     * Space: always O(1)
     *
     * --------------
     *
     * Discovered thanks to:
     * https://leetcode.com/problems/minimum-addition-to-make-integer-beautiful/solutions/2757973/eat-from-right/
     *
     * could be even more simplified
     */
    fun lessSpaceAndCleanButSlower(base: Long, maxDigitsSum: Int): Long {
        var number = base
        var power = 1L
        while (number.digitsSum() > maxDigitsSum) {
            val digit = (number / power) % 10
            number += (10 - digit) * power
            power *= 10
        }
        return number - base
    }

    private fun Long.digitsSum(): Long {
        var left = this
        var sum = 0L
        while (left > 0) {
            sum += left % 10
            left /= 10
        }
        return sum
    }
}

fun main() {
    println(
        MinimumAdditionToMakeIntegerBeautiful().fasterButComplex(
            base = 19,
            maxDigitsSum = 1,
        )
    )
}
