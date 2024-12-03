package com.keystarr.algorithm.other.advent2024

import java.io.File
import kotlin.math.abs

/**
 * https://adventofcode.com/2024/day/2
 *
 * Final notes:
 *  • part 1:
 *   • an interesting set of conditions which made me spent some time trying to make the code look pretty without
 *    duplicating the logic. Observation: first two numbers of the report establish the direction of it (incr/decr) BUT
 *    also we must check these for the "diff at most 3" => we either process first numbers before the loop duplicating the
 *    diff<3 check, or compute the diff between the two numbers twice => I opted for the latter. It could look strange to
 *    maintainers was it production code.
 *   • literally spent 15 mins on this minute stylistic choice, but hey, that could matter in the production.
 *    How are these cases called though, when we have to precompute something or duplicate conditions? It gotta be a pattern or smth
 *
 * Value gained:
 *  •
 */
class RedNosedReports {

    /**
     * problem rephrase:
     *  - given is a list of lists of integers;
     * Goal: compute and return the input metric
     *  metric = how many lists are safe
     *  safe = all numbers in the list are either monotonically increasing or decreasing, and the adjacent diff is at most 3
     *
     * Again, pure implementation. Reports appear to contain no less than 2 levels=numbers.
     */
    fun part1(reports: List<List<Int>>): Int {
        var safeCount = 0
        reports.forEach { report ->
            val wasIncreasing = (report[1] - report[0]) > 0
            for (i in 1 until report.size) {
                val diff = report[i] - report[i - 1]
                if (diff == 0 || abs(diff) > 3) return@forEach
                if (diff > 0 != wasIncreasing) return@forEach
            }
            safeCount++
        }
        return safeCount
    }
}

fun main() {
    val reports = mutableListOf<List<Int>>()
    File(INPUT_FILE_PATH).forEachLine { line -> reports.add(line.split(' ').map { it.toInt() }) }
    println(RedNosedReports().part1(reports))
}

private const val INPUT_FILE_PATH =
    "src/main/kotlin/com/keystarr/algorithm/other/advent2024/RedNosedReports_input.txt"
