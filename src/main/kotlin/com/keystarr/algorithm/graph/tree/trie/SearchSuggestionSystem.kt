package com.keystarr.algorithm.graph.tree.trie

import java.util.PriorityQueue
import kotlin.math.min

/**
 * LC-1268 https://leetcode.com/problems/search-suggestions-system/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= products.length <= 10^3;
 *  - 1 <= products\[i].length <= 3 * 10^3;
 *  - 1 <= sum(products\[i].length) <= 2*10^4;
 *  - 1 <= searchWord.length <= 10^3;
 *  - all the strings of products are unique;
 *  - products\[i] and searchWord consist of lowercase English.
 *
 * Final notes:
 *  - failed a run didn't catch that thing that 3 mins within the result must be sorted lexicographically => the problem
 *   statement said return "return the three lexicographically minimums products", but didn't specify the order of these!
 *   in a live interview I would better clarify that, which order is required;
 *  - 1st submit fail => missed an obvious case with a word's length being less than searchWord's length, a smart optimization
 *   I did for building a trie 🙂;
 *  - 2nd submit fail => missed an obvious edge case, after Xth char no common prefix for the searchWord in words
 *   => per the problem statement, add emptyList() to the results.
 *
 * Value gained:
 *  - 🏆 first problem I've ever solved with a Trie, wo-hoo!
 *  - practiced using a Trie with pre-processing, data at each node being an actual result, should that node be required
 *   in the final answer.
 */
class SearchSuggestionSystem {

    // TODO: understand and implement just the heap solution https://leetcode.com/problems/search-suggestions-system/solutions/471718/java-priority-queue-no-sort-or-trie/
    //  (or the one in the top bars of time)

    /**
     * find words with common prefix to target across multiple options => try a Trie (:D)
     *
     * goal:
     *  - return a list of lists, where answer\[i] represents the best valid words with common prefix with searchWord[0:i];
     *  - if there are more than 3 matching words => put into answer\[i] three minimum words lexicographically.
     *
     * design:
     *  - build the trie, but only up to [searchWord] depth;
     *   at each node store the answer => 3 lexicographically minimum words with prefix matching to the word up to current node
     *  - iterate through chars in searchWord, from each trie node found add `suggestions` to `results`. If not trie node
     *   is found => add emptyList() to results.
     *  - return results
     *
     * edge cases:
     *  - max lex value of a word => max 'z' chars (ascii code 122) => 3 * 10^3 * 122 => 3.66*10^5 => fits into int
     *  - after ith character in [searchWord] there are no more strings matching the prefix in the trie =>
     *   we must add the emptyList() to results;
     *  - there are no strings in [products] that have any common prefix with [searchWord] => a special case of the case
     *   above => correct as-is.
     *
     * Time: always O(n*k+g)
     *  - building a trie O(n*k*logm)=O(n*k), where n=words.length, k=average word length, m=max matches constraint, here its const 3
     *   (logm for adding into the heap)
     *  - main loop O(g*m*logm) = O(g) with m being at most 3, where g=searchWord.length
     * Space: average/worst O(n*k)
     *  - total characters count O(n*k)
     */
    fun trie(products: Array<String>, searchWord: String): List<List<String>> {
        val root = products.buildTrie(maxChars = searchWord.length)
        var currentNode: TrieNode? = root
        val results = mutableListOf<List<String>>()
        searchWord.forEach { currentChar ->
            currentNode = currentNode?.children?.get(currentChar)
            results.add(currentNode?.bestSuggestions?.sorted() ?: emptyList())
        }
        return results
    }

    private fun Array<String>.buildTrie(maxChars: Int) = TrieNode().also { root ->
        forEach { word ->
            var currentNode = root
            for (i in 0 until min(word.length, maxChars)) {
                val char = word[i]
                if (currentNode.children[char] == null) currentNode.children[char] = TrieNode()
                currentNode = currentNode.children.getValue(char)

                currentNode.bestSuggestions.add(word)
                if (currentNode.bestSuggestions.size > 3) {
                    currentNode.bestSuggestions.remove()
                }
            }
        }
    }

    /**
     * skipped implementation, Trie is better, but left for future retro
     *
     * ---------
     *
     * - sort the words lexicographically ascending;
     * - create new list results
     * - iterate through the characters in the \[searchWord]:
     *  - create new list result
     *  - iterate through all words:
     *   - if word\[i] == target\[i]
     *    - result.add(word)
     *   - if result.size == 3:
     *    - break
     *   - results.add(result)
     *
     * Time: O(n*m+n*k*log(n*k))
     *  - sorting O(n*k*log(n*k)), where n=words.size, k=average word length
     *  - main loop average/worst O(m*n), where m=searchWord.length
     *      best is just always 3 first words, then its O(m), but on average the amount of words to try would depend on n
     * Space: O(1) if we don't count results
     */
}

private data class TrieNode(
    val bestSuggestions: PriorityQueue<String> = PriorityQueue { o1, o2 -> o2.compareTo(o1) }, // for top k mins heap pattern
    val children: MutableMap<Char, TrieNode> = mutableMapOf(),
)

fun main() {
    println(
        SearchSuggestionSystem().trie(
            products = arrayOf("bags", "baggage", "banner", "box", "cloths"),
            searchWord = "bags",
        )
    )
}
