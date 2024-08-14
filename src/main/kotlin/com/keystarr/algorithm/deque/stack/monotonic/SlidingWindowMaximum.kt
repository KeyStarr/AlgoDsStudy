package com.keystarr.algorithm.deque.stack.monotonic

/**
 * LC-239 https://leetcode.com/problems/sliding-window-maximum/description/
 * difficulty: hard (!!!!!)
 * constraints:
 *  • 1 <= nums.size <= 10^5;
 *  • -10^4 <= nums\[i] <= 10^4;
 *  • 1 <= windowSize <= nums.length.
 *
 * Final notes:
 *  • solved via [efficient] in 1-1.5h, only knowing the hint to use the mon. sorted stack/deque/queue. Still quite some time!
 *  • took me, like, 5-6 actual RUNS to fix all the errors on 2 basic test cases, but actual submit took 1 attempt.
 *
 * Value gained:
 *  • I FIRST MA FIRST HARD PROBLEM ON LEETCODE, YAAAAY BABYYY!!!
 *  • this hard specimen requires multiple tools to crack it:
 *      1. sliding window pattern;
 *      2. monotonically sorted deque pattern;
 *      3. great skills with array pointers: remembering to +1 and -1 in ALL the RIGHT places; remembering to convert
 *          from index to num per need.
 *      And ofc general good edge cases analysis.
 *  • hypothesis: hard problems usually involve multiple patterns such as that one;
 *  • apparently "monotonically sorted stack" algo pattern generalizes to the "monotonically sorted deque", cool.
 */
class SlidingWindowMaximum {

    /**
     * Problem rephrase:
     * "return the maximum number of each valid sliding window of size [windowSize] traversing the [nums] by moving
     * one position to the right by each iteration".
     *
     * Idea:
     *  - copy and sort ascending the first [windowSize] numbers into `window` LinkedList<Int>(capacity=windowSize);
     *  - maxNumbers = IntArray(size=nums.size)
     *  - maxNumbers[0] = window\[windowSize-1]
     *  - iterate through nums until startInd+windowSize != nums.size (starting with startInd=1):
     *      - remove the first element from the window; (time O(1))
     *      - insert the new element to maintain sort; (time O(windowSize))
     *      - maxNumbers[startInd] = window\[windowSize-1]
     *      - startInd++;
     *  - return maxNumbers.
     *
     * Time: O(n^2) where n=nums.length:
     *  - we make n iterations, for each we insert the new element while maintaining the sort via windowSize iterations
     *      => n*windowSize;
     *  - since windowSize can be any value from 1 to n => windowSize depends on n;
     *  - O(n*windowSize) = O(n^2).
     * Space: O(1) if we don't count allocating the result array.
     *
     * How to improve time to O(n)?
     *
     * We could use IntArray(size=windowSize) instead of the LinkedList and reduce the search for the index to insert
     * down to O(logn) time, but then the insertion itself would take O(windowSize)=O(n) time STILL due to the need to
     * shift the elements from 1 to the insertionInd to the left, so that we both remove the head and make space for the
     * new element (windowSize time cause the number of iterations that would require would be proportional to windowSize).
     * => Array still gives time O(n^2)
     *
     * Since sliding window with a step of 1 is basically a queue (head=nums\[startInd], tail=nums[startInd+windowSize])
     * (remove from head, add to tail) => make use of that somehow? how to find the max of the queue with updates to the
     * head and the tail compared to the previous iteration?
     *
     *
     * Idea:
     *   - add first [windowSize] elements into the dequeue in the monotonically non-increasing order, don't add those that
     *      don't fit that criteria;
     *   - create maxNumbers IntArray(size=nums.size-windowSize)
     *   - maxNumbers[0] = deque.first()
     *   - iterate through startInd=1..nums.size-windowSize:
     *      - if (dequeue.first() == startInd - 1) deque.removeFirst()
     *      - while deque.isNotEmpty() && deque.last() < nums[startInd]:
     *          - deque.removeLast()
     *      - deque.addLast(startInd)
     *      - maxNumbers[startInd] = deque.first()
     *  - return maxNumbers
     *
     * Intuition:
     * ---
     * 1. if the newNumber is greater than M last numbers in the deque, then, for each valid window left to check
     * (that has those M numbers), newNumber will always be in the window alongside them and will always be larger
     * than all those M numbers => discard M numbers from the deque.
     * ---
     * 2. If newNumber is less or equal to the last number of the deque, then keep it, because once all current numbers
     * in the deque are popped due to window being moved => currentNumber will be the max IF numbers in nums\[currentInd:currentInd+windowSize]
     * will all be less than newNumber.
     *
     * DS operations required:
     *  - remove head
     *  - add tail
     *  - peek tail
     *  - remove tail
     * remove from both ends => deque
     *
     * Edge cases:
     *  - nums.size==1 => always return the array itself, could do early return for clarity, but is handled correctly as-is;
     *  - windowSize==1 => works correctly;
     *  - windowSize==nums.size => we add all numbers straight into the deque following the monotonic deque pattern
     *      => we'll skip the loop and return an array of size 1 with the max number amongst them, correct;
     *  - monotonically non-increasing means less OR EQUAL => handled.
     *  - nums are all monotonically non-increasing => we'll never removeLast(), deque is always valid, works correctly.
     *  - nums are all monotonically increasing => at the start of each iteration dequeue contains 1 element, works correctly.
     *
     * Time: O(n), where n=nums.length since the inner while is amortized O(1), cause we pop each number only once;
     * Space: O(n), cause windowSize can be in 1..nums.length, so we could say O(windowSize)=O(n) for the deque.
     */
    fun efficient(nums: IntArray, windowSize: Int): IntArray {
        val indDeque = ArrayDeque<Int>()
        val validWindowsAmount = nums.size - windowSize + 1
        val maxNumbers = IntArray(validWindowsAmount)

        // fill the deque with first windowSize elements, set max for the first window
        for (ind in 0 until windowSize) {
            while (indDeque.isNotEmpty() && nums[indDeque.last()] < nums[ind]) indDeque.removeLast()
            indDeque.addLast(ind)
        }
        maxNumbers[0] = nums[indDeque.first()]

        // find and save max for each valid window starting from the 2nd one
        for (startInd in 1 until validWindowsAmount) {
            if (indDeque.first() == startInd - 1) indDeque.removeFirst()
            val endInd = startInd + windowSize - 1
            while (indDeque.isNotEmpty() && nums[indDeque.last()] < nums[endInd]) indDeque.removeLast()
            indDeque.addLast(endInd)
            maxNumbers[startInd] = nums[indDeque.first()]
        }

        return maxNumbers
    }

    // TODO: simplify, refactor into a single loop
}

fun main() {
    println(SlidingWindowMaximum().efficient(intArrayOf(1, 3, -1, -3, 5, 3, 6, 7), 3).contentToString())
}
