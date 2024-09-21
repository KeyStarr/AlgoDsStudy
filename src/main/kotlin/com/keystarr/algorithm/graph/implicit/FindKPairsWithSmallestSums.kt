package com.keystarr.algorithm.graph.implicit

import java.util.PriorityQueue

/**
 * ⭐️ a spectacular greedy top(k) heap implicit graph (Dijkstra's) like efficient solution problem!
 *  (I feel like "what the heck just happened" rn)
 *
 * LC-373 https://leetcode.com/problems/find-k-pairs-with-smallest-sums/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= n, m <= 10^5;
 *  • -10^9 <= nums1\[i], nums2\[j] <= 10^9;
 *  • nums1 and nums2 are sorted non-decreasing;
 *  • 1 <= minsTarget <= 10^4 && minTarget <= n*m.
 *
 * Final notes:
 *  • ⚠️⚠️ gave up after 1h, didn't do brute force since it wouldn't fit the time constraints and failed come up with an efficient solution;
 *   solved a couple of examples by myself but failed to see the pattern that I could turn into a viable algorithm based on choices made
 *   each step;
 *
 *  • 🔥 there are so many tools used to solve this problem:
 *   • core: greedy principle for min sum candidates generation. Basically these transitions (i+1,j) (i,j+1) can be modelled
 *    as a directed acyclic graph (DAG);
 *   • if we model it as a graph, the catch is that while we consider candidates reachable within immediate edges of nodes
 *    traversed, we actually traverse based on not the current min candidates immediate edges BUT based on the minimum sum
 *    candidate out of all visited but not used so far! So its like a DAG traversal on steroids, neither DFS nor BFS,
 *    but based on a min heap => what's the name of that algorithm? Ah, its very close to Dijkstra's 🔥🔥🔥
 *    => and so we basically use a minHeap to get the next node with the minimum "path weight" to it
 *    + but we only consider each node once => use a hashset
 *
 *  • ⚠️ I think I could've arrived at the core greedy principle but I've chosen the wrong angle in [initial].
 *   - I should've just started from the observation (which I've made) that the first pair is always the best and tried
 *   to reason which pairs are the best ones next.
 *   - I've done that though sorta but I tried to come up with a full-on greedy strategy to actually choose the next best
 *   pair every time ⚠️ when I've realized I couldn't in 1h I gave up, but apparently I should've considered then
 *   not going for the next best pair in O(1) time BUT getting all the candidates lined up and LESS EFFICIENTLY select
 *   the next best one of those.
 *
 *  • ❗️❗️ what is here similar to Dijkstra's and what is different?
 *   different:
 *    • in Dijkstra's we can see a node X first with the path weight of K and then later encounter an edge to X with
 *     the total weight of G, and G maybe less than K => so we might first visit that node from the path weight G and then
 *     remove from the heap the path with the weight K, but since we've already seen this node with the path weight G there's
 *     no point in consider K, since it is never the optimal path
 *     VS
 *     here the pair sum remains constant no matter the previous nodes in the path => 🏆 if we've seen it once and added it
 *     into the heap there's no need to add it into the processing again, there can't be a path with a better metric to that node
 *     (again, since the node's metric doesn't depend on the path)
 *   similar:
 *    • in Dijkstra's we traverse based on the minimum total path weight, select the next node with that, and to do it
 *     efficiently we use a minHeap
 *     VS
 *     here we also traverse the DAG using the minHeap, but the minimization criteria is not the total path weight, but
 *     the node's pair sum which doesn't depend on the previous path
 *    • 🏆 i.o. both in Dijkstra's and here we use the minHeap to ensure visiting nodes always in the order of their minimum metric
 *
 *  🔥TL;DR; here we have a custom DAG traversal algorithm which guaranteed visiting nodes based on the non-decreasing order
 *   of their metric (pair sum), and the metric doesn't depend on the path.
 *   ❓TODO: try to comeback and learn if that's any industry recognized common algorithm
 *
 *  • oh yeah, actually this question is "top k min pairs" => ⚠️⚠️ should've considered using a min heap just cause of that!!!!!!!
 *   but didn't, didn't even think about the heap.
 *
 *  • 🏅 красавчик, что поборолся до конца и разобрался, понять и раскидал в тч раскопал до конца связь с Dijkstra's
 *   (though it took me 4 freaking hours!!!! and a night of despair lol)
 *
 * Value gained:
 *  • practiced solving a "find top k pairs out of 2 arrays" type problem using greedy and a Dijkstra's like DAG traversal
 *   algorithm based on the minHeap and a seen hashSet (another angle - topk minheap with a seen hashset) 🙉
 *  • apparently topk heap and an implicit DAG Dijkstra's like are both valid ways from different angles to describe a single solution???
 *  • 🏆 learned that we can derive our own efficient graph traversal algorithms based on the tools available, its not
 *   always reasonable to stick to the stock DFS/BFS/Dijkstra's etc even at the fundamental level (with minheap but not seen etc)
 *   more freedom, more uncertainty => rational creativity wins.
 */
