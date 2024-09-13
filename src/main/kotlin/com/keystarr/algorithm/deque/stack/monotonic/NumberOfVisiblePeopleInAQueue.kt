package com.keystarr.algorithm.deque.stack.monotonic

import kotlin.math.max

/**
 * LC-1944 https://leetcode.com/problems/number-of-visible-people-in-a-queue/description/
 * difficulty: hard
 * constraints:
 *  • 1 <= heights.size <= 10^5;
 *  • 1 <= heights\[i] <= 10^5;
 *  • all values in heights are unique.
 *
 * Final notes:
 *  • ⚠️⚠️🏆 done by myself in 1h20m;
 *  • intuitively felt that probably the best tool here is the monotonic stack, but spent a lot of time trying to figure out
 *   just what the pattern here is. Also felt bad physically and mentally at the time, so out of sheer will blindly tried
 *   dry running, getting the required result as according to the problem specification and looking for the pattern as
 *   I did it;
 *  • did [bruteForce] when got stuck with the efficient solution, which helped a lot 🔥 to solidify understanding of the pattern.
 *   Basically monotonic stack here is the optimization of that very same principle on which brute is based;
 *  • formulated twice exactly what needs to be done, though about 2 stacks at first, but then finally at the 100th dry run
 *   realized that we just need a single stack here and how to do it, though mechanically, haven't yet exactly understood why;
 *  • ⚠️💡 actually, phrased it wrong originally in [efficient]. The actual core concept here is: for each number find the
 *   length of the increasing subsequence that starts with the first number in heights\[ind+1:] that is LESS than heights\[ind],
 *   and ends with the first number that is greater than heights\[ind] in that same subarray, inclusive (or end of array, exclusive);
 *  • 🔥 deducing that core principle and realizing exactly how to implement it using the monotonic stack took me a loong time;
 *  • not sure how I could've done it faster though... more experience with phrasing / modelling => solving monotonic stack problems?
 *
 * Value gained:
 *  • practiced solving a "valid subsequence length for each number" efficiently using a strictly decreasing monotonic stack.
 */
class NumberOfVisiblePeopleInAQueue {

    // TODO: solved but way too long => repeat in 1-2 weeks
    //  consider simplifying like https://leetcode.com/problems/number-of-visible-people-in-a-queue/solutions/1359707/java-c-python-stack-solution-next-greater-element/

    // TODO (optional): consider trying out the other direction solution
    //  e.g. https://leetcode.com/problems/number-of-visible-people-in-a-queue/solutions/1359702/monostack/ (may be simpler to understand)

    /**
     * 10 6 8 5 11 9
     *
     *
     * observations:
     *  - when we encounter heights\[j] after some heights\[i] such that heights\[j] > heights\[i] => the ith person is guaranteed
     *   to not see anyone past the jth person;
     *  - every person but the last one can see at least always the next person.
     *
     * 10 6 8 5
     *
     * answer
     *  0: 4
     *  1: 2-1=1
     *  2: 2
     *  3: 1
     *  4: 1
     *  5: 0
     *
     *
     * the trick is - how do we catch that in between some higher and lower value in the descending monotonic stack there is
     *  some value larger than the smaller one => so we don't count the smaller??
     *
     * why is it important that all values in [heights] are unique?
     *
     * ------ brute force
     *
     * failed in reasonable time to get the efficient approach => try brute force first. Simply simulate the problem's conditions,
     * see if I can catch any insights from that
     *
     * Time: average/worst O(n^2)
     * Space: if not counting the answer - always O(1)
     *
     *
     */
    fun bruteForce(heights: IntArray): IntArray {
        val answer = IntArray(size = heights.size)
        heights.forEachIndexed { i, height ->
            var maxEncountered = -1
            for (j in i + 1..heights.lastIndex) {
                val nextHeight = heights[j]
                if (nextHeight > maxEncountered) answer[i] = answer[i] + 1
                if (nextHeight > height) break
                maxEncountered = max(nextHeight, maxEncountered)
            }
        }
        return answer
    }

