package com.keystarr.algorithm.search

class LinearSearchFromCLRS {

    /**
     * Loop invariant: at the start of each iteration numbers[0:i-1] does not contain a number equal to x
     * (interpret numbers[0:-1] as an empty subarray);
     *  - init: empty subarray, correct;
     *  - maintenance: correct before each subsequent iteration;
     *  - termination:
     *      - if the element equal to x is found, the loop is interrupted and the correct index is returned;
     *      - otherwise when i=numbers.size the loop is terminated and that always means that numbers[0:numbers.size-1]
     *          (the entire array) does not contain an element equal to x. Thus, `null` is returned.
     */
    operator fun invoke(numbers: IntArray, x: Int): Int? {
        for (i in numbers.indices) if (numbers[i] == x) return i
        return null
    }
}

fun main() {
    LinearSearchFromCLRS()(
        numbers = intArrayOf(3, 2, 5, 1, 4),
        x = 1,
    )
}
