package com.keystarr.datastructure.deque.stack

/**
 * LC-155 https://leetcode.com/problems/min-stack/
 * difficulty: medium
 * constraints:
 *  • -2^31 <= val <= 2^31 -1;
 *  • pop,top,getMin will be called on non-empty stack;
 *  • at most 3*10^4 calls will be made to methods;
 *  • each public function must execute always in O(1) time;
 *  • no explicit space constraint.
 *
 * Stack=LIFO
 * Core idea: use a default java doubly linked list as a backbone
 *  - push => link to tail, O(1)
 *  - pop => unlink and return tail O(1) for doubly
 *  - peek => get and return tail O(1) for doubly
 */
class MinStack {

    // TODO: implement in the future
//    fun push(value: Int) {
//
//    }
//
//    fun pop() {
//
//    }
//
//    fun top(): Int {
//
//    }
//
//    fun getMin(): Int {
//
//    }
}
