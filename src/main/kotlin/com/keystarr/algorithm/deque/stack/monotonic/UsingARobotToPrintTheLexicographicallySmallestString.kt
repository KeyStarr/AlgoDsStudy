package com.keystarr.algorithm.deque.stack.monotonic

/**
 * 💣 FULL RETRY a few weeks later in a shuffle: both recognition AND design/implementation
 * ⭐️ a great example of the monotonic stack (deque) problem, quite sophisticated
 * LC-2434 https://leetcode.com/problems/using-a-robot-to-print-the-lexicographically-smallest-string/description/
 * difficulty: medium (I'd say leet-hard, considering the combination of tools involved)
 * constraints:
 *  • 1 <= input.length <= 10^5;
 *  • input consists of only English lowercase.
 *
 * Final notes:
 *  • done [solution] "by myself" in 1h40m (⚠️⚠️⚠️). Full disclaimer: check all hints and skimmed top solutions and
 *   just verified that this is indeed the monotonic stack problem => proceeded to work out the rest of the solution by myself;
 *  • didn't record exactly, but I feel like I've realized that the solution involves using a monotonic stack
 *   in about ~30-40 mins ⚠️⚠️. Even managed to implement a wrong design based on a simple greedy rule;
 *  • failed the 1st submit and a couple of runs while working out details with the monotonic stack, first few iterations
 *   I've just taken the letter from the monotonic stack, and all in between of the input reversed => completely glanced
 *   over the need to check the top of the skipped items and the next stack item repeatedly => missed that the top of the
 *   skipped elements may be less than the actual NEXT min element in the input (monotonic stack);
 *  • the final solution involves:
 *   • a monotonic stack, to work out the increasing mins preserving the original order of the input;
 *   • a stack, to keep track of the numbers in between the increasing mins;
 *   • a greedy rule: to append into result either the next min element in the monotonic stack, or some top elements of the
 *    skipped elements stack (if these are less).
 *  • still, recognized the monotonic stack by myself 🎉, worked out the other stack and the greedy rule by myself 🎉,
 *   good progress. Now I need to do it FASTER for such problems;
 *  • PHEEEEEEEW.
 *
 * Value gained:
 *  • practiced recognizing and solving a problem efficiently using a monotonic non-decreasing stack (deque)
 *   + a 2nd plain Stack + a greedy rule.
 */
class UsingARobotToPrintTheLexicographicallySmallestString {

    // TODO: retry 1-2 weeks later