class FindKPairsWithSmallestSums {

    // TODO: full resolve in 2-3 weeks

    private val directions = arrayOf(
        intArrayOf(1, 0),
        intArrayOf(0, 1),
    )

    /**
     * Observations:
     *  1. fpr the first pair we always take (0,0). Cause these are guaranteed the minimum available elements from both arrays;
     *  2. since (0,0) for the first pair, what can we consider next? It makes sense to increase the total sum by the minimum
     *   amount possible, and since [nums] is sorted non-decreasing we can => either move the first ind by 1, or the second
     *   => try (1,0) and (0,1);
     *  3. if (1,0) turns out to be the minimum sum of the two, apply the same greedy principle to it: try to find the next
     *   pair while increasing the sum by the minimum possible => either (2,0) or (1,1);
     *  4. then the minimum can be either (0,1) or (2,0) or (1,1). Reasoning is: we don't know the size of the gaps between
     *   consecutive elements => can't predict by how much the sum exactly would change, but the minimum possible increment
     *   from the previous 2 smallest sums is definitely one of these by the logic of #2;
     *  5. suppose the min sum now is (0,1) => next new candidates are (0,2) and (1,1). Note that we've already added (1,1)
     *   to the candidates though, and we can add each pair to the result only once => discard the duplicate
     *   and add only (0,2) as the new candidate.
     *
     * The catch: its easy to find the first pair, but next pairs can be on different indices, since gaps between
     *  consecutive numbers in both arrays are arbitrary => sum can increase drastically if we move one index but not the other
     *  => we have to each step consider all new possible pairs with the MINIMUM total sum increase along with the older ones
     *   not yet taken.
     *
     * Design choices:
     *  1. we have many potential candidates, and we add to that collection live, and each step we need to always get the
     *   minimum sum candidate (if it exists) => what's the most efficient way of doing that?
     *   i.o. repeatedly getting minimums from a live modified collection => try a min heap
     *  2. as we might choose different candidates (from different "branches" of the candidates tree) and candidates may
     *   be duplicated, and we need to check a single one at most once => use a hashset to guarantee processing each candidate
     *   at most once.
     *
     * Edge cases: I see no real edge cases
     *
     * Time: always O(k * log(k)) with worst k=n*m
     *  - worst [minsTarget] == n*m which is the max number of pairs:
     *   - each pair we add to and remove from the minHeap O(log??) = O(logk);
     *    how many values worst can be stored in the heap?
     *     at first we add 1 candidate (1), then remove it and add 2 more (2), then remove 1 and potentially add two more (3)
     *     or we add one more due to seen (2) => at each iteration we always remove 1 value and add up to 2 new values into the heap
     *
     *     without further deliberation I'd say heap grows up to ~k? since worst every iteration we remove 1 value and add
     *     2 => grow the heap by 1, and we do it up to k times (possible only up to some value of k in relation to n*m though,
     *     then we'll see only 1 value added and then no new values added at all on the "leafs").
     *
     *  - and we stop at finding [minsTarget] candidates.
     *
     * Space: always O(k)
     *  - we've established above that heap grows in proportion to k;
     *  - seen set worst is n*m, but it grows as well as the heap in proportion to k;
     *  - don't count result.
     *
     * ------------
     *
     * Learned thanks to https://www.youtube.com/watch?v=i6sVnaqzgoA&t=764s&ab_channel=Alpha-Code and a bunch of other
     * solutions, dry-ran and tried to proof (informally) by myself as much as I could.
     */
    fun efficient(nums1: IntArray, nums2: IntArray, minsTarget: Int): List<List<Int>> {
        val minHeap = PriorityQueue<Candidate> { o1, o2 -> o1.sum - o2.sum }
        val minPairs = mutableListOf<List<Int>>()
        val seen = mutableSetOf<Candidate>()
        minHeap.add(Candidate(i = 0, j = 0, sum = nums1[0] + nums2[0]))
        while (minHeap.isNotEmpty() && minPairs.size < minsTarget) {
            val min = minHeap.remove()
            minPairs.add(listOf(nums1[min.i], nums2[min.j]))

            directions.forEach { direction ->
                val newI = min.i + direction[0]
                val newJ = min.j + direction[1]
                if (newI == nums1.size || newJ == nums2.size) return@forEach

                val newCandidate = Candidate(i = newI, j = newJ, sum = nums1[newI] + nums2[newJ])
                if (seen.contains(newCandidate)) return@forEach

                minHeap.add(newCandidate)
                seen.add(newCandidate)
            }
        }
        return minPairs
    }

