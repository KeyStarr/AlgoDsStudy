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
     * Problem rephrase:
     *  - we are given a directed graph represented via a matrix;
     *  - nodes are its elements, and they are labeled starting from the bottom left corner in a Boustrophedon style;
     *      (that is, each line starting from the bottom alternates being reversed)
     *  - edges are defined as:
     *      - if matrix[i][j] == -1 => from that node there are edges to [curr+1, min(curr+6,n^2)] nodes, that is,
     *       up to 6 nodes further, if these exist;
     *      - if matrix[i][j] != -1 => it is either a snake or a ladder, meaning that upon reaching that node we have to
     *       go to the node labeled as it's value, and it can be either forward or backwards
     *       => this node has a single edge, towards that labeled node
     *      - if we've reached a node via a snake or a ladder and it itself is a snake or a ladder => we don't use it
     *       and treat it as a -1 node for the next move.
     *      - a SINGLE move is considered using an edge from -1 to another node AND if it is a snake or a ladder, using
     *          its edge (still a single move).
     *
     * Goal: return the number of moves required to reach the top right corner from the bottom left corner.
     *  => return the shortest path in a directed graph with starting point (n-1,0) to (0,n-1)
     *
     * + BUT CAN WE USE A SNAKE/LADDER IF WEVE REACHED IT VIA ANOTHER SNAKE/LADDER, AS THE NEXT MOVE??? Or do we have to
     *  treat it as -1???? no examples, no text about it, no one to ask)))
     *  assume it will just be a separate move.
     *
     * Observations:
     *  - when does taking a snake lead to the shortest path?
     *      - only if we took a ladder earlier, and it is still the shortest path to take a snake back to another ladder
     *       to move even further
     *  - when does NOT taking a ladder lead to the shortest path?
     *      - only if the ladder goes to a -1 node all next 6 elements of which are snakes going much backwards
     *
     * -------------------------
     * change of course, try straight-forward BFS
     *
     * Idea: shortest path => try BFS
     *  - start BFS at (n-1, 0)
     *  - add to the queue every possible direction: take first six edges, but don't yet increase the move counter?
     *  - once remove a node from the queue, check the current node:
     *      - if it is -1 => consider move taken (add +1), add to the queue same first six edges with lastSnakeLadder=false
     *      - otherwise check:
     *          - if the last edge we took was a snake/a ladder => don't take it, move+=1, add first six edges lastSnakeLadder=false
     *          - else => add a single node to the queue, the value of the matrix[currentRow][currentColumn], don't increase the move counter,
     *              lastSnakeLadder=true
     *      all that with rules:
     *       - don't enqueue the node if it was already seen with the context of lastSnakeLadder being the same value
     *       - if the next node's label we reach == n^2 => ANSWER, return it's final number of moves taken
     *       - if we're at -1 and the sixth node is beyond n^2 => ANSWER
     *  - return -1
     *
     * sub-problem:
     *  how to determine the required matrix cell to traverse to from snake/ladder's label?
     * Idea #2:
     *  - pre-process the matrix into the hashmap node->edges?
     *
     * concern - we have a lot of edges with the same level of distance. Can we optimize prioritizing the most
     *  promising ones somehow?
     *
     * Edge cases:
     *  - n < 6 (2 or 4 since a square matrix) => return 1
     *  - smallest matrix, n == 2
     *
     * Time: average/worst O(n^2)
     *  - preprocessing O(n^2)
     *  - traversing O(n^2 * 2 + e)
     *      worst e~=6*n^2
     * Space: average/worst O(n^2)
     *  - preprocessing O(n^2 + e)
     *  - seen O(n^2)
     */
