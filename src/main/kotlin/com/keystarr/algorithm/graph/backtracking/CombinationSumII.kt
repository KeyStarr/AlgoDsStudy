package com.keystarr.algorithm.graph.backtracking

/**
 * LC-40 https://leetcode.com/problems/combination-sum-ii/description/
 * difficulty: medium
 * encounters: 💣 first - recognized backtracking, failed to implement in 1h
 * constraints:
 *  • 1 <= candidates.size <= 100;
 *  • 1 <= candidates\[i] <= 50;
 *  • 1 <= target <= 30.
 *
 * Final notes:
 *  • ⚠️⚠️ done [efficient] by myself in 1h7m:
 *   • recognized backtracking straight away;
 *   • failed 3 iterations to actually implement the right approach, struggled, really. Eventually done it by myself
 *    but couldn't rationally grasp exactly why it was that way exactly (kinda melted the brain trying to do the problem in the first place)
 *    (also a factor: wanted to eat. badly)
 *   • watched Neetcode, dry ran, but still don't fully grasp why it is exactly the way it is. Its the part of
 *    sorting + considering only the elements right of the currentInd that confuses me. I've tested that, yes,
 *    we would have tried all combinations including the currentInd number and all the previous numbers to it beforehand
 *    while taking each respective prev number and taking the current number then. And since order doesn't matter, we doing
 *    subsets => that would equal to what we'd try starting at the currentInd element and taking any prev element as the
 *    2nd one = starting at any of the prev elements and taking currentId element as the 2nd one. Huh, kinda showed it.
 *    And same reasoning goes for subsets of greater size, though I don't rationally grasp it, didn't dry run enough?
 *  • decided current understanding is OK as it is, but we gotta approach the problem later again, try and repeat.
 *
 * Value gained:
 *  • practiced recognizing and solving a backtracking distinct permutations type problem efficiently using backtracking.
 */
class CombinationSumII {

    // TODO: retry in 1-2 weeks. Try to understand deeper also, exactly why it works, all reasoning

    private val results = mutableListOf<List<Int>>()
    private lateinit var candidates: IntArray

    /**
     * how to prune efficiently?
     *  1. don't consider used numbers;
     *  2. how to efficiently prune on duplicate elements? i.o. when adding the new element to the combination leads to
     *   having same elements and same frequencies as the combination we considered before that?
     *
     * sorting?
     *
     * nums=1 3 1 5 3 2
     * t=7
     * eR=[[1 3 3], [5 2], [2 3 1 1], [3 3 1]]
     *
     * 1 2..
     * 2 1..
     *
     * 1 1 2 3 3 5
     *
     * to get available candidates just move the pointer right by 1
     * if we start with 1 and consider choices 1 1 2
     * if we start with 2 theres no point in trying 2 1 1, or 2 1 since we've tried that too then.
     *
     *
     * and we don't actually have to sort.
     * its just that if we're at currentCandidateInd, then we've tried starting with the elements with indices prior to it
     * => and considered prior element + current element combination already.
     *
     * Time: TODO
     * Space:
     */
    fun efficient(candidates: IntArray, targetSum: Int): List<List<Int>> {
        candidates.sort()
        this.candidates = candidates
        backtrack(candidatesLeftInd = 0, sumLeft = targetSum, currentCombination = mutableListOf())
        return results
    }

    private fun backtrack(
        candidatesLeftInd: Int,
        sumLeft: Int,
        currentCombination: MutableList<Int>,
    ) {
        if (sumLeft == 0) {
            results.add(ArrayList(currentCombination))
            return
        }

        var prevElement: Int? = null
        for (i in candidatesLeftInd until candidates.size) {
            val newElement = candidates[i]
            if (newElement == prevElement) continue
            if (newElement > sumLeft) return

            currentCombination.add(newElement)
            backtrack(candidatesLeftInd = i + 1, sumLeft = sumLeft - newElement, currentCombination)
            currentCombination.removeLast()

            prevElement = newElement
        }
    }


    /* the initial WRONG approach */


//    private val results = mutableSetOf<MutableMap<Int, Int>>()
//    private lateinit var used: BooleanArray
//    private lateinit var candidates: IntArray

