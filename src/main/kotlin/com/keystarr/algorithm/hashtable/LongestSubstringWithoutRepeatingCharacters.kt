package com.keystarr.algorithm.hashtable

import java.util.LinkedList

/**
 * LC-3 https://leetcode.com/problems/longest-substring-without-repeating-characters/description/
 * difficulty: medium
 * constraints:
 *  • 0 <= input.length <= 5*10^4;
 *  • input of English letters, digits, symbols and spaces;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • this problem is new worst failed submissions record of mine.. went in tired? lazy? didn't dry run the code,
 *      didn't properly brainstorm key cases => rushed right in and iterated over a several incredibly stupid ideas quickly;
 *  • done [crazy] by myself after 5 (!!!!) failed submissions. The idea is right, but the implementation is horrific,
 *      simply because instead of actually adding/deleting the characters from a linked list I could've simply used a
 *      map with most recent indices like in [efficient]!
 *  • a wonderful and humbling lesson though - maybe sliding window problems on finding the best valid subarray
 *      do, after all, require incrementing the leftInd one-by-one! funny, it's been presented that the reason why we
 *      usually shrink the window by 1 index is cause the array might become "valid" after a single such change, but
 *      in this particular problem we 100% the moment we encounter a duplicate character that we have to shrink the window
 *      to leftInd=mostRecentInd+1; but we still shrink it by incrementing leftInd, though here for the purposes of
 *      removing all obsolete characters from the window in between leftInd and mostRecentInd+1.
 *
 * Value gained:
 *  • even if I'm tired try to dry-run AT LEAST the mediums??? and try to REALLY think of ALL key major cases;
 *  • practiced sliding window for finding the best valid subarray.
 */
class LongestSubstringWithoutRepeatingCharacters {

    /**
     * Problem rephrased:
     * "Find the longest substring with distinct chars only".
     *
     * Hint: best valid subarray + "distinct"="occurrence at most 1" => Sliding Window + HashSet?
     *
     * Idea:
     *  - create currentCharSet Set<Char> and currentCharsSequence as LinkedList<Char>;
     *  - implement a classic sliding window for loop:
     *      - when encountering an already present character => remove from the set and sequence all characters that are in between
     *        rightInd and it's last most recent index;
     *      - if doesn't have it => add the char there and if currentLength > maxLength => maxLength = currentLength;
     *  - return maxLength.
     *
     * Time: O(n), cause only at most n removals from the map;
     * Space: O(k), where k=amount of at most consecutive distinct characters in input.
     */
    fun crazy(input: String): Int {
        if (input.length == 1) return 1

        val currentCharsSequence = LinkedList<Char>()
        val currentCharsSet = mutableSetOf<Char>()
        var maxLength = 0

        input.forEach { char ->
            if (currentCharsSet.contains(char)) {
                while (currentCharsSequence.first != char) {
                    val removedChar = currentCharsSequence.removeFirst()
                    currentCharsSet.remove(removedChar)
                }
                currentCharsSequence.removeFirst()
            }
            currentCharsSet.add(char)
            currentCharsSequence.add(char)
            if (currentCharsSet.size > maxLength) maxLength = currentCharsSet.size
        }

        return maxLength
    }

    /**
     * Hints:
     *  - best valid subarray => Sliding Window;
     *  - distinct=occurs at most once => HashMap/HashSet.
     *
     * Idea:
     * - create a hashset charOccurMap, char->its most recent index (Char->Int)
     * - implement a classic sliding window for loop:
     *   - if a character is not in the charOccurMap, then
     *      - add character to it
     *      - if rightInd-leftInd+1 > maxLength, assign it to length
     *   - if the set contains it => shrink the window => leftInd=charOccurMap\[currentChar]+1.
     * - return maxLength
     *
     * Time: O(n)
     * Space: O(m), where m-the amount of distinct characters in the longest valid subarray (at most n).
     *
     * discovered thanks to
     * https://leetcode.com/problems/longest-substring-without-repeating-characters/editorial/
     * (should've really got it myself tho)
     */
    fun efficient(input: String): Int {
        val charOccurMap = mutableMapOf<Char, Int>()
        var maxLength = 0
        var leftInd = 0
        input.forEachIndexed { rightInd, newChar ->
            val lastInd = charOccurMap[newChar]
            if (lastInd == null) {
                val currentLength = rightInd - leftInd + 1
                if (currentLength > maxLength) maxLength = currentLength
            } else {
                while (leftInd != lastInd + 1) {
                    charOccurMap.remove(input[leftInd])
                    leftInd++
                }
            }
            charOccurMap[newChar] = rightInd
        }
        return maxLength
    }
}

fun main() {
    println(LongestSubstringWithoutRepeatingCharacters().crazy("aabaab!bb"))
}
