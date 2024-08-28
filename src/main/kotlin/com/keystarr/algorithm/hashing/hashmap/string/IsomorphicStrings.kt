package com.keystarr.algorithm.hashing.hashmap.string

/**
 * ⭐️❌ honestly, its the first time in 230 problems that I'm just gonna skip this for now. I simply don't understand it
 *  right now no matter how I tried and its killing me.
 *
 *
 * LC-205 https://leetcode.com/problems/isomorphic-strings/description/
 * difficulty: easy (lol)
 * constraints:
 *  - 1 <= w1.length == w2.length <= 5*10^4.
 *
 * Final notes:
 *  - wow, I just don't understand the solution. 1h in, checked the official solution and here I am. Like, no clue, why
 *   is it correct for all cases? Tried dry-running and watching Neetcode, still don't get it. Wow.
 *
 * Value gained:
 *  -
 */
class IsomorphicStrings {

    // TODO: full retry, deep understand the solution

    /**
     * problem rephrase:
     *  - given: word1, word2 strings
     *  - valid operations:
     *   1. replace all occurrences of character X with character Y, preserving order;
     *   2. if X->Y was done, Z->Y is no longer available;
     *   3. all modifications are only done on one exclusively string of the pair.
     *  goal: return true if it's possible to get word1 from word2 or vice versa using only valid transformations.
     *
     * counting
     *
     * egg add
     * e: 1, g: 2
     * a: 1, d: 2
     *
     * paper title
     * p: 2, a: 1, e: 1, r: 1 => 1: 3, 2: 1
     * t: 2, i: 1, l: 1, e: 1 => 1: 3, 2: 1
     *
     * when is it impossible?
     *
     * 1. operations only change characters but not the amount of chars, neither reduce nor increase
     *  => if w1.length != w2.length early return false;
     * 2. lengths are same, also we must have the same frequency of occurrences. If these don't match, return false;
     * 3. lengths are same, frequency of occurrences are same, also the order matters.
     *  consider
     *  paper tilte
     *
     *  first 2 requirements are met, but since the order of the second t with freq=2 doesn't match the order of the 2nd p
     *   with freq=2 and these are only chars with freq 2 in either string => return false
     *
     *  hmm, iterate through word1, if w1Freq\[word1\[i]] != w2Freq\[word2\[i]] return false
     *  => 3rd property is that all characters in both strings on same positions must have same frequency.
     *
     * does it matter that we can't do conversion to the same character more than 1? We already accounted for that,
     *  designed with that being impossible in mind. Yep, if we could convert to same char, relative frequencies wouldn't matter
     *  in such a way, as we could change then some initial frequencies, merge them together, but we can't.
     *
     * i.o. we can't change length of the string, and we can't change any of the initial frequencies => thus frequencies on
     * corresponding positions must match. hm but consider
     * aaeerr
     * eeeerr
     *
     * w1
     * aa->ee
     * eeeerr TRUE
     * we had 2 separate original frequencies 2 (a) and 2 (e) and merged them together into 4 (e)
     * => changing the original frequencies is possible!
     * => we ONLY might increase the frequency of some already existing character ONCE (by converting some other character into it)
     *
     * ----
     *
     * w1 = bbbaaaba
     * w2 = aaabbbba
     *
     * freq1 = b: 4, a: 4
     * freq2 = a: 4, b: 4
     *
     * i=0,
     * c1=b, f1=4
     * c2=a f2=4
     *
     * so all frequencies on corresponding positions match, but the transformation is impossible
     * (eh, and I felt not being confident enough in the concept, couldn't ignore that unsteady feeling)
     * => why?
     *  we have in both strings same 2 distinct characters with same equal frequencies, but the strings are composed
     *  of these characters in different RELATIVE order
     */
    fun wrongFirst(w1: String, w2: String): Boolean {
        val w1Freq = w1.countFrequencies()
        val w2Freq = w2.countFrequencies()
        w1.forEachIndexed { ind, c1 ->
            val c2 = w2[ind]
            if (w1Freq[c1] != w2Freq[c2]) return false
        }
        return true
    }

