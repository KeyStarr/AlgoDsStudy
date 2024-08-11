package com.keystarr.algorithm.deque.stack

/**
 * LC-946 https://leetcode.com/problems/validate-stack-sequences/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= pushed.length <= 1000;
 *  • 0 <= pushed\[i] <= 1000;
 *  • all elements in pushed are unique;
 *  • popped.length == pushed.length;
 *  • popped is a permutation of pushed.
 *
 * Final notes:
 *  • done [efficientFromPopped] by myself in 40 mins ⚠️⚠️. First problem of the day, maybe? Got entangled into conditions
 *   and dry-running a bit. Still, then I need to practice Stacks more!
 *  • 2nd time submit, failed 1 attempt due to a logic outofbounds array index error;
 *  • a cool problem, just a Stack simulation basically.
 *
 * Value gained:
 *  • practiced recognizing and solving a problem efficiently with a Stack.
 */
class ValidateStackSequences {

    // TODO: solve from a pushed perspective

    /**
     *
     * assume pushed sequence is valid
     * when is the pop sequence invalid?
     *  - trivial: when pop contains an element push doesn't have
     *  - order:
     *   elements which are pushed earlier can't be in pop
     * CHANGE DIRECTION
     * =>
     * assume pop sequence is valid
     * when is the pushed sequence invalid?
     *  - trivial: when push contains an element pop doesn't have
     *   e.g. if 3 is in pop and not in push => return false
     *  - order: when we have already added some elements X, Y, Z (in that order) to pop element Z, and then try to pop X
     *   i.o. when some intermediate element had to have been pushed to pop the next element after it, and we're asked
     *    to pop one of the elements pushed before it, without popping that very "blocker" element
     *
     * idea:
     *  - create a stack
     *  - iterate through popped
     *  - when encountering popped\[i]:
     *      - if this element is at the top of the current stack => just pop it() (pushed are guaranteed to be unique, there won't be any other with that value)
     *      - else
     *       - add all pushed items up to that element and mark them, starting from the last marked element
     *       - if that element isnt found at all => return false
     *  - return true
     *
     * Edge cases:
     *  - lastPushedAdded reached popped.size - 1, BUT remaining popped elements are valid since they were all already added
     *   (in reverse order to popped) into the stack => we just pop all of them out => fail when lastPushedAdded == popped.size - 1
     *   only when the current popped elements is not on top of the stack;
     *  - pushed.size == popped.size == 1 => init stack with the first element and set lastPushedAdded=0, cause otherwise
     *   we'd return false always.
     *
     *
     * pushed=[1,5,2] popped=[5,1,2]
     * lastPushedAdded=2
     * stack=[]
     * answer: true => correct
     *
     *
     * pushed=[1,5,2] popped=[5,2,1]
     * lastPushedAdded=-1
     * stack=[]
     *
     *
     * pushed=[1,5,2] popped=[2,1,5]
     * lastPushedAdded=-1
     * stack=[1,5,]
     * answer: false => correct
     *
     * Time: average/worst O(n)
     *  - main loop has worst n=pushed.size=popped.size iterations
     *  - inner loop, in sum across all main loop iterations, has worst n iterations (coz we only move forward through n elements)
     *  - stack push/pop are amortized O(1) for ArrayDeque
     * Space: average/worst O(n)
     *  - worst stack has all elements (when first pop is the last push element) => O(n)
     */
    fun efficientFromPopped(pushed: IntArray, popped: IntArray): Boolean {
        val stack = ArrayDeque<Int>().apply { addLast(pushed[0]) }
        var toPushFromInd = 1
        popped.forEach { poppedElement ->
            if (stack.isNotEmpty() && stack.last() == poppedElement) {
                stack.removeLast()
                return@forEach
            }

            if (toPushFromInd == popped.size) return false

            while (toPushFromInd < pushed.size) {
                val element = pushed[toPushFromInd]
                toPushFromInd++
                if (element != poppedElement) stack.addLast(element) else break
            }
        }

        return true
    }
}

fun main() {
    println(
        ValidateStackSequences().efficientFromPopped(
            pushed = intArrayOf(1, 2, 3, 4, 5),
            popped = intArrayOf(4, 5, 3, 2, 1),
        )
    )
}
