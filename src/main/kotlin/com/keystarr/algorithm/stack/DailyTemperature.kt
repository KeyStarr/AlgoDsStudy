package com.keystarr.algorithm.stack

/**
 * LC-739 https://leetcode.com/problems/daily-temperatures/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= temperatures.length <= 10^5;
 *  • 30 <= temperature\[i] <= 100;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • done [efficient] by myself in 50 mins, first time read about the pattern and applied straight away.
 *
 * Value gained:
 *  • practiced applying the "monotonically sorted stack" algo-pattern;
 *  • practiced applying as quickly as possible, while understanding as deeply as necessary, a pattern as soon as
 *   I've read about it;
 *  • thanks to Leetcode DSA course explanation I've realized that we don't need to store the number itself in the stack,
 *      only the index, since, obviously, we can get it by index via O(1) from the original array.
 */
class DailyTemperature {

    /**
     * problem rephrase:
     * "for each temperature\[i] calculate the distance (count of elements) towards closest temperature\[j] such that j>i,
     * if no such j exists, answer is 0. Return the array of results of these computations for all elements in temperature"
     *
     * brute force idea: iterate over temperatures, for each value iterate through values to the right of it and find the
     * one that is greater than it, if it exists, result\[i]=j-i else result\[i]=0
     * (edge cases omitted)
     * time: average/worst O(n^2), where n=temperatures.size
     * space: O(n) if we count the result array.
     *
     * how to improve time to O(n)?
     * use a monotonically non-increasing stack algo pattern
     * (what would be the hint here though in real world scenario? now I just know)
     *
     * Idea:
     *  - create a Stack<Pair<Int,Int>> number to index;
     *  - create result IntArray of size temperatures.size with all values of 0;
     *  - iterate through [temperatures], currentNumber, currentInd:
     *      (the monotonically decreasing property of stack is violated)
     *      - while stack.isNotEmpty() && stack.peek() < currentTemp:
     *          - (number,ind) = stack.pop()
     *          - result[ind] = currentInd - ind
     *      - stack.push(Pair(currentTemp, currentInd));
     *  - return result.
     *
     * Edge cases:
     *  - temperatures.length==1 => always return [0], can do an early return, but works correctly as-is (never peeks);
     *  - some of the values in the middle are the largest decreasing, e.g. [50, 2, 3, 4, 30, 5, 6], here 50 and 30 =>
     *      they never get popped => stay 0 as it's the default array value on init => correct;
     *  - the last value must always be 0 => correct, same, due to default 0 in the array;
     *  - temps\[i]=temp\[i] is valid monotonically decreasing, handled;
     *
     * Time: always O(n), since the inner loop is amortized O(1) cause we pop each temperatures value at most once;
     * Space: average/worst O(n) where n=temperatures.size
     */
    fun efficient(temperatures: IntArray): IntArray {
        val stack = ArrayDeque<Int>()
        val result = IntArray(size = temperatures.size) { 0 }
        temperatures.forEachIndexed { currentInd, currentTemp ->
            while (stack.isNotEmpty() && temperatures[stack.first()] < currentTemp) {
                val poppedTempInd = stack.removeFirst()
                result[poppedTempInd] = currentInd - poppedTempInd
            }
            stack.addFirst(currentInd)
        }
        return result
    }
}