    /**
     *
     * can we optimize [bruteForce]? ideally O(n) time
     *
     * I feel there is some monotonic stack logic going on
     *
     *
     * 10 6 8 5 11 9
     *
     * 10 8 5 11
     *
     *
     * 10 8 6 5 9 11 9
     *
     * (10) 8 9 11
     *
     * I recall a similar problem where we've used a monotonic stack and simply, as we popped a value from the stack,
     *  wrote it to the answer, computing it from the number's index and current index.
     *
     * Here the trick is that we must count only such values between the index we pop and the current index, which
     *  were not towered by their left neighbors. How to do that? Or is this angle wrong entirely?
     *
     * What if we were, as we pop the value - iterate backwards? And get all values that we encounter according to the logic.
     *  Hm, that would be exactly the brute force actually, since in brute we iterate forward and simply break when we meet the first obstacle.
     *  (here we'd pop as we meet the obstacle and simply iterate backwards from that)
     *
     * Observation: hm, then basically the answer for each number is the increasing subsequence of the original array
     *  from (i+1) until the number that is either larger than heights\[i] or the end of the array!
     *
     * 2 stacks somehow? Ideally we should compute the result for each number that we pass ALL AT THE SAME TIME, not individually
     *  running the increasing stack for each i.
     * =>
     * What if we do both stacks at once - decreasing and increasing?
     *
     * example #1
     * input = 10 6 8 9 5 3 1 11 12
     *
     * decreasing = 10:0 9:2 5:0 3:1 11:
     *
     * decreasing = 10:0 9:2 5:0 11:(2)
     *
     * answer = 5:1, 6:1, 4: 2+1+1=4
     *
     *
     * 1. save into the monotonic strictly decreasing stack how many number did the value crash, i.o. numbers amount
     *  there are between the last value in the stack at the moment of adding and the new value in the stack
     * 2. as we pop the value:
     *  - add the number of
     *
     * ---------------------------
     *
     * => GOAL for each ith number - count the increasing subsequence between the ith and the jth number, j being either the FIRST
     *  number that heights\[j] > heights\[i] or the end of the array.
     *
     * input= 10 6 7 5 8 11 4
     * stack= 11:1 4:0
     * answer= 0: 3+1=4, 1: 0+1=1, 2: 1+1=2, 3: 0+1=1, 4: 0+1=1, 5: 1, 6: 0
     *
     * => approach;
     * 1. as we remove the number from the decreasing stack => write the answer for it
     * 2. as we add the number to the decreasing stack => increase the increasingSubsequence count for the current top of the stack, if its not empty
     *
     *       |
     * |     |
     * |     |
     * |   | |
     * |   | |
     * | | | |
     *
     * Edge cases:
     *  - heights.size == 1 => always return [0] => no increasing for the first while won't be entered => return [0], correct as-is.
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     *  worst is [heights] is sorted strictly decreasing => by the first loop's termination we have exactly n entries in the stack.
     */
    fun efficient(heights: IntArray): IntArray {
        val stack = mutableListOf<Entry>() // strictly decreasing
        val answer = IntArray(size = heights.size)
        heights.forEachIndexed { ind, newHeight ->
            while (stack.isNotEmpty() && heights[stack.last().ind] < newHeight) {
                val entry = stack.removeLast()
                answer[entry.ind] = entry.increasingSubsequenceLen + 1
            }
            if (stack.isNotEmpty()) stack.last().increasingSubsequenceLen++
            stack.add(Entry(ind = ind, increasingSubsequenceLen = 0))
        }

        while (stack.isNotEmpty()) {
            val entry = stack.removeLast()
            answer[entry.ind] = entry.increasingSubsequenceLen
        }

        return answer
    }

    /**
     * @param increasingSubsequenceLen - that starts at the first number from heights[ind+1:] that is less than heights\[ind]
     *  and ends (exclusive) with the first number greater than heights\[ind].
     */
    private class Entry(
        val ind: Int,
        var increasingSubsequenceLen: Int,
    )
}

fun main() {
    println(
        NumberOfVisiblePeopleInAQueue().bruteForce(
            heights = intArrayOf(10, 6, 7, 5, 8, 11, 4),
        ).contentToString()
    )
}
