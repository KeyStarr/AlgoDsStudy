package com.keystarr.algorithm.graph.tree.trie

/**
 * LC-208 https://leetcode.com/problems/implement-trie-prefix-tree/description/
 * difficulty: medium
 * constraints:
 *  - 1 <= word.length <= prefix.length <= 2 * 10^3;
 *  - word and prefix are only lowercase English;
 *  - at most 3 * 10^4 calls in total to [insert], [search] and [startsWith].
 *
 * Final notes:
 *  -
 *
 * Value gained:
 *  -
 */
class ImplementTrie {

    private val root = TrieNode()

    /**
     * Time: always O(n), where n=[word].length
     * Space: average/worst O(n)
     *  best case there's already a word with full [word] as a prefix => we take no extra space
     *  worst case is there's no prefix at all with [word] in the trie
     *  average is that the nodes we have to insert just depend on n
     */
    fun insert(word: String) {
        var currentNode = root
        word.forEach { char ->
            if (!currentNode.children.contains(char)) currentNode.children[char] = TrieNode()
            currentNode = currentNode.children.getValue(char)
        }
        currentNode.doesEndWord = true
    }

    /**
     * edge cases:
     *  - we have a word which contains [word] as its prefix, but no actual separate word [word] => expected is return false
     *   => we can't just traverse the tree and return true if there's a node for each char in [word], we must somehow
     *    efficiently retrieve actual words we have at a given prefix.
     *   => since we don't need the word itself, and only tell if it exists or not => just store a bool denoting whether
     *    a given char ends an actual inserted word.
     *
     * Time: O(n)
     * Space: O(1)
     */
    fun search(word: String): Boolean = findLastCharNode(word)?.doesEndWord == true

    /**
     * Time: O(n)
     * Space: O(1)
     */
    fun startsWith(prefix: String): Boolean = findLastCharNode(prefix) != null

    private fun findLastCharNode(word: String): TrieNode? {
        var currentNode = root
        word.forEach { char -> currentNode = currentNode.children[char] ?: return null }
        return currentNode
    }

    private class TrieNode(
        val children: MutableMap<Char, TrieNode> = mutableMapOf(),
        var doesEndWord: Boolean = false,
    )
}

fun main() {
    ImplementTrie().apply {
        insert("mousepad")
        println(search("mousepad"))
        println(search("mouse"))
        println(startsWith("m"))
        println(startsWith("k"))
    }
}