    /**
     * Judging by the example - we need to return k pairs with DISTINCT pair of indices. No one to ask, huh.
     *
     * We need to return k smallest sum pairs + both arrays are sorted non-decreasing -> try greedy
     *  each step select the next minimum sum pair available:
     *
     * 1 7 9
     * 1 3 13
     *
     * 1 1, 1 3 | 7 1, 9 1 | 7 3, 9 3 | 1 13 | 7 13, 9 13
     *
     * observations:
     *  - total number of pairs possible = n*m, since we have n numbers for the first slot and m numbers for the second;
     *  - the result list is never empty since k > 0 && k <= n*m + we always can build exactly k combinations.
     *
     *
     * but how would the algorithm go exactly? observe that on example above we actually moved indices quite sporadically
     *
     * it was, indices:
     *  0 0, 0 1 | 1 0, 2 0 | 1 1, 2 1 | 0 2 | 1 2, 2 2
     *
     *
     * given the constraints (worst k=n*m, worst n=10^5, m=10^5 so worst total number of combinations = 10^10)
     * we need the solution faster or equal to O(n). wait, would O(n) even pass?
     *
     * f1 t1 t3 f7 f9 t13
     *
     * 0 1, 0 2 | 3 1, 4 0 |
     *
     *
     * for the sum to be minimal we need to take a minimum number of nums1 and a minimum of nums2 such that this pair
     * wasn't seen before. how to do that?
     *
     *
     *
     * 1 7 9
     * 1 3 8
     *
     *
     * freeze nums1\[i], try all combinations with nums2\[j] with j=0.., until nums1\[i]+nums2\[j] > num1[i+1]+nums2\[j]
     * => then freeze nums2\[j] and continue
     *
     *
     * --------
     *
     * Disgusting brute force: generate all possible pairs, as we do that add all into a topk max heap (find k min sum)
     *  => build the result list out of the heap
     * time: O(logk * (n*m + k))
     *  - building the heap O(n*m*logk);
     *  - building the result O(klogk)
     *
     * too slow for 10^10 possible combinations
     *
     * => we have to do a greedy efficient O(k) solution, go through one currently min available pair straight to the next one k times
     *
     * -----------
     *
     *
     */
    fun initial(nums1: IntArray, nums2: IntArray, k: Int): List<List<Int>> {
        val posToLastJ = IntArray(size = nums1.size)
        val results = mutableListOf<List<Int>>()
        var i = 0
        var j = 0
        while (results.size < k) {
//            while ((i < nums1.size && j < nums2.size) && nums1[i] + nums2[j] < nums1[posToLastJ[i+1]] + nums2) {
//
//            }
        }
        return results
    }

    private data class Candidate(
        val i: Int,
        val j: Int,
        val sum: Int, // technically we could compute the sums live in the min heap comparator, but that should reduce time const a litle
    )
}

fun main() {

}
