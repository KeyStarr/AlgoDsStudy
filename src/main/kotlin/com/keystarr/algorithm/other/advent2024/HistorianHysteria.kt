package com.keystarr.algorithm.other.advent2024

import java.io.File
import kotlin.math.abs

/**
 * https://adventofcode.com/2024/day/1
 *
 * Final notes:
 *  • part 1 - pure implementation, nothing of interest;
 *  • part 2 - pure implementation would work, but found an unnecessary but cute lil optimization via the frequency hash map.
 *
 * Value gained:
 *  • practiced recognizing and implementing the frequency hash map to optimize two lists comparison.
 */
class HistorianHysteria {

    /**
     * Problem rephrase:
     *  - given are two lists of numbers (seemingly integers);
     *  - order isn't guaranteed;
     * Goal: compute and return the input metric
     *  metric = the total distance between the lists
     *         = the sum of abs(1st smallest number of list #1 - 2nd smallest number of list #2) etc through all numbers
     *  lists are guaranteed to be of equal size
     *
     * Pure implementation:
     *  1. parse the input;
     *  2. compute the metric.
     *
     * Runtime complexity doesn't really matter, just use sort.
     */
    fun part1(first: List<Int>, second: List<Int>): Int {
        val firstIterator = first.sorted().iterator()
        val secondIterator = second.sorted().iterator()
        var totalDistance = 0
        while (firstIterator.hasNext()) totalDistance += abs(firstIterator.next() - secondIterator.next())
        return totalDistance
    }

    /**
     * problem rephrase:
     *  Goal: given the same two lists compute another metric.
     *   metric = similarity score = for each number of the left list: totalScore += leftNumber * (number of times it is in the right list)
     *
     * Again, pure implementation. Performance doesn't matter here at all, the simplest approach would work: "iterate through
     *  the first list, for each number iterate through the second list and count the num of times its there"
     *  time O(n*m) space O(1)
     *
     * For funsies we might speed it up - since the left list may contain duplicates (although may not - nothing on that in the statement)
     *  we might pre-compute the right list number occurrences via O(m) time O(m) space and then speed up the search for each
     *  left list number down to O(1)
     *  time O(n+m) space O(m)
     *
     * Might use an array instead of the map by trivially finding the largest number in the input, to further optimize
     * at least the time const - but so not worth it :)
     */
    fun part2(first: List<Int>, second: List<Int>): Int {
        val secondFreqMap = mutableMapOf<Int, Int>()
        second.forEach { secondFreqMap[it] = (secondFreqMap[it] ?: 0) + 1 }

        var similarityScore = 0
        first.forEach { similarityScore += it * (secondFreqMap[it] ?: 0) }
        return similarityScore
    }
}

fun main() {
    val first = mutableListOf<Int>()
    val second = mutableListOf<Int>()
    File(INPUT_FILE_PATH).forEachLine { line ->
        val (num1, num2) = line.split("   ")
        first.add(num1.toInt())
        second.add(num2.toInt())
    }
    println(HistorianHysteria().part2(first, second))
}

private const val INPUT_FILE_PATH =
    "src/main/kotlin/com/keystarr/algorithm/other/advent2024/HistorianHysteria_input.txt"
