package com.keystarr.algorithm.graph.tree.trie

/**
 * LC-211 https://leetcode.com/problems/design-add-and-search-words-data-structure/description/
 * difficulty:
 * constraints:
 *  - 1 <= word.length <= 25;
 *  - word in addWord are English lowercase only;
 *  - word in search can also contain at most 2 dots, but English lowercase otherwise.
 *  - at most 10^4 calls to both methods.
 *
 * ----------
 *
 * approach #1 use a HashSet<String>
 *  add -> just add into the set, time O(1)
 *  search -> set.contains, O(1)
 * across all calls :
 *  time always O(n + m), where n=calls to add, m = calls to search
 *  space average/worst O(n), worst is all words to add are unique so n words exactly are stored in the set
 *
 * ops, cant use a set as-is due to the "." rule
 *
 * ----------
 *
 * approach #2 use a Trie
 *  add -> add key into the trie
 *   time O(k)
 *  search -> if there is a node that ends with the word.last() and it is isWordEnd=true => return true
 *   during traversal if word\[i]=='.' => try all directions then.
 *   time: O(n + m)
 *
 * search dar.m
 * darql
 * darkm
 * darbm
 *
 * ----------
 *
 * Final notes:
 *  • 🎉 solved by myself via a Trie in 30 mins;
 *  • 🏆 first time recognizing an efficient Trie solution "in the wild";
 *  • thought about the Trie, then went for the set anyway :D ignored the "." completely, then remembered about it mid-way;
 *  • what a beautiful Trie problem without explicitly mentioning prefixes in the problem statement at all! First time
 *   I see that here.
 *
 * Value gained:
 *  • practiced recognizing and solving a problem on searching the full word efficiently with a customized Trie, implementing
 *   the "any char" mask for the search.
 */
class DesignAddAndSearchWordsDataStructure {

    private val root = TrieNode()

    /**
     * Time: always O(k), where k=word.length
     * Space: average/worst O(k)
     *  worst is we had no words with common prefix with word, so we create k new trie nodes
     */
    fun addWord(word: String) {
        var currentNode = root
        word.forEach { char ->
            val key = char.toKey()
            if (currentNode.neighbours[key] == null) currentNode.neighbours[key] = TrieNode()
            currentNode = currentNode.neighbours[key]!!
        }
        currentNode.isWordEnd = true
    }

    /**
     * edge cases:
     *  - '.' is the last char in the word;
     *  - there no word in the trie but there is one that [word] is but a prefix of.
     *
     * Time: average/worst O(k), where k=word.length
     *  - worst is word\[0]=='.' and we have 26 choices to go (i.o. each added word has different first character, and then they differ)
     *   and we may have the 2nd dot later on, but since we have at most 26 branches, its still a const in the end.
     * Space: O(1)
     *  max depth is max word length + 1 = O(26)
     */
    fun search(word: String): Boolean = dfsSearch(root = root, currentInd = 0, word = word)

    private fun dfsSearch(
        root: TrieNode,
        currentInd: Int,
        word: String,
    ): Boolean {
        if (currentInd == word.length) return root.isWordEnd

        val char = word[currentInd]
        val nextInd = currentInd + 1
        if (char != '.') {
            val key = char.toKey()
            return root.neighbours[key]?.let { nextNode -> dfsSearch(nextNode, nextInd, word) } ?: false
        } else {
            root.neighbours.forEach { nextNode ->
                val isFound = nextNode?.let { dfsSearch(nextNode, nextInd, word) } ?: false
                if (isFound) return true
            }
        }
        return false
    }

    private fun Char.toKey() = this - 'a'

    private class TrieNode(
        val neighbours: Array<TrieNode?> = Array(size = 26) { null },
        var isWordEnd: Boolean = false,
    )
}

fun main() {
    DesignAddAndSearchWordsDataStructure().apply {
        addWord("bad")
        addWord("dad")
        addWord("mad")
        println(search("bad"))
    }
}
