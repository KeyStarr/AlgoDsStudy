package com.keystarr.algorithm.graph.tree.backtracking

/**
 * LC-77 https://leetcode.com/problems/combinations/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= n <= 20
 *  • 1 <= k <= n
 *
 * Final notes:
 *  • it's awesome how, once again, in order to arrive at the solution one must have reasoned to narrow the problem down
 *   to subsets of length k from vague "combinations"! it is a very common pattern to narrow down the required combination
 *   type from the one stated in the problem, should the problem statement actually narrow it;
 *  • the time complexity yet again is SUPER tricky to estimate, backtracking problems seem to be a few orders of harder
 *   to estimate compared to other problems.
 *
 * Value gained:
 *  • indeed the backtracking pattern worked as-is with just the difference in how we prune and how we add to answers;
 *  • practiced recognizing and designing backtracking.
 */
class Combinations {

    /**
     * problem rephrase: return all possible combinations of k numbers from range  [1, n]
     *
     * generate all possible options + small constraints (n <= 20) => try backtracking?
     *
     * combination is k numbers chosen from range [1,n] such that each number from that range can appear only once.
     * combinations are unordered, so [1,2,3] [3,2,1] [1,3,2] are equal combinations (duplicates)
     *
     * rephrase the problem again:
     * return all possible subsets of length k from the collection [1, n].
     *
     * pruning: each subsequent call to the [backtrack] must receive currentInd+1, i.o. for further traversing we consider
     *  only numbers after the current number in the original array.
     * results collection: when the current.size reaches [subsetLength] => add it into result, don't go any further
     *  (base case = leaves. height of the tree = [subsetLength])
     *  (which is a 2nd facet of pruning also)
     * otherwise backtracking as usual (at each node iterate through all numbers starting from currentInd, add currentNumber
     * into current list and call backtrack, after the call remove currentNumber from the current list)
     *
     * example dry run:
     * n=4 k=2, so the range is 1,2,3,4
     * 1100 (1,2)  0110 (2,3)
     * 1010 (1,3)  0101 (2,4)
     * 1001 (1,4)  0011 (3,4)
     *
     * total number of combinations would be
     *
     * Time: O(n*totalNumberOfCombinations), totalNumberOfCombinations=???
     *  - number of calls to [backtrack] equals to the total number of combinations, which is ????
     *  - the cost of each [backtrack] call involves iterating through the available edges, which depend for each node
     *   depend on n. Each iteration costs O(1).
     * Space: O(subsetLength*totalNumberOfCombinations)
     *  - height of the callstack is [subsetLength]
     *  - there are totalNumberOfCombinations combinations and each takes [subsetLength] length
     *
     * TODO: try to estimate time&space complexities as precise as possible, do my best without looking up the formula,
     *  just reason by the algo (basically the totalNumberOfCombinations)
     */
    fun backtracking(maxBoundary: Int, subsetLength: Int) = mutableListOf<List<Int>>().apply {
        backtrack(
            current = mutableListOf(),
            lowBoundary = 1,
            maxBoundary = maxBoundary,
            subsetLength = subsetLength,
            results = this,
        )
    }

    private fun backtrack(
        current: MutableList<Int>,
        lowBoundary: Int,
        maxBoundary: Int,
        subsetLength: Int,
        results: MutableList<List<Int>>
    ) {
        if (current.size == subsetLength) {
            results.add(ArrayList(current))
            return
        }

        (lowBoundary..maxBoundary).forEach { number ->
            current.add(number)
            backtrack(current, lowBoundary = number + 1, maxBoundary, subsetLength, results)
            current.removeLast()
        }
    }

    // TODO: implement and compare with a bitwise generation solution
    // TODO: implement iterative
}

fun main() {
    println(
        Combinations().backtracking(
            maxBoundary = 4,
            subsetLength = 2,
        )
    )
}
