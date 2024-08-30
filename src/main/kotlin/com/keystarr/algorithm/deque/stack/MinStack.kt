package com.keystarr.algorithm.deque.stack

/**
 * LC-155 https://leetcode.com/problems/min-stack/description/
 * difficulty: medium
 * constraints:
 *  • -2^31 <= value in [push] <= 2^31 - 1;
 *  • [pop], [top] and [getMin] will always be called on non-empty stack;
 *
 * goal: implement all methods with O(1) time.
 *
 * Final notes:
 *  • done efficient by myself in 25 mins;
 *  • realized the solution after dry running and writing down exactly which values we were supposed to return
 *   => it clicked just as I was writing the minimums / changing the minimum to be returned along with the number from the
 *   main stack. Even though I didn't see a solution straight away and even went to try out monotonic stacks :D
 *   🔥 all it took is dry run to see how a result is collected! (even with no algo in place, but by doing, so I've seen
 *    the algo, by doing these steps myself)
 *
 * Value gained:
 *  • practiced solving a data structure type problem by cleverly utilizing a secondary stack.
 */
class MinStack {

    private val mainStack = mutableListOf<Int>()
    private val minsStack = mutableListOf<MinStackEntry>()

    fun push(value: Int) {
        mainStack.add(value)
        if (minsStack.isEmpty() || value < minsStack.last().value) {
            minsStack.add(MinStackEntry(value = value, mainStackInd = mainStack.size - 1))
        }
    }

    /**
     * pop is guaranteed to be called on non empty [mainStack] => if mainStack.size == 1, then minsStack is always .size==1
     */
    fun pop(): Int {
        if (minsStack.last().mainStackInd == mainStack.lastIndex) minsStack.removeLast()
        return mainStack.removeLast()
    }

    fun top(): Int = mainStack.last()

    /**
     * if we called only [push], then [getMin] could simply return the minimum value across those that were pushed
     *  (which we could update on each push call)
     *
     * tricky: what if we [pop] before [getMin]? what's the next min value if we popped the current min value?
     *
     * incorrect trivial approaches:
     *  - keep all pushed values in a dynamic array sorted ascending => on each push insert value to maintain the sorted order
     *   => due to insert-in-the-middle average/worst time of push would be O(n), getMin O(1);
     *
     * => how to repeatedly get next min value in O(1) time in a live-modified collection?
     *  (that we can only remove/add the last element from!)
     *
     * push = 3 4 1 2          non increasing stack = 4 1    non decreasing stack = 1 2
     * getMin() = 1
     * pop()
     * getMin() = 1
     * pop()
     * getMin() = 3
     *
     * key here is that we can only remove from/add to the end => what pattern follows from it for finding the next min?
     *
     * note: we have time constraint, but no explicit space constraints => may try using multiple collections
     *
     * => repeatedly getting min => monotonic non-increasing stack? to which we save numbers with their indices
     *  => remove from the monotonic stack only when we [pop] the corresponding index (and we check always only the last element
     *  cause the order of elements in it would match the order of elements in the stack, all values in it are guaranteed
     *  to exist in the stack => if we are to remove from the monotonic stack, it would be only when we remove from the main stack
     *  => only from the end)
     *
     * is the first element of [mainStack] always present in [minsStack]? in what case that may be false?
     * 5
     *
     * check 2 monotonic stacks
     *
     * push = 10 3 5 1      non-increasing stack = 10 5 1    non-decreasing stack = 1
     *                      desired order (in order of popping) 1 3 10 => 10 3 1
     *
     * ----------------------------
     *
     * => AH, as we update the minimum, DO NOT remove it, simply push it into the stack with an index!
     */
    fun getMin(): Int = minsStack.last().value

    private class MinStackEntry(
        val value: Int,
        val mainStackInd: Int,
    )
}
