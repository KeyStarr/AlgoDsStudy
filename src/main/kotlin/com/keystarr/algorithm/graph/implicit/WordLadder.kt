package com.keystarr.algorithm.graph.implicit

import java.util.*

/**
 * LC-127 https://leetcode.com/problems/word-ladder/description/
 * difficulty: hard
 * constraints:
 *  • 1 <= beginWord.length <= 10
 *  • endWord.length == beginWord.length
 *  • 1 <= wordList.length <= 5000
 *  • wordList\[i].length == beginWord.length
 *  • beginWord, endWord and worldList\[i] consist of lowercase English
 *  • beginWord != endWord
 *  • all words in wordList are unique
 *
 * Final notes:
 *  • done by myself in ~1h;
 *  • recognized states and transitions as a graph straight away (had a hint, since it was implicit graphs starting session),
 *   but the trickiest part turned out to be the sub-problem of efficiently for each word X finding all its neighbors,
 *   that is, words for which valid transitions from X exist (seemed far easier at first).
 *
 * Value gained:
 *  • practiced solving an implicit graph problem via BFS;
 *  • it seems that multiple implicit graph problems have a single peculiar sub-problem that takes the most effort and
 *   makes it unique. Here its preprocessing input into a map graph, in [DetonateTheMaximumBombs] it was the Euclidean
 *   distance, determining whether a point is in a circle
 *   => so it is extremely important to have that quick pattern recognition, and key patterns themselves at one's fingertips,
 *   cause in a live interview scenario most of the effort then could be directed into explaining the process, doing everything
 *   fast and figuring out that unique complexity. Otherwise, there wouldn't be enough time!
 */
class WordLadder {

    // TODO: is that an efficient solution? Submitted successfully, but postponed checking the editorial thoroughly

    /**
     * original problem:
     *  - given: beginWord, endWord and wordList
     *  - a valid transformation is going from the beginWord to some word in wordList, only if those two words are different
     *   by exactly 1 character
     *  Goal: return the number of words along the shortest transformation sequence from beginWord to the endWord or 0
     *
     * states + transformations + goal as the shortest path => graphs BFS
     *
     * rephrase as a graph problem:
     *  - given: an undirected unweighted graph with possible multiple connected components;
     *  - node=word, edge=a valid transformation from the word to another, valid = different by exactly 1 character + targetWord from wordList
     * Goal: return the number of nodes along the shortest path from beginWord to endWord or 0 if it doesn't exist
     *
     * the TRICK is:
     * how to understand to which words from wordList exactly we can go from the current word?
     * preprocess wordList and beginWord into map node->neighbors Map<String, List<String>>???
     *
     * how to understand if two strings differ by exactly 1 character?
     * compare them via O(k) time by iterating through both and counting exactly by how many characters these are different,
     * where k=beginWord.length
     * (cut at least the const of the time by, if two strings match, adding 2 edges, and starting the inner loop from j=i+1)
     *
     * and for a single string to compare with all others would be O(n*k) time
     * the entire preprocessing would take then O(n*n*k) time
     *
     * Solution:
     *  - if endWord not in wordList => return -1
     *  - preprocess all strings into the node->neighbors Map<String, List<String>>
     *  - seen = HashSet<String>().apply { add(beginWord) }
     *  - do the bfs with a queue, pre-add beginWord there as well. Count the distance, once the neighbor equals endWord,
     *   return distance + 1 before even adding it into the queue;
     *  - return -1
     *
     * Edge cases:
     *  - beginWord is always considered valid, ok
     *  - endWord is not in wordList => return 0
     *  - wordList.length == 1 => if its not the end word, 0, would work correctly though as-is
     *
     * Time: average/worst O(k*n^2 + n + e), worst k=10, worst e=n^2 => O(n^2)
     *  - preprocessing: always O(n*n*k), where n=wordList.size
     *  - bfs: average/words O(n+e), worst e=n^2
     * Space: worst/average O(n^2)
     *  - map nodeToNeighbors: worst/average O(n+e), worst e=n^2 so worst/average O(n^2)
     *  - seen: worst/average O(n)
     */
    fun solution(beginWord: String, endWord: String, wordList: List<String>): Int {
        wordList.find { it == endWord } ?: return NO_VALID_ANSWER_CODE

        val nodeToNeighbors = mutableMapOf<String, MutableList<String>>()
        for (i in 0 until wordList.size - 1) {
            val word = wordList[i]
            val neighbors = wordList.getNeighbors(word, startFrom = i + 1)
            if (neighbors.isEmpty()) continue
            nodeToNeighbors.getOrPut(word) { mutableListOf() }.addAll(neighbors)
            neighbors.forEach { neighbor -> nodeToNeighbors.getOrPut(neighbor) { mutableListOf() }.add(word) }
        }

        // beginWord may or may not be in [wordList]
        if (nodeToNeighbors[beginWord] == null) {
            val beingWordNeighbors = wordList.getNeighbors(beginWord, startFrom = 0)
            nodeToNeighbors[beginWord] = beingWordNeighbors
        }

        val seen = mutableSetOf<String>().apply { add(beginWord) }
        val queue: Queue<String> = ArrayDeque<String>().apply { add(beginWord) }

        var nodesUsed = 1
        while (queue.isNotEmpty()) {
            val currentDistanceNodes = queue.size
            repeat(currentDistanceNodes) {
                val node = queue.remove()
                val neighbors = nodeToNeighbors[node]
                neighbors?.forEach { neighbor ->
                    if (seen.contains(neighbor)) return@forEach
                    if (neighbor == endWord) return nodesUsed + 1
                    queue.add(neighbor)
                    seen.add(neighbor)
                }
            }
            nodesUsed++
        }

        return NO_VALID_ANSWER_CODE
    }

    private fun List<String>.getNeighbors(targetWord: String, startFrom: Int): MutableList<String> {
        val neighbors = mutableListOf<String>()
        outerLoop@ for (i in startFrom until size) {
            val currentWord = get(i)
            var hasDiff = false
            for (j in targetWord.indices) {
                if (targetWord[j] != currentWord[j]) {
                    if (hasDiff) continue@outerLoop else hasDiff = true
                }
            }
            if (hasDiff) neighbors.add(currentWord)
        }
        return neighbors
    }

}

private const val NO_VALID_ANSWER_CODE = 0

fun main() {
    println(
        WordLadder().solution(
            beginWord = "hit",
            endWord = "cog",
            wordList = listOf("hot", "dot", "dog", "lot", "log", "cog"),
        )
    )
}
