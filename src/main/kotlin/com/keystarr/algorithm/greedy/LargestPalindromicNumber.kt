package com.keystarr.algorithm.greedy

/**
 * LC-2384 https://leetcode.com/problems/largest-palindromic-number/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= digits.length <= 10^5;
 *  • digits consists only of 0-9 chars.
 *
 * Final notes:
 *  • done [solution] by myself in 25 mins;
 *  • ⚠️ at first, interpreted the problem's goal wrong went to solve the wrong problem!
 *  • ⚠️ failed 2 submissions:
 *   • 1st: missed the edge case with leading zeroes, even though it was clearly stated in the problem statement ⚠️ (skimmed over it);
 *   • 2nd: did a poor string building premature optimization and failed to not add the oddSlot when it was null.
 *  • 🔥 finally, understood the goal correctly and made all core design choices correctly. Observed both that we can
 *   simply build the half of the palindrome and at the same time fill the odd slot, both build the half prioritizing the
 *   largest digits available at each step, and find the largest digit for the odd slot (greedy);
 *  • 💡 I got the half + "odd slot" tricks instantly thanks to that I've already solved a similar problem on best palindrome generation,
 *   only I think it asked for the palindrome's max length or smth. Anyway, practice on the topic helps to tackle harder problems 100%!
 *
 * Value gained:
 *  • recognized and solved "the best palindrome string generation" type problem efficiently using greedy;
 *  • READ THE PROBLEM STATEMENT! Cause I've seen a similar problem before - half-skimmed the statement and rushed to the
 *   solution. ⚠️ again and again I fall into the same trap - please, treat the problem more phenomenologically!!!
 */
class LargestPalindromicNumber {

    // TODO: done well, but could use more confidence for this type problem's edge cases - retry in 1-2 weeks

    /**
     * goal: return the LARGEST palindromic INTEGER formed from [digits]
     *  no leading zeroes!
     *
     * observations:
     *  1. since digits.length > 0, then there is always a valid answer with at least a single digit used;
     *  2. a palindrome always requires to have all digits duplicated, except for the middle one for an odd palindrome
     *   => we could base off that.
     *
     * approach:
     *  - count frequencies of all digits in [digits];
     *  - iterate through frequencies right-to-left:
     *   - if frequency is even => add freq/2 digits equal to key to the palindromeHalf StringBuilder;
     *    (take the largest digits as early as possible)
     *   - if frequency is odd, save the digit into the "oddSlot" variable.
     *    (take the biggest integer available as the odd slot)
     *  - return the palindromeHalf + (oddSlot ?: "") + palindromeHalf.reversed()
     *
     * basically we build the half of the palindrome using as many digits as we can, then building the entire palindrome
     *  out of the half and its reversed copy plus an optional single odd frequency digit.
     *
     * since [digits] consists of only chars 0-9 use a simple int array for frequency counting for better time/space const (than the hashmap)
     *
     * edge cases:
     *  - as we reach the frequency of '0' no digits had frequency > 0, and '0' frequency is > 0 => never put leading zeros,
     *   simply discard these then;
     *   when can we use zeroes in the palindrome? If zero count is odd we can use zero as an oddSlot, also if we have
     *   at least one any digit with freq > 1, then we can use a zero after it in the palindrome half;
     *  - zeroes count is even and there are no other digits present => always return '0' =>
     *
     * Time: always O(n)
     *  - counting O(n);
     *  - generating palindromeHalf O(n) since we have up to O(k), where k=distinct digits count iterations.
     *   Across all these iterations we add up to n/2 chars to palindromeHalf.
     *  - build the final palindrome is O(n).
     * Space: always O(n+k) = O(n) if we don't count the result
     *  - frequencies takes O(k) space, here worst k=10 so O(1);
     *  - palindromeHalf takes worst n/2 space;
     *  - in the end we effectively create up to 3 strings: one is palindromeHalf + oddSlot if it exists, then the reverse half
     *   creation, then half + reversedHalf. Anyway, that's O(n) space.
     *
     * ------------------------ optimization
     *
     * asymptotically we can't optimize, since we have to check all elements in [digits] in any case to estimate our resources
     *  that's always no less O(n) time, and result takes worst n space so that's always O(n) space.
     *
     * we could try to reduce the time const, say, if we'd instead of reversing and build the string out of the half would
     *  be the reversed half on the fly, but is it really worth it now? I don't think so
     *  (we could create another string builder and iterate from the start instead of from the end doing the same logic,
     *   only without the odd slot)
     */
    fun efficient(digits: String): String {
        val frequencies = IntArray(size = 10)
        digits.forEach { digit ->
            val key = digit - '0'
            frequencies[key] = frequencies[key] + 1
        }

        val palindromeHalf = StringBuilder()
        var oddSlot: Char? = null
        for (digitKey in frequencies.lastIndex downTo 0) {
            val frequency = frequencies[digitKey]
            val digit = '0' + digitKey
            if (digit != '0' || palindromeHalf.isNotEmpty()) repeat(frequency / 2) { palindromeHalf.append(digit) }
            if (oddSlot == null && frequency % 2 != 0) oddSlot = digit
        }

        return if (palindromeHalf.isEmpty() && oddSlot == null && frequencies[0] > 0) {
            "0"
        } else palindromeHalf.toString() + (oddSlot ?: "") + palindromeHalf.reverse().toString()
    }

    /**
     * Couldn't hold myself and optimized the const for [efficient], thanks for the inspiration to
     *  https://leetcode.com/problems/largest-palindromic-number/solutions/2456626/cpp-java-greedy-o-n-space-o-n-time/
     *
     * ----------
     *
     * Basically its much simpler than I initially presumed - just add odd slot and then trivially iterate right-to-left (respecting the odd slot)
     *  on the StringBuilder itself and append to its end! We can do that since its no iterator and we only add to end
     *  => not breaking the consistency, not modifying the initial range.
     *
     * Asymptotic complexity is same as [efficient].
     */
    fun efficientCleaner(digits: String): String {
        val frequencies = IntArray(size = 10)
        digits.forEach { digit ->
            val key = digit - '0'
            frequencies[key] = frequencies[key] + 1
        }

        // build the palindrome half and fill the odd slot with the number if such exists
        val result = StringBuilder()
        var oddSlot: Char? = null
        for (digitKey in frequencies.lastIndex downTo 0) {
            val frequency = frequencies[digitKey]
            val digit = '0' + digitKey
            if (digit != '0' || result.isNotEmpty()) repeat(frequency / 2) { result.append(digit) }
            if (oddSlot == null && frequency % 2 != 0) oddSlot = digit
        }

        // build the entire palindrome - add oddSlot and the second half
        oddSlot?.let { result.append(oddSlot) }
        val downFrom = result.lastIndex  - (if (oddSlot == null) 0 else 1)
        for (i in downFrom downTo 0) result.append(result[i])

        // condition for the edge case when there is only an even count of 0's and no other digits
        return if (result.isEmpty() && frequencies[0] > 0) "0" else result.toString()
    }
}

fun main() {
    println(
        LargestPalindromicNumber().efficientCleaner(
            digits = "6006",
        )
    )
}
