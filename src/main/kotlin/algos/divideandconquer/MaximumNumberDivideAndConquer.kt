package algos.divideandconquer

// from grocking book, chapter 4 p.44
// constraints unspecified, so guessing averagely relaxed:
// * 0 <= numbers.size < 10^5
// * 0 <= numbers[i] < 10^9
// * no time/space constraints
class MaximumNumberDivideAndConquer {

    fun recursive(numbers: IntArray, startFrom: Int = 0): Int? =
        if (startFrom == numbers.size) {
            null
        } else {
            val currentNumber = numbers[startFrom]
            val currentMax = recursive(numbers, startFrom + 1)
            if (currentMax == null || currentNumber > currentMax) currentNumber else currentMax
        }
}

fun main() {
    println(
        MaximumNumberDivideAndConquer().recursive(
            intArrayOf(99, 23, 999, 2343, 999999, 3432, 34234, 232, 1)
        )
    )
}
