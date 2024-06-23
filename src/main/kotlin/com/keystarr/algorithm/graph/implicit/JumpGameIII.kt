package com.keystarr.algorithm.graph.implicit

import java.util.ArrayDeque
import java.util.Queue

/**
 * LC-1306 https://leetcode.com/problems/jump-game-iii/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= array.length <= 5*10^4
 *  • 0 <= array\[i] < array.length
 *  • 0 <= start < array.length
 *
 * Final notes:
 *  • done [efficient] by myself in ~30-35 mins;
 *  • funny, I was 100% sure the first 30 minutes that the goal was to reach ALL nodes with 0 value, not any :D Thankfully
 *   at least I have realized that eventually. Though already implemented DFS by that time..
 *  • FAILED 1ST SUBMIT, MISSED AN EDGE CASE though literally wrote it down! Just assumed the algo is correct with it and 
 *   didn't check it manually. [0], start=0, that actually since the start node we add to seen straight away we dont check
 *   it for being a target within the loop! therefore we need to check it manually before the loop itself.
 *
 * Value gained:
 *  • practiced solving a problem with an implicit graph solution. Indeed, here it was clear as a day - because jump=transition;
 *  • LESSON TO PRACTICE: CHECK THE EDGE CASES BY DRY/MENTAL RUNNING THEM! Don't be overconfident.
 */
class JumpGameIII {

    private val directions = intArrayOf(1, -1)

    /**
     * Original problem:
     *  - given array of integers >0 and a start index of the array;
     *  - when at index `i` we can move to i+array\[i] or i-array\[i] indices of the array
     * Goal: return true if it is possible to reach ANY index with value 0.
     *
     * Problem rephrase - try graphs:
     *  - we are given a directed unweighted graph with possibly multiple connected components
     *  - nodes=elements of the array, edges=for each element there are 2 edges, if not out of bounds:
     *       - i+array[i]
     *       - i-array[i]
     * Goal: return true if it is possible to start at [start] and visit ANY node that has exactly 1 edge to itself
     *  (array\[node] == 0)
     *
     * Goal rephrase: return true if there is any node with a self-edge that is in the same connected component
     *  as the [start] node.
     *
     * Reach any index => reach first available index from start => try BFS (least number of steps for efficiency).
     *
     * Any index :
     *  - seen: IntArray\[array.size]
     *  - queue = ArrayDeque<Int>().apply{ add(start) }
     *  - seen\[start] = true
     *  - while (queue.isNotEmpty):
     *      -
     *
     * Edge cases:
     *  - if nodeEdges\[start] == 0 => return true straight away. Must do an early return because we mark [start] as seen
     *   and never check it to be 0 in the main loop!
     *   - all array elements are 0s => a subcase, same.
     *  - array.length == 1 start == 0
     *      - and array[0]==0 => return true, correct
     *      - otherwise return false, correct
     *
     * Time: worst/average O(n+e)=O(n), n=[nodeEdges].size, e~=2*n
     * Space: O(n),
     *  - seen takes O(n)
     *  - queue takes O(k), where k=the most amount of nodes with the same distance from [start], which depends on n => O(n)
     */
    fun efficient(nodeEdges: IntArray, start: Int): Boolean {
        if (nodeEdges[start] == 0) return true

        val queue: Queue<Int> = ArrayDeque<Int>().apply { add(start) }
        val seen = BooleanArray(nodeEdges.size)
        seen[start] = true
        while (queue.isNotEmpty()) {
            val node = queue.remove()
            directions.forEach { direction ->
                val jump = nodeEdges[node]
                val nextNode = node + jump * direction
                if (nextNode in nodeEdges.indices && !seen[nextNode]) {
                    if (nodeEdges[nextNode] == 0) return true
                    queue.add(nextNode)
                    seen[nextNode] = true
                }
            }
        }
        return false
    }
}
