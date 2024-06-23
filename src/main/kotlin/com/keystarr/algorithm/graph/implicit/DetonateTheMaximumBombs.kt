package com.keystarr.algorithm.graph.implicit

import kotlin.math.pow

/**
 * LC-2101 https://leetcode.com/problems/detonate-the-maximum-bombs/
 * difficulty: medium
 * constraints:
 *  - 1 <= bombs.length <= 300
 *  - bombs\[i].length == 3
 *  - 1 <= xi, yi, ri <= 10^5
 *
 * Final notes:
 *  - I didn't see the problem as a graph, EVEN THOUGH I HAD A HINT the efficient solution involved a graph, so tried
 *   a few other approaches first to no avail;
 *  - it is only when I've sketched what could be the nodes and what could be the edges, and realized the trick is here
 *   is following the detonation chain-reaction, that bombs directly in radius of the first one blown may actually detonate
 *   other bombs in their proximity, then it started to make sense as chain reaction => graph traversal;
 *  - other than that, designed decomposed into pre-processing and actual DFS implementation and got it figured out.
 *   Chose DFS cause its faster to implement and gives same complexities, and here I think theres no benefit it BFS
 *   cause we have to visit each node when traversing without any same distance based logic;
 *  - couldn't come up with the [containsInCircle] formula, didn't even try (but wouldnt be able to). I hope in real
 *   interviews such things will be provided.
 *
 * Value gained:
 *  -
 */
class DetonateTheMaximumBombs {

    /**
     * The original problem:
     *  - given a 2D IntArray bombs, where bomb\[i]=[xi,yi,ri], xi and yi being the center and ri being the radius of explosion
     *  - if any bomb's CENTER (xi,yi) is within the radius of another bomb Q => it will be detonated also if Q is detonated
     *  - it follows that if multiple bombs intersect => there might be chain reaction.
     * Goal: find which bomb to detonate, such that the maximum amount of bombs will be detonated as the result,
     *  return the number of bombs detonated.
     *
     * 1st approach idea - find the circle such that the most amount of other circle centers lie within it =>
     *  some kinda geometry formulas?
     *
     * Brute force idea, #3:
     *  - bombsDetonated = IntArray(size=bombs.size) { 1 } // at least the bomb itself
     *  - iterate through bombs\[i]:
     *   - iterate through bombs\[j], j!=i:
     *    - via O(1) check if bombs[j]'s center is in the radius of bombs[i] => bombsDetonated[i]++
     * !WOULDNT WORK cause doesnt take into account chain reactions!
     * Time: O(n^2)
     * Space: O(n)
     *
     * How to calculate via O(1) time all bombs which centers lie within bomb\[i] radius?
     *
     * cases:
     *  - a bomb has the max amount of direct neighbors that it detonates => its the answer
     *  - a bomb has not the max direct neighbors BUT it has the longest chain reaction => its the answer
     *  - a bomb has not max direct neighbors and not max chain reaction, but together its the max => its the answer
     *
     * ---------------------------
     *
     * 2nd approach try a Graph:
     *  - bomb=node, edge = when another bomb's center is within this bomb's radius
     *  => a directed unweighted graph with possible multiple connected components?
     *
     * graph
     * - pre-process:
     *  - bombsToDirectNeighbors = HashMap<Int, List<Int>>()
     *  - iterate through bombs\[i]:
     *   - iterate through bombs\[j], i != j:
     *    - if bombs[j]'s center lies within the radius of bombs[i] => bombsToDirectNeighbors[i].add(j)
     * - maxBombsDetonated=0
     * - iterate i through 0 until bombs.size:
     *  - seen = BooleanArray(size=bombs.size).apply{ seen\[i] = true }
     *  - bombsDetonated = dfs(currentBomb=i, bombsToDirectNeighbors, seen)
     *  - maxBombsDetonated=max(bombsDetonated,maxBombsDetonated)
     * - return maxBombsDetonated
     *
     * Goal: calculate the total amount of bombs detonated by a chain reaction started with the blow of `currentBomb`
     * dfs(currentBomb, bombsToDirectNeighbors, seen): Int
     *  - neighbors = bombsToDirectNeighbors\[currentBomb]
     *  - totalDetonations = 1
     *  - iterate through neighbors (if not null):
     *   - if (seen\[neighbor]) continue
     *   - seen\[neighbor] = true
     *   - totalDetonations += dfs(neighbor,bombsToDirectNeighbors, seen)
     *  - return totalDetonations
     *
     * Edge cases:
     *  - bombs.length == 1 => return 1, checked, correct;
     *  - one bomb is at the center of the entire field with radius=10^5 => then all points inside it would be counted,
     *   and their chains, correct;
     *  - count the initial bomb itself as a detonated one => init totalDetonations as 1 in the recursive dfs, accumulate
     *   the values from the subgraphs via backtracking. (base case is no neighbors and returns 1 for the bomb itself,
     *   each call dfs for bomb \[currentBomb] counts itself as detonated)
     *  - some bomb's center lies directly ON the circle of another bomb => in [containsInCircle] less OR EQUAL.
     *
     * Time: O(n^2)
     *  - pre-process: always O(n^2), cause checking intersection is O(1) and adding into map/arraylist creation and adding to it is amortized O(1)
     *  - calculation is O(n*(n+e)) = O(n^2)
     *      - dfs average/worst O(n+e), worst n=bombs.size, worst e~=bombs.size
     * Space: O(n + n + e) = O(n)
     *  - map bombsToDirectNeighbors O(n+e)
     *  - seen O(n)
     *
     *
     * -------
     *
     * Minor possible perf improvements:
     *  - convert to double and square radius for only once in for loop, pass it as an arg to [dfs];
     *  - clear and reuse already allocated `detonated` array instead of allocating it each anew for each iteration.
     */
    fun efficient(bombs: Array<IntArray>): Int {
        val bombsToDirectNeighbors = mutableMapOf<Int, MutableList<Int>>()
        for (i in bombs.indices) {
            val bombToExplode = bombs[i]
            for (j in bombs.indices) {
                if (i == j || !bombToExplode.containsInCircle(bombs[j])) continue
                bombsToDirectNeighbors.getOrPut(i) { mutableListOf() }.add(j)
            }
        }

        var maxBombsDetonated = 0
        for (i in bombs.indices) {
            val detonated = BooleanArray(size = bombs.size)
            detonated[i] = true
            val bombsDetonated = dfs(currentBomb = i, bombsToDirectNeighbors, detonated)
            if (bombsDetonated > maxBombsDetonated) maxBombsDetonated = bombsDetonated
        }
        return maxBombsDetonated
    }

    private fun IntArray.containsInCircle(point: IntArray): Boolean =
        (point[0] - this[0].toDouble()).pow(2.0) + (point[1] - this[1].toDouble()).pow(2.0) <= this[2].toDouble().pow(2)

    /**
     * Goal: calculate the total amount of bombs detonated by a chain reaction started with the detonation of [currentBomb],
     *  that were not already detonated (not marked as true in [detonated])
     */
    private fun dfs(currentBomb: Int, bombsToDirectNeighbors: Map<Int, List<Int>>, detonated: BooleanArray): Int {
        val neighbors = bombsToDirectNeighbors[currentBomb]
        var totalDetonations = 1
        neighbors?.forEach { neighbor ->
            if (detonated[neighbor]) return@forEach

            detonated[neighbor] = true
            totalDetonations += dfs(currentBomb = neighbor, bombsToDirectNeighbors, detonated)
        }
        return totalDetonations
    }
}
