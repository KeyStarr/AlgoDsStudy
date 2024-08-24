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

    fun justShuffle(problemSetFilePath: String, outputFilePath: String) {
        val problems = mutableListOf<String>()
        File(problemSetFilePath).forEachLine { line -> problems.add(line) }

        problems.shuffle()
        File(outputFilePath).writer().use { writer -> problems.forEach { writer.write("- $it\n") } }
    }

    /**
     * Context: in leet DSA course at least the bonus practice problems are roughly ordered by difficulty within each topic.
     * I've already tried trivially shuffling them all via [justShuffle] and got burned by hardest problems within topics
     * showing up ahead of the rest.
     * E.g. [com.keystarr.algorithm.deque.stack.monotonic.SumOfSubarrayRanges] is literally built on top of
     * [com.keystarr.algorithm.deque.stack.monotonic.SumOfSubarrayMinimums] and I got them in reverse - needless to say, I failed miserably.
     *
     * Goal: shuffle the problems for interleaving practice benefits, but preserve the order within each topic to optimize difficulty.
     *
     * @param topicsFilePath - a line starting with '//' must precede each topic in the file [topicsFilePath].
     */
    fun preserveOrderByTopics(topicsFilePath: String, outputFilePath: String) {
        var currentTopic = mutableListOf<String>()
        val problemsByTopics = mutableListOf<List<String>>()
        File(topicsFilePath).forEachLine { line ->
            if (line.isBlank()) return@forEachLine
            if (line.startsWith("//")) {
                if (currentTopic.isNotEmpty()) {
                    problemsByTopics.add(currentTopic)
                    currentTopic = mutableListOf()
                }
            } else {
                currentTopic.add(line)
            }
        }
        problemsByTopics.add(currentTopic)

        val shuffled = problemsByTopics.shufflePreserveTopicOrder()
        File(outputFilePath).writer().use { writer -> shuffled.forEach { writer.write("- $it\n") } }
    }

    private fun List<List<String>>.shufflePreserveTopicOrder(): List<String> {
        val leftTopics = mutableListOf<TopicProgress>()
        repeat(size) { leftTopics.add(TopicProgress(topicInd = it, currentInd = 0)) }

        val shuffled = mutableListOf<String>()
        while (leftTopics.isNotEmpty()) {
            val topicInd = leftTopics.indices.random()
            val topic = leftTopics[topicInd]
            val problems = get(topic.topicInd)
            shuffled.add(problems[topic.currentInd++])
            if (topic.currentInd == problems.size) leftTopics.removeAt(topicInd)
        }
        return shuffled
    }

    private class TopicProgress(val topicInd: Int, var currentInd: Int)
}

fun main() {
    ProblemsByTopicShuffler().preserveOrderByTopics(
        topicsFilePath = "/Users/kirillstarostin/Study/AlgoDSStudy/src/main/kotlin/com/keystarr/tool/problemset/leet_bonus_easy_med_topics.txt",
        outputFilePath = "/Users/kirillstarostin/Study/AlgoDSStudy/src/main/kotlin/com/keystarr/tool/shuffled/shuffled_easy_med_preserve_difficulty.txt",
    )
}
