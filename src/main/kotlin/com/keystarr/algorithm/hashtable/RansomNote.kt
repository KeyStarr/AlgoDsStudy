package com.keystarr.algorithm.hashtable

/**
 * LC-383 https://leetcode.com/problems/ransom-note/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= ransomNote.length, magazine.length <= 10^5;
 *  • ransomNote and magazine are both of lowercase English;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • done [efficient] in 15 mins (excuse - haven't been coding in 2 days);
 *  • no real value gained besides making use of getOrDefault in a creative way, and just practicing beautiful Kotlin style;
 *  • be more attentive to easy tasks? [efficientCleaner] when first implemented, overlooked an obvious pitfall and
 *      FAILED the first submission. Maybe coz tired rn, but still;
 *  • watch out for early returns, originally MISSED the one here - and it's a cool optimization.
 */
class RansomNote {

    /**
     * Problem rephrase:
     * "return true if all characters from "magazine" are present in "ransomNote" (including multiple occurrences of
     * distinct chars)"
     *
     * Idea:
     * - create a noteMap HashMap, character->occurrence (Char->Int);
     * - iterate through all chars of "ransomNote", record each character count in the noteMap; O(n)
     * - create an identical map for "magazine", iterate through all chars of "magazine", record the count of each; O(m)
     * - iterate through entries of noteMap, check if the value for each key in it is equal or less to the value for the same key
     *  in the magazineMap. If so, return true, otherwise false. O(1)
     *
     * Edge cases:
     *  - magazine < ransomNote => return false (didn't catch this optimization myself, learned after);
     *  - ransomNote.length=1 => works ok;
     *  - magazine.length=1 => works ok;
     *  - both lengths=1 => works ok.
     *
     * Time: O(n+m), where n=ransomNote.length and m=magazine.length;
     * Space: O(1) cause only 2 hashmaps, each taking consisting of at most 26 entries.
     */
    fun efficient(ransomNote: String, magazine: String): Boolean {
        if (ransomNote > magazine) return false

        val noteOccurMap = ransomNote.countEnglishLowerLetters()
        val magazineOccurMap = magazine.countEnglishLowerLetters()
        return noteOccurMap.entries.all { entry -> (entry.value <= magazineOccurMap.getOrDefault(entry.key, 0)) }
    }

    /**
     * Same as [efficient], but use a single HashMap. Doesn't impact asymptotic complexity, but the const is better, still.
     *
     * Count char occurrences in [magazine], but subtract from it upon encountering same characters in [ransomNote].
     * Return false in the process if some characters from the note are not found in the magazine, or exceed the amount of
     * chars in the magazine (value in magazineMap becomes < 0).
     *
     * Can't count only in [ransomNote], cause if the character was not in the [magazine], but was in the [ransomNote],
     * that case would be missed! FAILED a test case cause of that.
     */
    fun efficientCleaner(ransomNote: String, magazine: String): Boolean {
        if (ransomNote.length > magazine.length) return false

        val magazineOccurMap = magazine.countEnglishLowerLetters()
        ransomNote.forEach { char ->
            val currentOccur = magazineOccurMap[char] ?: return false
            val newOccur = currentOccur - 1
            if (newOccur < 0) return false
            magazineOccurMap[char] = newOccur
        }
        return true
    }

    private fun String.countEnglishLowerLetters() = mutableMapOf<Char, Int>().also { occurMap ->
        forEach { char -> occurMap[char] = occurMap.getOrDefault(char, 0) + 1 }
    }
}

fun main(){
    println(RansomNote().efficientCleaner("fffbfg", "effjfggbffjdgbjjhhdegh"))
}
