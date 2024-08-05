package com.keystarr.algorithm.other.bitmanipulation

/**
 * LC-461 https://leetcode.com/problems/hamming-distance/
 * difficulty: easy
 * constraints:
 *  • 0 <= x, y <= 2^31 - 1.
 *
 * Final notes:
 *  • a very straightforward problem for manual implementation, however there's a more efficient way with
 *   Integer.bitCount(x xor y), cause java's bitCount has significantly less const than [efficient], but given the
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
    fun efficient(x: Int, y: Int): Int {
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
}

fun main() {
    println(HammingDistance().efficient(x = 8, y = 3))
}
