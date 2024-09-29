package com.keystarr.tool

import java.io.File

class StatsCalculator {

    /**
     * idea: return the number of problems of each type of difficulty solved for each pattern
     *  - take difficulty from the problem class doc comment "difficulty :";
     *  - pattern = name of the package
     *
     * => print the prettified stats to console for now, check if that's useful/fun
     */
    operator fun invoke(runFilePath: String): RunStats {
        var cleanSolves = 0
        var almostClean = 0
        var dirty = 0
        var fails = 0
        var total = 0
        File(runFilePath).forEachLine { line ->
            if (line.isBlank() || !validFirstChars.contains(line.first())) return@forEachLine

            val evaluationSet = mutableSetOf<Char>()
            var ind = 0
            while (line[ind] != ' ') evaluationSet.add(line[ind++])

            if (evaluationSet.size == 1) {
                when {
                    evaluationSet.contains('+') -> cleanSolves++
                    evaluationSet.contains('~') -> fails++
                }
            } else {
                when (line.first()) {
                    '+' -> almostClean++
                    '~' -> dirty++
                }
            }
            total++
        }
        return RunStats(
            total = total,
            cleanSolves = cleanSolves,
            almostClean = almostClean,
            dirty = dirty,
            fails = fails,
        )
    }

    private val validFirstChars = setOf('-', '+', '~')
}

data class RunStats(
    val total: Int,
    val cleanSolves: Int,
    val almostClean: Int,
    val dirty: Int,
    val fails: Int,
) {
    private val done: Int
        get() = cleanSolves + almostClean + dirty + fails

    private val remaining: Int
        get() = total - done

    private val good: Int
        get() = cleanSolves + almostClean

    private val bad: Int
        get() = dirty + fails

    private val hardLearning: Int
        get() = almostClean + bad

    override fun toString(): String {
        return """
            ---- run stats ----
            problems: $total
            progress: 
              done: ${percent(done, total)} ($done)
              todo: ${percent(remaining, total)} ($remaining)
            done:
              good: ${percentDone(good)} ($good)
                clean: ${percentDone(cleanSolves)} ($cleanSolves)
                almostClean: ${percentDone(almostClean)} ($almostClean) 
              to improve: ${percentDone(bad)} ($bad)
                dirty: ${percentDone(dirty)} ($dirty)
                failed: ${percentDone(fails)} ($fails)
            learning value: ${percentDone(hardLearning)} ($hardLearning) 
            ---
        """.trimIndent()
    }

    private fun percentDone(part: Int) = percent(part, total = done)

    private fun percent(part: Int, total: Int) = "${((part.toFloat() / total) * 100).toInt()}%"
}

fun main() {
    println(
        StatsCalculator().invoke(
            runFilePath = "src/main/kotlin/com/keystarr/tool/runs/1_leet_bonus_easy_medium.txt",
        )
    )

    // TODO: implement also stats by topics - for the final stats of the run (where had most struggles => learned the most)
}
