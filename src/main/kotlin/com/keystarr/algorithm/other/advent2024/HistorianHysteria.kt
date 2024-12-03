package com.keystarr.algorithm.other.advent2024

import java.io.File
import kotlin.math.abs

/**
 * https://adventofcode.com/2024/day/1
 *
 * Final notes:
 *  • pure implementation, nothing of interest.
 *
 * Value gained:
 *  ---
 */
class HistorianHysteria {

    /**
     * Problem rephrase:
     *  - given are two lists of numbers (seemingly integers);
     *  - order isn't guaranteed;
     * Goal: compute and return the input metric
     *  metric = the sum of abs(the 1st smallest number of the first list - 2nd smallest number of the second list) etc through all numbers
     *  lists are guaranteed to be of equal height
     *
     * Pure implementation:
     *  1. parse the input;
     *  2. compute the metric.
     *
     * Runtime complexity doesn't really matter, just use sort.
     */
    fun solution(first: List<Int>, second: List<Int>): Int {
        val firstIterator = first.sorted().iterator()
        val secondIterator = second.sorted().iterator()
        var totalDistance = 0
        while (firstIterator.hasNext()) totalDistance += abs(firstIterator.next() - secondIterator.next())
        return totalDistance
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
    println(HistorianHysteria().solution(first, second))
}

private const val INPUT_FILE_PATH =
    "src/main/kotlin/com/keystarr/algorithm/other/advent2024/HistorianHysteria_input.txt"
