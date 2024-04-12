package algos.recursion

// from grocking book, chapter 4 p.43
// constraints unspecified, so guessing averagely relaxed:
// * 0 <= numbers.size < 10^5
// * 0 <= numbers[i] < 10^9
// no time/space constraints, just work.
class SumOfIntArrayDivideAndConquer {

    // time O(n)
    // space O(1)
    fun iterative(numbers: IntArray): Long {
        var sum = 0L
        numbers.indices.forEach { ind -> sum += numbers[ind] }
        return sum
    }

    // time O(N)
    // space O(N) - terrible, cause we have to make N copies of the array with it's size reducing by 1 each time
    // base case - do we only have 1 element left? then it is the sum of the array (really simple).
    // recursive case - literally cut the first element from the array, sum it with the result of the recursive sum of the remaining elements
    fun recursiveTerrible(numbers: IntArray): Long =
        if (numbers.isEmpty()) 0 else numbers[0] + recursiveTerrible(numbers.copyOfRange(1, numbers.size))

    // time O(N)
    // space O(1)
    // don't copy the array, instead use a pointer
    fun recursiveClean(numbers: IntArray, startFrom: Int = 0): Long =
        if (startFrom == numbers.size) 0 else numbers[startFrom] + recursiveClean(numbers, startFrom + 1)
}

fun main() {
    println(SumOfIntArrayDivideAndConquer().recursiveClean(intArrayOf(1, 2, 3)))
}
