package com.keystarr.algorithm.hashing.hashmap

/**
 * LC-451 https://leetcode.com/problems/sort-characters-by-frequency/description/
 * difficulty: medium (medium only for efficient, nlogn is leet-easy)
 * constraints:
 *  • 1 <= text.length <= 5 * 10^5;
 *  • text consists of English uppercase/lowercase and digits.
 *
 * Final notes:
 *  • done [suboptimalArray] by myself in 12 mins;
 *  • ⚠️ failed 1 test case for 2nd order sorting, but, imho, the problem wasn't clear on this with "if multiple answers,
 *   return in any order" and "sort only by desc freq";
 *  • 💡 quite a lot of counting type problems which are efficiently solved only using counting/bucket sort, huh. Then
 *   it's very valuable to master these tools as well.
 *
 * Value gained:
 *  • practiced both map and array counting approaches, [suboptimalMap] map is much better here imho, cleaner and better times/space const.
 */
class SortCharactersByFrequency {

    // TODO: solve efficiently for O(n) time via bucket sort, after learning it

    /**
     * goal: sort [text] in decreasing order based on the frequency of characters.
     *
     * approach:
     *  1. count the character frequency;
     *   - either use a hashmap;
     *   - or, since we have at most 62 unique characters, just an IntArray for counting, for better const (same asymptotic though).
     *  2. sort with comparator based on frequency.
     *
     * if chars X and Y have same frequency > 1, are we allowed to mix them in any order? e.g. X='a', Y='b' freq=2
     *  => is it okay to return "..abab.."?
     *
     * Apparently not, but ITS NOT stated in the problem that we must stick same characters together if there are multiple
     * equal frequencies of different distinct characters! Its stated in ANY order!
     * => anyway, if freq is the same, sort the chars alphabetically then (2nd level order of sorting).
     *
     * edge cases:
     *  - same frequency just stick together.
     *
     * Time: always O(nlogn)
     *  - counting is O(n);
     *  - sorting: convert to mutable list O(n), sort O(nlogn), convert into String O(n).
     * Space: always O(n)
     *  - temp mutable list of size n, result string of size n, sorting temp space logn.
     */
    fun suboptimalArray(text: String): String {
        val freq = IntArray(size = 62) // lowercase English is 0-25, uppercase English 26-51, digits 52-61.
        text.forEach { char -> freq[char.toKey()]++ }
        return text
            .toMutableList()
            .apply {
                sortWith { c1, c2 ->
                    val freqDiff = freq[c2.toKey()] - freq[c1.toKey()]
                    if (freqDiff == 0) c1 - c2 else freqDiff
                }
            }
            .joinToString(separator = "")
    }

    // lowercase English is 0-25, uppercase English 26-51, digits 52-61.
    private fun Char.toKey() = when (this) {
        in 'a'..'z' -> code - 'a'.code
        in 'A'..'Z' -> 26 + (code - 'A'.code)
        else -> 52 + (code - '0'.code)
    }

    /**
     * 1. count via a hashmap, not an array => to later easier sort entries;
     * 2. we're asked for sorting chars based on descending freq => sort the counters descending => we get the order of
     *  characters based on their descending frequency;
     * 3. iterate through sorted counters and simply construct add `count` corresponding chars into the string!
     *
     * bayeyaby
     *
     * y: 3
     * a : 2
     * b : 2
     * e : 1
     *
     * yyyaabbe
     *
     * Time: average/worst O(nlogn)
     *  - counting O(n);
     *  - sorting is O(klogk) where k=amount of distinct chars in [text], but k worst = n, so average/worst O(nlogn);
     *  - building is O(n), since we have always exactly n inner loop iterations, since we add each original char to the result string.
     * Space: average/worst O(n), if counting result then always O(n)
     */
    fun suboptimalMap(text: String): String {
        val charToFreqMap = mutableMapOf<Char, Int>()
        text.forEach { char -> charToFreqMap[char] = charToFreqMap.getOrDefault(char, 0) + 1 }

        val sortedEntries = charToFreqMap.entries.sortedByDescending { it.value }
        val builder = StringBuilder()
        sortedEntries.forEach { entry ->
            val (char, freq) = entry
            repeat(freq) { builder.append(char) }
        }
        return builder.toString()
    }
}

fun main(){
    println(
        SortCharactersByFrequency().suboptimalMap("bayeyaby")
    )
}
