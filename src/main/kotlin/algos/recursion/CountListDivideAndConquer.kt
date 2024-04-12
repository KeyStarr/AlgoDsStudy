package algos.recursion

// from grocking book, chapter 4 p.44
// constraints unspecified, so guessing averagely relaxed:
// * 0 <= numbers.size < 10^5
// * 0 <= numbers[i] < 10^9
// no time/space constraints, just work.
class CountListDivideAndConquer {

    // time O(n)
    // space O(1)
    fun recursive(numbers: IntArray, startFrom: Int = 0): Int =
        if (startFrom == numbers.size) 0 else 1 + recursive(numbers, startFrom + 1)
}

fun main() {
    println(CountListDivideAndConquer().recursive(intArrayOf(1, 2, 3)))
}
