package com.keystarr.algorithm.deque.priorityqueue.topk

import java.util.*

/**
 * 🚨LC-347 https://leetcode.com/problems/top-k-frequent-elements/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= nums.length <= 10^5
 *  - -10^4 <= nums\[i] <= 10^4
 *  - k in range [1, number of unique elements in the array]
 *  - answer is always unique
 *
 * Final notes:
 *  - cool, just read about the pattern, never done it before (besides just using a heap) and nailed it by myself 1st submit
 *   in 40 mins, including carefully considering everything for the first time;
 *  - indeed, to use the top k heap pattern we must have a finalized state of the elements, because each time the heap
 *   size exceeds k we remove elements (so, if an element state would change mid-run, and we have already deleted it thinking
 *   it was X, we can't go back and modify it again to be Y, that doesn't make sense);
 *  - here the sub-problem to get the finalized elements collection required to count the elements frequency, which is
 *   a typical hashmap use case => so used a hashmap for it for O(n) time and O(n) space;
 *  - then just performed the pattern of the top k heap, which really goes like this:
 *   "if we have a finalized collection of elements and need to get top k elements out of it according to some criteria,
 *   simply add all elements one-by-one into the minHeap => and when heap.size reaches k+1, remove an element (basically
 *   remove the minimum according to the criteria, which is just what we need, since top k means max k elements)"
 *  - what a weird trick though. Intuition tells me it's excessive while it appears that in reality that does allow to
 *   save some time (though I wonder what would benefits really be in real production, if there would be any. Maybe, like,
 *   on billions of data points for something like Facebook?)
 *
 * Value gained:
 *  - I loved it, just learnt about the top k heap pattern and havent even paraphrased it, just when straight ahead and
 *   built it, used it to get the job done => understood it way better. That's the way to go with learning anything
 *   engineering!
 *  - "heap top k" pattern:
 *      - prerequisite: the collection is finalized relating to the optimization criteria
 *      - a bit counterintuitive at first: use a minHeap in order to remove the minimum and finish with top k maximums
 *      - add all elements into the heap, even if the heap size limit is exceeded, but if it was, remove the minimum element
 *       (which may have been updated with the inserted one, but maybe the inserted one actually was one of current top k)
 *      - reduces time O(n*logn) to O(n*logk), which is small but important at least for interviews (and perhaps real
 *      highload production, huge datasets).
 */
class TopKFrequentElements {

    /**
     * Problem rephrase:
     *  - return k elements with the most amount of appearances in the array (IN ANY ORDER)
     *
     * top K elements => use a Heap/PriorityQueue. Priority = number of occurrences of an element in the array,
     * optimization vector: max (maxHeap)
     *
     * uh-huh, we must count occurrences for all number beforehand in order to get the top k occurrences with numbers
     * out of it, can't do it on the fly (cant compare and terminally remove that which we don't know full form of)
     *
     * Idea:
     *  - create a class NumWithOccurrences(val num: Int, var occurs: Int)
     *  - count occurrences of all elements into a map
     *  - create a java.util.PriorityQueue<NumWithOccurrences> with a custom Comparator operating exclusively on `NumWithOccurrences.occurs`
     *   and in the MINIMIZATION vector
     *  - iterate through map: O(n*logk)
     *   - heap.add(num) // time O(logk)
     *   - if (heap.size > k) heap.remove() // remove the minimum element, time O(logk) cause heap.size is at most k
     *  - iterate through the heap, add each number into the result array // O(k*logk), don't need to remove, any order
     *  // (java PriorityQueue's iterator order is not specified, not guaranteed to be the order of removal)
     *  - return result
     *
     * Edge cases:
     *  - nums.length == 1 => early return nums, but its ok, would work as-is in the general case, so correct;
     *  - k == nums.toSet().size => return all distinct elements in the array, correct;
     *  - all elements in nums are unique, so all occur=1, k < nums.size => how do we choose which elements do return then
     *   and which to drop? Which would be considered most frequent then?
     *   No info the description, nobody to ask => assume then any of the elements with same occurrence counts are
     *   valid choices.
     *
     * Time: average/worst O(n*logk), worst is k=n O(nlogn)
     * Space: O(n)
     */
    fun efficient(nums: IntArray, k: Int): IntArray {
        val numberToOccursMap = mutableMapOf<Int, NumberWithOccurrences>()
        nums.forEach { number -> // O(n)
            numberToOccursMap.getOrPut(number) { NumberWithOccurrences(number) }.occurCount++
        }

        val minHeap = PriorityQueue<NumberWithOccurrences> { o1, o2 ->
            if (o1.occurCount == o2.occurCount) 0 else if (o1.occurCount > o2.occurCount) 1 else -1
        }
        numberToOccursMap.values.forEach { // O(n*logk)
            minHeap.add(it)
            if (minHeap.size > k) minHeap.remove()
        }

        val result = IntArray(size = minHeap.size)
        minHeap.forEachIndexed { ind, numWithOccur -> result[ind] = numWithOccur.number }
        return result
    }

    private class NumberWithOccurrences(val number: Int, var occurCount: Int = 0)
}

fun main() {
    println(
        TopKFrequentElements().efficient(
            nums = intArrayOf(1, 1, 1, 2, 2, 3),
            k = 2,
        ).contentToString()
    )
}
