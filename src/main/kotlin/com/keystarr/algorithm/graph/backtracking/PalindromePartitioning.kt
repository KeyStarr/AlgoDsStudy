package com.keystarr.algorithm.graph.backtracking

/**
 * LC-131 https://leetcode.com/problems/palindrome-partitioning/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= text.length <= 16;
 *  • text contains only lowercase English.
 *
 * Final notes:
 *  • 🏅 done [backtracking] by myself in 35 mins;
 *  • ⚠️only at the 17th min mark realized that we need palindrome PARTITIONING not GENERATION what exactly that implied :DD
 *   before that defaulted to the recently solved palindrome generations problems reasoning cache in my head, automatically)));
 *  • ⚠️⚠️ couldn't estimate space under 1h total time, failed to come up with a way to optimize the palindrome checking too;
 *  • ❓so weird, I've solved the question by myself, checked most popular solutions (Neet and a few community ones), these match,
 *   yet I feel this distinct uncertainty like I don't understand it fully. Maybe that's because almost no one I've checked estimated
 *   time&space properly? Yes, and also cause I feel like palindrome checking could be possibly improved and maybe that mutable string
 *   deep copy could be done better. But its good enough right now, let's accept that uncertainty - I understand the core algorithm
 *   and that's good enough;
 *  • 🏆 good thing I've forced myself to implement the palindrome checking just for O(n) time straight-forward and finished
 *   the algo first before trying to optimize this sub-solution. Turns out that seems to be the most efficient way to do this!
 *   (at least I've found no such, but I havent checked ALL top solutions)
 *   => 💡 indeed, apparently by default going for straight-forward suboptimal ESPECIALLY for subproblems solutions may
 *    be good enough.
 *
 * Value gained:
 *  • practiced solving an "all valid substring combinations" type problem using backtracking and a StringBuilder;
 *  • ⭐️💡 reinforced that I should approach each problem phenomenologically!! I do indeed from time to time fall into that
 *   nasty trap of skimming the problem but actually solving some other reminiscent problem I've recently solved instead
 *   of the current one.
 */
class PalindromePartitioning {

    // TODO: did good enough, but still for more confidence => resolve in 2-3 weeks

    // TODO: recheck deeper understand time&space complexity

    /**
     * AH, its not just the palindrome GENERATION, but PARTITIONING!!!!
     *
     * We need to distribute all characters of [text] such that each character is at least in one string of the current
     *  "partitioning" and all such strings are valid palindromes:
     *  - start at the 0th ind and end too at the 0th, and if its a valid palindrome => call same function
     *   recursively and try to find the next valid palindrome in text[endInd+1:] if any exists, then again;
     *  - after all possible valid partitionings starting with the string text[0] have been checked => expand the first substring,
     *   add one more char to it and check all valid partitionings again, but only if it itself is a valid palindrome, otherwise
     *   go straight to the next expansion;
     *  - if we've received a call with startInd == text.length => we've distributed all chars of [text] such that all strings
     *   in the current partitionings are valid palindrome (otherwise we would have terminated early and wouldn't reach current index)
     *   => make a deep copy of the current partitioning and add it to the results.
     *
     * So pruning is that we don't try to partition the string further if the current substring is not a valid palindrome
     *  (we need all of substrings to be => trivially no further partitioning then will fix that current invalid substring)
     *
     *
     * Edge cases:
     *  - text.size == 1 => always return [[text[0]]] => correct as-is.
     *
     * Time: O(n^n * n)
     *  worst - each substring of [text] is a valid palindrome
     *  - at the first startInd==endInd==0 we have exactly n valid substrings (if all chars in the [text] are equal)
     *   => each then has (length-startInd) options to try as the second substring etc, so basically ~n;
     *  - each combination we check whether its a palindrome or not.
     *
     *  i.o. the tree has n width and n height, and each node takes ~n work to check whether its a palindrome or not.
     *   the real number of operations would be less, since there would be always less than n^n nodes, but it depends on it
     *   so that's a legit close upper boundary.
     *
     * Space: O(n^n)
     *  worst all substrings are valid palindromes => since we have ~n^n nodes in the tree, and we have to store
     *  all nodes of each branch in the list => all nodes across all result lists is about all nodes size
     */
    fun backtracking(text: String): List<List<String>> = mutableListOf<List<String>>().apply {
        backtrackingRecursive(
            startInd = 0,
            text = text,
            currentPartitioning = mutableListOf(),
            results = this,
        )
    }

    private fun backtrackingRecursive(
        startInd: Int,
        text: String,
        currentPartitioning: MutableList<StringBuilder>,
        results: MutableList<List<String>>,
    ) {
        if (startInd == text.length) {
            results.add(currentPartitioning.map { it.toString() })
            return
        }

        val substring = StringBuilder()
        for (endInd in startInd until text.length) {
            substring.append(text[endInd])
            if (!substring.isPalindrome()) continue

            currentPartitioning.add(substring)
            backtrackingRecursive(startInd = endInd + 1, text, currentPartitioning, results)
            currentPartitioning.removeLast()
        }
    }

    // TODO: can we optimize that? based on our palindrome creation context of always simply adding "one more letter"
    private fun StringBuilder.isPalindrome(): Boolean {
        var left = 0
        var right = lastIndex
        while (left < right) {
            if (get(left) != get(right)) return false
            left++
            right--
        }
        return true
    }

    /**
     * WRONG, solved the wrong problem o_O kinda missed to render the part that we have to partition the ENTIRE string
     *
     * ---------------
     *
     *
     * return all of something => consider backtracking? at least try. and given n <= 16 especially
     *
     * observation: any non-empty string has always at least one palindrome partitioning of all substrings with length == 1
     *
     * try to prune based on the palindrome generation concept of counting frequencies and generating just the half of the palindrome?
     *  NO we don't do palindromes as subsets/subsequences but SUBSTRINGS, so these gotta be continuous!
     *
     * why cant we just do the n^2 solution, try each char as the starting point at find all palindromes when i is the first char
     *  of the substring? and determine whether its a palindrome based on the frequencies count?
     *
     * (wrong) n^2 * k technically, which should be faster than the usual 2^x for backtracking, no?
     *
     * ah, we can't check for palindrome based on frequencies since, once again, consecutive order matters! abbaab technically
     * all frequencies match but thats not a palindrome (oh that context of another similar problem in my head where it mattered!)
     * => we'd check if thats a palindrome based on 2 pointers? but if we'd run it on each palindrome that would be n^3
     *
     *
     * how to check whether the current string is palindrome efficiently given our generation context? is it possible to O(1)
     *  instead of O(n) for each candidate?
     */
    fun solution(text: String): List<List<String>> {
        val results = mutableListOf<List<String>>()
        for (startInd in text.indices) {
            for (endInd in startInd until text.length) {
//                if (text.isPalindrome(startInd, endInd)) results.add(text.substringAsList(startInd, endInd + 1))
            }
        }
        return results
    }

    private fun String.substringAsList(startInd: Int, endInd: Int): List<String> {
        TODO()
    }

    private fun Char.toKey() = this - 'a'
}

fun main(){
    println(
        PalindromePartitioning().backtracking("aaaa")
    )
}
