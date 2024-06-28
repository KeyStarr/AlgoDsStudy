package com.keystarr.algorithm.greedy

/**
 * LC-881 https://leetcode.com/problems/boats•to•save•people/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= people.length <= 5 * 10^4
 *  • 1 <= people\[i] <= limit <= 3*10^4
 *
 * Final notes:
 *  • solved by myself via [solution] in ~30 mins;
 *  • the general approach to greedy problems worked flawlessly:
 *   • recognized that there might be an optimal greedy solution because the problem asked for the minimum, and cause
 *    there was a definition reminiscent of a single move;
 *   • asked: what could be an optimal step? Designed the solution around that. Assumed items were sorted
 *   => got the correct efficient solution.
 *
 * Value gained:
 *  • practiced recognizing the pattern and designing a greedy efficient algorithm.
 */
class BoatsToSavePeople {

    /**
     * Problem rephrase:
     *  - we are given an array of integers [numbers]
     *  - if 2 integers are less than or equal to [limit] then they make 1 combination. Each number is used exactly once.
     *   If there is no unused number X in the array such that numbers\[i] + X <= limit, than numbers\[i] alone
     *   constitutes 1 combination.
     * Goal: return the minimum number of combinations required such that each number is used exactly once.
     *
     * Minimum + problem statement resembles a single move definition => try greedy?
     *
     * Approaches:
     *
     * A. Find the max number AND find any number that fits max number out of the remaining such that they both add up to at most [limit].
     *  technically we could take the min, for the simplicity sake. If they don't fit, then max is combined alone.
     *
     *  why? because we increase the answer with each combination => we want to maximize the amount of numbers combined
     *  as a pair => max numbers are at risk of not fitting under the limit, so see them first. However, pair them with the
     *  max number available, so that
     *
     * Design:
     *  - sort numbers;
     *  - minInd=0, maxInd=numbers.size-1
     *  - combinationsCount = 0
     *  - while minInd < maxInd:
     *   - if (numbers\[maxInd] + numbers\[minInd] <= limit):
     *      - minInd++
     *   - maxInd--
     *   - combinationsCount++
     *  - if (minInd == maxInd) combinationsCount++
     *  - return combinationsCount
     *
     * Edge cases:
     *  - numbers\[i] == limit => always add exactly 1 combination;
     *  - numbers\[i] < limit but there is no matching numbers left => always add exactly 1 combination;
     *  - limit is always greater than or equal to the max number in [numbers] => we always can use any number
     *   at least alone in a single combination;
     *  - numbers.length == 1 => return 1 => correct as-is
     *  - if the loop terminates at minInd==maxInd => add 1 to combinationsCount after the loop, cause it means there
     *   was exactly one number left and we didn't use it in the loop.
     *   (either numbers.size%2==0 and there was an odd number of paired numbers
     *   or numbers.size%2==1 and there was an even number of paired numbers)
     *
     * Time: always O(nlogn)
     *  - sorting O(nlogn)
     *  - counting loop O(n)
     *  Space: if in-place sorting O(1) else O(n)
     */
    fun solution(numbers: IntArray, limit: Int): Int {
        numbers.sort()
        var minInd = 0
        var maxInd = numbers.size - 1
        var combinationsCount = 0
        while (minInd < maxInd) {
            if (numbers[minInd] + numbers[maxInd] <= limit) minInd++
            maxInd--
            combinationsCount++
        }
        // we actually don't need that, might do minInd <= maxInd, but this is easier to understand
        // (cause in case of minInd <= maxInd we'd try to check if we can pair the number with itself, which is meaningless)
        if (minInd == maxInd) combinationsCount++
        return combinationsCount
    }
}

fun main() {
    println(
        BoatsToSavePeople().solution(
            numbers = intArrayOf(3, 5, 3, 4),
            limit = 5,
        )
    )
}
