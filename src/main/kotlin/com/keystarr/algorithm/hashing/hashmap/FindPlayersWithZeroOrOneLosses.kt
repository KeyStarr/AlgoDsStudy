package com.keystarr.algorithm.hashing.hashmap

/**
 * LC-2225 https://leetcode.com/problems/find-players-with-zero-or-one-losses/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= matches.length <= 10^5;
 *  • matches\[i].length == 2;
 *  • 1 <= winner ith, loser ith <= 10^5;
 *  • winner ith != loser ith;
 *  • all matches\[i] are unique;
 *  • no explicit time/space.
 *
 * Final notes:
 *  • implemented [suboptimal] in 25 mins;
 *  • "return both lists in sorted order" is the trick in this problem, being the bottleneck;
 *  • again, as before with counting via hashmap problem, the faster time-wise is to use array.size = maxPossibleItemsCount
 *      and simply use indices as playerIds and values as losses count. But the trade off is space for real world uses.
 *      Is it always the way with counting via HashMap problems where items size is a reasonable number??
 */
class FindPlayersWithZeroOrOneLosses {

    /**
     * Tools: HashMap.
     *
     * Idea:
     * - allocate a hashmap<Int,Pair<Int,Int>> (pair - wins and losses respectively);
     * - iterate through all matches;
     *  - map[matches\[i][0]].first += 1;
     *  - map[matches\[i][1]].second += 1;
     * - iterate through map.entries and build 2 lists: the one where map\[i].second == 0 and map[\i].second == 1;
     * - sort both lists.
     *
     * Edge cases:
     *  - all fits into Int;
     *  - matches.length == 1 => works correctly.
     *
     * Time: always O(nlogn) due to sorting
     * Space: always O(n)
     */
    fun suboptimal(matches: Array<IntArray>): List<List<Int>> {
        val playerIdToLossMap = mutableMapOf<Int, Int>()
        matches.forEach { players ->
            val winnerId = players[0]
            if (!playerIdToLossMap.contains(winnerId)) playerIdToLossMap[winnerId] = 0

            val loserId = players[1]
            playerIdToLossMap[loserId] = playerIdToLossMap.getOrDefault(loserId, 0) + 1
        }

        val pureWinners = mutableListOf<Int>()
        val oneLossPlayers = mutableListOf<Int>()
        playerIdToLossMap.entries.forEach { entry ->
            when (entry.value) {
                0 -> pureWinners.add(entry.key)
                1 -> oneLossPlayers.add(entry.key)
            }
        }
        return listOf(pureWinners.sorted(), oneLossPlayers.sorted())
    }

    /**
     * Since sorting is the bottleneck (gives nlogn time) we could improve that:
     *  - use binary search for insertion => better const but still O(nlogn);
     *  - use merge sort => get O(n) time, but with a "bad" const;
     *
     *  Idea: add ids into lists during the first iteration, if there are more losses => remove those from the lists.
     *
     */
    fun efficient(matches: Array<IntArray>): List<List<Int>> {
        TODO("revisit, implement a solution using an array only (yes, terrible, 10^5 space always")
    }
}

fun main() {
    println(
        FindPlayersWithZeroOrOneLosses().suboptimal(
            arrayOf(
                intArrayOf(2, 3),
            )
        )
    )
}
