package com.keystarr.algorithm.greedy

import java.util.PriorityQueue

/**
 * LC-1484 https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= arr.length <= 10^5
 *  - 1 <= arr\[i] <= 10^9
 *  - 0 <= k <= arr.length
 *
 * Final notes:
 *  - I interpreted at first the problem statement "Find the least number of unique integers..", unique meaning that
 *   the answer is the amount of integers with frequency exactly 1! Spent ~40m designing the algorithm for that,
 *   got through 3 failed submissions (!!) and only then realized that actually the goal is to get the number of ALL
 *   DISTINCT numbers in the array... wow.
 *   from there on the solution was obvious, took me like 10-15 mins to design and implement it, 1st time submit.
 *  - actual best wording for the greedy strategy here is simply: "we improve the answer only by removing all duplicates
 *   of a number" => "each move remove all instances of the number with the lowest frequency, if not possible - finish"
 *
 * Value gained:
 *  - dunno even what to do better in the future to avoid misinterpreting the problem like that. In interviews hopefully
 *   the interviewer would catch that and correct me earlier, but they might not. Hm;
 *  - practiced recognizing and designing a greedy algorithm;
 *  - counting => hashmap, practiced that thought-hook. Not an array here cause we have 10^9 elements potentially and
 *   don't know in advance, exactly how many distinct elements there are in the collection (we might end up allocating
 *   the array of 10^9, only to find out there's 1 number with 10^9-1 duplicates, and use exactly one slot of it!
 *   so on average a hashmap here is cleaner to use and probably more optimal even with the hashing bearing down the time const)
 */
class LeastNumberOfUniqueIntegersAfterKRemovals {

    /**
     * Goal: return the minimum number of DISTINCT NUMBERS LEFT in the array after removing exactly [k] elements from [numbers].
     *
     * minimum => try greedy. What would be the locally optimal step?
     * since we reduce the distinct number count only if we remove ALL such equal numbers from the array =>
     * each step remove the number with the minimum occurrences.
     *  (cause it's the shortest path to removing any present number in the array with all its duplicates =>
     *  minimizing the metric)
     *
     * actually we can do even simpler: reduce k by the amount of minimum occurrences. If k is greater => return the
     * count of the distinct numbers left.
     *
     * repeatedly getting minimums = count the occurrences, then sort them ascending and simply iterate through the array.
     * return occurs.size - i
     *
     * Edge cases:
     *  - k == numbers.length => i.o. remove all the numbers => early return 0 for O(1) time (otherwise O(nlogn) time but correct)
     *  - k == 0 => just return occurs.count for O(n) time (instead of O(nlogn) time)
     *  - numbers.length == 1 => k is either 1 or 0, both are handled already.
     *  - k is at most numbers.size => while loop is ok without checking for i out of bounds.
     *
     * Time: O(nlogn) for sorting, worst case is the amount of distinct numbers = n, then O(nlogn), best case is
     *  there's only 1 distinct number (even if n=10^5), then time is O(1).
     * Space: O(n) for the hashmap, worst O(n), best O(1)
     */
    fun suboptimal(numbers: IntArray, k: Int): Int {
        if (k == numbers.size) return 0

        val occursMap = mutableMapOf<Int, Int>()
        numbers.forEach { number ->
            occursMap[number] = occursMap.getOrDefault(number, 0) + 1
        }
        if (k == 0) return occursMap.size

        val occursSorted = occursMap.values.sorted()
        var operationsLeft = k
        var i = 0
        while (operationsLeft > 0 && occursSorted[i] <= operationsLeft) {
            operationsLeft -= occursSorted[i]
            i++
        }
        return occursSorted.size - i
    }

    // TODO: solve via O(n) time

