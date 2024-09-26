package com.keystarr.algorithm.dp.other

/**
 * ⭐️ a curious example of a string-based DP problem with a DP+Trie and a simpler distinct solution paths
 * LC-139 https://leetcode.com/problems/word-break/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= text.length <= 300;
 *  • 1 <= words.size <= 10^3;
 *  • 1 <= words\[i].size <= 20;
 *  • text and words consist of only lowercase English;
 *  • all values in words are unique.
 *
 * Final notes:
 *  • designed Trie + DP solution in 28 mins;
 *  • fully done via [topDownDp] in 56 mins;
 *  • first approach actually is viable (albeit slower), just do a DP over it;
 *  • apparently there are multiple approaches to this problem, and there are simpler ones than what I did with [topDownDp]
 *   DP and a Trie. But decided to leave it for the future, since current solution I think would've been adequate in an
 *   interview setting.
 *
 * Value gained:
 *  • practiced solving a "find any valid string split combination" type problem using a Trie and top-down DP.
 */
class WordBreak {

    // TODO: also solve via a "words-dict-first" approach, apparently its a bit more efficient and simpler
    //  e.g. https://www.youtube.com/watch?v=Sx9NNgInc3A&ab_channel=NeetCode

    /**
     * problem rephrase:
     *  given:
     *   - text: String
     *   - words: List<String>
     *  goal: return true if [text] can be split such that each resulting substring is in [words].
     *   (duplicates are ok)
     *
     * trivial approach:
     *  - pre-process [words] into HashSet<String>;
     *  - iterate through [text], use StringBuilder to builder the current string, append each new char to it;
     *  - after each append check, if wordSet.contains(currentText) => split, i.o. clear current string builder;
     *  - if after terminating we still have any chars in the builder => return false, otherwise true.
     *
     * Time: X
     * Space: X
     *
     * WRONG, since as we've proven below that we can't just greedily split at the first valid word.
     *
     * ---------- more efficient approach: try using a Trie?
     *
     *  note that we basically want to see whether the currently built word (as we go through the [text]) is in the words
     *  => instead of, at each step, building the String out of the StringBuilder we can simply advance through the Trie,
     *   and actually if there's no valid next node available, we may even then early return false.
     *
     *  Trie:
     *  - pre-process [words] into a Trie;
     *  - iterate through [text], use currentNode pointer to the current trie node;
     *  - instead of appending, for each new char of [text] try to find it as the next available node in the Trie
     *   => if it doesn't exist, return false, otherwise advance;
     *   - if the word does exist at the current node => "finish" the word and reset the currentNode pointer?
     *    WAIT, WHAT IF actually we cut too early, and there was a longer word in [text], AND since we cut at the shorter
     *    valid word => there is now no valid word to use the remainder of the would-be longer word characters?
     *
     *   => perhaps, if currentNode ends on a valid word, store it into some variable, and if further we end with no valid word
     *    => cut at that last valid point and scroll back the currentNode pointer?
     *
     *  hmm, what if actually we have a substring of text such that there are multiple valid words in it of varying length? (all starting at [0])
     *   is it always correct to reset back to the longest one basically when we can't proceed any further?
     *   example?
     *
     *  text = sweetcodeliker
     *  words=["sweet","sweetcode","sweetcodelike", "codeliker"]
     *
     *  if we'd just continue the longest, then split when we cant at the longest encountered word, we'd take "sweetcodelike"
     *  and end up with the unusable "r". However, if we'd split at "sweet" we could use "codeliker" for the rest.
     *
     *  there can be examples when in a similar case it would be possible only to split then at "sweetcode" or even "sweetcodelike"
     *
     * => at each word we encounter while traversing we have a choice to make, and each choice impacts further choices
     * => try DP?
     * => do subproblems overlap?
     *
     *  try an example
     *
     *  text = sweetcodeliker
     *  words=["sweet","code","like","sweetcode","sweetcodelike", "codeliker"]
     *
     *  we can split at "sweet", then "code" and end up with text="liker" and currentNode=root
     *  or split at "sweetcode" and end up with text="liker" and currentNode=root
     *  => subproblems do overlap
     *
     *  ??but could subproblems overlap with currentNode!=root??
     *
     * yes, try DP
     *
     * -----
     *
     * DP goal: try the original goal
     *
     * input state:
     *  startInd: Int - denoting the input string as the substring of text[startInd:]
     *  currentNode: TrieNode - denoting at which Trie node we currently are (val == the previous valid char of the current substring)
     *   => how many nodes we've processed, basically how far we've advanced the current substring of text without splitting.
     *
     * recurrence relation:
     *  try to minimize choices=work at each state
     *  basically we either take each char into the current string or try splitting if its possible, and return true if either is true
     *
     *  dp(startInd) =
     *    split = if (currentNode.isWordEnd) dp(startInd = startInd+1, currentNode = root) else false
     *    continue = if (currentNode.neighbors[text[startInd]] != null) dp(startInd = startInd+1, currentNode = currentNode.neighbors[char]) else false
     *
     * base cases:
     *  - startInd == nums.size => return currentNode.isWordEnd:
     *   - if we're out of text chars and the current trie node is a valid word => we can split and be done;
     *   - if its not a word, we can't split => this combination yields false, no valid full split into the valid words.
     *
     * Time: average O(n*k + n*m) = O(n*(k+m)), n=text.length and m=words.size
     *  worst O(n*k + 2^n)
     *
     *  - building the Trie is O(n*k), where k=average word length;
     *  - we have up to n*m states, at each state we do O(1) work
     *   we have the decision tree of worst height n, the worst width is when at each node we can both take the split or
     *   continue => worst 2^n states.
     *
     * Space: average/worst O(m*k)
     * - Trie takes: worst is none strings in [words] have common prefixes, then Trie holds O(m*k) characters;
     * - worst dp dfs tree height is n.
     *
     * while running on the example caught the bug of not checking whether we can split based on whether the root has
     *  current char as the next char.
     *
     * --------------- optimization
     *
     * bottom-up would be weird here at least without changing directions / recurrence equation, since we'd have to
     *  do inner loop through all the trie nodes, basically do a dfs there, and on average we won't have a lot of states
     *  that we would check then => I estimate this will surpass even the time const win of iterative vs recursive callstack
     * => with the given DP direction and state formula top-down is the better approach, at least certainly time const wise
     *
     * BUT bottom-up can be space-optimized since for the computation of any given startInd we only need the startInd+1
     * => we can use 2 rows and reduce space to O(m).
     */
    fun topDownDp(text: String, words: List<String>): Boolean {
        val trieRoot = words.toTrie()
        return topDownDpRecursive(
            startInd = 0,
            currentNode = trieRoot,
            cache = Array(size = text.length) { mutableMapOf() },
            trieRoot = trieRoot,
            text = text,
        )
    }

