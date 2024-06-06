package com.keystarr.algorithm.hashing.hashmap

/**
 * LC-49 https://leetcode.com/problems/group-anagrams/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= strings.length <= 10^4;
 *  • 0 <= strings\[i].length <= 100;
 *  • strings\[i] is only lowercase English.
 *
 * Value gained:
 *  • recalled that Array.hashcode() in Kotlin/Java is done by reference, in Kotlin only `Array.contentHashcode()`
 *      computes hashcode of all elements;
 *  • practiced HashMap for a case when key is a collection;
 *  • practiced HashMap for counting in a case where counting isn't explicit in the problem statement;
 *  • apparently it's useful to implement some solutions in a more general way, if that doesn't give an asymptotic improvement,
 *      don't make const arrays/hardcoded string patterns. "more resistant to follow-ups" - interesting!
 *  • it seems that checking multiple anagrams efficiently is either:
 *      • sort both strings, they must be equal;
 *      • count all chars occurrences if these consist of a reasonably small set of characters, both arrays must equal.
 */
class GroupAnagrams {

    /**
     * Problem rephrasing:
     * "add all strings which are permutations of the same set of variables into a single list, return 2D List of all such
     * strings"
     *
     * If array.size=26 of all English lowercase letters where value is occurrence,
     * then strings s1 and s2 are anagrams if a1=a2.
     *
     * Idea:
     *  - allocate anagramsMap<Int, MutableList<String>>
     *  - iterate through all strings - O(n*m):
     *      - count all letters occurrences in strings[i], save it into occurArray - O(m), where m=strings[i].length;
     *      - compute hashcode from occurArray, if anagramsMap[hash] exists, then add current string there, else
     *          do anagramsMap[hash]=mutableListOf(strings[i]).
     *  - convert anagramsMap into a list.
     *
     * Edge cases:
     *  - strings\[i].length=0 => would be hashcode of IntArray with all 0's, works correctly;
     *  - strings.length=1 => works correctly outa the box.
     *
     * Time: O(n*m), where n=strings.length and m = average strings\[i];
     * Space: average/worst O(n) for hashmap.
     */
    fun efficient(strings: Array<String>): List<List<String>> {
        val anagramsMap = mutableMapOf<Int, MutableList<String>>()
        strings.forEach { string ->
            val occurrencesHash = string.lettersOccurrences().contentHashCode()
            if (anagramsMap.contains(occurrencesHash)) {
                anagramsMap[occurrencesHash]!!.add(string)
            } else {
                anagramsMap[occurrencesHash] = mutableListOf(string)
            }
        }
        return anagramsMap.values.toList()
    }

    private fun String.lettersOccurrences() = IntArray(26).also { occurrences ->
        forEach {
            val ind = it - 'a'
            occurrences[ind] = occurrences[ind] + 1
        }
    }

    /**
     * Since anagram is basically a permutation => all valid anagrams must be equal after sorting.
     *
     * Time: O(n*m*log(m))
     * Space: O(n)
     */
    fun efficientMoreGeneralizable(strings: Array<String>): List<List<String>> {
        val anagramsMap = mutableMapOf<String, MutableList<String>>()
        strings.forEach { string -> // O(n*m*log(m))
            val key = String(string.toCharArray().sortedArray()) // O(m*log(m))
            if (anagramsMap.contains(key)) anagramsMap[key]!!.add(string) else anagramsMap[key] = mutableListOf(string)
        }
        return anagramsMap.values.toList()
    }
}

fun main() {
    println(
        GroupAnagrams().efficient(
            arrayOf("", "a", "", "a")
        )
    )
}