    /**
     * ORIGINALLY BASED ON WRONG ASSUMPTIONS, realized way too deep in and tried to recover => got entangled into this mess only deeper.
     * (read final notes to know how and why)
     *
     * -------------
     *
     * Goal: return the minimum number of unique integers after removing exactly [k] elements from [arr]
     * i.o. remove exactly k integers, such that there are maximum duplicate numbers possible in the array.
     *
     * minimum => try greedy?
     * what would be the locally optimal step?
     *
     * 2 approaches:
     * A. if there are unique numbers, delete them all first, then delete numbers with most occurrences (all within k total operations)
     * B. always delete the number with least occurrences
     *  (naturally first delete all unique numbers)
     * C. like A, but if the occurrence is reduced to 1, if there are operations left, delete the last number straight away.
     *
     * A is better in a case of [4,3,1,1,3,3,2] k=3
     * but B is better in a case of [4,4,3,3,2,2,1,1] k=4
     * C gives the best result in both
     *
     * Total proof for C:
     *  - prioritize removing uniques, cause it directly minimizes the amount of unique numbers without complicating
     *   the environment (the affect of uniques count is directly -1);
     *  - then prioritize numbers with most occurrences to minimize encountering a case when the max number of occurrences
     *   is 2, and we have only 1 operationsLeft (producing a uniqueNumber as the result).
     *
     * Design:
     *  - pre-process: count the occurrence of all numbers in the array, use a hashmap: <Int, Int> for that;
     *      (at most we have 10^5 occurs for a single number => int will do)
     *  - operationsLeft = k
     *  - add all just occurrences into a maxHeap, but don't add those which are == 1, and reduce operationsLeft for each.
     *   if operationsLeft == 0 => count the unique numbers left and return it;
     *  - while(operationsLeft > 0):
     *   - reducedOccurrence = maxHeap.remove() - 1
     *   - operationsLeft--
     *   - if (reducedOccurrence == 1)
     *    - if (operationsLeft == 0) return 1
     *    - else operationsLeft--
     *   - else maxHeap.add(reducedOccurrence)
     *  - return maxHeap.count()
     *
     * Edge cases:
     *  - k == arr.length => i.o. remove all the numbers, so return 0 => works correctly as-is, but could do an early return for O(1) time;
     *  - k == 0 => just count the unique numbers and return the answer, works correctly as-is and for O(n) too though bigger const;
     *  - arr.length == 1 => then k is either 0 or 1, so the answer is either 1 or 0 => works correctly as-is.
     *  - all integers are distinct =>
     *  - min occurrences is greater than k =>
     *
     * Time: average/worst O(nlogn + k * logn), worst k=n => average/worst O(nlogn)
     *  - counting O(n)
     *  - populating the heap O(n*logn)
     *  - numbers removal loop O(k * logn)
     *  - counting the unique numbers O(n)
     * Space: O(n)
     *  - heap is O(n)
     *  - occurrences array is O(n)
     *
     * -----------
     *
     * potential improvements:
     *  - write a custom PriorityQueue that would have maxheap ordering and would support heapify from a collection for O(n)
     *   (the java's default PriorityQueue can unfortunately only do construct the minheap from a collection for O(n) time)
     *   then best time would be O(n) instead.
     *  - could count the unique numbers first via O(n) and if it is more than [k] return k-uniqueCount straight away,
     *   that case would be O(N) time then (now its O(nlogn) due to heap construction)
     */
    fun incorrectWrongAssumption(numbers: IntArray, k: Int): Int {
        val occursMap = mutableMapOf<Int, Int>()
        numbers.forEach { number ->
            occursMap[number] = occursMap.getOrDefault(number, 0) + 1
        }

        val uniqueNumbersCount = occursMap.values.count { it == 1 }
        if (uniqueNumbersCount >= k) return occursMap.size - k

        val maxHeap = PriorityQueue<Int>(Comparator.reverseOrder())
        var operationsLeft = k
        occursMap.values.forEach { occurrence ->
            if (occurrence == 1) {
                if (operationsLeft == 0) return occursMap.size - k
                operationsLeft--
            } else {
                maxHeap.add(occurrence)
            }
        }

        while (operationsLeft > 0) {
            val reducedOccur = maxHeap.remove() - 1
            operationsLeft--
            if (reducedOccur == 1) {
                if (operationsLeft == 0) maxHeap.add(reducedOccur) else operationsLeft--
            } else {
                maxHeap.add(reducedOccur)
            }
        }

        return maxHeap.count()
    }
}

fun main() {
    println(
        LeastNumberOfUniqueIntegersAfterKRemovals().suboptimal(
            numbers = intArrayOf(4,3,1,1,3,3,2),
            k = 3,
        )
    )
}