    private fun List<String>.toTrie(): TrieNode {
        val root = TrieNode()
        var currentNode = root
        forEach { word ->
            word.forEach { char ->
                val charKey = char.toKey()
                if (currentNode.neighbors[charKey] == null) currentNode.neighbors[charKey] = TrieNode()
                currentNode = currentNode.neighbors[charKey]!!
            }
            currentNode.isWordEnd = true
            currentNode = root
        }
        return root
    }

    private fun topDownDpRecursive(
        startInd: Int,
        currentNode: TrieNode,
        cache: Array<MutableMap<TrieNode, Boolean>>,
        trieRoot: TrieNode,
        text: String,
    ): Boolean {
        if (startInd == text.length) return currentNode.isWordEnd

        val cachedResult = cache[startInd][currentNode]
        if (cachedResult != null) return cachedResult

        val newCharKey = text[startInd].toKey()
        val nextProceedNode = currentNode.neighbors[newCharKey]
        val nextInd = startInd + 1
        val proceed = if (nextProceedNode != null) {
            topDownDpRecursive(
                startInd = nextInd,
                currentNode = nextProceedNode,
                cache, trieRoot, text,
            )
        } else false

        if (proceed) return true.also { cache[startInd][currentNode] = true }

        val nextSplitNode = trieRoot.neighbors[newCharKey]
        val split = if (currentNode.isWordEnd && nextSplitNode != null) {
            topDownDpRecursive(
                startInd = nextInd,
                currentNode = nextSplitNode,
                cache, trieRoot, text,
            )
        } else false

        return split.also { cache[startInd][currentNode] = it }
    }

    private fun Char.toKey() = this - 'a'

    private class TrieNode(
        val neighbors: Array<TrieNode?> = Array(size = 26) { null },
        var isWordEnd: Boolean = false,
    )
}

fun main(){
    println(
        WordBreak().topDownDp(
            text = "leetcode",
            words = listOf("leet", "code"),
        )
    )
}
