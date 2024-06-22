package com.keystarr.algorithm.graph.implicit

import java.util.*
import java.util.ArrayDeque

/**
 * LC-433 https://leetcode.com/problems/minimum-genetic-mutation/description/
 * difficulty: medium
 * constraints:
 *  - 0 <= bank.length <= 10
 *  - startGene.length == endGene.length == bank\[i].length == 8
 *  - startGene, endGene and bank\[i] consist only of ['A', 'C', 'G', 'T']
 *
 * Final notes:
 *  - done by myself in ~40 mins, but wrote everything extensively, could've done faster;
 *  - recognizing the pattern here was very easy, cool;
 *  - weird that a medium has basically no real edge case here.
 *
 * Value gained:
 *  - practiced recognizing an implicit graph problem;
 *  - practiced BFS on an undirected graph.
 */
class MinimumGeneticMutation {

    private val allowedChars = arrayOf("A", "C", "G", "T")

    /**
     * original problem:
     *  - a gene is represented via a 8-char string, each letter is of [A,C,G,T];
     *  - we're given a startGene string, endGene string and geneBank - an array of valid genes;
     *  - a mutation is a change of 1 character in the startGene string, we might change any character to one of the
     *   valid characters above.
     * Goal: return the minimum number of mutation=changes in the startGene string to achieve the endGene string,
     *  and the string after each mutation(change) must be one of the geneBank string. or -1 if it doesnt exist
     *
     * hints:
     *  - the problem involves changing input by a single modification multiple times to achieve target => transitioning
     *   between states via valid transitions
     *  - goal is to find the fewest amount of operations to come from start to end => fewest amount of transitions
     * => try graph BFS?
     *
     * Problem rephrase:
     *  - we're give an undirected graph, where nodes are the possible gene string states and edges are
     *   changes of 1 letter from the start string to the mutatedString; e.g. AACC -> AACA, AACC -> AAAC etc
     *  - we might traverse only those edges that lead to valid nodes, validity defines such that the node (string)
     *   must be in the geneBank array;
     * Goal: what is the shortest path from [startGene] to [endGene] string, where each node visited is one of geneBank?
     *
     * Idea:
     *  - transform [geneBank] into a HashSet;
     *  - create seen as a HashSet<String>;
     *  - use BFS:
     *      - add to queue as a start node [startGene];
     *      - currentMutations = 0
     *      - repeat(queue.size):
     *          - gene = queue.remove()
     *          - add into the queue all possible mutations of `gene` that are not yet seen and that are contained in geneBankSet,
     *           if one of them == endGene, return currentMutations+1
     *      - currentMutations++
     *  - return -1
     *
     * Edge cases:
     *  - endGene not in geneBankSet => early return -1 (also covers the case of geneBank.size == 0)
     *  - startGene == endGene => return 0
     *
     * Time: generally O((n + e)*g*t + k), actually worst O(10 + 80 + 10) = O(100) = O(1)
     *  n = g^t
     *  e = (g^t)*(g*t-1)
     *  - transform geneBank into a set => at most O(10), in general O(k)
     *  - bfs => average/worst O(n + e),
     *   - worst n = 4^8 but actually 10, cause there are up to 10 valid states only that we're allowed to visit;
     *   - worst e = 4^8 * 8, but then again, maximum that we're allowed to try due to [geneBank] constraint is 10*8=80.
     *  - inside bfs, when computing all possible mutations from the current gene string =>
     *   time O(g*t) for creation of new gene strings, where:
     *      - g = amount of possible characters at one place in the string;
     *      - t = the length of the gene string.
     *     in this problem these are const g=4 t=8 so its O(1).
     * Space: technically O(1)
     *  - geneBank takes O(10), generally O(k)
     *  - seen takes worst O(10), generally O(n)
     *
     * -------
     *
     * Possible optimization: modify only those characters in [startGene] that are actually different from [endGene]?
     *  though theoretically [bank] may be such that actually requires to modify temporarily those characters that ARE in
     *  [startGene] but ARE NOT different from the [endGene]? Yea, then it's wrong.
     */
    fun efficient(startGene: String, endGene: String, bank: Array<String>): Int {
        if (startGene == endGene) return 0

        val validGenesSet = bank.toSet()
        if (!validGenesSet.contains(endGene)) return -1

        val queue: Queue<String> = ArrayDeque<String>().apply { add(startGene) }
        val seen = mutableSetOf<String>().apply { add(startGene) }
        var mutationsCount = 0
        while (queue.isNotEmpty()) {
            val mutationsToProcessSize = queue.size
            repeat(mutationsToProcessSize) {
                val gene = queue.remove()
                repeat(startGene.length) { geneInd ->
                    allowedChars.forEach { char ->
                        val newGene = gene.replaceRange(geneInd, geneInd + 1, char)
                        if (!seen.contains(newGene) && validGenesSet.contains(newGene)) {
                            if (newGene == endGene) return mutationsCount + 1
                            queue.add(newGene)
                            seen.add(newGene)
                        }
                    }
                }
            }
            mutationsCount++
        }

        return -1
    }

    // TODO: is there a way to limit the combinations we check => optimize at least the const? is it even feasible?
    //  cause we may check so many mutations that are not even close to what we seek.
}

fun main() {
    println(
        MinimumGeneticMutation().efficient(
            startGene = "AACCGGTT",
            endGene = "AAACGGTA",
            bank = arrayOf("AACCGGTA", "AACCGCTA", "AAACGGTA"),
        )
    )
}