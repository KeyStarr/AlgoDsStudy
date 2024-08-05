package com.keystarr.algorithm.graph.backtracking

/**
 * ⭐️ LC-52 https://leetcode.com/problems/n-queens-ii/description/
 * constraints:
 *  • 1 <= n <= 9.
 *
 * Final notes:
 *  • decided to jump into the solution since it was in the course's theory, didn't attempt reasonably to solve by myself first;
 *  • wow, the trick with diagonals is awesome;
 *  • and yet again, backtracking (based on a decision tree, really) helps to implement a reasonable solution to a super
 *   hard (at first at least) combinations generation problem.
 *
 * Value gained:
 *  • learnt a great trick on how to determine via a O(1) whether 2 elements in a matrix lie on the same diagonal/anti-diagonal;
 *  • learnt of a solution to a "classic" problem of placing n queens on nxn board.
 */
class NQueensII {

    /**
     * goal: return the number of distinct solutions (distinct boards with n correctly placed queens)
     * - a queen always takes up the entire row and the column
     * - tricky case: how to efficiently check which diagonals are already taken?
     *
     * we have to place exactly n queens + 1 row has at most 1 queen => we consider only variants which place 1 queen at each row.
     *
     * generation => try backtracking:
     *  - pruning: for the next move call [backtrack] only on cells no queen attacks:
     *      - the cell's row isnt taken
     *      - the cell's column isnt taken
     *      - the cell's lies on no taken diagonal
     *  - backtrack:
     *   - forward: add the queen placement to taken row+column+diagonals
     *   - backtrack: remove the current queen's placement from row+column+diagonals
     *  - how to add to answer?
     *   - when exactly [n] queens were placed. keep track of queen placement counter? or just rows.size
     *
     * how to check if cell's diagonal isn't taken?
     *  => keep track of both taken diagonals and anti-diagonals:
     *   - a diagonal both ways has the same diff (row-column), cause both row and column are either decremented or incremented by the same value
     *      (+1, +1 => diff never changes)
     *   - an anti-diagonal both ways has the same (row+column), cause row gets incremented and column decremented (+1 -1 => sum never changes)
     *  some cells have both cells on a diagonal and anti-diagonal, and both these have different diffs
     *  => create sets for both the diagonal and anti-diagonal!!! store (row-column) diff in each => check if the current cell
     *  is on any diagonal already taken by taking its (row-column) and checking in both sets.
     *
     * go through rows! place all queens possible on the current row, respecting previously placed queens.
     *
     * Edge cases:
     *  - n == 1 => 1, correct as-is
     *  - n == 2 => 0, correct
     *
     * Time: O(n!) TODO: prove, why is true time/space estimation unknown? why is approximate n! exactly?
     * Space:
     */
    fun efficient(n: Int): Int = backtrack(
        n = n,
        currentRow = -1,
        currentColumn = 0,
        columnsAttacked = mutableSetOf(),
        diagonalsAttacked = mutableSetOf(),
        antiDiagonalsAttacked = mutableSetOf(),
    )

    private fun backtrack(
        n: Int,
        currentRow: Int,
        currentColumn: Int,
        columnsAttacked: MutableSet<Int>,
        diagonalsAttacked: MutableSet<Int>,
        antiDiagonalsAttacked: MutableSet<Int>,
    ): Int {
        val diagonalDiff = currentRow - currentColumn
        val antiDiagonalSum = currentRow + currentColumn
        if (columnsAttacked.contains(currentColumn)
            || diagonalsAttacked.contains(diagonalDiff)
            || antiDiagonalsAttacked.contains(antiDiagonalSum)
        ) return 0 // that cell is attacked => can't place a queen there

        // current cell is unoccupied => we placed the queen there, and we placed 1 queen on each previous row guaranteed
        if (currentRow == n - 1) return 1

        if (currentRow != -1) {
            columnsAttacked.add(currentColumn)
            diagonalsAttacked.add(diagonalDiff)
            antiDiagonalsAttacked.add(antiDiagonalSum)
        }

        var totalSolutions = 0
        val newRow = currentRow + 1
        for (newColumn in 0 until n) {
            totalSolutions += backtrack(
                n = n,
                currentRow = newRow,
                currentColumn = newColumn,
                columnsAttacked = columnsAttacked,
                diagonalsAttacked = diagonalsAttacked,
                antiDiagonalsAttacked = antiDiagonalsAttacked,
            )
        }

        columnsAttacked.remove(currentColumn)
        diagonalsAttacked.remove(diagonalDiff)
        antiDiagonalsAttacked.remove(antiDiagonalSum)

        return totalSolutions
    }

    /**
     * we have at most 9 columns => can utilize a single Int with bitwise operations to keep track of `seen` nodes
     * => improve time const and space const (but space is O(n) anyway cause of the callstack)
     *
     * - we have columns in the range of [0,9];
     * - diagonals => we store via a diff, and depending on whether we choose row-column or column-row it can go negative,
     *  with a total range of (0,9)=-9 to (9,0)=9, so [-9,9] => we can add +9 to the ith bit, so -9 is the 0th bit and 9 is the 18th bit;
     * - anti-diagonals => we store the sum, spanning from (0,0)=0 to (9,9)=18, so [0,18].
     *
     * TL;DR; same complexities as [efficient], but better both time and space const.
     */
    fun efficientBitwise(n: Int): Int = backtrackWithBitwise(
        n = n,
        currentRow = -1,
        currentColumn = 0,
        columnsAttacked = 0,
        diagonalsAttacked = 0,
        antiDiagonalsAttacked = 0,
    )

    private fun backtrackWithBitwise(
        n: Int,
        currentRow: Int,
        currentColumn: Int,
        columnsAttacked: Int,
        diagonalsAttacked: Int,
        antiDiagonalsAttacked: Int,
    ): Int {
        val diagonalDiff = currentRow - currentColumn
        val antiDiagonalSum = currentRow + currentColumn
        if (columnsAttacked and (1 shl currentColumn) != 0
            || diagonalsAttacked and (1 shl diagonalDiff + 9) != 0
            || antiDiagonalsAttacked and (1 shl antiDiagonalSum) != 0
        ) return 0 // that cell is attacked => can't place a queen there

        // current cell is unoccupied => we placed the queen there, and we placed 1 queen on each previous row guaranteed
        if (currentRow == n - 1) return 1

        var newColumnsAttacked = columnsAttacked
        var newDiagonalsAttacked = diagonalsAttacked
        var newAntiDiagonalsAttacked = antiDiagonalsAttacked
        if (currentRow != -1) {
            newColumnsAttacked = columnsAttacked.xor(1 shl currentColumn)
            newDiagonalsAttacked = diagonalsAttacked.xor(1 shl (diagonalDiff + 9))
            newAntiDiagonalsAttacked = antiDiagonalsAttacked.xor(1 shl antiDiagonalSum)
        }

        var totalSolutions = 0
        val newRow = currentRow + 1
        for (newColumn in 0 until n) {
            totalSolutions += backtrackWithBitwise(
                n = n,
                currentRow = newRow,
                currentColumn = newColumn,
                columnsAttacked = newColumnsAttacked,
                diagonalsAttacked = newDiagonalsAttacked,
                antiDiagonalsAttacked = newAntiDiagonalsAttacked,
            )
        }

        return totalSolutions
    }
}

fun main() {
    println(NQueensII().efficient(n = 10))
}
