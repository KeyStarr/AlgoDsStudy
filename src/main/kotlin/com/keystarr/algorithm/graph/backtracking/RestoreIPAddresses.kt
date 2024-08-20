package com.keystarr.algorithm.graph.backtracking

/**
 * LC-93 https://leetcode.com/problems/restore-ip-addresses/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= input.length <= 20.
 *
 * Final notes:
 *  • 🎉 done [efficient] by myself in 45 mins;
 *   🔥 recognized that it is a backtracking problem in a few mins (generate all of something + but input.length < 20 completely gave it away).
 *   🏆 1st attempt submit, a problem with quite some edge cases.
 *  • there probably is a more clean way / maybe better time const then [efficient], I could optimize it;
 *  • postponed simplifying the solution / diving deeper into time/space, cause I got bigger priorities now, I doubt
 *   that'd be the most important problem I'd now face during interviews.
 *
 * Value gained:
 *  • practiced recognizing and solving a problem efficiently using backtracking.
 */
class RestoreIPAddresses {

    private lateinit var input: String
    private val results = mutableListOf<String>()
    private val dots = mutableListOf<Int>()

    // TODO: simplify the solution and understand the time/space complexity better
    //  try https://www.youtube.com/watch?v=61tN4YEdiTM

    /**
     * problem rephrase:
     *  given: a string that consists of digits only
     *  rules for generation:
     *   - 4 dots, each after a valid integer up to 255 inclusive, with no leading zeros;
     *   - all original digits and order must be preserved (cant reorder/remove/add).
     *  goal: generate and return all valid strings
     *
     * return all of something => try backtracking
     *
     * try iterating through the string, what choices can we make?
     *  if input\[i] == '0' && currentNumber.isEmpty() => we must put a dot right after it always, cause a valid number can't have leading zeros
     *  else =>
     *   choice:
     *    - put a dot right after the current digit
     *    - keep the digit and go to next digit
     *     ONLY available if next digit added to the current digits forms a number no greater than 255
     *
     *  base case:
     *   - we've all four dots => if the rest of the strings forms a valid number => add it to the collection, otherwise
     *    simply return.
     *
     * we have to make copies of the string => use StringBuilder
     *
     * Edge case:
     *  - no valid ip addresses can be generated, e.g. "00000" =>
     *  - max valid ip address length is XXX.XXX.XXX.XXX, so the input size would be for that 3*4=12
     *   => if [input].size > 12 return emptyList always, early return for O(1) time;
     *  - min valid ip address length is X.X.X.X => input must be at least 4 chars => if [input].size < 4 return emptyList always,
     *   early return for O(1) time.
     *
     * Time: O(2^n) but theres an even lower boundary
     *  - worst is we have no 0s, so at each digit we have 2 branches: place a dot, don't place a dot.
     *   BUT:
     *    - we can only place up to 3 dots total in one combination;
     *    - we can have up to 3 digits (and less than 255) until WE HAVE to place a dot.
     *   => O(2^n) but there's actually a lower bound asymptotically even cause of the 3 dots + 3 digits limitations.
     * Space: O(1) if we don't count the results (including the result string creation)
     *  - max depth is 3 digits * 4 => 12 => recursive callstack is O(12);
     *  - dots can grow up to O(3).
     * -------------------
     *
     * we could predict based on the input size how digits we must have of full 3 digits.
     */
    fun efficient(input: String): List<String> {
        if (input.length < 4 || input.length > 12) return emptyList()

        this.input = input
        backtracking(
            currentNumber = 0,
            currentInd = 0,
        )
        return results
    }

    private fun backtracking(
        currentNumber: Int,
        currentInd: Int,
    ) {
        if (currentInd == input.length) return

        if (dots.size == 3) {
            val digitsRemain = input.length - currentInd
            if (digitsRemain > 3) return
            if (input[currentInd] == '0' && digitsRemain != 1) return

            val lastNumber = input.substring(currentInd).toInt()
            if (lastNumber > 255) return

            val result = StringBuilder(input)
            dots.forEachIndexed { ind, dotInd -> result.insert(dotInd + ind, '.') }
            results.add(result.toString())
            return
        }

        val nextInd = currentInd + 1
        if (input[currentInd] == '0' && currentNumber == 0) {
            // a number can't start with a leading zero, and we have no number in the buffer => always place a dot (so the number is just '0')
            dots.add(nextInd)
            backtracking(
                currentNumber = 0,
                currentInd = nextInd,
            )
            dots.removeLast()
        } else {
            val newNumber = currentNumber * 10 + input[currentInd].digitToInt()
            if (newNumber > 255) return

            // try placing a dot
            dots.add(nextInd)
            backtracking(
                currentNumber = 0,
                currentInd = nextInd,
            )
            dots.removeLast()

            // skip a dot, try including the next digit into the current number
            backtracking(currentNumber = newNumber, currentInd = nextInd)
        }
    }
}

fun main() {
    println(
        RestoreIPAddresses().efficient(
            input = "101023",
        )
    )
}
