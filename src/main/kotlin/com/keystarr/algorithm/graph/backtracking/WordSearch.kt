package com.keystarr.algorithm.graph.backtracking

/**
 * LC-79 https://leetcode.com/problems/word-search/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= board.length, board\[i].length <= 15
 *  • 1 <= word.length <= 15
 *  • board and word consists only of lowercase and uppercase English letters
 *
 * Final notes:
 *  • done [efficient] by myself
 *  • cool, same backtracking pattern even though here we have a graph (traversal is still a decision tree);
 *  • unusual => we launch DFS from every node of the graph individually.
 *
 * Value gained:
 *  • practiced backtracking on a graph represented as a matrix;
 *  • unusual, learnt that it might be necessary to launch dfs from each graph node individually.
 */
class WordSearch {

    private val directions = arrayOf(
        intArrayOf(0, -1), // left
        intArrayOf(0, 1), // right
        intArrayOf(-1, 0), // down
        intArrayOf(1, 0), // up
    )

    /**
     * goal: find if the word (represented by a number of consecutive cells) exists in the board.
     *
     * we can model each board's cell as a node and edges would be direct neighbours (sharing the direct border)
     * => traverse through the board taking only directions with promising candidates, i.o. when the next node's value
     * matches the expected next letter in the word.
     * => try DFS with backtracking to build the word:
     *  pruning = taking only the edges which lead to next valid letters
     *  result = when the amount of nodes taken equals the word size return true and just propagate it as the answer
     *
     * matrix is m x n (not square)
     *
     * use the seen 2D Array<BooleanArray> to avoid visiting already visited nodes, since one node can only be used once
     * to construct the word.
     *
     * traverse through the entire board, call [backtrack] on every cell
     *
     * Edge cases:
     *  - m*n < word.length e.g. m=1, n=1 (one letter in the board) and word.length=10 => always return false,
     *   could do an early return for O(1), but it would work correctly
     *
     * Time: O(n*m*3^word.length)
     *  think of it as a decision tree
     *  - we launch the backtracking from each cell, so worst it's n*m starts (if each letter in the board == word[0])
     *  - each original backtracking call at worst can have up to 3^word.length traversals, since each node has on average
     *   3 neighbors and, in worst case, each neighbor == word\[i] => we can visit up to 3^word.length nodes if all letters
     *   in all directions match (think, all board\[i] are the same letter, and [word] consists of multiple same letter)
     * Space: O(n*m + word.length)
     *  - worst callstack is word.length exactly
     *  - seen takes n*m space
     */
    fun efficient(board: Array<CharArray>, word: String): Boolean {
        val m = board.size
        val n = board[0].size
        if (m * n < word.length) return false

        val seen = Array(size = m) { BooleanArray(n) }
        for (i in board.indices) {
            for (j in board[0].indices) {
                seen[i][j] = true
                val wasFound = backtrack(
                    row = i,
                    column = j,
                    letterInd = 0,
                    board = board,
                    word = word,
                    seen = seen,
                )
                if (wasFound) return true
                seen[i][j] = false
            }
        }
        return false
    }

    private fun backtrack(
        row: Int,
        column: Int,
        letterInd: Int,
        board: Array<CharArray>,
        word: String,
        seen: Array<BooleanArray>,
    ): Boolean {
        if (board[row][column] != word[letterInd]) return false
        if (letterInd == word.length - 1) return true // the letter fits, and it was the last one

        directions.forEach { direction ->
            val newRow = row + direction[0]
            val newColumn = column + direction[1]
            if ((newRow !in board.indices || newColumn !in board[0].indices)
                || seen[newRow][newColumn]
            ) return@forEach

            seen[newRow][newColumn] = true
            val wasFound = backtrack(
                row = newRow,
                column = newColumn,
                letterInd = letterInd + 1,
                board = board,
                word = word,
                seen = seen,
            )
            if (wasFound) return true
            seen[newRow][newColumn] = false
        }
        return false
    }
}

fun main() {
    println(
        WordSearch().efficient(
            board = arrayOf(
                charArrayOf('A', 'B', 'C', 'E'),
                charArrayOf('S', 'F', 'C', 'S'),
                charArrayOf('A', 'D', 'E', 'E'),
            ),
            word = "SEE",
        )
    )
}
