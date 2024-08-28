package com.keystarr.algorithm.hashing.hashmap.string

/**
 * ⭐️ a very good counting string permutation problem example
 * LC-791 https://leetcode.com/problems/custom-sort-string/description/
 * difficulty: medium
 * constraints:
 *  • order and text contain only lowercase English;
 *  • 1 <= order.length <= 26;
 *  • 1 <= text.length <= 200;
 *  • all characters of order are unique.
 *
 * Final notes:
 *  • 🏆 done [efficient] by myself in ~12 mins;
 *  • I started reasoning from the order => generation based on counting was my first though. Guess I've done a similar
 *   solution sometime in the past, so I though quickly of it, perhaps it was in the back of my mind.
 *
 * Value gained:
 *  • practiced solving a valid string permutation type problem using counting.
 */
class CustomSortString {

    // TODO: try to do a bucket sort solution after learning the concept

    /**
     * problem rephrase:
     *  - given:
     *   - order: String
     *   - text: String
     *  Goal: find the permutation of [text] such that all character there appear in order defined as order, return it.
     *   if some char of [text] doesn't appear in order => it can take any position as long as other chars positions are in order.
     *
     * approach:
     *  - associate each character of order to its position in order, integer value -> CharArray where ind is position and
     *   value is the character, if that order value isn't defined mark it as a special char;
     *  - iterate through text, build a map char -> occurrenceCount;
     *  - iterate through charToOrder array:
     *   - if text charToOccurrence map contains the character => append the occurrence value of chars to the result string
     *    and set occurrence to 0;
     *   - else skip;
     *  - iterate through charToOccurrence entries, for each entry that isnt 0 append chars to result (chars with no defined order);
     *  - return builder.toString
     * => actually we don't need to associate anything for order, since [order] already defines char->ind as its order position
     *  association.
     *
     * No real edge cases.
     *
     * Time: always O(n+m), since worst m=26 = O(n)
     *  - counting frequency of chars in [text] is O(n), where n=text.length;
     *  - appending all existing characters defined by [order] is:
     *   - always m=order.length iterations, worst m=26;
     *   - `repeat` appending worst cost across all outer iterations is when each existing character is defined in [order]
     *    => we append exactly n characters;
     *   => average/worst O(m+n)
     *  - last loop has k=number of distinct chars in [text] iterations, and worst appending is when none of the existing
     *   characters in [text] are contained in [order] => n appends => average/worst O(n);
     * Space: always O(n)
     *  - word char to freq map worst is when all chars in text are distinct => O(n);
     *  - result is always of length n, but we don't usually count it.
     */
    fun efficient(order: String, text: String): String {
        val charToFreqMap = mutableMapOf<Char, Int>()
        text.forEach { char -> charToFreqMap[char] = charToFreqMap.getOrDefault(char, 0) + 1 }

        val result = StringBuilder()
        order.forEach { targetChar ->
            val freq = charToFreqMap[targetChar] ?: return@forEach
            repeat(freq) { result.append(targetChar) }
            charToFreqMap[targetChar] = 0
        }

        charToFreqMap.entries.forEach { entry ->
            val (char, freq) = entry
            repeat(freq) { result.append(char) }
        }

        return result.toString()
    }
}
