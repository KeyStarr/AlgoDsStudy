package com.keystarr.tool

import java.io.File

/**
 * Context - in Leetcode's DSA course there are bonus problems for practice on each topic.
 * Purpose - shuffle the bonus problems from all course's topics for the interleaving final practice at the end of the course.
 *  Since I practiced all tools specifically one-by-one, I didn't have to recognize which exact pattern to use for algo design,
 *  mostly just how to adapt it to the problem => now is the time to finally learn to recognize exactly when which tool
 *  to use best AND, of course, to practice tools themselves further.
 *
 * problem sets:
 * - leet_bonus_problems_easy_medium.txt - the original Leet DSA course bonus problems easy-medium (except those I've done already + a couple new on top)
 * - leet_bonus_problems_hard.txt - same, but only hard ones.
 */
class ProblemsByTopicShuffler {

    operator fun invoke(problemSetFilePath: String, outputFilePath: String) {
        val problems = mutableListOf<String>()
        File(problemSetFilePath).forEachLine { line -> problems.add(line) }

        problems.shuffle()
        File(outputFilePath).writer().use { writer -> problems.forEach { writer.write("- $it\n") } }
    }
}

fun main() {
    ProblemsByTopicShuffler()(
        problemSetFilePath = "",
        outputFilePath = ""
    )
}