//    fun solution(board: Array<IntArray>): Int {
//        val n = board.size
//        val elementsSize = n * n
//
//        val nodeToEdgesMap = mutableMapOf<Int, MutableList<Edge>>()
//        var currentLabel = 0
//        for (i in (n - 1 downTo 0)) {
//            val isReversed = (n - i) % 2 == 0
//            for (j in if (isReversed) (n - 1 downTo 0) else board.indices) {
//                val edges = nodeToEdgesMap.getOrPut(currentLabel) { mutableListOf() }
//                if (board[i][j] == -1) {
//                    val nextElements = min(6, elementsSize - currentLabel)
//                    repeat(nextElements) { increment ->
//                        edges.add(Edge(targetNode = currentLabel + increment, isSnakeOrLadder = false))
//                    }
//                } else {
//                    edges.add(Edge(targetNode = board[i][j] - 1, isSnakeOrLadder = true))
//                }
//                currentLabel++
//            }
//        }
//
//        val queue: Queue<NodeVisit> = ArrayDeque<NodeVisit>().apply {
//            add(NodeVisit(node = 0, wasLastEdgeSnakeOrLadder = false, movesCounter = 0))
//        }
//        val seen = Array(size = n * n) { BooleanArray(size = 2) }
//        seen[0][WAS_SEEN] = true
//        seen[0][WAS_LAST_EDGE_SNAKE_OR_LADDER] = false // explicit
//
//        while (queue.isNotEmpty()) {
//            val nodeVisit = queue.remove()
//            val edges = nodeToEdgesMap[nodeVisit.node]!!
//            edges.forEach { edge ->
//                if (edge.isSnakeOrLadder && !nodeVisit.wasLastEdgeSnakeOrLadder) {
//
//                }
//            }
//        }
//    }

//    fun solution1(board: Array<IntArray>): Int {
//        val n = board.size
//        val elementsSize = n * n
//        val queue: Queue<NodeVisit> = ArrayDeque<NodeVisit>().apply {
//            add(NodeVisit(rowInd = n - 1, columnInd = 0, wasLastEdgeSnakeOrLadder = false, movesCounter = 0))
//        }
//        val seen = Array(size = n * n) { BooleanArray(size = 2) }
//
//        while (queue.isNotEmpty()) {
//            val nodeVisit = queue.remove()
//            val element = board[nodeVisit.rowInd][nodeVisit.columnInd]
//            if (element != -1 && !nodeVisit.wasLastEdgeSnakeOrLadder) {
//                // a snake or ladder and we can take it
//            } else {
//                // a regular element
//                repeat(6) { increment ->
//                    val rawColumn = nodeVisit.columnInd + increment
//                    var newRow = nodeVisit.rowInd - rawColumn / n
//                    val isReversed = (n - nodeVisit.rowInd) % 2 == 0
//                    var newColumn =
//                }
//            }
//        }
//
//        return -1
//    }

//    fun solution2(board: Array<IntArray>): Int {
//        val n = board.size
//        if (n < 3) return 1
//
//        for (i in (n - 1 downTo 0)) {
//            val isReversed = (n - i) % 2 == 0
//            if (isReversed) {
//                for (j in (0 until n / 2)) {
//                    val buf = board[i][j]
//                    val pairIndFromEnd = n - j - 1
//                    board[i][j] = board[i][pairIndFromEnd]
//                    board[i][pairIndFromEnd] = buf
//                }
//            }
//        }
//
//        val queue: Queue<NodeVisit> = ArrayDeque<NodeVisit>().apply {
//            add(NodeVisit(rowInd = n - 1, columnInd = 0, wasLastEdgeSnakeOrLadder = false, movesCounter = 0))
//        }
//        val seen = Array(size = n) { Array(size = n) { BooleanArray(size = 2) } }
//
//        while (queue.isNotEmpty()) {
//            val nodeVisit = queue.remove()
//            val element = board[nodeVisit.rowInd][nodeVisit.columnInd]
//            if (element != -1 && !nodeVisit.wasLastEdgeSnakeOrLadder) {
//                val nodeLabel = element - 1
//                val nextRow = n - nodeLabel / n
//                val nextColumn = nodeLabel % n
//                if (seen[nextRow][nextColumn][LAST_EDGE_SNAKE_OR_LADDER]) continue
//
//                queue.add(
//                    NodeVisit(
//                        rowInd = nextRow,
//                        columnInd = nextColumn,
//                        wasLastEdgeSnakeOrLadder = true,
//                        movesCounter = nodeVisit.movesCounter,
//                    )
//                )
//                seen[nextRow][nextColumn][LAST_EDGE_SNAKE_OR_LADDER] = true
//            } else {
//                for (i in 1..6) {
//                    val rawColumn = nodeVisit.columnInd + i
//                    val newColumn = rawColumn % n
//                    val newRow = nodeVisit.rowInd + rawColumn / n
//                    if (newRow >= n) break
//                    if (newRow == 0 && newColumn == n - 1) return nodeVisit.movesCounter
//                    if (seen[newRow][newColumn][LAST_EDGE_NORMAL]) continue
//                    queue.add(
//                        NodeVisit(
//                            rowInd = newRow,
//                            columnInd = newColumn,
//                            wasLastEdgeSnakeOrLadder = false,
//                            movesCounter = nodeVisit.movesCounter + 1
//                        )
//                    )
//                    seen[newRow][newColumn][LAST_EDGE_NORMAL] = true
//                }
//            }
//        }
//
//        return -1
//    }

