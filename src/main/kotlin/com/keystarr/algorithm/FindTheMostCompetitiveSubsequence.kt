package com.keystarr.algorithm

/**
 * 💣 retry - reinforce, spent too much time on recognizing the approach
 * LC-1673 https://leetcode.com/problems/find-the-most-competitive-subsequence/submissions/1355399788/
 * constraints;
 *  • 1 <= nums.size <= 10^5;
 *  • 0 <= nums\[i] <= 10^9;
 *  • 1 <= subsequenceSize <= nums.size
 *
 * Final notes:
 *  • 🎉 done [efficient] by myself in 55 mins;
 *  • ⚠️💣 generally, I definitely need more interleaving practice with monotonic stack. Found out the full approach core with the
 *   monotonic stack at, like, 30 mins. First thought of the monotonic stack seriously at about 20 mins mark (just by feelings,
 *   didn't mark the time);
 *  • though DSA course author stated it's a pretty rare topic => we have other bigger priorities right now. But in general
 *   it would be pretty cool to do that;
 *  • 🔥🔥 at first, when I just read the problem statement, I had a blank mind => how to even find such a weirdly phrased
 *   subsequence? The constraint on the best set me off. Then I looked at the example, dry ran it, and started to notice
 *   some pattern that we need to consider the minimum elements.
 *   Then gradually started to build up: how do we select the first best element to our subsequence?
 *   How to select the next best element ith?
 *   => started to "feel" intuitively, recognizing the pattern of the "next best min" in a stream of integers, but
 *    with an additional constraint of matching the required combination size.
 *   => was sure it's monotonic stack then, and dry ran, and proceeded to implement it;
 *  • failed 2 submissions:
 *   both initial implementation bugs
 *    - 1st: as we remove the number from the collection => forgot to increase the counter of how many elements we need to find;
 *    - 2nd: returned from kotlin's `forEachIndexed` meaning to break the loop, but actually just skipped an iteration 🤦‍;
 *  • now that I wrote "repeatedly finding mins" I thought about using a heap for that, but here actually the order of the
 *   answer matters, and heap doesn't guarantee the order, and making it work would not be trivial + it's time complexity
 *   would add a factor of either (log subsequenceSize) or even (log nums.size) to O(n) time, so it's not worth considering,
 *   neither was it an optimal stepping stone;
 *  • 🏆 monotonic stack - what gave it away was the fact that we needed repeated minimums, but only up to a point,
 *   and then the rest of the numbers, plus all of that IN ORDER => repeated mins + in order => monotonic stack, not the heap.
 *
 * Value gained:
 *  • practiced recognizing and solving a problem efficiently with a monotonic stack with additional constraint on top.
 */
class FindTheMostCompetitiveSubsequence {

    // TODO: repeat in 1-2 weeks, to reinforce the approach recognition better

