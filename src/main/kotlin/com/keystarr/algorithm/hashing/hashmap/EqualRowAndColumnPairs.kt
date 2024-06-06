package com.keystarr.algorithm.hashing.hashmap

import kotlin.math.min

/**
 * LC-2352 https://leetcode.com/problems/equal-row-and-column-pairs/description/
 * difficulty: medium
 * constraints:
 *  • n == grid.length == grid\[i].length;
 *  • 1 <= n <= 200;
 *  • 1 <= grid\[i]\[j] <= 10^5;
 *  • no explicit time/space.
 *
 * Value gained:
 *  • verified by experience that when using arrays in keys in a HashMap, one shouldn't use array's hash, for
 *      then the program is incorrect by design => when collision happens, there is a bug (and it will inevitably happen).
 *      => turn array into a string instead!
 */
class EqualRowAndColumnPairs {

    /**
     * Row ith = grid\[i];
     * Column jth = [grid\[0]\[j],grid\[1]\[j],..,grid\[n-1]\[j]].
     *
     * Problem rephrase:
     * "count pairs such that a row equals to a column, count each distinct pair only 1".
     *
     * Counting => hashmap?
     *
     * Tools: Pre-computation, HashMap
     *
     * Idea:
     *  - iterate through the grid, hash contents of each array (row), add into the hashmap by Int->Int, hash->occurrences count
     *      (to count all occurs as pairs if a matching column is found); (time O(n^2))
     *  - iterate through columns: (time O(n))
     *      - add all column elements into a temp IntArray, hash it; (time
     *      - if map contains the hash => pairsCount+=map\[columnHash];
     *      - return pairsCount.
     *
     * Edge cases:
     *  - n=1 => one element, representing both a column and a row => works correctly, always return 1 (pair count),
     *      may do early return, may leave as-is;
     *  - n=2 => works correctly, nothing special;
     *  - fits into int, no sum/multiply.
     *
     * Time: always O(n^2)
     * Space: average/worst O(n)
     *
     * SUBMIT RESULT - FAILED 1 TEST CASE due to a hash collision!
     */
    fun plainHash(grid: Array<IntArray>): Int {
        val rowHashToOccurMap = mutableMapOf<Int, Int>()
        grid.forEach { row ->
            val rowHash = row.contentHashCode()
            rowHashToOccurMap[rowHash] = rowHashToOccurMap.getOrDefault(rowHash, 0) + 1
        }

        val columnTemp = IntArray(size = grid.size)
        var pairsCount = 0
        for (i in grid.indices) {
            for (j in grid.indices) columnTemp[j] = grid[j][i]

            val columnHash = columnTemp.contentHashCode()
            pairsCount += rowHashToOccurMap[columnHash] ?: 0
        }

        return pairsCount
    }

    /**
     * Same as [plainHash] but instead of using a plain array hash add a `k` elements of the original array as a prefix
     * to the hash => decrease the chance of collisions.
     *
     * As I explained in detail:
     * https://leetcode.com/problems/equal-row-and-column-pairs/solutions/5193215/funny-clean-kotlin-hashmap-time-o-n-2-space-o-n-2/
     * This program is still incorrect by design, because in real prod the collision will inevitably happen, and that
     * would give a guaranteed bug (incorrect pair count!).
     *
     * So for real prod, probably always, a win of space with a factor of n is not worth it here.
     *
     * ACCEPTED
     */
    fun hashWithPrefix(grid: Array<IntArray>): Int {
        val rowHashToOccurMap = mutableMapOf<String, Int>()
        val prefixSize = min(grid.size, 10)
        grid.forEach { row ->
            val rowHash = row.toSmartKey(prefixSize)
            rowHashToOccurMap[rowHash] = rowHashToOccurMap.getOrDefault(rowHash, 0) + 1
        }

        val columnTemp = IntArray(size = grid.size)
        var pairsCount = 0
        for (i in grid.indices) {
            for (j in grid.indices) columnTemp[j] = grid[j][i]

            val columnHash = columnTemp.toSmartKey(prefixSize)
            pairsCount += rowHashToOccurMap[columnHash] ?: 0
        }

        return pairsCount
    }

    private fun IntArray.toSmartKey(prefixSize: Int) = copyOfRange(0, prefixSize).contentToString() + contentHashCode()

    /**
     * Same as [hashWithPrefix] except for keys - we use an apparently classic trick here and convert the arrays to
     * strings.
     *
     * Time: O(n^2)
     * Space: O(n^2) cause we store `n` rows as strings each with a length of `n` as keys in the array.
     *
     * ACCEPTED
     */
    fun rowContentsAsString(grid: Array<IntArray>): Int {
        val rowToOccurMap = mutableMapOf<String, Int>()
        grid.forEach { row ->
            val rowStr = row.contentToString()
            rowToOccurMap[rowStr] = rowToOccurMap.getOrDefault(rowStr, 0) + 1
        }

        val columnTemp = IntArray(size = grid.size)
        var pairsCount = 0
        for (i in grid.indices) {
            for (j in grid.indices) columnTemp[j] = grid[j][i]

            val columnStr = columnTemp.contentToString()
            pairsCount += rowToOccurMap[columnStr] ?: 0
        }

        return pairsCount
    }
}

fun main() {
    println(
        EqualRowAndColumnPairs().plainHash(
            arrayOf(
                intArrayOf(2, 1),
                intArrayOf(3, 32),
            )
        )
    )
}
