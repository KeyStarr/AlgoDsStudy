package com.keystarr.algorithm.greedy

/**
 * LC-2410 https://leetcode.com/problems/maximum-matching-of-players-with-trainers/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= players.length, trainers.length <= 10^5
 *  • 1 <= players\[i], trainers\[j] <= 10^9
 *
 * Final notes:
 *  • done [efficient] by myself in 20 mins, greedy was my 1st idea 🔥;
 *  • another greedy where sorting was the first step of the solution, huh. Considered using a heap first for repeatedly
 *   getting mins, but then realized we actually don't modify the collection => fixed that decision branch in the map 🙌.
 *
 * Value gained:
 *  • practiced recognizing and using a greedy paradigm for an efficient solution;
 *  • reinforced the idea that, if we need to get repeatedly max/min with no modifications, except for just taking
 *   1 element at a time => just sort + iterate, no heap.
 */
class MaximumMatchingOfPlayersWithTrainers {

    /**
     * problem rephrase:
     *  - given:
     *   - players: IntArray
     *   - trainers: IntArray
     *  - step rules:
     *   - players\[i] and trainers\[j] form a pair only if players\[i] <= trainers\[j];
     *   - a single players\[i] can only be matched with a trainers\[j] and vice versa.
     *  - goal: return the max possible amount of matches.
     *
     * approach:
     *  - we're making a choice every step
     *  - each choice is individual and almost doesn't affect other choices (besides reducing trainer by 1)
     * => try greedy
     *
     * locally optimal choice: for each player find the minimum valid trainer (>= than it) from remaining ones.
     *  proof: if there are more than 1 values of trainers >= than a player, if we select not the min, we might end up
     *   in a situation where that very value was the only one matching for another player (the min would be too small for it)
     *   => we would loose a match.
     *
     * we need to repeatedly get the min of the remaining [trainers] => just sort it, cause we simply move the ind along it, we don't modify any values
     * we also need to sort the [players] ascending. So that if for current min player the min trainer doesn't fit => there is
     *  guaranteed no match for that trainer in that input.
     *
     * Edge cases:
     *  - players.size == trainers.size== 1 => correct, nothing special;
     *  - players.size != trainers.size => nothing special, we match players/trainers as-is correctly.
     *
     * players=[3]
     * trainers=[4]
     * currentPlayer=3
     * currentTrainer=4
     * matchesCount=1 => answer, correct
     *
     * players=[1,3,7]
     * trainers=[0,1,2,5,8]
     * currentPlayer=7
     * currentTrainer=8
     * matchesCount=3 => answer, correct
     *
     * Time: O(nlogn+mlogm), where n=players.size and m=trainers.size
     *  - O(nlogn) for sorting players
     *  - O(mlogm) for sorting trainers
     *  - main loop has worst n iterations;
     *  - inner loop has across all main loop iterations worst m iterations.
     * Space: O(logn) if in-place sorting, otherwise O(n+m)
     */
    fun efficient(players: IntArray, trainers: IntArray): Int {
        players.sort()
        trainers.sort()
        var matchesCount = 0
        var trainerInd = 0
        players.forEach { player ->
            while (trainerInd < trainers.size) {
                val trainer = trainers[trainerInd]
                trainerInd++
                if (player <= trainer) {
                    matchesCount++
                    break
                }
            }
            if (trainerInd == trainers.size) return matchesCount
        }
        return matchesCount
    }
}