    /**
     * problem rephrase:
     *  - given:
     *   - an array of integers
     *   - subsequenceSize: Int
     *  - goal: return the best valid subsequence
     *   - valid: a subsequence of [nums] with size == [subsequenceSize]
     *   - best: across all VALID subsequences, the first number that it differs with each other, is always less
     *    than the number on the corresponding position in the other subsequence.
     *
     * i.o. best valid combination of elements
     *
     * ------- approach ---------
     *
     * we could try selecting exactly one number at a time, until we reach [subsequenceSize]
     *
     * suppose we select a number X with index ith => when is it the wrong choice = the best valid subsequence doesn't contain it
     *  on current given subsequence position?
     *
     * observation:
     *  if the minimum UNIQUE element in the array is position such, that we can take it and still have enough elements to match
     *   [subsequenceSize-1] => the answer always stars with that element, and other elements don't matter
     *  if its not unique => the answer still ALWAYS starts with it, and then we need to find the next element
     *   => the next min element of the remaining part of the array, but such that we still have elements to fill the required
     *    subsequence capacity left.
     *
     * non-decreasing monotonic stack?
     *
     * nums=[3 5 2 6] sSize=2
     *
     * when we encounter a new minimum => if we have enough numbers left to match the required length, that's the best choice.
     *
     * 2 4 3 3 5 4 9 6, sSize=4
     *
     * 2 3
     * toFindSize=2
     * remainingSize=8-2=6
     * 2 < 6
     *
     * 2 3 3 4
     * toFindSize=4-5=-1
     * optionsSize=8-4=4
     * -1 < 4
     *
     * 2 3 3 4 6
     * toFindSize=-2
     * optionsSize=1
     * -2 < 1
     *
     * take the first 4 elements from the stack => 2 3 3 4
     *
     *
     * major edge case: if we just run the monotonic non-decreasing stack => what if we don't have enough elements?
     *  => each time we remove check if we have enough elements left? if not enough => don't remove and just take all the current
     *  values PLUS all the remaining values?
     * it has to be the answer, cause since we've got all min elements in order, the subsequence could not have used any other
     *  elements before them, since the chosen ones would always beat the other choices prior to the current moment.
     *
     * major edge case: on loop's termination stack.size > [subsequenceSize]
     *  => just select the first [subsequenceSize] numbers from the stack
     *
     * basically the goal = return the subsequence of minimums of size [subsequenceSize], or fill with the remaining numbers
     *  if the required size can't be matched only with minimums.
     *
     * other edge cases:
     *  - nums.size == 1 & [subsequenceSize] == 1 => we must take that element
     *  - [subsequenceSize] == nums.size =>
     *
     *
     * 2 1 4 3, sSize =4
     *
     * 2
     * i=1
     * toFindSize=3
     * optionsSize=3
     * 3 !< 3
     * don't remove
     *
     * ans = 2 1 4 3
     *
     * ---------------------
     *
     * nums.size=28
     * sSize=24
     *
     * i=0
     * 84
     *
     * i=1
     * 84 (10)
     * toFindSize=23
     * optionsSize=28-1=27
     * 23 < 27
     * => 10
     *
     * i=2
     * 10 71
     *
     * i=3
     * 10 71 (23)
     * toFindSize=24-2=22
     * optionsSize=28-3=25
     * 22 < 25 => 10 23
     *
     * i=4
     * 10 23 66
     *
     * i=5
     * 10 23 66 (61)
     * toFindSize=24-3=21
     * optionsSize=28-5=23
     * 21 < 23 => 10 23 61
     *
     * i=6
     * 10 23 61 62
     * i=7
     * 10 23 61 62 64
     * i=8
     * 10 23 61 62 64 (34)
     * toFindSize=24-5=19
     * optionsSize=28-8=20
     * 19<20 => 10 23 61 62, toFindSize=20
     * 20<20 => add all the rest numbers as they come!
     *
     *
     * a valid answer always exists cause [subsequenceSize] <= nums.size.
     *
     * Time: average/worst O(n + n) = always O(n)
     *  - inner while across or outer loop iterations has worst, if nums is strictly decreasing and subsequenceSize==1
     *   => (n-1) iterations = (n-1) pop, each pop costs O(1);
     *  - outer loop has exactly n iterations always.
     *  Space: average/worst O(n) if we don't count the result
     *   - worst stack.size is nums is strictly increasing and [subsequenceSize]=nums.size => O(n)
     *
     * Time can't be improved asymptotically, cause since the problem asks for the best valid combination of elements
     *  => we have to check up to n elements at least once. I don't think we could take any shortcuts.
     */
    fun efficient(nums: IntArray, subsequenceSize: Int): IntArray {
        val stack = ArrayDeque<Int>()
        nums.forEachIndexed { ind, num ->
            val chosenSize = stack.size
            var toFindSize = subsequenceSize - chosenSize
            val optionsSize = nums.size - ind
            while (stack.isNotEmpty()
                && stack.last() > num
                && (toFindSize < optionsSize)
            ) {
                stack.removeLast()
                toFindSize++
            }
            stack.addLast(num)
        }

        val result = IntArray(size = subsequenceSize)
        var resultInd = 0
        for (number in stack) {
            if (resultInd == subsequenceSize) break
            result[resultInd++] = number
        }

        return result
    }

    /**
     * Notice that actually we may always skip at most (nums.size-subsequenceSize) elements to get to the actual minimums
     * => precompute the number of skips we can perform and check that in the while.
     *
     * Skip cause we prioritize min numbers to be as early as possible in the subsequence.
     *
     * Discovered thanks to: https://leetcode.com/problems/find-the-most-competitive-subsequence/solutions/1027495/python-stack-solution-explained/
     */
    fun efficientCleaner(nums: IntArray, subsequenceSize: Int): IntArray {
        val stack = ArrayDeque<Int>()
        var skipsLeft = nums.size - subsequenceSize
        nums.forEachIndexed { ind, num ->
            while ((stack.isNotEmpty() && stack.last() > num) && (skipsLeft > 0)) {
                stack.removeLast()
                skipsLeft--
            }
            stack.addLast(num)
        }

        val result = IntArray(size = subsequenceSize)
        var resultInd = 0
        for (number in stack) {
            if (resultInd == subsequenceSize) break
            result[resultInd++] = number
        }

        return result
    }
}

fun main() {
    println(
        FindTheMostCompetitiveSubsequence().efficient(
            nums = intArrayOf(
                84, 10, 71, 23, 66, 61, 62, 64, 34, 41, 80, 25, 91, 43, 4, 75, 65, 13, 41, 46, 90, 55, 85, 61, 95, 71,
            ),
            subsequenceSize = 24,
        ).contentToString()
    )
}
