package com.keystarr.algorithm.graph.backtracking

/**
 * LC-39 https://leetcode.com/problems/combination-sum/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= candidates.length <= 30
 *  • 2 <= candidates\[i] <= 40
 *  • all elements in candidates are distinct;
 *  • 1 <= target <= 40;
 *  • there will be no greater than 150 elements in answers for any test case.
 *
 * Final notes:
 *  • cool, same basic backtracking pattern;
 *  • yet again, can't straight away understand/prove the time complexity.
 *
 * Value gained:
 *  • practiced backtracking on a generation problem.
 */
class CombinationSum {

    /**
     * given: array of distinct integers [candidates], [target]
     * goal: generate and return all possible combinations of [candidates] such that each sums up to [target]. Combinations
     *  are different if at least one of [candidates] frequency is different => order doesn't matter, only frequency
     *  (same frequencies, different order => duplicates, return only one such)
     *
     * (why candidates are strictly more than 1???? possible hint)
     *
     * generation => try backtracking?
     *  - what is the current state?
     *      - current: currentCombination AND currentSum;
     *      - traverse forward => add the next node's value to currentSum and that number to currentCombination;
     *      - backtrack => only remove that number from currentCombination.
     *  - where to traverse = what are neighbors? how to avoid duplicate combinations? how to consider same digit multiple times for the sum?
     *      - iterate through all [candidates] starting ONLY FROM currentInd, and call backtrack for each.
     *       start from currentInd so all valid combinations which contain all reasonable frequencies of currentNumber
     *       will be considered only once.
     *  - when to stop? when to add result?
     *      base cases:
     *      - currentSum == target => copy and add currentCombination to results, return;
     *      - currentSum > target => just return.
     *
     * edge cases:
     *  - candidates.length == 1 => only consider different frequencies of that single number => correct;
     *  - target == 1 => since min candidate is 2 => always return an empty list => correct;
     *  - maxSum is 40, no overflow for int.
     *
     * Time: O(n^(target/minCandidate)) // TODO: why exactly? prove
     *  - we do DFS => time complexity is O(n*k+e), where k=work we do at each node:
     *   - what's the worst amount of nodes and edges?
     *      - what is the height of the tree?
     *       min candidate combined with only itself = target/minCandidate
     *      - how many edges does each node have?
     *       each node has edges to nodes with values that come after it in the original [candidates] array
     *        => on average it's n=candidates.length nodes
     *      => how many nodes do we have in total?
     *       the number of edges for each node ^ depth of the tree
     *
     *   - what's the worst work we do at each node? iterate through n edges and on base cases copy an array of at most
     *    target/minCandidate elements.
     *
     * Space: O(target/minCandidate)
     *  - what's the max size of `currentCombination`? target/minCandidate
     *  - what's the max height of callstack for [backtrack]? target/minCandidate
     *  - what's the max amount of `results`? leave it out
     *
     * ---
     *
     * possible optimization: sort [candidates] and check currentSum>target before the call to [backtrack], if it is
     *  => break the loop and return (no point in considering any digits more than current, sum is only going to increase
     *   since candidates are strictly positive)
     */
    fun solution(candidates: IntArray, target: Int) = mutableListOf<List<Int>>().apply {
        backtrack(
            currentCombination = mutableListOf(),
            currentSum = 0,
            currentInd = 0,
            candidates = candidates,
            target = target,
            results = this,
        )
    }

    private fun backtrack(
        currentCombination: MutableList<Int>,
        currentSum: Int,
        currentInd: Int,
        candidates: IntArray,
        target: Int,
        results: MutableList<List<Int>>,
    ) {
        if (currentSum == target) {
            results.add(ArrayList(currentCombination))
            return
        }
        if (currentSum > target) return

        for (i in currentInd until candidates.size) {
            val digit = candidates[i]
            currentCombination.add(digit)
            backtrack(
                currentCombination = currentCombination,
                currentSum = currentSum + digit,
                currentInd = i,
                candidates = candidates,
                target = target,
                results = results,
            )
            currentCombination.removeLast()
        }
    }
}

fun main() {
    println(
        CombinationSum().solution(
            candidates = intArrayOf(2, 3, 6, 7),
            target = 7,
        )
    )
}