    private fun String.countFrequencies(): Map<Char, Int> = mutableMapOf<Char, Int>().also { map ->
        forEach { char -> map[char] = map.getOrDefault(char, 0) + 1 }
    }

    /**
     * right after [wrongFirst]
     *
     * what if we, for each character, not only count its frequency, but SAVE each index we can find this char at into a list
     * so
     *
     * w1 = bbbaaaba
     * w2 = aaabbbba
     *
     * f1= a:[3,4,5,7], b:[0,1,2,6]
     * f2= a:[0,1,2,7], b:[3,4,5,6]
     *
     *
     * w1=egg  w2=add
     * f1 = e: [0], g: [1,2]
     * f2 = a: [0], d: [1,2]
     *
     *
     * w1=aaeerr   w2=eeeerr
     * f1= a: [0,1], e=[2,3], r=[4,5]
     * f2= e: [0,1,2,3], r=[4,5]
     *
     *
     * 1. iterate through [w1]:
     *  if f1[w1\[i]] == f2[w2\[i]] || f1\[w1\[i]] + f1\[w2\[i]] == f2\[w2\[i]]
     *   we consider the transformation to be possible???
     *   if not return false?
     * 2. try transforming both first w1 then if not true w2.
     */
    fun abomination(w1: String, w2: String): Boolean {
        val p1 = w1.collectPositions()
        val p2 = w2.collectPositions()
        return w1.isomorphic(w2, p1, p2) || w2.isomorphic(w1, p2, p1)
    }

    private fun String.isomorphic(other: String, p1: Map<Char, List<Int>>, p2: Map<Char, List<Int>>): Boolean {
        forEachIndexed { ind, char ->
            val otherChar = other[ind]
            if (p1[char] != p2[otherChar]
                && ((p1.getValue(char) + (p1[otherChar] ?: emptyList())) != p2[otherChar])
            ) return false
        }
        return true
    }

    private fun String.collectPositions(): Map<Char, List<Int>> = mutableMapOf<Char, MutableList<Int>>().also { map ->
        forEachIndexed { ind, char ->
            if (map[char] == null) map[char] = mutableListOf()
            map.getValue(char).add(ind)
        }
    }

    /**
     * observations:
     *  1. since we cannot change the other string, as we go through the 1st string, if characters don't match, we
     *   MUST transform w1\[i] into w2\[i]. And record for later that all instances of that char were changed. If we encounter
     *   same char in w1 further, we must treat it as it is w2\[i] character, and if it doesn't match the corresponding w2's char
     *   => return false, cause any further change would violate equivalences of the previous occurrences, there are no options
     *  2. should we check both w1 to w2 and w2 to w1? Is there actually a case when w1 cant be transformed into w2,
     *   but w2 CAN be transformed into w1 or vice versa?
     *
     *   w1=badc
     *   w2=baba
     *   transformations=d:b, c:a
     *
     *   i=2, d!=b, babc
     *   i=3, c!=a, baba
     *   correct
     *
     *   w1=baba
     *   w2=badc
     *   transformations=b->d
     *
     *   i=2, b!=d, dada => WRONG, first b must not have been changed
     *
     *
     *
     *
     *
     *
     */
    fun solution(w1: String, w2: String): Boolean {
        val transformations = mutableMapOf<Char, Char>()

        w1.forEachIndexed { ind, c1 ->
            val c2 = w2[ind]
            val actualC1 = transformations[c1] ?: c1
            if (actualC1 == c2) return@forEachIndexed

            if (transformations.contains(c1)) return false
            transformations[c1] = c2
        }

        return true
    }
}

fun main() {
    println(
        IsomorphicStrings().abomination(
            w1 = "foo",
            w2 = "bar",
        )
    )
}
