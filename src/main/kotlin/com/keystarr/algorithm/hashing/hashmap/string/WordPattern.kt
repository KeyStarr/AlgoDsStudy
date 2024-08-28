package com.keystarr.algorithm.hashing.hashmap.string

/**
 * ⭐️ a grand example of a strings matching type problem with NO single pass reasonable solution
 *  hm, I experienced same level of frustration with [com.keystarr.algorithm.graph.MaximalNetworkRank] - it being the first
 *  graph property calculation problem I've encountered without a O(n) solution (only O(n^2))
 *
 * LC-290 https://leetcode.com/problems/word-pattern/description/
 * difficulty: easy (LOOOOOOOOOOOOOOOOOOOOOOOL)
 * constraints:
 *  • 1 <= pattern.length <= 300;
 *  • pattern contains only lowercase English;
 *  • 1 <= text.length <= 3000;
 *  • text contains only lowercase English and spaces;
 *  • text doesnt have neither trailing nor leading spaces;
 *  • all words in text are separated by a single space.
 *
 * Final notes:
 *  • ⚠️⚠️⚠️ done [efficientAbomination] by myself in 50 mins;
 *  • ⚠️ FAILED 4 edge cases!!! and made an obviously overcomplicated solution;
 *  • there gotta be a better basic approach, better core design choices for simpler more maintainable logic.
 *   UPD: and here it is [efficientClean], actually same most design choices, but just get all words first!
 *  • leet-easy problem. huh. apparently its not worth it going for a single pass straight away even for array/string type
 *   questions. It makes sense to go for the best solution (probable) asymptotically, but I guess lets not max optimize
 *   time/space consts straight away.
 *
 * Value gained:
 *  • practiced solving a strings matching type problem using 2 hashmaps for a single association for O(1) checks both ways;
 *  • learned that its better to do the simplest asymptotically best (probable) solution, don't bother too much with the const
 *   first. Even for array/strings type problem. Here I could've just split them words first, though I haven't even though of that
 *   absolutely. Hm;
 */
class WordPattern {

    /**
     * Exactly same core ideas as [efficientAbomination], BUT don't try to check everything in one pass!
     * And don't try to save space const:
     *  1. find all words, add into a list 'words';
     *  2. check if words.size != pattern.size => return false;
     *  3. declare map patternChar->word and a map word->patternChar;
     *  3. iterate through pattern and keep an index on the current word:
     *   - if pattern char isn't mapped to any word:
     *    - if current word IS mapped to another pattern char => return false;
     *    - else map word to char and char to word;
     *   - else if a word it is mapped to != currentWord => return false;
     *  4. if we terminated the loop without returning => return true.
     *
     * Time: always O(n)
     *  - words splitting is exactly n iterations for main loop and creation of strings with ~n chars total => O(n) time;
     *  - main loop is O(n).
     * Space: always O(n)
     *  - words list, total chars in all words are n-words.size space = O(n);
     *  - both maps.
     *
     * ------
     * Learned thanks to https://www.youtube.com/watch?v=W_akoecmCbM
     */
    fun efficientClean(pattern: String, text: String): Boolean {
        val words = text.split(' ')
        if (pattern.length != words.size) return false

        val patternToWordMap = mutableMapOf<Char, Int>()
        val wordToPatternMap = mutableMapOf<String, Int>()
        for (i in pattern.indices) {
            val currentWord = words[i]
            val currentPattern = pattern[i]
            if (patternToWordMap.contains(currentPattern)) {
                val targetWordInd = patternToWordMap.getValue(currentPattern)
                if (words[targetWordInd] != currentWord) return false
            } else {
                if (wordToPatternMap.contains(currentWord)) return false
                patternToWordMap[currentPattern] = i
                wordToPatternMap[currentWord] = i
            }
        }
        return true
    }

    /**
     * problem rephrase:
     *  - given:
     *   - pattern: String
     *   - text: String
     *  Goal: return true if text matches the pattern
     *   matches = each character of [pattern] is a word in the [text]. Equal characters mean equal words.
     *    associate each character at the first character appearance with the word in [text] on corresponding index
     *
     * Approach:
     *  - declare a map patternChar->word;
     *  - as we encounter a new word in [text], increment patternInd update targetWord by map[patternChar], if its null,
     *   then as we will encounter the next word => add current word to the map with that patternChar;
     *  - if targetWord isnt null, compare it by character with the current word in [text], if some chars are not equal
     *   => return false.
     *  - return true if havent returned yet.
     *
     * Edge cases:
     *  - exactly a single space is guaranteed between each word in [text], and no trailing/leading spaces;
     *  - what if pattern.size != number of words in text?
     *   if pattern.size < number of words, do we treat the pattern cyclical?
     *   if pattern.size > number of words, do we consider it valid?
     *   based on "full pattern match" in the description => consider these cases as invalid, return false
     *   => ⚠️ formulated BUT FORGOT TO CHECK => failed BOTH submits!
     *   => check both cases in a special way, with special ifs (marked them in code);
     *
     *  - text.size == 1 => if pattern.size == 1 return true else false => correct, we'd just have targetWordInd == currentWordInd.
     *  - FAILED submit: we encounter a word that is already mapped to a patter char, but current pattern character isn't mapped to anything!
     *   special case of that, another edge: the word that is already associated with a char is the last word in the text
     *
     *  - FAILED submit: current word in [text] has same characters with target word BUT ends earlier! => return false
     *   => special 100500th early return as well.......
     *
     * Time: always O(n)
     *  - main loop has worst n iterations, n=text.size;
     *  - across all main loop iterations we create substrings of total size at worst == n - whitespacesCount.
     * Space: average/worst O(n)
     *  - worst all pattern chars are distinct => patternToWordStartIndMap has exactly n entries,
     *   wordToPatternIndMap has exactly n entries as well, all keys strings total length == n - whitespacesCount.
     */
    fun efficientAbomination(pattern: String, text: String): Boolean {
        val patternToWordStartIndMap = mutableMapOf<Char, Int>()
        val wordToPatternIndMap = mutableMapOf<String, Int>()
        var patternInd = 0
        var currentWordStartInd = 0
        var targetWordInd = 0
        for (i in 0..text.length) {
            if (i == text.length || text[i] == ' ') {
                // currentWord is only a prefix of targetWord => we haven't checked all chars of target word
                if (i != targetWordInd && text[targetWordInd] != ' ') return false

                // pattern.size < number of words in text:
                if (i != text.length && text[i] == ' ' && patternInd == pattern.length - 1) return false

                val currentWord = text.substring(currentWordStartInd, i)
                val patternChar = pattern[patternInd]
                if (!patternToWordStartIndMap.contains(patternChar)) {
                    if (wordToPatternIndMap.contains(currentWord)) return false
                    patternToWordStartIndMap[patternChar] = currentWordStartInd
                    wordToPatternIndMap[currentWord] = patternInd
                }
                if (i == text.length) break

                currentWordStartInd = i + 1
                val newPatternChar = pattern[++patternInd]
                targetWordInd = patternToWordStartIndMap[newPatternChar] ?: currentWordStartInd
                continue
            }

            if (text[i] != text[targetWordInd]) return false
            targetWordInd++
        }

        // if not equal, then pattern.size > number of words in text
        return patternInd == pattern.length - 1
    }
}

fun main() {
    println(
        WordPattern().efficientClean(
            pattern = "abbc",
            text = "dog cat cat dog",
        )
    )
}
