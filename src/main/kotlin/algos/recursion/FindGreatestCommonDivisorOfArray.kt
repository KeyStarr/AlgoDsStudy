package algos.recursion

/*
 1979 https://leetcode.com/problems/find-greatest-common-divisor-of-array/description/
 * easy
 * 2 <= nums.length <= 10^3
 * 1 <= nums[i] <= 10^3
 * no explicit time nor memory constraints
 * => O(n), even O(n^2) should do, with simple int (4 bytes) type
 *
 * divide and conquer!!
*/
class FindGreatestCommonDivisorOfArray {

    // TODO: estimate findGcd
    // time: O(n) + O(n) + ???, quite difficult to comprehend just now, smth about the golden ration
    // space: ???
    fun solve(nums: IntArray): Int {
        val min = nums.findMin()
        val max = nums.findMax()
        return findGcd(min, max)
    }

    private fun IntArray.findMin(): Int {
        var min = Int.MAX_VALUE
        forEach { if (it < min) min = it }
        return min
    }

    private fun IntArray.findMax(): Int {
        var max = Int.MIN_VALUE
        forEach { if (it > max) max = it }
        return max
    }

    fun solveShorter(nums: IntArray): Int {
        val min = nums.minOf { it }
        val max = nums.maxOf { it }
        return findGcd(min, max)
    }

    /*
    * Intuition: GCD(A,B) = GCD(B,R), where R = A - B * Q
    *
    * Developed probably even before Euclid's time, that's wild.
    * Great explanation of the proof here: https://www.khanacademy.org/computing/computer-science/cryptography/modarithmetic/a/the-euclidean-algorithm
     */
    private fun findGcd(lesser: Int, greater: Int): Int {
        val r = greater % lesser
        return if (r == 0) {
            lesser
        } else {
            findGcd(
                lesser = r,
                greater = lesser,
            )
        }
    }
}

fun main() {
    println(
        FindGreatestCommonDivisorOfArray().solve(
            nums = intArrayOf(43, 99),
        )
    )
}
