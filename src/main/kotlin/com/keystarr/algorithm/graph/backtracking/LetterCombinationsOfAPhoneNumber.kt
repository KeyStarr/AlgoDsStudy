package com.keystarr.algorithm.graph.backtracking

/**
 * LC-17 https://leetcode.com/problems/letter-combinations-of-a-phone-number/description/
 * difficulty: medium
 * constraints:
 *  • 0 <= digits.length <= 4
 *  • digits\[i] is a digit in the range ['2','9']
 *
 * Final notes:
 *  • done both [bfs] and [dfs] by myself;
 *  • time estimation once again for backtracking is really uncomfortable for me rn.
 *
 * Value gained:
 *  • yep the backtracking pattern really works, the only parts changed are pruning and the result add condition;
 *  • practiced backtracking on a tree.
 */
class LetterCombinationsOfAPhoneNumber {

    private val digitToLettersMap = mapOf(
        '2' to arrayOf('a', 'b', 'c'),
        '3' to arrayOf('d', 'e', 'f'),
        '4' to arrayOf('g', 'h', 'i'),
        '5' to arrayOf('j', 'k', 'l'),
        '6' to arrayOf('m', 'n', 'o'),
        '7' to arrayOf('p', 'q', 'r', 's'),
        '8' to arrayOf('t', 'u', 'v'),
        '9' to arrayOf('w', 'x', 'y', 'z'),
    )

    /**
     * problem rephrase:
     *  - given is a string of arbitrary length which contains only digits from 2 to 9;
     *  - each digit is mapped to different letters, even different amounts of letters;
     * goal: return all possible combinations of letters.
     *
     * naive iterative solution => BFS:
     * - create a hashmap digit->letters
     * - create intermediateResultsQueue: Queue<List<Char>>
     * - intermediateResults.add(emptyList())
     * - iterate through [digits]:
     *  - currentResults = intermediateResultsQueue.size
     *  - while currentResults > 0:
     *      - str = currentResults.pop()
     *      - iterate through map\[currentDigit]:
     *       - newStr = ArrayList(str)
     *       - newStr.add(currentLetter)
     *       - intermediateResultsQueue.add(newStr)
     * - return intermediateResultsQueue.map { it }
     *
     * Edge cases:
     *  - digits.size == 0 => expected result is an emptyList, but we return a list with an empty string => early return emptyList;
     *
     * Time: O(n*n^k), here worst k=4, basically a const, so O(n^2)
     *  - at most one digit can map to 4 letters => worst total amount of combinations is n^4, where n=digits.size
     *   (in general n^k, where k= the amount of letters for a digit)
     *  - typical BFS takes O(nodes+edges) time
     *  - here worst nodes=n^4, edges=n^4, so generally BFS takes O(n^k) time
     *  - for each node visit we copy the intermediateStr, which length differs but depends on n => each node visit takes O(n) time
     * Space: O(m) if we don't include the result, m=the max amount of nodes at a single level, for the queue.
     */
    fun bfs(digits: String): List<String> {
        if (digits.isEmpty()) return emptyList()

        val resultsQueue = ArrayDeque<String>().apply { add("") }
        digits.forEach { currentDigit ->
            val previousLevelSize = resultsQueue.size
            val currentLetters = digitToLettersMap.getValue(currentDigit)
            repeat(previousLevelSize) {
                val intermediateResult = resultsQueue.removeFirst()
                currentLetters.forEach { letter -> resultsQueue.add(intermediateResult + letter) }
            }
        }
        return resultsQueue.map { it }
    }

    /**
     * Classic dfs backtracking with:
     *  - pruning: for each node go to all letters available, represented by the next digit and stop when we used all digits in the input;
     *  - add the string to results when the desired length is reached;
     *  - no "undo" operation required for backtracking, cause when we add the string to the result all letters would be
     *   overwritten by the right ones.
     *
     * Time: ?? TODO: estimate precisely
     * Space:
     */
    fun dfs(digits: String): List<String> = mutableListOf<String>().apply {
        if (digits.isEmpty()) return this
        backtrack(
            current = CharArray(size = digits.length),
            currentInd = 0,
            digits = digits,
            results = this,
        )
    }

    private fun backtrack(
        current: CharArray,
        currentInd: Int,
        digits: String,
        results: MutableList<String>,
    ) {
        if (currentInd == current.size) {
            results.add(String(current))
            return
        }

        val letters = digitToLettersMap.getValue(digits[currentInd])
        for (letter in letters) {
            current[currentInd] = letter
            backtrack(
                current = current,
                currentInd = currentInd + 1,
                digits = digits,
                results = results,
            )
        }
    }
}

fun main() {
    println(LetterCombinationsOfAPhoneNumber().dfs("23"))
}
