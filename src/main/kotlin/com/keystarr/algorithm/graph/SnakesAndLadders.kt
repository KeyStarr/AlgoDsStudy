package com.keystarr.algorithm.graph

import java.util.*

/**
 * constraints:
 *  - n == board.length == board\[i].length
 *  - 2 <= n <= 20 (at least 4, most 400 nodes)
 *  - board\[i]\[j] is either -1 or in [1,n^2]
 *  - the squares labeled as 1 and n^2 do not have any ladders or snakes
 */
class SnakesAndLadders {

    /**
     * idea: graph as a hashmap
     * - convert the matrix into a map node->edges following the snake and ladder rules;
     * - do bfs through the map starting at 1 and targeting node n*n with seen
     * time: O(n*n + e)
     * space: O(n*n + e) for the map, seen also taken into account
     *
     * idea #2: use matrix as-is as a graph
     *  - do bfs (cause the shortest path on a graph) on the matrix as-is:
     *   - map matrix rowInd/columnInd into node labels and back on-the-fly;
     *   - add to the queue either all 6 next nodes (that are not seen yet):
     *      - check straight away if one is a ladder or a snake => in which case add to the queue the destination straight away
     *       instead of the original node (basically treat such nodes as edges straight to the destinationNode=board[i][j])
     *   - use seen, add nodes to seen as soon as we add them to the queue;
     *   - use a single moveCounter variable, process nodes according to their distance from the start;
     *   - as soon as we encounter the target node (with label n*n) return current moveCounter.
     *
     * tricks:
     *  - when we encounter a ladder or a snake, don't add the node to the queue as-is but instead add the ladder/snake
     *   destination coords straight away (that way we'll not use the ladder/snake at the destination if there are some);
     *  - visit each cell at most 1 regardless of the path context, because what nodes we've travelled so far does not
     *   impact at all our future path (we've taken care of not using the current node if its a snake/ladder if previous
     *   one was snake/ladder already).
     *
     * Edge cases:
     *  - if n == 2 => return 1, works correctly as is.
     *
     * Time: average/worst O(n*n + e) but with a better const than idea#1
     * Space: O(n*n) for seen
     */
    fun solution(board: Array<IntArray>): Int {
        val n = board.size
        val queue: Queue<Int> = ArrayDeque<Int>().apply { add(0) }
        val seen = BooleanArray(size = n * n)
        seen[n * n - 1] = true

        var moveCounter = 0
        val nextNodesArray = IntArray(size = 6)
        val endNode = n * n - 1
        while (queue.isNotEmpty()) {
            val nodesOnCurrentMove = queue.size
            repeat(nodesOnCurrentMove) {
                val node = queue.remove()
                board.generateNextNodes(node, endNode, nextNodesArray)
                for (nextNode in nextNodesArray) {
                    if (nextNode == endNode) return moveCounter + 1
                    if (seen[nextNode]) continue
                    queue.add(nextNode)
                    seen[nextNode] = true
                }
            }
            moveCounter++
        }

        return -1
    }

    /**
     * - if there are less than 6 elements left => return only the coordinates of the farthest node (it will always be the target node);
     * - else generate the coords for the next 6 elements of the matrix, check each:
     *  - if these are either a snake or a ladder, then instead of adding the these new coords, map the snake/ladder node label into coords and add those
     *  - else add the coords as-is
     */
    private fun Array<IntArray>.generateNextNodes(node: Int, endNode: Int, nextNodes: IntArray) =
        if (node + 6 >= endNode) {
            nextNodes[0] = endNode
        } else {
            repeat(6) { ind ->
                val newNode = node + (ind + 1)
                val coords = newNode.toCoordinates(size)
                val ladderOrSnakeNextNode = this[coords[0]][coords[1]]
                nextNodes[ind] = if (ladderOrSnakeNextNode != -1) ladderOrSnakeNextNode - 1 else newNode
            }
        }

    private fun Int.toCoordinates(n: Int): IntArray {
        val rowInd = n - 1 - this / n
        val columnInd = if (rowInd.isRowReversed(n)) n - 1 - (this % n) else this % n
        return intArrayOf(rowInd, columnInd)
    }

    private fun Int.isRowReversed(n: Int) = (n - this) % 2 == 0
}

fun main() {
    println(
        SnakesAndLadders().solution(
//            arrayOf(
//                intArrayOf(-1, -1, -1, -1, -1, -1),
//                intArrayOf(-1, -1, -1, -1, -1, -1),
//                intArrayOf(-1, -1, -1, -1, -1, -1),
//                intArrayOf(-1, 35, -1, -1, 13, -1),
//                intArrayOf(-1, -1, -1, -1, -1, -1),
//                intArrayOf(-1, 15, -1, -1, -1, -1)
//            )
            arrayOf(
                intArrayOf(-1, -1),
                intArrayOf(-1, -1),
            )
        )
    )
}
