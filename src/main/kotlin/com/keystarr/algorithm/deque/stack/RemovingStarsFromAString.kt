package com.keystarr.algorithm.deque.stack

/**
 * LC-2390 https://leetcode.com/problems/removing-stars-from-a-string/description/
 * difficulty: medium (imho, leet-easy - no advanced DSA neither complex observations/edge cases)
 * constraints;
 *  • 1 <= text.length <= 10^5;
 *  • text consists of lowercase English and *.
 *
 * Final notes:
 *  • done [solution] by myself in ~5 mins;
 *  • core observation was that: we only need to remove the last non-removed character prior to the *
 *   then simple conclusion => if we iterate through the string and add all non-* chars into a stack, as we encounter *
 *   we might simply pop from the stack.
 *   It was very trivial to make, somehow. Done it almost instinctively, can't really retrace the process, jumped to it straight.
 *
 * Value gained:
 *  • practiced solving a string modification problem efficiently using a stack.
 */
class RemovingStarsFromAString {

    /**
     *
     * approach:
     *  - we can simply iterate through the text, adding each char that's not an * as-is;
     *  - as we encounter *, we might simply remove the last added character from the StringBuilder, if there are any.
     *   Removal from end is O(1), cause internally it uses an array and only moves the end pointer in that case;
     * that's it. We might grow the space larger than we'd grow it if we'd add only chars that would be actually in the
     *  final result, but that's impacting only a const, space would grow only up to n-starsCount anyway, so its O(n),
     *  and only if we really count the result space.
     *
     * UPD: we use StringBuilder as a stack here, really (add to /remove from last)
     *
     * Edge cases:
     *  - we encounter an * but we have no chars left in the builder => can we remove only the * itself and no char left to it,
     *   if there are none? AH, "input is generated such that the operation is ALWAYS possible", ok, such a case will never be present.
     *
     * Time: always O(n)
     *  - we always visit each [text] char exactly once;
     *  - add to StringBuilder is amortized O(1);
     *  - remove last from StringBuilder is O(1).
     * Space: average/worst O(n) if we consider result space, otherwise O(1)
     */
    fun solution(text: String): String = StringBuilder().apply {
        text.forEach { char ->
            if (char == '*') deleteAt(length - 1) else append(char)
        }
    }.toString()
}
