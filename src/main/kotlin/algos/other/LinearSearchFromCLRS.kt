package algos.other

class LinearSearchFromCLRS {

    /**
     * Loop invariant: starting from i=1, numbers[0:i-1] does not contain a number equal to x;
     *  - init: skip the i=0, correct and does not leave out first array element (it would be checked on i=1);
     *  - maintenance: correct for each iteration;
     *  - termination:
     *      - if the element equal to x is found, the loop is interrupted and `i` is returned;
     *      - otherwise when i=numbers.size the loop is terminated and that always means that numbers[0:numbers.size-1]
     *          does not contain an element equal to x. Thus, `null` is returned.
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
