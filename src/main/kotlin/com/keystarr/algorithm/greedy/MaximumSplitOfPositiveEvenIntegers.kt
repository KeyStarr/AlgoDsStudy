package com.keystarr.algorithm.greedy

/**
 * ⭐️ wow, a sqrt efficient solution problem example
 * LC-2178 https://leetcode.com/problems/maximum-split-of-positive-even-integers/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= finalSum <= 10^10.
 *
 * Final notes:
 *  • ⚠️ tried to solve this problem at the end of the day, gave up super quickly after ~30 mins;
 *  • what I got right by myself:
 *   • if input is odd => always return empty list, since there is no sum of even numbers that adds up to it;
 *   • its no backtracking for sure, the solution must run close to O(logn) time since max input is 10^10;
 *    (technically not correct - 🔥its either square root, log or const time, so less than or equal to square root time)
 *   that's about it)))
 *  • what I got wrong:
 *   • ⚠️ first of all, read the problem wrong (or not, hard to tell now) and went for the "product" not the sum!;
 *   • tried to approach from the division by 2 perspective, then trying to combine all available 2's one by one
 *    into all the numbers we can come up with. Technically that's very much a valid approach, it would be more complex than
 *   [efficientCleaner] in terms of implementation, but it'd run with same complexities and a very close idea.
 *   ⚠️ but I failed to see it through and actually finish it, should've tried harder and seen until the end or 1h mark.
 *  • 🔥 in retrospect, greedy makes perfect sense here, since we try to maximize the amount of numbers => each step we make
 *   the locally optimal choice = take the smallest valid number available until we can't. Why didn't I see this angle to begin with,
 *   what should I learn to see such perspectives clearer? ❓
 *
 * Value gained:
 *  • practiced solving a "find and return maximum valid combination" type question using tricky greedy.
 */
class MaximumSplitOfPositiveEvenIntegers {

    // TODO: full resolve in 2-3 weeks, with full proof for the algorithm and the complexities estimation

    /**
     * We want to maximize the amount of EVEN DISTINCT numbers in the sum => try greedy?
     *
     * if [finalSum] is odd - there is no sum of even numbers that equals [finalSum] => return emptyList()
     *
     * try greedy: try to, each step, take the smallest even number available, since it will detract the least
     * from the [finalSum] => maximize our chance to get the maximum amount of numbers in the final sum
     *
     * when to stop?
     *  when (leftSum - currentNumber) < nextNumber
     *  i.o. if we take current number, we won't be able to text the nextNumber
     *
     * reason - since leftSum is less than next number, we won't be able to fulfill it since all even numbers less than nextNumber
     * are already taken (distinct constraint violation) => stop.
     * and since we check it every step, we will guaranteed stop when leftSum >= currentNumber and is even, so we can take it
     * as the last number (unless it was broken to begin with, that is, odd or less than currentNumber, which can't happen
     * here since we early return on odd and the least even number we can get as [finalSum] == 2).
     *
     *
     * (leftSum - currentNumber) < nextNumber
     *
     * leftSum = 12
     * currentNumber = 2
     *
     * 12 - 2= (10 >= 4) = 2+2
     * numbers=[2]
     * leftSum=12-2=10
     * currentNumber=2+2=4
     *
     * (10 - 4) = (6 >= 6) = 4 + 2
     * numbers=[2,4]
     * leftSum=10-4=6
     * currentNumber=4+2=6
     *
     * 6-6= (0 >= 8) =6+2
     * terminate
     *
     * leftSum=6!=0 => numbers=[2,4,6]
     * end, correct
     *
     * Time: always O(sqrt(finalSum)
     * Space: always O(1), if we don't count the result
     *
     *
     * 2 + 4 + .. + (2 * X) = finalSum
     * what is X?
     * 2 *(1 + 2 + .. + X) = finalSum
     * 2 * (X*(X-1)/2) = finalSum (just the arithmetic progression sum formula with the last number being X and a step of +1)
     * X*(X-1) = finalSum
     * X^2 - X = finalSum
     * X^2 ~= finalSum (approximate since we need big O, just the asymptotic upper boundary)
     * X ~= sqrt(finalSum)
     */
    fun efficientCleaner(finalSum: Long): List<Long> {
        if (finalSum % 2 == 1L) return emptyList()

        val numbers = mutableListOf<Long>()
        var leftSum = finalSum
        var currentNumber = 2L
        while (leftSum - currentNumber >= currentNumber + 2) {
            numbers.add(currentNumber)
            leftSum -= currentNumber
            currentNumber += 2
        }
        numbers.add(leftSum) // we guaranteed arrive here when leftSum > numbers.last()
        return numbers
    }