//    private class Edge(
//        val rowInd: Int,
//        val columnInd: Int,
//        val isSnakeOrLadder: Boolean,
//    )
//
//    private class NodeVisit(
//        val rowInd: Int,
//        val columnInd: Int,
//        val wasLastEdgeSnakeOrLadder: Boolean,
//        val movesCounter: Int,
//    )


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
     *  -
     *
     * Edge cases:
     *  - if n == 2 => return 1
     *  -
     *
     * Time: average/worst O(n*n + e) but with a better const than idea#1
     * Space: O(n*n) for seen
     */
    fun solution(board: Array<IntArray>): Int {
        val n = board.size
        val queue: Queue<IntArray> = ArrayDeque<IntArray>().apply { add(intArrayOf(n - 1, 0)) }
        val seen = Array(size = n) { BooleanArray(size = n) }
        seen[n - 1][0] = true

        var moveCounter = 0
        val endCoords = findEndCoords(n)
        while (queue.isNotEmpty()) {
            val nodesOnCurrentMove = queue.size
            repeat(nodesOnCurrentMove) {
                val nodeCoords = queue.remove()
                val nextCoords = board.generateNextCoords(nodeCoords)
                for (coords in nextCoords) {
                    val rowInd = coords[0]
                    val columnInd = coords[1]
                    if (coords.contentEquals(endCoords)) return moveCounter + 1
                    if (seen[rowInd][columnInd]) continue
                    queue.add(coords)
                    seen[rowInd][columnInd] = true
                }
            }
            moveCounter++
        }

        return -1
    }

    private fun findEndCoords(n: Int) = intArrayOf(0, if (0.isRowReversed(n)) 0 else n - 1)

    /**
     * - if there are less than 6 elements left => return only the coordinates of the farthest node (it will always be the target node);
     * - else generate the coords for the next 6 elements of the matrix, check each:
     *  - if these are either a snake or a ladder, then instead of adding the these new coords, map the snake/ladder node label into coords and add those
     *  - else add the coords as-is
     */
    private fun Array<IntArray>.generateNextCoords(currentCoords: IntArray): List<IntArray> {
        val nextCoords = mutableListOf<IntArray>()
        val n = size
        var baseRowInd = currentCoords[0]
        var baseColumnInd = currentCoords[1]
        repeat(6) {
            val isCurrentReversed = baseRowInd.isRowReversed(n)
            val nextRowInd: Int
            val nextColumnInd: Int
            if (isCurrentReversed) {
                if (baseColumnInd == 0) {
                    nextRowInd = baseRowInd - 1
                    nextColumnInd = 0
                } else {
                    nextRowInd = baseRowInd
                    nextColumnInd = baseColumnInd - 1
                }
            } else {
                if (baseColumnInd == n - 1) {
                    nextRowInd = baseRowInd - 1
                    nextColumnInd = n - 1
                } else {
                    nextRowInd = baseRowInd
                    nextColumnInd = baseColumnInd + 1
                }
            }
            if (nextRowInd == -1) return@repeat
            val nextDirection = this[nextRowInd][nextColumnInd]
            val newCoords =
                if (nextDirection != -1) nextDirection.toCoordinates(n) else intArrayOf(nextRowInd, nextColumnInd)
            nextCoords.add(newCoords)
            baseRowInd = nextRowInd
            baseColumnInd = nextColumnInd
        }
        return nextCoords
    }

    private fun Int.toCoordinates(n: Int): IntArray {
        val labelFromZero = this - 1
        val rowInd = n - 1 - labelFromZero / n
        val columnInd = if (rowInd.isRowReversed(n)) n - 1 - (labelFromZero % n) else labelFromZero % n
        return intArrayOf(rowInd, columnInd)
    }

    private fun Int.isRowReversed(n: Int) = (n - this) % 2 == 0
}

//private const val LAST_EDGE_NORMAL = 0
//private const val LAST_EDGE_SNAKE_OR_LADDER = 1

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
                intArrayOf(-1,-1),
                intArrayOf(-1,-1),
            )
        )
    )
}
