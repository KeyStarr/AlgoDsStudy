package com.keystarr.algorithm.deque.stack

import kotlin.collections.ArrayDeque

/**
 * LC-901 https://leetcode.com/problems/online-stock-span/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= price <= 10^5;
 *  • at most 10^4 calls.
 *
 * Final notes:
 *  • I completely failed to solve the problem by myself in 1h one day, and 0.5h the next day;
 *  • I misunderstood the problem requirement TWO TIMES:
 *      • first I MISSED the trick that the new price may actually be larger than N previous numbers, then another
 *          price may actually be smaller than it, and the next price might be larger than all previous numbers together!
 *          I CAN'T even STILL describe clearly exactly how that works, but at least I get it intuitively now.
 *          And I was actually 101% sure that the solution without the thing was absolutely the right one, though I felt
 *          it was weirdly simple for a medium;
 *      • then I caught that case, BUT MISSED the part that we only count consecutive intervals STARTING FROM TODAY!
 *          So naturally since I tried to find the longest interval, and considered intervals not starting from today,
 *          I failed to see how to apply the monotonic stack;
 *      FINALLY I gave up and read the editorial, and then I realized that I got the problem condition wrong.
 *
 * Value gained:
 *  • practiced not spending too much time on a problem I don't understand and move on. 1h one day, skipped it went to
 *      another topic => 0.5h the next day, read editorial and learned. Good;
 *  • learnt that monotonic stack's property to build a sequence of decreasing maximums with respect to order can be
 *      used to count intervals in between those maximums! or something.
 *
 * TODO: I still don't understand neither the statement nor the solution "fully", to explain it in simple format terms.
 *  Revisit later (at the end of the course?) and fix that.
 */
class OnlineStockSpan {

    private val stack = ArrayDeque<IntArray>()

    /**
     *
     * Problem rephrase:
     * "for each call return the number of consecutive calls starting from the current where [price] is less or equal to
     * the current call's [price]"
     *
     * Brute: save the [price] from each call into the array, on each call iterate backwards from the end of the array
     *  and count until we either exceed the start boundary of the array or find the number greater than current [price].
     *  Total time: average/worst O(n^2) where n = number of calls, cause the number of iterations each call would take
     *      is proportional on average to the number of calls.
     *  Total space: always O(n)
     *
     * How to improve time to O(n)?
     * => How to reduce the count of the span inside a single call from O(n) to O(1)?
     *
     * Intuition - "shrink" each interval of consecutive days less or equal to X into the data structure (price, intervalSize),
     *  where price=X.
     *
     *
     * save each [price] into the Stack along with its answer:
     *  - when current call's [price] < stack.peek()
     *   => it means that current day has no previous days with [price] less or equal to it =>
     *    save [price] and 1 into the stack, return 1;
     *  - otherwise we're on a streak and need to count all previous consecutive days starting from today where [price]
     *      was less or equal to today's price:
     *          - answer += stack.pop().answer, because automatically all days that were <= that number are also <= current [price];
     *          - do that until stack.isEmpty() or stack.peek() > [price], cause there might be many such intervals that
     *              RELATIVE TO EACH OTHER weren't valid, but RELATIVE TO CURRENT [price] are valid!
     *  - put current [price] with its answer into the stack;
     *  - return answer.
     *
     * Time: always O(n), where n = number of calls. We have inner while loop for popping, but since each [price] will
     *  be added to and popped from the stack at most once, so we might consider it "spread" across all iterations as O(1)
     *  for each iteration (push once, pop once for each number).
     * Space: worst/average O(n) cause the stack size will depend on intervals in the input data, so on average
     *  is simply depends on n.
     */
    fun next(price: Int): Int {
        var currentSpan = 1 // we always count today
        while (stack.isNotEmpty() && stack.last()[0] <= price) {
            val (_, span) = stack.removeLast()
            currentSpan += span
        }
        stack.addLast(intArrayOf(price, currentSpan))
        return currentSpan
    }
}

fun main() {
    OnlineStockSpan().apply {
        intArrayOf(100, 80, 60, 70, 60, 75, 85).forEach { println(next(it)) }
    }
}
