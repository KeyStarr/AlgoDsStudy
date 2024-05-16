package com.keystarr.algorithm.other

// Exercise 2.1.5 from CLRS.
/* Meta-thoughts
   I've implemented the correct algorithm both before coming-up with a formal loop invariant and proving algorithm's
   correction with it by induction. And before I've written unit tests (unless, ofc, I missed some cases).
   However, finding the loop invariant did turn my mind around and from it, I clearly deduced the edge case with bit
   transfer to arraySize+1. Feels cool and kinda useful for posterior confidence?

   Why did I rigorously unit-test? Felt right, but took quite some effort (like, 40 mins?). In a way, to verify my version
   of the loop invariant. On the other hand, just to be sure - my over-commitment again?..
 */
class IntBinaryArraySumCLRS {

    /**
     * Constraints:
     * - [a] and [b] are binary representations of integers, both of size [arraySize];
     * - and a[i] and b[i] is either 0 or 1;
     * - "integers" but neither overflow behavior nor [arraySize] limits are unspecified, assume 0 <= [arraySize] < 16
     * - whether there might be leading zeros is unspecified, assume that might happen only if either [a] or [b]
     *  first element is 1 and the other's is 0 (cause of [arraySize]), exclude other cases for simplicity.
     *
     *  Loop invariant (based on book's namings):
     *      1. each c[k] in c[n+1:i+1]=(a[t]+b[t])%1 including transfer 1s, where t=k-1, for i < n.
     *      2. c[0] is a transfer bit from (a[1] + b[1] + transfer);
     *  - init: special case i = n (first iteration) excluded, first item is checked later, correct;
     *  - maintenance: i+1 accounts to go back to the previous iteration's result, correct;
     *  - termination:
     *      - i=0 => so the invariant p.1 is true for c[n+1:1] and a[n:0], b[n:0];
     *      - c[0] (actually the last bit of the sum) is accounted for by condition p.2;
     *      => thus, c[0:n+1] is the result of binary sum of a + b.
     *
     * @return sum = a + b, in a binary form, via array of size: [arraySize] + 1 (due to possible bit transfer),
     *  in case of
     */
    fun naive(a: IntArray, b: IntArray, arraySize: Int): IntArray {
        if (a.size != arraySize || b.size != arraySize) throw IllegalArgumentException("a and b size must match arraySize")
        if (arraySize > INT_BIT_COUNT_PREVENT_OVERFLOW) throw IllegalArgumentException("arraySize must be in 1..15 range")

        val sum = IntArray(size = a.size + 1)
        var buff = 0
        for (i in (a.size - 1) downTo 0) {
            buff += a[i] + b[i]
            if (buff < 2) {
                sum[i + 1] = buff
                buff = 0
            } else {
                sum[i + 1] = buff - 2
                buff = 1
            }
        }
        if (buff == 1) sum[0] = 1

        return sum
    }

    /**
     * Things I got wrong above and fixed here:
     * 1. CRITICAL: the task clearly states that lower bits are at the start of the array, higher are at the end (left-to-right) - I missed it and
     *  thought *assumed* the order would be just as the binary representation is written (right-to-left);
     * 2. NORMAL:
     */
    fun naiveFixedAfterCheckingTheAnswer(a: IntArray, b: IntArray, arraySize: Int): IntArray {
        if (a.size != arraySize || b.size != arraySize) throw IllegalArgumentException("a and b size must match arraySize")
        if (arraySize > INT_BIT_COUNT_PREVENT_OVERFLOW) throw IllegalArgumentException("arraySize must be in 1..15 range")

        val sum = IntArray(size = a.size + 1)
        var buff = 0
        for (i in 0 until arraySize) {
            buff += a[i] + b[i]
            sum[i] = buff % 2
            buff = if (buff > 1) 1 else 0
        }
        sum[arraySize] = buff

        return sum
    }
}

private const val INT_BIT_COUNT_PREVENT_OVERFLOW = 15

fun main() {
    println(
        IntBinaryArraySumCLRS().naiveFixedAfterCheckingTheAnswer(
            a = intArrayOf(1),
            b = intArrayOf(1),
            arraySize = 1,
        ).contentToString()
    )
}
