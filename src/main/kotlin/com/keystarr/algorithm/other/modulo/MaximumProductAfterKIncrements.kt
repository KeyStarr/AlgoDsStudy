package com.keystarr.algorithm.other.modulo

import java.util.PriorityQueue

/**
 * LC-2233 https://leetcode.com/problems/maximum-product-after-k-increments/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.size <= 10^5
 *  • 1 <= maxOperations <= 10^5
 *  • 0 <= nums\[i] <= 10^6
 *
 * Final notes:
 *  • at the first thought considered incrementing the max number to be the best strategy (after incrementing all 0's).
 *   Turns out that's wrong, the best choice is to repeatedly increment the minimum number (after each increment finding
 *   the next min). Learned that only after reading the solution, but sorta guessed it by myself after failing the 1st
 *   submit, without the proof though, intuitively (though at first thought about finding the min number once and incrementing
 *   it for all operations, and failed a submit too)) only then thought about repeatedly finding the min);
 *  • the hook I've placed earlier on "repeatedly finding the next min/max, especially with live collection modification => heap"
 *   worked surprisingly, as soon as I've seen the need for "the next min", the heap came to mind;
 *  • it's literal brain-candy how elegant the proof for why we choose the next min to increment is (basically just coz
 *   if we increment the Y which is less than x, then we repeat (Y+1) now X times, but if we increment X, we repeat (X+1) Y times
 *   and it all boils down to that extra factor of +X > +Y).
 *
 * Value gained:
 *  • surprisingly, practiced using the heap to repeatedly find the next min with live collection modifications;
 *  • practiced modular arithmetics to efficiently compute the modulo of a product;
 *  • learned about the proof of why, to maximize the product of some elements, we need to repeatedly increment the min element
 *   repeatedly.
 */
class MaximumProductAfterKIncrements {

    /**
     * goal: return the modulo of the maximum product of [nums] after incrementing some numbers for at most [maxOperations].
     *
     * which numbers to increment?
     *  - nums\[i] can == 0, then the entire product is 0 => we must always increment all 0's to at least 1;
     *  - if after that we still have operations left => choose the maximum number and increment it for all operations left.
     *
     * modulo loop invariant: `result equals the module of the sum of all nums elements from 0 to ith exclusive`
     *
     * edge case:
     *  - max product:
     *   10^5 numbers, each is 10^6, and 10^5 increment operations
     *   without the increment the product of nums is (10^6)^10^5 => well beyond Long
     *   we increment the min numbers repeatedly, so with increment it is ((10^6 + 1) * 10^5 + 10^6) ^ 10^5
     *   doesn't change much :) except that the modulo becomes funky instead of just a 0
     *   => compute the modulo of the sum by taking the result of, for each element:  `((previousElement % divisor) + currentElement) % divisor';
     *    since the divisor=INT.MAX, we can have a situation of result=10^6, minHeap.remove()=10^6
     *    => result = 10^6 * 10^6 => 10^12
     *    => we need to use Long for the intermediate result!
     *  - we've used all operations but there are still 0's => return 0.
     *
     * Time: O(nlogn)
     *  - heap construction from [nums] O(n)
     *  - increment O([maxOperations] * logn)
     *  - product loop O(n*logn)
     * Space: O(n) for the heap
     */
    fun efficient(nums: IntArray, maxOperations: Int): Int {
        val minHeap = PriorityQueue(nums.toList())
        repeat(maxOperations) { minHeap.add(minHeap.remove() + 1) }

        var result = 1L
        while (minHeap.isNotEmpty()) result = (result * minHeap.remove()) % MODULO
        return result.toInt()
    }
}

// to guarantee fit into int
private const val MODULO = 1_000_000_000 + 7
