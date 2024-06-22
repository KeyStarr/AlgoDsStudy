package com.keystarr.algorithm.graph.implicit

import java.util.ArrayDeque
import java.util.Queue

/**
 * LC-752 https://leetcode.com/problems/open-the-lock/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= deadends.length <= 500
 *  • deadends\[i].length == 4
 *  • target.length == 4, target is not in deadends
 *  • target and deadends\[i] consist of digits only
 *
 * Final notes:
 *  • first attempted to model each separate wheel as a connected component
 *   of an undirected graph and quickly got stuck. The actual solution is SO MUCH BETTER, read it in the DSA course;
 *  • evaluated time/space wrong at first! Used the default for BFS: time average/worst O(n+e); space average/worst O(n) for seen
 *      but in fact here n can be expressed for precisely as 10^k, and also it is a const here! and also we must take into
 *      account the deadends conversion to set.
 *
 * Value gained:
 *  • solved my first problem on implicit graphs!!!!!
 *  • figure out - what are the best practice for modelling implicit graph problems? As I have written above, here I thought
 *   that it'd make sense to model each wheel as a connected component. Is the combined state always or more frequently
 *   better as a node? If the answer is a combined state =>> model nodes as combined states???
 *  • evaluate time/space ALWAYS IN THE CONTEXT, PHENOMENOLOGICALLY for the specific problem! Like, here BFS is lifted
 *   out of the big O time complexity being a const, in favor of, oh, the actual `deadends` to set conversion :D
 */
class OpenTheLock {

    /**
     * States + valid transitions + the fewest amount of operations => a Graph with BFS?
     *
     * Problem rephrase:
     *  - we are given an undirected graph with 10^4 nodes;
     *  - node=the state of the lock (collective, of all 4 wheels at once) e.g. 0000, edges=allowed transitions from
     *   current lock state to other states => by turning one wheel either forward or backwards. E.g. 0000 has undirected
     *   edges to 0001, 0010, 0100, 1000, 0009, 0090, 0900, 9000 (each state then has 8 edges: 4 wheels, 2 moves on any = 8)
     * Goal: return the amount of edges along the shortest path from "0000" to [target] node without using any [deadends] nodes.
     *
     * Shortest path => BFS. Blocked nodes => prefill `seen` with them.
     * How to compute all possible nodes from the given node: String, e.g. "0000"?
     *  idea 1: repeat(4) { ind ->
     *      forwardDigit = (node[i].toInt() + 1) % 10
     *      backwardDigit = if (node[i] == '0') 9 else node[i].toInt() - 1
     *      forwardNode = node.replace(i, forwardDigit)
     *      backwardNode = node.replace(i, backwardDigit)
     *      if !seen.contains(backwardDigit) {
     *          queue.add(backwardDigit)
     *          seen.add(backwardDigit)
     *      }
     *      if !seen.contains(forwardDigit) {
     *          queue.add(forwardDigit)
     *          seen.add(forwardDigit)
     *      }
     *  }
     *
     * Edge cases:
     *  - starting point (0000) is in [deadends] => return -1
     *  - target==start node => return 0
     *
     * Time: O(10^k * k^2 + 8*10^k + m), here k always 4 and m varies => O(10^4*4^2 + 8*10^4 + m) => O(m)
     *  - we visit average/worst 10^k nodes, where k=amount of elements in the lock (here - const 4)
     *  - for each node we perform k^2 work, cause we for each character of the state (k chars) we construct a new string,
     *   which takes k time;
     *  - on average/worst we also try 8*10^k edges;
     *  - also we convert the [deadends] into a set which takes m=deadends.length time
     * Space: O(10^k + m) = O(m)
     *  - seen set takes on average/worst 10^k + m.
     */
    fun efficient(deadends: Array<String>, target: String): Int {
        if (target == START_NODE) return 0

        val queue: Queue<String> = ArrayDeque<String>().apply { add(START_NODE) }
        val seen = HashSet<String>().apply {
            deadends.forEach(::add)
            if (contains(START_NODE)) return -1
            add(START_NODE)
        }

        var currentDistance = 0
        while (queue.isNotEmpty()) {
            val currentDistanceNodesSize = queue.size
            repeat(currentDistanceNodesSize) {
                val node = queue.remove()
                repeat(4) { charToReplaceInd ->
                    repeat(2) { isBackward ->
                        val digit = node[charToReplaceInd].digitToInt()
                        val newDigit = if (isBackward == 1) (10 + (digit - 1)) % 10 else (digit + 1) % 10
                        val newNode = node.replaceRange(charToReplaceInd, charToReplaceInd + 1, newDigit.toString())
                        if (newNode == target) return ++currentDistance
                        if (!seen.contains(newNode)) {
                            queue.add(newNode)
                            seen.add(newNode)
                        }
                    }
                }
            }
            currentDistance++
        }
        return -1
    }
}

private const val START_NODE = "0000"

fun main() {
    println(
        OpenTheLock().efficient(
            deadends = arrayOf("0201", "0101", "0102", "1212", "2002"),
            target = "0202",
        )
    )
}
