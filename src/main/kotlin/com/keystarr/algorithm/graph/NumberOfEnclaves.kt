package com.keystarr.algorithm.graph

import java.util.ArrayDeque
import java.util.Queue

/**
 * LC-1020 https://leetcode.com/problems/number-of-enclaves/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= grid.size, grid[0].size <= 500;
 *  • grid\[i]\[j] is either 0 or 1.
 *
 * Final notes:
 *  • 🎉🎉 got the core idea pretty fast, in, like, 5 mins: to traverse from the boundaries (thinking from the opposite
 *   of what is asked, quite naturally though, didn't "feel" opposite to me, cause boundaries are kinda put on the front);
 *    => multi-start BFS;
 *  • done [efficient] by myself in 30 mins;
 *  • given was a graph in a form of matrix, where cells are nodes and edges are direct boundaries.
 *
 * Value gained:
 *  • practiced recognizing and solving a problem efficiently with multi-start BFS;
 *  • practiced designing the solution from the opposite of what is asked. The problem asked for the number of isolated ones,
 *   but we calculate the number of connected ones and subtract it from the total to get the isolated ones count.
 */
class NumberOfEnclaves {

    private val directions = arrayOf(
        intArrayOf(0, 1), // right
        intArrayOf(0, -1), // left
        intArrayOf(1, 0), // down
        intArrayOf(-1, 0), // up
    )

    // TODO: try DFS for practice

    /**
     * problem rephrase:
     *  - given:
     *   - a matrix, where if matrix\[i]\[j] == 1, there's a valid path through that cell, otherwise there's no path;
     *   - valid move: go from current cell to a directly adjacent cell with value 1, or out of the grid boundary;
     *  goal: return the total number LAND CELLS in the "islands" => components of the matrix such that from each there's no valid path
     *   to the boundary of the matrix.
     *
     * idea: we could try finding all cells with '1' reachable from the boundaries of the matrix and subtract that from the
     *  total number of '1's.
     *  => either do DFS/BFS from all cells on the boundaries of the matrix which equal to 1.
     *
     * do BFS for efficiency (no callstack).
     *
     * Edge cases:
     *  - n <= 2 or m <= 2 then just always return the total number of ones (all land cells present are at the boundaries of the matrix).
     *
     * Time: always O(n*m)
     *  - count total number of 1s => O(n*m)
     *  - counting traversal: worst all matrix cells are 1s => we reach each once => O(n*m)
     * Space:
     */
    fun efficient(grid: Array<IntArray>): Int {
        val m = grid.size
        val n = grid[0].size

        var totalOnes = 0
        grid.forEach { row ->
            row.forEach { cell -> if (cell == 1) totalOnes++ }
        }
        if (n <= 2 || m <= 2) return 0

        val queue: Queue<Cell> = ArrayDeque()
        val seen = Array(size = m) { BooleanArray(size = n) }
        var nonIslandLandCells = 0

        for (row in grid.indices) {
            for (column in grid[0].indices) {
                if ((row == 0 || column == 0 || row == m - 1 || column == n - 1) && grid[row][column] == 1) {
                    queue.add(Cell(row = row, column = column))
                    seen[row][column] = true
                    nonIslandLandCells++
                }
            }
        }

//        messier (DRY), but slightly more efficient
//        val boundaryRows = listOf(0, m - 1)
//        boundaryRows.forEach { row ->
//            for (column in 0 until n) {
//                if (grid[row][column] == 1) {
//                    queue.add(Cell(row = row, column = column))
//                    seen[row][column] = true
//                    nonIslandLandCells++
//                }
//            }
//        }
//
//        val boundaryColumns = listOf(0, n - 1)
//        boundaryColumns.forEach { column ->
//            for (row in 1 until m - 1) {
//                if (grid[row][column] == 1) {
//                    queue.add(Cell(row, column))
//                    seen[row][column] = true
//                    nonIslandLandCells++
//                }
//            }
//        }

        while (queue.isNotEmpty()) {
            val cell = queue.remove()
            directions.forEach { direction ->
                val newRow = cell.row + direction[0]
                val newColumn = cell.column + direction[1]
                if ((newRow !in grid.indices || newColumn !in grid[0].indices)
                    || seen[newRow][newColumn]
                    || grid[newRow][newColumn] == 0
                ) return@forEach
                queue.add(Cell(newRow, newColumn))
                seen[newRow][newColumn] = true
                nonIslandLandCells++
            }
        }

        return totalOnes - nonIslandLandCells
    }

    private class Cell(val row: Int, val column: Int)
}

fun main() {
    println(
        NumberOfEnclaves().efficient(
            grid = arrayOf(
                intArrayOf(0, 1, 1, 0),
                intArrayOf(0, 0, 1, 0),
                intArrayOf(0, 0, 1, 0),
                intArrayOf(0, 0, 0, 0)
            )
        )
    )
}