    /**
     * observe that we may use only UNIQUE EVEN integers to sum up to [finalSum]
     * => when is there no valid split then - what are the properties of [finalSum] in that case?
     *
     * if finalSum % 2 == 1 => return emptyList()
     *
     * else (finalSum is even) though what can we do?
     *  e.g. finalSum = 22, 22 / 2 = 11, cant
     *
     * 24
     * 12 * 2
     * 6 * 4
     *
     * 2 * 4 * 6 = 48
     *
     * 48
     * 24 * 2
     * 12 * 2 * 2
     * 6 * 2 * 2 * 2 = 6 * 2 * 4
     *
     * 22
     * 11 * 2 = empty list
     *
     * ------------------ WRONG, READ THE QUESTION PROPERLY!
     *
     * Not the product - the sum!
     *
     * well, still - if [finalSum] is odd, we cant split it into any number of even numbers sum => return empty list
     *
     * we could try log2(finalSum) to determine the closest power of two to the sum
     *
     * 22
     * log2(22) floor = 4
     * 2^4=16
     * 22-16=8
     * 2 4 6 10
     *
     * since max finalSum is 10^10 theres gotta be a near O(logn) solution
     *
     * 10 8 6 4 2
     *
     * well the straight forward solution one of the could be - subtract 2 from [finalSum] until there's none left,
     *
     * hm but what if we simply [finalSum] / 2 => thats how many twos we can get, then we just need to construct as many
     *  unique positive integers as possible that sum up to [finalSum] out of these 2's
     *
     * 10/2=5
     * 2 + 2*4
     * 2*2 + 2*3
     * 2 *3 + 2*2 tried already
     * 2 * 4 + 2 tried already
     *
     *
     * 48/2=24
     * => what's the maximum number of positive distinct numbers we can construct out of K=24 2's, such that we use every 2?
     *
     * its basically 1*2 + 2 * 2 + 3 * 3 + .. whats the max number we can use here such that the sum. Wait
     * could it be a progression?
     *
     * 2 * 4 * 8 = 64
     *
     * wait but we can use numbers that we may construct from the multiplication of odd and even numbers though
     * like 48 actually can be 22+16 = 22 + 14 + 2,
     *
     * --------------------------------------------------
     *
     *
     * gave up after 25-30 mins, was the last problem of the day and I didn't even see a thread leading to the solution
     * (but maybe just felt weak and didnt try hard enough) => read the solutions section, got the main idea of doing greedy
     * and designed&implemented the rest myself
     */
    fun efficient(finalSum: Long): List<Long> {
        if (finalSum % 2 == 1L) return emptyList()
        if (finalSum < 8) return listOf(finalSum)

        val components = mutableListOf<Long>()
        var currentComponent = 0L
        var currentSum = 0L
        while (currentSum + currentComponent + 2 <= finalSum) {
            currentComponent += 2
            currentSum += currentComponent
            components.add(currentComponent)
        }

        var remainder = finalSum - currentSum
        if (remainder > 0) {
            if (remainder <= components.last()) remainder += components.removeLast()
            components.add(remainder)
        }

        return components
    }
}

fun main() {
    println(
        MaximumSplitOfPositiveEvenIntegers().efficient(48)
    )
}
