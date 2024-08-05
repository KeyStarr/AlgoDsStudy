package com.keystarr.algorithm.other.bitmanipulation

/**
 * ⭐️ what a beautiful bit manipulation problem so many approaches are applicable here
 * LC-461 https://leetcode.com/problems/hamming-distance/
 * difficulty: easy
 * constraints:
 *  • 0 <= x, y <= 2^31 - 1.
 *
 * Final notes:
 *  • a very straightforward problem for manual implementation, however there's a more efficient way with
 *   Integer.bitCount(x xor y), cause java's bitCount has significantly less const than [shiftBoth], but given the
 *   Int 32 bits asymptotically the same.
 *
 * Value gained:
 *  • practiced solving a problem efficiently using bit manipulation;
 *  • learned about Integer.bitCount and how elegantly efficient it is (just skimmed though).
 */
class HammingDistance {

    /**
     * fits into int
     *
     * until x == 0 && y == 0:
     *  - get the least significant bit from both and compare it, if different, increment the distance
     *  - shr both
     *
     * edge case:
     *  - one number is 0, and the other still has significant bits left => keep going until both are 0 (though technically
     *   we then could add simply the 1's count of that other number to the distance, but just keep with the algo for
     *   simplicity, which would essentially do the same)
     *
     * Time: O(k), where k=number of bits in max(x,y), here since both are Int => O(k=32) => O(1)
     * Space: O(1)
     */
    fun shiftBoth(x: Int, y: Int): Int {
        var xRemain = x
        var yRemain = y
        var distance = 0
        while (xRemain != 0 || yRemain != 0) {
            val xBit = xRemain and 1
            val yBit = yRemain and 1
            distance += if (xBit != yBit) 1 else 0
            xRemain = xRemain shr 1
            yRemain = yRemain shr 1
        }
        return distance
    }

    /**
     * 1. xor [x] and [y] to get the number with all significant bits where [x] and [y] were different;
     * 2. shr the xor result until its 0, count all 1's.
     *
     * asymptotically the complexities are same as [shiftBoth], but a time const should be slightly better, since we
     * perform a single xor and shr/and only half the time then [shiftBoth]. Plus it's much more concise and elegant!
     */
    fun shiftXor(x: Int, y: Int): Int {
        var bitDiff = x xor y
        var distance = 0
        while (bitDiff != 0) {
            distance += bitDiff and 1
            bitDiff = bitDiff shr 1
        }
        return distance
    }

    /**
     * 1. get the bit diff via [x] xor [y];
     * 2. use the efficient implementation of Integer.bitCount to count the bits of that.
     */
    fun builtIn(x: Int, y: Int): Int = Integer.bitCount(x xor y)
}

fun main() {
    println(HammingDistance().shiftXor(x = 1, y = 2))
}