    /**
     * WRONG 1st APPROACH - just a greedy rule
     *
     * problem rephrase:
     *  - given: a string
     *  - step rules, either:
     *   - remove the first character of [input] and add it to the end of [removed] (1st)
     *   - or remove the last character of [removed] and add it to the end of [result] (2nd)
     *  goal: return the lexicographically MIN string that can be achieved in [result].
     *
     * can we stop prematurely, or do we have to move until all chars from input are in result?
     *  assume we have to go until the end (judging by examples), there's no one to ask
     *
     * lexicographical comparison:
     *  iterate through both strings, if str1\[i] > str2\[i] then str1 is greater lexicographically.
     *
     * input =
     * buffer =
     * result = azz (<zza
     *
     * basically we want to select the minimums amongst the letters in input and place them the earliest in result
     * => ideal `result` is input in ascending sort (alphabetically)
     *  BUT we cant always do that, as we are constrained by the ordering of letters in [input]
     *
     * observations:
     *  - 1st move, if we do it repeatedly = reversing the input
     *  - `removed` is acting as a Stack (FIFO)
     *  -
     *
     * input = za
     * buffer = z
     * result =
     *
     * 1st step we always have to do the 1st move
     * 2nd step we have a choice: 1st move or 2nd
     *  - if do the 1st move => we have take that new char ahead of the last in buffer
     *   when is that optimal?
     *    when input.first() < buffer.last() do the 1st move, so that input.first() would be in result earlier
     *    than buffer.last()
     *   otherwise => do the 2nd move.
     *
     * so its plain Greedy
     *  + building a String dynamically + returning a String => StringBuilder for result
     *
     * Time: always O(n)
     *  - outer loop is always exactly n iterations, where n=input.length
     *  - inner loop across all outer loop iterations + the last loop, are executed (together) exactly once for each character
     * Space: always O(n) if we count result, otherwise average/worst O(n)
     *  - worst buffer is when input is alphabetically sorted descending => worst buffer is O(n)
     *  - result is always eventually of length n
     *
     * --------------------------------------------
     *
     * CORRECT, 2nd APPROACH - monotonic stack + greedy + 2nd stack
     * (but outdated, changed a lot live)
     *
     * input =
     * buffer = bdda
     * result = addb
     *
     * greedy rule:
     * IF ALL CHARS remaining in the input are GREATER than buffer.last() => take buffer.last() into the result
     *  otherwise append input.first() to buffer
     *
     * brute force: literally go through all remaining chars in the input => O(n^2)
     * how to optimize getting checking that?
     *  if we could get the minimum character of all remaining ones => we can compare just that to buffer.last()
     *  but how to repeatedly get the next minimum character as we "move the window", i.o. remove the first char from the input.
     *
     * sliding window heap?
     *
     * monotonic stack non-decreasing?
     * YES, repeatedly getting mins in the original order
     *
     * we go through [input] and if we encounter a char less than all previous ones => we have to take it BEFORE
     * all the previous characters. If we encounter a character equal to the previous least one => we have to take it
     *  BEFORE any characters in-between it's last ind and the current ind.
     * =>
     * design:
     *  1. "feed" the input into the monotonic non-decreasing deque;
     *  2. iterate through the stack:
     *   - if input\[i] == deque.first():
     *    - result.append(deque.remove() + buffer.toString().reverse())
     *   - else:
     *    - buffer.append(input\[i])
     * UPD:
     *  - we need a greedy rule to, before we proceed skipping elements until we reach the current deque one,
     *   while buffer.last() < deque[\i] => result.append(buffer.remove())
     *   and only then attempt skipping the elements adding them to the buffer
     *  - actually its buffer.last() <= deque[\i], if EQUALS choose the item from the buffer, since there might be
     *   items in the input until we reach deque[\i], and buffer.last() then is guaranteed LESS than all of them!
     *   (cause deque\[i] is monotonic non-decreasing)
     *
     * edge cases:
     *  - [input] is sorted ascending alphabetically => return input as-is => correct, just the second "while" would never be entered;
     *  - can we ever run out of input chars but be not on the last char in the deck?
     *    no, the last input char will always be in the deck.
     *    if its equal to deck.last() => we add it to the deck
     *    if its greater deck.last() => we add it to the deck
     *    if its less than deck.last() => we remove from the deck until its either not or the deck is empty => we add it to the deck.
     *   => both input and the deck would be "emptied" at the same time.
     *   => no need for the 2nd while loop ind boundary checking.
     *  - input.length == 1 => correct.
     *
     * Time: always O(n)
     *  - add into the monotonic stack O(n);
     *  - main loop: O(n)
     *   across all iterations, each element of input will be added to remains at most once, and to the result exactly once
     *   worst we have n elements in the deck, when input is sorted ascending alphabetically;
     *  - remains at the end worst may have exactly n-1 elements if the input was sorted strictly descending => O(n) for
     *   the string construction and reversing at the end.
     * Space: without result average/worst O(n), with result always O(n)
     *  - deque at worst is O(n)
     *  - remains at worst is O(n)
     *  - result is always exactly n elements
     *
     * ----------
     *
     * bydizfve
     *
     * input = ydizfve
     * buffer = yizfv
     * result = bdevfziy
     *
     * bac
     * stack=ac
     * input = bac
     * buffer = b
     * result = a
     *
     *
     * input=vzhofnpo
     * stack=fno
     *
     * iC=p
     * sC=o
     * remains=vzho
     * result=fn
     *
     * if remains.last() == deque\[current] => TAKE REMAINS.LAST()
     *
     */
    fun solution(input: String): String {
        val deque = ArrayDeque<Char>()
        input.forEach { char ->
            while (deque.isNotEmpty() && char < deque.last()) deque.removeLast()
            deque.addLast(char)
        }

        var inputInd = 0
        val remains = StringBuilder()
        val result = StringBuilder()
        deque.forEach { char ->
            while (remains.isNotEmpty() && remains.last() <= char) result.append(remains.removeLast())

            while (input[inputInd] != char) {
                remains.append(input[inputInd])
                inputInd++
            }
            result.append(char)
            inputInd++
        }

        return result.append(remains.reversed()).toString()
    }

    private fun StringBuilder.removeLast(): Char {
        val lastChar = get(lastIndex)
        deleteAt(lastIndex)
        return lastChar
    }
}

/**
 * input = bac
 * stack=ac
 *
 * fI=0
 * iI=2
 * eI=0
 *
 * remains =
 * result = a
 */
fun main() {
    println(
        UsingARobotToPrintTheLexicographicallySmallestString().solution(
            "bac"
        )
    )
}