    /**
     * WRONG => TLE
     * (not surprisingly! since we prune in a horrible manner by computing the hashcode for many potentially invalid
     * combinations, which takes O(combinationLength) time each!)
     *
     * ---------
     *
     * problem rephrase:
     *  - given:
     *   - candidates: IntArray (NOT DISTINCT numbers);
     *   - targetSum: Int.
     *  Goal: return all valid DISTINCT subsets of [candidates]
     *   valid = all elements sum up to exactly [targetSum].
     *
     *   does order of elements within result subsets need to match the order of elements in candidates???
     *    => no one to ask, input examples suggest otherwise.
     *
     * generate all of something => try backtracking
     *
     *
     * how do we generate/prune?
     *  - keep `candidatesLeft: MutableSet<Int>`, out of which we may consider new elements;
     *   NO, we weren't told the candidatesLeft are actually distinct.
     *   then either declare BooleanArray(size=candidates.size) to mark used ones and iterate over it
     *   OR keep a hashmap candidate->count candidatesLeft, where count is count left. Or IntArray for counting where index=candidate, value=count;
     *
     *  - for the current position consider ONLY UNIQUE NUMBERS. Cause any duplicate do a number already tried would have
     *   the same exact impact on the metric (sumLeft) => lead to a duplicate subset;
     *   WRONG, not just that!
     *   actually, we have ever tried up to the current position an equal subset => then we would have already tried
     *   all possible further valid choices => we'd have duplicates!
     *   => when making a choice for position X we need to make sure that candidates\[i], when added to the current
     *    subset, that we've never seen such a subset before.
     *   => the simplest solution is to keep track of the results as a SET of SETS and, upon adding the new number, check
     *    if there was a set like that already, if so, then PRUNE, otherwise go further.
     *   WRONG, duplicate elements might repeat in a valid combination => if we use a set to keep track of the current
     *    combination we loose knowledge of duplicate elements in it. But we still need to tell for a new partial combination
     *    candidate, if we've already considered the same frequencies of same elements before => use a HashMap to track
     *    the current combination and store the result combinations.
     *
     *  - try to add any element which, when added to the current subset the sumLeft > 0.
     *
     * Edge cases:
     *  - candidates.size == 1 => if candidates\[0] == targetSum => return [[candidates[0]], otherwise emptyList => correct as-is;
     *  - targetSum == 1 => we might make candidates only with a single number that == 1, there might be many such numbers,
     *   but we need to return always, if there's at least a single element==1 the [[1]] =>
     *
     *
     * [1,1,1,1] tS=1
     *
     * results=[[1]]
     *
     */
//    fun abomination(candidates: IntArray, targetSum: Int): List<List<Int>> {
//        this.candidates = candidates
//        this.used = BooleanArray(size = candidates.size)
//        abominationBacktrack(
//            currentCombination = mutableMapOf(),
//            sumLeft = targetSum
//        )
//        return results.map { numToFreqMap ->
//            val combination = mutableListOf<Int>()
//            numToFreqMap.entries.forEach { entry ->
//                val (num, frequency) = entry
//                repeat(frequency) { combination.add(num) }
//            }
//            combination
//        }
//    }
//
//    private fun abominationBacktrack(
//        currentCombination: MutableMap<Int, Int>,
//        sumLeft: Int
//    ) {
//        if (sumLeft < 0) return
//        if (sumLeft == 0) {
//            results.add(HashMap(currentCombination))
//            return
//        }
//
//        candidates.forEachIndexed { ind, candidate ->
//            if (used[ind]) return@forEachIndexed
//
//            val prevCount = currentCombination[candidate]
//            currentCombination[candidate] = (prevCount ?: 0) + 1
//            if (results.contains(currentCombination)) {
//                if (prevCount == null) currentCombination.remove(candidate) else currentCombination[candidate] =
//                    prevCount
//                return@forEachIndexed
//            }
//
//            used[ind] = true
//            abominationBacktrack(currentCombination, sumLeft = sumLeft - candidate)
//            if (prevCount == null) currentCombination.remove(candidate) else currentCombination[candidate] = prevCount
//            used[ind] = false
//        }
//    }
}

// 1 1 1 2 2
fun main() {
    println(
        CombinationSumII().efficient(
            candidates = intArrayOf(1, 2, 2, 1, 1),
            targetSum = 3,
        )
    )
}
