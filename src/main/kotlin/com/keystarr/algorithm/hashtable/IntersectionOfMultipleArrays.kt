package com.keystarr.algorithm.hashtable

/**
 * LC-2248 https://leetcode.com/problems/intersection-of-multiple-arrays/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= nums.length <= 1000;
 *  • 1 <= sum(nums\[i].length) <= 1000;
 *  • 1 <= nums\[i]\[j] <= 1000.
 *
 * Final notes:
 *  • all three solutions have the same asymptotic complexity within ;
 *  • in real cases if input size is randomized than on average [array] would work faster than others, but take more memory;
 *   [hashmap] would work slower but take less memory; [hashset] would take less memory but perform the slowest
 *   due to either GC caused by the creation of the set object every ith iteration or clearing the set after each iteration.
 */
class IntersectionOfMultipleArrays {

    /**
     * Rephrase problem for clarity: "find and return the intersection of N sets".
     *
     * - convert nums[0] into a set, write into `intersection` variable;
     * - iterate through 2D array starting from the 1st index;
     *  - convert the nums\[i] into a set;
     *  - iterate through each number of `intersection`:
     *      - if nums\[i] has it, continue;
     *      - if nums\[i] doesn't have it, remove that number from the intersection.
     *
     * Edge cases:
     *  - no intersection => works fine;
     *  - nums.size = 1, nums\[0].size = 1 => works fine.
     *
     * Time: O(n*m + k*log(k)) = O(n*m)
     *  - where n = nums.size, m = average (?) nums\[i].size, k = size of intersection;
     *  - k*log(k) is at worst 1000*log2(1000)~9000, whereas n*m is at worst 10^6, so k*log(k) asymptotically doesn't matter.
     * Space: O(m), cause we keep only 2 sets at once, each of 1001 numbers max.
     */
    fun hashset(nums: Array<IntArray>): List<Int> {
        var intersectionSet = nums[0].toSet()
        for (i in 1 until nums.size) { // time: O(n)
            val currentSet = nums[i].toSet() // time: O(m)
            val newIntersection = mutableSetOf<Int>()
            intersectionSet.forEach { if (currentSet.contains(it)) newIntersection.add(it) } // time: O(m)
            intersectionSet = newIntersection
        }
        return intersectionSet.sorted() // time: O(k*log(k)
    }

    /**
     * Idea:
     * - since nums\[i]\[j] is within [0,1000] range, lets simply create a hashmap to keep track of the count of
     *  each encountered number;
     * - iterate through all numbers in all arrays, increment count for each number encountered in the hashmap;
     * - at the end, add all numbers that have exactly nums.size count into a list and return it.
     *
     * (might also set hashmap's initial capacity to 1000 to minimize resizing on average)
     *
     * Time: O(n*m + k*log(k)) = O(n*m) with same definitions as [hashset];
     * Space: O(t), where t - the amount of distinct numbers through all arrays, max 1001.
     */
    fun hashmap(numbers2D: Array<IntArray>): List<Int> {
        val numbersCountMap = mutableMapOf<Int, Int>()
        numbers2D.forEach { numbers1D ->
            numbers1D.forEach { number ->
                numbersCountMap[number] = numbersCountMap.getOrDefault(number, 0) + 1
            }
        }

        val result = ArrayList<Int>()
        numbersCountMap.entries.forEach { entry ->
            if (entry.value == numbers2D.size) {
                val pos = (result.binarySearch(entry.key) + 1) * -1
                result.add(pos, entry.key)
            }
        }
        return result
    }

    /**
     * Hints:
     *  - the solution should probably provide sorting of the result by default as a byproduct, or at least allow
     *      to perform it within O(n).
     *
     * Idea - actually we have at most 1000 distinct numbers and no explicit time/memory constraints
     * => simply allocate the array of that size for counting. We don't have to sort at all then.
     *
     * No real edge cases.
     *
     * Time: O(n*m)
     * Space: always O(1001 + t) with definition above [hashmap]
     *
     * Discovered thanks to https://leetcode.com/problems/intersection-of-multiple-arrays/solutions/1976940/java-easy-solution/
     */
    fun array(numbers2D: Array<IntArray>): List<Int> {
        val counters = IntArray(size = 1001)
        numbers2D.forEach { numbers1D -> numbers1D.forEach { counters[it]++ } }
        return mutableListOf<Int>().apply {
            for (i in counters.indices) if (counters[i] == numbers2D.size) add(i)
        }
    }
}

fun main() {
    println(
        IntersectionOfMultipleArrays().hashmap(
            numbers2D = arrayOf(
                intArrayOf(7, 34, 45, 10, 12, 27, 13),
                intArrayOf(27, 21, 45, 10, 12, 13),
            )
        )
    )
}
