package com.keystarr.algorithm.deque.stack.monotonic

/**
 * LC-1475 https://leetcode.com/problems/final-prices-with-a-special-discount-in-a-shop/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= prices.length <= 500;
 *  • 1 <= prices\[i] <= 1000.
 *
 * Final notes:
 *  • done [efficient] by myself in 12 mins;
 *  • to crack the problem, like in [com.keystarr.algorithm.dp.matrix.MaximalSquare], one needed to make a single CORE observation:
 *   "for each element we need to find the first element that is less or equal to it, or none";
 *  • I was able to arrive at that observation simply after abstracting from the problem's story, rephrased the problem abstractly
 *   => here we go. Then its basically a consequence of the definition of the monotonic strictly increasing stack, when we pop
 *   we find just that => recognized that thanks to prior practice, very much intuitively before I could even explain.
 *   Well yea, this next "min"/"max" tools practice is paying off.
 *
 * Value gained:
 *  • practiced recognizing and solving a problem efficiently using a monotonic strictly increasing stack.
 */
class FinalPricesWithASpecialDiscountInShop {

    /**
     * Problem rephrase:
     *  - given:
     *   - prices array of integers
     *  - item transformation rule:
     *   - prices\[i] = prices\[i] - discount, where discount is the first element after the ith index that is prices\[j] <= prices\[i]
     *    i.o. the final ith price is the ith price minus the first price after it less than or equal to it
     *  Goal: return the array of final prices for each item, considering the discount rule.
     *
     * CORE OBSERVATION: for each element we need to find the first element that is less or equal to it, or none
     * => monotonic strictly increasing stack
     *
     * when we pop from the stack => it means that for the popped element we've found the first price that is less than or equal to it
     * => we may calculate its answer
     * => for that we need the index of that element, store these in the stack.
     *
     * after the loop, if stack isn't empty - for every element left there's no discount, just write these as is to the answer.
     *
     * Edge cases:
     *  - prices.size == 1 => first while isnt entered, result is the price itself set in the 2nd while => correct
     *
     * Time: always O(n)
     * Space: average/worst O(n)
     *  worst is when [prices] is strictly increasing => stack size is exactly n at the end
     */
    fun efficient(prices: IntArray): IntArray {
        val indStack = mutableListOf<Int>() // strictly monotonically increasing
        val result = IntArray(size = prices.size)
        for (i in prices.indices) {
            val newPrice = prices[i]
            while (indStack.isNotEmpty() && prices[indStack.last()] >= newPrice) {
                val oldPriceInd = indStack.removeLast()
                result[oldPriceInd] = prices[oldPriceInd] - newPrice
            }
            indStack.add(i)
        }

        while (indStack.isNotEmpty()) {
            val oldPriceInd = indStack.removeLast()
            result[oldPriceInd] = prices[oldPriceInd]
        }

        return result
    }
}
